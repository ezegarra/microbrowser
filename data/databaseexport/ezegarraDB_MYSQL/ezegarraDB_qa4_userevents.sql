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
-- Table structure for table `qa4_userevents`
--

DROP TABLE IF EXISTS `qa4_userevents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qa4_userevents` (
  `userid` int(10) unsigned NOT NULL,
  `entitytype` char(1) character set ascii NOT NULL,
  `entityid` int(10) unsigned NOT NULL,
  `questionid` int(10) unsigned NOT NULL,
  `lastpostid` int(10) unsigned NOT NULL,
  `updatetype` char(1) character set ascii default NULL,
  `lastuserid` int(10) unsigned default NULL,
  `updated` datetime NOT NULL,
  KEY `userid` (`userid`,`updated`),
  KEY `questionid` (`questionid`,`userid`),
  CONSTRAINT `qa4_userevents_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `qa4_users` (`userid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa4_userevents`
--

LOCK TABLES `qa4_userevents` WRITE;
/*!40000 ALTER TABLE `qa4_userevents` DISABLE KEYS */;
INSERT INTO `qa4_userevents` VALUES (1,'-',0,17672929,17672929,'H',1,'2014-11-18 15:35:08'),(1,'-',0,1,1,'H',1,'2014-11-18 15:35:16'),(1,'-',0,1,3,'H',1,'2014-11-18 15:35:20'),(1,'-',0,1,2,'H',1,'2014-11-18 15:35:24'),(2,'-',0,9913,17672927,'H',1,'2014-12-08 17:05:39'),(1,'-',0,9913,17672948,'H',1,'2014-12-16 12:54:03'),(1,'-',0,9913,17672949,'H',1,'2014-12-16 14:36:08'),(1,'-',0,9913,17672950,'H',1,'2014-12-16 17:09:53');
/*!40000 ALTER TABLE `qa4_userevents` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:24:58
