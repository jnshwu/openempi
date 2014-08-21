/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */
package org.openhealthtools.openpixpdq.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.openhealthtools.openpixpdq.api.IPixUpdateNotificationRequest;


/**
 * The consumer class to consume the {@link IPixUpdateNotificationRequest}
 * in the blocking queue. The producer of requests is the PixFeedHandler which
 * generates {@link IPixUpdateNotificationRequest}.
 * 
 * @author Wenzhi Li
 * @version 1.0, Feb 14, 2009
 * @see IPixUpdateNotificationRequest
 */
public class PixUpdateNotifier extends Thread {
	private static PixUpdateNotifier instance = null;

	/* The blocking queue to hold all the PixUpdateNotification requests */
	private final BlockingQueue<IPixUpdateNotificationRequest> queue =
	      new LinkedBlockingQueue<IPixUpdateNotificationRequest>();

	//private constructor
	private PixUpdateNotifier() {}
	
	/**
	 * Gets the singleton class. 
	 */
	public static synchronized PixUpdateNotifier getInstance() {
		if (instance == null) {
			instance = new PixUpdateNotifier();
			instance.start();
		}
		return instance;	
	}
	
	/**
	 * Accepts a {@link IPixUpdateNotificationRequest} from a producer.
	 * 
	 * @param pixUpdateNotificationRequest
	 */
	public void accept(IPixUpdateNotificationRequest pixUpdateNotificationRequest) {
		while (true) {
			try {  
		    	queue.put(pixUpdateNotificationRequest);
		    	return;
	        }catch (InterruptedException e) {
	        }
		}
	}

	@Override
    public void run() {
       while (true) {
    	   try {
	            execute(queue.take());
           }catch (InterruptedException e) {
           }
       }
   }

   /**
    * Executes a {@link IPixUpdateNotificationRequest}
    * 
    * @param pixUpdateNotificationRequest the request to execute
    */
   private void execute(final IPixUpdateNotificationRequest pixUpdateNotificationRequest) {
      new Thread(new Runnable() {
         public void run() {
            pixUpdateNotificationRequest.execute();
         }
      }).start();
   }

}
