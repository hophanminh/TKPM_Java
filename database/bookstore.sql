CREATE DATABASE  IF NOT EXISTS `bookstore` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bookstore`;
-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: bookstore
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `idBill` int NOT NULL AUTO_INCREMENT,
  `dateBill` datetime(6) DEFAULT NULL,
  `paidBill` float DEFAULT NULL,
  `totalBill` float DEFAULT NULL,
  `totalDiscountBill` float DEFAULT NULL,
  `idCustomer` int DEFAULT NULL,
  `id` int DEFAULT NULL,
  `idStore` int DEFAULT NULL,
  PRIMARY KEY (`idBill`),
  KEY `FK9hdkbdnw4uk3krgqdd9vgr258` (`idCustomer`),
  KEY `FKeu5n7rn7f1tpng0ccxjs48j7q` (`id`),
  KEY `FKjuxnia176joyr3rmb7j0jdyvi` (`idStore`),
  CONSTRAINT `FK9hdkbdnw4uk3krgqdd9vgr258` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`idCustomer`),
  CONSTRAINT `FKeu5n7rn7f1tpng0ccxjs48j7q` FOREIGN KEY (`id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FKjuxnia176joyr3rmb7j0jdyvi` FOREIGN KEY (`idStore`) REFERENCES `store` (`idStore`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (6,'2020-08-06 22:33:40.000000',100,100,0,NULL,1,1),(7,'2020-08-06 22:41:09.000000',200,200,0,NULL,1,1),(8,'2020-08-06 22:41:36.000000',300,300,0,NULL,1,1),(9,'2020-09-06 22:41:36.000000',350,350,0,NULL,1,1),(10,'2020-09-06 22:41:36.000000',20,20,0,NULL,1,1),(11,'2020-09-06 22:41:36.000000',5000,5000,0,NULL,1,1),(12,'2020-07-06 22:41:36.000000',999,999,0,NULL,1,1),(13,'2019-07-06 22:41:36.000000',356,356,0,NULL,1,1),(14,'2020-06-06 22:41:36.000000',350,350,0,NULL,1,1),(15,'2019-10-06 22:41:36.000000',100,100,0,NULL,1,1),(16,'2020-08-21 15:09:54.000000',4095,4095,0,NULL,1,1),(17,'2020-08-21 15:20:26.000000',8675,8675,0,NULL,1,1),(18,'2020-08-21 15:25:15.000000',143295,143295,0,NULL,1,1),(19,'2020-08-21 15:29:25.000000',135,135,0,NULL,1,1),(20,'2020-08-21 15:34:16.000000',85,85,0,NULL,1,1),(21,'2020-08-21 15:34:42.000000',130,130,0,NULL,1,1),(22,'2020-08-21 15:51:30.000000',10,10,0,NULL,1,1),(23,'2020-08-21 15:53:34.000000',100,100,0,NULL,1,1),(24,'2020-08-21 15:54:37.000000',10,10,0,NULL,1,1),(25,'2020-08-21 16:05:20.000000',10,10,0,NULL,1,1),(26,'2020-08-21 16:05:48.000000',0,0,0,NULL,1,1),(27,'2020-08-21 16:05:49.000000',0,0,0,NULL,1,1),(28,'2020-08-21 16:07:54.000000',0,0,0,NULL,1,1),(29,'2020-08-21 22:29:26.000000',10,10,0,1,1,1);
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_item`
--

