-- The rental_listings table
CREATE TABLE `rental_listings` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`url` VARCHAR(528) DEFAULT NULL, 
	`html` TEXT DEFAULT NULL, 
	`created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00', 
	`updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
	`creator_id` int(11) DEFAULT NULL, 
	`updater_id` int(11) DEFAULT NULL, 
	PRIMARY KEY (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
