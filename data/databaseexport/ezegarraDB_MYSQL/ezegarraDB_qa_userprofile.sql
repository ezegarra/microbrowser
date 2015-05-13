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
-- Table structure for table `qa_userprofile`
--

DROP TABLE IF EXISTS `qa_userprofile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qa_userprofile` (
  `userid` int(10) unsigned NOT NULL,
  `title` varchar(40) NOT NULL,
  `content` varchar(8000) NOT NULL,
  UNIQUE KEY `userid` (`userid`,`title`),
  CONSTRAINT `qa_userprofile_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `qa_users` (`userid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa_userprofile`
--

LOCK TABLES `qa_userprofile` WRITE;
/*!40000 ALTER TABLE `qa_userprofile` DISABLE KEYS */;
INSERT INTO `qa_userprofile` VALUES (1,'about',''),(1,'course',''),(1,'location',''),(1,'name',''),(1,'website',''),(2,'about','software engineering'),(2,'course','CS1530'),(2,'location','6101 Sennott Square'),(2,'name','Shi-Kuo Chang'),(2,'website','www.cs.pitt.edu/~chang'),(5,'about','Sometimes I code things.'),(5,'course','CS 1530'),(5,'location','University of Pittsburgh'),(5,'name','Kyra Lee'),(5,'website','http://www.pitt.edu/~kfl5/'),(10,'about',''),(10,'course',''),(10,'location',''),(10,'name',''),(10,'website',''),(14,'about','Senior COE'),(14,'course',''),(14,'location','Pittsburgh'),(14,'name','Jamie Richardson'),(14,'website',''),(18,'about','Anon\n\n401,441,445,447,449,1501,1502,1520,1550,1555,1566'),(18,'course','cs1520'),(18,'location','Pittsburgh'),(18,'name','Will'),(18,'website','www.fallenreaper.com'),(19,'about',''),(19,'course','CS1530'),(19,'location',''),(19,'name',''),(19,'website',''),(23,'about','Senior at UPitt, CS Major, budding Android developer, Gundam enthusiast.'),(23,'course','CS1530'),(23,'location',''),(23,'name','Vince Tran'),(23,'website','http://vincetran.me'),(26,'about','Enrolled in CS1530'),(26,'course',''),(26,'location',''),(26,'name','Dave Turka'),(26,'website','www.daveturka.com'),(33,'about','I am a 3rd year phd student.'),(33,'course','CS 2310'),(33,'location','Room 6508'),(33,'name','Yingze Wang'),(33,'website','www.cs.pitt.edu/~yingzewang'),(34,'about','2nd year Master\'s student of Comp Sci at Pitt'),(34,'course',''),(34,'location',''),(34,'name','Richard Anderson'),(34,'website','www.cs.pitt.edu'),(35,'about',''),(35,'course',''),(35,'location',''),(35,'name',''),(35,'website',''),(39,'about',''),(39,'course','CS2310'),(39,'location',''),(39,'name','Kelli Ireland'),(39,'website',''),(45,'about',''),(45,'course',''),(45,'location',''),(45,'name','Mengmeng Li'),(45,'website',''),(46,'about',''),(46,'course',''),(46,'location',''),(46,'name',''),(46,'website',''),(47,'about',''),(47,'course',''),(47,'location',''),(47,'name','Imani'),(47,'website',''),(49,'about',''),(49,'course',''),(49,'location',''),(49,'name',''),(49,'website',''),(51,'about',''),(51,'course',''),(51,'location',''),(51,'name','Jingtao Wang'),(51,'website',''),(52,'about',''),(52,'course',''),(52,'location',''),(52,'name',''),(52,'website',''),(54,'about',''),(54,'course',''),(54,'location',''),(54,'name',''),(54,'website',''),(55,'about',''),(55,'course',''),(55,'location',''),(55,'name','Jim McMahon'),(55,'website',''),(57,'about',''),(57,'course',''),(57,'location',''),(57,'name',''),(57,'website',''),(59,'about','<?php info();>'),(59,'course',''),(59,'location',''),(59,'name',''),(59,'website',''),(63,'about',''),(63,'course',''),(63,'location',''),(63,'name',''),(63,'website',''),(64,'about','I am a senior at the University of Pittsburgh majoring in Computer Science.'),(64,'course',''),(64,'location','Pittsburgh'),(64,'name','Erin Plazek'),(64,'website',''),(65,'about','President.'),(65,'course',''),(65,'location','My \'Alleged\' Mistress\' home'),(65,'name','Herman Cain'),(65,'website','www.pornhub.com'),(73,'about',''),(73,'course',''),(73,'location',''),(73,'name',''),(73,'website',''),(77,'about',''),(77,'course','CS 1635'),(77,'location',''),(77,'name','Evan McCullough'),(77,'website',''),(78,'about','Undergraduate Student at the University of Pittsburgh.'),(78,'course','CS 1635'),(78,'location',''),(78,'name','Bence Feher'),(78,'website','http://mips.cs.pitt.edu/cs1635-spring12/User:Bence_Feher'),(80,'about',''),(80,'course','CS 1635'),(80,'location',''),(80,'name','Spencer Krause'),(80,'website','http://www.roboticsclub.org/projects/tankchair'),(88,'about','Hi,I am a student from UK'),(88,'course',''),(88,'location',''),(88,'name','Hugo Reway'),(88,'website','http://uvuw.net'),(89,'about',''),(89,'course',''),(89,'location',''),(89,'name',''),(89,'website','http://www.beatsbydreheadphones.net/');
/*!40000 ALTER TABLE `qa_userprofile` ENABLE KEYS */;
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
