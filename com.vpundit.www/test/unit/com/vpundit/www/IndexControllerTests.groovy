package com.vpundit.www



import javax.servlet.http.HttpServletRequest;
import org.codehaus.groovy.grails.plugins.testing.*
import grails.test.mixin.*
import groovy.mock.interceptor.MockFor;

import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(IndexController)
@Mock(XMLService)
class IndexControllerTests
{
	void testSubmit()
	{
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><reasoning><reasoningInputSequence>"+
		"<reasoningInput datatype=\"Real\" inputNumber=\"11\"/></reasoningInputSequence><dataOutput datatype=\"Real\"/>"+
		"<operatorSequence><operator structIndex=\"0\" type=\"Reciprocal\"><operatorInputSequence>IN8</operatorInputSequence>"+
		"<weightSequence>1.0</weightSequence><modSequence>true</modSequence></operator></operatorSequence></reasoning>"
		final file = new GrailsMockMultipartFile("file", xml.bytes)
		request.addFile(file)
		controller.submit()
		def jsonTree = controller.modelAndView.model.get("treeField")
		fail("convert the json to XML using a library in the front end and compare")
	}
}
