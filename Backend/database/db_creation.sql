CREATE TABLE IF NOT EXISTS `user` (
	`id` int AUTO_INCREMENT NOT NULL UNIQUE,
	`email` varchar(128) NOT NULL,
	`password_hash` varchar(255) NOT NULL,
	`first_name` varchar(64) NOT NULL,
	`surname` varchar(64) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `artefact` (
	`id` int AUTO_INCREMENT NOT NULL UNIQUE,
	`name` varchar(64) NOT NULL,
	`description` varchar(512) NOT NULL,
	`quest_id` int NOT NULL,
	`image_url` varchar(254) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `quest` (
	`id` int AUTO_INCREMENT NOT NULL UNIQUE,
	`title` varchar(32) NOT NULL,
	`description` varchar(256) NOT NULL,
	`long_description` varchar(1024),
	`complete_description` varchar(1024) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `objective` (
	`id` int AUTO_INCREMENT NOT NULL UNIQUE,
	`latitude` double NOT NULL,
	`longitude` double NOT NULL,
	`name` varchar(64) NOT NULL,
	`title` varchar(64) NOT NULL,
	`description` varchar(1024) NOT NULL,
	`image_url` varchar(128),
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `artefact_user` (
	`id` int AUTO_INCREMENT NOT NULL UNIQUE,
	`artefact_id` int NOT NULL,
	`user_id` int NOT NULL,
	`collected_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `quest_objective` (
	`id` int AUTO_INCREMENT NOT NULL UNIQUE,
	`quest_id` int NOT NULL,
	`objective_id` int NOT NULL,
	`sequence` int NOT NULL,
	PRIMARY KEY (`id`)
);


ALTER TABLE `artefact` ADD CONSTRAINT `artefact_fk3` FOREIGN KEY (`quest_id`) REFERENCES `quest`(`id`);


ALTER TABLE `artefact_user` ADD CONSTRAINT `artefact_user_fk1` FOREIGN KEY (`artefact_id`) REFERENCES `artefact`(`id`);

ALTER TABLE `artefact_user` ADD CONSTRAINT `artefact_user_fk2` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`);
ALTER TABLE `quest_objective` ADD CONSTRAINT `quest_objective_fk1` FOREIGN KEY (`quest_id`) REFERENCES `quest`(`id`);

ALTER TABLE `quest_objective` ADD CONSTRAINT `quest_objective_fk2` FOREIGN KEY (`objective_id`) REFERENCES `objective`(`id`);
