

DROP TABLE IF EXISTS `_BANK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_BANK` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `location` varchar(255) CHARACTER SET latin1 NOT NULL,
  `version` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pob4j2cq8phjbqbvi2hkj79ta` (`location`),
  UNIQUE KEY `UK_ci45miw0qwvswhtgr3d6t4ajs` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_BANK`
--

LOCK TABLES `_BANK` WRITE;
/*!40000 ALTER TABLE `_BANK` DISABLE KEYS */;
INSERT INTO `_BANK` VALUES (4,'Serg home 1',1,'72.0.202.3'),(6,'Serg home 2',0,'72.0.202.4'),(7,'Serg home 3',0,'72.0.202.5'),(8,'Oleg home1',0,'72.0.202.19'),(9,'Oleg home 2',1,'72.0.202.21');
/*!40000 ALTER TABLE `_BANK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_BOX`
--

DROP TABLE IF EXISTS `_BOX`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_BOX` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `capacity` int(11) NOT NULL,
  `descr` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `disposition` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET latin1 NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r59ndyn87p6979sy3i2wakjfg` (`ip`),
  UNIQUE KEY `UK_9cjw5ax9u5ynkh21t415gpsx9` (`disposition`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_BOX`
--

LOCK TABLES `_BOX` WRITE;
/*!40000 ALTER TABLE `_BOX` DISABLE KEYS */;
INSERT INTO `_BOX` VALUES (1,4,'first 1','at home','192.168.200.45',1),(3,4,'balk','balk','192.168.200.46',0),(4,8,'4323111','sdfadsf','192.168.4.138',4),(5,8,'ffddasdfasd','ffsdf','192.168.5.102',1),(6,4,'0987','gggg','192.168.5.206',4),(7,4,'??','???','192.168.5.230',0),(8,4,'ff','dd','192.168.6.70',0),(9,8,'fake','ppdadfadf','1.1.1.1',0),(10,8,'172.17.1.34','sfsdf','172.17.1.34',1),(11,8,'172.17.1.36','vova','172.17.1.36',0),(14,8,'172.17.1.38','gfadfg','172.17.1.38',0);
/*!40000 ALTER TABLE `_BOX` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_CARD`
--

DROP TABLE IF EXISTS `_CARD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_CARD` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `number` varchar(10) DEFAULT NULL,
  `place` int(11) DEFAULT NULL,
  `sernumber` varchar(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `bank_id` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `channelId` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  `activation` datetime DEFAULT NULL,
  `blockdate` datetime DEFAULT NULL,
  `blocked` bit(1) NOT NULL,
  `descr` varchar(255) DEFAULT NULL,
  `dlimit` int(11) NOT NULL,
  `mlimit` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_3o760n3nclfuodpsr7dywfso6` (`name`),
  UNIQUE KEY `UK_mo5o7okef60go9skvfs2ab8ue` (`sernumber`),
  UNIQUE KEY `UK_2jy6lvkmo9ksehe81hm0r1qwf` (`bank_id`,`place`),
  UNIQUE KEY `UK_agi3qve42789fb2cpkbdy0pqe` (`number`),
  KEY `FK_fbg18qyrq29oc4cpbrxxqqwgo` (`group_id`),
  CONSTRAINT `FK_5s24x4pqeq8y3bqhm5qy7sl7o` FOREIGN KEY (`bank_id`) REFERENCES `_BANK` (`id`),
  CONSTRAINT `FK_fbg18qyrq29oc4cpbrxxqqwgo` FOREIGN KEY (`group_id`) REFERENCES `_GRP` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_CARD`
--

LOCK TABLES `_CARD` WRITE;
/*!40000 ALTER TABLE `_CARD` DISABLE KEYS */;
INSERT INTO `_CARD` VALUES (71,'mgf512','9295427508',18,'013065004353197',1,8,7,0,'',NULL,NULL,'\0','067670 16.04 teplypriem',66,2581),(72,'mgf513','9299166749',19,'352846038141057',1,8,7,0,'',NULL,NULL,'\0','665051 16.04 teplypriem',51,2581),(73,'mgf516','9264730243',22,'013065007359829',1,8,7,0,'',NULL,NULL,'\0','805151 22.04/09.05 teplypriem ballance -4',56,2581),(74,'mgf40d','9268858519',29,'868267016047991',1,6,7,0,'',NULL,NULL,'\0','123123 20.02 perehodi_na_nol_100 17.08.15',61,2588),(75,'mgf411','9258214596',17,'358967049568148',1,6,7,0,'',NULL,NULL,'\0','516805 04.05 teplypriem+',64,2581),(76,'mgf31a','9295019783',26,'358842009754121',1,4,7,0,'',NULL,NULL,'\0','015109 25.02/11.03 teplypriem+',63,2581),(77,'mgf51a','9264726872',26,'359717018382453',1,8,7,0,'',NULL,NULL,'\0','751510 22.04/09.05 teplypriem',53,2581),(78,'mgf402','9295110082',2,'013065003127881',1,6,7,0,'',NULL,NULL,'\0','156805 15.12/12.01 ',52,2581),(79,'mgf30b','9256715134',11,'356095003971553',1,4,7,0,'',NULL,NULL,'\0','577989 24.02/08.03 teplypriem',42,2581),(80,'mgf104','9258729221',4,'352787024381549',1,7,7,0,'',NULL,NULL,'\0','590965 17.03/27.03 none',61,2581),(81,'mgf10e','9256715654',14,'355241033875122',1,7,7,0,'',NULL,NULL,'\0','167611 20.03/29.03 none',55,2581),(82,'mgf111','9256715173',17,'355241034998162',1,7,7,0,'',NULL,NULL,'\0','997981 20.03/29.03 none',60,2581),(83,'mgf112','9258728349',18,'352921024967714',1,7,7,0,'',NULL,NULL,'\0','917658 20.03/29.03 none',55,2581),(84,'mgf801','9266099472',1,'356810033966310',1,9,7,0,'',NULL,NULL,'\0','595878 24.03/05.04 none',63,2581),(85,'mgf805','9257361489',5,'358842003600213',1,9,7,0,'',NULL,NULL,'\0','878905 24.03/05.04 none',44,2581),(86,'mgf11a','9258732095',26,'490110704977127',1,7,7,0,'',NULL,NULL,'\0','180961 25.03/05.04 none',38,2581),(87,'mgf807','9266099465',7,'354010003881902',1,9,7,0,'',NULL,NULL,'\0','981680 25.03/05.04 none',48,2581),(88,'mgf809','9266099463',9,'352787020651283',3,9,7,0,'',NULL,NULL,'\0','787116 25.03/05.04 none',40,2581),(89,'mgf40b','9254720603',11,'354010009944712',1,6,7,0,'',NULL,NULL,'\0','non 27.05/11.06 none',44,2581),(90,'mgf309','9265524218',9,'868267014188433',1,4,7,0,'',NULL,NULL,'\0','585059 05.04/15.04 none',40,2581),(91,'mgf315','9265526719',21,'013065004871552',1,4,7,0,'',NULL,NULL,'\0','105516 05.04/15.04 none',62,2581),(92,'mgf410','9265486939',16,'359614045879637',1,6,7,0,'',NULL,NULL,'\0','501158 06.04/15.04 none',63,2581),(93,'mgf414','9256717639',20,'358343045311794',3,6,7,0,'',NULL,NULL,'\0','670019 06.04/15.04 none',67,2581),(94,'mgf41a','9265890285',26,'358967041976349',1,6,7,0,'',NULL,NULL,'\0','598151 06.04/20.04 none',52,2581),(95,'mgf41c','9256307362',28,'356095009312778',1,6,7,0,'',NULL,NULL,'\0','978167 06.04/20.04 none',41,2581),(96,'mgf41f','9265487814',31,'356095009048810',1,6,7,0,'',NULL,NULL,'\0','8fipW6 08.04/20.04 none',67,2581),(97,'mgf420','9266438845',32,'356810032108799',1,6,7,0,'',NULL,NULL,'\0','tfh7cm 08.04/20.04 none',64,2581),(98,'mgf102','9256715459',2,'450060340840775',1,7,7,0,'',NULL,NULL,'\0','Vd4YAt 08.04/20.04 none',58,2581);
/*!40000 ALTER TABLE `_CARD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_CHANNEL`
--

DROP TABLE IF EXISTS `_CHANNEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_CHANNEL` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) NOT NULL,
  `line` int(11) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `box_id` bigint(20) NOT NULL,
  `card` bigint(20) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_h281mn1a65exl7qcg1jk4kxit` (`name`),
  UNIQUE KEY `UK_4x56qbtt93yr61ty357p3j521` (`box_id`,`line`),
  UNIQUE KEY `UK_m4rnxpbpjmy0uhj6jje0rl7v8` (`card`),
  KEY `FK_9s89uj3tv3mwk6gq3jfnch9dx` (`group_id`),
  CONSTRAINT `FK_9s89uj3tv3mwk6gq3jfnch9dx` FOREIGN KEY (`group_id`) REFERENCES `_GRP` (`id`),
  CONSTRAINT `FK_em0rt76ep3sg78o2whydlhs37` FOREIGN KEY (`box_id`) REFERENCES `_BOX` (`id`),
  CONSTRAINT `FK_m4rnxpbpjmy0uhj6jje0rl7v8` FOREIGN KEY (`card`) REFERENCES `_CARD` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_CHANNEL`
--

LOCK TABLES `_CHANNEL` WRITE;
/*!40000 ALTER TABLE `_CHANNEL` DISABLE KEYS */;
INSERT INTO `_CHANNEL` VALUES (87,'',0,'bln41',1,7,NULL,4),(88,'',1,'bln79',1,11,NULL,4),(89,'',7,'bln94',1,10,NULL,4),(90,'',5,'bln91',1,4,NULL,4),(91,'',0,'bln81',1,8,NULL,4),(92,'',4,'bln75',1,11,NULL,4),(93,'',3,'bln74',1,11,NULL,4),(94,'',1,'bln34',1,5,NULL,4),(95,'',2,'bln43',1,4,NULL,4),(96,'',4,'bln80',1,14,NULL,4),(97,'',2,'bln45',1,3,NULL,4),(98,'',2,'bln32',1,14,NULL,4),(99,'',1,'bln44',1,3,NULL,4),(100,'',6,'bln33',1,5,NULL,4),(101,'',2,'bln84',1,5,NULL,4),(102,'',7,'bln31',1,11,NULL,4),(103,'',4,'bln92',1,10,NULL,4),(104,'',1,'bln90',1,14,NULL,4),(105,'',0,'bln87',1,1,NULL,4),(106,'',1,'mts42',1,6,NULL,1),(107,'',6,'mts88',1,10,NULL,1),(108,'',3,'mts94',1,10,NULL,1),(109,'',6,'mts75',1,14,NULL,1),(110,'',2,'mts91',1,10,NULL,1),(111,'',3,'mts89',1,6,NULL,1),(112,'',0,'mts80',1,11,NULL,1),(113,'',6,'mts85',1,4,NULL,1),(114,'',2,'mts84',1,11,NULL,1),(115,'',0,'mts74',1,14,NULL,1),(116,'',0,'mts79',1,10,NULL,1),(117,'',1,'mts46',1,8,NULL,1),(118,'',2,'mts43',1,7,NULL,1),(119,'',3,'mts78',1,14,NULL,1),(120,'',3,'mts47',1,8,NULL,1),(121,'',5,'mts81',1,5,NULL,1),(122,'',1,'mts70',1,10,NULL,1),(123,'',6,'mts44',1,11,NULL,1),(124,'',1,'mts77',1,4,NULL,1),(125,'',1,'mts41',1,7,NULL,1),(126,'',5,'mgf92',2,10,NULL,7),(127,'',5,'mgf73',2,11,NULL,7),(128,'',5,'mgf71',2,14,NULL,7),(129,'',7,'mgf46',2,14,NULL,7),(130,'',1,'mgf36',2,1,NULL,7),(131,'',2,'mgf30',2,1,NULL,7),(132,'',3,'mgf34',2,1,NULL,7),(133,'',0,'mgf43',2,3,NULL,7),(134,'',3,'mgf81',2,3,NULL,7),(135,'',0,'mgf87',1,4,NULL,7),(136,'',3,'mgf80',1,4,NULL,7),(137,'',4,'mgf49',1,4,NULL,7),(138,'',7,'mgf35',1,4,NULL,7),(139,'',0,'mgf85',1,5,NULL,7),(140,'',3,'mgf86',1,5,NULL,7),(141,'',4,'mgf75',1,5,NULL,7),(142,'',7,'mgf70',1,5,NULL,7),(143,'',0,'mgf37',1,6,NULL,7),(144,'',2,'mgf38',1,6,NULL,7),(145,'',3,'mgf93',1,7,NULL,7),(146,'',2,'mgf74',1,8,NULL,7);
/*!40000 ALTER TABLE `_CHANNEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_CHANNEL__TRUNK`
--

DROP TABLE IF EXISTS `_CHANNEL__TRUNK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_CHANNEL__TRUNK` (
  `channels_id` bigint(20) NOT NULL,
  `trunks_id` bigint(20) NOT NULL,
  PRIMARY KEY (`channels_id`,`trunks_id`),
  KEY `FK_5pib1fihst04tikff5uui3o7n` (`trunks_id`),
  CONSTRAINT `FK_5nl7mw5moqxaf9t3335pw1pul` FOREIGN KEY (`channels_id`) REFERENCES `_CHANNEL` (`id`),
  CONSTRAINT `FK_5pib1fihst04tikff5uui3o7n` FOREIGN KEY (`trunks_id`) REFERENCES `_TRUNK` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_CHANNEL__TRUNK`
--

LOCK TABLES `_CHANNEL__TRUNK` WRITE;
/*!40000 ALTER TABLE `_CHANNEL__TRUNK` DISABLE KEYS */;
INSERT INTO `_CHANNEL__TRUNK` VALUES (126,1),(127,1),(128,1),(129,1),(130,1),(131,1),(132,1),(133,1),(134,1);
/*!40000 ALTER TABLE `_CHANNEL__TRUNK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_GRP`
--

DROP TABLE IF EXISTS `_GRP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_GRP` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `oper` int(11) DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `descr` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4h2pb0vx4x8wbx9u7ykpwiq8e` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_GRP`
--

LOCK TABLES `_GRP` WRITE;
/*!40000 ALTER TABLE `_GRP` DISABLE KEYS */;
INSERT INTO `_GRP` VALUES (1,'red1',0,8,'red1 for first'),(2,'green212',1,5,'green2'),(4,'yellow3',2,3,'yellow3'),(7,'mgfrus',1,2,'mgfrus');
/*!40000 ALTER TABLE `_GRP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `_TRUNK`
--

DROP TABLE IF EXISTS `_TRUNK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `_TRUNK` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `descr` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9rc70ctuyf600xl39mt14qkcr` (`name`),
  UNIQUE KEY `UK_9jkkstux5m9k4rupwtvew3f6p` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `_TRUNK`
--

LOCK TABLES `_TRUNK` WRITE;
/*!40000 ALTER TABLE `_TRUNK` DISABLE KEYS */;
INSERT INTO `_TRUNK` VALUES (1,'trnk_mgfrus','trnk_mgfrus',5),(2,'descr1','tr2',1),(3,'descr3','tr3',0),(4,'test meg trunk','test_meg_trunk',1);
/*!40000 ALTER TABLE `_TRUNK` ENABLE KEYS */;
UNLOCK TABLES;
