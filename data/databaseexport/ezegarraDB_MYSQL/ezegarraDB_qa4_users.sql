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
-- Table structure for table `qa4_users`
--

DROP TABLE IF EXISTS `qa4_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qa4_users` (
  `userid` int(10) unsigned NOT NULL auto_increment,
  `created` datetime NOT NULL,
  `createip` int(10) unsigned NOT NULL,
  `email` varchar(80) NOT NULL,
  `handle` varchar(20) NOT NULL,
  `avatarblobid` bigint(20) unsigned default NULL,
  `avatarwidth` smallint(5) unsigned default NULL,
  `avatarheight` smallint(5) unsigned default NULL,
  `passsalt` binary(16) default NULL,
  `passcheck` binary(20) default NULL,
  `level` tinyint(3) unsigned NOT NULL,
  `loggedin` datetime NOT NULL,
  `loginip` int(10) unsigned NOT NULL,
  `written` datetime default NULL,
  `writeip` int(10) unsigned default NULL,
  `emailcode` char(8) character set ascii NOT NULL default '',
  `sessioncode` char(8) character set ascii NOT NULL default '',
  `sessionsource` varchar(16) character set ascii default '',
  `flags` smallint(5) unsigned NOT NULL default '0',
  `wallposts` mediumint(9) NOT NULL default '0',
  PRIMARY KEY  (`userid`),
  KEY `email` (`email`),
  KEY `handle` (`handle`),
  KEY `level` (`level`),
  KEY `created` (`created`,`level`,`flags`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa4_users`
--

LOCK TABLES `qa4_users` WRITE;
/*!40000 ALTER TABLE `qa4_users` DISABLE KEYS */;
INSERT INTO `qa4_users` VALUES (1,'2014-05-03 00:38:02',180558595,'ezegarra@cs.pitt.edu','ezegarra',NULL,NULL,NULL,'pddglat6wbuokxt3','Dƒä„]ÇËEΩ*‘+ê\nMôì',120,'2014-12-17 14:48:02',2291068430,'2014-12-16 17:10:06',2291068430,'','d7fyq1lg',NULL,0,0),(2,'2014-05-09 16:00:09',2291068430,'adora@yahoo.cn','adora',NULL,NULL,NULL,'gqxb4fsmc0i1abbc','“◊bK5ÔZ&ƒ&rf\'/≤ı¶òE',0,'2014-11-18 15:34:48',2291068430,'2014-05-09 16:04:27',2291068430,'pc5ne72p','5uoohe50',NULL,0,0);
/*!40000 ALTER TABLE `qa4_users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:25:11
