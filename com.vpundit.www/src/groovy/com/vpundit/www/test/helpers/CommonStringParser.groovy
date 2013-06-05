package com.vpundit.www.test.helpers

class CommonStringParser 
{
	static def GetClassNameOnly(Class _class)
	{	
		def tokens = _class.getName().tokenize('.')
		tokens[tokens.size() - 1]
	}
}
