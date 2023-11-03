CREATE DATABASE  IF NOT EXISTS `poseidon`;

USE `poseidon`;

DROP TABLE IF EXISTS `bidlist`;
CREATE TABLE `bidlist` (
  `bid_list_id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(30) DEFAULT NULL,
  `ask` double DEFAULT NULL,
  `ask_quantity` double DEFAULT NULL,
  `benchmark` varchar(100) DEFAULT NULL,
  `bid` double DEFAULT NULL,
  `bid_list_date` tinyblob,
  `bid_quantity` double DEFAULT NULL,
  `book` varchar(100) DEFAULT NULL,
  `commentary` varchar(100) DEFAULT NULL,
  `creation_date` tinyblob,
  `creation_name` varchar(100) DEFAULT NULL,
  `deal_name` varchar(100) DEFAULT NULL,
  `deal_type` varchar(100) DEFAULT NULL,
  `revision_date` tinyblob,
  `revision_name` varchar(100) DEFAULT NULL,
  `security` varchar(100) DEFAULT NULL,
  `side` varchar(100) DEFAULT NULL,
  `source_list_id` varchar(100) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `trader` varchar(100) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`bid_list_id`)
) ENGINE=InnoDB;

INSERT INTO bidlist(account, type, bid_quantity)
VALUES ('Account1', 'Type1', 12.55),
       ('Account2', 'Type2', 13.55),
       ('Account3', 'Type3', 15.55);

DROP TABLE IF EXISTS `curvepoint`;
CREATE TABLE `curvepoint` (
  `id` int NOT NULL AUTO_INCREMENT,
  `as_of_date` datetime(6) DEFAULT NULL,
  `curve_id` int NOT NULL,
  `term` double NOT NULL,
  `value` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO curvepoint(curve_id, term, value)
VALUES (5,25.5,31.3),(6,28.7,41.2),(7,38.7,54.5);

DROP TABLE IF EXISTS `rating`;
CREATE TABLE `rating` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fitch_rating` varchar(100) DEFAULT NULL,
  `moodys_rating` varchar(100) DEFAULT NULL,
  `order_number` int DEFAULT NULL,
  `sand_p_rating` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO rating(moodys_rating, sand_p_rating, fitch_rating, order_number)
VALUES ('Moodys Rating1','Sand P Rating1','Firch rating1', 11),
       ('Moodys Rating2','Sand P Rating2','Firch rating2',22),
       ('Moodys Rating3','Sand P Rating3','Firch rating2',33);

DROP TABLE IF EXISTS `rulename`;
CREATE TABLE `rulename` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(100) DEFAULT NULL,
  `json` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `sql_part` varchar(100) DEFAULT NULL,
  `sql_str` varchar(100) DEFAULT NULL,
  `template` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO rulename(name, description, json, template, sql_str, sql_part) VALUES
('Name1','Description1','Json1','Template1','SqlStr1','SqlPart1'),
('Name2','Description2','Json2','Template2','SqlStr2','SqlPart2'),
('Name3','Description3','Json3','Template3','SqlStr3','SqlPart3');

DROP TABLE IF EXISTS `trade`;
CREATE TABLE `trade` (
  `trade_id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(50) DEFAULT NULL,
  `benchmark` varchar(255) DEFAULT NULL,
  `book` varchar(255) DEFAULT NULL,
  `buy_price` double DEFAULT NULL,
  `buy_quantity` double NOT NULL,
  `creation_date` tinyblob,
  `creation_name` varchar(255) DEFAULT NULL,
  `deal_name` varchar(255) DEFAULT NULL,
  `deal_type` varchar(255) DEFAULT NULL,
  `revision_date` tinyblob,
  `revision_name` varchar(255) DEFAULT NULL,
  `security` varchar(255) DEFAULT NULL,
  `sell_price` double DEFAULT NULL,
  `sell_quantity` double DEFAULT NULL,
  `side` varchar(255) DEFAULT NULL,
  `source_list_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `trade_date` tinyblob,
  `trader` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`trade_id`)
) ENGINE=InnoDB;

INSERT INTO trade(account, type, buy_quantity)
VALUES('Account1','Type1',11.5),
      ('Account2','Type2',22.5),
      ('Account3','Type3',33.5);


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `users`(full_name, password, role, user_name) VALUES
('John Fox','$2a$12$jquteE.jyY2Zf.Z0ZyFIUu3StAfJaOwj69/3Xd0pKgSmifyPjU9dy','ADMIN','john'),
('Ronda Perl','$2a$12$jquteE.jyY2Zf.Z0ZyFIUu3StAfJaOwj69/3Xd0pKgSmifyPjU9dy','ADMIN','ronda'),
('Tony Fox','$2a$12$JPdmDKdLt1fICDeygtUqdORnWAtzhDv/64ogy6UHsO7dxDXpegi/i','USER','tony'),
('Adam Foster','$2a$12$JPdmDKdLt1fICDeygtUqdORnWAtzhDv/64ogy6UHsO7dxDXpegi/i','USER','adam'),
('Gordon Moore','$2a$12$JPdmDKdLt1fICDeygtUqdORnWAtzhDv/64ogy6UHsO7dxDXpegi/i','USER','gordon');
