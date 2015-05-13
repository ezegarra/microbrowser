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
-- Table structure for table `qa_cache`
--

DROP TABLE IF EXISTS `qa_cache`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qa_cache` (
  `type` char(8) character set ascii NOT NULL,
  `cacheid` bigint(20) unsigned NOT NULL default '0',
  `content` mediumblob NOT NULL,
  `created` datetime NOT NULL,
  `lastread` datetime NOT NULL,
  PRIMARY KEY  (`type`,`cacheid`),
  KEY `lastread` (`lastread`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qa_cache`
--

LOCK TABLES `qa_cache` WRITE;
/*!40000 ALTER TABLE `qa_cache` DISABLE KEYS */;
INSERT INTO `qa_cache` VALUES ('i_40',116924610228715924,'����\0JFIF\0\0\0\0\0\0��\0;CREATOR: gd-jpeg v1.0 (using IJG JPEG v62), quality = 90\n��\0C\0\n\n\n\r\r��\0C		\r\r��\0\0(\0(\"\0��\0\0\0\0\0\0\0\0\0\0\0	\n��\0�\0\0\0}\0!1AQa\"q2���#B��R��$3br�	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz���������������������������������������������������������������������������\0\0\0\0\0\0\0\0	\n��\0�\0\0w\0!1AQaq\"2�B����	#3R�br�\n$4�%�\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz��������������������������������������������������������������������������\0\0\0?\0����(���d4�! �4�J�\r��/�I���C�3�I�\0¨��ԟD7�R�H���ܥ@�;N0F{��~|(�\\�D�&��Yl��8.X�	|�L��:�r�XG�����g��\n���g��8\n[�3���\0��O�\0����MџI����\"�P�q�+��τ?�M��������YҬ<7�C��tm:�.e��}�r��>T,I#$�\0|E���(�x���e|]���\'�y��2��E��N�A_���Wc�e��Ws_I|�m�#�����������ĕǛ�k�oݹ���*���-�N���\0�O?�D�Y�R���i�ԉAl�EX/��>�����~~��e�����Z�\0�����3��e���8(A�(���\0f/���5���M\Z����!��8{�[�r����!��__|?��x!�E�Եf�m��W�\\I���\0�K`rI���l :u�l��5�R�6�\r��89��\0�<1�+�\0�A�|N���Yݥ�Fʬ�;��O�k�G�/����%��|>�L�.�1�3��npX�p0O\'���\0���R�#H�&:r���i�FN�y�\09�@�s֊(���','2012-02-25 13:00:26','2012-05-18 08:44:03'),('i_40',1289346026190210411,'����\0JFIF\0\0\0\0\0\0��\0;CREATOR: gd-jpeg v1.0 (using IJG JPEG v62), quality = 90\n��\0C\0\n\n\n\r\r��\0C		\r\r��\0\0(\0%\"\0��\0\0\0\0\0\0\0\0\0\0\0	\n��\0�\0\0\0}\0!1AQa\"q2���#B��R��$3br�	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz���������������������������������������������������������������������������\0\0\0\0\0\0\0\0	\n��\0�\0\0w\0!1AQaq\"2�B����	#3R�br�\n$4�%�\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz��������������������������������������������������������������������������\0\0\0?\0ͷ��E�o�~+�G�m;J����g؎ y�$��8�zu�������^�\0����y	x�\0�W��@�q5(^;�xjJ��-�4�>\"��@�̺���A�DДP=?��zg�|Ui��_�F`�˞��LNq��\rd�]���G����u��9�Yx��I�R�Q��i���Ŗ�z���\0��>����ӊ��\Z���G=(��ln���#̱�vz	+�׍����v��=R+�^����!RTLX�UpGB^09⾑׵m��;j~\"�,�]=87�,jO������_&|d��>|X�\'��iW��ŭ�gPh<�^2�pD?�c��H��s\\��:��wG^��/{fR���������Og�M��G�\'v[��Ċ���|?k��G�^�b�M:�[w��S��Q����o��8��/�t�-6�D�$	i��F�WH�\"�	8���^�/~���K�.�hvk�LwC+�C|��Z��G��pp�ГN}ٌ��t����Ba\'���|5����K����ͯ���!n4�b� x��\0u�׃��@�Ez\Z��>���X���K�:���}�w6��䥴Y�#E�\0\0:�O&���:Ѣ���$P�m�����d1�>��ۏƊ+�	-�y��Eֻ���?�o�D>��p���^�*��-èoܡ8�4&\\㒱���?7.�f�����F�����&�C�wc�I�$�(����^�o�zC��&����q9��(����?��','2012-02-29 22:18:27','2012-02-29 22:18:27'),('i_40',17302793980867049595,'����\0JFIF\0\0\0\0\0\0��\0;CREATOR: gd-jpeg v1.0 (using IJG JPEG v62), quality = 90\n��\0C\0\n\n\n\r\r��\0C		\r\r��\0\0(\0\"\0��\0\0\0\0\0\0\0\0\0\0\0	\n��\0�\0\0\0}\0!1AQa\"q2���#B��R��$3br�	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz���������������������������������������������������������������������������\0\0\0\0\0\0\0\0	\n��\0�\0\0w\0!1AQaq\"2�B����	#3R�br�\n$4�%�\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz��������������������������������������������������������������������������\0\0\0?\0���[�d�[�|\\���h~$������&����\nB��F\0�d���Ҽ�8�0ȯ��_�����8xJK}��X��`�T��\\K$\0�3��c�xⲩ\'鹥$�%ͱ���F|=�~|]״�O��:}�K�|�R���P[$���^g���ԏ���O��߃�������5�)l�[T.Y��9vRQ��q����/���\Z���?��W��<�e=���(��J�=�Dt\Zsn�|	��Z��\',񾁧;�7\\��_��\Zi��^�2+���ߢ~ζQ���\\f����~e�`���ւ�tl��*��??�\n���\n���R&8=6�s����I���ҏ�)ֳ%�׃4h�H#Sd璱*��\0�u�|z���X���D�se\'n���>�B�`Np9�}���hO�:Յ�iq�ٛ\\��Ltq�X`�w�VmjZzX�����u�$ڍ�r�����\0\"�<�\0���T~�ڞ�2�A#���*_cJZ3��','2012-02-29 22:18:27','2012-02-29 22:18:27');
/*!40000 ALTER TABLE `qa_cache` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-03 14:25:08
