-- The users table
CREATE TABLE users (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`email` VARCHAR(255) DEFAULT NULL, 
	`first_name` VARCHAR(255) DEFAULT NULL, 
	`last_name` VARCHAR(255) DEFAULT NULL, 
	`password` BINARY(60) DEFAULT NULL, 
	`activated` TINYINT(1) DEFAULT NULL, 
	`email_token` VARCHAR(32) DEFAULT NULL, 
	`created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00', 
	`updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
	`creator_id` int(11) DEFAULT NULL, 
	`updater_id` int(11) DEFAULT NULL, 
	PRIMARY KEY (id), 
	UNIQUE KEY `uk_users_email` (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
