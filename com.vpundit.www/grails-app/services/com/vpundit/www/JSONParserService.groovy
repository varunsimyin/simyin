package com.vpundit.www

import com.vpundit.www.custom.TreeNode
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MappingJsonFactory
import com.fasterxml.jackson.databind.util.TokenBuffer.Parser
import grails.converters.*

class JSONParserService {

	def configurationService
	def grailsApplication

	
	/**
	 * converts a json string to its equivalent tree representation
	 * @param json
	 * the json string to convert to a tree
	 * @param parentNode
	 * the parent of the newly created tree
	 * @return
	 */
	def JSONToTree(String json, TreeNode parentNode) {
		JsonFactory factory = new MappingJsonFactory()
		JsonParser parser = factory.createJsonParser(json)

		TreeNode currentNode
		def start = parser.nextToken() // will return JsonToken.START_OBJECT (verify?)

		if(start == JsonToken.START_OBJECT){
			def radius = grailsApplication.config.frontend.JSONTree.radius
			currentNode = new TreeNode(radius)
		}
		else{
			return null
		}

		while (parser.nextToken() != JsonToken.END_OBJECT) {
			String fieldname = parser.getCurrentName()

			if(fieldname == null){
				return null
			}

			switch (fieldname){
				case "radius":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					def radius = parser.getIntValue()
					currentNode.Radius = radius
					break
				case "x":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					def x = parser.getIntValue()
					currentNode.X = x
					break
				case "y":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					def y = parser.getIntValue()
					currentNode.Y = y
					break
				case "parentX":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					def parentX = parser.getIntValue()
					currentNode.ParentX = parentX
					break
				case "parentY":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					def parentY = parser.getIntValue()
					currentNode.ParentY = parentY
					break
				case "id":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					currentNode.Id = parser.getText()
					break
				case "myDepth":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					currentNode.MyDepth = parser.getIntValue()
					break
				case "order":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					currentNode.Order = parser.getIntValue()
					break
				case "parent":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					JsonNode node = parser.readValueAsTree()
					if(node != null){
						currentNode.Parent = parentNode
					}
					break
				case "value":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					def val = parser.getText()
					currentNode.Value = val
					break
				case "info":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					def val = parser.getText()
					currentNode.Info = val;
					break
				case "weight":
					parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					def val = parser.getText()
					currentNode.Weight = val
					break;
				case "children":
					def token = parser.nextToken() // move to value, or START_OBJECT/START_ARRAY
					if (token == JsonToken.START_ARRAY) {
						def nextToken = parser.nextToken()
						// For each of the records in the array
						boolean exitLoop = false
						while (!exitLoop && nextToken != JsonToken.END_ARRAY) {
							// read the record into a tree model,
							// this moves the parsing position to the end of it
							JsonNode node = parser.readValueAsTree()
							if(node == null){
								exitLoop = true
							}
							else
							{
								String newJson = node.toString()
								TreeNode child = JSONToTree(newJson, currentNode)
								currentNode.Children.add(child)
							}

							nextToken = parser.nextToken()
						}
					}
					break
				default:
					break

			}
		}
		parser.close() // ensure resources get cleaned up timely and properly

		return currentNode
	}
}
