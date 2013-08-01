package com.neopixl.restpixl;

/**
 * Request enum
 * Fin all REST existing mode
 * @author odemolliens
 * Neopixl
 */
public enum NPRequestEnum {
	GET(0,"GET"), POST(1,"POST"), PUT(2,"PUT"), DELETE(3,"DELETE");

	private int code;
	private String codeName;

	private NPRequestEnum(int c, String cN) {
		code = c;
		codeName = cN;
	}

	public int getCode() {
		return code;
	}
	
	public String getCodeName() {
		return codeName;
	}
}
