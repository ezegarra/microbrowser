CREATE DATABASE  IF NOT EXISTS `ezegarraDB` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `ezegarraDB`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: mysql.cs.pitt.edu    Database: ezegarraDB
-- ------------------------------------------------------
-- Server version	5.0.45-community-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Not dumping tablespaces as no INFORMATION_SCHEMA.FILES table on this server
--

--
-- Table structure for table `mp_user_category_map`
--

DROP TABLE IF EXISTS `mp_user_category_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mp_user_category_map` (
  `userid` int(10) NOT NULL,
  `categoryid` int(10) NOT NULL,
  PRIMARY KEY  (`userid`,`categoryid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='A mapping between users and categories';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mp_user_category_map`
--

LOCK TABLES `mp_user_category_map` WRITE;
/*!40000 ALTER TABLE `mp_user_category_map` DISABLE KEYS */;
INSERT INTO `mp_user_category_map` VALUES (1,3),(1,4),(1,6),(2,3),(2,4),(3,3),(3,6),(4,3),(5,3),(6,3),(7,3),(8,3),(9,3),(10,3),(11,3),(12,3),(13,3),(14,3),(15,3),(16,3),(17,3),(17,6),(18,3),(19,3),(20,3),(21,3),(22,3),(23,3),(23,6),(24,3),(25,3),(26,3),(27,3),(27,6),(28,3),(29,3),(30,3),(30,6),(31,3),(32,3),(33,4),(34,3),(34,4),(35,3),(36,4),(37,4),(38,4),(38,6),(39,4),(40,3),(40,4),(41,3),(41,6),(42,3),(43,3),(43,4),(44,4),(45,4),(46,3),(47,3),(48,3),(49,3),(50,4),(51,3),(51,4),(51,6),(52,3),(53,3),(53,6),(55,3),(56,3),(57,3),(64,6),(70,6),(71,6),(72,6),(73,6),(74,6),(75,6),(76,6),(77,6),(78,6),(79,6),(80,6),(81,6),(82,6),(83,6),(84,6),(85,6),(86,6),(87,6),(88,6),(89,6),(90,6),(91,6);
/*!40000 ALTER TABLE `mp_user_category_map` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:25:17
