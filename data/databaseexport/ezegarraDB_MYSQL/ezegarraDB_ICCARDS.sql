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
-- Table structure for table `ICCARDS`
--

DROP TABLE IF EXISTS `ICCARDS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ICCARDS` (
  `ICID` int(11) NOT NULL auto_increment,
  `ICNAME` varchar(50) default NULL,
  `ICDESCRIPTION` varchar(255) default NULL,
  `ICINTPATTERN` varchar(25) NOT NULL,
  `ICTASK` varchar(255) default NULL,
  `ICTIMECRITICALCONDITION` varchar(50) default NULL,
  `ICNUMBERCURRENT` int(11) default NULL,
  `ICNUMBERTOTAL` int(11) default NULL,
  `ICOTHERNAME` varchar(255) default NULL,
  `ICOTHERMESSAGE` varchar(255) default NULL,
  `ICOTHERTASK` char(5) NOT NULL,
  `ICENTRYID` int(11) NOT NULL,
  PRIMARY KEY  (`ICID`),
  KEY `ICCARDS_ICCARDENTRIES_FK` (`ICENTRYID`),
  KEY `ICCARDS_ICPATTERNS_FK` (`ICINTPATTERN`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ICCARDS`
--

LOCK TABLES `ICCARDS` WRITE;
/*!40000 ALTER TABLE `ICCARDS` DISABLE KEYS */;
INSERT INTO `ICCARDS` VALUES (1,'Sample card for demo','a sample descroptionx','other','ictaskx','ictimecriti < 22',1,1,'toher ml','o,k wee',';lkwe',3),(7,'sample ic card','a new ic card to validate REST service','other','demo ic card creation','1',1,1,'reviewer','review results','revwr',5),(8,'this is a new card','a sample descroptionx','other','ictaskx','ictimecriti < 22',1,1,'toher ml','o,k wee',';lkwe',3),(5,'testing new card','a sample descroptionx','other','ictaskx','ictimecriti < 22',1,1,'toher ml','o,k wee',';lkwe',3),(6,'testing new card','a sample descroptionx','other','ictaskx','ictimecriti < 22',1,1,'toher ml','o,k wee',';lkwe',3);
/*!40000 ALTER TABLE `ICCARDS` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:25:04
