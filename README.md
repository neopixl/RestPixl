![Logo](https://raw.github.com/neopixl/RestPixl/master/Sample/RestPixlSample/res/drawable-xxhdpi/small.png ) RestPixl
========

The easy way to make REST call for your Android application !

RestPixl is an extract of the framework Droid Parts. But it is better and easier to use. 

Several corrections have been made to support the old API

Provides
========

- Works with basics auth

- Works with request priority

- Queue service available

- GET/POST/PUT/DELETE available

- Single instance of RestClient to have a keep alive connexion with the server

- Works with old API 

- Uses compression & http caching on API > 13.
- Supports in and out headers.


How use it
========

1. Simply import [RestPixl-1.0.0-SNAPSHOT.jar](https://github.com/neopixl/RestPixl/raw/master/Sample/RestPixlSample/libs/RestPixl-1.0.0-SNAPSHOT.jar "RestPixl-1.0.0-SNAPSHOT.jar") in your project.
2. Ready to call web service !
3. Use NPGETRequest, NPPOSTRequest, NPPUTRequest or NPDELETERequest
4. Don't forget implement request listener
5. Use request priority with NPBackgroundLoadingManager class


Copyright
==========


  	Copyright 2013 Neopixl

	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
	
	file except in compliance with the License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software distributed under
	
	the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
	
	ANY KIND, either express or implied. See the License for the specific language governing
	
	permissions and limitations under the License.

	A different license may apply to other software included in this package,
	
	including DroidParts. Please consult their respective headers for the terms 
	
	of their individual licenses.


