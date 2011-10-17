-- The unauthed_unauthed_users_listings table
CREATE TABLE `unauthed_users_listings` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`IPv4_address` INT UNSIGNED DEFAULT NULL, 
	`rental_listing_id` INT(11) DEFAULT NULL, 
	`created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00', 
	`creator_id` int(11) DEFAULT NULL, 
	`updater_id` int(11) DEFAULT NULL, 
	PRIMARY KEY (id),
	CONSTRAINT `fk_unauthed_users_listings_rental_listing_id` FOREIGN KEY (rental_listing_id) REFERENCES rental_listings (id) ON DELETE CASCADE,
	UNIQUE user_listing (IPv4_address, rental_listing_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
