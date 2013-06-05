package com.vpundit.www

import static org.junit.Assert.*


import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*
import org.xml.sax.SAXException
import org.gmock.GMockTestCase
import org.gmock.WithGMock

import com.vpundit.www.custom.CoreException

import datastructures.ExposedNode
import persistentrep.ReasoningXMLGenerator
import persistentrep.ReasoningXMLLoader
import reasoning.Reasoning
import groovy.mock.interceptor.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */

@TestFor(XMLService)
@WithGMock
class XMLServiceTests
{

	void testParseXml()
	{

		ExposedNode<Reasoning> node = new ExposedNode<Reasoning>()
		node.index = 666

		def loader = mock(ReasoningXMLLoader)

		//we will fake that this is valid since we just want to test that the service
		//returns the same object as the xml loader on correct inputs
		//the assumptions is that the core will test this more thoroughly

		loader.parseString("we will fake that this is valid").returns(node)

		service.reasoningXMLLoader = loader

		play
		{
			def result = service.parseXML("we will fake that this is valid")
			assert result == node && result.index == node.index && result.index == 666
		}
	}


	//this tests that the web converts exceptions to CoreException
	void testParseXMLFaultyInput()
	{
		def loader = mock(ReasoningXMLLoader)
		
		//create a mock that throws SAXException on invalid string
		loader.parseString("invalid").raises(SAXException, "Content is not allowed in prolog.")

		def logger = mock(org.apache.commons.logging.Log)
		
		//we are also going to test the logger. we are making sure that it's called
		//we are making sure the type of logged exception is SAXException
		//and that the exception message of the logged exception is the correct one.
		
		logger.error(match{it.class == SAXException.class && it.message ==  "Content is not allowed in prolog."})

		play
		{
			service.reasoningXMLLoader = loader
			service.log = logger
			
			//make sure we fail with the right exception
			def message = shouldFail(CoreException)
			{  
				service.parseXML("invalid")  
			}

			assert message == "Core was unable to parse XML string."

		}
	}
}
