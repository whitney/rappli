(function () {

	"use strict";

	var RAPPLI_HTML = '<span class="state"></span>'
			+ '<a href="#" class="rapply-add-item">+</a> / '
			+ '<a href="#" class="rapply-remove-item">-</a>',

		// Open up a channel to communicate to our bg_page
		CHAN = chrome.extension.connect({name: "rapply"}),

		Listing;

	/* Helper Methods */

	function trim(str) {
		var str = str.replace(/^\s\s*/, ''),
			ws = /\s/,
			i = str.length;
		while (ws.test(str.charAt(--i)));
		return str.slice(0, i + 1);
	}

	// @@CLASS: Listing
	Listing = (function () {
		function Listing(el) {
			this._element = el.parentNode.parentNode;
			this._nodes = this._element.childNodes;
			this.price = this._extractPrice(this._element.innerHTML);
			this.postDate = this._extractDate(this._nodes[0]);
		}

		Listing.prototype._extractPrice = function (htmlStr) {
			var PRICE_RX = /\$([0-9]+)/,
				price;

			if (htmlStr.match(PRICE_RX)) {
				price = htmlStr.match(PRICE_RX)[0];
			}
			return price;
		}

		Listing.prototype._extractDate = function (node) {
			var postDate;
			// If it's not a link then it's the post date.
			if (node.tagName !== "A") {
				postDate = trim(node.nodeValue)
						.replace(/\s-/, '');
				postDate = new Date(postDate);
			} else {
				postDate = new Date();
			}
			return postDate;
		}

		return Listing;
	})();

	function rapplyMouseOver(e) {
		var el = this;
		el.style.background = "#d6d6d6";
	}

	function rapplyMouseOut() {
		this.style.background = "none";
	}

	function processListing() {
		var listing = new Listing(this);
		return false;
	}

	function removeListing() {
		alert ('removed');
	}

	function rapplify(el) {
		var widget = document.createElement("div");
			widget.className = "rapply-helper";
			widget.innerHTML = RAPPLI_HTML;
			widget.style.float = "right";

		el.className += "rapplified";
		el.onmouseover = rapplyMouseOver;
		el.onmouseout = rapplyMouseOut;
		// Bind click event to "add-listing"
		widget.childNodes[1].onclick = processListing;
		// Bind click even to "remove-listing"
		widget.childNodes[3].onclick = removeListing;
		el.appendChild(widget)
	}

	function collectListings() {
		var listings = document.getElementsByTagName("p");
		for (var i = 0; i < listings.length; i++) {
			rapplify(listings[i]);
		}
	}

	collectListings();

})();
