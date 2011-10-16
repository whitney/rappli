(function ($) {

	"use strict";

	var WIDGET_HTML = [
			'<div class="rapply-widget">',
			'<span class="state"></span>',
			'<a href="#" class="queue-listing">+</a> / ',
			'<a href="#" class="dequeue-listing">-</a>',
			'</div>'
		].join(""),

		/**
		 * Current date.
		 */
		TODAY = new Date(),

		/**
		 * RegEx for extracting the price from a title.
		 */
		RX_PRICE = /\$([0-9]+)/,

		/**
		 * RegEx for extracting the # of bedrooms.
		 */
		RX_BR = /(\d+)br|BR|bR|Br/,

		/**
		 * RegEx for extracting the # bathrooms.
		 */
		RX_BA = /(\d+)ba|BA|bA|Ba/,

		/**
		 * RegEx for extracting the dimensions.
		 */
		RX_SIZE = /(\d+)ft|FT|fT|Ft/,

		/**
		 * RegEx for extracting the post id from a url.
		 */
		RX_POST_ID = /([0-9]+)\.html/,

		/**
		 * RegEx for extracting the post date.
		 */
		RX_POST_DATE = /\w{3}\s\d+\s-/,

		/**
		 * A channel for communicating with the extension
		 */
		chan = chrome.extension.connect({name: "rapply"}), 

		Listing;

	//======================
	//	Helper Methods
	//======================

	//======================
	//	Classes
	//======================

	Listing = (function () {
		function Listing(element) {
			this.__elem__ = element;
			this.data = {};
			this.extractPostDate();
			this.extractPostTitleAndUrl();
			this.extractPrice();
			this.extractPostId();
			this.extractSpecs();
		}

		Listing.prototype.extractPostTitleAndUrl = function () {
			var title = $('a', this.__elem__)[0];
			this.data.postTitle = title.text;
			this.data.postUrl = title.href;
		}

		Listing.prototype.extractSpecs = function () {
			var br, ba, size;

			if (RX_BR.test(this.data.postTitle)) {
				br = this.data.postTitle.match(RX_BR)[1];
			}

			if (RX_BA.test(this.data.postTitle)) {
				ba = this.data.postTitle.match(RX_BA)[1];
			}

			if (RX_SIZE.test(this.data.postTitle)) {
				size = this.data.postTitle.match(RX_SIZE)[1];
			}

			this.data.specs = {
				'br': br,
				'ba': ba,
				'size': size
			};
		};

		Listing.prototype.extractPrice = function () {
			var price;

			if (RX_PRICE.test(this.data.postTitle)) {
				price = this.data.postTitle.match(RX_PRICE)[1];
			}

			this.data.postPrice = price;
		};

		Listing.prototype.extractPostId = function () {
			var postId;

			if (RX_POST_ID.test(this.data.postUrl)) {
				postId = parseInt(this.data.postUrl.match(RX_POST_ID)[1]);
			}
			this.data.postId = postId;
		}

		// We must assume that if the date is not part of the post text, it is
		// the date in the H4 tag preceding the listing.
		Listing.prototype.extractPostDate = function () {
			var postDate,
				nodes = this.__elem__.childNodes;

			if (nodes[0].nodeType === 3) {
				var dateText = nodes[0].textContent.replace(" - ", "");
				dateText += " " + TODAY.getFullYear();
				dateText = $.trim(dateText);
				postDate = new Date(dateText).getTime();
			} else { 
				var dateText = $(this.__elem__).prevAll('h4').text();
				dateText += " " + TODAY.getFullYear();
				postDate = new Date(dateText).getTime();
			}
			this.data.postDate = postDate;
		};

		return Listing;
	}());

	//======================
	//	Event Handlers
	//======================

	function queueListing(e) {
		var postElem = $(this).parents('p')[0],
			listing = new Listing(postElem), 
			listingJSON = JSON.stringify(listing.data),
			postId = listing.data.postId;

		listing.__elem__.id = listing.data.postId;
		$(postElem).addClass("item-queued");

		console.log(listing);
		sessionStorage.setItem(postId, listingJSON);

		$.ajax({
			url: "http://localhost:9000/craigslist/listings",
			type: "POST",
			dataType: "application/json",
			contentType: "application/json",
			data: listingJSON
		});

		chan.postMessage({action: "newTab"});
		return false;
	}

	function dequeueListing(e) {
		var postElem = $(this).parents('p')[0], 
			postId = postElem.id, 
			listingUrl = JSON.parse(sessionStorage.getItem(postId)).postUrl;

		$.ajax({
			url: "http://localhost:9000/craigslist/listings",
			context: postElem,
			type: "DELETE", 
			data: {url: listingUrl},
			success: function(){
				$(this).removeClass("item-queued");
				sessionStorage.removeItem(postId);
			}
		});
		return false;
	}

	//=====================
	//	Main
	//=====================

	function checkQueue(elem) {
		var postUrl = $('a', elem).attr('href'), 
			urlFragments = postUrl.split('/'), 
			postId;

		postId = parseInt(urlFragments[urlFragments.length - 1]
				.replace(".html", ""));

		return sessionStorage.getItem(postId) ? true : false;
	}

	function rapplify() {
		var posts = $('p[align!=center]');
		$(WIDGET_HTML).appendTo(posts);
		posts.addClass("rapplified");
		$.each(posts, function(k, v) {
			if (checkQueue(v)) {
				$(this).addClass("item-queued");
			}
		});
	}

	$('document').ready(function () {
		rapplify();

		$('a.queue-listing').live('click', queueListing);
		$('a.dequeue-listing').live('click', dequeueListing);
	});

}(jQuery));
