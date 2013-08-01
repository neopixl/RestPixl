
package org.droidparts.http.worker;

import static com.neopixl.restpixl.NPRestPixlConf.BUFFER_SIZE;

import android.annotation.SuppressLint;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.droidparts.http.HTTPException;
import org.droidparts.util.io.IOUtils;

import com.neopixl.logger.NPLog;

@SuppressLint("DefaultLocale")
class HTTPInputStream extends BufferedInputStream {

	static HTTPInputStream getInstance(HttpURLConnection conn,
			boolean useErrorStream) throws HTTPException {
		try {
			InputStream is = useErrorStream ? conn.getErrorStream() : conn
					.getInputStream();
			is = getUnpackedInputStream(conn.getContentEncoding(), is);
			return new HTTPInputStream(is, conn, null);
		} catch (Exception e) {
			throw new HTTPException(e);
		}
	}

	public static HTTPInputStream getInstance(HttpResponse resp)
			throws HTTPException {
		HttpEntity entity = resp.getEntity();
		try {
			InputStream is = entity.getContent();
			Header ce = entity.getContentEncoding();
			is = getUnpackedInputStream(ce != null ? ce.getValue() : null, is);
			return new HTTPInputStream(is, null, entity);
		} catch (Exception e) {
			throw new HTTPException(e);
		}
	}

	private static InputStream getUnpackedInputStream(String contentEncoding,
			InputStream is) throws IOException {
		NPLog.d("Content-Encoding: " + contentEncoding);
		if (isNotEmpty(contentEncoding)) {
			contentEncoding = contentEncoding.toLowerCase();
			if (contentEncoding.contains("gzip")) {
				return new GZIPInputStream(is);
			} else if (contentEncoding.contains("deflate")) {
				return new InflaterInputStream(is);
			}
		}
		return is;
	}

	private final HttpURLConnection conn;
	private final HttpEntity entity;

	private HTTPInputStream(InputStream is, HttpURLConnection conn,
			HttpEntity entity) throws HTTPException {
		super(is, BUFFER_SIZE);
		this.conn = conn;
		this.entity = entity;
	}

	public String readAndClose() throws HTTPException {
		try {
			return IOUtils.readAndCloseInputStream(this);
		} catch (Exception e) {
			throw new HTTPException(e);
		}
	}

	@Override
	public void close() throws IOException {
		super.close();
		if (conn != null) {
			conn.disconnect();
		} else if (entity != null) {
			entity.consumeContent();
		}
	}
	
	public static boolean isNotEmpty(CharSequence str) {
		return !isEmpty(str);
	}

	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

}