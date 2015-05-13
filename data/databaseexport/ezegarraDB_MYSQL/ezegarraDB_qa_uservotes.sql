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
-- Table structure for table `qa_uservotes`
--

DROP TABLE IF EXISTS `qa_uservotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qa_uservotes` (
  `postid` int(10) unsigned NOT NULL,
  `userid` int(10) unsigned NOT NULL,
  `vote` tinyint(4) NOT NULL,
  `flag` tinyint(4) NOT NULL,
  UNIQUE KEY `userid` (`userid`,`postid`),
  KEY `postid` (`postid`),
  CONSTRAINT `qa_uservotes_ibfk_1` FOREIGN KEY (`postid`) REFERENCES `qa_posts` (`postid`) ON DELETE CASCADE,
  CONSTRAINT `qa_uservotes_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `qa_users` (`userid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa_uservotes`
--

LOCK TABLES `qa_uservotes` WRITE;
/*!40000 ALTER TABLE `qa_uservotes` DISABLE KEYS */;
INSERT INTO `qa_uservotes` VALUES (41,2,0,0),(134,2,1,0),(209,7,1,0),(16,8,1,0),(50,8,1,0),(209,8,1,0),(210,8,-1,0),(137,10,0,0),(12,13,1,0),(17,13,1,0),(31,13,0,0),(33,13,0,0),(35,13,1,0),(50,13,1,0),(53,13,1,0),(57,13,1,0),(59,13,1,0),(63,13,1,0),(72,13,1,0),(76,13,1,0),(78,13,1,0),(80,13,1,0),(98,13,1,0),(101,13,1,0),(107,13,1,0),(108,13,1,0),(111,13,1,0),(112,13,1,0),(115,13,1,0),(121,13,1,0),(123,13,1,0),(125,13,1,0),(130,13,1,0),(132,13,1,0),(137,13,1,0),(139,13,1,0),(140,13,1,0),(142,13,1,0),(143,13,1,0),(146,13,1,0),(148,13,1,0),(149,13,1,0),(150,13,1,0),(152,13,-1,0),(153,13,1,0),(166,13,1,0),(169,13,1,0),(171,13,1,0),(173,13,1,0),(174,13,1,0),(179,13,1,0),(181,13,1,0),(183,13,1,0),(185,13,1,0),(186,13,1,0),(189,13,1,0),(191,13,1,0),(192,13,1,0),(193,13,1,0),(196,13,1,0),(153,18,1,0),(209,19,1,0),(210,19,0,1),(216,19,0,1),(194,20,1,0),(16,22,-1,0),(45,23,1,0),(47,23,1,0),(50,23,1,0),(53,23,1,0),(54,23,1,0),(57,23,1,0),(59,23,1,0),(60,23,1,0),(61,23,1,0),(63,23,1,0),(64,23,1,0),(70,23,1,0),(72,23,1,0),(75,23,1,0),(76,23,1,0),(77,23,1,0),(92,23,1,0),(94,23,1,0),(96,23,1,0),(98,23,1,0),(107,23,1,0),(108,23,1,0),(110,23,1,0),(111,23,1,0),(112,23,1,0),(115,23,1,0),(121,23,1,0),(122,23,1,0),(123,23,1,0),(125,23,1,0),(132,23,1,0),(134,23,1,0),(152,23,-1,0),(153,23,1,0),(166,23,1,0),(169,23,1,0),(174,23,1,0),(179,23,1,0),(181,23,1,0),(183,23,1,0),(185,23,1,0),(186,23,1,0),(189,23,1,0),(190,23,1,0),(193,23,1,0),(194,23,1,0),(205,23,1,0),(210,23,0,1),(216,23,0,1),(152,25,-1,0),(84,26,1,0),(209,29,-1,0),(210,29,1,0),(143,30,1,0),(237,30,1,0),(238,30,1,0),(16,31,1,0),(62,33,1,0),(92,33,1,0),(152,35,1,0),(166,35,1,0),(169,35,1,0),(171,35,1,0),(173,35,1,0),(174,35,1,0),(179,35,1,0),(181,35,1,0),(183,35,1,0),(90,36,1,0),(92,36,1,0),(56,39,1,0),(62,39,-1,0),(74,39,1,0),(92,39,1,0),(48,40,1,0),(150,42,0,0),(185,42,1,0),(186,42,1,0),(193,42,1,0),(216,42,-1,0),(217,42,-1,0),(134,45,1,0),(208,46,1,0),(209,46,-1,0),(215,46,1,0),(12,47,1,0),(26,47,1,0),(78,47,1,0),(115,47,1,0),(121,47,1,0),(123,47,1,0),(124,47,1,0),(125,47,1,0),(130,47,1,0),(132,47,1,0),(137,47,1,0),(140,47,1,0),(142,47,1,0),(146,47,1,0),(148,47,1,0),(152,47,1,0),(166,47,1,0),(169,47,1,0),(171,47,1,0),(173,47,1,0),(174,47,1,0),(179,47,1,0),(181,47,1,0),(183,47,1,0),(209,47,0,0),(210,47,0,0),(113,51,1,0),(227,51,1,0),(250,51,1,0),(54,53,1,0),(133,53,1,0),(139,53,1,0),(140,53,0,0),(141,53,1,0),(167,53,1,0),(193,53,1,0),(194,53,-1,0),(227,53,1,0),(130,58,1,0),(130,59,1,0),(209,64,-1,0),(210,64,1,0),(208,65,1,0),(209,65,-1,0),(210,65,1,0),(216,65,1,0),(217,65,1,0),(208,66,1,0),(209,66,-1,0),(210,66,1,0),(209,67,-1,1),(210,67,1,0),(213,67,0,1),(215,67,1,0),(227,73,1,0),(250,74,1,0),(226,79,1,0),(260,82,1,0),(227,90,1,0);
/*!40000 ALTER TABLE `qa_uservotes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:25:00
