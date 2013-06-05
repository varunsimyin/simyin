package com.vpundit.www.tests.integration

import static org.junit.Assert.*


import com.vpundit.www.IndexController;
import com.vpundit.www.JSONParserService;
import com.vpundit.www.XMLService

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile
import org.junit.*

class IndexControllerIntegrationTests {

	IndexController controller
	def reasoningXMLLoader
    @Before
    void setUp() {
        // Setup logic here

		
		BasicConfigurator.configure()
		org.apache.log4j.LogManager.rootLogger.level = Level.DEBUG
		org.apache.log4j.Logger log = org.apache.log4j.LogManager.getLogger("IndexControler")
		IndexController.class.metaClass.getLog << {-> log}
		
		controller = new IndexController()
		controller.XMLService = new XMLService()
		controller.XMLService.reasoningXMLLoader = reasoningXMLLoader
	
		
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testTreeConversion() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><reasoning><reasoningInputSequence>"+
		"<reasoningInput datatype=\"Real\" inputNumber=\"11\"/></reasoningInputSequence><dataOutput datatype=\"Real\"/>"+
		"<operatorSequence><operator structIndex=\"0\" type=\"Reciprocal\"><operatorInputSequence>IN8</operatorInputSequence>"+
		"<weightSequence>1.0</weightSequence><modSequence>true</modSequence></operator></operatorSequence></reasoning>"
		final file = new GrailsMockMultipartFile("file", xml.bytes)
		controller.request.addFile(file)

		controller.submit()
		def jsonTree = controller.modelAndView.model.get("treeField")
		JSONParserService jsonService = new JSONParserService()
		jsonService.grailsApplication = new org.codehaus.groovy.grails.commons.DefaultGrailsApplication()
		def tree = jsonService.JSONToTree(jsonTree.toString(), null)
		
		//varun will help out with getting the tree to a persistentrep.Reasoning here, so we can test out all the conversions
		fail("need to extract the treefield from the rendered page and convert it to xml and compare it with the starting xml")
    }
}
