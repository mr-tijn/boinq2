-- MySQL dump 10.13  Distrib 5.7.20, for osx10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: boinq
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.22-MariaDB-1~jessie

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
-- Table structure for table `databasechangelog`
--

DROP TABLE IF EXISTS `databasechangelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `databasechangelog`
--

LOCK TABLES `databasechangelog` WRITE;
/*!40000 ALTER TABLE `databasechangelog` DISABLE KEYS */;
INSERT INTO `databasechangelog` VALUES ('00000000000001','jhipster','classpath:config/liquibase/changelog/00000000000000_initial_schema.xml','2017-07-13 10:18:04',1,'EXECUTED','7:6f8b16e01166e5004d9909209c06b873','createTable, createIndex (x2), createTable (x2), addPrimaryKey, createTable, addForeignKeyConstraint (x3), loadData, dropDefaultValue, loadData (x2), createTable (x2), addPrimaryKey, createIndex (x2), addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-1','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:04',2,'EXECUTED','7:6112f92b437b4d0ecfcdf038fd04ed2f','createTable','',NULL,'3.3.2'),('1441697573365-3','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:04',3,'EXECUTED','7:18babd20b8222790af3e48953e46ea64','createTable','',NULL,'3.3.2'),('1441697573365-4','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:05',4,'EXECUTED','7:7ac2e675fbac0f2e2d66eb7837077189','createTable','',NULL,'3.3.2'),('1441697573365-8','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:05',5,'EXECUTED','7:3286064708b955b0098846809d95aaf0','createTable','',NULL,'3.3.2'),('1441697573365-9','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:05',6,'EXECUTED','7:7839055a7c7494e22bec41d8b5be7818','createTable','',NULL,'3.3.2'),('1441697573365-10','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:05',7,'EXECUTED','7:3b7d72d2453bf6782b87afc61b575948','createTable','',NULL,'3.3.2'),('1441697573365-11','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:05',8,'EXECUTED','7:39182a5bcf1a94ab7235fad2e655a6e0','createTable','',NULL,'3.3.2'),('1441697573365-12','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:06',9,'EXECUTED','7:c8ec756bccfe4ea61bc073eebd86a756','createTable','',NULL,'3.3.2'),('1441697573365-13','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:06',10,'EXECUTED','7:790d767fb645398445e7c71622ae2655','createTable','',NULL,'3.3.2'),('1441697573365-22','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:06',11,'EXECUTED','7:0c77ed189e0fb3ac7f10ff4bccc557f7','addUniqueConstraint','',NULL,'3.3.2'),('1441697573365-23','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:06',12,'EXECUTED','7:03687900ea4d1e6395c83486163aa6d9','addUniqueConstraint','',NULL,'3.3.2'),('1441697573365-25','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:07',13,'EXECUTED','7:d67ad99631f55404d84cc90c27722024','addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-26','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:07',14,'EXECUTED','7:08d29ce6c10c1a5e4ba2aac16f54f5d0','addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-27','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:08',15,'EXECUTED','7:f4c8bf45c768bec5423e23dcdcb588e0','addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-28','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:08',16,'EXECUTED','7:2cbd3405637cf30e021bdf4113d38c70','addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-29','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:09',17,'EXECUTED','7:177b5def6ff758e590b4d3883a15b7e2','addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-31','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:09',18,'EXECUTED','7:bc0b7ed8e9cd9f0bcf79c1c75f98e7c0','addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-32','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:10',19,'EXECUTED','7:a7d83eb574b7691b0d7758d9b2b56a0e','addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-33','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:10',20,'EXECUTED','7:ae3424034f60845abe02f080d5df8674','addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-34','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:11',21,'EXECUTED','7:8af8871bdfef8d2ef66ad50c9308cdd6','addForeignKeyConstraint','',NULL,'3.3.2'),('1441697573365-35','martijn (generated)','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:11',22,'EXECUTED','7:3c7813f7cecbde47d8014699ccdb89c4','addForeignKeyConstraint','',NULL,'3.3.2'),('martijnmanual001','martijn','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:11',23,'EXECUTED','7:34ba3998e8daad5bf20670a079f71d10','loadData (x3)','',NULL,'3.3.2'),('create featurequery tables','martijn','classpath:config/liquibase/changelog/boinqInit.xml','2017-07-13 10:18:16',24,'EXECUTED','7:bc03386df59b60f1840930f0f6b507ae','createTable (x4), addForeignKeyConstraint (x7)','',NULL,'3.3.2'),('add columns for feature type criterion','martijn','classpath:config/liquibase/changelog/martijn_0001.xml','2017-07-13 10:18:16',25,'EXECUTED','7:5262930b104832e89efa992ef1582f52','addColumn','',NULL,'3.3.2'),('add index column to featureselect','martijn','classpath:config/liquibase/changelog/martijn_0002.xml','2017-07-13 10:18:16',26,'EXECUTED','7:1fc3a9f13d7434b0a72a43d65105b4d2','addColumn','',NULL,'3.3.2'),('add name column to featurequery','martijn','classpath:config/liquibase/changelog/martijn_0002.xml','2017-07-13 10:18:17',27,'EXECUTED','7:3324d02c2c8fa5145b191a48773c46aa','addColumn','',NULL,'3.3.2'),('martijn0003.1','martijn','classpath:config/liquibase/changelog/martijn_0003.xml','2017-07-13 10:18:17',28,'EXECUTED','7:6cee8e0e6fc58e62d1b531624eb49e53','addColumn, update','',NULL,'3.3.2'),('add columns for field match criterion','martijn','classpath:config/liquibase/changelog/martijn_0004.xml','2017-07-13 10:18:17',29,'EXECUTED','7:54716446064331fcaf07d12f40d9677e','addColumn','',NULL,'3.3.2'),('add column for strand match','martijn','classpath:config/liquibase/changelog/martijn_0004.xml','2017-07-13 10:18:18',30,'EXECUTED','7:0e47eb3ade337128655691e9a021199a','addColumn','',NULL,'3.3.2'),('add columns to featurequery','martijn','classpath:config/liquibase/changelog/martijn_0004.xml','2017-07-13 10:18:18',31,'EXECUTED','7:e0918717ab1b88081d3e806ac428e5a8','addColumn','',NULL,'3.3.2'),('add analysis table','martijn','classpath:config/liquibase/changelog/martijn_0004.xml','2017-07-13 10:18:19',32,'EXECUTED','7:c00c0a6c532743c40a17ec4a3fb4ee7b','createTable (x2), addForeignKeyConstraint','',NULL,'3.3.2'),('add connector table','martijn','classpath:config/liquibase/changelog/martijn_0004.xml','2017-07-13 10:18:21',33,'EXECUTED','7:ed4692eca8bdfc7b8fb24fe1ca6fc413','createTable, addColumn, addForeignKeyConstraint (x2)','',NULL,'3.3.2'),('add template tables','martijn','classpath:config/liquibase/changelog/graphengine.xml','2017-07-13 10:18:32',34,'EXECUTED','7:27b9e34e18b94bd2c3c3f250d21f0bf1','createTable (x3), addForeignKeyConstraint (x3), createTable, addForeignKeyConstraint, createTable, addForeignKeyConstraint (x2), createTable, addForeignKeyConstraint, createTable, addForeignKeyConstraint, createTable, addForeignKeyConstraint (x4),...','',NULL,'3.3.2'),('import graphengine data','martijn','classpath:config/liquibase/changelog/graphengine.xml','2017-07-13 10:18:32',35,'EXECUTED','7:6c62ef4265cd7bfd262c187b27a1c929','loadData (x3)','',NULL,'3.3.2'),('add reference map','martijn','classpath:config/liquibase/changelog/createReferenceMap.xml','2017-12-26 09:21:00',36,'EXECUTED','7:d40cad54056bc44eb35824f0b8f48031','createTable, addForeignKeyConstraint','',NULL,'3.3.2');
/*!40000 ALTER TABLE `databasechangelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `databasechangeloglock`
--

DROP TABLE IF EXISTS `databasechangeloglock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `databasechangeloglock`
--

LOCK TABLES `databasechangeloglock` WRITE;
/*!40000 ALTER TABLE `databasechangeloglock` DISABLE KEYS */;
INSERT INTO `databasechangeloglock` VALUES (1,'\0',NULL,NULL);
/*!40000 ALTER TABLE `databasechangeloglock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('T_RAWDATAFILE',11);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_authority`
--

DROP TABLE IF EXISTS `jhi_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_authority` (
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_authority`
--

