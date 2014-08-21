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
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.ws.addressing.ContextUtils;
import org.openhealthtools.openpixpdq.api.IMessageStoreLogger;
import org.openhealthtools.openpixpdq.api.MessageStore;
import org.openhealthtools.openpixpdq.common.PixPdqFactory;

public class CustomLoggingOutInterceptor extends LoggingOutInterceptor
{
    private static final String LOG_SETUP = LoggingOutInterceptor.class.getName() + ".log-setup";
    
	private IMessageStoreLogger messageLogger;
	
	public CustomLoggingOutInterceptor() {
        addAfter(LoggingOutInterceptor.class.getName());
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		Message incoming = message.getExchange().getInMessage();
		String inOperation = "NONE";
		if (incoming != null) {
			inOperation = extractOperation(incoming);
		}
		Message outgoing = message;
		String outOperation = extractOperation(outgoing);
		MessageStore messageLog = new MessageStore();
		messageLog.setId((String)message.getExchange().get(LoggingMessage.ID_KEY));
		
		StringBuffer msgAsString = new StringBuffer();
        getMessageAsString(incoming, msgAsString);
		messageLog.setInMessage(msgAsString.toString());
		
		msgAsString = new StringBuffer();
        getMessageAsString(outgoing, msgAsString);		
		messageLog.setOutMessage(msgAsString.toString());
		
		messageLog.setMessageDate(new Date());
		messageLog.setTriggerEvent((inOperation != null) ? inOperation : outOperation);
		IMessageStoreLogger messageLogger = getMessageStoreLogger();
		if (messageLogger != null) {
			messageLogger.saveLog(messageLog);
		}
	}

	private void getMessageAsString(Message message, StringBuffer msgAsString) {
		final OutputStream os = message.getContent(OutputStream.class);
        final Writer iowriter = message.getContent(Writer.class);
        if (os == null && iowriter == null) {
        	msgAsString.append("<EMPTY>");
            return;
        }
        String theMsg = message.toString();
        // Write the output while caching it for the log message
        @SuppressWarnings("unused")
		boolean hasLogged = message.containsKey(LOG_SETUP);
//        if (!hasLogged) {
            message.put(LOG_SETUP, Boolean.TRUE);
            if (os != null) {
                final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
                if (threshold > 0) {
                    newOut.setThreshold(threshold);
                }
                message.setContent(OutputStream.class, newOut);
                newOut.registerCallback(new LoggingCallback(msgAsString, message, os));
            } else {
                message.setContent(Writer.class, new LogWriter(msgAsString, message, iowriter));
            }
//        }
	}

	
    private String extractOperation(Message message) {
    	BindingOperationInfo bindingOpInfo =
                message.getExchange().get(BindingOperationInfo.class);
        String action = null;
    	if (bindingOpInfo != null) {
    		if (bindingOpInfo.isUnwrappedCapable()) {
    			bindingOpInfo = bindingOpInfo.getUnwrappedOperation();
    		}
    		action = (String)message.get(ContextUtils.ACTION);
            if (StringUtils.isEmpty(action)) {
            	action = (String) message.get(SoapBindingConstants.SOAP_ACTION);
            }
            if (action == null || "".equals(action)) {
            	MessageInfo msgInfo = 
            			ContextUtils.isRequestor(message)
            			? bindingOpInfo.getOperationInfo().getInput()
            					: bindingOpInfo.getOperationInfo().getOutput();
            	action = (String) msgInfo.getProperty(ContextUtils.ACTION);
            }
    	}
       return action;
	}

	@SuppressWarnings("unused")
	private LoggingMessage setupBuffer(Message message, String direction) {
        final LoggingMessage buffer;
        String id = (String)message.getExchange().get(LoggingMessage.ID_KEY);
        if (id == null) {
            id = LoggingMessage.nextId();
            message.getExchange().put(LoggingMessage.ID_KEY, id);
        }
        buffer = new LoggingMessage(direction + "\n---------------------------", id);
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
    
    private class LogWriter extends FilterWriter {
        StringWriter out2;
        int count;
        StringBuffer theLog;
        Message message;
        
        public LogWriter(StringBuffer theLog, Message message, Writer writer) {
            super(writer);
            this.theLog = theLog;
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
            theLog.append(buffer.toString());
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
        private final StringBuffer theLog;
        
        public LoggingCallback(final StringBuffer theLog, final Message msg, final OutputStream os) {
            this.theLog = theLog;
            this.message = msg;
            this.origStream = os;
        }

        public void onFlush(CachedOutputStream cos) {  
            
        }
        
        public void onClose(CachedOutputStream cos) {
            LoggingMessage buffer = setupBuffer(message);

            String ct = (String) message.get(Message.CONTENT_TYPE);
            if (!isShowBinaryContent() && isBinaryContent(ct)) {
                buffer.getMessage().append(BINARY_CONTENT_MESSAGE).append('\n');
                theLog.append(formatLoggingMessage(buffer));
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

            theLog.append(formatLoggingMessage(buffer));
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

	private IMessageStoreLogger getMessageStoreLogger() {
		if (messageLogger == null) {
			messageLogger = PixPdqFactory.getMessageStoreLogger();
		}
		return messageLogger;
	}
}
