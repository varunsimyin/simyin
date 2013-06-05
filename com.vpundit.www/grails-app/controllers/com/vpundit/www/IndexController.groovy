package com.vpundit.www


import javassist.runtime.Cflow.Depth
import org.codehaus.groovy.grails.web.json.JSONObject
//import org.springframework.web.servlet.ModelAndView
import com.fasterxml.jackson.databind.ObjectMapper

import com.vpundit.www.custom.*
import grails.converters.*


class IndexController
{
	def treeService
	def JSONParserService
	def XMLService
	def configurationService
	def grailsApplication

	def index()
	{
	}

	def submit()
	{
		log.error("test ivan")
		def file = request.getFile("file")
		def xml = file.inputStream.text
		def reasoning = XMLService.parseXML(xml)
		def tree = reasoning.node.asTree()
		def jsonTree = tree.Root as JSON
		println jsonTree.toString()
		render (view: "index", model: [treeField: jsonTree, treeLegend : tree.Info.Legend])
		
	}
	def fixoverlaps()
	{
		def parm = params
		def reque = request.JSON
		String jsonTree = JSON.parse(params.tree)

		TreeNode tree = JSONParserService.JSONToTree(jsonTree,null)

		treeService.makeSymmetric(tree)

		def overlap = treeService.findRootOverlaps(tree, tree)

		while(overlap != null)
		{
			treeService.spreadChildrenApart(overlap)

			overlap = treeService.findRootOverlaps(tree, tree)
		}


		render tree as JSON
	}

	def getRadius()
	{
		def radius = grailsApplication.config.frontend.JSONTree.radius
		render radius
	}
}
