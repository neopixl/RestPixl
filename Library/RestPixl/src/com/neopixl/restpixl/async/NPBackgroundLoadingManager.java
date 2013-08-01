package com.neopixl.restpixl.async;

import java.util.ArrayList;

import com.neopixl.restpixl.request.NPRequest;

public class NPBackgroundLoadingManager {

	public static void purgeRequests() {
		NPAsynchTask.THREAD_POOL_EXECUTOR.purge();
	}
	
	
	
    @SuppressWarnings("rawtypes")
	public static void cancelDuplicateJob(NPRequest request) {
    	
    	ArrayList<NPRequest> cancelledRequest = new ArrayList<NPRequest>();
    	String currentUrl = request.prepareRequestUrlToSend();
    	for(Object o : NPAsynchTask.sWorkQueue) {
    		NPNewAsyncTask.ComparableFutureTask t = (NPNewAsyncTask.ComparableFutureTask)o;
    		if (t.task instanceof NPRequest) {
    			NPRequest r = (NPRequest)t.task;
    			if (r != request) {
    				if(r != null){
    					String resolvedUrl = r.prepareRequestUrlToSend();
    					
    					if (currentUrl.equals(resolvedUrl)) {
        					cancelledRequest.add(r);
        				}
    				}
    				
    			}
    		}
    	}
    	
    	for (NPRequest r : cancelledRequest) {
    		r.cancel(false);
    	}
    }

	public static void queueRequest(NPRequest request) {
		if (request != null) {
			request.setPriority(NPRequestPriority.REQ_BACKGROUND_PRIORITY);
			request.executeOnExecutor(NPAsynchTask.THREAD_POOL_EXECUTOR);
		}
	}
	
	public static void queueLowRequest(NPRequest request) {
		if (request != null) {
			request.setPriority(NPRequestPriority.REQ_LOW_BACKGROUND_PRIORITY);
			request.executeOnExecutor(NPAsynchTask.THREAD_POOL_EXECUTOR);
		}
	}

	public static void queuePriorityRequest(NPRequest request) {
		if (request != null) {
			request.setPriority(NPRequestPriority.REQ_BOOT_PRIORITY);
			request.executeOnExecutor(NPAsynchTask.THREAD_POOL_EXECUTOR);
		}
	}

	public static void queueInteractiveRequest(NPRequest request) {
		if (request != null) {
			request.setPriority(NPRequestPriority.REQ_INTERACTIVE_PRIORITY);
			request.executeOnExecutor(NPAsynchTask.THREAD_POOL_EXECUTOR);
		}
	}

}
