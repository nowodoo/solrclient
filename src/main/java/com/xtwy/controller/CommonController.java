package com.xtwy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class CommonController {
	
	
	@RequestMapping("/skip/{page}")
	public String skipPage(@PathVariable("page")String page){
		return page;
	}

}