LOCK TABLES `jhi_authority` WRITE;
/*!40000 ALTER TABLE `jhi_authority` DISABLE KEYS */;
INSERT INTO `jhi_authority` VALUES ('ROLE_ADMIN'),('ROLE_USER');
/*!40000 ALTER TABLE `jhi_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_persistent_audit_event`
--

DROP TABLE IF EXISTS `jhi_persistent_audit_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_persistent_audit_event` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `principal` varchar(255) NOT NULL,
  `event_date` timestamp NULL DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `idx_persistent_audit_event` (`principal`,`event_date`)
) ENGINE=InnoDB AUTO_INCREMENT=243 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_persistent_audit_event`
--

LOCK TABLES `jhi_persistent_audit_event` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_event` DISABLE KEYS */;
INSERT INTO `jhi_persistent_audit_event` VALUES (1,'anonymousUser','2017-07-13 12:23:36','AUTHORIZATION_FAILURE'),(2,'anonymousUser','2017-07-13 12:23:36','AUTHORIZATION_FAILURE'),(3,'admin','2017-07-13 12:23:45','AUTHENTICATION_SUCCESS'),(4,'admin','2017-07-13 12:23:45','AUTHENTICATION_SUCCESS'),(5,'admin','2017-07-13 12:23:45','AUTHENTICATION_SUCCESS'),(6,'admin','2017-07-13 13:17:35','AUTHENTICATION_SUCCESS'),(7,'admin','2017-07-13 13:17:35','AUTHENTICATION_SUCCESS'),(8,'anonymousUser','2017-07-13 13:17:42','AUTHORIZATION_FAILURE'),(9,'admin','2017-07-13 13:17:48','AUTHENTICATION_SUCCESS'),(10,'admin','2017-07-13 13:17:48','AUTHENTICATION_SUCCESS'),(11,'admin','2017-07-13 13:17:49','AUTHENTICATION_SUCCESS'),(12,'admin','2017-07-13 14:56:52','AUTHENTICATION_SUCCESS'),(13,'admin','2017-07-13 15:11:16','AUTHENTICATION_SUCCESS'),(14,'anonymousUser','2017-09-23 20:28:47','AUTHORIZATION_FAILURE'),(15,'anonymousUser','2017-09-23 20:28:47','AUTHORIZATION_FAILURE'),(16,'admin','2017-09-23 20:28:53','AUTHENTICATION_SUCCESS'),(17,'admin','2017-09-23 20:28:53','AUTHENTICATION_SUCCESS'),(18,'admin','2017-09-23 20:28:53','AUTHENTICATION_SUCCESS'),(19,'admin','2017-09-23 21:28:13','AUTHENTICATION_SUCCESS'),(20,'admin','2017-09-23 21:28:13','AUTHENTICATION_SUCCESS'),(21,'anonymousUser','2017-10-04 18:18:52','AUTHORIZATION_FAILURE'),(22,'anonymousUser','2017-10-04 18:18:52','AUTHORIZATION_FAILURE'),(23,'admin','2017-10-04 18:18:56','AUTHENTICATION_SUCCESS'),(24,'admin','2017-10-04 18:18:56','AUTHENTICATION_SUCCESS'),(25,'admin','2017-10-04 18:18:56','AUTHENTICATION_SUCCESS'),(26,'admin','2017-10-04 18:30:33','AUTHENTICATION_SUCCESS'),(27,'admin','2017-10-05 11:27:02','AUTHENTICATION_SUCCESS'),(28,'admin','2017-10-05 11:27:02','AUTHENTICATION_SUCCESS'),(29,'admin','2017-10-05 11:27:07','AUTHENTICATION_SUCCESS'),(30,'admin','2017-10-05 11:27:07','AUTHENTICATION_SUCCESS'),(31,'admin','2017-10-05 11:27:07','AUTHENTICATION_SUCCESS'),(32,'admin','2017-10-05 13:30:16','AUTHENTICATION_SUCCESS'),(33,'admin','2017-10-05 14:15:18','AUTHENTICATION_SUCCESS'),(34,'admin','2017-10-05 14:15:18','AUTHENTICATION_SUCCESS'),(35,'anonymousUser','2017-10-05 17:03:59','AUTHORIZATION_FAILURE'),(36,'anonymousUser','2017-10-05 17:04:00','AUTHORIZATION_FAILURE'),(37,'anonymousUser','2017-10-05 17:04:00','AUTHORIZATION_FAILURE'),(38,'admin','2017-10-05 17:04:03','AUTHENTICATION_SUCCESS'),(39,'admin','2017-10-05 17:04:03','AUTHENTICATION_SUCCESS'),(40,'admin','2017-10-05 17:04:04','AUTHENTICATION_SUCCESS'),(41,'admin','2017-10-05 18:19:28','AUTHENTICATION_SUCCESS'),(42,'admin','2017-10-20 16:38:34','AUTHENTICATION_SUCCESS'),(43,'admin','2017-10-24 13:27:42','AUTHENTICATION_SUCCESS'),(44,'admin','2017-10-24 14:06:37','AUTHENTICATION_SUCCESS'),(45,'admin','2017-10-24 15:36:14','AUTHENTICATION_SUCCESS'),(46,'admin','2017-10-25 14:40:21','AUTHENTICATION_SUCCESS'),(47,'anonymousUser','2017-12-11 10:11:15','AUTHORIZATION_FAILURE'),(48,'anonymousUser','2017-12-11 10:11:15','AUTHORIZATION_FAILURE'),(49,'admin','2017-12-11 10:11:22','AUTHENTICATION_SUCCESS'),(50,'admin','2017-12-11 10:11:22','AUTHENTICATION_SUCCESS'),(51,'admin','2017-12-11 10:11:22','AUTHENTICATION_SUCCESS'),(52,'admin','2017-12-12 10:46:43','AUTHENTICATION_SUCCESS'),(53,'anonymousUser','2017-12-12 10:49:11','AUTHORIZATION_FAILURE'),(54,'anonymousUser','2017-12-12 10:49:11','AUTHORIZATION_FAILURE'),(55,'admin','2017-12-12 10:49:15','AUTHENTICATION_SUCCESS'),(56,'admin','2017-12-12 10:49:15','AUTHENTICATION_SUCCESS'),(57,'admin','2017-12-12 10:49:15','AUTHENTICATION_SUCCESS'),(58,'admin','2017-12-12 11:12:07','AUTHENTICATION_SUCCESS'),(59,'anonymousUser','2017-12-12 11:12:33','AUTHORIZATION_FAILURE'),(60,'admin','2017-12-12 11:12:43','AUTHENTICATION_SUCCESS'),(61,'admin','2017-12-12 11:12:43','AUTHENTICATION_SUCCESS'),(62,'admin','2017-12-12 11:12:43','AUTHENTICATION_SUCCESS'),(63,'admin','2017-12-12 11:16:54','AUTHENTICATION_SUCCESS'),(64,'admin','2017-12-12 13:50:59','AUTHENTICATION_SUCCESS'),(65,'admin','2017-12-12 14:57:53','AUTHENTICATION_SUCCESS'),(66,'admin','2017-12-12 14:57:53','AUTHENTICATION_SUCCESS'),(67,'admin','2017-12-12 16:59:04','AUTHENTICATION_SUCCESS'),(68,'anonymousUser','2017-12-12 16:59:11','AUTHORIZATION_FAILURE'),(69,'admin','2017-12-12 16:59:17','AUTHENTICATION_SUCCESS'),(70,'admin','2017-12-12 16:59:17','AUTHENTICATION_SUCCESS'),(71,'admin','2017-12-12 16:59:17','AUTHENTICATION_SUCCESS'),(72,'admin','2017-12-14 20:39:08','AUTHENTICATION_SUCCESS'),(73,'anonymousUser','2017-12-14 20:39:14','AUTHORIZATION_FAILURE'),(74,'admin','2017-12-14 20:39:20','AUTHENTICATION_SUCCESS'),(75,'admin','2017-12-14 20:39:20','AUTHENTICATION_SUCCESS'),(76,'admin','2017-12-14 20:39:20','AUTHENTICATION_SUCCESS'),(77,'admin','2017-12-14 20:43:41','AUTHENTICATION_SUCCESS'),(78,'admin','2017-12-14 20:43:41','AUTHENTICATION_SUCCESS'),(79,'admin','2017-12-14 20:43:44','AUTHENTICATION_SUCCESS'),(80,'admin','2017-12-14 20:43:44','AUTHENTICATION_SUCCESS'),(81,'admin','2017-12-14 20:43:44','AUTHENTICATION_SUCCESS'),(82,'admin','2017-12-15 13:59:29','AUTHENTICATION_SUCCESS'),(83,'admin','2017-12-15 15:29:54','AUTHENTICATION_SUCCESS'),(84,'admin','2017-12-18 12:26:17','AUTHENTICATION_SUCCESS'),(85,'admin','2017-12-19 12:34:17','AUTHENTICATION_SUCCESS'),(86,'admin','2017-12-19 12:44:00','AUTHENTICATION_SUCCESS'),(87,'admin','2017-12-19 14:01:03','AUTHENTICATION_SUCCESS'),(88,'admin','2017-12-19 14:01:03','AUTHENTICATION_SUCCESS'),(89,'anonymousUser','2017-12-19 14:01:07','AUTHORIZATION_FAILURE'),(90,'anonymousUser','2017-12-19 14:01:10','AUTHORIZATION_FAILURE'),(91,'anonymousUser','2017-12-19 14:01:10','AUTHORIZATION_FAILURE'),(92,'anonymousUser','2017-12-20 11:18:19','AUTHORIZATION_FAILURE'),(93,'anonymousUser','2017-12-20 11:18:19','AUTHORIZATION_FAILURE'),(94,'admin','2017-12-20 11:18:26','AUTHENTICATION_SUCCESS'),(95,'admin','2017-12-20 11:18:26','AUTHENTICATION_SUCCESS'),(96,'admin','2017-12-20 11:18:26','AUTHENTICATION_SUCCESS'),(97,'admin','2017-12-20 16:19:28','AUTHENTICATION_SUCCESS'),(98,'admin','2017-12-20 18:01:11','AUTHENTICATION_SUCCESS'),(99,'admin','2017-12-20 18:43:41','AUTHENTICATION_SUCCESS'),(100,'admin','2017-12-20 18:43:41','AUTHENTICATION_SUCCESS'),(101,'admin','2017-12-20 18:43:43','AUTHENTICATION_SUCCESS'),(102,'admin','2017-12-20 18:43:43','AUTHENTICATION_SUCCESS'),(103,'admin','2017-12-20 18:43:43','AUTHENTICATION_SUCCESS'),(104,'admin','2017-12-20 21:34:05','AUTHENTICATION_SUCCESS'),(105,'admin','2017-12-20 21:34:05','AUTHENTICATION_SUCCESS'),(106,'admin','2017-12-21 10:53:53','AUTHENTICATION_SUCCESS'),(107,'admin','2017-12-21 11:42:43','AUTHENTICATION_SUCCESS'),(108,'admin','2017-12-21 11:42:43','AUTHENTICATION_SUCCESS'),(109,'admin','2017-12-26 10:22:53','AUTHENTICATION_SUCCESS'),(110,'admin','2017-12-26 11:31:23','AUTHENTICATION_SUCCESS'),(111,'admin','2017-12-26 11:31:23','AUTHENTICATION_SUCCESS'),(112,'anonymousUser','2017-12-26 11:31:27','AUTHORIZATION_FAILURE'),(113,'admin','2017-12-26 11:31:34','AUTHENTICATION_SUCCESS'),(114,'admin','2017-12-26 11:31:34','AUTHENTICATION_SUCCESS'),(115,'admin','2017-12-26 11:31:34','AUTHENTICATION_SUCCESS'),(116,'admin','2017-12-26 13:18:36','AUTHENTICATION_SUCCESS'),(117,'admin','2017-12-26 13:18:36','AUTHENTICATION_SUCCESS'),(118,'admin','2017-12-26 13:18:40','AUTHENTICATION_SUCCESS'),(119,'admin','2017-12-26 13:18:40','AUTHENTICATION_SUCCESS'),(120,'admin','2017-12-26 13:18:40','AUTHENTICATION_SUCCESS'),(121,'admin','2017-12-26 14:52:36','AUTHENTICATION_SUCCESS'),(122,'anonymousUser','2017-12-26 14:52:59','AUTHORIZATION_FAILURE'),(123,'admin','2017-12-26 14:53:08','AUTHENTICATION_SUCCESS'),(124,'admin','2017-12-26 14:53:08','AUTHENTICATION_SUCCESS'),(125,'admin','2017-12-26 14:53:08','AUTHENTICATION_SUCCESS'),(126,'admin','2017-12-26 15:35:03','AUTHENTICATION_SUCCESS'),(127,'admin','2017-12-26 15:35:03','AUTHENTICATION_SUCCESS'),(128,'admin','2017-12-26 18:04:11','AUTHENTICATION_SUCCESS'),(129,'admin','2017-12-26 18:04:11','AUTHENTICATION_SUCCESS'),(130,'anonymousUser','2017-12-26 18:04:20','AUTHORIZATION_FAILURE'),(131,'admin','2017-12-26 18:04:26','AUTHENTICATION_SUCCESS'),(132,'admin','2017-12-26 18:04:26','AUTHENTICATION_SUCCESS'),(133,'admin','2017-12-26 18:04:27','AUTHENTICATION_SUCCESS'),(134,'admin','2017-12-26 22:08:45','AUTHENTICATION_SUCCESS'),(135,'admin','2017-12-26 22:08:45','AUTHENTICATION_SUCCESS'),(136,'anonymousUser','2017-12-26 22:08:53','AUTHORIZATION_FAILURE'),(137,'admin','2017-12-26 22:08:59','AUTHENTICATION_SUCCESS'),(138,'admin','2017-12-26 22:08:59','AUTHENTICATION_SUCCESS'),(139,'admin','2017-12-26 22:08:59','AUTHENTICATION_SUCCESS'),(140,'admin','2017-12-27 08:45:05','AUTHENTICATION_SUCCESS'),(141,'anonymousUser','2017-12-27 08:45:16','AUTHORIZATION_FAILURE'),(142,'anonymousUser','2017-12-27 08:45:20','AUTHORIZATION_FAILURE'),(143,'anonymousUser','2017-12-27 08:45:20','AUTHORIZATION_FAILURE'),(144,'admin','2017-12-27 08:45:27','AUTHENTICATION_SUCCESS'),(145,'admin','2017-12-27 08:45:27','AUTHENTICATION_SUCCESS'),(146,'admin','2017-12-27 08:45:28','AUTHENTICATION_SUCCESS'),(147,'admin','2017-12-27 09:43:49','AUTHENTICATION_SUCCESS'),(148,'admin','2017-12-27 09:43:49','AUTHENTICATION_SUCCESS'),(149,'admin','2017-12-28 08:30:23','AUTHENTICATION_SUCCESS'),(150,'admin','2017-12-28 08:30:23','AUTHENTICATION_SUCCESS'),(151,'anonymousUser','2017-12-28 08:30:30','AUTHORIZATION_FAILURE'),(152,'admin','2017-12-28 08:30:35','AUTHENTICATION_SUCCESS'),(153,'admin','2017-12-28 08:30:35','AUTHENTICATION_SUCCESS'),(154,'admin','2017-12-28 08:30:35','AUTHENTICATION_SUCCESS'),(155,'admin','2017-12-28 09:22:01','AUTHENTICATION_SUCCESS'),(156,'admin','2017-12-28 09:22:01','AUTHENTICATION_SUCCESS'),(157,'admin','2017-12-29 07:31:17','AUTHENTICATION_SUCCESS'),(158,'admin','2017-12-29 08:18:41','AUTHENTICATION_SUCCESS'),(159,'admin','2017-12-29 08:42:19','AUTHENTICATION_SUCCESS'),(160,'admin','2017-12-29 12:12:17','AUTHENTICATION_SUCCESS'),(161,'admin','2017-12-29 12:12:17','AUTHENTICATION_SUCCESS'),(162,'anonymousUser','2017-12-29 12:12:24','AUTHORIZATION_FAILURE'),(163,'admin','2017-12-29 12:12:30','AUTHENTICATION_SUCCESS'),(164,'admin','2017-12-29 12:12:30','AUTHENTICATION_SUCCESS'),(165,'admin','2017-12-29 12:12:30','AUTHENTICATION_SUCCESS'),(166,'admin','2017-12-29 12:43:00','AUTHENTICATION_SUCCESS'),(167,'admin','2017-12-29 12:43:00','AUTHENTICATION_SUCCESS'),(168,'admin','2017-12-29 12:43:03','AUTHENTICATION_SUCCESS'),(169,'admin','2017-12-29 12:43:03','AUTHENTICATION_SUCCESS'),(170,'admin','2017-12-29 12:43:03','AUTHENTICATION_SUCCESS'),(171,'admin','2017-12-29 13:44:41','AUTHENTICATION_SUCCESS'),(172,'admin','2017-12-29 13:44:42','AUTHENTICATION_SUCCESS'),(173,'anonymousUser','2017-12-29 13:44:46','AUTHORIZATION_FAILURE'),(174,'admin','2017-12-29 13:44:53','AUTHENTICATION_SUCCESS'),(175,'admin','2017-12-29 13:44:53','AUTHENTICATION_SUCCESS'),(176,'admin','2017-12-29 13:44:53','AUTHENTICATION_SUCCESS'),(177,'admin','2017-12-29 14:13:07','AUTHENTICATION_SUCCESS'),(178,'admin','2017-12-29 14:13:07','AUTHENTICATION_SUCCESS'),(179,'admin','2017-12-29 14:13:10','AUTHENTICATION_SUCCESS'),(180,'admin','2017-12-29 14:13:10','AUTHENTICATION_SUCCESS'),(181,'admin','2017-12-29 14:13:10','AUTHENTICATION_SUCCESS'),(182,'admin','2017-12-29 14:24:21','AUTHENTICATION_SUCCESS'),(183,'anonymousUser','2017-12-29 14:24:25','AUTHORIZATION_FAILURE'),(184,'admin','2017-12-29 14:24:31','AUTHENTICATION_SUCCESS'),(185,'admin','2017-12-29 14:24:31','AUTHENTICATION_SUCCESS'),(186,'admin','2017-12-29 14:24:31','AUTHENTICATION_SUCCESS'),(187,'admin','2018-01-02 10:34:05','AUTHENTICATION_SUCCESS'),(188,'admin','2018-01-02 10:56:13','AUTHENTICATION_SUCCESS'),(189,'admin','2018-01-02 10:56:13','AUTHENTICATION_SUCCESS'),(190,'anonymousUser','2018-01-02 10:57:35','AUTHORIZATION_FAILURE'),(191,'admin','2018-01-02 10:57:40','AUTHENTICATION_SUCCESS'),(192,'admin','2018-01-02 10:57:40','AUTHENTICATION_SUCCESS'),(193,'admin','2018-01-02 10:57:41','AUTHENTICATION_SUCCESS'),(194,'admin','2018-01-02 12:03:57','AUTHENTICATION_SUCCESS'),(195,'anonymousUser','2018-01-02 12:04:02','AUTHORIZATION_FAILURE'),(196,'admin','2018-01-02 12:04:06','AUTHENTICATION_SUCCESS'),(197,'admin','2018-01-02 12:04:06','AUTHENTICATION_SUCCESS'),(198,'admin','2018-01-02 12:04:06','AUTHENTICATION_SUCCESS'),(199,'admin','2018-01-02 12:38:25','AUTHENTICATION_SUCCESS'),(200,'admin','2018-01-02 12:38:25','AUTHENTICATION_SUCCESS'),(201,'anonymousUser','2018-01-02 12:38:29','AUTHORIZATION_FAILURE'),(202,'admin','2018-01-02 12:38:34','AUTHENTICATION_SUCCESS'),(203,'admin','2018-01-02 12:38:34','AUTHENTICATION_SUCCESS'),(204,'admin','2018-01-02 12:38:34','AUTHENTICATION_SUCCESS'),(205,'admin','2018-01-02 12:59:28','AUTHENTICATION_SUCCESS'),(206,'anonymousUser','2018-01-02 12:59:32','AUTHORIZATION_FAILURE'),(207,'admin','2018-01-02 12:59:37','AUTHENTICATION_SUCCESS'),(208,'admin','2018-01-02 12:59:37','AUTHENTICATION_SUCCESS'),(209,'admin','2018-01-02 12:59:37','AUTHENTICATION_SUCCESS'),(210,'admin','2018-01-02 13:17:37','AUTHENTICATION_SUCCESS'),(211,'admin','2018-01-02 13:17:37','AUTHENTICATION_SUCCESS'),(212,'admin','2018-01-02 13:17:41','AUTHENTICATION_SUCCESS'),(213,'admin','2018-01-02 13:17:41','AUTHENTICATION_SUCCESS'),(214,'admin','2018-01-02 13:17:41','AUTHENTICATION_SUCCESS'),(215,'anonymousUser','2018-01-03 23:23:15','AUTHORIZATION_FAILURE'),(216,'anonymousUser','2018-01-03 23:23:15','AUTHORIZATION_FAILURE'),(217,'admin','2018-01-03 23:23:22','AUTHENTICATION_SUCCESS'),(218,'admin','2018-01-03 23:23:22','AUTHENTICATION_SUCCESS'),(219,'admin','2018-01-03 23:23:22','AUTHENTICATION_SUCCESS'),(220,'admin','2018-01-04 11:55:44','AUTHENTICATION_SUCCESS'),(221,'admin','2018-01-04 13:47:09','AUTHENTICATION_SUCCESS'),(222,'admin','2018-01-04 13:47:09','AUTHENTICATION_SUCCESS'),(223,'admin','2018-01-04 15:21:43','AUTHENTICATION_SUCCESS'),(224,'admin','2018-01-04 15:58:55','AUTHENTICATION_SUCCESS'),(225,'admin','2018-01-04 17:44:32','AUTHENTICATION_SUCCESS'),(226,'admin','2018-01-04 20:53:50','AUTHENTICATION_SUCCESS'),(227,'admin','2018-01-05 11:45:50','AUTHENTICATION_SUCCESS'),(228,'admin','2018-01-05 13:13:23','AUTHENTICATION_SUCCESS'),(229,'admin','2018-01-05 15:22:39','AUTHENTICATION_SUCCESS'),(230,'admin','2018-01-06 04:52:02','AUTHENTICATION_SUCCESS'),(231,'admin','2018-01-06 14:51:45','AUTHENTICATION_SUCCESS'),(232,'admin','2018-01-06 14:51:45','AUTHENTICATION_SUCCESS'),(233,'anonymousUser','2018-01-06 15:18:23','AUTHORIZATION_FAILURE'),(234,'anonymousUser','2018-01-06 15:18:23','AUTHORIZATION_FAILURE'),(235,'admin','2018-01-06 15:18:28','AUTHENTICATION_SUCCESS'),(236,'admin','2018-01-06 15:18:29','AUTHENTICATION_SUCCESS'),(237,'admin','2018-01-06 15:18:29','AUTHENTICATION_SUCCESS'),(238,'admin','2018-01-08 09:19:28','AUTHENTICATION_SUCCESS'),(239,'admin','2018-01-08 11:03:47','AUTHENTICATION_SUCCESS'),(240,'admin','2018-01-09 10:39:59','AUTHENTICATION_SUCCESS'),(241,'admin','2018-01-09 12:41:49','AUTHENTICATION_SUCCESS'),(242,'admin','2018-01-09 12:44:58','AUTHENTICATION_SUCCESS');
/*!40000 ALTER TABLE `jhi_persistent_audit_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_persistent_audit_evt_data`
--

DROP TABLE IF EXISTS `jhi_persistent_audit_evt_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_persistent_audit_evt_data` (
  `event_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`,`name`),
  KEY `idx_persistent_audit_evt_data` (`event_id`),
  CONSTRAINT `fk_evt_pers_audit_evt_data` FOREIGN KEY (`event_id`) REFERENCES `jhi_persistent_audit_event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_persistent_audit_evt_data`
--

LOCK TABLES `jhi_persistent_audit_evt_data` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` DISABLE KEYS */;
INSERT INTO `jhi_persistent_audit_evt_data` VALUES (1,'message','Access is denied'),(1,'type','org.springframework.security.access.AccessDeniedException'),(2,'message','Access is denied'),(2,'type','org.springframework.security.access.AccessDeniedException'),(3,'remoteAddress','0:0:0:0:0:0:0:1'),(3,'sessionId','6nw745v29znk1o3hxet6v8of8'),(4,'remoteAddress','0:0:0:0:0:0:0:1'),(4,'sessionId','6nw745v29znk1o3hxet6v8of8'),(5,'remoteAddress','0:0:0:0:0:0:0:1'),(5,'sessionId','6nw745v29znk1o3hxet6v8of8'),(6,'remoteAddress','0:0:0:0:0:0:0:1'),(6,'sessionId','wcaam8qk5ewt55gkqqpdtbuf'),(7,'remoteAddress','0:0:0:0:0:0:0:1'),(7,'sessionId','wcaam8qk5ewt55gkqqpdtbuf'),(8,'message','Access is denied'),(8,'type','org.springframework.security.access.AccessDeniedException'),(9,'remoteAddress','0:0:0:0:0:0:0:1'),(9,'sessionId','4u2cnstkyw2thz4s2xr14v59'),(10,'remoteAddress','0:0:0:0:0:0:0:1'),(10,'sessionId','4u2cnstkyw2thz4s2xr14v59'),(11,'remoteAddress','0:0:0:0:0:0:0:1'),(11,'sessionId','4u2cnstkyw2thz4s2xr14v59'),(12,'remoteAddress','0:0:0:0:0:0:0:1'),(12,'sessionId','t7fw3k9lqldj1uq44dlrrsn6l'),(13,'remoteAddress','0:0:0:0:0:0:0:1'),(13,'sessionId','1dmr8s94xdhq01azbelmnvdhn6'),(14,'message','Access is denied'),(14,'type','org.springframework.security.access.AccessDeniedException'),(15,'message','Access is denied'),(15,'type','org.springframework.security.access.AccessDeniedException'),(16,'remoteAddress','127.0.0.1'),(16,'sessionId','13pz931z5xr4f5y6sl9sfk2k9'),(17,'remoteAddress','127.0.0.1'),(17,'sessionId','13pz931z5xr4f5y6sl9sfk2k9'),(18,'remoteAddress','127.0.0.1'),(18,'sessionId','13pz931z5xr4f5y6sl9sfk2k9'),(19,'remoteAddress','127.0.0.1'),(19,'sessionId','5i40s3ctvuwoovumj0750mgj'),(20,'remoteAddress','127.0.0.1'),(20,'sessionId','5i40s3ctvuwoovumj0750mgj'),(21,'message','Access is denied'),(21,'type','org.springframework.security.access.AccessDeniedException'),(22,'message','Access is denied'),(22,'type','org.springframework.security.access.AccessDeniedException'),(23,'remoteAddress','0:0:0:0:0:0:0:1'),(23,'sessionId','1pma63647k3eodw1y2qa5g2b7'),(24,'remoteAddress','0:0:0:0:0:0:0:1'),(24,'sessionId','1pma63647k3eodw1y2qa5g2b7'),(25,'remoteAddress','0:0:0:0:0:0:0:1'),(25,'sessionId','1pma63647k3eodw1y2qa5g2b7'),(26,'remoteAddress','0:0:0:0:0:0:0:1'),(26,'sessionId','jni6b7zkm4851vqrvc8v4s5zw'),(27,'remoteAddress','0:0:0:0:0:0:0:1'),(27,'sessionId','1ve9lpx2u98qm5eut1xv460i6'),(28,'remoteAddress','0:0:0:0:0:0:0:1'),(28,'sessionId','1ve9lpx2u98qm5eut1xv460i6'),(29,'remoteAddress','0:0:0:0:0:0:0:1'),(29,'sessionId','diexksqkpb4w1l1du6t5u03j2'),(30,'remoteAddress','0:0:0:0:0:0:0:1'),(30,'sessionId','diexksqkpb4w1l1du6t5u03j2'),(31,'remoteAddress','0:0:0:0:0:0:0:1'),(31,'sessionId','diexksqkpb4w1l1du6t5u03j2'),(32,'remoteAddress','0:0:0:0:0:0:0:1'),(32,'sessionId','rkz28s86j37i82d0l0yce9c9'),(33,'remoteAddress','0:0:0:0:0:0:0:1'),(33,'sessionId','dv9a74086x65te8ybvtc9vx0'),(34,'remoteAddress','0:0:0:0:0:0:0:1'),(34,'sessionId','1xnlnsfn1w08e14nhgas2ilqkv'),(35,'message','Access is denied'),(35,'type','org.springframework.security.access.AccessDeniedException'),(36,'message','Access is denied'),(36,'type','org.springframework.security.access.AccessDeniedException'),(37,'message','Access is denied'),(37,'type','org.springframework.security.access.AccessDeniedException'),(38,'remoteAddress','0:0:0:0:0:0:0:1'),(38,'sessionId','1wjoh50x4rl01g2w6x4crjftr'),(39,'remoteAddress','0:0:0:0:0:0:0:1'),(39,'sessionId','1wjoh50x4rl01g2w6x4crjftr'),(40,'remoteAddress','0:0:0:0:0:0:0:1'),(40,'sessionId','1wjoh50x4rl01g2w6x4crjftr'),(41,'remoteAddress','0:0:0:0:0:0:0:1'),(41,'sessionId','12d6ce3l7l8wd2t4w1knong2j'),(42,'remoteAddress','0:0:0:0:0:0:0:1'),(42,'sessionId','1kacxy6mbyusdpev55s45sk3m'),(43,'remoteAddress','0:0:0:0:0:0:0:1'),(43,'sessionId','9j3kq0wrft4y18vgoajkhl525'),(44,'remoteAddress','0:0:0:0:0:0:0:1'),(44,'sessionId','razs6jrrpotpr5kvqnplncxb'),(45,'remoteAddress','0:0:0:0:0:0:0:1'),(45,'sessionId','1jp6tdpjzhz1h1wtoiyquyzjn2'),(46,'remoteAddress','0:0:0:0:0:0:0:1'),(46,'sessionId','1mdvq7d0ywbkm1mvqbnstc1u3i'),(47,'message','Access is denied'),(47,'type','org.springframework.security.access.AccessDeniedException'),(48,'message','Access is denied'),(48,'type','org.springframework.security.access.AccessDeniedException'),(49,'remoteAddress','0:0:0:0:0:0:0:1'),(49,'sessionId','1468ycjil6x8d14rm60tp55273'),(50,'remoteAddress','0:0:0:0:0:0:0:1'),(50,'sessionId','1468ycjil6x8d14rm60tp55273'),(51,'remoteAddress','0:0:0:0:0:0:0:1'),(51,'sessionId','1468ycjil6x8d14rm60tp55273'),(52,'remoteAddress','0:0:0:0:0:0:0:1'),(52,'sessionId','4n5umlm3eqzd1u0gdtljkrlen'),(53,'message','Access is denied'),(53,'type','org.springframework.security.access.AccessDeniedException'),(54,'message','Access is denied'),(54,'type','org.springframework.security.access.AccessDeniedException'),(55,'remoteAddress','0:0:0:0:0:0:0:1'),(55,'sessionId','1m7af95p4gqbcoehqsbhv28vk'),(56,'remoteAddress','0:0:0:0:0:0:0:1'),(56,'sessionId','1m7af95p4gqbcoehqsbhv28vk'),(57,'remoteAddress','0:0:0:0:0:0:0:1'),(57,'sessionId','1m7af95p4gqbcoehqsbhv28vk'),(58,'remoteAddress','0:0:0:0:0:0:0:1'),(58,'sessionId','1qg1sut1480q91hsq1vnx1c24h'),(59,'message','Access is denied'),(59,'type','org.springframework.security.access.AccessDeniedException'),(60,'remoteAddress','0:0:0:0:0:0:0:1'),(60,'sessionId','s2utum414p89169zw5p28lyhu'),(61,'remoteAddress','0:0:0:0:0:0:0:1'),(61,'sessionId','s2utum414p89169zw5p28lyhu'),(62,'remoteAddress','0:0:0:0:0:0:0:1'),(62,'sessionId','s2utum414p89169zw5p28lyhu'),(63,'remoteAddress','0:0:0:0:0:0:0:1'),(63,'sessionId','5rc45irxyirktl0ek986x1r'),(64,'remoteAddress','0:0:0:0:0:0:0:1'),(64,'sessionId','920k0i1b3kost8n79sin658'),(65,'remoteAddress','0:0:0:0:0:0:0:1'),(65,'sessionId','1jl5qxybwvcv4kb00owev05h8'),(66,'remoteAddress','0:0:0:0:0:0:0:1'),(66,'sessionId','1jl5qxybwvcv4kb00owev05h8'),(67,'remoteAddress','0:0:0:0:0:0:0:1'),(67,'sessionId','zdqogkn8aokrtu8d4k0bkl3m'),(68,'message','Access is denied'),(68,'type','org.springframework.security.access.AccessDeniedException'),(69,'remoteAddress','0:0:0:0:0:0:0:1'),(69,'sessionId','1orbd0ncm97fs188yrvewz9xrp'),(70,'remoteAddress','0:0:0:0:0:0:0:1'),(70,'sessionId','1orbd0ncm97fs188yrvewz9xrp'),(71,'remoteAddress','0:0:0:0:0:0:0:1'),(71,'sessionId','1orbd0ncm97fs188yrvewz9xrp'),(72,'remoteAddress','0:0:0:0:0:0:0:1'),(72,'sessionId','etqntmxo6iji1fqssh8f4ck2f'),(73,'message','Access is denied'),(73,'type','org.springframework.security.access.AccessDeniedException'),(74,'remoteAddress','0:0:0:0:0:0:0:1'),(74,'sessionId','xb4bu0gkc1sa11qvvuv4yeurg'),(75,'remoteAddress','0:0:0:0:0:0:0:1'),(75,'sessionId','xb4bu0gkc1sa11qvvuv4yeurg'),(76,'remoteAddress','0:0:0:0:0:0:0:1'),(76,'sessionId','xb4bu0gkc1sa11qvvuv4yeurg'),(77,'remoteAddress','0:0:0:0:0:0:0:1'),(77,'sessionId','15y6qf9621gd31sv8t6yhkhm1v'),(78,'remoteAddress','0:0:0:0:0:0:0:1'),(78,'sessionId','15y6qf9621gd31sv8t6yhkhm1v'),(79,'remoteAddress','0:0:0:0:0:0:0:1'),(79,'sessionId','1i8fss0dy602c1ax706d3olutg'),(80,'remoteAddress','0:0:0:0:0:0:0:1'),(80,'sessionId','1i8fss0dy602c1ax706d3olutg'),(81,'remoteAddress','0:0:0:0:0:0:0:1'),(81,'sessionId','1i8fss0dy602c1ax706d3olutg'),(82,'remoteAddress','0:0:0:0:0:0:0:1'),(82,'sessionId','b0yb72pnwlbriv1rjw6ow2co'),(83,'remoteAddress','0:0:0:0:0:0:0:1'),(83,'sessionId','mo21k0sjcb0816tueamvq65o3'),(84,'remoteAddress','0:0:0:0:0:0:0:1'),(84,'sessionId','1olo3dj3isb3c17tf053dsk3y6'),(85,'remoteAddress','0:0:0:0:0:0:0:1'),(85,'sessionId','wu2gfd0ylesm18vnp1g17lffb'),(86,'remoteAddress','0:0:0:0:0:0:0:1'),(86,'sessionId','177zxrg3epn1w1rms0yypg4wng'),(87,'remoteAddress','0:0:0:0:0:0:0:1'),(87,'sessionId','1oe9cw9joptyt16r5u9a6tlmit'),(88,'remoteAddress','0:0:0:0:0:0:0:1'),(88,'sessionId','1oe9cw9joptyt16r5u9a6tlmit'),(89,'message','Access is denied'),(89,'type','org.springframework.security.access.AccessDeniedException'),(90,'message','Access is denied'),(90,'type','org.springframework.security.access.AccessDeniedException'),(91,'message','Access is denied'),(91,'type','org.springframework.security.access.AccessDeniedException'),(92,'message','Access is denied'),(92,'type','org.springframework.security.access.AccessDeniedException'),(93,'message','Access is denied'),(93,'type','org.springframework.security.access.AccessDeniedException'),(94,'remoteAddress','0:0:0:0:0:0:0:1'),(94,'sessionId','18kew43k3x56yg4ji7zuaoig0'),(95,'remoteAddress','0:0:0:0:0:0:0:1'),(95,'sessionId','18kew43k3x56yg4ji7zuaoig0'),(96,'remoteAddress','0:0:0:0:0:0:0:1'),(96,'sessionId','18kew43k3x56yg4ji7zuaoig0'),(97,'remoteAddress','0:0:0:0:0:0:0:1'),(97,'sessionId','zhw3digtn1jdgefeghql7pmt'),(98,'remoteAddress','0:0:0:0:0:0:0:1'),(98,'sessionId','c4d0yy5x5wdg1ulzhsnvcb8le'),(99,'remoteAddress','0:0:0:0:0:0:0:1'),(99,'sessionId','1my0gyu7lz6fi6k8vbfrom9qs'),(100,'remoteAddress','0:0:0:0:0:0:0:1'),(100,'sessionId','1my0gyu7lz6fi6k8vbfrom9qs'),(101,'remoteAddress','0:0:0:0:0:0:0:1'),(101,'sessionId','1gyly9drdm9tot4fwwqaehgj0'),(102,'remoteAddress','0:0:0:0:0:0:0:1'),(102,'sessionId','1gyly9drdm9tot4fwwqaehgj0'),(103,'remoteAddress','0:0:0:0:0:0:0:1'),(103,'sessionId','1gyly9drdm9tot4fwwqaehgj0'),(104,'remoteAddress','0:0:0:0:0:0:0:1'),(104,'sessionId','10s3y5qmhvdd612p461z3t05f8'),(105,'remoteAddress','0:0:0:0:0:0:0:1'),(105,'sessionId','10s3y5qmhvdd612p461z3t05f8'),(106,'remoteAddress','0:0:0:0:0:0:0:1'),(106,'sessionId','1sl78hrj1jwxe15kevvmjpw2'),(107,'remoteAddress','0:0:0:0:0:0:0:1'),(107,'sessionId','c7acui92heo0boflofewyj97'),(108,'remoteAddress','0:0:0:0:0:0:0:1'),(108,'sessionId','c7acui92heo0boflofewyj97'),(109,'remoteAddress','0:0:0:0:0:0:0:1'),(109,'sessionId','bp1prpf4e53o15kmyd9mwu7ax'),(110,'remoteAddress','0:0:0:0:0:0:0:1'),(110,'sessionId','181wlx3m0a0unmjxhfccl8ysq'),(111,'remoteAddress','0:0:0:0:0:0:0:1'),(111,'sessionId','181wlx3m0a0unmjxhfccl8ysq'),(112,'message','Access is denied'),(112,'type','org.springframework.security.access.AccessDeniedException'),(113,'remoteAddress','0:0:0:0:0:0:0:1'),(113,'sessionId','19c4q3mt8sva746p8gi3tph9r'),(114,'remoteAddress','0:0:0:0:0:0:0:1'),(114,'sessionId','19c4q3mt8sva746p8gi3tph9r'),(115,'remoteAddress','0:0:0:0:0:0:0:1'),(115,'sessionId','19c4q3mt8sva746p8gi3tph9r'),(116,'remoteAddress','0:0:0:0:0:0:0:1'),(116,'sessionId','1d3l7c8wqk2p871bpheuk7m33'),(117,'remoteAddress','0:0:0:0:0:0:0:1'),(117,'sessionId','1d3l7c8wqk2p871bpheuk7m33'),(118,'remoteAddress','0:0:0:0:0:0:0:1'),(118,'sessionId','119sdd7ai9ng554ptbua4aq66'),(119,'remoteAddress','0:0:0:0:0:0:0:1'),(119,'sessionId','119sdd7ai9ng554ptbua4aq66'),(120,'remoteAddress','0:0:0:0:0:0:0:1'),(120,'sessionId','119sdd7ai9ng554ptbua4aq66'),(121,'remoteAddress','0:0:0:0:0:0:0:1'),(121,'sessionId','1crvau69irm6f1kga8roj1xifb'),(122,'message','Access is denied'),(122,'type','org.springframework.security.access.AccessDeniedException'),(123,'remoteAddress','0:0:0:0:0:0:0:1'),(123,'sessionId','14f1ye0qtpn3znywcilfslyzv'),(124,'remoteAddress','0:0:0:0:0:0:0:1'),(124,'sessionId','14f1ye0qtpn3znywcilfslyzv'),(125,'remoteAddress','0:0:0:0:0:0:0:1'),(125,'sessionId','14f1ye0qtpn3znywcilfslyzv'),(126,'remoteAddress','0:0:0:0:0:0:0:1'),(126,'sessionId','19mejfz8vlo91chwm0o87b430'),(127,'remoteAddress','0:0:0:0:0:0:0:1'),(127,'sessionId','19mejfz8vlo91chwm0o87b430'),(128,'remoteAddress','0:0:0:0:0:0:0:1'),(128,'sessionId','vtkah67laa3ashfguh7nzilg'),(129,'remoteAddress','0:0:0:0:0:0:0:1'),(129,'sessionId','vtkah67laa3ashfguh7nzilg'),(130,'message','Access is denied'),(130,'type','org.springframework.security.access.AccessDeniedException'),(131,'remoteAddress','0:0:0:0:0:0:0:1'),(131,'sessionId','jkdpnt0svbin1p75ubo7qzlzg'),(132,'remoteAddress','0:0:0:0:0:0:0:1'),(132,'sessionId','jkdpnt0svbin1p75ubo7qzlzg'),(133,'remoteAddress','0:0:0:0:0:0:0:1'),(133,'sessionId','jkdpnt0svbin1p75ubo7qzlzg'),(134,'remoteAddress','0:0:0:0:0:0:0:1'),(134,'sessionId','phpva6ufpbc9695onzht3nm9'),(135,'remoteAddress','0:0:0:0:0:0:0:1'),(135,'sessionId','phpva6ufpbc9695onzht3nm9'),(136,'message','Access is denied'),(136,'type','org.springframework.security.access.AccessDeniedException'),(137,'remoteAddress','0:0:0:0:0:0:0:1'),(137,'sessionId','1j4vtcnsvm1tol7wmfg8xqdkj'),(138,'remoteAddress','0:0:0:0:0:0:0:1'),(138,'sessionId','1j4vtcnsvm1tol7wmfg8xqdkj'),(139,'remoteAddress','0:0:0:0:0:0:0:1'),(139,'sessionId','1j4vtcnsvm1tol7wmfg8xqdkj'),(140,'remoteAddress','0:0:0:0:0:0:0:1'),(140,'sessionId','1ktrj57nl7n3iwmyqmy2ebf9h'),(141,'message','Access is denied'),(141,'type','org.springframework.security.access.AccessDeniedException'),(142,'message','Access is denied'),(142,'type','org.springframework.security.access.AccessDeniedException'),(143,'message','Access is denied'),(143,'type','org.springframework.security.access.AccessDeniedException'),(144,'remoteAddress','0:0:0:0:0:0:0:1'),(144,'sessionId','2rex9s1gr83d1oy3q41msmi18'),(145,'remoteAddress','0:0:0:0:0:0:0:1'),(145,'sessionId','2rex9s1gr83d1oy3q41msmi18'),(146,'remoteAddress','0:0:0:0:0:0:0:1'),(146,'sessionId','2rex9s1gr83d1oy3q41msmi18'),(147,'remoteAddress','0:0:0:0:0:0:0:1'),(147,'sessionId','9qjk8mbygr21rivt2lhyoey0'),(148,'remoteAddress','0:0:0:0:0:0:0:1'),(148,'sessionId','9qjk8mbygr21rivt2lhyoey0'),(149,'remoteAddress','0:0:0:0:0:0:0:1'),(149,'sessionId','60lg94wm6ikxx4wsb0xixgdu'),(150,'remoteAddress','0:0:0:0:0:0:0:1'),(150,'sessionId','60lg94wm6ikxx4wsb0xixgdu'),(151,'message','Access is denied'),(151,'type','org.springframework.security.access.AccessDeniedException'),(152,'remoteAddress','0:0:0:0:0:0:0:1'),(152,'sessionId','f0sgyy38xu4pvf5bsxgj99eq'),(153,'remoteAddress','0:0:0:0:0:0:0:1'),(153,'sessionId','f0sgyy38xu4pvf5bsxgj99eq'),(154,'remoteAddress','0:0:0:0:0:0:0:1'),(154,'sessionId','f0sgyy38xu4pvf5bsxgj99eq'),(155,'remoteAddress','0:0:0:0:0:0:0:1'),(155,'sessionId','156j0m43c7oyt1lmdqojwtq79j'),(156,'remoteAddress','0:0:0:0:0:0:0:1'),(156,'sessionId','156j0m43c7oyt1lmdqojwtq79j'),(157,'remoteAddress','0:0:0:0:0:0:0:1'),(157,'sessionId','ho3tjs32bwjdkatzdoq0pkr3'),(158,'remoteAddress','0:0:0:0:0:0:0:1'),(158,'sessionId','1b8ycc3cklksf1drcskasl3up9'),(159,'remoteAddress','0:0:0:0:0:0:0:1'),(159,'sessionId','1ji28035htni6aspliwe75vv9'),(160,'remoteAddress','0:0:0:0:0:0:0:1'),(160,'sessionId','116jvr24atofgx06pb2kft8w6'),(161,'remoteAddress','0:0:0:0:0:0:0:1'),(161,'sessionId','116jvr24atofgx06pb2kft8w6'),(162,'message','Access is denied'),(162,'type','org.springframework.security.access.AccessDeniedException'),(163,'remoteAddress','0:0:0:0:0:0:0:1'),(163,'sessionId','26d4q77c6o6x1mpgtf26x5ev7'),(164,'remoteAddress','0:0:0:0:0:0:0:1'),(164,'sessionId','26d4q77c6o6x1mpgtf26x5ev7'),(165,'remoteAddress','0:0:0:0:0:0:0:1'),(165,'sessionId','26d4q77c6o6x1mpgtf26x5ev7'),(166,'remoteAddress','0:0:0:0:0:0:0:1'),(166,'sessionId','1uc7yaqo0zj834rib8dqauj5a'),(167,'remoteAddress','0:0:0:0:0:0:0:1'),(167,'sessionId','1uc7yaqo0zj834rib8dqauj5a'),(168,'remoteAddress','0:0:0:0:0:0:0:1'),(168,'sessionId','qlyet6j5ehjz1lkhbwou0h00v'),(169,'remoteAddress','0:0:0:0:0:0:0:1'),(169,'sessionId','qlyet6j5ehjz1lkhbwou0h00v'),(170,'remoteAddress','0:0:0:0:0:0:0:1'),(170,'sessionId','qlyet6j5ehjz1lkhbwou0h00v'),(171,'remoteAddress','0:0:0:0:0:0:0:1'),(171,'sessionId','xu4p3vxb7r117hkd32qqejab'),(172,'remoteAddress','0:0:0:0:0:0:0:1'),(172,'sessionId','xu4p3vxb7r117hkd32qqejab'),(173,'message','Access is denied'),(173,'type','org.springframework.security.access.AccessDeniedException'),(174,'remoteAddress','0:0:0:0:0:0:0:1'),(174,'sessionId','1wkrm1nx22o1tcn3rchhmvtb'),(175,'remoteAddress','0:0:0:0:0:0:0:1'),(175,'sessionId','1wkrm1nx22o1tcn3rchhmvtb'),(176,'remoteAddress','0:0:0:0:0:0:0:1'),(176,'sessionId','1wkrm1nx22o1tcn3rchhmvtb'),(177,'remoteAddress','0:0:0:0:0:0:0:1'),(177,'sessionId','1fux4fh3x55is1slw76nsxz6cu'),(178,'remoteAddress','0:0:0:0:0:0:0:1'),(178,'sessionId','1fux4fh3x55is1slw76nsxz6cu'),(179,'remoteAddress','0:0:0:0:0:0:0:1'),(179,'sessionId','1s4p731zl91mn1vrvz0vl8ghpf'),(180,'remoteAddress','0:0:0:0:0:0:0:1'),(180,'sessionId','1s4p731zl91mn1vrvz0vl8ghpf'),(181,'remoteAddress','0:0:0:0:0:0:0:1'),(181,'sessionId','1s4p731zl91mn1vrvz0vl8ghpf'),(182,'remoteAddress','0:0:0:0:0:0:0:1'),(182,'sessionId','1ejfcxrdjjgropqu1mztxbqk'),(183,'message','Access is denied'),(183,'type','org.springframework.security.access.AccessDeniedException'),(184,'remoteAddress','0:0:0:0:0:0:0:1'),(184,'sessionId','1i16zh0557fplrsxvqibh1yig'),(185,'remoteAddress','0:0:0:0:0:0:0:1'),(185,'sessionId','1i16zh0557fplrsxvqibh1yig'),(186,'remoteAddress','0:0:0:0:0:0:0:1'),(186,'sessionId','1i16zh0557fplrsxvqibh1yig'),(187,'remoteAddress','0:0:0:0:0:0:0:1'),(187,'sessionId','muytpc8ur25wf46u4ymxtz63'),(188,'remoteAddress','0:0:0:0:0:0:0:1'),(188,'sessionId','272kc0wsrsf01hgcsv38fzohk'),(189,'remoteAddress','0:0:0:0:0:0:0:1'),(189,'sessionId','1149jmxfhjuka18fsdgj0n67fj'),(190,'message','Access is denied'),(190,'type','org.springframework.security.access.AccessDeniedException'),(191,'remoteAddress','0:0:0:0:0:0:0:1'),(191,'sessionId','kjwisnwpf9cz1wi81cs4qkz30'),(192,'remoteAddress','0:0:0:0:0:0:0:1'),(192,'sessionId','kjwisnwpf9cz1wi81cs4qkz30'),(193,'remoteAddress','0:0:0:0:0:0:0:1'),(193,'sessionId','kjwisnwpf9cz1wi81cs4qkz30'),(194,'remoteAddress','0:0:0:0:0:0:0:1'),(194,'sessionId','1ovpwo1dxfvfxn4bnvj6mc1cn'),(195,'message','Access is denied'),(195,'type','org.springframework.security.access.AccessDeniedException'),(196,'remoteAddress','0:0:0:0:0:0:0:1'),(196,'sessionId','1xikmgyz0mqwestcs30vht0ug'),(197,'remoteAddress','0:0:0:0:0:0:0:1'),(197,'sessionId','1xikmgyz0mqwestcs30vht0ug'),(198,'remoteAddress','0:0:0:0:0:0:0:1'),(198,'sessionId','1xikmgyz0mqwestcs30vht0ug'),(199,'remoteAddress','0:0:0:0:0:0:0:1'),(199,'sessionId','1du3tmilo0klf1l6b9b1wpk8i4'),(200,'remoteAddress','0:0:0:0:0:0:0:1'),(200,'sessionId','1du3tmilo0klf1l6b9b1wpk8i4'),(201,'message','Access is denied'),(201,'type','org.springframework.security.access.AccessDeniedException'),(202,'remoteAddress','0:0:0:0:0:0:0:1'),(202,'sessionId','1p0cz1ijx8mfj5isjub77x5dn'),(203,'remoteAddress','0:0:0:0:0:0:0:1'),(203,'sessionId','1p0cz1ijx8mfj5isjub77x5dn'),(204,'remoteAddress','0:0:0:0:0:0:0:1'),(204,'sessionId','1p0cz1ijx8mfj5isjub77x5dn'),(205,'remoteAddress','0:0:0:0:0:0:0:1'),(205,'sessionId','q0tlopjjs6lo1wwwgxiwbji4y'),(206,'message','Access is denied'),(206,'type','org.springframework.security.access.AccessDeniedException'),(207,'remoteAddress','0:0:0:0:0:0:0:1'),(207,'sessionId','1hrulzvuv2a4xzeospn4fp3u3'),(208,'remoteAddress','0:0:0:0:0:0:0:1'),(208,'sessionId','1hrulzvuv2a4xzeospn4fp3u3'),(209,'remoteAddress','0:0:0:0:0:0:0:1'),(209,'sessionId','1hrulzvuv2a4xzeospn4fp3u3'),(210,'remoteAddress','0:0:0:0:0:0:0:1'),(210,'sessionId','iylxt660dioxusz435lbnwb2'),(211,'remoteAddress','0:0:0:0:0:0:0:1'),(211,'sessionId','iylxt660dioxusz435lbnwb2'),(212,'remoteAddress','0:0:0:0:0:0:0:1'),(212,'sessionId','16tzut3x2ndsxb7knxy5tibhf'),(213,'remoteAddress','0:0:0:0:0:0:0:1'),(213,'sessionId','16tzut3x2ndsxb7knxy5tibhf'),(214,'remoteAddress','0:0:0:0:0:0:0:1'),(214,'sessionId','16tzut3x2ndsxb7knxy5tibhf'),(215,'message','Access is denied'),(215,'type','org.springframework.security.access.AccessDeniedException'),(216,'message','Access is denied'),(216,'type','org.springframework.security.access.AccessDeniedException'),(217,'remoteAddress','0:0:0:0:0:0:0:1'),(217,'sessionId','2r8lywhce6gfg91i6vqfutdn'),(218,'remoteAddress','0:0:0:0:0:0:0:1'),(218,'sessionId','2r8lywhce6gfg91i6vqfutdn'),(219,'remoteAddress','0:0:0:0:0:0:0:1'),(219,'sessionId','2r8lywhce6gfg91i6vqfutdn'),(220,'remoteAddress','0:0:0:0:0:0:0:1'),(220,'sessionId','mmf4mhqraek8fslyeydpy0bz'),(221,'remoteAddress','0:0:0:0:0:0:0:1'),(221,'sessionId','1eh574ctqlz0cr4m0up74tvhx'),(222,'remoteAddress','0:0:0:0:0:0:0:1'),(222,'sessionId','1pxpbzimhzisg1r9i3221qj858'),(223,'remoteAddress','0:0:0:0:0:0:0:1'),(223,'sessionId','ed96orkkve5u1r5ely7lxk7ed'),(224,'remoteAddress','0:0:0:0:0:0:0:1'),(224,'sessionId','1iqpv49wcyu9c1nqt7p2luw92k'),(225,'remoteAddress','0:0:0:0:0:0:0:1'),(225,'sessionId','fy5thwpokzv6ynnsvi2rxyja'),(226,'remoteAddress','0:0:0:0:0:0:0:1'),(226,'sessionId','1wa0g9i2i56owjcqnxbglrofe'),(227,'remoteAddress','0:0:0:0:0:0:0:1'),(227,'sessionId','3p24p3zt0re1s01umuzorbla'),(228,'remoteAddress','0:0:0:0:0:0:0:1'),(228,'sessionId','1bjq7gcuegnly1tbjxv10nicfm'),(229,'remoteAddress','0:0:0:0:0:0:0:1'),(229,'sessionId','79iqt0ww90p8s0s16vt88yga'),(230,'remoteAddress','0:0:0:0:0:0:0:1'),(230,'sessionId','1sxiwovxdgt9l1gyvdg6rwteba'),(231,'remoteAddress','0:0:0:0:0:0:0:1'),(231,'sessionId','7eaxios99mfi1qzkpb0ybaovt'),(232,'remoteAddress','0:0:0:0:0:0:0:1'),(232,'sessionId','8n2yykk6c88uz9u5hzhsypyt'),(233,'message','Access is denied'),(233,'type','org.springframework.security.access.AccessDeniedException'),(234,'message','Access is denied'),(234,'type','org.springframework.security.access.AccessDeniedException'),(235,'remoteAddress','0:0:0:0:0:0:0:1'),(235,'sessionId','11z62e13p1hw81kvg3paagmaog'),(236,'remoteAddress','0:0:0:0:0:0:0:1'),(236,'sessionId','11z62e13p1hw81kvg3paagmaog'),(237,'remoteAddress','0:0:0:0:0:0:0:1'),(237,'sessionId','11z62e13p1hw81kvg3paagmaog'),(238,'remoteAddress','0:0:0:0:0:0:0:1'),(238,'sessionId','1qzpykkry0n6k1p3opj281b0t3'),(239,'remoteAddress','0:0:0:0:0:0:0:1'),(239,'sessionId','b74ozc9fk2ba6dc6t1xn737w'),(240,'remoteAddress','0:0:0:0:0:0:0:1'),(240,'sessionId','1nsscoiw70z3a9x4jq5hzkinw'),(241,'remoteAddress','0:0:0:0:0:0:0:1'),(241,'sessionId','149ofcw03l2un1d6sqx46u4fs1'),(242,'remoteAddress','0:0:0:0:0:0:0:1'),(242,'sessionId','1emyxwl9fl6tarf5w85vx5yeq');
/*!40000 ALTER TABLE `jhi_persistent_audit_evt_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_persistent_token`
--

DROP TABLE IF EXISTS `jhi_persistent_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_persistent_token` (
  `series` varchar(255) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `token_value` varchar(255) NOT NULL,
  `token_date` date DEFAULT NULL,
  `ip_address` varchar(39) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`series`),
  KEY `fk_user_persistent_token` (`user_id`),
  CONSTRAINT `fk_user_persistent_token` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_persistent_token`
--

LOCK TABLES `jhi_persistent_token` WRITE;
/*!40000 ALTER TABLE `jhi_persistent_token` DISABLE KEYS */;
INSERT INTO `jhi_persistent_token` VALUES ('0Dquf7iI2KDmct+5p9jObg==',3,'gO19C6bGcX+hT1U3JwIKtA==','2017-12-12','0:0:0:0:0:0:0:1','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36'),('8UmPVG1zNkucvjl9R2WA3w==',3,'IGdJw5KOeJzFoDP+CEwgNQ==','2018-01-02','0:0:0:0:0:0:0:1','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36'),('Ber0CPvlzbG2Rd+YdB80kQ==',3,'6NJDvFECuBYmdXRG67eUeA==','2017-12-29','0:0:0:0:0:0:0:1','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36'),('dPwAz0jM6ESWWwqn7zwodg==',3,'3jL74RTuLc4t6I+wVsDI5w==','2017-12-14','0:0:0:0:0:0:0:1','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36'),('etwxsjn6rEZiUt6eWCmA9g==',3,'VgAhMMDWaG7ljEZ7yhwBhA==','2017-12-26','0:0:0:0:0:0:0:1','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36'),('H8zALfoF5PGVB6de79QcnQ==',3,'LIjzwjodcRqtB5oZYbb5lw==','2017-12-20','0:0:0:0:0:0:0:1','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36'),('L2mDbXLvUwzGaZkghNsKfA==',3,'hjmTBJpUqHLSCQ2O0hRB3A==','2017-12-29','0:0:0:0:0:0:0:1','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36'),('L5/PeJmFAuoGoNKrXqk0hw==',3,'5W053Z69SqjhLjGzb2WGvA==','2018-01-09','0:0:0:0:0:0:0:1','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36'),('vU9Oxlp/BYPOMRzmtsE10Q==',3,'WTCR0UrGvoFZYNpmg2cbSQ==','2018-01-02','0:0:0:0:0:0:0:1','Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36');
/*!40000 ALTER TABLE `jhi_persistent_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_user`
--

DROP TABLE IF EXISTS `jhi_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `PASSWORD` varchar(60) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(5) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NOT NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`),
  UNIQUE KEY `idx_user_login` (`login`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `idx_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user`
--

LOCK TABLES `jhi_user` WRITE;
/*!40000 ALTER TABLE `jhi_user` DISABLE KEYS */;
INSERT INTO `jhi_user` VALUES (1,'system','$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG','System','System','system@localhost','','en',NULL,NULL,'system','2017-07-13 10:18:02',NULL,NULL,NULL),(2,'anonymousUser','$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO','Anonymous','User','anonymous@localhost','','en',NULL,NULL,'system','2017-07-13 10:18:02',NULL,NULL,NULL),(3,'admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost','','en',NULL,NULL,'system','2017-07-13 10:18:02',NULL,NULL,NULL),(4,'user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','User','User','user@localhost','','en',NULL,NULL,'system','2017-07-13 10:18:02',NULL,NULL,NULL);
/*!40000 ALTER TABLE `jhi_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_user_authority`
--

DROP TABLE IF EXISTS `jhi_user_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`),
  CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user_authority`
--

LOCK TABLES `jhi_user_authority` WRITE;
/*!40000 ALTER TABLE `jhi_user_authority` DISABLE KEYS */;
INSERT INTO `jhi_user_authority` VALUES (1,'ROLE_ADMIN'),(1,'ROLE_USER'),(3,'ROLE_ADMIN'),(3,'ROLE_USER'),(4,'ROLE_USER');
/*!40000 ALTER TABLE `jhi_user_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_analysis`
--

DROP TABLE IF EXISTS `t_analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_analysis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `description` text,
  `error_description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_analysis`
--

LOCK TABLES `t_analysis` WRITE;
/*!40000 ALTER TABLE `t_analysis` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_analysis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_datasource`
--

DROP TABLE IF EXISTS `t_datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_datasource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `endpoint_url` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `is_public` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `endpoint_update_url` varchar(255) DEFAULT NULL,
  `meta_graph_name` varchar(255) DEFAULT NULL,
  `meta_endpoint_url` varchar(255) DEFAULT NULL,
  `iri` varchar(255) DEFAULT NULL,
  `META_ENDPOINT_UPDATE_URL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_nacyl39t0jqfg2i80af976s6b` (`owner_id`),
  CONSTRAINT `fk_nacyl39t0jqfg2i80af976s6b` FOREIGN KEY (`owner_id`) REFERENCES `jhi_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_datasource`
--

LOCK TABLES `t_datasource` WRITE;
/*!40000 ALTER TABLE `t_datasource` DISABLE KEYS */;
INSERT INTO `t_datasource` VALUES (1,'http://fuseki:3030/boinq/sparql',0,'','Default Local Endpoint',3,'http://fuseki:3030/boinq/update','http://www.boinq.org/iri/graph/meta/','http://fuseki:3030/boinq/sparql','s','http://fuseki:3030/boinq/update'),(2,'https://www.ebi.ac.uk/rdf/services/sparql',1,'','Ensembl SPARQL Endpoint',3,NULL,'http://www.boinq.org/iri/graph/meta/','http://fuseki:3030/boinq/sparql','https://www.ebi.ac.uk/rdf/services/sparql','http://fuseki:3030/boinq/update'),(3,'http://sparql.uniprot.org',3,'','Uniprot SPARQL Endpoint',3,NULL,'http://www.boinq.org/iri/graph/meta/','http://bigdata:9999/bigdata/namespace/boinq/sparql','http://sparql.uniprot.org','http://bigdata:9999/bigdata/namespace/boinq/sparql'),(4,'http://rdf.disgenet.org/sparql/',3,'','DisGeNet SPARQL Endpoint',3,NULL,'http://www.boinq.org/iri/graph/meta/','http://bigdata:9999/bigdata/namespace/boinq/sparql','http://rdf.disgenet.org/sparql/','http://bigdata:9999/bigdata/namespace/boinq/sparql'),(5,'https://id.nlm.nih.gov/mesh/sparql',3,'','Medical Subject Headers',3,NULL,NULL,NULL,'https://id.nlm.nih.gov/mesh/',NULL);
/*!40000 ALTER TABLE `t_datasource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_datasource_t_track`
--

DROP TABLE IF EXISTS `t_datasource_t_track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_datasource_t_track` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `T_DATASOURCE_id` bigint(20) DEFAULT NULL,
  `tracks_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UC_T_DATASOURCE_T_TRACK_TRACKS_ID` (`tracks_id`),
  KEY `fk_qahly5cqvghitfeqekbgeqk1t` (`T_DATASOURCE_id`),
  CONSTRAINT `fk_cyex7yx8vb80gi2vq2eswyu4q` FOREIGN KEY (`tracks_id`) REFERENCES `t_track` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_qahly5cqvghitfeqekbgeqk1t` FOREIGN KEY (`T_DATASOURCE_id`) REFERENCES `t_datasource` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_datasource_t_track`
--

LOCK TABLES `t_datasource_t_track` WRITE;
/*!40000 ALTER TABLE `t_datasource_t_track` DISABLE KEYS */;
INSERT INTO `t_datasource_t_track` VALUES (101,3,3),(102,4,4),(120,2,116),(148,4,144),(151,5,147),(152,1,148);
/*!40000 ALTER TABLE `t_datasource_t_track` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_edgetemplate`
--

DROP TABLE IF EXISTS `t_edgetemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_edgetemplate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_nodetemplate_id` bigint(20) DEFAULT NULL,
  `to_nodetemplate_id` bigint(20) DEFAULT NULL,
  `term` varchar(255) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `graphtemplate_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_edge_fromNode` (`from_nodetemplate_id`),
  KEY `fk_edge_tomNode` (`to_nodetemplate_id`),
  KEY `fk_edge_graphtemplate` (`graphtemplate_id`),
  CONSTRAINT `fk_edge_fromNode` FOREIGN KEY (`from_nodetemplate_id`) REFERENCES `t_nodetemplate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_edge_graphtemplate` FOREIGN KEY (`graphtemplate_id`) REFERENCES `t_graphtemplate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_edge_tomNode` FOREIGN KEY (`to_nodetemplate_id`) REFERENCES `t_nodetemplate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=480 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_edgetemplate`
--

LOCK TABLES `t_edgetemplate` WRITE;
/*!40000 ALTER TABLE `t_edgetemplate` DISABLE KEYS */;
INSERT INTO `t_edgetemplate` VALUES (1,1,2,'http://semanticscience.org/resource/SIO_000628','refers to',1),(2,3,4,'http://www.w3.org/119/02/22-rdf-syntax-ns#type','a',1),(4,2,3,'http://semanticscience.org/resource/SIO_000062','is participant in',1),(5,3,6,'http://www.w3.org/2004/02/skos/core#exactMatch','exactMatch',1),(6,7,8,'http://www.w3.org/2004/02/skos/core#exactMatch','exactMatch',1),(8,1,7,'http://semanticscience.org/resource/SIO_000628','refers to',1),(99,99,100,'http://purl.org/dc/terms/description',NULL,100),(100,101,102,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,100),(101,99,103,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,100),(102,104,105,'http://purl.org/dc/terms/description',NULL,100),(103,99,104,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,100),(104,101,106,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,100),(105,99,107,'http://biohackathon.org/resource/faldo#location',NULL,100),(106,106,108,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,100),(107,104,109,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,100),(108,109,110,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,100),(109,104,111,'http://biohackathon.org/resource/faldo#location',NULL,100),(110,101,112,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,100),(111,112,113,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,100),(112,103,114,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,100),(113,104,115,'http://purl.org/dc/terms/identifier',NULL,100),(114,102,116,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,100),(115,99,117,'http://purl.org/dc/terms/identifier',NULL,100),(116,101,99,'http://www.boinq.org/iri/ontologies/format#defines',NULL,100),(117,104,118,'http://www.w3.org/2000/01/rdf-schema#label',NULL,100),(118,99,119,'http://www.w3.org/2000/01/rdf-schema#label',NULL,100),(119,120,121,'aa','sdfa',101),(120,122,123,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,103),(121,124,125,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,103),(122,122,126,'http://www.boinq.org/iri/ontologies/format#defines',NULL,103),(123,126,127,'http://www.w3.org/2000/01/rdf-schema#label',NULL,103),(124,123,128,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,103),(125,126,129,'http://purl.org/dc/terms/description',NULL,103),(126,126,130,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,103),(127,126,131,'http://purl.org/dc/terms/identifier',NULL,103),(128,126,132,'http://biohackathon.org/resource/faldo#location',NULL,103),(129,122,124,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,103),(130,130,133,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,103),(131,134,135,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,103),(132,122,134,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,103),(133,126,136,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,103),(134,137,138,'http://purl.org/dc/terms/identifier',NULL,106),(135,139,140,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,106),(136,141,142,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,106),(137,141,143,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,106),(138,141,137,'http://www.boinq.org/iri/ontologies/format#defines',NULL,106),(139,143,144,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,106),(140,137,145,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,106),(141,137,146,'http://www.w3.org/2000/01/rdf-schema#label',NULL,106),(142,137,147,'http://biohackathon.org/resource/faldo#location',NULL,106),(143,137,148,'http://purl.org/dc/terms/description',NULL,106),(144,141,149,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,106),(145,137,139,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,106),(146,149,150,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,106),(147,142,151,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,106),(148,152,153,'http://biohackathon.org/resource/faldo#location',NULL,108),(149,152,154,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,108),(150,152,155,'http://www.w3.org/2000/01/rdf-schema#label',NULL,108),(151,152,156,'http://purl.org/dc/terms/identifier',NULL,108),(152,157,158,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,108),(153,159,160,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,108),(154,152,161,'http://purl.org/dc/terms/description',NULL,108),(155,157,162,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,108),(156,163,164,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,108),(157,162,165,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,108),(158,152,159,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,108),(159,157,163,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,108),(160,158,166,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,108),(161,157,152,'http://www.boinq.org/iri/ontologies/format#defines',NULL,108),(162,167,168,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,110),(163,167,169,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,110),(164,167,170,'http://biohackathon.org/resource/faldo#location',NULL,110),(165,167,171,'http://www.w3.org/2000/01/rdf-schema#label',NULL,110),(166,172,173,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,110),(167,167,174,'http://purl.org/dc/terms/description',NULL,110),(168,167,175,'http://purl.org/dc/terms/identifier',NULL,110),(169,172,176,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,110),(170,173,177,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,110),(171,172,178,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,110),(172,169,179,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,110),(173,178,180,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,110),(174,172,167,'http://www.boinq.org/iri/ontologies/format#defines',NULL,110),(175,176,181,'http://www.w3.org/1999/02/22-rdf-syntax-ns#value',NULL,110),(176,182,183,'http://biohackathon.org/resource/faldo#location',NULL,112),(177,182,184,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,112),(178,182,185,'http://www.w3.org/2000/01/rdf-schema#label',NULL,112),(179,182,186,'http://purl.org/dc/terms/identifier',NULL,112),(180,182,187,'http://purl.org/dc/terms/description',NULL,112),(181,188,189,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,112),(182,182,190,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,112),(183,188,191,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,112),(184,188,182,'http://www.boinq.org/iri/ontologies/format#defines',NULL,112),(185,188,192,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,112),(186,193,194,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,114),(187,195,196,'http://www.w3.org/1999/02/22-rdf-syntax-ns#type',NULL,114),(188,195,197,'http://www.w3.org/2000/01/rdf-schema#label',NULL,114),(189,193,198,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,114),(190,195,199,'http://biohackathon.org/resource/faldo#location',NULL,114),(191,195,200,'http://purl.org/dc/terms/description',NULL,114),(192,193,201,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,114),(193,195,202,'http://purl.org/dc/terms/identifier',NULL,114),(194,193,195,'http://www.boinq.org/iri/ontologies/format#defines',NULL,114),(195,195,203,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,114),(196,195,204,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,114),(197,205,206,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,116),(198,207,208,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,116),(199,205,209,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,116),(200,205,210,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,116),(201,207,211,'http://www.w3.org/1999/02/22-rdf-syntax-ns#type',NULL,116),(202,207,212,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,116),(203,205,207,'http://www.boinq.org/iri/ontologies/format#defines',NULL,116),(204,207,213,'http://biohackathon.org/resource/faldo#location',NULL,116),(205,207,214,'http://www.w3.org/2000/01/rdf-schema#label',NULL,116),(206,207,215,'http://purl.org/dc/terms/description',NULL,116),(207,207,216,'http://purl.org/dc/terms/identifier',NULL,116),(230,251,252,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,121),(231,253,254,'http://purl.org/dc/terms/description',NULL,121),(232,255,256,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,121),(233,257,258,'http://purl.org/dc/terms/identifier',NULL,121),(234,259,260,'http://www.w3.org/2000/01/rdf-schema#label',NULL,121),(235,261,262,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,121),(236,263,264,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,121),(237,265,266,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,121),(238,267,268,'http://www.boinq.org/iri/ontologies/format#defines',NULL,121),(239,269,270,'http://www.w3.org/1999/02/22-rdf-syntax-ns#type',NULL,121),(240,271,272,'http://biohackathon.org/resource/faldo#location',NULL,121),(241,273,274,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,123),(242,275,276,'http://www.w3.org/1999/02/22-rdf-syntax-ns#type',NULL,123),(243,275,277,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,123),(244,273,278,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,123),(245,275,279,'http://purl.org/dc/terms/identifier',NULL,123),(246,275,280,'http://purl.org/dc/terms/description',NULL,123),(248,275,282,'http://biohackathon.org/resource/faldo#location',NULL,123),(249,275,283,'http://www.w3.org/2000/01/rdf-schema#label',NULL,123),(250,275,284,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,123),(251,273,275,'http://www.boinq.org/iri/ontologies/format#defines',NULL,123),(252,273,281,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,123),(253,285,286,'http://purl.org/dc/terms/identifier',NULL,125),(254,285,287,'http://www.w3.org/1999/02/22-rdf-syntax-ns#type',NULL,125),(255,288,289,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,125),(256,285,290,'http://purl.org/dc/terms/description',NULL,125),(257,288,291,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,125),(258,285,292,'http://purl.obolibrary.org/obo/so-xp.obo#has_part',NULL,125),(259,285,293,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,125),(260,285,294,'http://www.w3.org/2000/01/rdf-schema#label',NULL,125),(261,288,295,'http://purl.obolibrary.org/obo/so-xp.obo#has_quality',NULL,125),(262,285,296,'http://biohackathon.org/resource/faldo#location',NULL,125),(263,288,285,'http://www.boinq.org/iri/ontologies/format#defines',NULL,125),(264,297,298,'http://purl.obolibrary.org/obo/SO_transcribed_from','Transcribed from',126),(265,299,300,'<http://biohackathon.org/resource/faldo#location>','location',126),(266,301,302,'<http://biohackathon.org/resource/faldo#location>','location',126),(267,303,304,'http://to','',127),(268,303,305,'http://to','',127),(269,304,306,'http://to','',127),(270,307,308,'http://purl.obolibrary.org/obo/SO_transcribed_from','transcribed from',128),(271,308,309,'http://biohackathon.org/resource/faldo#location','location',128),(272,307,310,'http://biohackathon.org/resource/faldo#location','location',128),(273,307,311,'http://purl.obolibrary.org/obo/SO_has_part','has part',128),(274,307,312,'http://semanticscience.org/resource/SIO_000974','has ordered part',128),(275,312,311,'http://semanticscience.org/resource/SIO_000628','refers to',128),(276,312,313,'http://semanticscience.org/resource/SIO_000300','has value',128),(277,311,314,'http://biohackathon.org/resource/faldo#location','location',128),(278,308,315,'http://www.w3.org/2004/02/skos/core#altLabel','alt label',128),(279,307,316,'http://purl.obolibrary.org/obo/SO_translates_to','translates to',128),(280,308,317,'http://purl.org/dc/elements/1.1/identifier','identifier',128),(281,307,318,'http://purl.org/dc/elements/1.1/identifier','identifier',128),(282,311,319,'http://purl.org/dc/elements/1.1/identifier','identifier',128),(283,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,130),(284,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,130),(285,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,130),(286,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,130),(287,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,131),(288,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,131),(289,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,131),(290,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,131),(291,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,132),(292,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,132),(293,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,132),(294,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,132),(295,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,133),(296,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,133),(297,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,133),(298,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,133),(299,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,134),(300,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,134),(301,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,134),(302,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,134),(303,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,135),(304,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,135),(305,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,135),(306,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,135),(307,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,136),(308,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,136),(309,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,136),(310,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,136),(311,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,137),(312,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,137),(313,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,137),(314,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,137),(315,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,137),(316,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,137),(317,311,314,'http://biohackathon.org/resource/faldo#location',NULL,137),(318,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,138),(319,311,314,'http://biohackathon.org/resource/faldo#location',NULL,138),(320,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,138),(321,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,138),(322,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,138),(323,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,138),(324,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,138),(325,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,139),(326,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,139),(327,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,139),(328,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,139),(329,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,139),(330,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,139),(331,311,314,'http://biohackathon.org/resource/faldo#location',NULL,139),(332,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,140),(333,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,140),(334,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,140),(335,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,140),(336,311,314,'http://biohackathon.org/resource/faldo#location',NULL,140),(337,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,140),(338,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,140),(339,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,141),(340,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,141),(341,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,141),(342,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,141),(343,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,141),(344,311,314,'http://biohackathon.org/resource/faldo#location',NULL,141),(345,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,141),(346,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,142),(347,311,314,'http://biohackathon.org/resource/faldo#location',NULL,142),(348,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,142),(349,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,142),(350,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,142),(351,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,142),(352,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,142),(353,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,143),(354,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,143),(355,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,143),(356,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,143),(357,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,143),(358,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,143),(359,311,314,'http://biohackathon.org/resource/faldo#location',NULL,143),(360,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,144),(361,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,144),(362,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,144),(363,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,144),(364,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,144),(365,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,144),(366,311,314,'http://biohackathon.org/resource/faldo#location',NULL,144),(367,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,145),(368,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,145),(369,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,145),(370,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,145),(371,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,145),(372,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,145),(373,311,314,'http://biohackathon.org/resource/faldo#location',NULL,145),(374,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,146),(375,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,146),(376,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,146),(377,311,314,'http://biohackathon.org/resource/faldo#location',NULL,146),(378,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,146),(379,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,146),(380,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,146),(381,311,314,'http://biohackathon.org/resource/faldo#location',NULL,147),(382,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,147),(383,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,147),(384,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,147),(385,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,147),(386,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,147),(387,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,148),(388,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,148),(389,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,148),(390,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,148),(391,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,148),(392,311,314,'http://biohackathon.org/resource/faldo#location',NULL,148),(393,311,314,'http://biohackathon.org/resource/faldo#location',NULL,149),(394,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,149),(395,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,149),(396,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,149),(397,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,149),(398,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,149),(399,311,314,'http://biohackathon.org/resource/faldo#location',NULL,150),(400,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,150),(401,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,150),(402,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,150),(403,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,150),(404,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,150),(405,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,151),(406,311,314,'http://biohackathon.org/resource/faldo#location',NULL,151),(407,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,151),(408,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,151),(409,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,151),(410,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,151),(411,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,152),(412,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,152),(413,311,314,'http://biohackathon.org/resource/faldo#location',NULL,152),(414,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,152),(415,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,152),(416,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,152),(417,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,153),(418,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,153),(419,311,314,'http://biohackathon.org/resource/faldo#location',NULL,153),(420,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,153),(421,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,153),(422,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,153),(423,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,153),(424,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,154),(425,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,154),(426,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,154),(427,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,154),(428,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,154),(429,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,154),(430,311,314,'http://biohackathon.org/resource/faldo#location',NULL,154),(431,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,155),(432,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,155),(433,311,314,'http://biohackathon.org/resource/faldo#location',NULL,155),(434,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,155),(435,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,155),(436,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,155),(437,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,155),(438,312,313,'http://semanticscience.org/resource/SIO_000300',NULL,156),(439,311,314,'http://biohackathon.org/resource/faldo#location',NULL,156),(440,307,312,'http://semanticscience.org/resource/SIO_000974',NULL,156),(441,311,319,'http://purl.org/dc/elements/1.1/identifier',NULL,156),(442,307,318,'http://purl.org/dc/elements/1.1/identifier',NULL,156),(443,307,311,'http://purl.obolibrary.org/obo/SO_has_part',NULL,156),(444,312,311,'http://semanticscience.org/resource/SIO_000628',NULL,156),(445,1,320,'http://semanticscience.org/resource/SIO_000628','refers to',1),(446,320,320,'','',1),(447,320,321,'','',1),(450,324,325,'http://semanticscience.org/resource/SIO_000628','refers to',157),(451,324,326,'http://semanticscience.org/resource/SIO_000628','refers to',157),(452,326,327,'http://www.w3.org/2004/02/skos/core#exactMatch','exactMatch',157),(453,325,328,'http://www.w3.org/2004/02/skos/core#exactMatch','exactMatch',157),(454,324,333,'http://semanticscience.org/resource/SIO_000628','refers to',157),(455,326,330,'http://semanticscience.org/resource/SIO_000332','is about',157),(456,333,332,'http://www.w3.org/2004/02/skos/core#exactMatch','exactMatch',157),(457,333,334,'http://www.w3.org/2000/01/rdf-schema#subClassOf','subClassOf',157),(458,324,335,'http://semanticscience.org/resource/SIO_000216','has measurement value',157),(459,316,336,'http://purl.org/dc/elements/1.1/identifier','identifier',128),(460,308,337,'http://www.w3.org/2000/01/rdf-schema#seeAlso','seeAlso',128),(461,338,339,'http://www.w3.org/2000/01/rdf-schema#label','label',158),(462,340,341,'http://www.w3.org/2000/01/rdf-schema#label','label',158),(463,342,343,'http://id.nlm.nih.gov/mesh/vocab#preferredConcept','preferredConcept',158),(464,344,345,'http://id.nlm.nih.gov/mesh/vocab#treeNumber','treeNumber',158),(465,347,346,'','',160),(466,347,348,'','',160),(467,349,350,'atob','',161),(468,349,351,'atoc','',161),(469,353,352,'http://id.nlm.nih.gov/mesh/vocab#preferredConcept','preferredConcept',163),(470,355,356,'atoc','',164),(471,355,354,'atob','',164),(472,358,357,'atob','',165),(473,360,359,'http://id.nlm.nih.gov/mesh/vocab#term','term',162),(474,361,360,'http://id.nlm.nih.gov/mesh/vocab#preferredConcept','preferredConcept',162),(475,361,362,'http://id.nlm.nih.gov/mesh/vocab#treeNumber','treeNumber',162),(476,360,364,'http://www.w3.org/2000/01/rdf-schema#label','label',162),(477,359,363,'http://www.w3.org/2000/01/rdf-schema#label','label',162),(478,361,365,'http://www.w3.org/2000/01/rdf-schema#label','label',162),(479,362,366,'http://id.nlm.nih.gov/mesh/vocab#parentTreeNumber','parentTreeNumber',162);
/*!40000 ALTER TABLE `t_edgetemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_featureconnector`
--

DROP TABLE IF EXISTS `t_featureconnector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_featureconnector` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `path_expression` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_featureconnector`
--

LOCK TABLES `t_featureconnector` WRITE;
/*!40000 ALTER TABLE `t_featureconnector` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_featureconnector` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_featurejoin`
--

DROP TABLE IF EXISTS `t_featurejoin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_featurejoin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_select_id` bigint(20) DEFAULT NULL,
  `target_select_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `feature_query_id` bigint(20) DEFAULT NULL,
  `same_strand` bit(1) DEFAULT NULL,
  `source_connector_id` bigint(20) DEFAULT NULL,
  `target_connector_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_joinToQuery` (`feature_query_id`),
  KEY `fk_joinToSourceSelect` (`source_select_id`),
  KEY `fk_joinToTargetSelect` (`target_select_id`),
  KEY `fk_featureJoinToSourceConnector` (`source_connector_id`),
  KEY `fk_featureJoinToTargetConnector` (`target_connector_id`),
  CONSTRAINT `fk_featureJoinToSourceConnector` FOREIGN KEY (`source_connector_id`) REFERENCES `t_featureconnector` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_featureJoinToTargetConnector` FOREIGN KEY (`target_connector_id`) REFERENCES `t_featureconnector` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_joinToQuery` FOREIGN KEY (`feature_query_id`) REFERENCES `t_featurequery` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_joinToSourceSelect` FOREIGN KEY (`source_select_id`) REFERENCES `t_featureselect` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_joinToTargetSelect` FOREIGN KEY (`target_select_id`) REFERENCES `t_featureselect` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_featurejoin`
--

LOCK TABLES `t_featurejoin` WRITE;
/*!40000 ALTER TABLE `t_featurejoin` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_featurejoin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_featurequery`
--

DROP TABLE IF EXISTS `t_featurequery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_featurequery` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `target_graph` varchar(255) DEFAULT NULL,
  `reference_assembly_uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_queryToUser` (`owner_id`),
  CONSTRAINT `fk_queryToUser` FOREIGN KEY (`owner_id`) REFERENCES `jhi_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_featurequery`
--

LOCK TABLES `t_featurequery` WRITE;
/*!40000 ALTER TABLE `t_featurequery` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_featurequery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_featureselect`
--

DROP TABLE IF EXISTS `t_featureselect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_featureselect` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `track_id` bigint(20) DEFAULT NULL,
  `retrieve_feature_data` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `feature_query_id` bigint(20) DEFAULT NULL,
  `view_x` int(11) DEFAULT NULL,
  `view_y` int(11) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_selectToQuery` (`feature_query_id`),
  KEY `fk_selectToTrack` (`track_id`),
  CONSTRAINT `fk_selectToQuery` FOREIGN KEY (`feature_query_id`) REFERENCES `t_featurequery` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_selectToTrack` FOREIGN KEY (`track_id`) REFERENCES `t_track` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_featureselect`
--

LOCK TABLES `t_featureselect` WRITE;
/*!40000 ALTER TABLE `t_featureselect` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_featureselect` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_featureselectcriterion`
--

DROP TABLE IF EXISTS `t_featureselectcriterion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_featureselectcriterion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `start` bigint(20) DEFAULT NULL,
  `end` bigint(20) DEFAULT NULL,
  `contig` varchar(255) DEFAULT NULL,
  `strand` bit(1) DEFAULT NULL,
  `feature_select_id` bigint(20) DEFAULT NULL,
  `feature_type_uri` varchar(255) DEFAULT NULL,
  `feature_type_label` varchar(255) DEFAULT NULL,
  `match_name` varchar(255) DEFAULT NULL,
  `path_expression` varchar(255) DEFAULT NULL,
  `exact_match` bit(1) DEFAULT NULL,
  `min_double` double DEFAULT NULL,
  `max_double` double DEFAULT NULL,
  `match_double` double DEFAULT NULL,
  `min_long` bigint(20) DEFAULT NULL,
  `max_long` bigint(20) DEFAULT NULL,
  `match_long` bigint(20) DEFAULT NULL,
  `match_string` varchar(255) DEFAULT NULL,
  `term_uri` varchar(255) DEFAULT NULL,
  `term_label` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_criterionToSelect` (`feature_select_id`),
  CONSTRAINT `fk_criterionToSelect` FOREIGN KEY (`feature_select_id`) REFERENCES `t_featureselect` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_featureselectcriterion`
--

LOCK TABLES `t_featureselectcriterion` WRITE;
/*!40000 ALTER TABLE `t_featureselectcriterion` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_featureselectcriterion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_graphtemplate`
--

DROP TABLE IF EXISTS `t_graphtemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_graphtemplate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `endpoint_url` varchar(255) DEFAULT NULL,
  `graph_iri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=167 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_graphtemplate`
--

LOCK TABLES `t_graphtemplate` WRITE;
/*!40000 ALTER TABLE `t_graphtemplate` DISABLE KEYS */;
INSERT INTO `t_graphtemplate` VALUES (1,1,'DisGeNet','http:/rdf.disgenet.org/sparql/','http://rdf.disgenet.org'),(99,0,NULL,NULL,NULL),(100,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_99'),(101,0,NULL,NULL,NULL),(102,0,NULL,NULL,NULL),(103,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_100'),(104,0,NULL,NULL,NULL),(105,0,NULL,NULL,NULL),(106,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_102'),(107,0,NULL,NULL,NULL),(108,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_103'),(109,0,NULL,NULL,NULL),(110,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_104'),(111,0,NULL,NULL,NULL),(112,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_105'),(113,0,NULL,NULL,NULL),(114,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_106'),(115,0,NULL,NULL,NULL),(116,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_107'),(117,0,NULL,NULL,NULL),(118,0,NULL,NULL,NULL),(119,0,NULL,NULL,NULL),(120,0,NULL,NULL,NULL),(121,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_111'),(122,0,NULL,NULL,NULL),(123,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_112'),(124,0,NULL,NULL,NULL),(125,0,NULL,NULL,'http://www.boinq.org/iri/graph/local#1_113'),(126,0,NULL,NULL,NULL),(127,0,NULL,NULL,'http://rdf.ebi.ac.uk/dataset/homo_sapiens'),(128,1,NULL,'https://www.ebi.ac.uk/rdf/services/sparql','http://rdf.ebi.ac.uk/dataset/homo_sapiens'),(130,0,NULL,'','TranscriptTest1'),(131,0,NULL,'','TranscriptTest1'),(132,0,NULL,'','TranscriptTest1'),(133,0,NULL,'','TranscriptTest1'),(134,0,NULL,'','TranscriptTest1'),(135,0,NULL,'','TranscriptTest1'),(136,0,NULL,'','TranscriptTest1'),(137,0,NULL,'','http://www.boinq.org/examples/transcript'),(138,0,NULL,'','http://www.boinq.org/examples/transcript'),(139,0,NULL,'','http://www.boinq.org/examples/transcript'),(140,0,NULL,'','http://www.boinq.org/examples/transcript'),(141,0,NULL,'','http://www.boinq.org/examples/transcript'),(142,0,NULL,'','http://www.boinq.org/examples/transcript'),(143,0,NULL,'','http://www.boinq.org/examples/transcript'),(144,0,NULL,'','http://www.boinq.org/examples/transcript'),(145,0,NULL,'','http://www.boinq.org/examples/transcript'),(146,0,NULL,'','http://www.boinq.org/examples/transcript'),(147,0,NULL,'','http://www.boinq.org/testTranscript1'),(148,0,NULL,'','http://www.boinq.org/testTranscript1'),(149,0,NULL,'','http://www.boinq.org/testTranscript1'),(150,0,NULL,'','http://www.boinq.org/testTranscript1'),(151,0,NULL,'','http://www.boinq.org/testTranscript1'),(152,0,NULL,'','http://www.boinq.org/testTranscript1'),(153,0,NULL,'','testGraph3'),(154,0,NULL,'','testGraph3'),(155,0,NULL,'','testGraph3'),(156,0,NULL,'','testGraph3'),(157,1,NULL,'http://rdf.disgenet.org/sparql/','http://rdf.disgenet.org'),(158,0,NULL,NULL,NULL),(159,0,NULL,NULL,NULL),(160,0,NULL,NULL,NULL),(161,0,NULL,NULL,NULL),(162,1,NULL,'https://id.nlm.nih.gov/mesh/sparql','http://id.nlm.nih.gov/mesh'),(163,0,NULL,NULL,NULL),(164,0,NULL,NULL,NULL),(165,0,NULL,NULL,NULL),(166,0,NULL,NULL,NULL);
/*!40000 ALTER TABLE `t_graphtemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_nodefilter`
--

DROP TABLE IF EXISTS `t_nodefilter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_nodefilter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL,
  `case_insensitive` bit(1) DEFAULT NULL,
  `negate` bit(1) DEFAULT NULL,
  `exact_match` bit(1) DEFAULT NULL,
  `min_integer` bigint(20) DEFAULT NULL,
  `max_integer` bigint(20) DEFAULT NULL,
  `integer_value` bigint(20) DEFAULT NULL,
  `min_double` double DEFAULT NULL,
  `max_double` double DEFAULT NULL,
  `double_value` double DEFAULT NULL,
  `include_min` bit(1) DEFAULT NULL,
  `include_max` bit(1) DEFAULT NULL,
  `string_value` varchar(255) DEFAULT NULL,
  `term_values` text,
  `contig` varchar(255) DEFAULT NULL,
  `strand` bit(1) DEFAULT NULL,
  `querynode_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_nodefilter_querynode` (`querynode_id`),
  CONSTRAINT `fk_nodefilter_querynode` FOREIGN KEY (`querynode_id`) REFERENCES `t_querynode` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_nodefilter`
--

LOCK TABLES `t_nodefilter` WRITE;
/*!40000 ALTER TABLE `t_nodefilter` DISABLE KEYS */;
INSERT INTO `t_nodefilter` VALUES (2,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,168),(3,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,170),(4,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,184),(5,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,189),(6,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,194),(7,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,204),(8,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,206),(9,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,218),(10,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,225),(11,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,227),(12,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,237),(13,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,243),(14,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,246),(15,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,250),(19,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','ENST00000380152','','',NULL,278),(20,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','','http://id.nlm.nih.gov/mesh/vocab#Concept','',NULL,284),(31,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','','http://purl.obolibrary.org/obo/HP_0006749','',NULL,324),(32,0,'\0','\0','',NULL,NULL,1,NULL,NULL,NULL,'\0','\0','','','',NULL,333),(33,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','','http://purl.obolibrary.org/obo/HP_0006749','',NULL,340),(34,0,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','','http://id.nlm.nih.gov/mesh/vocab#Concept','',NULL,345),(35,3,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','Colon','','',NULL,347),(36,3,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','Cancer','','',NULL,347),(37,0,'\0','\0','',NULL,NULL,1,NULL,NULL,NULL,'\0','\0','','','',NULL,363),(38,3,'\0','\0','\0',NULL,NULL,NULL,NULL,NULL,NULL,'\0','\0','Colonic neoplasms','','',NULL,370),(39,1,'\0','\0','\0',NULL,NULL,NULL,0.4,NULL,NULL,'\0','\0','','','',NULL,372);
/*!40000 ALTER TABLE `t_nodefilter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_nodetemplate`
--

DROP TABLE IF EXISTS `t_nodetemplate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_nodetemplate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `variable_prefix` varchar(255) DEFAULT NULL,
  `value_source_type` int(11) DEFAULT NULL,
  `values_endpoint` varchar(255) DEFAULT NULL,
  `values_graph` varchar(255) DEFAULT NULL,
  `values_root_term` varchar(255) DEFAULT NULL,
  `fixed_value` varchar(255) DEFAULT NULL,
  `fixed_type` varchar(255) DEFAULT NULL,
  `literal_xsd_type` varchar(255) DEFAULT NULL,
  `assembly` varchar(255) DEFAULT NULL,
  `filterable` bit(1) DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `x` int(11) DEFAULT NULL,
  `y` int(11) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_nodetemplate`
--

LOCK TABLES `t_nodetemplate` WRITE;
/*!40000 ALTER TABLE `t_nodetemplate` DISABLE KEYS */;
INSERT INTO `t_nodetemplate` VALUES (1,4,'GDA','Gene Disease Association','gda',0,'','','','','http://semanticscience.org/resource/SIO_000983','',NULL,'\0','blue',92,93,1),(2,4,'Gene','Gene','gene',0,'','','','','http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16612','',NULL,'\0','blue',93,183,2),(3,0,'?pw','','pw',0,'','','','',NULL,'',NULL,'\0','blue',194,255,3),(4,0,'Biochemical Pathway','','type',0,'','','','http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C20633',NULL,'',NULL,'\0','blue',298,252,4),(6,0,'pathway ID','','pathwayId',0,'','','','',NULL,'',NULL,'','blue',294,320,6),(7,4,'Disease','Disease','disease',0,'','','','','http://semanticscience.org/resource/SIO_010299','',NULL,'','blue',197,20,7),(8,1,'Id','Disease Identifier','diseaseId',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','blue',340,19,8),(99,0,NULL,'The main feature',NULL,0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0000240',NULL,NULL,'\0','blue',551,187,1),(100,1,'Description',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',561,326,5),(101,0,NULL,'The entry describing the feature and its subfeatures',NULL,0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',314,186,0),(102,4,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue',NULL,NULL,'\0','blue',377,227,15),(103,4,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685',NULL,NULL,'\0','blue',615,91,17),(104,0,NULL,'Subfeature',NULL,0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001060',NULL,NULL,'\0','blue',791,183,2),(105,1,'Description',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',835,327,8),(106,4,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred',NULL,NULL,'\0','blue',373,258,11),(107,2,'Faldo Location','Location according the FALDO ontology',NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',503,38,9),(108,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',462,258,12),(109,4,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685',NULL,NULL,'\0','blue',876,83,19),(110,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#double',NULL,'','yellow',975,80,20),(111,2,'Faldo Location','Location according the FALDO ontology',NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',766,41,10),(112,4,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen',NULL,NULL,'\0','blue',368,296,13),(113,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',458,295,14),(114,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#double',NULL,'','yellow',704,89,18),(115,1,'Identifier',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',882,234,6),(116,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',464,226,16),(117,1,'Identifier',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',636,234,3),(118,1,'Label',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',883,294,7),(119,1,'Label',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',622,292,4),(120,1,'asdfa','asdfa','ffadsf',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','\0','',243,136,1),(121,0,'sadfaf','ff','ff',2,'','','','ffaaasee',NULL,'','','\0','',485,142,2),(122,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',50,50,0),(123,4,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen',NULL,NULL,'\0','blue',150,0,9),(124,4,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred',NULL,NULL,'\0','blue',150,-50,7),(125,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,-50,8),(126,4,'Main Feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0000110',NULL,NULL,'\0','blue',166,302,1),(127,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',164,285,4),(128,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,0,10),(129,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',58,281,5),(130,4,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685',NULL,NULL,'\0','blue',266,202,13),(131,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',261,296,3),(132,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',152,58,6),(133,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#double',NULL,'','yellow',416,202,14),(134,4,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue',NULL,NULL,'\0','blue',150,50,11),(135,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,50,12),(136,4,'Feature','Subfeature','subFeature',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0000110',NULL,NULL,'\0','blue',791,183,2),(137,4,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0000110',NULL,NULL,'\0','blue',-150,50,1),(138,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',-200,50,3),(139,4,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685',NULL,NULL,'\0','blue',-50,-50,13),(140,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#double',NULL,'','yellow',100,-50,14),(141,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',50,50,0),(142,4,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue',NULL,NULL,'\0','blue',150,50,11),(143,4,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred',NULL,NULL,'\0','blue',150,-50,7),(144,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,-50,8),(145,4,'Subfeature','Subfeature','subfeature',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0000110',NULL,NULL,'\0','blue',-250,50,2),(146,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',-250,50,4),(147,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',-500,50,6),(148,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',-350,50,5),(149,4,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen',NULL,NULL,'\0','blue',150,0,9),(150,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,0,10),(151,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,50,12),(152,4,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0000110',NULL,NULL,'\0','blue',150,150,1),(153,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',150,100,6),(154,4,'Subfeature','Subfeature','subfeature',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0000110',NULL,NULL,'\0','blue',250,150,2),(155,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',200,200,4),(156,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',150,200,3),(157,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',50,50,0),(158,4,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred',NULL,NULL,'\0','blue',150,-50,7),(159,4,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685',NULL,NULL,'\0','blue',250,50,13),(160,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#double',NULL,'','yellow',400,50,14),(161,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',100,200,5),(162,4,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen',NULL,NULL,'\0','blue',150,0,9),(163,4,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue',NULL,NULL,'\0','blue',150,50,11),(164,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,50,12),(165,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,0,10),(166,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,-50,8),(167,4,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0000110',NULL,NULL,'\0','blue',150,150,1),(168,4,'Subfeature','Subfeature','subfeature',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0000110',NULL,NULL,'\0','blue',250,150,2),(169,4,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685',NULL,NULL,'\0','blue',250,50,13),(170,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',150,100,6),(171,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',200,200,4),(172,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',50,50,0),(173,4,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred',NULL,NULL,'\0','blue',150,-50,7),(174,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',100,200,5),(175,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',150,200,3),(176,4,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen',NULL,NULL,'\0','blue',150,0,9),(177,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,-50,8),(178,4,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue',NULL,NULL,'\0','blue',150,50,11),(179,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#double',NULL,'','yellow',400,50,14),(180,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,50,12),(181,1,'value',NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#integer',NULL,'','yellow',300,0,10),(182,0,'Main feature','The main feature','http://purl.obolibrary.org/obo/SO_0001060',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,100,4),(183,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',300,-50,9),(184,3,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685','http://www.w3.org/2001/XMLSchema#double',NULL,'','blue',200,-50,10),(185,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',400,150,7),(186,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',300,150,6),(187,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',200,150,8),(188,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',150,100,0),(189,3,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',200,150,1),(190,0,'Subfeature','Subfeature','http://purl.obolibrary.org/obo/SO_0000110',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',450,100,5),(191,3,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',200,200,2),(192,3,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',200,250,3),(193,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',150,150,0),(194,3,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',100,200,1),(195,0,'Main feature','The main feature','http://purl.obolibrary.org/obo/SO_0001060',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(196,0,'Feature Type',NULL,'featureType',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','blue',400,200,11),(197,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',400,250,7),(198,3,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',150,200,2),(199,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',300,200,9),(200,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',200,250,8),(201,3,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',200,200,3),(202,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',300,250,6),(203,3,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685','http://www.w3.org/2001/XMLSchema#double',NULL,'','blue',200,200,10),(204,0,'Subfeature','Subfeature','http://purl.obolibrary.org/obo/SO_0000110',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',450,150,5),(205,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',150,150,0),(206,3,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',200,200,3),(207,0,'Main feature','The main feature','http://purl.obolibrary.org/obo/SO_0001060',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(208,0,'Subfeature','Subfeature','http://purl.obolibrary.org/obo/SO_0000400',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',450,150,5),(209,3,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',150,200,2),(210,3,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',100,200,1),(211,0,'Feature Type',NULL,'featureType',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','blue',400,100,11),(212,3,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685','http://www.w3.org/2001/XMLSchema#double',NULL,'','blue',200,100,10),(213,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',300,100,9),(214,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',400,250,7),(215,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',200,250,8),(216,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',300,250,6),(251,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(252,3,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685','http://www.w3.org/2001/XMLSchema#double',NULL,'','blue',200,100,10),(253,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(254,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',200,250,8),(255,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(256,0,'Subfeature','Subfeature','subFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',444,184,5),(257,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(258,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',300,250,6),(259,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(260,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',400,250,7),(261,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',150,150,0),(262,3,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',150,200,2),(263,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',150,150,0),(264,3,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',100,200,1),(265,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',150,150,0),(266,3,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',200,200,3),(267,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',150,150,0),(268,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(269,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(270,0,'Feature Type',NULL,'featureType',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','blue',412,108,11),(271,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',300,150,4),(272,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',403,49,9),(273,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',131,130,0),(274,3,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',200,200,3),(275,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',305,175,4),(276,0,'Feature Type',NULL,'featureType',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','blue',400,100,11),(277,0,'Subfeature','Subfeature','subFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',450,150,5),(278,3,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',150,200,2),(279,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',300,250,6),(280,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',200,250,8),(281,3,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',100,200,1),(282,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',300,100,9),(283,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',400,250,7),(284,3,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685','http://www.w3.org/2001/XMLSchema#double',NULL,'','blue',200,100,10),(285,0,'Main feature','The main feature','mainFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',290,183,4),(286,1,'Identifier',NULL,'mainId',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',300,250,6),(287,0,'Feature Type',NULL,'featureType',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','blue',400,100,11),(288,4,'Entry','The entry describing the feature and its subfeatures','entry',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#BED_Entry',NULL,NULL,'\0','blue',138,131,0),(289,3,'RGB red',NULL,'rgbRed',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBred','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',100,200,1),(290,1,'Description',NULL,'mainDescription',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',200,250,8),(291,3,'RGB green',NULL,'rgbGreen',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBgreen','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',150,200,2),(292,0,'Subfeature','Subfeature','subFeature',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0','blue',450,150,5),(293,3,'Score',NULL,'score',0,NULL,NULL,NULL,NULL,'http://purl.obolibrary.org/obo/SO_0001685','http://www.w3.org/2001/XMLSchema#double',NULL,'','blue',200,100,10),(294,1,'Label',NULL,'mainLabel',0,NULL,NULL,NULL,NULL,NULL,'http://www.w3.org/2001/XMLSchema#string',NULL,'','yellow',400,250,7),(295,3,'RGB blue',NULL,'rgbBlue',0,NULL,NULL,NULL,NULL,'http://www.boinq.org/iri/ontologies/format#RGBblue','http://www.w3.org/2001/XMLSchema#integer',NULL,'','blue',200,200,3),(296,2,'Location','Location according the FALDO ontology','mainLocation',0,NULL,NULL,NULL,NULL,NULL,NULL,'GRCh38','','green',300,100,9),(297,4,'Transcript','transcript','transcript',0,'','','','','http://purl.obolibrary.org/obo/SO_0000673','','','','',242,232,2),(298,4,'Gene','A protein coding gene','gene',0,'','','','','http://purl.obolibrary.org/obo/SO_0000704','','','','',241,127,1),(299,4,'Transcript','transcript','transcript',0,'','','','','http://purl.obolibrary.org/obo/SO_0000673','','','','',242,232,2),(300,2,'transcript_location','Transcript location','transcript_location',0,'','','','',NULL,'','','','',399,234,4),(301,4,'Gene','A protein coding gene','gene',0,'','','','','http://purl.obolibrary.org/obo/SO_0000704','','','','',241,127,1),(302,2,'gene_location','Gene location','gene_location',0,'','','','',NULL,'','','','',404,130,3),(303,0,'node1','','node1',0,'','','','',NULL,'','','\0','',394,32,1),(304,0,'node2','','node2',0,'','','','',NULL,'','','\0','',442,136,2),(305,0,'node3','','',0,'','','','',NULL,'','','\0','',299,202,3),(306,0,'node4','','node4',0,'','','','',NULL,'','','\0','',512,207,4),(307,4,'Transcript','','transcript',0,'','','','','http://purl.obolibrary.org/obo/SO_0000234','','','','',227,160,2),(308,4,'Gene','','gene',0,'','','','','http://purl.obolibrary.org/obo/SO_0001217','','','','',225,44,1),(309,2,'gene_location','Location of the Gene','gene_location',0,'','','','',NULL,'','','','',374,43,3),(310,2,'transcript_location','Location of the Transcript','transcript_location',0,'','','','',NULL,'','','','',366,162,4),(311,4,'Exon','','exon',0,'','','','','http://purl.obolibrary.org/obo/SO_0000147','','','','',226,314,5),(312,4,'Ordered part','','ordered_part',0,'','','','','http://semanticscience.org/resource/SIO_001261','','','\0','',159,239,6),(313,1,'Rank','Rank of the Exon in the Transcript','rank',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#integer','','','',100,313,7),(314,2,'exon_location','Location of the Exon','exon_location',0,'','','','',NULL,'','','','',338,312,8),(315,1,'Synonym','','synonym',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',323,107,9),(316,0,'Translation','','translation',1,'','','','',NULL,'','','\0','',109,161,10),(317,1,'Id','Gene Identifier','gene_id',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',303,18,11),(318,1,'Id','Transcript identifier','transcript_id',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',282,126,12),(319,1,'Id','Exon Identifier','exon_id',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',283,278,13),(320,4,'Phenotype','Phenotype','phenotype',0,'','','','','http://semanticscience.org/resource/SIO_010056','','','\0','',200,130,9),(321,1,'Id','Phenotype Identifier','phenotypeId',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',298,86,10),(324,4,'GDA','Gene Disease Association','gda',0,'','','','','http://semanticscience.org/resource/SIO_001121','','','\0','',274,158,1),(325,4,'Gene','Gene','gene',0,'','','','','http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16612','','','\0','',144,149,2),(326,4,'DiseaseClass','Disease Class','diseaseClass',0,'','','','','http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C7057','','','\0','',346,223,3),(327,1,'Id','Disease id','diseaseId',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',383,308,4),(328,0,'Id','Gene Id','geneId',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',144,229,5),(329,4,'Phenotype','Phenotype','phenotype',0,'','','','','http://semanticscience.org/resource/SIO_010056','','','\0','',364,70,7),(330,0,'Mesh','MeSH Topical Descriptor','meshTerm',1,'http://id.nlm.nih.gov/mesh/sparql','MeSH','http://id.nlm.nih.gov/mesh/vocab#TopicalDescriptor','',NULL,'','','','',434,200,6),(331,4,'Phenotype','Phenotype','phenotype',0,'','','','','http://semanticscience.org/resource/SIO_010056','','','\0','',364,70,7),(332,1,'Id','Phenotype Id','phenotypeId',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',438,21,8),(333,4,'Phenotype','Phenotype','phenotype',0,'','','','','http://semanticscience.org/resource/SIO_010056','','','\0','',364,76,7),(334,0,'HPO','Human Phenotype Ontology term','hpo',1,'http://fuseki:3030/static/sparql','http://purl.bioontology.org/ontology/HP','','',NULL,'','','','',435,132,9),(335,3,'Score','Score of the association','score',0,'','','','','http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C25338','http://www.w3.org/2001/XMLSchema#double','','','',236,74,10),(336,1,'Id','Translation Identifier','translation_id',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',23,161,14),(337,0,'Xref','Cross reference to other identifiers','alternativeId',1,'','','','',NULL,'','','\0','',116,48,15),(338,4,'Topic','Topical Descriptor','topic',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#TopicalDescriptor','','','\0','',260,183,2),(339,1,'label','Label','label',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','\0','',150,111,4),(340,4,'Concept','MeSH Concept','concept',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#Concept','','','\0','',263,66,3),(341,1,'label','Label','label',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','\0','',150,111,4),(342,4,'Topic','Topical Descriptor','topic',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#TopicalDescriptor','','','\0','',260,183,2),(343,4,'Concept','MeSH Concept','concept',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#Concept','','','\0','',263,66,3),(344,4,'Topic','Topical Descriptor','topic',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#TopicalDescriptor','','','\0','',260,183,2),(345,0,'TreeNumber','Mesh Tree Number','treeNumber',1,'https://id.nlm.nih.gov/mesh/sparql','','','',NULL,'','','\0','',410,181,1),(346,0,'one','','',0,'','','','',NULL,'','','\0','',263,116,1),(347,0,'two','','',0,'','','','',NULL,'','','\0','',190,196,2),(348,0,'one2','','',0,'','','','',NULL,'','','\0','',323,243,3),(349,0,'a','','',0,'','','','',NULL,'','','\0','',351,114,1),(350,0,'b','','',0,'','','','',NULL,'','','\0','',236,249,2),(351,0,'c','','',0,'','','','',NULL,'','','\0','',574,232,3),(352,4,'Concept','Concept','concept',0,'','','','',NULL,'','','\0','',511,99,2),(353,4,'Topic','Topical Descriptor','topic',0,'','','','',NULL,'','','\0','',294,137,1),(354,0,'c','','',0,'','','','',NULL,'','','\0','',257,273,3),(355,0,'a','','',0,'','','','',NULL,'','','\0','',269,132,1),(356,0,'b','','',0,'','','','',NULL,'','','\0','',458,118,2),(357,0,'b','','',0,'','','','',NULL,'','','\0','',332,162,2),(358,0,'a','','',0,'','','','',NULL,'','','\0','',167,161,1),(359,4,'Term','MeSH Term','term',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#Term','','','\0','',343,286,2),(360,4,'Concept','MeSH Concept','concept',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#Concept','','','\0','',213,240,1),(361,4,'Topic','Topical Descriptor','topic',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#TopicalDescriptor','','','\0','',104,169,3),(362,4,'TreeNumber','MeSH Tree Number, hierarchical','treeNumber',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#TreeNumber','','','\0','',208,86,4),(363,1,'label','Term Label','termLabel',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',343,361,6),(364,1,'label','Concept label','conceptLabel',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',187,332,5),(365,1,'label','Topic label','topicLabel',0,'','','','',NULL,'http://www.w3.org/2001/XMLSchema#string','','','',74,286,7),(366,4,'Parent','Parent Tree Number (use in separate graph for recursion)','parentTreeNumber',0,'','','','','http://id.nlm.nih.gov/mesh/vocab#TreeNumber','','','\0','',361,159,8);
/*!40000 ALTER TABLE `t_nodetemplate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_progress`
--

DROP TABLE IF EXISTS `t_progress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_progress` (
  `analysis_id` bigint(20) DEFAULT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `percentage` float DEFAULT NULL,
  KEY `fk_progressToAnalysis` (`analysis_id`),
  CONSTRAINT `fk_progressToAnalysis` FOREIGN KEY (`analysis_id`) REFERENCES `t_analysis` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_progress`
--

LOCK TABLES `t_progress` WRITE;
/*!40000 ALTER TABLE `t_progress` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_project`
--

DROP TABLE IF EXISTS `t_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_a0kdmtvfioekufbkj7t3161qe` (`owner_id`),
  CONSTRAINT `fk_a0kdmtvfioekufbkj7t3161qe` FOREIGN KEY (`owner_id`) REFERENCES `jhi_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_project`
--

LOCK TABLES `t_project` WRITE;
/*!40000 ALTER TABLE `t_project` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_project_t_track`
--

DROP TABLE IF EXISTS `t_project_t_track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_project_t_track` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `T_PROJECT_id` bigint(20) DEFAULT NULL,
  `tracks_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_gxlx7clnpyr5voo4qxyyrn3kh` (`T_PROJECT_id`),
  KEY `fk_sjplolmoi7krxotu85hr3mr24` (`tracks_id`),
  CONSTRAINT `fk_gxlx7clnpyr5voo4qxyyrn3kh` FOREIGN KEY (`T_PROJECT_id`) REFERENCES `t_project` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_sjplolmoi7krxotu85hr3mr24` FOREIGN KEY (`tracks_id`) REFERENCES `t_track` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_project_t_track`
--

LOCK TABLES `t_project_t_track` WRITE;
/*!40000 ALTER TABLE `t_project_t_track` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_project_t_track` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_querybridge`
--

DROP TABLE IF EXISTS `t_querybridge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_querybridge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_node_id` bigint(20) DEFAULT NULL,
  `from_graph_id` bigint(20) DEFAULT NULL,
  `to_node_id` bigint(20) DEFAULT NULL,
  `to_graph_id` bigint(20) DEFAULT NULL,
  `string_to_entity_template` varchar(255) DEFAULT NULL,
  `literal_to_literal_match_type` int(11) DEFAULT NULL,
  `match_strand` bit(1) DEFAULT NULL,
  `querydefinition_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_querybridge_fromGraph` (`from_graph_id`),
  KEY `fk_querybridge_fromNode` (`from_node_id`),
  KEY `fk_querybridge_toGraph` (`to_graph_id`),
  KEY `fk_querybridge_toNode` (`to_node_id`),
  KEY `fk_querybridge_querydefinition` (`querydefinition_id`),
  CONSTRAINT `fk_querybridge_fromGraph` FOREIGN KEY (`from_graph_id`) REFERENCES `t_querygraph` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_querybridge_fromNode` FOREIGN KEY (`from_node_id`) REFERENCES `t_querynode` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_querybridge_querydefinition` FOREIGN KEY (`querydefinition_id`) REFERENCES `t_querydefinition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_querybridge_toGraph` FOREIGN KEY (`to_graph_id`) REFERENCES `t_querygraph` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_querybridge_toNode` FOREIGN KEY (`to_node_id`) REFERENCES `t_querynode` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_querybridge`
--

LOCK TABLES `t_querybridge` WRITE;
/*!40000 ALTER TABLE `t_querybridge` DISABLE KEYS */;
INSERT INTO `t_querybridge` VALUES (100,137,104,139,105,'asdfa',0,'\0',103),(101,326,135,321,134,'',0,'\0',117),(102,339,137,334,136,'',7,'\0',118),(103,348,139,355,140,'',0,'\0',120),(104,364,142,361,141,'',0,'\0',121),(105,371,143,368,142,'',0,'\0',121);
/*!40000 ALTER TABLE `t_querybridge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_querydefinition`
--

DROP TABLE IF EXISTS `t_querydefinition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_querydefinition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `species` varchar(255) DEFAULT NULL,
  `assembly` varchar(255) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `target_graph` varchar(255) DEFAULT NULL,
  `target_file` varchar(255) DEFAULT NULL,
  `result_as_table` bit(1) DEFAULT NULL,
  `sparql_query` text,
  PRIMARY KEY (`id`),
  KEY `fk_querydefinition_user` (`owner_id`),
  CONSTRAINT `fk_querydefinition_user` FOREIGN KEY (`owner_id`) REFERENCES `jhi_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_querydefinition`
--

LOCK TABLES `t_querydefinition` WRITE;
/*!40000 ALTER TABLE `t_querydefinition` DISABLE KEYS */;
INSERT INTO `t_querydefinition` VALUES (103,'mainman',0,'','','',3,'','','\0',NULL),(108,'Check ensembl example',0,'See second example from https://www.ebi.ac.uk/rdf/services/sparql','','',3,'testEnsembl','','\0',NULL),(109,'ENST00000380152',0,'Reproduce ensembl query','','',3,'testEnsembl1','','\0',''),(110,'ENST00000380152',0,'Find all exons of transcript ENST00000380152\nMimicks example query from https://www.ebi.ac.uk/rdf/services/sparql','','',3,'TranscriptExample','','\0',''),(115,'ENST00000380152',0,'Reproduce ensembl exons from ENST00000380152','','',3,'testGraph3','','\0',''),(116,'',0,'','','',3,'','','\0',''),(117,'Test1',0,'Find location of transcripts associated with intestinal tract tumors','','',3,'http://boinq.org/resultTest1','','\0',''),(118,'TestQuery1',0,'Find location of first exons of genes associated with malignant gastrointestinal tumors','','',3,'testQuery1','','\0','PREFIX  owl:  <http://www.w3.org/2002/07/owl#>\nPREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>\nPREFIX  gfvo: <http://www.biointerchange.org/gfvo#>\nPREFIX  skos: <http://www.w3.org/2004/02/skos/core#>\nPREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX  ensembl: <http://rdf.ebi.ac.uk/resource/>\nPREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nPREFIX  faldo: <http://biohackathon.org/resource/faldo#>\nPREFIX  sio:  <http://semanticscience.org/resource/>\nPREFIX  dcterms: <http://purl.org/dc/terms/>\nPREFIX  obo:  <http://purl.obolibrary.org/obo/>\nPREFIX  foaf: <http://xmlns.com/foaf/0.1/>\nPREFIX  dc:   <http://purl.org/dc/elements/1.1/>\n\nINSERT {\n  GRAPH <testQuery1> {\n    ?exon_location1_begin faldo:position ?exon_location1_beginpos .\n    ?ordered_part1 sio:SIO_000300 ?rank1 .\n    ?gene3 rdf:type <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16612> .\n    ?transcript1 rdf:type obo:SO_0000234 .\n    ?phenotype1 rdfs:subClassOf ?hpo1 .\n    ?transcript1 obo:SO_transcribed_from ?gene2 .\n    ?exon1 faldo:location ?exon_location1 .\n    ?transcript1 sio:SIO_000974 ?ordered_part1 .\n    ?gda1 sio:SIO_000628 ?gene3 .\n    ?exon_location1 faldo:end ?exon_location1_end .\n    ?gene3 skos:exactMatch ?geneId1 .\n    ?ordered_part1 rdf:type sio:SIO_001261 .\n    ?gene2 skos:altLabel ?synonym1 .\n    ?exon_location1_begin faldo:reference ?exon_location1_reference .\n    ?exon_location1_end faldo:position ?exon_location1_endpos .\n    ?exon_location1 faldo:begin ?exon_location1_begin .\n    ?gene2 rdf:type obo:SO_0000704 .\n    ?exon_location1_begin rdf:type ?exon_location1_positiontype .\n    ?exon1 rdf:type obo:SO_0000147 .\n    ?ordered_part1 sio:SIO_000628 ?exon1 .\n    ?gda1 rdf:type sio:SIO_000983 .\n    ?gda1 sio:SIO_000628 ?phenotype1 .\n    ?gene2 dc:identifier ?gene_id1 .\n    ?phenotype1 rdf:type sio:SIO_010056 .\n  }\n}\nWHERE\n  { SERVICE <https://www.ebi.ac.uk/rdf/services/sparql>\n      { GRAPH <http://rdf.ebi.ac.uk/dataset/homo_sapiens>\n          { ?gene2    dc:identifier         ?gene_id1 .\n            ?transcript1  sio:SIO_000974    ?ordered_part1 .\n            ?exon1    faldo:location        ?exon_location1 .\n            ?transcript1  obo:SO_transcribed_from  ?gene2 .\n            ?gene2    skos:altLabel         ?synonym1 .\n            ?ordered_part1\n                      sio:SIO_000628        ?exon1 ;\n                      sio:SIO_000300        ?rank1 .\n            ?exon_location1\n                      faldo:begin           ?exon_location1_begin .\n            ?exon_location1_begin\n                      faldo:position        ?exon_location1_beginpos ;\n                      faldo:reference       ?exon_location1_reference .\n            ?exon_location1\n                      faldo:end             ?exon_location1_end .\n            ?exon_location1_end\n                      faldo:position        ?exon_location1_endpos .\n            ?exon_location1_begin\n                      rdf:type              ?exon_location1_positiontype .\n            ?gene2    rdf:type              obo:SO_0000704 .\n            ?ordered_part1\n                      rdf:type              sio:SIO_001261 .\n            ?transcript1  rdf:type          obo:SO_0000234 .\n            ?exon1    rdf:type              obo:SO_0000147\n            FILTER ( iri(?exon_location1_value_positiontype) IN (iri(faldo:ForwardStrandPosition), iri(faldo:ReverseStrandPosition)) )\n            FILTER ( ?rank1_value = 1 )\n          }}\n    SERVICE <http:/rdf.disgenet.org/sparql/>\n      { GRAPH <http://rdf.disgenet.org>\n          { ?gene3    skos:exactMatch  ?geneId1 .\n            ?gda1     sio:SIO_000628   ?gene3 .\n            ?phenotype1  rdfs:subClassOf  ?hpo1 .\n            ?gda1     sio:SIO_000628   ?phenotype1 .\n            ?gene3    rdf:type         <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16612> .\n            ?gda1     rdf:type         sio:SIO_000983 .\n            ?phenotype1  rdf:type      sio:SIO_010056\n            FILTER sameTerm(?hpo1_value, \"\")\n          }}\n    FILTER ( str(?geneId1) = str(?synonym1) )\n  }\n'),(119,'s',0,'s','','',3,'s','','\0','PREFIX  owl:  <http://www.w3.org/2002/07/owl#>\nPREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>\nPREFIX  gfvo: <http://www.biointerchange.org/gfvo#>\nPREFIX  skos: <http://www.w3.org/2004/02/skos/core#>\nPREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX  ensembl: <http://rdf.ebi.ac.uk/resource/>\nPREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nPREFIX  faldo: <http://biohackathon.org/resource/faldo#>\nPREFIX  sio:  <http://semanticscience.org/resource/>\nPREFIX  dcterms: <http://purl.org/dc/terms/>\nPREFIX  obo:  <http://purl.obolibrary.org/obo/>\nPREFIX  foaf: <http://xmlns.com/foaf/0.1/>\nPREFIX  dc:   <http://purl.org/dc/elements/1.1/>\n\nINSERT {\n  GRAPH <s> {\n    ?gene4 rdf:type <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16612> .\n    ?disease0 rdf:type sio:SIO_010299 .\n    ?gda2 rdf:type sio:SIO_000983 .\n    ?disease0 sio:SIO_000095 ?meshTerm0 .\n    ?gda2 sio:SIO_000628 ?gene4 .\n    ?gene4 skos:exactMatch ?geneId2 .\n    ?gda2 sio:SIO_000628 ?disease0 .\n  }\n}\nWHERE\n  { SERVICE <http:/rdf.disgenet.org/sparql/>\n      { GRAPH <http://rdf.disgenet.org>\n          { ?gene4    skos:exactMatch  ?geneId2 .\n            ?disease0  sio:SIO_000095  ?meshTerm0 .\n            ?gda2     sio:SIO_000628   ?disease0 ;\n                      sio:SIO_000628   ?gene4 .\n            ?gene4    rdf:type         <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C16612> .\n            ?gda2     rdf:type         sio:SIO_000983 .\n            ?disease0  rdf:type        sio:SIO_010299\n            FILTER sameTerm(?meshTerm0_value, \"\")\n          }}\n  }\n'),(120,'Find Genes related to Colon',0,'','','',3,'colonGenes','','\0','PREFIX  owl:  <http://www.w3.org/2002/07/owl#>\nPREFIX  meshv: <http://id.nlm.nih.gov/mesh/vocab#>\nPREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>\nPREFIX  gfvo: <http://www.biointerchange.org/gfvo#>\nPREFIX  skos: <http://www.w3.org/2004/02/skos/core#>\nPREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX  ensembl: <http://rdf.ebi.ac.uk/resource/>\nPREFIX  ncit: <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#>\nPREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nPREFIX  faldo: <http://biohackathon.org/resource/faldo#>\nPREFIX  sio:  <http://semanticscience.org/resource/>\nPREFIX  dcterms: <http://purl.org/dc/terms/>\nPREFIX  obo:  <http://purl.obolibrary.org/obo/>\nPREFIX  foaf: <http://xmlns.com/foaf/0.1/>\nPREFIX  mesh: <https://id.nlm.nih.gov/mesh/>\nPREFIX  dc:   <http://purl.org/dc/elements/1.1/>\n\nINSERT {\n  GRAPH <colonGenes> {\n    ?gda4 sio:SIO_000628 ?diseaseClass4 .\n    ?gene4 rdf:type ncit:C16612 .\n    ?concept4 rdfs:label ?conceptLabel4 .\n    ?gda4 sio:SIO_000216 ?score4 .\n    ?diseaseClass4 sio:SIO_000332 ?xref4 .\n    ?gda4 rdf:type sio:SIO_001121 .\n    ?diseaseClass4 rdf:type ncit:C7057 .\n    ?concept4 rdf:type meshv:Concept .\n    ?xref4 meshv:preferredConcept ?concept4 .\n    ?xref4 rdf:type meshv:TopicalDescriptor .\n    ?gene4 skos:exactMatch ?geneId4 .\n    ?gda4 sio:SIO_000628 ?gene4 .\n  }\n}\nWHERE\n  { SERVICE mesh:sparql\n      { GRAPH <http://id.nlm.nih.gov/mesh/>\n          { ?xref4    meshv:preferredConcept  ?concept4 .\n            ?concept4  rdfs:label           ?conceptLabel4 .\n            ?xref4    rdf:type              meshv:TopicalDescriptor .\n            ?concept4  rdf:type             meshv:Concept\n            FILTER regex(str(?conceptLabel4), \"Cancer\", \"i\")\n            FILTER regex(str(?conceptLabel4), \"Colon\", \"i\")\n          }}\n    SERVICE <http://rdf.disgenet.org/sparql/>\n      { GRAPH <http://rdf.disgenet.org>\n          { ?gda4     sio:SIO_000216   ?score4 .\n            ?diseaseClass4\n                      sio:SIO_000332   ?xref4 .\n            ?gda4     sio:SIO_000628   ?diseaseClass4 .\n            ?gene4    skos:exactMatch  ?geneId4 .\n            ?gda4     sio:SIO_000628   ?gene4 .\n            ?diseaseClass4\n                      rdf:type         ncit:C7057 .\n            ?gene4    rdf:type         ncit:C16612 .\n            ?gda4     rdf:type         sio:SIO_001121 .\n            ?score4   rdf:type         ncit:C25338 ;\n                      sio:SIO_000300   ?score4_value\n          }}\n  }\n'),(121,'Find first exons of genes related to Colon cancer',0,'Find disease terms from MeSH related to Colonic neoplasms, use this in DisGeNet to find associated genes with a large score, then retrieve locations of first exons of those genes.','','',3,'colonCancerFirstExons','','\0','PREFIX  owl:  <http://www.w3.org/2002/07/owl#>\nPREFIX  meshv: <http://id.nlm.nih.gov/mesh/vocab#>\nPREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#>\nPREFIX  gfvo: <http://www.biointerchange.org/gfvo#>\nPREFIX  skos: <http://www.w3.org/2004/02/skos/core#>\nPREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX  ensembl: <http://rdf.ebi.ac.uk/resource/>\nPREFIX  ncit: <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#>\nPREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\nPREFIX  faldo: <http://biohackathon.org/resource/faldo#>\nPREFIX  sio:  <http://semanticscience.org/resource/>\nPREFIX  dcterms: <http://purl.org/dc/terms/>\nPREFIX  obo:  <http://purl.obolibrary.org/obo/>\nPREFIX  foaf: <http://xmlns.com/foaf/0.1/>\nPREFIX  mesh: <http://id.nlm.nih.gov/mesh/>\nPREFIX  dc:   <http://purl.org/dc/elements/1.1/>\n\nINSERT {\n  GRAPH <colonCancerFirstExons> {\n    ?ordered_part0 rdf:type sio:SIO_001261 .\n    ?concept0 rdf:type meshv:Concept .\n    ?exon0 rdf:type obo:SO_0000147 .\n    ?concept0 rdfs:label ?conceptLabel0 .\n    ?gda0 sio:SIO_000216 ?score0 .\n    ?transcript0 rdf:type obo:SO_0000234 .\n    ?xref1 meshv:preferredConcept ?concept0 .\n    ?diseaseClass0 rdf:type ncit:C7057 .\n    ?transcript0 sio:SIO_000974 ?ordered_part0 .\n    ?exon_location0 faldo:begin ?exon_location0_begin .\n    ?gda0 sio:SIO_000628 ?diseaseClass0 .\n    ?exon_location0_begin faldo:position ?exon_location0_beginpos .\n    ?diseaseClass0 sio:SIO_000332 ?xref1 .\n    ?gene0 dc:identifier ?gene_id0 .\n    ?gene0 rdfs:seeAlso ?xref0 .\n    ?exon_location0_begin rdf:type ?exon_location0_positiontype .\n    ?xref0 rdf:type ncit:C16612 .\n    ?ordered_part0 sio:SIO_000628 ?exon0 .\n    ?exon_location0 faldo:end ?exon_location0_end .\n    ?exon_location0_end faldo:position ?exon_location0_endpos .\n    ?transcript0 obo:SO_transcribed_from ?gene0 .\n    ?gda0 sio:SIO_000628 ?xref0 .\n    ?gda0 rdf:type sio:SIO_001121 .\n    ?exon_location0_begin faldo:reference ?exon_location0_reference .\n    ?gene0 rdf:type obo:SO_0001217 .\n    ?ordered_part0 sio:SIO_000300 ?rank0 .\n    ?exon0 faldo:location ?exon_location0 .\n    ?xref1 rdf:type meshv:TopicalDescriptor .\n  }\n}\nWHERE\n  { SERVICE <https://id.nlm.nih.gov/mesh/sparql>\n      { GRAPH <http://id.nlm.nih.gov/mesh>\n          { ?concept0  rdfs:label           ?conceptLabel0 .\n            ?xref1    meshv:preferredConcept  ?concept0 ;\n                      rdf:type              meshv:TopicalDescriptor .\n            ?concept0  rdf:type             meshv:Concept\n            FILTER regex(str(?conceptLabel0), \"Colonic neoplasms\", \"i\")\n          }}\n    SERVICE <https://www.ebi.ac.uk/rdf/services/sparql>\n      { GRAPH <http://rdf.ebi.ac.uk/dataset/homo_sapiens>\n          { ?gene0    rdfs:seeAlso          ?xref0 .\n            ?ordered_part0\n                      sio:SIO_000300        ?rank0 .\n            ?transcript0  sio:SIO_000974    ?ordered_part0 .\n            ?gene0    dc:identifier         ?gene_id0 .\n            ?exon0    faldo:location        ?exon_location0 .\n            ?transcript0  obo:SO_transcribed_from  ?gene0 .\n            ?ordered_part0\n                      sio:SIO_000628        ?exon0 .\n            ?exon_location0\n                      faldo:begin           ?exon_location0_begin .\n            ?exon_location0_begin\n                      faldo:position        ?exon_location0_beginpos ;\n                      faldo:reference       ?exon_location0_reference .\n            ?exon_location0\n                      faldo:end             ?exon_location0_end .\n            ?exon_location0_end\n                      faldo:position        ?exon_location0_endpos .\n            ?exon_location0_begin\n                      rdf:type              ?exon_location0_positiontype .\n            ?exon0    rdf:type              obo:SO_0000147 .\n            ?ordered_part0\n                      rdf:type              sio:SIO_001261 .\n            ?gene0    rdf:type              obo:SO_0001217 .\n            ?transcript0  rdf:type          obo:SO_0000234\n            FILTER ( iri(?exon_location0_positiontype) IN (iri(faldo:ForwardStrandPosition), iri(faldo:ReverseStrandPosition)) )\n            FILTER ( ?rank0 = 1 )\n          }}\n    SERVICE <http://rdf.disgenet.org/sparql/>\n      { GRAPH <http://rdf.disgenet.org>\n          { ?gda0     sio:SIO_000628  ?xref0 ;\n                      sio:SIO_000216  ?score0 ;\n                      sio:SIO_000628  ?diseaseClass0 .\n            ?diseaseClass0\n                      sio:SIO_000332  ?xref1 ;\n                      rdf:type        ncit:C7057 .\n            ?gda0     rdf:type        sio:SIO_001121 .\n            ?score0   rdf:type        ncit:C25338 ;\n                      sio:SIO_000300  ?score0_value .\n            ?xref0    rdf:type        ncit:C16612\n            FILTER ( ?score0_value > 0.4e0 )\n          }}\n  }\n');
/*!40000 ALTER TABLE `t_querydefinition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_queryedge`
--

DROP TABLE IF EXISTS `t_queryedge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_queryedge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `edgetemplate_id` bigint(20) DEFAULT NULL,
  `from_querynode_id` bigint(20) DEFAULT NULL,
  `to_querynode_id` bigint(20) DEFAULT NULL,
  `retrieve` bit(1) DEFAULT NULL,
  `querygraph_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_queryedge_edgetemplate` (`edgetemplate_id`),
  KEY `fk_queryedge_from_querynode` (`from_querynode_id`),
  KEY `fk_queryedge_to_querynode` (`to_querynode_id`),
  KEY `fk_queryedge_querygraph` (`querygraph_id`),
  CONSTRAINT `fk_queryedge_edgetemplate` FOREIGN KEY (`edgetemplate_id`) REFERENCES `t_edgetemplate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_queryedge_from_querynode` FOREIGN KEY (`from_querynode_id`) REFERENCES `t_querynode` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_queryedge_querygraph` FOREIGN KEY (`querygraph_id`) REFERENCES `t_querygraph` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_queryedge_to_querynode` FOREIGN KEY (`to_querynode_id`) REFERENCES `t_querynode` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=337 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_queryedge`
--

LOCK TABLES `t_queryedge` WRITE;
/*!40000 ALTER TABLE `t_queryedge` DISABLE KEYS */;
INSERT INTO `t_queryedge` VALUES (114,101,129,130,'\0',NULL),(115,112,130,131,'\0',NULL),(118,101,135,136,'',NULL),(119,112,136,137,'',104),(120,5,138,139,'',105),(136,275,163,164,'',NULL),(137,277,164,165,'',NULL),(138,273,166,164,'',NULL),(139,276,163,167,'',114),(140,274,166,163,'',NULL),(141,281,166,168,'',NULL),(142,273,169,164,'',NULL),(143,281,169,170,'',114),(144,274,169,163,'',114),(145,275,163,171,'',114),(146,273,169,171,'',114),(147,273,172,173,'',115),(148,274,172,174,'',115),(149,275,174,173,'',115),(150,275,175,176,'',116),(151,273,177,176,'',116),(152,274,177,175,'',116),(156,274,181,182,'',118),(157,273,181,183,'',118),(158,275,182,183,'',118),(159,281,181,184,'',118),(160,274,185,186,'',119),(161,282,187,188,'',119),(162,281,185,189,'',119),(163,277,187,190,'',119),(164,275,186,187,'',119),(165,273,185,187,'',119),(166,277,191,192,'',120),(167,281,193,194,'',120),(168,273,193,191,'',120),(169,276,195,196,'',120),(170,274,193,195,'',120),(171,282,191,197,'',120),(172,275,195,191,'',120),(173,282,198,199,'',121),(174,277,198,200,'',121),(175,273,201,198,'',121),(176,275,202,198,'',121),(177,274,201,202,'',121),(178,276,202,203,'',121),(179,281,201,204,'',121),(180,281,205,206,'',122),(181,282,207,208,'',122),(182,274,205,209,'',122),(183,275,209,207,'',122),(184,273,205,207,'',122),(185,277,207,210,'',122),(186,276,209,211,'',122),(187,276,212,213,'',123),(188,282,214,215,'',123),(189,273,216,214,'',123),(190,274,216,212,'',123),(191,275,212,214,'',123),(192,277,214,217,'',123),(193,281,216,218,'',123),(194,276,219,220,'',124),(195,282,221,222,'',124),(196,277,221,223,'',124),(197,274,224,219,'',124),(198,281,224,225,'',124),(199,275,219,221,'',124),(200,273,224,221,'',124),(201,281,226,227,'',125),(202,274,226,228,'',125),(203,282,229,230,'',125),(204,273,226,229,'',125),(205,275,228,229,'',125),(206,277,229,231,'',125),(207,276,228,232,'',125),(208,275,233,234,'',126),(209,272,235,236,'',126),(210,274,235,233,'',126),(211,281,235,237,'',126),(212,273,235,234,'',126),(213,282,234,238,'',126),(214,277,234,239,'',126),(215,274,240,241,'',127),(216,273,240,242,'',127),(217,275,241,242,'',127),(218,281,240,243,'',127),(219,276,241,244,'',127),(220,277,173,245,'',115),(221,281,172,246,'',115),(222,274,247,248,'',128),(223,273,247,249,'',128),(224,281,247,250,'',128),(225,276,248,251,'',128),(226,277,249,252,'',128),(227,275,248,249,'',128),(228,282,249,253,'',128),(250,274,275,276,'',132),(251,275,276,277,'',132),(252,273,275,277,'',132),(253,281,275,278,'',132),(254,282,277,279,'',132),(255,277,277,280,'',132),(256,276,276,281,'',132),(257,451,282,283,'',NULL),(258,455,283,284,'',NULL),(289,454,282,315,'',133),(290,457,315,316,'',133),(291,450,282,317,'',133),(292,272,318,319,'',134),(293,278,320,321,'',134),(294,270,318,320,'',134),(295,454,322,323,'',135),(296,457,323,324,'',135),(297,450,322,325,'',135),(298,453,325,326,'',135),(299,458,322,327,'',135),(300,270,328,329,'',136),(301,280,329,330,'',136),(302,275,331,332,'',136),(303,276,331,333,'',136),(304,278,329,334,'',136),(305,277,332,335,'',136),(306,274,328,331,'',136),(307,450,336,337,'',137),(308,454,336,338,'',137),(309,453,337,339,'',137),(310,457,338,340,'',137),(311,453,341,342,'',138),(312,450,343,341,'',138),(313,451,343,344,'',138),(314,455,344,345,'',138),(315,476,346,347,'',139),(316,475,348,349,'',NULL),(317,474,348,346,'',139),(318,458,350,351,'',140),(319,451,350,352,'',140),(320,453,353,354,'',140),(321,450,350,353,'',140),(322,455,352,355,'',140),(323,277,356,357,'',141),(324,270,358,359,'',141),(325,275,360,356,'',141),(326,460,359,361,'',141),(327,280,359,362,'',141),(328,274,358,360,'',141),(329,276,360,363,'',141),(330,453,364,365,'',NULL),(331,451,366,367,'',142),(332,450,366,364,'',142),(333,455,367,368,'',142),(334,476,369,370,'',143),(335,474,371,369,'',143),(336,458,366,372,'',142);
/*!40000 ALTER TABLE `t_queryedge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_querygraph`
--

DROP TABLE IF EXISTS `t_querygraph`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_querygraph` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idx` int(11) DEFAULT NULL,
  `x` int(11) DEFAULT NULL,
  `y` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `querydefinition_id` bigint(20) DEFAULT NULL,
  `graphtemplate_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_querygraph_querydefinition` (`querydefinition_id`),
  KEY `fk_querygraph_graphtemplate` (`graphtemplate_id`),
  CONSTRAINT `fk_querygraph_graphtemplate` FOREIGN KEY (`graphtemplate_id`) REFERENCES `t_graphtemplate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_querygraph_querydefinition` FOREIGN KEY (`querydefinition_id`) REFERENCES `t_querydefinition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_querygraph`
--

LOCK TABLES `t_querygraph` WRITE;
/*!40000 ALTER TABLE `t_querygraph` DISABLE KEYS */;
INSERT INTO `t_querygraph` VALUES (104,0,131,140,'pol',103,100),(105,1,381,161,'GDA',103,1),(114,1,155,130,'Ensembl Human Genes, Transcripts and Exons_1',108,128),(115,0,306,84,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(116,0,299,145,'Ensembl Human Genes, Transcripts and Exons_0',110,128),(118,0,241,146,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(119,0,161,209,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(120,0,414,180,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(121,0,210,154,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(122,0,391,190,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(123,0,356,152,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(124,0,445,139,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(125,0,355,234,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(126,0,239,132,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(127,0,290,194,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(128,0,489,252,'Ensembl Human Genes, Transcripts and Exons_0',NULL,128),(132,0,408,229,'Ensembl Human Genes, Transcripts and Exons_0',115,128),(133,0,263,147,'Simplified GDA_0',116,157),(134,0,153,104,'Ensembl Human Genes, Transcripts and Exons_0',117,128),(135,1,349,70,'Simplified GDA_1',117,157),(136,0,116,94,'Ensembl Human Genes, Transcripts and Exons_0',118,128),(137,1,386,177,'Simplified GDA_1',118,157),(138,0,362,137,'Simplified GDA_0',119,157),(139,0,201,150,'MeSH Core_0',120,162),(140,1,377,79,'Simplified GDA_1',120,157),(141,0,381,54,'Ensembl Human Genes, Transcripts and Exons_0',121,128),(142,2,288,106,'Simplified GDA_2',121,157),(143,1,183,165,'MeSH Core_1',121,162);
/*!40000 ALTER TABLE `t_querygraph` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_querynode`
--

DROP TABLE IF EXISTS `t_querynode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_querynode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nodetemplate_id` bigint(20) DEFAULT NULL,
  `entity_values` text,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_querynode_nodetemplate` (`nodetemplate_id`),
  CONSTRAINT `fk_querynode_nodetemplate` FOREIGN KEY (`nodetemplate_id`) REFERENCES `t_nodetemplate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=373 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_querynode`
--

LOCK TABLES `t_querynode` WRITE;
/*!40000 ALTER TABLE `t_querynode` DISABLE KEYS */;
INSERT INTO `t_querynode` VALUES (129,99,'',1),(130,103,'',2),(131,114,'',3),(135,99,'',1),(136,103,'',2),(137,114,'',3),(138,3,'',1),(139,6,'',2),(163,312,'',3),(164,311,'',2),(165,314,'',5),(166,307,'',1),(167,313,'',4),(168,318,'',6),(169,307,'',6),(170,318,'',7),(171,311,'',8),(172,307,'',1),(173,311,'',2),(174,312,'',3),(175,312,'',2),(176,311,'',3),(177,307,'',1),(181,307,'',1),(182,312,'',3),(183,311,'',2),(184,318,'',4),(185,307,'',1),(186,312,'',3),(187,311,'',2),(188,319,'',6),(189,318,'',4),(190,314,'',5),(191,311,'',3),(192,314,'',7),(193,307,'',1),(194,318,'',2),(195,312,'',4),(196,313,'',5),(197,319,'',6),(198,311,'',4),(199,319,'',7),(200,314,'',6),(201,307,'',1),(202,312,'',3),(203,313,'',5),(204,318,'',2),(205,307,'',1),(206,318,'',2),(207,311,'',4),(208,319,'',6),(209,312,'',3),(210,314,'',7),(211,313,'',5),(212,312,'',4),(213,313,'',5),(214,311,'',3),(215,319,'',6),(216,307,'',1),(217,314,'',7),(218,318,'',2),(219,312,'',4),(220,313,'',5),(221,311,'',3),(222,319,'',6),(223,314,'',7),(224,307,'',1),(225,318,'',2),(226,307,'',1),(227,318,'',2),(228,312,'',4),(229,311,'',3),(230,319,'',6),(231,314,'',7),(232,313,'',5),(233,312,'',5),(234,311,'',4),(235,307,'',1),(236,310,'',2),(237,318,'',3),(238,319,'',6),(239,314,'',7),(240,307,'',1),(241,312,'',4),(242,311,'',3),(243,318,'',2),(244,313,'',5),(245,314,'',5),(246,318,'',4),(247,307,'',1),(248,312,'',4),(249,311,'',3),(250,318,'',2),(251,313,'',5),(252,314,'',7),(253,319,'',6),(275,307,'',1),(276,312,'',4),(277,311,'',3),(278,318,'',2),(279,319,'',7),(280,314,'',6),(281,313,'',5),(282,324,'',1),(283,326,'',2),(284,330,'',3),(315,333,'',3),(316,334,'',4),(317,325,'',5),(318,307,'',3),(319,310,'',4),(320,308,'',1),(321,315,'',2),(322,324,'',3),(323,333,'',4),(324,334,'',5),(325,325,'',1),(326,328,'',2),(327,335,'',6),(328,307,'',4),(329,308,'',1),(330,317,'',2),(331,312,'',5),(332,311,'',6),(333,313,'',7),(334,315,'',3),(335,314,'',8),(336,324,'',1),(337,325,'',2),(338,333,'',4),(339,328,'',3),(340,334,'',5),(341,325,'',4),(342,328,'',5),(343,324,'',3),(344,326,'',1),(345,330,'',2),(346,360,'',3),(347,364,'',4),(348,361,'',1),(349,362,'',2),(350,324,'',3),(351,335,'',4),(352,326,'',5),(353,325,'',1),(354,328,'',2),(355,330,'',6),(356,311,'',7),(357,314,'',8),(358,307,'',4),(359,308,'',1),(360,312,'',5),(361,337,'',3),(362,317,'',2),(363,313,'',6),(364,325,'',4),(365,328,'',5),(366,324,'',3),(367,326,'',1),(368,330,'',2),(369,360,'',2),(370,364,'',3),(371,361,'',1),(372,335,'',5);
/*!40000 ALTER TABLE `t_querynode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_rawdatafile`
--

DROP TABLE IF EXISTS `t_rawdatafile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_rawdatafile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_path` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `track_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_1sr430mljt676vgn8mc98gvf4` (`track_id`),
  CONSTRAINT `fk_1sr430mljt676vgn8mc98gvf4` FOREIGN KEY (`track_id`) REFERENCES `t_track` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=229381 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_rawdatafile`
--

LOCK TABLES `t_rawdatafile` WRITE;
/*!40000 ALTER TABLE `t_rawdatafile` DISABLE KEYS */;
INSERT INTO `t_rawdatafile` VALUES (98304,'/opt/boinq/files/5f7/25287-639d-42bc-866e-7c6d5ff7484b/lamina.bed',3,102),(98305,'/opt/boinq/files/7ef/1d33d-7689-46f9-a756-7f60f42b694c/lamina.bed',2,102),(196608,'/data/brol',0,109),(196609,'brollo',0,109),(196610,'kaka',0,109),(196611,'pipi',0,109),(196612,'prot',0,109),(196613,'em',0,109),(196614,'pipi',0,109),(196615,'bimbo',0,109),(229376,'klutser',0,109),(229377,'kaka',0,109),(229378,'full path to file on server',0,109),(229379,'full path to file on server',0,109),(229380,'kevin de hond',0,109);
/*!40000 ALTER TABLE `t_rawdatafile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_referencemapentry`
--

DROP TABLE IF EXISTS `t_referencemapentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_referencemapentry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `graphtemplate_id` bigint(20) DEFAULT NULL,
  `boinq_reference_iri` varchar(255) DEFAULT NULL,
  `remote_reference_iri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_referencemapentry_graphtemplate` (`graphtemplate_id`),
  CONSTRAINT `fk_referencemapentry_graphtemplate` FOREIGN KEY (`graphtemplate_id`) REFERENCES `t_graphtemplate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_referencemapentry`
--

LOCK TABLES `t_referencemapentry` WRITE;
/*!40000 ALTER TABLE `t_referencemapentry` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_referencemapentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_regionofinterest`
--

DROP TABLE IF EXISTS `t_regionofinterest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_regionofinterest` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sample_date_attribute` date DEFAULT NULL,
  `sample_text_attribute` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_regionofinterest`
--

LOCK TABLES `t_regionofinterest` WRITE;
/*!40000 ALTER TABLE `t_regionofinterest` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_regionofinterest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_stringcollections`
--

DROP TABLE IF EXISTS `t_stringcollections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_stringcollections` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nodetemplate_id` bigint(20) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `idx` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stringcollections_nodetemplate` (`nodetemplate_id`),
  CONSTRAINT `fk_stringcollections_nodetemplate` FOREIGN KEY (`nodetemplate_id`) REFERENCES `t_nodetemplate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_stringcollections`
--

LOCK TABLES `t_stringcollections` WRITE;
/*!40000 ALTER TABLE `t_stringcollections` DISABLE KEYS */;
INSERT INTO `t_stringcollections` VALUES (1,196,'http://purl.obolibrary.org/obo/SO_0001060',0),(2,196,'http://purl.obolibrary.org/obo/SO_0000110',1),(3,211,'http://purl.obolibrary.org/obo/SO_0001060',0),(4,211,'http://purl.obolibrary.org/obo/SO_0000400',1),(5,306,'alpha',0),(6,306,'beta',1);
/*!40000 ALTER TABLE `t_stringcollections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_track`
--

DROP TABLE IF EXISTS `t_track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_track` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `graph_name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `datasource_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `species` varchar(255) DEFAULT NULL,
  `assembly` varchar(255) DEFAULT NULL,
  `contig_prefix` varchar(255) DEFAULT NULL,
  `entry_count` bigint(20) DEFAULT NULL,
  `feature_count` bigint(20) DEFAULT NULL,
  `triple_count` bigint(20) DEFAULT NULL,
  `graphtemplate_id` bigint(20) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `fk_79ifc99gxvpn74rs90bl9oroj` (`datasource_id`),
  CONSTRAINT `fk_79ifc99gxvpn74rs90bl9oroj` FOREIGN KEY (`datasource_id`) REFERENCES `t_datasource` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_track`
--

LOCK TABLES `t_track` WRITE;
/*!40000 ALTER TABLE `t_track` DISABLE KEYS */;
INSERT INTO `t_track` VALUES (3,'http://sparql.uniprot.org/uniprot/',0,3,'Uniprot Protein','http://purl.obolibrary.org/obo/SO_0001410',NULL,NULL,NULL,NULL,0,0,0,NULL,NULL),(4,'http://rdf.disgenet.org',0,4,'GDA','generic',NULL,'Homo sapiens',NULL,NULL,0,0,0,1,'Simplified view of Gene Disease Associations'),(102,'http://www.boinq.org/iri/graph/local#1_102',3,1,'TestNew','feature','BED','Homo sapiens','GRCh38','chr',0,0,0,106,NULL),(109,'http://www.boinq.org/iri/graph/local#1_109',0,1,'dfg','feature','BED','Homo sapiens','GRCh38',NULL,0,0,0,118,NULL),(114,'<http://rdf.ebi.ac.uk/dataset/homo_sapiens>',0,2,'Ensembl Human','generic',NULL,NULL,NULL,NULL,0,0,0,126,NULL),(116,'http://rdf.ebi.ac.uk/dataset/homo_sapiens',0,2,'Ensembl Human Genes, Transcripts and Exons','generic',NULL,'Homo sapiens','GRCh38',NULL,0,0,0,128,'Genes, Transcripts and Exons from ENSEMBL Human endpoint'),(117,NULL,0,NULL,'Execution Tue Dec 26 22:09:12 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,130,'Auto generated track resulting from querydefinition ENST00000380152'),(118,NULL,0,NULL,'Execution Tue Dec 26 22:20:59 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,131,'Auto generated track resulting from querydefinition ENST00000380152'),(119,NULL,0,NULL,'Execution Tue Dec 26 22:28:27 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,132,'Auto generated track resulting from querydefinition ENST00000380152'),(120,NULL,0,NULL,'Execution Wed Dec 27 08:45:43 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,133,'Auto generated track resulting from querydefinition ENST00000380152'),(121,NULL,0,NULL,'Execution Wed Dec 27 08:49:33 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,134,'Auto generated track resulting from querydefinition ENST00000380152'),(122,NULL,0,NULL,'Execution Wed Dec 27 09:02:30 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,135,'Auto generated track resulting from querydefinition ENST00000380152'),(123,NULL,0,NULL,'Execution Wed Dec 27 09:43:53 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,136,'Auto generated track resulting from querydefinition ENST00000380152'),(124,NULL,0,NULL,'Execution Fri Dec 29 12:16:11 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,137,'Auto generated track resulting from querydefinition ENST00000380152'),(125,NULL,0,NULL,'Execution Fri Dec 29 12:43:07 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,138,'Auto generated track resulting from querydefinition ENST00000380152'),(126,NULL,0,NULL,'Execution Fri Dec 29 12:46:32 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,139,'Auto generated track resulting from querydefinition ENST00000380152'),(127,NULL,0,NULL,'Execution Fri Dec 29 12:46:32 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,140,'Auto generated track resulting from querydefinition ENST00000380152'),(128,NULL,0,NULL,'Execution Fri Dec 29 13:01:46 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,141,'Auto generated track resulting from querydefinition ENST00000380152'),(129,NULL,0,NULL,'Execution Fri Dec 29 13:25:17 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,142,'Auto generated track resulting from querydefinition ENST00000380152'),(130,NULL,0,NULL,'Execution Fri Dec 29 13:25:17 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,143,'Auto generated track resulting from querydefinition ENST00000380152'),(131,NULL,0,NULL,'Execution Fri Dec 29 13:28:06 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,144,'Auto generated track resulting from querydefinition ENST00000380152'),(132,NULL,0,NULL,'Execution Fri Dec 29 13:34:03 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,145,'Auto generated track resulting from querydefinition ENST00000380152'),(133,NULL,0,NULL,'Execution Fri Dec 29 13:44:58 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,146,'Auto generated track resulting from querydefinition ENST00000380152'),(134,NULL,0,NULL,'Execution Fri Dec 29 13:55:47 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,147,'Auto generated track resulting from querydefinition Transcript retrieval'),(135,NULL,0,NULL,'Execution Fri Dec 29 14:01:30 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,148,'Auto generated track resulting from querydefinition Transcript retrieval'),(136,NULL,0,NULL,'Execution Fri Dec 29 14:10:53 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,149,'Auto generated track resulting from querydefinition Transcript retrieval'),(137,NULL,0,NULL,'Execution Fri Dec 29 14:13:29 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,150,'Auto generated track resulting from querydefinition Transcript retrieval'),(138,NULL,0,NULL,'Execution Fri Dec 29 14:24:44 CET 2017',NULL,NULL,NULL,NULL,NULL,0,0,0,151,'Auto generated track resulting from querydefinition Transcript retrieval'),(139,NULL,0,NULL,'Execution Tue Jan 02 10:34:31 CET 2018',NULL,NULL,NULL,NULL,NULL,0,0,0,152,'Auto generated track resulting from querydefinition Transcript retrieval'),(140,NULL,0,NULL,'Execution Tue Jan 02 13:19:04 CET 2018',NULL,NULL,NULL,NULL,NULL,0,0,0,153,'Auto generated track resulting from querydefinition ENST00000380152'),(141,NULL,0,NULL,'Execution Tue Jan 02 13:29:07 CET 2018',NULL,NULL,NULL,NULL,NULL,0,0,0,154,'Auto generated track resulting from querydefinition ENST00000380152'),(142,NULL,0,NULL,'Execution Wed Jan 03 23:23:54 CET 2018',NULL,NULL,NULL,NULL,NULL,0,0,0,155,'Auto generated track resulting from querydefinition ENST00000380152'),(143,NULL,0,NULL,'Execution Wed Jan 03 23:29:04 CET 2018',NULL,NULL,NULL,NULL,NULL,0,0,0,156,'Auto generated track resulting from querydefinition ENST00000380152'),(144,'http://rdf.disgenet.org',0,4,'Simplified GDA','generic',NULL,NULL,NULL,NULL,0,0,0,157,'Gene Disease Associations from DisGeNet, limited to the most simple form: linking genes and diseases/phenotypes by ID.'),(147,'http://id.nlm.nih.gov/mesh/',0,5,'MeSH Core','generic',NULL,NULL,NULL,NULL,0,0,0,162,'Core concepts of the Medical Subject Headings ontology'),(148,'http://www.boinq.org/iri/graph/local#1_148',0,1,'RNASEQ_HCT116_WT','feature','BED','Homo sapiens','GRCh38',NULL,0,0,0,166,NULL);
/*!40000 ALTER TABLE `t_track` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_track_t_rawdatafile`
--

DROP TABLE IF EXISTS `t_track_t_rawdatafile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_track_t_rawdatafile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `T_TRACK_id` bigint(20) DEFAULT NULL,
  `rawDataFiles_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UC_T_TRACK_T_RAWDATAFILE_RAWDATAFILES_ID` (`rawDataFiles_id`),
  KEY `fk_jrfqf4yqyf2c8v7khfbk9ar8a` (`T_TRACK_id`),
  CONSTRAINT `fk_436vll7708q8p6oqjt16a0ah` FOREIGN KEY (`rawDataFiles_id`) REFERENCES `t_rawdatafile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_jrfqf4yqyf2c8v7khfbk9ar8a` FOREIGN KEY (`T_TRACK_id`) REFERENCES `t_track` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_track_t_rawdatafile`
--

LOCK TABLES `t_track_t_rawdatafile` WRITE;
/*!40000 ALTER TABLE `t_track_t_rawdatafile` DISABLE KEYS */;
INSERT INTO `t_track_t_rawdatafile` VALUES (103,102,98305),(126,109,229380);
/*!40000 ALTER TABLE `t_track_t_rawdatafile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'boinq'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-09 15:00:48
