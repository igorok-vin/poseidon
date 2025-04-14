CREATE DATABASE  IF NOT EXISTS `poseidon` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `poseidon`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: poseidon
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bidlist`
--

DROP TABLE IF EXISTS `bidlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bidlist`
--

LOCK TABLES `bidlist` WRITE;
/*!40000 ALTER TABLE `bidlist` DISABLE KEYS */;
INSERT INTO `bidlist` VALUES (4,'Account2',NULL,NULL,NULL,NULL,NULL,17.14,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Type2'),(5,'Account3',NULL,NULL,NULL,NULL,NULL,13,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Type2'),(6,'Account8',NULL,NULL,NULL,NULL,NULL,18.5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Type3');
/*!40000 ALTER TABLE `bidlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curvepoint`
--

DROP TABLE IF EXISTS `curvepoint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `curvepoint` (
  `id` int NOT NULL AUTO_INCREMENT,
  `as_of_date` datetime(6) DEFAULT NULL,
  `curve_id` int NOT NULL,
  `term` double NOT NULL,
  `value` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curvepoint`
--

LOCK TABLES `curvepoint` WRITE;
/*!40000 ALTER TABLE `curvepoint` DISABLE KEYS */;
INSERT INTO `curvepoint` VALUES (5,NULL,7,0.08,0.07),(6,NULL,3,0.06,0.07),(7,NULL,2,0.01,0.02);
/*!40000 ALTER TABLE `curvepoint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rating` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fitch_rating` varchar(100) DEFAULT NULL,
  `moodys_rating` varchar(100) DEFAULT NULL,
  `order_number` int DEFAULT NULL,
  `sand_p_rating` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
INSERT INTO `rating` VALUES (1,'Firch rating1','Moodys Rating1',11,'Sand P Rating1'),(2,'Firch rating2','Moodys Rating2',15,'Sand P Rating2');
/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rulename`
--

DROP TABLE IF EXISTS `rulename`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rulename` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(100) DEFAULT NULL,
  `json` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `sql_part` varchar(100) DEFAULT NULL,
  `sql_str` varchar(100) DEFAULT NULL,
  `template` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rulename`
--

LOCK TABLES `rulename` WRITE;
/*!40000 ALTER TABLE `rulename` DISABLE KEYS */;
INSERT INTO `rulename` VALUES (1,'Description22','Json22','Name22','SqlPart22','SqlStr22','Template22'),(2,'Description2','Json2','Name2','SqlPart2','SqlStr2','Template2');
/*!40000 ALTER TABLE `rulename` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settings` (
  `key` varchar(128) NOT NULL,
  `value` varchar(1024) NOT NULL,
  `category` varchar(45) NOT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES ('DELETE_PERMISSION','true','PERMISSION'),('UPDATE_PERMISSION','false','PERMISSION');
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trade`
--

DROP TABLE IF EXISTS `trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trade`
--

LOCK TABLES `trade` WRITE;
/*!40000 ALTER TABLE `trade` DISABLE KEYS */;
INSERT INTO `trade` VALUES (1,'Account1',NULL,NULL,NULL,27,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Type27'),(2,'Account2',NULL,NULL,NULL,27.5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Type2');
/*!40000 ALTER TABLE `trade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'John Fox','$2a$12$N8llvvhrDEdsmmciSVpOy.YG.d8RGGTfpgafIwBxyofUz6FTRUD.m','ADMIN','john'),(2,'Ronda Perl','$2a$12$N8llvvhrDEdsmmciSVpOy.YG.d8RGGTfpgafIwBxyofUz6FTRUD.m','USER','ronda'),(3,'Tony Fox','$2a$12$N8llvvhrDEdsmmciSVpOy.YG.d8RGGTfpgafIwBxyofUz6FTRUD.m','USER','tony'),(4,'Adam Foster','$2a$12$N8llvvhrDEdsmmciSVpOy.YG.d8RGGTfpgafIwBxyofUz6FTRUD.m','USER','adam'),(5,'Gordon Moore','$2a$12$N8llvvhrDEdsmmciSVpOy.YG.d8RGGTfpgafIwBxyofUz6FTRUD.m','USER','gordon'),(8,'Igor Nikolaienko','$2a$10$cHqtsuz3oXZgBKUJ2Git/uvX99Lg6gKP/y8VZ0.y12TLpDRZgmqUO','USER','igor');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-13 21:05:20
