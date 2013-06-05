package com.vpundit.www



import grails.test.GrailsUnitTestCase
import grails.test.mixin.*
import org.junit.*
import com.vpundit.www.custom.TreeNode

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(TreeService)
class TreeServiceTests
{
	
	void testFindOverlaps()
	{
		int radius = 10
		def root = new TreeNode("5", null, 0, radius, "0", "")
		root.X = 300
		root.Y = 200
		root.Id = "root"

		def node1 = new TreeNode("5", root, 1, radius, "0", "")
		node1.X = 200
		node1.Y = 300
		node1.Id = "left"

		def node2 = new TreeNode("5", root, 1, radius, "0", "")
		node2.X = 200
		node2.Y = 300
		node2.Id = "right"

		root.AddChild(node2)
		root.AddChild(node1)

		def overlaps = service.findOverlaps(node1, root)

		assert overlaps == root
	}

	//tests to see if two overlapping children will return the parent 
	void testFindRootOverlaps()
	{
		int radius = 30
		def root = new TreeNode("5", null, 0, radius, "0", "")
		root.X = 300
		root.Y = 200
		root.Id = "root"

		def node1 = new TreeNode("5", root, 0, radius, "0", "")
		node1.X = 200
		node1.Y = 300
		node1.Id = "left"

		TreeNode node2 = new TreeNode("5", root, 0, radius, "0", "")
		node2.X = 220
		node2.Y = 300
		node2.Id = "right"

		root.AddChild(node2)
		root.AddChild(node1)

		def overlaps = service.findRootOverlaps(root, root)

		assert overlaps == root
	}

	void testFixOverlaps()
	{
		int radius = 10
		def root = new TreeNode("5", null, 0, radius, "0", "")
		root.X = 300
		root.Y = 200
		root.Id = "root"

		def node1 = new TreeNode("5", root, 0, radius, "0", "")
		node1.X = 100
		node1.Y = 300
		node1.Id = "left"


		def node3 = new TreeNode("5", root, 0, radius, "0", "")
		node3.X = 195
		node3.Y = 300
		node3.Id = "middle"

		def node2 = new TreeNode("5", root, 0, radius, "0", "")
		node2.X = 200
		node2.Y = 300
		node2.Id = "right"

		root.AddChild(node2)
		root.AddChild(node1)
		root.AddChild(node3)

		//the following fixes trees
		def overlap = service.findRootOverlaps(root, root)

		while(overlap != null)
		{
			service.spreadChildrenApart(overlap)

			overlap = service.findRootOverlaps(root, root)
		}
		//end fixing overlaps
		
		def overlaps = service.findRootOverlaps(root, root)

		assert overlaps == null
	}
}
