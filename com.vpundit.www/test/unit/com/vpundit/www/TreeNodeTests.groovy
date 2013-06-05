package com.vpundit.www

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*
import net.java.quickcheck.Generator
import net.java.quickcheck.generator.PrimitiveGenerators

import com.vpundit.www.custom.TreeNode
import com.vpundit.www.test.helpers.TreeNodeGenerator;

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class TreeNodeTests {

	Generator<String> stringGen
	Generator<Integer> intGen
	TreeNodeGenerator treeNodeGen

	@Before
	void setUp() {
		intGen  = PrimitiveGenerators.integers(5, 20)
		stringGen = PrimitiveGenerators.strings(5, 100)
		treeNodeGen = new TreeNodeGenerator()
	}

	void tearDown() {
		// Tear down logic here
	}

	@Test
	void testConstructorNullParent() {
		def val = stringGen.next()
		def depth = intGen.next()
		def rad = intGen.next()
		def weight = stringGen.next()
		def info = stringGen.next()
		def tree = new TreeNode(val, null, depth, rad, weight, info)

		assert tree.Value == val
		assert tree.Parent == null
		assert tree.MyDepth == depth
		assert tree.Radius == rad
		assert tree.Children.size == 0
		assert tree.Weight == weight
		assert tree.Info == info
	}

	@Test
	void testNotEquals() {
		def node = treeNodeGen.next()
		def node2 = treeNodeGen.next()

		assert node != node2
	}

	@Test
	void testEqualsNullParent() {
		def node = treeNodeGen.next()

		def val = node.Value
		def depth = node.MyDepth
		def rad = node.Radius
		def weight = node.Weight
		def info = node.Info
		def tree = new TreeNode(val, null, depth, rad, weight, info)
		node.Parent = null
		tree.Id = node.Id
		assert tree == node
	}

	@Test
	void testEqualsWithParent() {
		def node = treeNodeGen.next()

		def val = node.Value
		def depth = node.MyDepth
		def rad = node.Radius
		def weight = node.Weight
		def info = node.Info
		def parent = node.Parent
		def tree = new TreeNode(val, parent, depth, rad, weight, info)
		
		tree.Id = node.Id
		assert tree == node
	}

	@Test
	void testEqualsNullChildren() 
	{
		def node = treeNodeGen.next()

		def val = node.Value
		def depth = node.MyDepth
		def rad = node.Radius
		def weight = node.Weight
		def info = node.Info
		def parent = node.Parent
		def tree = new TreeNode(val, parent, depth, rad, weight, info)
		tree.Children = null
		tree.Id = node.Id
		node.Children = null
		assert tree == node
	}

	@Test
	void testEqualsWithChildren() {
		
		def child1 = treeNodeGen.next()
		def child2 = treeNodeGen.next()
		
		def node = treeNodeGen.next()

		node.Children.add(child1)
		node.Children.add(child2)
		
		def val = node.Value
		def depth = node.MyDepth
		def rad = node.Radius
		def weight = node.Weight
		def info = node.Info
		def parent = node.Parent
		def tree = new TreeNode(val, parent, depth, rad, weight, info)
		tree.Children.add(child1)
		tree.Children.add(child2)
		
		tree.Id = node.Id
		assert tree == node
	}

	@Test
	void testEqualsDifferByOne() {
		
		def node = treeNodeGen.next()

		def val = stringGen.next()
		def depth = node.MyDepth
		def rad = node.Radius
		def weight = node.Weight
		def info = node.Info
		def parent = node.Parent
		def tree = new TreeNode(val, parent, depth, rad, weight, info)
		tree.Children = null
		tree.Id = node.Id
		node.Children = null
		assert tree != node
		assert tree.Value != node.Value
	}
	
	@Test
	void testEqualsDifferentChildren() {
		def child1 = treeNodeGen.next()
		def child2 = treeNodeGen.next()
		
		def node = treeNodeGen.next()

		node.Children.add(child1)
		
		def val = node.Value
		def depth = node.MyDepth
		def rad = node.Radius
		def weight = node.Weight
		def info = node.Info
		def parent = node.Parent
		def tree = new TreeNode(val, parent, depth, rad, weight, info)
		tree.Children.add(child1)
		tree.Children.add(child2)
		
		tree.Id = node.Id
		assert tree != node
	}
	
	@Test
	void testEqualsDifferInChildren() {
		def child1 = treeNodeGen.next()
		def child2 = treeNodeGen.next()
		
		def node = treeNodeGen.next()

		node.Children.add(child1)
		node.Children.add(child2)
		
		def val = node.Value
		def depth = node.MyDepth
		def rad = node.Radius
		def weight = node.Weight
		def info = node.Info
		def parent = node.Parent
		def tree = new TreeNode(val, parent, depth, rad, weight, info)
		tree.Children.add(child1)
		
		def newChild = child2.MakeCopy()
		newChild.Value = stringGen.next()
		tree.Children.add(newChild)
		
		tree.Id = node.Id
		assert tree != node
	}
}
