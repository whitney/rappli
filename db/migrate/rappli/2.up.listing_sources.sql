-- The listing_sources table
CREATE TABLE listing_sources (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) DEFAULT NULL, 
	`code` VARCHAR(255) DEFAULT NULL, 
	PRIMARY KEY (id), 
	UNIQUE KEY `uk_listing_sources_code` (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
