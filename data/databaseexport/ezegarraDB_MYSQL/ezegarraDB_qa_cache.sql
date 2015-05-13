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
INSERT INTO `qa_cache` VALUES ('i_40',116924610228715924,'ÿØÿà\0JFIF\0\0\0\0\0\0ÿş\0;CREATOR: gd-jpeg v1.0 (using IJG JPEG v62), quality = 90\nÿÛ\0C\0\n\n\n\r\rÿÛ\0C		\r\rÿÀ\0\0(\0(\"\0ÿÄ\0\0\0\0\0\0\0\0\0\0\0	\nÿÄ\0µ\0\0\0}\0!1AQa\"q2‘¡#B±ÁRÑğ$3br‚	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzƒ„…†‡ˆ‰Š’“”•–—˜™š¢£¤¥¦§¨©ª²³´µ¶·¸¹ºÂÃÄÅÆÇÈÉÊÒÓÔÕÖ×ØÙÚáâãäåæçèéêñòóôõö÷øùúÿÄ\0\0\0\0\0\0\0\0	\nÿÄ\0µ\0\0w\0!1AQaq\"2B‘¡±Á	#3RğbrÑ\n$4á%ñ\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz‚ƒ„…†‡ˆ‰Š’“”•–—˜™š¢£¤¥¦§¨©ª²³´µ¶·¸¹ºÂÃÄÅÆÇÈÉÊÒÓÔÕÖ×ØÙÚâãäåæçèéêòóôõö÷øùúÿÚ\0\0\0?\0üª¢Š(­Óüd4! „4·J \rëò/ÌIï“Á¯Cø3ûIÿ\0Â¨×åÔŸD7æRÂHÒçËÜ¥@À;N0F{÷¨~|(“\\ğDş&šÚYl­š8.X°	|ÅLñ‘÷:ärËXGà¡ªë‹g¥É\n¬îİg“±8\n[€3§Š\0ú×Oÿ\0‚¯ø‹MÑŸIĞüûˆ\"ŸP²qĞ+§µÏ„?°MŸÀ©ãøñûYÒ¬<7¦CöÈtm:ñ.eº}Õr¼’>T,I#$¥\0|E¨»Ø(êx ±¿e|]ñ„µ\'øy¦é¾2Ò‚E­øNúA_ÃæÈWc¸eÀÁWs_I|ømğ#âù¼ğ¥¥ø‹á×Ä•Ç›¢k—oİ¹Ïü±*¬ªİ-Nµåÿ\0ğO?ÚDøYñRğôé¶i´Ô‰AlÒEX/üæ>Àâ¾÷¹×~~ÓÉe¥øÃÂÖZ¤\0¡Êà´ÑÈ3–ŠeÚñò8(AÎ(Äíÿ\0f/‡şÓ5Ÿø‡M\Z•ªÆ!º¿8{¨[¬rÄø!³É__|?ø¡x!’E¹Ôµf„m·“Wœ\\IöÈÄ\0ÎK`rIÍü×l :u©læû5ÔRà6Ç\r´ô89¢Š\0é<1ã+ÿ\0üAÓ|Nˆ³ŞYİ¥àFÊ¬£;±ìOäkôGà/ü³Áú%Ôú|>›LÕ.Û1ê3•›npX¤p0O\'ô¢Š\0öˆğR­#HÑ&:r¤³Ái¢FNŞy\09Ï@ØsÖŠ( ÿÙ','2012-02-25 13:00:26','2012-05-18 08:44:03'),('i_40',1289346026190210411,'ÿØÿà\0JFIF\0\0\0\0\0\0ÿş\0;CREATOR: gd-jpeg v1.0 (using IJG JPEG v62), quality = 90\nÿÛ\0C\0\n\n\n\r\rÿÛ\0C		\r\rÿÀ\0\0(\0%\"\0ÿÄ\0\0\0\0\0\0\0\0\0\0\0	\nÿÄ\0µ\0\0\0}\0!1AQa\"q2‘¡#B±ÁRÑğ$3br‚	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzƒ„…†‡ˆ‰Š’“”•–—˜™š¢£¤¥¦§¨©ª²³´µ¶·¸¹ºÂÃÄÅÆÇÈÉÊÒÓÔÕÖ×ØÙÚáâãäåæçèéêñòóôõö÷øùúÿÄ\0\0\0\0\0\0\0\0	\nÿÄ\0µ\0\0w\0!1AQaq\"2B‘¡±Á	#3RğbrÑ\n$4á%ñ\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz‚ƒ„…†‡ˆ‰Š’“”•–—˜™š¢£¤¥¦§¨©ª²³´µ¶·¸¹ºÂÃÄÅÆÇÈÉÊÒÓÔÕÖ×ØÙÚâãäåæçèéêòóôõö÷øùúÿÚ\0\0\0?\0Í·°ÏE®oÅ~+—Gºm;J²úš¨gØ yÇ$‘Î8ã½zu®“Àâ¼÷Ãú^ÿ\0ê’“ı¢y	xØ\0ÅWŸ÷@â¹q5(^;xjJ¤í-4Ô>\"øË@“Ìºµ´AËDĞ”P=?‰Ízg|UiãÍ_ÛF`•ËİÎLNqã\rdü]ğá†ÆGòö©Éu×û9ê±Yx¶÷I–R‡Q‹÷i´ŸŞÅ–Æz¹ÿ\0€×>¼§¤ÙÓŠÃÆ\ZÁĞöG=(­éln™¢½#Ì±èvz	+Ê×ø¯Áúv‡ã=R+é^Ò„í!RTLX€UpGB^09â¾‘×µmÀº;j~\"Õ,ô]=87³,jO Éù›Ğ“_&|dı¢>|Xñ\'‡ôiWúõÅ­ËgPh<¸^2§pD?¼cáHÁÆs\\µé:±÷wG^ª¥/{fRø’Şğî—ÅúêOgåM”»G‡\'v[ŒãÄŠËı–|?k®üGñ^§b‚M:Ê[w¼¦SÁİQ¹÷÷­o¾8°ñ/‡tû-6ÇDó$	i§ÛF×WHù\"‚	8ãóï^Å/~Îí×Kò.õhvk¶LwC+ÈC|Z„‡GßÔppÁĞ“N}ÙŒ«ùtÔûÖçBa\'İÍÌ|5ı¦şüKĞÍôšÍ¯†¯¢!n4íbá xØÿ\0u‰×ƒÈü@éEz\Z£Ê>ø«ñXøÃã½KÄ:ÅÌÓ}¢w6ÖÎä¥´Yù#Eè\0\0:O&»ßÙ:Ñ¢ı ü$P™mïÅÊÀ½d1£>ÑõÛÆŠ+¾	-yê™öEÖ»ğ÷á?ÂoüD>Óì¼pº´Ú^™*’é-Ã¨oÜ¡84&\\ã’±õù°?7.ìf¿¿šòîF¸»º‘î&šC–wc’I÷$š(¬ğíı^šo¢zC–•&üÊóèq9Ğî(¢ŠÖÃ?ÿÙ','2012-02-29 22:18:27','2012-02-29 22:18:27'),('i_40',17302793980867049595,'ÿØÿà\0JFIF\0\0\0\0\0\0ÿş\0;CREATOR: gd-jpeg v1.0 (using IJG JPEG v62), quality = 90\nÿÛ\0C\0\n\n\n\r\rÿÛ\0C		\r\rÿÀ\0\0(\0\"\0ÿÄ\0\0\0\0\0\0\0\0\0\0\0	\nÿÄ\0µ\0\0\0}\0!1AQa\"q2‘¡#B±ÁRÑğ$3br‚	\n\Z%&\'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyzƒ„…†‡ˆ‰Š’“”•–—˜™š¢£¤¥¦§¨©ª²³´µ¶·¸¹ºÂÃÄÅÆÇÈÉÊÒÓÔÕÖ×ØÙÚáâãäåæçèéêñòóôõö÷øùúÿÄ\0\0\0\0\0\0\0\0	\nÿÄ\0µ\0\0w\0!1AQaq\"2B‘¡±Á	#3RğbrÑ\n$4á%ñ\Z&\'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz‚ƒ„…†‡ˆ‰Š’“”•–—˜™š¢£¤¥¦§¨©ª²³´µ¶·¸¹ºÂÃÄÅÆÇÈÉÊÒÓÔÕÖ×ØÙÚâãäåæçèéêòóôõö÷øùúÿÚ\0\0\0?\0ü«½[ödø[£|\\øµ¦h~$»»²ğîÉ&¿šÀ´\nBˆşF\0ïdûÃÏÒ¼º8÷0È¯İ€_³Ÿƒüû8xJK}ÂÛX¼Ñ`“T¿Š\\K$\0³3ıã†cxâ²©\'é¹¥$¥%Í±øíûF|=Ğ~|]×´ßO Û:}–K°|õR€ííP[$œŒ^gŒõ¯ÔÛóàO„ôßƒš¿ˆ ğ×Ù5İ)læ[T.Y…È9vRQ†àqùŸ¥/ØÙî\ZÖÖõ?ÕùW•Á<äe=¾œı(„ïJœ=ëDt\Zsnı|	¸ƒZøà\',ñ¾§;²7\\Ãí_…¾\Zi¶ì^½2+õçöß¢~Î¶QÜÎÒ\\f‘œœá~eŒ`Šƒğ¨¨Ö‚„tlóø*¼–??²\nú¡\n„œíR&8=6¼sëŠü—I˜¯Òø)Ö³%í×ƒ4hØH#Sdç’±*œÿ\0ßuğ|z…XäñÅD£se\'n§³è>ŒB¸`Np9Í}¡ğâhOƒ:Õ…´iq§Ù›\\±âLtqéX`úwÍVmjZzXù³ãş¯uã¯$ÚÜr½‰–ÏÌ\0\"ñ<¤\0¤ˆT~äÚ˜2»A#Œ×âŠ*_cJZ3ÿÙ','2012-02-29 22:18:27','2012-02-29 22:18:27');
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
