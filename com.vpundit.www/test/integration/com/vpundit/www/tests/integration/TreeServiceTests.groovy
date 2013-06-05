package com.vpundit.www.tests.integration

import static org.junit.Assert.*
import org.junit.*

import com.vpundit.www.XMLService;
import com.vpundit.www.custom.Tree;
import com.vpundit.www.custom.TreeNode

class TreeServiceTests {

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
	void testThatTreeServiceGivesExtraInfo() {

		def xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><reasoning><reasoningInputSequence>"+
				"<reasoningInput datatype=\"TextList\" inputNumber=\"1\"/></reasoningInputSequence><dataOutput datatype=\"Bool\"/>"+
				"<operatorSequence><operator structIndex=\"0\" type=\"SSTextMatcher\">"+
				"<operatorInputSequence>IN0,SS:[POS: verb];2696957,SS:[POS: verb];118530</operatorInputSequence>"+
				"<weightSequence>null,0.23166769959704592,0.5539387455616729</weightSequence><modSequence>true,true,true</modSequence>"+
				"</operator><operator structIndex=\"0\" type=\"Threshold\"><operatorInputSequence>0,Real:1.0</operatorInputSequence>"+
				"<weightSequence>1.0,0.922645971695879</weightSequence><modSequence>false,true</modSequence></operator>"+
				"<operator structIndex=\"1\" type=\"SSTextMatcher\">"+
				"<operatorInputSequence>IN0,SS:[POS: noun];9281009,SS:[POS: adverb];50110</operatorInputSequence>"+
				"<weightSequence>null,-0.37828635283779755,-0.7548684773432697</weightSequence><modSequence>true,true,true</modSequence>"+
				"</operator><operator structIndex=\"1\" type=\"Threshold\"><operatorInputSequence>2,Real:1.0</operatorInputSequence>"+
				"<weightSequence>1.0,-0.14320225940072429</weightSequence><modSequence>false,true</modSequence></operator>"+
				"<operator structIndex=\"2\" type=\"Or\"><operatorInputSequence>1,3</operatorInputSequence><weightSequence>false,true</weightSequence>"+
				"<modSequence>true,true</modSequence></operator></operatorSequence></reasoning>"

		def reasoning = xmlService.parseXML(xml)
		Tree tree = reasoning.node.asTree()
		for(def child in tree.Root.Children) {
			validateChildrenInfos(child)
		}
	}

	//this is used only in one place for now. if we see a need to validate the children like this in
	//more than one place, we'll have to implement some kind of a flatten method to do this
	private void validateChildrenInfos(TreeNode tree)
	{
		if(tree == null)
		{
			return;
		}
		for(def child in tree.Children)
		{
			switch (child.Value) {
				case "C:0":
					assert child.Info == "[POS: verb];2696957:\nrun, go\nhave a particular form; \"the story or argument runs as "+
					"follows\"; \"as the saying goes...\""
					break;
				case "C:1":
					assert child.Info == "[POS: verb];118530:\nget, let, have\ncause to move; cause to be in a certain position or "+
					"condition; \"He got his squad on the ball\"; \"This let me in for a big surprise\"; \"He got a girl into trouble\""
					break;
				case "C:3":
					assert child.Info == "[POS: noun];9281009:\nrivulet, rill, run, runnel, streamlet\na small stream"
					break;
				case "C:4":
					assert child.Info == "[POS: adverb];50110:\nnow\n(prefatory or transitional) indicates a change of subject or "+
					"activity; \"Now the next problem is...\""
					break;
				default:
					validateChildrenInfos(child)
					break;
			}
		}
	}
}
