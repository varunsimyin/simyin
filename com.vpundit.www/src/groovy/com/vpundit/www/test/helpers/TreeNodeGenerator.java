package com.vpundit.www.test.helpers;

import com.vpundit.www.custom.TreeNode;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;

public class TreeNodeGenerator implements Generator<TreeNode>
{
	Generator<String> stringGen = PrimitiveGenerators.strings(5, 100);
	Generator<Integer> intGen = PrimitiveGenerators.integers(5, 20);

	@Override
	public TreeNode next()
	{
		return new TreeNode(stringGen.next(), null, intGen.next(), intGen.next(), stringGen.next(), stringGen.next());
	}

}
