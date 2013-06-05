package com.vpundit.www.tests.integration

import static org.junit.Assert.*

import org.apache.commons.lang.mutable.MutableInt;
import org.junit.*
import org.apache.log4j.Logger;
import persistentrep.ReasoningXMLLoader;
import reasoning.Reasoning;
import grails.converters.*
import com.vpundit.www.*
import com.vpundit.www.custom.*
import datastructures.ExposedNode;

class JSONParserServiceTests extends GroovyTestCase
{

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
	void testParsingSingleNode()
	{
		String xml = """<?xml version="1.0" encoding="UTF-8" standalone="no"?><reasoning><reasoningInputSequence><reasoningInput datatype="Real" inputNumber="11"/></reasoningInputSequence><dataOutput datatype="Real"/><operatorSequence><operator structIndex="0" type="Reciprocal"><operatorInputSequence>IN8</operatorInputSequence><weightSequence>1.0</weightSequence><modSequence>true</modSequence></operator></operatorSequence></reasoning>"""
		
		def xmlService = new XMLService()
		ReasoningXMLLoader loader = new ReasoningXMLLoader();
		
		xmlService.reasoningXMLLoader = loader
		ExposedNode<Reasoning> exposedNode = xmlService.parseXML(xml)
		
		def treeService = new TreeService()

		def root = new TreeNode("5", null, 0, 10, "weight", "info")
		root.X = 300
		root.Y = 200
		
		TreeInfo info = new TreeInfo()
		MutableInt mInt = new MutableInt()
		def res = exposedNode.node.asTree()

		treeService.buildTree(root, 1,exposedNode.node.operatorSequence.tail.node ,  mInt, info)
		
		//root.log = null;
		def converter = new JSON(root)
		String expectedJSONRepresentation = converter.toString()

		//validate tree info that it was populated correctly here. 
		def jsonService = new JSONParserService()
		
		//generate the grailsApplication object since it doesn't get injected by default
		jsonService.grailsApplication = new org.codehaus.groovy.grails.commons.DefaultGrailsApplication()
		
		//convert from json to tree(TreeNode)
		def convertedTree = jsonService.JSONToTree(expectedJSONRepresentation, null)
		
		//convert to json to be able to compare with original
		def resultConverter = new JSON(convertedTree)
		String actualJSONRepresentation = resultConverter.toString()
		
		//compare expected and actual
		assert expectedJSONRepresentation == actualJSONRepresentation
	}
	
	@Test
	void testJSONToTree()
	{
		
		def logger = Logger.getLogger("test")
		def jsonTree = """{"myDepth":0,"weight":"","children":[{"myDepth":1,"weight":"1.00","children":[],"class":"com.vpundit.www.custom.TreeNode","parent":{"_ref":"../..","class":"com.vpundit.www.custom.TreeNode"},"info":"","id":"9939dfef-269a-461f-ac99-b1136fc63f8f","parentX":1000,"value":"Input","parentY":100,"radius":40,"y":260,"x":1000}],"class":"com.vpundit.www.custom.TreeNode","parent":null,"info":"","id":"4da52cc0-8bc4-4c69-9f00-553992b940d1","parentX":0,"value":"Reciprocal","parentY":0,"radius":40,"y":100,"x":1000}"""
		def service = new JSONParserService()
		service.grailsApplication = new org.codehaus.groovy.grails.commons.DefaultGrailsApplication()

		TreeNode tree = service.JSONToTree(jsonTree,null)
		assert tree.Value == "Reciprocal"
		assert tree.Value.count == 10
		assert tree.Radius == service.grailsApplication.config.frontend.JSONTree.radius
		assert tree.MyDepth == 0
		assert tree.Children.size == 1
		assert tree.Children[0].Children.size == 0
		tree.Children[0].Parent.log = logger
		assert tree.Children[0].Parent.equals(tree)
		assert tree.Children[0].Value == "Input"
		
	}
}
