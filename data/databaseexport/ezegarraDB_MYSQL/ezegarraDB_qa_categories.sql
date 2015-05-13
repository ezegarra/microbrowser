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
-- Table structure for table `qa_categories`
--

DROP TABLE IF EXISTS `qa_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qa_categories` (
  `categoryid` int(10) unsigned NOT NULL auto_increment,
  `parentid` int(10) unsigned default NULL,
  `title` varchar(80) NOT NULL,
  `tags` varchar(200) NOT NULL,
  `content` varchar(800) NOT NULL default '',
  `qcount` int(10) unsigned NOT NULL default '0',
  `position` smallint(5) unsigned NOT NULL,
  `backpath` varchar(804) NOT NULL default '',
  PRIMARY KEY  (`categoryid`),
  UNIQUE KEY `parentid` (`parentid`,`tags`),
  UNIQUE KEY `parentid_2` (`parentid`,`position`),
  KEY `backpath` (`backpath`(200))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa_categories`
--

LOCK TABLES `qa_categories` WRITE;
/*!40000 ALTER TABLE `qa_categories` DISABLE KEYS */;
INSERT INTO `qa_categories` VALUES (1,NULL,'CS1530 - Software Engineering','CS1530','This course is one of the courses in the software engineering track. This course is intended to cover the object-oriented approach to software engineering, combining both the theoretical principles and the practical aspects of software design using the JAVA language. Students will learn the fundamentals of object-oriented software engineering and participate in a group project on software design using JAVA. Therefore there are no individual exercises, only group projects with at least five deliverables. The midterm and final cover the principles of software design methodology with emphasis on object-oriented approach rather than the traditional structural approach. The sequel of this course is CS1631 Software Design Methodology.',56,1,'CS1530'),(2,NULL,'CS2310 - Multimedia Software Engineering','CS2350','This course is one of the graduate courses in software engineering. The course concentrates on the investigation of the dual role of multimedia software engineering to apply software engineering principles to the design of multimedia systems, and to apply multimedia technologies to the practice of software engineering.',19,2,'CS2350'),(3,1,'CS1530 - Fall 2011','fall-2011','Fall Semester 2011',56,1,'fall-2011/CS1530'),(4,2,'CS2310 - Fall 2011','fall-2011','',19,1,'fall-2011/CS2350'),(5,NULL,'CS1635 - Designing, Prototyping and Evaluating Mobile Interfaces','cs1635-designing-prototyping-and-evaluating-mobile-interfaces','With more than 5.3 billion units in use, mobile phones and portable\ninternet tablets (iPhone, Android etc) have already become the most\npopular computing device in human history. Their portability and\ncommunication capabilities may revolutionize how people do their daily\nwork and interact with other people in ways PCs have done during the\npast 30 years. In this rebooted course, you will learn essential\nskills on designing, prototyping and evaluating mobile interfaces. Key\ntopics of this course include -\n\n - The iterative process of designing, prototyping and evaluating\nmobile applications and related techniques and tools\n - State-of-the-art mobile interfaces and interaction technologies\n - How to design and implement effective applications for the small\nscreen, with limited I/O modalities ',13,3,'cs1635-designing-prototyping-and-evaluating-mobile-interfaces'),(6,5,'CS1635 - Spring 2012','cs1635-spring-2012','',13,1,'cs1635-spring-2012/cs1635-designing-prototyping-and-evaluating-mobile-interfaces');
/*!40000 ALTER TABLE `qa_categories` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:25:01
