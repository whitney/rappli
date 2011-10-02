(function ($) {

	"use strict";

	var WIDGET_HTML = [
			'<div class="rapply-widget"',
			'<span class="state"></span>',
			'<a href="#" class="queue-listing">+</a> / ',
			'<a href="#" class="dequeue-listing">-</a>',
			'</div>'
		].join(""),

		/**
		 * RegEx for extracting the price from a title.
		 */
		RX_PRICE = /\$([0-9]+)/,

		RX_POST_ID = /([0-9]+)\.html/,

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
			this.extractPostTitleAndUrl();
			this.extractPrice();
			this.extractPostId();
		}

		Listing.prototype.extractPostTitleAndUrl = function () {
			var title = $('a', this.__elem__)[0];
			this.data.postTitle = title.text;
			this.data.postUrl = title.href;
		}

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

		return Listing;
	}());

	//======================
	//	Event Handlers
	//======================

	function queueListing(e) {
		var postElem = $(this).parents('p')[0],
			listing = new Listing(postElem);
		listing.__elem__.id = listing.data.postId;
		$(postElem).addClass("item-queued");
		console.log(listing);
		return false;
	}

	function dequeueListing(e) {
		var postElem = $(this).parent('p');
		$(postElem).removeClass('item-queued');
		return false;
	}

	//=====================
	//	Main
	//=====================

	function rapplify() {
		var posts = $('p[align!=center]');
		$(WIDGET_HTML).appendTo(posts);
		posts.addClass("rapplified");
	}

	$('document').ready(function () {
		rapplify();

		$('a.queue-listing').live('click', queueListing);
		$('a.dequeue-listing').live('click', dequeueListing);
	});

}(jQuery));
