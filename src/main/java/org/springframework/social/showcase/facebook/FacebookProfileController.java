///*
// * Copyright 2011 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.springframework.social.showcase.facebook;
//
//import javax.inject.Inject;
//
//import org.springframework.social.connect.Connection;
//import org.springframework.social.connect.ConnectionRepository;
//import org.springframework.social.facebook.api.Facebook;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//@Controller
//public class FacebookProfileController {
//	
//	@Inject
//	private ConnectionRepository connectionRepository;
//
//	@RequestMapping(value="/facebook", method=RequestMethod.GET)
//	public String home(Model model) {
//		Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
//		if (connection == null) {
//			return "redirect:/connect/facebook";
//		}
//		model.addAttribute("profile", connection.getApi().userOperations().getUserProfile());
//		return "facebook/profile";
//	}
//
//}
