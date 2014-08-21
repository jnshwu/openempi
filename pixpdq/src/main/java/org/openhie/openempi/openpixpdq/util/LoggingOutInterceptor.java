/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openhie.openempi.openpixpdq.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.openhealthtools.openpixpdq.api.MessageStore;

/**
 * 
 */
@NoJSR250Annotations
public class LoggingOutInterceptor extends AbstractLoggingInterceptor {
    private static final Logger LOG = LogUtils.getLogger(LoggingOutInterceptor.class);
    private static final String LOG_SETUP = LoggingOutInterceptor.class.getName() + ".log-setup";
    
    public LoggingOutInterceptor(String phase) {
        super(phase);
        addBefore(StaxOutInterceptor.class.getName());
    }
    public LoggingOutInterceptor() {
        this(Phase.PRE_STREAM);
    }    
    public LoggingOutInterceptor(int lim) {
        this();
        limit = lim;
    }

    public LoggingOutInterceptor(PrintWriter w) {
        this();
        this.writer = w;
    }    

    public void handleMessage(Message message) throws Fault {
        final OutputStream os = message.getContent(OutputStream.class);
        final Writer iowriter = message.getContent(Writer.class);
        if (os == null && iowriter == null) {
            return;
        }
        Logger logger = getMessageLogger(message);
        if (logger.isLoggable(Level.INFO) || writer != null) {
            // Write the output while caching it for the log message
            boolean hasLogged = message.containsKey(LOG_SETUP);
            if (!hasLogged) {
                message.put(LOG_SETUP, Boolean.TRUE);
                if (os != null) {
                    final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
                    if (threshold > 0) {
                        newOut.setThreshold(threshold);
                    }
                    message.setContent(OutputStream.class, newOut);
                    newOut.registerCallback(new LoggingCallback(logger, message, os));
                } else {
                    message.setContent(Writer.class, new LogWriter(logger, message, iowriter));
                }
            }
        }
    }
    
    private LoggingMessage setupBuffer(Message message) {
        String id = (String)message.getExchange().get(LoggingMessage.ID_KEY);
        if (id == null) {
            id = LoggingMessage.nextId();
            message.getExchange().put(LoggingMessage.ID_KEY, id);
        }
        final LoggingMessage buffer 
            = new LoggingMessage("Outbound Message\n---------------------------",
                                 id);
        
        Integer responseCode = (Integer)message.get(Message.RESPONSE_CODE);
        if (responseCode != null) {
            buffer.getResponseCode().append(responseCode);
        }
        
        String encoding = (String)message.get(Message.ENCODING);
        if (encoding != null) {
            buffer.getEncoding().append(encoding);
        }            
        String httpMethod = (String)message.get(Message.HTTP_REQUEST_METHOD);
        if (httpMethod != null) {
            buffer.getHttpMethod().append(httpMethod);
        }
        String address = (String)message.get(Message.ENDPOINT_ADDRESS);
        if (address != null) {
            buffer.getAddress().append(address);
        }
        String ct = (String)message.get(Message.CONTENT_TYPE);
        if (ct != null) {
            buffer.getContentType().append(ct);
        }
        Object headers = message.get(Message.PROTOCOL_HEADERS);
        if (headers != null) {
            buffer.getHeader().append(headers);
        }
        return buffer;
    }

    protected void processPixPdqMessage(Message message, String formattedMessage) {
    	MessageStore messageStore = getMessageStoreFromMessage(message);
    	if (messageStore == null) {
    		// If the message store is not there in an outgoing message then
    		// that means that the message is flowing from the outgoing port
    		// to the incoming port. We will log the message on the way back
    		// in.
    		messageStore = new MessageStore();
    		messageStore.setMessageDate(new java.util.Date());
    		messageStore.setId((String) message.getExchange().get(LoggingMessage.ID_KEY));
    		messageStore.setInMessage(formattedMessage);
    		messageStore.setTriggerEvent(extractOperation(message));
    		message.put(PIXPDQ_MESSAGE_STORE, messageStore);
    	} else {
    		messageStore.setOutMessage(formattedMessage);
    		if (messageStore.getTriggerEvent() == null) {
    			messageStore.setTriggerEvent(extractOperation(message));
    		}
    		logMessage(messageStore);
    	}
	}
    
