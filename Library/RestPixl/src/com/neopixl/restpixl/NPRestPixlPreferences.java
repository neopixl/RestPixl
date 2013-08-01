package com.neopixl.restpixl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.neopixl.logger.NPLog;

/**
 * Get information from Manifest
 * @author odemolliens
 * Neopixl
 */
public class NPRestPixlPreferences {

	/**
	 * Return package="..." value from AndroidManifest file
	 * @param context
	 * @return string	distribution token
	 */
	public static String getPackageName(Context context)
	{
		if(context==null){
			NPLog.e("Context can't be nil");
			return "ERROR";
		}
		try {

			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pinfo.packageName;

		} catch (NameNotFoundException e) {
			NPLog.e("Can't found package=\"...\" value in manifest");
			NPLog.e("Error log:\n"+e.getStackTrace());
			return "ERROR";
		}
	}

	/**
	 * Return android:versionCode="..." value from AndroidManifest file
	 * @param context
	 * @return string	distribution token
	 */
	public static int getVersionCode(Context context)
	{
		if(context==null){
			NPLog.e("Context can't be nil");
			return -1;
		}
		try {

			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pinfo.versionCode;

		} catch (NameNotFoundException e) {
			NPLog.e("Can't found android:versionCode=\"...\" value in manifest");
			NPLog.e("Error log:\n"+e.getStackTrace());
			return -1;
		}
	}

	/**
	 * Return android:versionName="..." value from AndroidManifest file
	 * @param context
	 * @return string	distribution token
	 */
	public static String getVersionName(Context context)
	{
		if(context==null){
			NPLog.e("Context can't be nil");
			return "ERROR";
		}
		try {

			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pinfo.versionName;

		} catch (NameNotFoundException e) {
			NPLog.e("Can't found android:versionName=\"...\" value in manifest");
			NPLog.e("Error log:\n"+e.getStackTrace());
			return "ERROR";
		}
	}
}
