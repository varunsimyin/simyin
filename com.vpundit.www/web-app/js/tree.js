/**
 * @author Ivan Alagenchev
 */

function draw_rectangles() {
	var rect1;
	var rect2;
	var sideLength = 10;
	rect1 = paper.rect(500, 30, sideLength, sideLength).attr({
		fill : "orange",
		cursor : "move"
	});
	rect1.id = "myrect";
	rect1.data("originalX", 500);
	rect1.data("originalY", 30);

	rect1.drag(onmove, onstart, onend);

	rect2 = paper.rect(500, 50, sideLength, sideLength).attr({
		fill : "red",
		cursor : "move"
	});
	rect2.id = "myrect2";
	rect2.data("originalX", 500);
	rect2.data("originalY", 50);

	rect2.drag(onmove, onstart, onend);

};

function addTip(circle, txt) {

	$(circle.node).hover(function() {
		$("#tip").text(txt);
		$("#tip").fadeIn();
		$("#tip").offset({
			"position" : "absolute",
			top : (parseInt(circle.attr("cy")) + 250),
			left : (parseInt(circle.attr("cx")) - 100)
		});
	}, function() {
		$("#tip").fadeOut(200);
	});
}

function draw_tree(node) {

	if (node == null) {
		return;
	}

	paper.text(node.x, node.y, node.value);
	var circle = paper.circle(node.x, node.y, radius);
	circle.attr("fill", "white");
	circle.attr("fill-opacity", "0");
	circle.attr("stroke", "black");
	circle.id = node.id;
	circle.drag(circle_onmove, circle_onstart, circle_onend);

	if (node.info != "") {
		addTip(circle, node.info);
	}
	
	if (node.parent != null) {
		var parentY = node.parentY + radius;
		var myY = node.y - radius;

		var parm = "M" + node.parentX + "," + parentY + "L" + node.x + "," + myY + "\"";
		paper.path(parm);
		paper.text((node.parentX + node.x) / 2, (node.parentY + node.y) / 2, node.weight)
	}
	$.each(node.children, function(index, child) {
		draw_tree(child);
	});

};

var circle_onmove = function(dx, dy) {

	this.attr({
		cx : (this.ox + dx),
		cy : (this.oy + dy)
	});

};

var circle_onstart = function() {
	this.ox = this.attr("cx");
	this.oy = this.attr("cy");
};

var circle_onend = function() {

	var endX = this.attr("cx");
	var endY = this.attr("cy");
	paper.clear();
	setNewCircleLocation(this.id, endX, endY, root_node);
	draw_tree(root_node);
	draw_rectangles();

};

function setNewCircleLocation(circleId, endX, endY, root) {
	if (root.id == circleId) {
		root.x = endX;
		root.y = endY;

		$.each(root.children, function(index, child) {
			child.parentX = endX;
			child.parentY = endY;
		});

		var json = JSON.stringify(root_node);
		document.getElementById('treeField').setAttribute('value', json);
	} else {
		$.each(root.children, function(index, child) {
			setNewCircleLocation(circleId, endX, endY, child);
		});

	}

};

var onmove = function(dx, dy) {

	this.attr({
		x : (this.ox + dx),
		y : (this.oy + dy)
	});

};

var onstart = function() {
	this.ox = this.attr("x");
	this.oy = this.attr("y");
};

var onend = function() {

	var endX = this.attr("x");
	var endY = this.attr("y");

	var treeFieldJSON = document.getElementById('treeField').value;
	var treeField = eval("(" + treeFieldJSON + ")");

	var id = getCircleIdForDrop(endX, endY, treeField);

	if (id == null) {
		return;
	}

	var circle = paper.getById(id);

	var circleX = circle.attr("cx");
	var circleY = circle.attr("cy");
	var radius = circle.attr("r");

	if (endX - sideLength < circleX + radius && endX + sideLength > circleX - radius && endY - sideLength < circleY + radius && endY + sideLength > circleY - radius) {
		var newColor = this.attr("fill");
		circle.attr("fill", newColor);
		var originalX = this.data("originalX");
		var originalY = this.data("originalY");
		this.attr("x", originalX);
		this.attr("y", originalY);
	}

};

function getCircleIdForDrop(dropX, dropY, node) {

	if (node == null) {
		return null;
	}

	//go through each node from the tree and see if x and y are within any nodes

	if (dropX < (node.x + radius) && dropX > (node.x - radius) && dropY < (node.y + radius) && dropY > (node.y - radius)) {
		return node.id;
	}

	var left = getCircleIdForDrop(dropX, dropY, node.leftChild);
	if (left != null) {
		return left;
	}
	var right = getCircleIdForDrop(dropX, dropY, node.rightChild);

	if (right != null) {
		return right;
	}

	return null;
};