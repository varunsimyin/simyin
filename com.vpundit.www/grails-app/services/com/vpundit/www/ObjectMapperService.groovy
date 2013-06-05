package com.vpundit.www

import org.apache.commons.lang.mutable.MutableInt;

import operators.Operator
import datastructures.ExposedNode
import reasoning.Reasoning
import com.vpundit.www.custom.TreeInfo;
import com.vpundit.www.custom.TreeNode
import com.vpundit.www.custom.Tree
import com.vpundit.www.test.helpers.CommonStringParser;

class ObjectMapperService
{

	def grailsApplication
	def treeService

	Tree asTree(Reasoning reasoning)
	{

		def coreTree = reasoning.operatorSequence.tail.node
		def value = CommonStringParser.GetClassNameOnly(coreTree.getClass())
		String weight = reasoning.outputNode.inputEdges.get(0).get(0).weight.displayString()
		def root = new TreeNode(value, null, 0, grailsApplication.config.frontend.JSONTree.radius, weight, "")
		root.X = 1000
		root.Y = 100

		TreeInfo info = new TreeInfo()
		info.Legend = ""
		MutableInt count = new MutableInt(0)
		treeService.buildTree(root, 1, coreTree, count, info)

		treeService.makeSymmetric(root)

		def overlap = treeService.findRootOverlaps(root, root)

		while(overlap != null)
		{
			treeService.spreadChildrenApart(overlap)

			overlap = treeService.findRootOverlaps(root, root)
		}

		new Tree(root, info)
	}
	
	Reasoning asReasoning(Tree tree)
	{
		//Reasoning reasoning = new Reasoning
		null
	}
}
