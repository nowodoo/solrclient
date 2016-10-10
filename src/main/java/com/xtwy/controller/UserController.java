package com.xtwy.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xtwy.model.User;
import com.xtwy.service.UserService;

@RequestMapping("/user")
@Controller
public class UserController {
	
	@Resource
	private UserService userService;
	@RequestMapping("/get/{id}")
	public @ResponseBody Object getUser(@PathVariable("id")Integer id){
		try {
			return userService.get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/save")
	public @ResponseBody Object save(){
		try {
			User user = new User();
			user.setId(9);
			user.setDescription("testAdd");
			user.setHobby("非常擅长泡妞！");
			user.setAge(12);
			user.setSex("男");
			user.setUserName("小黄");
			return userService.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/del/{id}")
	public @ResponseBody Object delUser(@PathVariable("id")Integer id){
		try {
			return userService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping("/search/{key}")
	public @ResponseBody Object search(@PathVariable("key")String key){
		try {
			return userService.search(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/highlight/{key}")
	public @ResponseBody Object highlight(@PathVariable("key")String key){
		try {
			return userService.searchHighLight(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
