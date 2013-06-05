package com.vpundit.www.tests.integration

import static org.junit.Assert.*
import org.junit.*
import org.xml.sax.SAXException;

import com.vpundit.www.XMLService;
import com.vpundit.www.custom.CoreException;

import datastructures.ExposedNode

import persistentrep.ReasoningXMLGenerator
import reasoning.Reasoning


class XMLServiceIntegrationTests extends GroovyTestCase
{

	def reasoningXMLLoader
	def objectMapperService

	@Before
	void setUp()
	{
		// Setup logic here
	}

	@After
	void tearDown()
	{
		// Tear down logic here
	}
	
	@Test
	void testParsing()
	{
		String xml = """<?xml version="1.0" encoding="UTF-8" standalone="no"?><reasoning><reasoningInputSequence><reasoningInput datatype="Real" inputNumber="11"/></reasoningInputSequence><dataOutput datatype="Real"/><operatorSequence><operator structIndex="0" type="Reciprocal"><operatorInputSequence>IN8</operatorInputSequence><weightSequence>1.0</weightSequence><modSequence>true</modSequence></operator></operatorSequence></reasoning>"""
		
		def service = new XMLService()
		service.reasoningXMLLoader = reasoningXMLLoader
		def reasoning = service.parseXML(xml)

		assert reasoning != null
		
	}

	@Test
	void testFaultyInput()
	{
		def service = new XMLService()

		def message = shouldFail(CoreException)
		{
			service.reasoningXMLLoader = reasoningXMLLoader
			service.parseXML("invalid")
		}
		
		assert message == "Core was unable to parse XML string."
	}
}
