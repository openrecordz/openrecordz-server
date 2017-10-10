package openrecordz.web;


import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.Controller

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class TestController 
//implements Controller
 {

   
	@RequestMapping("/groovy/status_groovy")
    public @ResponseBody String getStatus() {
            return "Hello World from groovy!";
    }
}