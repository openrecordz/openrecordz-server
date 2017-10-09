/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package shoppino.security.exception;

public class UsernameAlreadyInUseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1501815954590923018L;

	public UsernameAlreadyInUseException(String username) {
		super("The username '" + username + "' is already in use.");
	}
}
