
package org.droidparts.contract;

public interface Constants {

	String UTF8 = "utf-8";
	int BUFFER_SIZE = 8 * 1024;

	static interface ManifestMeta {

		String DEPENDENCY_PROVIDER = "neopixl_dependency_provider";

		String LOG_LEVEL = "neopixl_log_level";

		String DISABLE = "disable";
		String VERBOSE = "verbose";
		String DEBUG = "debug";
		String INFO = "info";
		String WARN = "warn";
		String ERROR = "error";
		String ASSERT = "assert";

	}

}
