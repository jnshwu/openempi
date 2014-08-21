package org.openhealthtools.openpixpdq.common;

import java.io.IOException;

public interface IHL7Channel<M>
{
	public M sendMessage(M message) throws IOException;
	
	public M sendMessage(M message, boolean keepOpen) throws IOException;
	
	public void close();
	
	public boolean isOpen();	
}
