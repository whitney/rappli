(function () {

	"use strict";

	var RAPPLI_HTML = '<span class="state"></span>'
			+ '<a href="#" class="rappli-add-item">+</a> / '
			+ '<a href="#" class="rappli-remove-item">-</a>',

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
			console.log(node.nodeType);
			if (node.tagName !== "A") {
				postDate = trim(node.nodeValue)
						.replace(/\s-/, '');
			}
			return postDate;
		}

		return Listing;
	})();

	function rappliMouseOver() {
		var el = this;
		el.style.background = "#d6d6d6";
	}

	function rappliMouseOut() {
		this.style.background = "none";
	}

	function processListing() {
		var listing = new Listing(this);
		console.log(listing);
	}

	function removeListing() {
		alert ('removed');
	}

	function rapplify(el) {
		var t = document.createElement("div");
			t.className = "rappli-helper";
			t.innerHTML = RAPPLI_HTML;
			t.style.float = "right";

		el.onmouseover = rappliMouseOver;
		el.onmouseout = rappliMouseOut;
		// Bind click event to "add-listing"
		t.childNodes[1].onclick = processListing;
		// Bind click even to "remove-listing"
		t.childNodes[3].onclick = removeListing;
		el.appendChild(t)
	}

	function collectListings() {
		var listings = document.getElementsByTagName("p");
		for (var i = 0; i < listings.length; i++) {
			rapplify(listings[i]);
		}
	}

	collectListings();

})();
