package com.neopixl.restpixl.request;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.neopixl.restpixl.NPRequestEnum;
import com.neopixl.restpixl.async.NPParamResolver;

/**
 * NPDELETERequest
 * @author Olivier Demolliens - Neopixl
 * Easy DELETE request
 */

public class NPDELETERequest extends NPRequest{

	/**
	 * <br>NPDELETERequest</br>
	 * <br>Used with <b><em>DELETE</em></b> request</br>
	 * <br>Constructor with <b>no content data</b></br>
	 * @param <b>context</b> Activity (callbacks)
	 * @param <b>url</b> Url service
	 * @param <b>resolvers</b> Parameter(s) in URL
	 * @param <b>encode</b> encode url with FORMAT paramater in {@link Conf}
	 * @param <b>user</b> auth user
	 * @param <b>pwd</b> auth password
	 * @param <b>headers</b> requests header(s)
	 */
	public NPDELETERequest(Context context, String url,
			List<NPParamResolver> resolvers, boolean encode, String user,
			String pwd, HashMap<String, String> headers) {
		super(context, url, resolvers, encode, user, pwd, headers, null,NPRequestEnum.DELETE);
	}

	/**
	 * <br>NPDELETERequest</br>
	 * <br>Used with <b><em>DELETE</em></b> request</br>
	 * <br>Constructor with <b>content data</b></br>
	 * @param <b>context</b> Activity (callbacks)
	 * @param <b>url</b> Url service
	 * @param <b>resolvers</b> Parameter(s) in URL
	 * @param <b>encode</b> encode url with FORMAT paramater in {@link Conf}
	 * @param <b>user</b> auth user
	 * @param <b>pwd</b> auth password
	 * @param <b>data</b> request data (json for example)
	 * @param <b>headers</b> requests header(s)
	 */
	public NPDELETERequest(Context context, String url,
			List<NPParamResolver> resolvers, boolean encode, String user,
			String pwd,String data, HashMap<String, String> headers) {
		super(context, url, resolvers, encode, user, pwd, headers, data,NPRequestEnum.DELETE);
	}

	/**
	 * <br>rebuildNPDELETERequestWith(NPDELETERequest request, NPRequestDelegate delegate)</br>
	 * <br>Use to rebuild NPDELETERequest object</br>
	 * @param <b>request</b> already launched
	 * @param <b>delegate</b> activity callback
	 * @return new NPDELETERequest with parameter from other NPDELETERequest
	 */
	public static NPDELETERequest rebuildNPDELETERequestWith(NPDELETERequest request, NPRequestListener delegate){

		NPDELETERequest newRequest = null;

		if(request.haveData()){
			newRequest = new NPDELETERequest(request.getContext(), request.getUrl(), request.getResolvers(), request.isEncode(), request.getUser(), request.getPwd(), request.getData(), request.getmHeaders());
		}else{
			newRequest = new NPDELETERequest(request.getContext(), request.getUrl(), request.getResolvers(), request.isEncode(), request.getUser(), request.getPwd(), request.getmHeaders());
		}

		return newRequest;
	}
}
