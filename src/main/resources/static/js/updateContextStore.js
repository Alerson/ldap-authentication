$(function() {
	var attributeCounter = 0;

	$('#main').on('click', '.add-same-level', function(e) { 
		e.preventDefault();
		
		console.log("ADD SAME LEVEL");
		
		var link = $(this);
		
		var article = link.closest("article");
		console.log(article);
		
		var fromLevel = article.attr('data-cs-level');
		var fromInputNumber = article.attr('data-input-number');
		
		console.log("Level: " + fromLevel + ", Input Number: " + fromInputNumber);
		
		var level = parseInt(fromLevel) - 1;
		var nextInputNumber = parseInt(fromInputNumber) + 1;
		
		var inputNumber = article.parent().closest("article").attr('data-input-number');
		
		console.log(fromLevel + ", " + nextInputNumber + ", " + level + ", " + inputNumber);
		
		addCSLevelAtFrom(fromLevel, nextInputNumber, level, inputNumber);
		
		addSameLevelLinksMaintainer(link);
	});
	
	$('#main').on('click', '.add-next-level', function(e) { 
		e.preventDefault();
		
		console.log("ADD NEXT LEVEL");
		
		var link = $(this);
		var article = link.closest("article");
		console.log(article);
		
		var fromLevel = article.attr('data-cs-level');
		var fromInputNumber = article.attr('data-input-number');
		
		console.log("Level: " + fromLevel + ", Input Number: " + fromInputNumber);
		
		var nextLevel = parseInt(fromLevel) + 1;
		var nextInputNumber = parseInt(fromInputNumber) + 1;
		
		addCSLevelAtFrom(nextLevel, 1, fromLevel, fromInputNumber);
		
		addNextLevelLinksMaintainer(link);
	});
	
	$('#main').on('click', '.remove-input', function(e) {
		e.preventDefault();
		
		console.log("REMOVE INPUT");
		
		var link = $(this);
		var article = link.closest("article");
		var parentArticle = article.parent().closest("article");
		var level = article.attr('data-cs-level');
		
		sameLevelInputs = $("[data-cs-level=" + level + "]");
		var numberOfInputs = sameLevelInputs.length;
		var lastInput = sameLevelInputs.last();
		
		article.remove();
		
		// Check if we are removing the last input
		if(lastInput.attr('id') == article.attr('id')) {
			readdSameLevelLink(article);
		}
		
		if(numberOfInputs <= 1) {
			readdNextLevelLink(parentArticle);
		}
	});
	
	$('#main').on('click', '.add-attributes', function(e) {
		e.preventDefault();
		
		attributeCounter = attributeCounter + 1;
		
		console.log("ADD ATTRIBUTES");
		
		var article = $(this).closest("article");
		console.log(article);
		
		var fromLevel = article.attr('data-cs-level');
		var fromInputNumber = article.attr('data-input-number');
		
		console.log("Level: " + fromLevel + ", Input Number: " + fromInputNumber);
		
		addCSAttributesAtFrom(fromLevel, fromInputNumber);
	});
	
	$('#main').on('click', '.remove-attribute', function(e) {
		e.preventDefault();
		
		console.log("REMOVE ATTRIBUTE");
		
		var article = $(this).closest("article");
		
		article.remove();
	});
	
	$("#submit-update-cs").click(function(e) {
		e.preventDefault();
		
		console.log("SUBMIT UPDATE CS");
		
		var articleLevel1 = $('#level-1-input-1');
				
		// level = 1
		var attributes = [];
		attributes = populateAttributesFromLevel(articleLevel1, attributes);
	
	});
	
	function populateAttributesFromLevel(level, attributes) {
		
		if(level.children('article').length == 0) {
			return attributes;
		}
		
		level.children('article').each(function(index, level) {
			attributes.push(level, addAttributes(level, attributes));
		});
	}
	
	function addAttributes(level, attributes) {
		var attrArea = level.children('.attributes-area').first();
		
		attrArea.children('.attribute-container').each(function(index, attribute) {
			var attrObj = {
				name	:	attribute.find('.attribute-name-field').first();
				value	:	attribute.find('.attribute-value-field').first()
			}
			
			attributes.push(attrObj);
		});
		
		return attributes;
	}
	
	function addNextLevelLinksMaintainer(link) {
		link.remove();
	}
	
	function addSameLevelLinksMaintainer(link) {		
		link.remove();
	}
	
	function readdSameLevelLink(article) {
		var level = article.attr('data-cs-level');
		
		console.log("AOPA");
		lastInput = $("[data-cs-level=" + level + "]").last();

		var template = $.templates("#template-add-same-level-link");
		var htmlOutput = template.render(null);
		lastInput.find('.add-same-level-container').first().append(htmlOutput);
	}
	
	function readdNextLevelLink(parentArticle) {
		linkContainer = parentArticle.children('.add-links-container').first().children('.add-next-level-container').first();
		
		var template = $.templates("#template-add-next-level-link");
		var htmlOutput = template.render(null);
		linkContainer.append(htmlOutput);
	}
	
	function addCSLevelAtFrom(level, inputNumber, fromLevel, fromInputNumber) {
		var level = parseInt(level);
		var inputNumber = parseInt(inputNumber);
		var fromLevel = fromLevel.toString();
		var fromInputNumber = fromInputNumber.toString();
		
		var data = {
			levelNumber: level,
			inputNumber: inputNumber,
			offsetQty: level-1
		};
		
		var template = $.templates("#template-level");
		
		var htmlOutput = template.render(data);
		
		$("#level-" + fromLevel + "-input-" + fromInputNumber).append(htmlOutput);
	}
	
	function addCSAttributesAtFrom(level, inputNumber) {
		var level = parseInt(level);
		var inputNumber = parseInt(inputNumber);
		
		var data = {
			levelNumber: level,
			inputNumber: inputNumber,
			offsetQty: level-1,
			counter: attributeCounter
		};
		
		var template = $.templates("#template-attribute");
		
		var htmlOutput = template.render(data);
		
		$("#level-" + level + "-input-" + inputNumber + "-attributes").append(htmlOutput);
	}
	
	function getValuesToBeUpdated() {
		
		$("#level-1-input-1").find('.level-container').each(function(index) {
			console.log(index + " - Level: " + $(this).attr('data-cs-level') + ", Input: " + $(this).attr('data-input-number'));
		});
		
		
	}
	
});