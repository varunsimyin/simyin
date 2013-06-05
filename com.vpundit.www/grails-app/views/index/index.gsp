
<%@ page import="com.vpundit.www.Index"%>
<!DOCTYPE HTML>
<html>

<head>

<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'index.label', default: 'Index')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
<g:javascript src="raphael-min.js" />
<g:javascript library="jquery" />
<g:javascript src="tree.js" />


	<g:hiddenField name="treeField" value="${treeField}" />

	<!-- the html element where to put the paper -->
	<button id="fixbutton">fix tree overlaps</button>
	</br> Select file:

	<g:form action="submit" method="post" enctype="multipart/form-data">
		<input type="file" name="file" />
		<input type="submit" />
	</g:form>
	<br>
	<label>
		<!--  ${treeLegend} --> 
	</label>
	
	<div id="paper1"></div>
	<div id="tip" class="tip"></div>
	<g:javascript>




	var paper = Raphael("paper1", "4000", "4000");
	paper.safari();
	var hiddenVal = document.getElementById('treeField').value;
	if(hiddenVal != "")
	{
		var root_node = eval("(" + document.getElementById('treeField').value + ")");
	
		var radius = get_radius();
	
		paper.clear();
		draw_tree(root_node);
	}
	//draw_rectangles();

		$(document).ready(function() {
		$("#fixbutton").click(function() {
			var data = {
				tree : JSON.stringify(root_node)
			};
			$.ajax({
				type : "POST",
				url : "${request.contextPath}/index/fixoverlaps",
				dataType : 'json',
				data : data,
				success : function(data) {
					paper.clear();
					var json = JSON.stringify(data);
					document.getElementById('treeField').setAttribute('value', json);
					root_node = data;
					draw_tree(data);
					draw_rectangles();
	
				},
				error : function(request, status, error) {
					alert("error fixing tree " + error);
				}
			});
		});
	});
	

	function get_tree() {

		$.ajax({
			url : "${request.contextPath}/index/getTree",
			dataType : 'json',
			success : function(data) {
	
				var json = JSON.stringify(data);
				document.getElementById('treeField').setAttribute('value', json);
				root_node = data;
				draw_tree(data);
	
			},
			error : function(request, status, error) {
				alert("error getting tree");
			}
		});
	};
	


	function get_radius() {
		var result;
		$.ajax({
			url : "${request.contextPath}/index/getRadius",
			dataType : 'text',
			success : function(data) {
				result = parseInt(data);
			},
			error : function(request, status, error) {
				alert("error getting radius");
			},
			async : false
		});
		return result;
	} 

	</g:javascript>

</body>
</html>
