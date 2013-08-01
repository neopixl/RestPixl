
package org.droidparts.util.io;

import static com.neopixl.restpixl.NPRestPixlConf.BUFFER_SIZE;
import static com.neopixl.restpixl.NPRestPixlConf.FORMAT;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import com.neopixl.logger.NPLog;

public class IOUtils {

	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, FORMAT);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("failed to encode", e);
		}
	}

	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, FORMAT);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("failed to decode", e);
		}
	}

	public static void silentlyClose(Closeable... closeables) {
		for (Closeable cl : closeables) {
			try {
				if (cl != null) {
					cl.close();
				}
			} catch (Exception e) {
				NPLog.d(e);
			}
		}
	}

	public static String readAndCloseInputStream(InputStream is)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, FORMAT),
					BUFFER_SIZE);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			return sb.toString();
		} finally {
			silentlyClose(br);
		}
	}

	public static ArrayList<File> getFileList(File dir,
			String... fileExtensions) {
		final ArrayList<File> files = new ArrayList<File>();
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				if (fileExtensions.length == 0) {
					files.add(file);
				} else {
					
					String fileName = file.getName().toLowerCase();
					for (String ext : fileExtensions) {
						if (fileName.endsWith(ext)) {
							files.add(file);
							break;
						}
					}
				}
			} else {
				files.addAll(getFileList(file, fileExtensions));
			}
		}
		return files;
	}

	public static void copy(File fileFrom, File fileTo) throws IOException {
		FileChannel src = null;
		FileChannel dst = null;
		try {
			src = new FileInputStream(fileFrom).getChannel();
			dst = new FileOutputStream(fileTo).getChannel();
			dst.transferFrom(src, 0, src.size());
		} finally {
			silentlyClose(src, dst);
		}

	}

}
