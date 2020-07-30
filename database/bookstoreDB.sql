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
  `madeBill` int DEFAULT NULL,
  `noteBill` varchar(255) DEFAULT NULL,
  `totalBill` int DEFAULT NULL,
  `idCustomer` int DEFAULT NULL,
  PRIMARY KEY (`idBill`),
  KEY `FK9hdkbdnw4uk3krgqdd9vgr258` (`idCustomer`),
  CONSTRAINT `FK9hdkbdnw4uk3krgqdd9vgr258` FOREIGN KEY (`idCustomer`) REFERENCES `customer` (`idCustomer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
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
INSERT INTO `book` VALUES ('1','','',0,1),('2','','',0,2),('1','','',0,3),('2','','',0,4),('5','','',0,5),('7','','',0,6),('8','','',0,7),('9','','',0,8),('9','','',0,9);
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
INSERT INTO `book_has_genre` VALUES (7,1),(9,3),(8,4),(8,5),(8,6),(9,6),(9,7),(7,8);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount` (
  `idDiscount` int NOT NULL AUTO_INCREMENT,
  `discount` int DEFAULT NULL,
  `endDate` datetime(6) DEFAULT NULL,
  `startDate` datetime(6) DEFAULT NULL,
  `idStore` int NOT NULL,
  PRIMARY KEY (`idDiscount`),
  KEY `FKtf1d2t6oyvcnb2axeyt08plop` (`idStore`),
  CONSTRAINT `FKtf1d2t6oyvcnb2axeyt08plop` FOREIGN KEY (`idStore`) REFERENCES `store` (`idStore`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discount`
--

LOCK TABLES `discount` WRITE;
/*!40000 ALTER TABLE `discount` DISABLE KEYS */;
/*!40000 ALTER TABLE `discount` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'abc','minh','admin','100',1,100,'2001-01-01 00:00:00.000000',1,'admin',NULL,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (1,'Kinh dị'),(2,'Trinh thám'),(3,'Hài kịch'),(4,'Ngụ ngôn'),(5,'Viễn tưởng'),(6,'Tình cảm'),(7,'Châm biếm'),(8,'Khoa học');
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
  `quantityItem` int DEFAULT NULL,
  PRIMARY KEY (`idItem`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,0,'1',0,1),(2,0,'2',0,1),(3,0,'1',0,1),(4,0,'2',0,1),(5,0,'5',0,1),(6,0,'7',0,1),(7,0,'8',0,1),(8,0,'9',0,1),(9,0,'9',0,1);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
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
  `idCompany` int NOT NULL,
  PRIMARY KEY (`idStorage`),
  KEY `FK5rl56air366vk8dn1yf70gve9` (`idCompany`),
  CONSTRAINT `FK5rl56air366vk8dn1yf70gve9` FOREIGN KEY (`idCompany`) REFERENCES `company` (`idCompany`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage`
--

LOCK TABLES `storage` WRITE;
/*!40000 ALTER TABLE `storage` DISABLE KEYS */;
/*!40000 ALTER TABLE `storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storage_has_item`
--

DROP TABLE IF EXISTS `storage_has_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage_has_item` (
  `item_idItem` int NOT NULL,
  `storage_idStore` int NOT NULL,
  PRIMARY KEY (`item_idItem`,`storage_idStore`),
  KEY `FKnfj8jlfn0x98mie0fdn73g2si` (`storage_idStore`),
  CONSTRAINT `FK12g3rrl0n95yfg7d4nbhxejah` FOREIGN KEY (`item_idItem`) REFERENCES `item` (`idItem`),
  CONSTRAINT `FKnfj8jlfn0x98mie0fdn73g2si` FOREIGN KEY (`storage_idStore`) REFERENCES `storage` (`idStorage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage_has_item`
--

LOCK TABLES `storage_has_item` WRITE;
/*!40000 ALTER TABLE `storage_has_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `storage_has_item` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (1,'address1','store1',0,0,NULL),(2,'address2','store2',0,0,NULL);
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_has_item`
--

DROP TABLE IF EXISTS `store_has_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_has_item` (
  `item_idItem` int NOT NULL,
  `store_idStore` int NOT NULL,
  PRIMARY KEY (`item_idItem`,`store_idStore`),
  KEY `FKsokbxswlnkhm1mc459403bkqx` (`store_idStore`),
  CONSTRAINT `FKeuyvc2c0g2r6p9iyo0hhvxu64` FOREIGN KEY (`item_idItem`) REFERENCES `item` (`idItem`),
  CONSTRAINT `FKsokbxswlnkhm1mc459403bkqx` FOREIGN KEY (`store_idStore`) REFERENCES `store` (`idStore`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_has_item`
--

LOCK TABLES `store_has_item` WRITE;
/*!40000 ALTER TABLE `store_has_item` DISABLE KEYS */;
INSERT INTO `store_has_item` VALUES (1,1),(2,1),(7,1),(8,1),(9,1),(3,2),(4,2),(5,2),(6,2);
/*!40000 ALTER TABLE `store_has_item` ENABLE KEYS */;
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
  KEY `FK49wibsf8v7jyl6q6bow6393x0` (`storage_id`),
  CONSTRAINT `FK49wibsf8v7jyl6q6bow6393x0` FOREIGN KEY (`storage_id`) REFERENCES `store` (`idStore`),
  CONSTRAINT `FKf16t9p7sn93pc0es40i4fvg8j` FOREIGN KEY (`store_id`) REFERENCES `store` (`idStore`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_has_storage`
--

LOCK TABLES `store_has_storage` WRITE;
/*!40000 ALTER TABLE `store_has_storage` DISABLE KEYS */;
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

-- Dump completed on 2020-07-30  8:28:19
