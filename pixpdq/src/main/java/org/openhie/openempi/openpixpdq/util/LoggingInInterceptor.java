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

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedWriter;
import org.apache.cxf.io.DelegatingInputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.ws.addressing.ContextUtils;
import org.jfree.util.Log;
import org.openhealthtools.openpixpdq.api.MessageStore;



/**
 * A simple logging handler which outputs the bytes of the message to the
 * Logger.
 */
@NoJSR250Annotations
public class LoggingInInterceptor extends AbstractLoggingInterceptor {
    private static final Logger LOG = LogUtils.getLogger(LoggingInInterceptor.class);
    
    public LoggingInInterceptor() {
        super(Phase.RECEIVE);
    }
    
    public LoggingInInterceptor(String phase) {
        super(phase);
    }

    public LoggingInInterceptor(String id, String phase) {
        super(id, phase);
    }

    public LoggingInInterceptor(int lim) {
        this();
        limit = lim;
    }
    public LoggingInInterceptor(String id, int lim) {
        this(id, Phase.RECEIVE);
        limit = lim;
    }

    public LoggingInInterceptor(PrintWriter w) {
        this();
        this.writer = w;
    }
    public LoggingInInterceptor(String id, PrintWriter w) {
        this(id, Phase.RECEIVE);
        this.writer = w;
    }
    
    public void handleMessage(Message message) throws Fault {
        Logger logger = getMessageLogger(message);
        if (writer != null || logger.isLoggable(Level.INFO)) {
            logging(logger, message);
        }
    }

    protected void logging(Logger logger, Message message) throws Fault {
        if (message.containsKey(LoggingMessage.ID_KEY)) {
            return;
        }
        String id = (String)message.getExchange().get(LoggingMessage.ID_KEY);
        if (id == null) {
            id = LoggingMessage.nextId();
            message.getExchange().put(LoggingMessage.ID_KEY, id);
        }
        message.put(LoggingMessage.ID_KEY, id);
        final LoggingMessage buffer 
            = new LoggingMessage("Inbound Message\n----------------------------", id);

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
        String ct = (String)message.get(Message.CONTENT_TYPE);
        if (ct != null) {
            buffer.getContentType().append(ct);
        }
        Object headers = message.get(Message.PROTOCOL_HEADERS);

        if (headers != null) {
            buffer.getHeader().append(headers);
        }
        String uri = (String)message.get(Message.REQUEST_URL);
        if (uri != null) {
            buffer.getAddress().append(uri);
            String query = (String)message.get(Message.QUERY_STRING);
            if (query != null) {
                buffer.getAddress().append("?").append(query);
            }
        }
        
        if (!isShowBinaryContent() && isBinaryContent(ct)) {
            buffer.getMessage().append(BINARY_CONTENT_MESSAGE).append('\n');
            log(logger, buffer.toString());
            return;
        }
        
        InputStream is = message.getContent(InputStream.class);
        if (is != null) {
            CachedOutputStream bos = new CachedOutputStream();
            if (threshold > 0) {
                bos.setThreshold(threshold);
            }
            try {
                // use the appropriate input stream and restore it later
                InputStream bis = is instanceof DelegatingInputStream 
                    ? ((DelegatingInputStream)is).getInputStream() : is;
                
                IOUtils.copyAndCloseInput(bis, bos);
                bos.flush();
                bis = bos.getInputStream();
                
                // restore the delegating input stream or the input stream
                if (is instanceof DelegatingInputStream) {
                    ((DelegatingInputStream)is).setInputStream(bis);
                } else {
                    message.setContent(InputStream.class, bis);
                }

                if (bos.getTempFile() != null) {
                    //large thing on disk...
                    buffer.getMessage().append("\nMessage (saved to tmp file):\n");
                    buffer.getMessage().append("Filename: " + bos.getTempFile().getAbsolutePath() + "\n");
                }
                if (bos.size() > limit) {
                    buffer.getMessage().append("(message truncated to " + limit + " bytes)\n");
                }
                writePayload(buffer.getPayload(), bos, encoding, ct); 
                    
                bos.close();
            } catch (Exception e) {
                throw new Fault(e);
            }
        } else {
            Reader reader = message.getContent(Reader.class);
            if (reader != null) {
                try {
                    CachedWriter writer = new CachedWriter();
                    IOUtils.copyAndCloseInput(reader, writer);
                    message.setContent(Reader.class, writer.getReader());
                    
                    if (writer.getTempFile() != null) {
                        //large thing on disk...
                        buffer.getMessage().append("\nMessage (saved to tmp file):\n");
                        buffer.getMessage().append("Filename: " + writer.getTempFile().getAbsolutePath() + "\n");
                    }
                    if (writer.size() > limit) {
                        buffer.getMessage().append("(message truncated to " + limit + " bytes)\n");
                    }
                    writer.writeCacheTo(buffer.getPayload(), limit);
                } catch (Exception e) {
                    throw new Fault(e);
                }
            }
        }
        String formattedMessage = formatLoggingMessage(buffer);
        processPixPdqMessage(message, formattedMessage);
        log(logger, formattedMessage);
    }
	
    protected String extractOperation(Message message) {
    	Object headers = message.get(Message.PROTOCOL_HEADERS);
        if (headers != null && (headers instanceof Map)) {
        	Map<String,Object> headerMap = (Map<String,Object>) headers;
            Log.debug("Headers are: " + headers);
            String contentTypeKey = hasContentTypeKey(headerMap.keySet());
            if (contentTypeKey != null) {
	            Object contentType = headerMap.get(contentTypeKey);
	            Log.debug("Content type is " + contentType);
	            List<String> types = (List<String>) contentType;
	            for (String type : types) {
	            	if (type.indexOf("action") >= 0) {
	            		String actionPortion = type.substring(type.indexOf("action"));
	            		String[] parts = actionPortion.split("\"");
	            		return parts[1];
	            	}
	            }
            }
        }
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

    private String hasContentTypeKey(Set<String> keySet) {
    	for (String key : keySet) {
    		if (key.toLowerCase().equals("content-type")) {
    			return key;
    		}
    	}
		return null;
	}

	protected void processPixPdqMessage(Message message, String formattedMessage) {
    	MessageStore messageStore = getMessageStoreFromMessage(message);
    	if (messageStore == null) {
    		// If the message store is not there in an incoming message then
    		// that means that the message is flowing from the incoming port
    		// to the outgoing port. We will log the message on the way out.
    		messageStore = new MessageStore();
    		messageStore.setMessageDate(new java.util.Date());
    		messageStore.setId((String) message.getExchange().get(LoggingMessage.ID_KEY));
    		messageStore.setInMessage(formattedMessage);
    		messageStore.setTriggerEvent(extractOperation(message));
    		message.put(PIXPDQ_MESSAGE_STORE, messageStore);
    	} else {
    		messageStore.setInMessage(formattedMessage);
    		logMessage(messageStore);
    	}
	}

	protected String formatLoggingMessage(LoggingMessage loggingMessage) {

        return loggingMessage.toString();
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }
}
