package com.vpundit.www

import com.vpundit.www.custom.TreeNode
import grails.converters.JSON

class ProductController 
{
       def show() {
		   
		   TreeNode tree = new TreeNode(10)
		   println "id is " + params.id
		   
        if (params.id =="1") {
			tree.Id = "id_test"
            render tree as JSON
        }
        else {

			tree.Id = "id_else"
			render tree as JSON
        }
    }
}
