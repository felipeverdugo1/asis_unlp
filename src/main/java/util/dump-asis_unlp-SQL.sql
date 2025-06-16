-- MySQL dump 10.13  Distrib 8.1.0, for Win64 (x86_64)
--
-- Host: localhost    Database: asis_unlp
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `barrios`
--

DROP TABLE IF EXISTS `barrios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `barrios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `geolocalizacion` varchar(255) NOT NULL,
  `informacion` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ll2lkl4lrom8de110m6pdqtri` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barrios`
--

LOCK TABLES `barrios` WRITE;
/*!40000 ALTER TABLE `barrios` DISABLE KEYS */;
INSERT INTO `barrios` VALUES (2,'Geo Test','Info Test','TEST_Barrio Test');
/*!40000 ALTER TABLE `barrios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campanias`
--

DROP TABLE IF EXISTS `campanias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campanias` (
  `fecha_fin` date NOT NULL,
  `fecha_inicio` date NOT NULL,
  `barrio_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6dn773tvtx1xwg7elr8huvh0m` (`barrio_id`),
  CONSTRAINT `FK6dn773tvtx1xwg7elr8huvh0m` FOREIGN KEY (`barrio_id`) REFERENCES `barrios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campanias`
--

LOCK TABLES `campanias` WRITE;
/*!40000 ALTER TABLE `campanias` DISABLE KEYS */;
INSERT INTO `campanias` VALUES ('2025-06-23','2025-06-13',2,1,'TEST_Campaña Test 1');
/*!40000 ALTER TABLE `campanias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encuestadores`
--

DROP TABLE IF EXISTS `encuestadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `encuestadores` (
  `edad` int NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dni` varchar(255) NOT NULL,
  `genero` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `ocupacion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encuestadores`
--

LOCK TABLES `encuestadores` WRITE;
/*!40000 ALTER TABLE `encuestadores` DISABLE KEYS */;
INSERT INTO `encuestadores` VALUES (32,1,'41999111','Masculino','TEST_Encuestador de prueba','Psicologo'),(12,2,'41999111','Masculino','TEST_otro de prueba','Medico');
/*!40000 ALTER TABLE `encuestadores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encuestas`
--

DROP TABLE IF EXISTS `encuestas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `encuestas` (
  `fecha` date NOT NULL,
  `encuestador_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `jornada_id` bigint NOT NULL,
  `zona_id` bigint DEFAULT NULL,
  `nombreUnico` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKevfaocf2h9k90bdejn8h2glfl` (`encuestador_id`),
  KEY `FKdpuagtm0iocma390qw9fco5sk` (`jornada_id`),
  KEY `FKtg67eythyewq8ql9eny949ult` (`zona_id`),
  CONSTRAINT `FKdpuagtm0iocma390qw9fco5sk` FOREIGN KEY (`jornada_id`) REFERENCES `jornadas` (`id`),
  CONSTRAINT `FKevfaocf2h9k90bdejn8h2glfl` FOREIGN KEY (`encuestador_id`) REFERENCES `encuestadores` (`id`),
  CONSTRAINT `FKtg67eythyewq8ql9eny949ult` FOREIGN KEY (`zona_id`) REFERENCES `zonas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encuestas`
--

LOCK TABLES `encuestas` WRITE;
/*!40000 ALTER TABLE `encuestas` DISABLE KEYS */;
INSERT INTO `encuestas` VALUES ('2025-06-13',1,1,1,1,'Encuesta de prueba'),('2025-06-16',2,2,2,2,'Encuesta de prueba');
/*!40000 ALTER TABLE `encuestas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filtros_personalizados`
--

DROP TABLE IF EXISTS `filtros_personalizados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filtros_personalizados` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint DEFAULT NULL,
  `criterios` text,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5n0p6rqywx27bb84j3uk1b3av` (`usuario_id`),
  CONSTRAINT `FK5n0p6rqywx27bb84j3uk1b3av` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filtros_personalizados`
--

LOCK TABLES `filtros_personalizados` WRITE;
/*!40000 ALTER TABLE `filtros_personalizados` DISABLE KEYS */;
INSERT INTO `filtros_personalizados` VALUES (1,4,'criterios','TEST_filtro'),(2,NULL,'criterios','TEST_filtro2');
/*!40000 ALTER TABLE `filtros_personalizados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jornada_zona`
--

DROP TABLE IF EXISTS `jornada_zona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jornada_zona` (
  `jornada_id` bigint NOT NULL,
  `zona_id` bigint NOT NULL,
  KEY `FKa70upmtixape4thsty8wrxhg7` (`zona_id`),
  KEY `FKf9lu0bkb03d5wtv2iff7yecow` (`jornada_id`),
  CONSTRAINT `FKa70upmtixape4thsty8wrxhg7` FOREIGN KEY (`zona_id`) REFERENCES `zonas` (`id`),
  CONSTRAINT `FKf9lu0bkb03d5wtv2iff7yecow` FOREIGN KEY (`jornada_id`) REFERENCES `jornadas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jornada_zona`
--

LOCK TABLES `jornada_zona` WRITE;
/*!40000 ALTER TABLE `jornada_zona` DISABLE KEYS */;
/*!40000 ALTER TABLE `jornada_zona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jornadas`
--

DROP TABLE IF EXISTS `jornadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jornadas` (
  `fecha_fin` date NOT NULL,
  `fecha_inicio` date NOT NULL,
  `campana_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKi5ut29ree5r9kqh2k7rblfsto` (`campana_id`),
  CONSTRAINT `FKi5ut29ree5r9kqh2k7rblfsto` FOREIGN KEY (`campana_id`) REFERENCES `campanias` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jornadas`
--

LOCK TABLES `jornadas` WRITE;
/*!40000 ALTER TABLE `jornadas` DISABLE KEYS */;
INSERT INTO `jornadas` VALUES ('2025-06-16','2025-06-13',1,1),('2025-06-20','2025-06-17',1,2),('2025-06-23','2025-06-21',1,3);
/*!40000 ALTER TABLE `jornadas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organizaciones_sociales`
--

DROP TABLE IF EXISTS `organizaciones_sociales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organizaciones_sociales` (
  `barrio_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint DEFAULT NULL,
  `actividad_principal` varchar(255) NOT NULL,
  `domicilio` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK66r0dn94w1u8oixf6qd48kjaq` (`barrio_id`),
  KEY `FKoofotymh8gu4ji44j5fmgj4n7` (`usuario_id`),
  CONSTRAINT `FK66r0dn94w1u8oixf6qd48kjaq` FOREIGN KEY (`barrio_id`) REFERENCES `barrios` (`id`),
  CONSTRAINT `FKoofotymh8gu4ji44j5fmgj4n7` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organizaciones_sociales`
--

LOCK TABLES `organizaciones_sociales` WRITE;
/*!40000 ALTER TABLE `organizaciones_sociales` DISABLE KEYS */;
INSERT INTO `organizaciones_sociales` VALUES (2,1,2,'Trabajos duros','Docimiliooo','TEST_org social 1');
/*!40000 ALTER TABLE `organizaciones_sociales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preguntas`
--

DROP TABLE IF EXISTS `preguntas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preguntas` (
  `encuesta_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pregunta` varchar(255) NOT NULL,
  `respuesta` varchar(255) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3w8gqrftdu5fqgyxyiwlv4dva` (`encuesta_id`),
  CONSTRAINT `FK3w8gqrftdu5fqgyxyiwlv4dva` FOREIGN KEY (`encuesta_id`) REFERENCES `encuestas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preguntas`
--

LOCK TABLES `preguntas` WRITE;
/*!40000 ALTER TABLE `preguntas` DISABLE KEYS */;
INSERT INTO `preguntas` VALUES (1,1,'¿Pregunta?','Respusta','Social'),(1,2,'¿pregunta?','Respuesta','Social'),(2,3,'¿Pregunta?','Respuesta','Social');
/*!40000 ALTER TABLE `preguntas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reportes`
--

DROP TABLE IF EXISTS `reportes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reportes` (
  `fecha_creacion` date NOT NULL,
  `campaña_id` bigint DEFAULT NULL,
  `compartido_con` bigint DEFAULT NULL,
  `creado_por` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) NOT NULL,
  `nombreUnico` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5lu5d95dmbfpvx8hxl2l1eqgi` (`compartido_con`),
  KEY `FK2rdsnp3pp6kbv8minrmmudr5e` (`creado_por`),
  KEY `FKogy42it8siyibdkbqmck0g31y` (`campaña_id`),
  CONSTRAINT `FK2rdsnp3pp6kbv8minrmmudr5e` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK5lu5d95dmbfpvx8hxl2l1eqgi` FOREIGN KEY (`compartido_con`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKogy42it8siyibdkbqmck0g31y` FOREIGN KEY (`campaña_id`) REFERENCES `campanias` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reportes`
--

LOCK TABLES `reportes` WRITE;
/*!40000 ALTER TABLE `reportes` DISABLE KEYS */;
INSERT INTO `reportes` VALUES ('2025-06-13',1,NULL,3,1,'Reporte de prueba','Reporte de prueba'),('2025-06-13',1,NULL,4,2,'Reporte de prueba','Reporte de prueba'),('2025-06-13',1,2,5,3,'Reporte de prueba','Reporte de prueba');
/*!40000 ALTER TABLE `reportes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ldv0v52e0udsh2h1rs0r0gw1n` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'TEST_ADMINISTRADOR'),(4,'TEST_ORG_SOCIAL'),(3,'TEST_PERSONAL_SALUD'),(2,'TEST_VISITANTE');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_roles`
--

DROP TABLE IF EXISTS `usuario_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_roles` (
  `rol_id` bigint NOT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`rol_id`,`usuario_id`),
  KEY `FKuu9tea04xb29m2km5lwe46ua` (`usuario_id`),
  CONSTRAINT `FKbt9i9yrb9ug88xnh82n9m60pr` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKuu9tea04xb29m2km5lwe46ua` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_roles`
--

LOCK TABLES `usuario_roles` WRITE;
/*!40000 ALTER TABLE `usuario_roles` DISABLE KEYS */;
INSERT INTO `usuario_roles` VALUES (1,1),(3,1),(4,2),(3,3),(3,4),(3,5);
/*!40000 ALTER TABLE `usuario_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `habilitado` bit(1) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `especialidad` varchar(255) DEFAULT NULL,
  `nombre_usuario` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kfsp0s1tflm1cwlj8idhqsad0` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (_binary '',1,'TEST_admin@test.com',NULL,'TEST_admin','TEST_admin'),(_binary '',2,'usuario1@gmail.com',NULL,'TEST_Referente test 1','password'),(_binary '',3,'usuario2@gmail.com',NULL,'TEST_Personal test 1','password'),(_binary '',4,'usuario3@gmail.com',NULL,'TEST_Personal test 2','password'),(_binary '',5,'usuario4@gmail.com',NULL,'TEST_Personal test 3','password');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zonas`
--

DROP TABLE IF EXISTS `zonas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zonas` (
  `barrio_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `geolocalizacion` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs9um80wqukh16whrg2o94tjjn` (`barrio_id`),
  CONSTRAINT `FKs9um80wqukh16whrg2o94tjjn` FOREIGN KEY (`barrio_id`) REFERENCES `barrios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zonas`
--

LOCK TABLES `zonas` WRITE;
/*!40000 ALTER TABLE `zonas` DISABLE KEYS */;
INSERT INTO `zonas` VALUES (2,1,'Geo Test Zona','Zona Test 1'),(2,2,'Geo Test Zona','Zona Test 2'),(2,3,'Geo Test Zona','Zona Test 3');
/*!40000 ALTER TABLE `zonas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'asis_unlp'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-13 17:54:06
