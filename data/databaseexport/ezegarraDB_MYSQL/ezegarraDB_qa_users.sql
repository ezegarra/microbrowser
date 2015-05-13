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
-- Table structure for table `qa_users`
--

DROP TABLE IF EXISTS `qa_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qa_users` (
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
  `flags` tinyint(3) unsigned NOT NULL default '0',
  PRIMARY KEY  (`userid`),
  KEY `email` (`email`),
  KEY `handle` (`handle`),
  KEY `level` (`level`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa_users`
--

LOCK TABLES `qa_users` WRITE;
/*!40000 ALTER TABLE `qa_users` DISABLE KEYS */;
INSERT INTO `qa_users` VALUES (1,'2011-07-14 16:05:24',2184306630,'ezegarra@cs.pitt.edu','ezegarra',NULL,NULL,NULL,'gaullmahhwghhpd4','�`���W�����b~~=�v��',120,'2012-11-17 06:42:10',1248818130,'2012-05-21 06:13:09',2907159287,'','aasaskc4',NULL,24),(2,'2011-07-21 14:42:43',1204735120,'chang@cs.pitt.edu','changsk',NULL,NULL,NULL,'onnf0gi8p2imk53l','up�k��C]���O��Pd�',50,'2011-10-04 18:13:04',2184306618,'2011-12-12 22:42:53',1204735120,'x51bs0g5','58l084nd',NULL,16),(3,'2011-08-23 16:23:03',2184306630,'ezegarra@yahoo.com','test01',NULL,NULL,NULL,'regbxkdlojzvczo6','da�6�:�玃=�,�6�_�',0,'2012-10-03 14:03:58',2184306652,NULL,NULL,'','p23xxh1c',NULL,1),(4,'2011-08-30 16:38:41',2184305725,'dir11@pitt.edu','dianaren',NULL,NULL,NULL,'dm3o1s5v98dpq9i3',';�����%�}E�q�����',0,'2011-12-12 13:26:08',2184305745,NULL,NULL,'','u3zovqlq',NULL,1),(5,'2011-08-30 16:38:49',2184305715,'kfl5@pitt.edu','kfl5',NULL,NULL,NULL,'g6b1geq1py7mscq6','���|n�f�Sދ�Q�VF��',0,'2011-11-28 03:13:04',402898008,'2011-12-12 10:56:53',402898008,'','bkc9vrts',NULL,17),(6,'2011-08-30 16:39:50',2184305714,'fys1@pitt.edu','fys1',NULL,NULL,NULL,'81y6qm8dvgrmqnl3','E��`J\r�lEn*��T�\Z��`',0,'2011-12-11 00:24:00',1204253526,'2011-12-11 00:27:21',1204253526,'','t5ut3t4h',NULL,1),(7,'2011-08-30 16:39:55',2184306289,'mrf32@pitt.edu','mrf32',NULL,NULL,NULL,'r9i6mb5etngne01g','Du�ݔ߮��@���/�Y�k�',0,'2011-12-11 20:25:01',402912489,'2011-12-12 14:47:32',2530476785,'','fdxsu33g',NULL,1),(8,'2011-08-30 16:39:55',2184305735,'rbf8@pitt.edu','rbf8',NULL,NULL,NULL,'94qjygwzbvrb8se0','Q~�������J��<�ᚺ',0,'2012-02-26 19:43:22',1204743790,'2011-12-12 13:09:48',2184305747,'','o3v0mbbm',NULL,1),(9,'2011-08-30 16:40:08',2184305722,'edwardhy@live.cn','yah15',NULL,NULL,NULL,'cq3qhpbvt4ajx50l','�	亂=�s�B۝[ʟK-��',0,'2011-11-17 13:54:03',2184305797,NULL,NULL,'3nqep8zl','z8g2191w',NULL,0),(10,'2011-08-30 16:40:16',2184306287,'custadvocate2006@yahoo.com','gestrich',NULL,NULL,NULL,'llfncymkr2wktw4f','J��Q>��7u�[����)�֯',0,'2011-11-08 22:01:36',1813077573,'2011-11-08 21:57:14',1813077573,'','tbkck0jw',NULL,17),(11,'2011-08-30 16:40:17',2184305739,'capvc1@pitt.edu','capvc1',NULL,NULL,NULL,'uk12tfrjhv8vtbiy','�,V�!\'�*��U��=',0,'2011-12-09 10:29:12',2530484284,'2011-12-09 10:31:58',2530484284,'','w3l4urjf',NULL,1),(12,'2011-08-30 16:40:25',2184305742,'kac173@pitt.edu','kac173',NULL,NULL,NULL,'41njool1ihgx2c48','�ٍd��6�������W��',0,'2011-12-10 11:42:59',1659877455,NULL,NULL,'','umm9ggbr',NULL,1),(13,'2011-08-30 16:40:25',2184305721,'jdl56@pitt.edu','jdl56',NULL,NULL,NULL,'809avk3j01614kez','A|�4-ƙM<4QW\Zͽ�',0,'2011-12-09 20:15:11',411303753,'2011-12-09 20:17:50',411303753,'','77g1p7e6',NULL,1),(14,'2011-08-30 16:40:30',2184305740,'jcr43@pitt.edu','jcr43',NULL,NULL,NULL,'85j57jzu3qg11d2w','�ҁ �3\"� �^<�dQ!',0,'2011-12-12 15:12:42',2530476870,'2011-11-29 04:08:20',1134807961,'','ivh7sylx',NULL,17),(15,'2011-08-30 16:40:32',2184305736,'jjw38@pitt.edu','jwits',NULL,NULL,NULL,'3g0m707cd5btrwvh','gy���2���i	��f ��',0,'2011-10-19 18:41:19',2184287274,NULL,NULL,'bmtco8ua','5wcaohma',NULL,0),(16,'2011-08-30 16:40:37',2184305733,'mil49@pitt.edu','goop',NULL,NULL,NULL,'pr7h11uwsrb6f80l','�Ҷ\\�w����+��x+7�',0,'2011-11-17 14:42:18',2530476680,NULL,NULL,'zs8ko6ey','9lfugzzw',NULL,0),(17,'2011-08-30 16:40:38',2184305720,'dfj5@pitt.edu','dfj5',NULL,NULL,NULL,'nphaq9ddfbx9cjti','��5�AS���qS���6:�hF�',0,'2012-01-11 18:48:21',1195119538,NULL,NULL,'','avehj9vk',NULL,1),(18,'2011-08-30 16:40:39',2184306290,'white.man.on.campus@gmail.com','fallenreaper',NULL,NULL,NULL,'xc3igtt610hgxxqd','�u���r���A~`��\'���',0,'2011-12-01 13:47:17',3331065037,'2011-11-29 08:37:56',3331065037,'','dprfnm9d',NULL,17),(19,'2011-08-30 16:40:40',2184305727,'jec84@pitt.edu','jec84',NULL,NULL,NULL,'ogmlqxgn8t6k53ev','���$�-�	ɭ��&d��\\',0,'2011-12-13 01:27:35',207716674,'2011-12-12 19:11:08',1195146737,'','62mxlbxg',NULL,17),(20,'2011-08-30 16:40:41',2184305737,'liamthekerr@gmail.com','lkerr',NULL,NULL,NULL,'3fuabllo2tswmd9x','�&=�Jj�iA�`�K�)D�',0,'2011-12-11 16:34:07',411260129,'2011-12-09 21:22:54',1813071203,'v23m4hfl','bn66tsp7',NULL,0),(21,'2011-08-30 16:40:44',2184305726,'chloelbark@gmail.com','chloelbark',NULL,NULL,NULL,'uz6b5spvbh257nv6','�\0B�u��z�����+���',0,'2011-11-08 13:23:26',1203164414,NULL,NULL,'','f00gv1q8',NULL,1),(22,'2011-08-30 16:40:45',2184267571,'bjm57@pitt.edu','bjm57',NULL,NULL,NULL,'3mbfb3epqmludo63','�0Q0�M_[�t|�Uw\nbd,�',0,'2011-12-12 13:30:19',1136270560,'2011-09-01 16:57:05',2530488029,'','ptcus9rk',NULL,1),(23,'2011-08-30 16:40:54',2184305711,'vtt2@pitt.edu','vtt2',NULL,NULL,NULL,'x6y56f2d2al06v5v','�#��bMB�5;�}a�e��\Z�',0,'2012-02-26 19:40:50',1204253369,'2012-02-02 08:46:10',2184287688,'','tdufsw8e',NULL,25),(24,'2011-08-30 16:41:03',2184305762,'bdw14@pitt.edu','bdw14',NULL,NULL,NULL,'rhdk4r7f3sf0k2sd','�B�~/L����7���s',0,'2011-12-12 12:17:50',2530476612,NULL,NULL,'zvv7wk2l','lv0m3ndi',NULL,0),(25,'2011-08-30 16:41:13',2530488096,'gew11@pitt.edu','gew11',NULL,NULL,NULL,'pp1d1uzhv8iwltcx','��͑נ^a7���M�M{��',0,'2011-12-06 15:36:01',2184305747,'2011-11-21 14:21:05',2530477140,'fvjvgsv9','fmeemtpw',NULL,0),(26,'2011-08-30 16:41:14',2184305741,'dave.turka@gmail.com','dturka',NULL,NULL,NULL,'h58fe0jkznuc6a05','[IQ^�zi#��R6,��/���',0,'2011-12-12 12:52:59',2291042845,'2011-12-09 12:46:50',2184286522,'','pz6tqz8c',NULL,25),(27,'2011-08-30 16:41:28',2184305761,'kim.cooperrider@gmail.com','kimcoop',NULL,NULL,NULL,'4vrbqlueto7yigzi','�����<ф#���t��t��',0,'2012-02-01 17:33:25',2530476977,'2011-09-26 13:02:38',1659650728,'','8l60fwam',NULL,1),(28,'2011-08-30 16:44:39',2184306291,'sgm18@pitt.edu','sgm18',NULL,NULL,NULL,'s32uxmwhjeukxqqt','`�#W7�\'����X��B�Nbx(',0,'2011-11-15 16:05:19',2530476975,NULL,NULL,'991vk6mt','u7doe8up',NULL,0),(29,'2011-08-30 16:46:00',2184305722,'day21@pitt.edu','day21',NULL,NULL,NULL,'avmdt5haxntzbuff','WNg�/H�$�K��H�_P��',0,'2011-11-29 16:02:31',2184305723,'2011-12-12 13:03:58',2530493020,'i1wztdj2','5ipp2a9j',NULL,0),(30,'2011-08-30 16:50:35',2184305732,'chn22@pitt.edu','chn22',NULL,NULL,NULL,'yh0h55df36xopoa7','��%V�8�\rY YP����/QG',0,'2012-03-26 13:06:24',1136270530,'2012-02-26 14:17:21',1136270530,'','l6mhe8su',NULL,1),(31,'2011-08-30 17:08:40',2184286797,'kmj32@pitt.edu','kevinjeffries',NULL,NULL,NULL,'qq3gmtyse27i9zsi','�/�C�;��E����2~���',0,'2011-10-06 13:12:35',2184261044,'2011-09-01 16:52:22',2184305732,'','qi1hhg7b',NULL,1),(32,'2011-08-30 17:46:21',2530478692,'lol16@Pitt.edu','Victor0720',NULL,NULL,NULL,'iutejb6zgq1plwgm','��6�1�o��k�ػQF����',0,'2011-11-23 23:00:50',411243230,NULL,NULL,'','rauowgag',NULL,1),(33,'2011-08-30 18:12:54',2530487969,'yingzewang@gmail.com','yingzewang',NULL,NULL,NULL,'3lu13ypehk27kai8','������z�Z9��Q��c�',0,'2011-12-10 22:35:09',2163075834,'2011-10-05 16:19:50',1204743687,'','bqqsqnn3',NULL,17),(34,'2011-08-30 18:24:44',2184305798,'rla20@pitt.edu','AndersonRich',NULL,NULL,NULL,'hammn8gxsdwc2cry','![�Z��b�Aj�V>]\'QG+�',0,'2011-11-20 20:35:20',1651495102,'2011-11-20 20:38:53',1651495102,'','0gb46qs5',NULL,17),(35,'2011-08-30 19:02:16',1195135250,'sag89@pitt.edu','sag89',NULL,NULL,NULL,'kq813xu82r4wngm9','���gC���h�#К��W',0,'2011-12-12 14:30:20',2530476852,'2011-12-09 20:09:27',1195135250,'','s2xzfpki',NULL,17),(36,'2011-08-30 19:06:12',1195226338,'hfriedberg@gmail.com','hfriedberg',NULL,NULL,NULL,'ko7wjyhbk34zx4if',';$ ��-t�s�H{�2\ri\07\n',0,'2011-12-07 12:12:03',2530477011,'2011-10-20 15:30:05',2530476869,'','133eybr6',NULL,1),(37,'2011-08-30 19:10:00',1135296744,'msd44@pitt.edu','msd44',NULL,NULL,NULL,'uj8liezyre72d34v','/7��: �r�[�\r�}\0',0,'2011-12-01 13:54:57',1135296744,NULL,NULL,'','267i3hio',NULL,1),(38,'2011-08-30 20:22:49',1195184889,'STR32@pitt.edu','srojcewicz',NULL,NULL,NULL,'z1iuzqieoxih9hep','��\rl�MR�%�i6I�.�s�',0,'2012-03-08 11:42:23',411246587,'2012-02-26 21:22:48',411246587,'','q4lls7jk',NULL,1),(39,'2011-08-30 21:23:06',1626117312,'kireland@cs.pitt.edu','kireland',NULL,NULL,NULL,'upqnxtx2hlkmyd7q','Ox��܂�׻��U1ƻ7',0,'2011-11-20 11:10:33',1626117312,'2011-11-20 11:13:36',1626117312,'','67voj3gk',NULL,17),(40,'2011-08-31 16:39:58',2184306215,'ouyang@cs.pitt.edu','ouyang',NULL,NULL,NULL,'r1vg6kujyuutssia','��/�e��F=�J�ޙx�h',0,'2011-11-28 23:31:28',2184306215,'2011-11-16 17:14:25',2184306215,'','tr1w8ich',NULL,1),(41,'2011-08-31 19:03:36',2530487721,'kej25@pitt.edu','kej25',NULL,NULL,NULL,'zij2qkru3ifmnh8f','��p���\r��p�$�F�¯�7%',0,'2011-11-15 23:09:54',1135297877,NULL,NULL,'','0nc3yti6',NULL,1),(42,'2011-08-31 19:40:28',1136264755,'djc42@pitt.edu','djc42',NULL,NULL,NULL,'8ld2wiuf82lzcx88','��힢����)��P��\")',0,'2011-12-12 23:06:52',1136264755,'2011-12-12 13:59:06',2184305737,'81z9xv7e','hl17tluk',NULL,0),(43,'2011-08-31 20:38:23',1204253516,'sddyshou@gmail.com','sddys',NULL,NULL,NULL,'n5raeb13mig3s4ep','�[O/�j�E�rn��P�|�',0,'2011-11-08 13:43:19',2530476653,'2011-11-28 18:08:34',1204253516,'','8ilnhyza',NULL,1),(44,'2011-09-01 10:30:45',1204255664,'lej16@pitt.edu','lej16',NULL,NULL,NULL,'lcgpcw1g38b47j6k','x�͔��x��&�R��T��X�2',0,'2011-10-20 11:13:43',1203156445,NULL,NULL,'','qkrlzl86',NULL,1),(45,'2011-09-01 10:52:09',2013319367,'mel74@pitt.edu','mel74',NULL,NULL,NULL,'a90tbcc9c84966md','`�Z&*�;sf��_�A��ݷ',0,'2011-10-12 19:38:33',402897997,'2011-11-08 17:39:45',2530477005,'vjdhei31','aa0to3bj',NULL,16),(46,'2011-09-01 11:59:49',2184286986,'bad20@pitt.edu','BIGBALLER90',17302793980867049595,300,400,'i2oq1088jxvll5io','�3�jM�7+_�g�2���',0,'2011-12-08 15:41:17',2184305722,'2011-12-12 13:24:53',2530493116,'','4wy611pv',NULL,21),(47,'2011-09-01 12:50:52',2530485860,'inp2@pitt.edu','inp2',NULL,NULL,NULL,'vcagw7pcf4aqlrk4','�o��W��3�;���',0,'2011-12-12 13:40:54',2291012205,'2011-12-12 13:29:19',2291012205,'','1f800c33',NULL,17),(48,'2011-09-01 16:03:56',2184305724,'ngd7@pitt.edu','ngd7',NULL,NULL,NULL,'l1cgbgtzpfoog2e2','���51��X�AҀ���',0,'2011-09-01 16:03:56',2184305724,NULL,NULL,'rwi92zrb','kh4npri6',NULL,0),(49,'2011-09-01 16:52:47',2184305735,'jjh47@pitt.edu','jjh47',NULL,NULL,NULL,'tfyz5drs9owy3mbu','��5��}�R:#Zm��ĺpY�',0,'2011-12-12 12:48:30',2530487429,'2011-12-12 12:49:32',2530487429,'','pdk9x8fa',NULL,17),(50,'2011-09-01 19:19:56',1247947046,'rur7@pitt.edu','star',NULL,NULL,NULL,'0bdnytiwe2058f1m','���K�0�MF[��ɝ!�',0,'2011-12-13 00:14:07',1812154294,NULL,NULL,'11rv3kw9','m0tfj33j',NULL,0),(51,'2011-09-06 17:41:11',2184306040,'jingtaow@gmail.com','jingtaow',NULL,NULL,NULL,'lnw415vjubgv3yev','�SY���DO���T	��2�',0,'2012-02-29 23:40:31',1659637834,'2012-02-29 23:49:26',1659637834,'','geh5jmxj',NULL,17),(52,'2011-09-20 17:46:33',2530476632,'wilkie05@gmail.com','wilkie',NULL,NULL,NULL,'sbmoxgrf9eb6x28y','be��ī��ѕ��	�{:�',0,'2011-10-13 16:16:02',2184306201,NULL,NULL,'ymyiypzz','xh7u8w10',NULL,24),(53,'2011-09-21 10:25:56',1813077573,'wbg2@pitt.edu','wbg2',NULL,NULL,NULL,'0vt0v0nfh8rohl4r','��Mz�\n]k��ُ�=�OTUQ',0,'2012-02-19 07:35:11',1813077573,'2012-02-19 07:47:51',1813077573,'','8jscu6ka',NULL,1),(54,'2011-09-21 10:29:10',1813077573,'billightened@gmail.com','wbg',NULL,NULL,NULL,'tmjoo6jsx4ihrhjv','x{�<���Oy�k#)C�\Z�\"|D',0,'2011-11-08 22:02:43',1813077573,NULL,NULL,'ykxhwqvb','ohqoi15g',NULL,16),(55,'2011-09-22 22:03:56',2907380813,'james.mcmahon@fedex.com','jmcmahon',NULL,NULL,NULL,'ho9unfpvy4zt32gw','5�l����>Z�j��0',0,'2011-11-28 13:36:14',3331065037,NULL,NULL,'','0az14x9j',NULL,17),(56,'2011-09-23 06:43:10',2907380813,'gpompa@compunetix.com','gpompa',NULL,NULL,NULL,'jvbpys3humkauyf0','�ϳGDb��.�f�y��w���',0,'2011-09-23 06:51:48',1283315384,NULL,NULL,'','cbnxa664',NULL,1),(57,'2011-10-04 18:21:20',2530476774,'sun@cs.pitt.edu','sun',NULL,NULL,NULL,'l7zeg6h4rgriix0w','Q�)�_P>��{��	rjb8���',0,'2011-12-11 00:42:16',402855048,NULL,NULL,'','npruh1l2',NULL,25),(58,'2011-11-01 21:41:02',2184250029,'lulz@pwnt.com','\'\'\'\'\'\'\'\'\'\'\'\'',NULL,NULL,NULL,'v4p4apshsaw0ylt0','Ş�S���g�ՅA�M���',0,'2011-11-01 21:41:02',2184250029,'2011-11-01 21:42:10',2184250029,'eju8tt7s','kifcyaqd',NULL,0),(59,'2011-11-01 21:43:36',2184250029,'lulz@trolllolol.com','<?php info();>',NULL,NULL,NULL,'gik8u5770cm36xb4','q���k3ʎ�x�Eb��',0,'2011-11-01 21:43:36',2184250029,'2011-11-01 21:44:27',2184250029,'sm6xw1dj','46crod30',NULL,16),(60,'2011-11-08 17:44:15',2530477045,'smlsun.yu@gmail.com','Boyu',NULL,NULL,NULL,'xnereql7k8e2eevw','�\\����#~\rx�^E�X+U8\nq',0,'2011-11-08 17:44:16',2530477045,NULL,NULL,'ng6kbq91','ls0i8fs8',NULL,0),(61,'2011-11-14 16:48:36',2530492083,'prg27@pitt.edu','prg27',NULL,NULL,NULL,'0l20e4xfri2gqnhy','�9��қ���w������',0,'2011-11-17 15:54:30',2530476721,NULL,NULL,'','ki3r4tfv',NULL,1),(62,'2011-12-12 12:49:39',2530487311,'patricepittmail@gmail.com','prg272',NULL,NULL,NULL,'ekne3a68au1qm2mf','�.\'�)|n�B�?�#G_T��',0,'2011-12-12 12:49:39',2530487311,NULL,NULL,'1tk0mw1o','m3j08xzl',NULL,0),(63,'2011-12-12 12:55:02',2530487311,'patricegerard1st@yahoo.com','JohnDoe',NULL,NULL,NULL,'ny0uz5c3yr1d3unt','��+�⑇-;�p�	����',0,'2011-12-12 12:55:03',2530487311,NULL,NULL,'yt4f7axj','5l37xq9g',NULL,24),(64,'2011-12-12 12:57:30',2530487766,'erinpzk@gmail.com','eep15',NULL,NULL,NULL,'ety1h53qvjs8rnb5','w�J��75Af=�*e�N',0,'2012-01-20 13:11:34',1195225483,'2011-12-12 13:04:26',2530487766,'','7yrbmufq',NULL,16),(65,'2011-12-12 13:00:48',2530487311,'patricegerardjr@gmail.com','Kat Daddy Cain',1289346026190210411,377,400,'648d1g7zcrgp37lx','�����,�3���O��\n',0,'2011-12-12 13:00:48',2530487311,'2011-12-12 13:24:36',2530487311,'','j9oyv1yx',NULL,21),(66,'2011-12-12 13:05:24',2530487766,'eep15@pitt.edu','BIGGESTballer',NULL,NULL,NULL,'w8ygf79t96vdea96','|CPTl�mWg9�\0��6<\'',0,'2011-12-12 13:05:24',2530487766,'2011-12-12 13:08:17',2530487766,'bze6rzcr','s7r007hj',NULL,0),(67,'2011-12-12 13:14:42',2530487766,'superstar064@aol.com','Balling_is_a_habit',NULL,NULL,NULL,'6kvofgtet3g6c02v','7�_X\'y;l��B}m/.5؃\\�',0,'2011-12-12 13:14:42',2530487766,'2011-12-12 15:19:02',2530487766,'','uuzab78v',NULL,1),(68,'2011-12-12 13:27:15',2530493116,'fezmond72690@yahoo.com','Big-Will-OReily',NULL,NULL,NULL,'oguq30junkpgcyna','�}J��#\Z$���g�B���',0,'2011-12-12 13:27:16',2530493116,NULL,NULL,'retkgle6','rsbd85v3',NULL,0),(69,'2011-12-12 14:39:07',2530492070,'patricepittmai1l@gmail.com','jkjk',NULL,NULL,NULL,'hys7qod1mowl17nw','�0���cy�ӥ\"c��&dg',0,'2011-12-12 14:39:07',2530492070,NULL,NULL,'ntb0uxbs','p6kk9cz2',NULL,0),(70,'2012-01-19 18:15:19',1135305117,'nrt13@pitt.edu','Nicholas Tate',NULL,NULL,NULL,'itvn4hl4dj4bueq3','��{^�P��=�k+�p�',0,'2012-02-28 03:48:12',1195225568,'2012-02-28 03:51:25',1195225568,'vq17ett6','g65zr6ln',NULL,0),(71,'2012-01-19 18:48:00',402854332,'amc177@pitt.edu','amc177',NULL,NULL,NULL,'t1uyzlji3fcfvn1c','���&�c��Txm&Ϫ�am',0,'2012-02-25 23:59:38',1136264818,NULL,NULL,'s12987bs','5yapwcss',NULL,0),(72,'2012-01-19 19:30:48',2184266075,'ead44@pitt.edu','donahoel',NULL,NULL,NULL,'ue7u6kt379lnutpk','��!�����ن��O�Ck',0,'2012-02-27 21:46:11',2184266027,NULL,NULL,'rkqa7w7h','94fmjfn7',NULL,0),(73,'2012-01-19 19:49:03',1204250645,'juh30@pitt.edu','Junchao Hua',NULL,NULL,NULL,'zt737x3vkm47ho72','Li�@f�d�7M�mFF��r�',0,'2012-01-21 17:08:31',1626017600,'2012-01-28 11:51:33',1626015101,'','1f4fxvum',NULL,17),(74,'2012-01-19 20:54:35',1651500030,'tft3@pitt.edu','ttaylor',NULL,NULL,NULL,'hd3j4xtk9myyjvhl','Ilձ~mv����i-�(�̈́\Z�',0,'2012-03-01 00:50:12',1651500030,'2012-02-26 21:18:54',1651500030,'','bvpeds8q',NULL,1),(75,'2012-01-19 21:56:44',1283313715,'lws8@pitt.edu','LarrySmeltzer',NULL,NULL,NULL,'5ns1poqvltgsyfhv',':F�[�8	󱳋_�������',0,'2012-02-16 13:40:10',2530477014,'2012-02-26 19:18:25',1283313715,'','88ld0f5u',NULL,1),(76,'2012-01-19 23:51:07',1135296534,'avr7@pitt.edu','Aaron Robertson',NULL,NULL,NULL,'2otreg9hkafi9q1c','�iM��S	Td�����`��\"2',0,'2012-04-10 00:10:16',1135296534,NULL,NULL,'6z45nfdn','v25ezkan',NULL,0),(77,'2012-01-20 00:31:15',2184269749,'emm82+microprobe@pitt.edu','BIGred053',NULL,NULL,NULL,'ho98z5sljbf0b2rv','pdh���!��z��g�M���n�',0,'2012-04-10 16:55:22',2530476565,NULL,NULL,'','14rksxyv',NULL,25),(78,'2012-01-20 14:47:15',2907409109,'bbf5@pitt.edu','bfeher',116924610228715924,100,100,'6x0au44hh0pf9v3h','r�Y��-0R��Y�z������',0,'2012-02-25 12:48:37',2530477042,'2012-02-25 13:00:26',2530477042,'','xnj5ca1s',NULL,21),(79,'2012-01-21 15:19:31',1074275046,'anthony.mcalexander@gmail.com','Anthony McAlexander',NULL,NULL,NULL,'2fl25dm399x2kpeu','��D��bO?ӛ��987j�',0,'2012-02-25 12:45:53',2530476794,'2012-01-26 13:20:36',2530477042,'odvbsnol','ot3jhr6q',NULL,0),(80,'2012-01-21 19:57:41',1136264818,'spencer.krause@gmail.com','thegrandlebowski',NULL,NULL,NULL,'1h9my2xhp2vc6v92','�!|�����Y�/?1k\0�',0,'2012-01-21 19:57:41',1136264818,'2012-01-21 20:00:47',1136264818,'1fhgu1wv','vusvaoch',NULL,18),(81,'2012-01-24 19:34:56',1813071203,'ltk7@pitt.edu','ltk7',NULL,NULL,NULL,'mymctwv4exlrckvk','i6�TW�����̐Q�Ӧ',0,'2012-01-24 19:35:11',1813071203,NULL,NULL,'qa8zfxls','lym54r5w',NULL,0),(82,'2012-01-25 02:06:49',1204253516,'brendanliu8899@gmail.com','Yuxin_Liu',NULL,NULL,NULL,'nri0afi0nugkivtc','���oܞ�������N�\r)��',0,'2012-02-29 21:34:53',1204253516,'2012-02-29 21:44:19',1204253516,'','4nxlcsd5',NULL,1),(83,'2012-01-25 23:05:38',1204732104,'jon.p.chmura@gmail.com','Jon Chmura',NULL,NULL,NULL,'rscsfp8spdh0ujiy','�[�!��֫˘�Ǚ���',0,'2012-04-09 20:44:43',1204732104,NULL,NULL,'','tsvhu4oy',NULL,0),(84,'2012-01-31 16:06:57',2801363816,'dew39@pitt.edu','dew39',NULL,NULL,NULL,'j374odooe266m922','��u$�\rɪ����;�o[h�E',0,'2012-01-31 16:06:57',2801363816,NULL,NULL,'','fnfauv5s',NULL,1),(85,'2012-01-31 20:51:22',1136263312,'slamhammer@gmail.com','Jon',NULL,NULL,NULL,'r7eqo599mk51igv5','N���stS��	FM�;i�=W�',0,'2012-04-10 10:04:25',1136263312,NULL,NULL,'','smyzws70',NULL,1),(86,'2012-02-06 20:03:20',2184262561,'Jmc185@gmail.com','Jim Cervone',NULL,NULL,NULL,'mxqzsesfl6ug3vu5','<����f�#���;Q���\\�',0,'2012-02-06 20:03:20',2184262561,NULL,NULL,'rughmka9','fown3grt',NULL,0),(87,'2012-02-14 01:30:16',2045925636,'ajose@tataelxsi.co.in','Achamma Jose',NULL,NULL,NULL,'onyddndaz6lgg9n8','�%-���)�J�a�8��I��W',0,'2012-02-14 01:30:16',2045925636,NULL,NULL,'bf7ebtgt','j01oo7cw',NULL,0),(88,'2012-03-07 04:18:53',1893227053,'horacerosale24@hotmail.com','health007',NULL,NULL,NULL,'baivz1loqppip5od','eܖ]�i��مr��y~�V�5',0,'2012-03-07 04:18:54',1893227053,NULL,NULL,'','xc7y7dxo',NULL,17),(89,'2012-03-28 23:14:29',2039457032,'allenlily@hush.com','londep',NULL,NULL,NULL,'wwn267gzlv6jqzt6','u\\O�f���~j$r�ih�m��',0,'2012-03-28 23:14:30',2039457032,NULL,NULL,'6e0ds0ni','bly7l38k',NULL,16),(90,'2012-05-18 08:41:18',2992331112,'erik-89@yandex.ru','erik',NULL,NULL,NULL,'nyxdqdmvj7ctc8jd','\\+9��!��TR��d2(?eS|',0,'2012-05-18 08:41:19',2992331112,'2012-05-18 08:43:32',2992331112,'s1e7rmk6','2oztyic1',NULL,0),(91,'2012-05-21 03:07:22',2057429985,'isaac.black35@yahoo.com','black35',NULL,NULL,NULL,'15b5d7lttibcljae','�� �Q|��q�:��<96��',0,'2012-05-21 03:07:23',2057429985,'2012-05-21 03:11:02',2057429985,'atvnfif5','b4vq48l8',NULL,0);
/*!40000 ALTER TABLE `qa_users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:25:21