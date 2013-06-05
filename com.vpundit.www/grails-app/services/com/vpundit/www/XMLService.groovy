package com.vpundit.www

import com.vpundit.www.custom.CoreException

import persistentrep.ReasoningXMLLoader
import reasoning.Reasoning
import datastructures.ExposedNode

class XMLService
{
	def reasoningXMLLoader
	ExposedNode<Reasoning> parseXML(String xmlStringToParse)
	{
		def result
		try
		{
			result = reasoningXMLLoader.parseString(xmlStringToParse)
		}
		catch(exception)
		{
			log.error(exception)
			throw new CoreException("Core was unable to parse XML string.")
		}
		result
	}
}