DROP TABLE IF EXISTS `bill_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill_item` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `count` int DEFAULT NULL,
  `discount` float DEFAULT NULL,
  `total` float DEFAULT NULL,
  `bill_ID` int DEFAULT NULL,
  `item_ID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKq629fl2y8m275qnmwca9uo05i` (`bill_ID`),
  KEY `FKqwjtl51kha8hk7ajmdw9q10g2` (`item_ID`),
  CONSTRAINT `FKq629fl2y8m275qnmwca9uo05i` FOREIGN KEY (`bill_ID`) REFERENCES `bill` (`idBill`),
  CONSTRAINT `FKqwjtl51kha8hk7ajmdw9q10g2` FOREIGN KEY (`item_ID`) REFERENCES `item` (`idItem`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_item`
--

LOCK TABLES `bill_item` WRITE;
/*!40000 ALTER TABLE `bill_item` DISABLE KEYS */;
INSERT INTO `bill_item` VALUES (10,1,0,1000,6,6),(11,1,0,1000,6,6),(12,1,0,0,6,3),(13,1,0,0,6,2),(14,1,0,5,6,5),(15,1,0,0,7,2),(16,1,0,0,7,2),(17,1,0,0,7,3),(18,1,0,0,8,3),(20,1,0,0,8,3),(21,6,0,600,16,3),(22,3,0,300,16,3),(23,4,0,40,16,2),(24,5,0,2500,16,9),(25,6,0,600,16,3),(26,11,0,55,16,5),(27,12,0,120,17,2),(28,2,0,20,17,2),(29,3,0,1500,17,9),(30,5,0,5000,17,6),(31,12,0,1200,17,3),(32,3,0,300,17,3),(33,7,0,35,17,5),(34,1,0,500,17,9),(35,15,0,15000,18,6),(36,3,0,3000,18,6),(37,7,0,35,18,5),(38,9,0,4500,18,9),(39,5,0,2500,18,9),(40,6,0,6000,18,6),(41,22,0,2200,18,3),(42,11,0,110000,18,8),(43,6,0,60,18,2),(44,3,0,30,19,2),(45,5,0,25,19,5),(46,8,0,80,19,2),(47,2,0,20,20,2),(48,5,0,25,20,5),(49,4,0,40,20,2),(50,25,0,125,21,5),(51,1,0,5,21,5),(52,1,0,10,22,2),(53,1,0,100,23,3),(54,1,0,10,24,2),(55,1,0,10,25,2),(56,1,0,10,29,2);
/*!40000 ALTER TABLE `bill_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `authorBook` varchar(255) DEFAULT NULL,
  `descriptionBook` varchar(255) DEFAULT NULL,
  `pulisherBook` varchar(255) DEFAULT NULL,
  `yearBook` int DEFAULT NULL,
  `idItem` int NOT NULL,
  PRIMARY KEY (`idItem`),
  CONSTRAINT `FK7gqi8kpljj6v5s1v2u5q17pa8` FOREIGN KEY (`idItem`) REFERENCES `item` (`idItem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('','','',0,2),('1','1','1',0,3),('','','',0,5),('2','2','2',0,7),('abc','abcccccccc','abc',0,9),('','','',0,10),('','','',0,15);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `book_has_genre`
--

DROP TABLE IF EXISTS `book_has_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book_has_genre` (
  `book_idItem` int NOT NULL,
  `genre_idGenre` int NOT NULL,
  PRIMARY KEY (`book_idItem`,`genre_idGenre`),
  KEY `FK4m717uics1h8x06apch3yyk17` (`genre_idGenre`),
  CONSTRAINT `FK4m717uics1h8x06apch3yyk17` FOREIGN KEY (`genre_idGenre`) REFERENCES `genre` (`idGenre`),
  CONSTRAINT `FKb6wr0bhtuosjrkegrb46213vn` FOREIGN KEY (`book_idItem`) REFERENCES `book` (`idItem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book_has_genre`
--

LOCK TABLES `book_has_genre` WRITE;
/*!40000 ALTER TABLE `book_has_genre` DISABLE KEYS */;
INSERT INTO `book_has_genre` VALUES (9,1),(3,3),(10,4),(3,6),(9,6),(15,6),(3,7),(9,7),(10,8),(15,11),(15,12);
/*!40000 ALTER TABLE `book_has_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `idCompany` int NOT NULL AUTO_INCREMENT,
  `nameCompany` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `idCustomer` int NOT NULL AUTO_INCREMENT,
  `dobCustomer` datetime(6) DEFAULT NULL,
  `emailCustomer` varchar(255) DEFAULT NULL,
  `identifyIDCustomer` varchar(255) DEFAULT NULL,
  `nameCustomer` varchar(255) DEFAULT NULL,
  `idStore` int NOT NULL,
  PRIMARY KEY (`idCustomer`),
  KEY `FKp853lk617jabiajx5v92p6dk` (`idStore`),
  CONSTRAINT `FKp853lk617jabiajx5v92p6dk` FOREIGN KEY (`idStore`) REFERENCES `store` (`idStore`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'2020-08-02 00:00:00.000000','email1@gmail.com','1234567890','cus',1),(2,'2020-07-29 00:00:00.000000','a@a.con','1234567890','aa',1);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `position` int DEFAULT NULL,
  `salary` int DEFAULT NULL,
  `startDate` datetime(6) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `idStorage` int DEFAULT NULL,
  `idStore` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhyl3a2dj2m2v2bxwoilgnk45f` (`idStorage`),
  KEY `FKeuw8bt43qrdcltudeggey9riv` (`idStore`),
  CONSTRAINT `FKeuw8bt43qrdcltudeggey9riv` FOREIGN KEY (`idStore`) REFERENCES `store` (`idStore`),
  CONSTRAINT `FKhyl3a2dj2m2v2bxwoilgnk45f` FOREIGN KEY (`idStorage`) REFERENCES `storage` (`idStorage`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'address','admin','$2a$10$ghh3q7QxjwcJC9kZV0BwreuVvMHg0UKU8go/z4VatO0Z6ojp.Vdsm','1234567890',2,1,'2020-08-01 00:00:00.000000',1,'admin',NULL,1),(2,'address','admin','$2a$10$UrJvxtdJuW1N/kKqiLmInOhXDZCMdsbtuhN9oELpU0x9E.H8DuWie','1234567890',1,2,'2020-08-01 00:00:00.000000',1,'admin2',NULL,1),(3,'address','user','$2a$10$ghh3q7QxjwcJC9kZV0BwreuVvMHg0UKU8go/z4VatO0Z6ojp.Vdsm','1234567890',0,123,'2020-08-01 00:00:00.000000',1,'user',NULL,1);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genre` (
  `idGenre` int NOT NULL AUTO_INCREMENT,
  `nameGenre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idGenre`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (1,'genre1'),(2,'genre2'),(3,'genre3'),(4,'genre4'),(5,'genre5'),(6,'genre6'),(7,'genre7'),(8,'genre8'),(9,'genre9'),(10,'genre10'),(11,'genre11'),(12,'genre12');
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `idItem` int NOT NULL AUTO_INCREMENT,
  `costItem` float DEFAULT NULL,
  `nameItem` varchar(255) DEFAULT NULL,
  `priceItem` float DEFAULT NULL,
  PRIMARY KEY (`idItem`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,0,'item',0),(2,5,'item2',10),(3,50,'item3',100),(5,5,'abcdef',5),(6,5000,'abc',1000),(7,0,'1',0),(8,8000,'abc',10000),(9,250,'book',500),(10,1,'book',12),(11,70,'stored item',100),(14,0,'abc',0),(15,10,'book',20);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_storage`
--

DROP TABLE IF EXISTS `item_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_storage` (
  `item_ID` int NOT NULL,
  `storage_ID` int NOT NULL,
  `count` int DEFAULT NULL,
  PRIMARY KEY (`item_ID`,`storage_ID`),
  KEY `FK32pww9pl6per5t8g120ns8msw` (`storage_ID`),
  CONSTRAINT `FK32pww9pl6per5t8g120ns8msw` FOREIGN KEY (`storage_ID`) REFERENCES `storage` (`idStorage`),
  CONSTRAINT `FK8v0aaudta3ax90nnxj39sefng` FOREIGN KEY (`item_ID`) REFERENCES `item` (`idItem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_storage`
--

LOCK TABLES `item_storage` WRITE;
/*!40000 ALTER TABLE `item_storage` DISABLE KEYS */;
INSERT INTO `item_storage` VALUES (1,2,58),(10,3,1),(11,1,159),(11,2,0),(11,3,21);
/*!40000 ALTER TABLE `item_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_store`
--

DROP TABLE IF EXISTS `item_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_store` (
  `item_ID` int NOT NULL,
  `store_ID` int NOT NULL,
  `count` int DEFAULT NULL,
  PRIMARY KEY (`item_ID`,`store_ID`),
  KEY `FKcyqxd0lu7wbr6shn2s0j8rplq` (`store_ID`),
  CONSTRAINT `FKc3to4aawxur5vt9197ohvkrra` FOREIGN KEY (`item_ID`) REFERENCES `item` (`idItem`),
  CONSTRAINT `FKcyqxd0lu7wbr6shn2s0j8rplq` FOREIGN KEY (`store_ID`) REFERENCES `store` (`idStore`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_store`
--

LOCK TABLES `item_store` WRITE;
/*!40000 ALTER TABLE `item_store` DISABLE KEYS */;
INSERT INTO `item_store` VALUES (1,1,2),(1,2,51),(2,1,2),(3,2,7),(3,3,5),(3,5,44),(5,1,26),(5,3,10),(6,1,222),(7,3,1),(8,2,9),(8,3,1),(9,1,120),(11,2,0),(11,5,10),(14,1,1),(15,1,50);
/*!40000 ALTER TABLE `item_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storage`
--

DROP TABLE IF EXISTS `storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage` (
  `idStorage` int NOT NULL AUTO_INCREMENT,
  `addressStorage` varchar(255) DEFAULT NULL,
  `nameStorage` varchar(255) DEFAULT NULL,
  `idCompany` int DEFAULT NULL,
  PRIMARY KEY (`idStorage`),
  KEY `FK5rl56air366vk8dn1yf70gve9` (`idCompany`),
  CONSTRAINT `FK5rl56air366vk8dn1yf70gve9` FOREIGN KEY (`idCompany`) REFERENCES `company` (`idCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage`
--

LOCK TABLES `storage` WRITE;
/*!40000 ALTER TABLE `storage` DISABLE KEYS */;
INSERT INTO `storage` VALUES (1,'address1','storage1',NULL),(2,'address2','storage2',NULL),(3,'address3','storage3',NULL),(4,'address4','storage4',NULL);
/*!40000 ALTER TABLE `storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store` (
  `idStore` int NOT NULL AUTO_INCREMENT,
  `addressStore` varchar(255) DEFAULT NULL,
  `nameStore` varchar(255) DEFAULT NULL,
  `outcomeStore` int DEFAULT NULL,
  `revenueStore` int DEFAULT NULL,
  `idCompany` int DEFAULT NULL,
  PRIMARY KEY (`idStore`),
  KEY `FKb9s2x22rcoag9h75tiasn9i3g` (`idCompany`),
  CONSTRAINT `FKb9s2x22rcoag9h75tiasn9i3g` FOREIGN KEY (`idCompany`) REFERENCES `company` (`idCompany`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (1,'address1','store1',0,0,NULL),(2,'address2','store2',0,0,NULL),(3,'address3','store3',0,0,NULL),(5,'address5','store5',0,0,NULL),(6,'address6','store6',0,0,NULL),(7,'address7','store7',0,0,NULL);
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_has_storage`
--

DROP TABLE IF EXISTS `store_has_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_has_storage` (
  `store_id` int NOT NULL,
  `storage_id` int NOT NULL,
  PRIMARY KEY (`store_id`,`storage_id`),
  KEY `FKg6m1xxnbu8ljhhi31k63qc1gh` (`storage_id`),
  CONSTRAINT `FKf16t9p7sn93pc0es40i4fvg8j` FOREIGN KEY (`store_id`) REFERENCES `store` (`idStore`),
  CONSTRAINT `FKg6m1xxnbu8ljhhi31k63qc1gh` FOREIGN KEY (`storage_id`) REFERENCES `storage` (`idStorage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_has_storage`
--

LOCK TABLES `store_has_storage` WRITE;
/*!40000 ALTER TABLE `store_has_storage` DISABLE KEYS */;
INSERT INTO `store_has_storage` VALUES (1,1),(2,1),(5,1),(1,2),(2,2),(5,2),(5,3),(1,4),(5,4);
/*!40000 ALTER TABLE `store_has_storage` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-22 12:22:20
