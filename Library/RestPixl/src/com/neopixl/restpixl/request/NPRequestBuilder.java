package com.neopixl.restpixl.request;

import android.util.Log;

/**
 * NPRequestBuilder
 * @author Olivier Demolliens
 * Easy request builder
 */

public class NPRequestBuilder {

	/**
	 * <br>rebuildGETRequest</br>
	 * <br>Rebuild finished <b>GET</b> request</br>
	 * @param request old <b>NPGETRequest</b> 
	 * @param delegate Activity callback
	 * @return new <b>GET</b> request with parameter from old request
	 */
	public static NPGETRequest rebuildGETRequest(NPGETRequest request, NPRequestListener delegate)
	{
		return (NPGETRequest)rebuildRequest(request,delegate);
	}

	/**
	 * <br>rebuildPOSTRequest</br>
	 * <br>Rebuild finished <b>POST</b> request</br>
	 * @param request old <b>NPPOSTRequest</b> 
	 * @param delegate Activity callback
	 * @return new <b>POST</b> request with parameter from old request
	 */
	public static NPPOSTRequest rebuildPOSTRequest(NPPOSTRequest request, NPRequestListener delegate)
	{
		return (NPPOSTRequest)rebuildRequest(request,delegate);
	}

	/**
	 * <br>rebuildPUTRequest</br>
	 * <br>Rebuild finished <b>PUT</b> request</br>
	 * @param request old <b>NPPUTRequest</b> 
	 * @param delegate Activity callback
	 * @return new <b>PUT</b> request with parameter from old request
	 */
	public static NPPUTRequest rebuildPUTRequest(NPPUTRequest request, NPRequestListener delegate)
	{
		return (NPPUTRequest)rebuildRequest(request,delegate);
	}

	/**
	 * <br>rebuildDELETERequest</br>
	 * <br>Rebuild finished <b>DELETE</b> request</br>
	 * @param request old <b>NPDELETERequest</b> 
	 * @param delegate Activity callback
	 * @return new <b>DELETE</b> request with parameter from old request
	 */
	public static NPDELETERequest rebuildDELETERequest(NPDELETERequest request, NPRequestListener delegate)
	{
		return (NPDELETERequest)rebuildRequest(request,delegate);
	}

	/**
	 * <br>Request Builder</br>
	 * @param request old <b>NPRequest</b> 
	 * @param delegate Activity callback
	 * @return  new request with parameter from old request
	 */
	
	private static NPRequest rebuildRequest(NPRequest request, NPRequestListener delegate)
	{
		NPRequest newRequest = null;

		if(request instanceof NPGETRequest){
			newRequest = NPGETRequest.rebuildNPGETRequestWith((NPGETRequest) request, delegate);
		}else if(request instanceof NPPOSTRequest){
			newRequest = NPPOSTRequest.rebuildNPPOSTRequestWith((NPPOSTRequest) request, delegate);
		}else if(request instanceof NPPUTRequest){
			newRequest = NPPUTRequest.rebuildNPPUTERequestWith((NPPUTRequest) request, delegate);
		}else if(request instanceof NPDELETERequest){
			newRequest = NPDELETERequest.rebuildNPDELETERequestWith((NPDELETERequest) request, delegate);
		}else{
			if(request instanceof NPRequest){
				Log.e("RestPixl","NPRequest can't used directly (used child)");
			}else{
				Log.e("RestPixl","unrecognized object:"+request.getClass().getName());
			}
		}

		return newRequest;
	}
}
