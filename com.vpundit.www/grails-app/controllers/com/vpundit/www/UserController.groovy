package com.vpundit.www

import com.vpundit.www.custom.TreeNode
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

class UserController 
{
	
	def userService
	
	def authenticationService

    def index() { }
	
	def show()
	{
		def user = new User(username: null, passwd: null)
		if(request.JSON.has("username") && request.JSON.has("passwd"))
		{
			String username = request.JSON.get("username")
			String passwd = request.JSON.get("passwd")
		
			user = authenticationService.authenticate(username, passwd)
		}
		
		render user as JSON
	}
	
	def update()
	{
		String oldUserName = null
		String oldPasswd = null
		if(request.JSON.has("oldusername") && request.JSON.has("oldpasswd"))
		{
			oldUserName = request.JSON.get("oldusername")
			oldPasswd = request.JSON.get("oldpasswd")
		}
		
		def newUser = new User(username: request.JSON.get("newusername"), passwd: request.JSON.get("newpasswd"))
		def result = userService.updateUser(oldUserName, oldPasswd, newUser)
		
		if(result == true)
		{
			render "success"
			//render new JSONObject().put("result", "success")
		}
		else
		{
			render "failure"
			//render new JSONObject().put("result", "failure")
		}
	}
	
	def delete()
	{
		
	}
	
	def login()
	{	
		userService.login(username: request.JSON.get("username"), passwd: request.JSON.get("passwd"))
	}
}