    private class LogWriter extends FilterWriter {
        StringWriter out2;
        int count;
        Logger logger; //NOPMD
        Message message;
        
        public LogWriter(Logger logger, Message message, Writer writer) {
            super(writer);
            this.logger = logger;
            this.message = message;
            if (!(writer instanceof StringWriter)) {
                out2 = new StringWriter();
            }
        }
        public void write(int c) throws IOException {
            super.write(c);
            if (out2 != null && count < limit) {
                out2.write(c);
            }
            count++;
        }
        public void write(char[] cbuf, int off, int len) throws IOException {
            super.write(cbuf, off, len);
            if (out2 != null && count < limit) {
                out2.write(cbuf, off, len);
            }
            count += len;
        }
        public void write(String str, int off, int len) throws IOException {
            super.write(str, off, len);
            if (out2 != null && count < limit) {
                out2.write(str, off, len);
            }
            count += len;
        }
        public void close() throws IOException {
            LoggingMessage buffer = setupBuffer(message);
            if (count >= limit) {
                buffer.getMessage().append("(message truncated to " + limit + " bytes)\n");
            }
            StringWriter w2 = out2;
            if (w2 == null) {
                w2 = (StringWriter)out;
            }
            String ct = (String)message.get(Message.CONTENT_TYPE);
            try {
                writePayload(buffer.getPayload(), w2, ct); 
            } catch (Exception ex) {
                //ignore
            }
            log(logger, buffer.toString());
            processPixPdqMessage(message, buffer.toString());
            message.setContent(Writer.class, out);
            super.close();
        }
    }

    protected String formatLoggingMessage(LoggingMessage buffer) {
        return buffer.toString();
    }

    class LoggingCallback implements CachedOutputStreamCallback {
        
        private final Message message;
        private final OutputStream origStream;
        private final Logger logger; //NOPMD
        
        public LoggingCallback(final Logger logger, final Message msg, final OutputStream os) {
            this.logger = logger;
            this.message = msg;
            this.origStream = os;
        }

        public void onFlush(CachedOutputStream cos) {  
            
        }
        
        public void onClose(CachedOutputStream cos) {
            LoggingMessage buffer = setupBuffer(message);

            String ct = (String)message.get(Message.CONTENT_TYPE);
            if (!isShowBinaryContent() && isBinaryContent(ct)) {
                buffer.getMessage().append(BINARY_CONTENT_MESSAGE).append('\n');
                String formattedMessage = formatLoggingMessage(buffer);
                processPixPdqMessage(message, formattedMessage);
                log(logger, formattedMessage);
                return;
            }
            
            if (cos.getTempFile() == null) {
                //buffer.append("Outbound Message:\n");
                if (cos.size() > limit) {
                    buffer.getMessage().append("(message truncated to " + limit + " bytes)\n");
                }
            } else {
                buffer.getMessage().append("Outbound Message (saved to tmp file):\n");
                buffer.getMessage().append("Filename: " + cos.getTempFile().getAbsolutePath() + "\n");
                if (cos.size() > limit) {
                    buffer.getMessage().append("(message truncated to " + limit + " bytes)\n");
                }
            }
            try {
                String encoding = (String)message.get(Message.ENCODING);
                writePayload(buffer.getPayload(), cos, encoding, ct); 
            } catch (Exception ex) {
                //ignore
            }

            String formattedMessage = formatLoggingMessage(buffer);
            processPixPdqMessage(message, formattedMessage);
            log(logger, formattedMessage);
            try {
                //empty out the cache
                cos.lockOutputStream();
                cos.resetOut(null, false);
            } catch (Exception ex) {
                //ignore
            }
            message.setContent(OutputStream.class, 
                               origStream);
        }
    }

    @Override
    protected Logger getLogger() {
        return LOG;
        
    }

}
