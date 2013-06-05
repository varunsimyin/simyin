package com.vpundit.www

class UserService {
	
	AuthenticationService authenticationService
	
    def login(String username, String passwd) 
	{
		def user = authenticationService.authenticate(username, passwd)
		
		if(user != null)
		{
			return true
		}
		else
		{
			return false
		}
    }
	
	def getUser(String usernameParam)
	{
		def u = User.findByUsername(usernameParam)
		
		return u
	}
	
	def updateUser(String username, String passwd, User newUser)
	{
		if(username == null)
		{
			if(getUser(newUser.username) == null)
			{
				newUser.save()
				return true
			}
			else
			{
				return false
			}
		}
		else 
		{
			def oldUser = authenticationService.authenticate(username, passwd)
			if(oldUser != null)
			{
				oldUser.username = newUser.username;
				oldUser.passwd = newUser.passwd
				oldUser.save()
				
				return true
			}
			else
			{
				return false
			}
		}
		
	}
}
