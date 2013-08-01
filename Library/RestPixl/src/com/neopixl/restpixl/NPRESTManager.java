package com.neopixl.restpixl;

import org.droidparts.http.RESTClient;

import android.content.Context;

/**
 * Single REST instance
 * @author odemolliens
 * Neopixl
 */
public class NPRESTManager {
	//Singleton
	private static NPRESTManager instance;
	
	//Global
	private static Context context;
	
	//RESTClient
	private RESTClient restClient;
	
	public RESTClient getRestClient() {
		return restClient;
	}

	// Restrict the constructor from being instantiated
	private NPRESTManager(Context ctx) {
		context = ctx;
		restClient = new RESTClient(context);
	}
	
	public static NPRESTManager getInstance(Context ctx){
		if(instance==null){
			instance=new NPRESTManager(ctx);
		}
		return instance;
	}
	
}
