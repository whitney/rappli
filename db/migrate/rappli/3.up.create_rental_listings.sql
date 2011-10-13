-- The rental_listings table
CREATE TABLE `rental_listings` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`url` VARCHAR(528) DEFAULT NULL, 
	`html` TEXT DEFAULT NULL, 
	`price` VARCHAR(32) DEFAULT NULL,
	`listing_source_id` INT(11) DEFAULT NULL, 
	`created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00', 
	`updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
	`creator_id` int(11) DEFAULT NULL, 
	`updater_id` int(11) DEFAULT NULL, 
	PRIMARY KEY (id),  
	CONSTRAINT `fk_rental_listings_listing_source_id` FOREIGN KEY (listing_source_id) REFERENCES listing_sources (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
