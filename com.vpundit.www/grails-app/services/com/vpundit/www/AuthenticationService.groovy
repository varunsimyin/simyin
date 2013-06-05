package com.vpundit.www

class AuthenticationService {

    def authenticate(String username, String passwd) 
	{
		if(username == null)
		{
			return null
		}
		
		def user = User.findByUsername(username)
		if(user == null)
		{
			return null
		}
		else if(passwd != user.passwd)
		{
			return null
		}
		else
		{
			return user
		}
    }
}
