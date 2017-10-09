/*
 * Copyright (C) 2012 j73x73r@gmail.com.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package shoppino.domain;


/**
 * @author Christoph Strobl
 */
public interface SearchablePerson extends Searchable {

//  String USERNAME_FIELD = "username";
  String FULLNAME_FIELD = "fullName";
  String PHOTO_FIELD = "photo";
  String TENANT_FIELD = "tenant";
  String TENANTS_FIELD = "tenants";   
}
