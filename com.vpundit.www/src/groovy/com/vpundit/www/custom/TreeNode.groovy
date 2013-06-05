package com.vpundit.www.custom

import java.util.UUID

import org.apache.log4j.Logger;

class TreeNode
{
	String Value
	ArrayList<TreeNode> Children
	String Id
	int X
	int Y
	TreeNode Parent
	int ParentX
	int ParentY
	int MyDepth
	int Radius
	String Weight
	String Info
	private def log

		
	/**
	 * this one is used by JSONParserService
	 * @param radius
	 * the radius to be used for drawing the node
	 */
	TreeNode(int radius)
	{
		Radius = radius
		Children = new ArrayList<TreeNode>()
	}

	/**
	 * @param value 
	 * the value to be held by the node
	 * @param parent 
	 * the TreeNode, which is parent to this node
	 * @param myDepth 
	 * the tree depth where this node will fall in
	 * --I'm not sure this is really needed anymore, or if we can't get away without the depth -- Ivan
	 * --I know it's still being used though
	 * @param radius 
	 * the raidus to be used by drawing the node
	 * @param weight 
	 * the weight that will be used when drawing the node. It describes the weight of this particular
	 * attribute when calculating output or input
	 * @param info
	 * textual description describing the node
	 */
	TreeNode(String value, TreeNode parent, int myDepth, int radius, String weight, String info)
	{
		//log = org.apache.commons.logging.LogFactory.getLog(TreeNode)
		Weight = weight
		MyDepth = myDepth

		Id = UUID.randomUUID().toString()

		Value = value
		Children = new ArrayList<TreeNode>()
		Parent = parent
		Radius = radius
		Info = info
		if(parent != null)
		{
			ParentX = parent.X
			parentY = parent.Y
		}
	}

	/**
	 * @param value
	 * @return
	 */
	def AddChild(def value, String weight, String info)
	{
		TreeNode child = new TreeNode(value, this, this.MyDepth + 1, Radius, weight, info)
		if(Children.size > 0)
		{
			child.X = Children.get(Children.size - 1).X + 3*Radius
		}
		else
		{
			child.X = this.X - 2*Radius
		}
		child.Y = this.Y + 4 * Radius
		this.Children.add(child)
		this.Children.sort
		{it.X}

		child
	}

	def AddChild(TreeNode child)
	{
		this.Children.add(child)
		this.Children.sort
		{it.X}
	}
	
	@Override
	public String toString()
	{
		String temp = " Value: " + Value + ", Id: "+ Id + ", X: " + X + ", Y: " + Y + ", Parent: " + Parent?.Id + ", ParentX: " + ParentX + ", ParentY: " + ParentY + 
		", MyDepth: "+ MyDepth + ", Radius: " + Radius + ", Weight: " + Weight + ", Info: " + Info 
		return super.toString() + temp;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof TreeNode)
		{
			if(Parent?.Id != obj?.Parent?.Id)
			{
				return false
			}
			else if(Children?.size != obj.Children?.size || !Id.equals(obj.Id) || !Value.equals(obj.Value) || X != obj.X || Y != obj.Y || 
				ParentX != obj.ParentX || ParentY != obj.ParentY || MyDepth != obj.MyDepth || Radius != obj.Radius || 
				Weight != obj.Weight || !Info.equals(obj.Info) )
			{
				return false
			}
			else
			{
				for(int i = 0; i < Children?.size; i++)
				{
					log?.debug("comparing children: " + Children[i]?.toString() + " and "+ obj.Children[i]?.toString())
					if(!Children[i].equals(obj.Children[i]))
					{
						return false;
					}
				}
				
				return true;
			}
			
		}
		else
		{
			return false
		}
	}
	
	public TreeNode MakeCopy()
	{
		def newNode = new TreeNode(Value, Parent, MyDepth, Radius, Weight, Info)
		newNode.Id = Id
		newNode.Children = new ArrayList<TreeNode>(Children)
		newNode
	}
}
