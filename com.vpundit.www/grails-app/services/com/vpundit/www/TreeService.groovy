package com.vpundit.www

import org.apache.commons.lang.mutable.MutableInt;

import operators.Operator
import com.vpundit.www.custom.TreeInfo;
import com.vpundit.www.custom.TreeNode
import com.vpundit.www.test.helpers.CommonStringParser;

class TreeService
{
	def configurationService
	def grailsApplication

	def makeSymmetric(TreeNode parentNode)
	{
		if(parentNode == null)
		{
			return
		}

		parentNode.Children?.sort
		{
			it.X
		}
		

		int middleIndex = parentNode.Children.size()/2


		for(child in parentNode.Children)
		{
			child.ParentX = parentNode.X
			child.ParentY = parentNode.Y
			child.Y = parentNode.Y + 4 * grailsApplication.config.frontend.JSONTree.radius
		}

		for(int i = 0; i < middleIndex; i++)
		{
			if(i == 0)
			{
				parentNode.Children[i].X = parentNode.X - 3 * grailsApplication.config.frontend.JSONTree.radius
			}
			else
			{
				parentNode.Children[i].X = parentNode.Children[i - 1].X - 3 * grailsApplication.config.frontend.JSONTree.radius
			}

			makeSymmetric(parentNode.Children[i])
		}


		boolean odd = false

		if(parentNode.Children.size % 2 != 0)
		{
			parentNode.Children[middleIndex].X = parentNode.X
			odd = true
			middleIndex++
		}
		int upperBound = parentNode.Children.size()

		for (int i = middleIndex; i < upperBound; i ++)
		{
			def me = parentNode.Children[i]
			
			if(i == middleIndex)
			{
				me.X = parentNode.X + 3 * grailsApplication.config.frontend.JSONTree.radius
			}
			else
			{
				me.X = parentNode.Children[i - 1].X + 3 * grailsApplication.config.frontend.JSONTree.radius
			}
			
			makeSymmetric(parentNode.Children[i])
		}
		
		parentNode.Children?.sort
		{
			it.X
		}
	}

	def spreadChildrenApart(TreeNode parentNode)
	{
		int shiftAmount = 2 * grailsApplication.config.frontend.JSONTree.radius

		if(parentNode == null)
		{
			return
		}

		int middleIndex = parentNode.Children.size()/2

		for(int i = 0; i < middleIndex; i++)
		{
			def me = parentNode.Children[i]
			def myShift = shiftAmount * (i + 1) * -1
			me.X += myShift
			makeSymmetric(me)

		}


		boolean odd = false

		if(parentNode.Children.size % 3 == 0)
		{
			odd = true
			middleIndex++
		}
		int upperBound = parentNode.Children.size()

		for (int i = middleIndex; i < upperBound; i ++)
		{
			def me = parentNode.Children[i]
			def myShift = shiftAmount * (i - middleIndex + 1)
			me.X += myShift
			makeSymmetric(me)
		}

	}

	def makeChildrenFollowMe(TreeNode node, int shiftAmount)
	{

		node.ParentX = node.Parent.X
		node.ParentY = node.Parent.Y
		node.X += shiftAmount
		node.Y = node.Parent.Y + 4 * node.Radius

		for(child in node.Children)
		{
			makeChildrenFollowMe(child, shiftAmount)
		}
		
	}

	def findRootOverlaps(TreeNode root, TreeNode tree)
	{
		if(root == null || tree == null)
		{
			return null
		}
		def overlap = findOverlaps(root, tree)

		if(overlap != null)
		{
			return overlap
		}

		for(rootChild in root.Children)
		{
			overlap = findRootOverlaps(rootChild, tree)
			if(overlap != null)
			{
				return overlap
			}
		}

		null
	}
	
	
	/**
	 * Checks to see if there are overlaps with the node and the tree
	 * @param node
	 * the node for which we will be checking to see if there are overlaps with it in the tree
	 * @param tree
	 * the tree which we will run through to check and see if there are overlaps with this node
	 * @return
	 */
	def findOverlaps(TreeNode node, TreeNode tree)
	{
		if(tree == null)
		{
			return null
		}


		boolean rightOverlap = node.X <= tree.X && (node.X + node.Radius ) >= (tree.X - tree.Radius)
		boolean leftOverlap = node.X >= tree.X && (node.X - node.Radius) <= (tree.X + tree.Radius)
		
		
		if(node.Id != tree.Id && (rightOverlap || leftOverlap) && node.myDepth == tree.myDepth)
		{
			return findLeastCommonParent(node, tree)
		}


		for(treeChild in tree.Children)
		{
			TreeNode childOverlap = findOverlaps(node, treeChild)

			if(childOverlap != null)
			{
				return childOverlap
			}
		}
	}

	def findLeastCommonParent(TreeNode node1, TreeNode node2)
	{
		if(node1.is(null) || node2.is(null))
		{
			return null
		}

		def isCommon = checkIfParentInAncestry(node1, node2.Parent)

		if(isCommon)
		{
			return node2.Parent
		}

		findLeastCommonParent(node1, node2.Parent)
	}

	def checkIfParentInAncestry(TreeNode node, TreeNode parent)
	{
		if(node == null || parent == null)
		{
			return false
		}

		if(node.Parent?.Id == parent.Id)
		{
			return true
		}

		checkIfParentInAncestry(node.Parent, parent)
	}

	//builds a front end tree from core tree
	//treeinfo is what gets to display the legend on the tree screen in the web gui
	def buildTree(TreeNode treeParentNode, int currentDepth, Operator coreParentNode, MutableInt constantCount, TreeInfo treeInfo)
	{
		if(coreParentNode.inputEdges == null || coreParentNode.inputEdges.empty)
		{
			return
		}

		currentDepth++
		for(outerChildren in coreParentNode.inputEdges)
		{
			for(innerChild in outerChildren)
			{
				def _class = innerChild.input.node.getClass()
				def value = CommonStringParser.GetClassNameOnly(_class)
				def weight = innerChild.weight.displayString()
				String nodeInfo = ""
				if(value.toLowerCase().equals("constant"))
				{
					value = "C:" + constantCount
					nodeInfo = innerChild.input.node.output?.extraInfo()
					treeInfo.Legend += value + "|" + nodeInfo + "</br>"
					constantCount.value += 1
				}
				
				def childTree = treeParentNode.AddChild(value, weight,nodeInfo )


				buildTree(childTree, currentDepth, innerChild.input.node, constantCount, treeInfo)
			}
		}


		void
	}
}
