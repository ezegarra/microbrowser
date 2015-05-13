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
-- Table structure for table `VISUAL_PATTERNS`
--

DROP TABLE IF EXISTS `VISUAL_PATTERNS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VISUAL_PATTERNS` (
  `ID` int(11) NOT NULL,
  `PATTERNNAME` varchar(350) NOT NULL,
  `PATTERNDESCRIPTION` varchar(350) default NULL,
  `PATTERNSOLUTION` varchar(350) default NULL,
  `CREATIONDATE` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `OWNERUSERID` int(11) default NULL,
  `LASTEDITORUSERID` int(11) default NULL,
  `LASTACTIVITYDATE` timestamp NOT NULL default '0000-00-00 00:00:00',
  `TAGS` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VISUAL_PATTERNS`
--

LOCK TABLES `VISUAL_PATTERNS` WRITE;
/*!40000 ALTER TABLE `VISUAL_PATTERNS` DISABLE KEYS */;
INSERT INTO `VISUAL_PATTERNS` VALUES (2109443059,'Reference Provider','The user is trying to address a particular functionality and it is looking for references to services that can address the problems. ','Provide a reference to an external service or utility that can provide the needed functionality.','2014-02-26 22:49:47',1,1,'2014-02-26 22:49:47',''),(1641682794,'Trusted Advisor','The answer seeker is looking for advice regarding the recommended or appropriate way to implement a task or complete a set of instructions.  The answer seeker might have an answer or a set of answers and is looking for confirmation.','To answer these types of questions, the answerer should start by providing the recommended answer to address the problem or situation.  Then, the answered should provide some level of reasoning behind the benefits of the selected answer.','2014-02-26 22:50:10',1,1,'2014-02-26 22:50:10',''),(-771804441,'Educator','The answer seeker is looking for an explanation of a topic, error message or code.','The answerer should start by providing a brief description of the error followed by either a reference to where additional information can be found or by a longer explanation of the error.','2014-02-26 22:50:41',1,1,'2014-02-26 22:50:41',''),(1796611198,'Comparator','The answer seeker is trying to determine the difference between two or more topics.','The answerer should provide a comparison between the different options submitted  by the answer seeker.','2014-04-16 10:15:26',1,1,'2014-04-16 10:15:26','');
/*!40000 ALTER TABLE `VISUAL_PATTERNS` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:25:07
