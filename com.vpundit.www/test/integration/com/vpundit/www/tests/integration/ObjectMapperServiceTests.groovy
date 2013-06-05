package com.vpundit.www.tests.integration

import static org.junit.Assert.*
import org.junit.*
import com.vpundit.www.XMLService;

class ObjectMapperServiceTests {

	XMLService xmlService
	def reasoningXMLLoader
	
    @Before
    void setUp() {
		xmlService = new XMLService()
		xmlService.reasoningXMLLoader = reasoningXMLLoader
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testAsTree() {
		def xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><reasoning><reasoningInputSequence><reasoningInput "+
		"datatype=\"Real\" inputNumber=\"11\"/></reasoningInputSequence><dataOutput datatype=\"Bool\"/><operatorSequence>"+
		"<operator structIndex=\"0\" type=\"Reciprocal\"><operatorInputSequence>IN8</operatorInputSequence><weightSequence>1.0</weightSequence>"+
		"<modSequence>true</modSequence></operator><operator structIndex=\"0\" type=\"Multiply\"><operatorInputSequence>0,IN10</operatorInputSequence>"+
		"<weightSequence>1.0,1.0</weightSequence><modSequence>true,true</modSequence></operator><operator structIndex=\"1\" type=\"Add\">"+
		"<operatorInputSequence>IN9,IN3,1</operatorInputSequence><weightSequence>0.8725361263281668,-0.2684577700393682,-0.06250598384426187</weightSequence>"+
		"<modSequence>true,true,true</modSequence></operator><operator structIndex=\"2\" type=\"Threshold\"><operatorInputSequence>2,Real:1.0</operatorInputSequence>"+
		"<weightSequence>1.0,-11.368707076484245</weightSequence><modSequence>false,true</modSequence></operator></operatorSequence></reasoning>"
		def reasoning = xmlService.parseXML(xml)
		def tree = reasoning.node.asTree()
		def temp = tree.asReasoning()
    }
}
