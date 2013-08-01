package com.neopixl.restpixl.async;

/**
 * Request priority
 * @author odemolliens
 * Neopixl
 */
public enum NPRequestPriority {
	
	/**
	 * Very low priority
	 */
	REQ_LOW_BACKGROUND_PRIORITY ( -200 ),
	
	/**
	 * Low priority
	 */
	REQ_BACKGROUND_PRIORITY ( -100 ),
	
	/**
	 * middle priority
	 */
	REQ_INTERACTIVE_PRIORITY( 50 ),

	/**
	 * high priority
	 */
	REQ_BOOT_PRIORITY ( 100 );
	
	
    @SuppressWarnings("unused")
	private final int value; 
    
    NPRequestPriority(int value) {
    	this.value = value;
    }
}
