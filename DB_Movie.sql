-- --------------------------------------------------------
-- Máy chủ:                      127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Phiên bản:           12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for moviecartoon
CREATE DATABASE IF NOT EXISTS `moviecartoon` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `moviecartoon`;

-- Dumping structure for table moviecartoon.comment
CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `rating` int NOT NULL,
  `time_stamp` datetime(6) NOT NULL,
  `episode_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8crmm7n5uxkmbgmnbgnamisc8` (`episode_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8crmm7n5uxkmbgmnbgnamisc8` FOREIGN KEY (`episode_id`) REFERENCES `episode` (`id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.comment: ~19 rows (approximately)
DELETE FROM `comment`;
INSERT INTO `comment` (`id`, `content`, `rating`, `time_stamp`, `episode_id`, `user_id`) VALUES
	(19, 'Dở vãi!', 1, '2024-12-21 13:05:55.040000', 135, 46),
	(20, 'Cũng được', 3, '2024-12-21 13:06:08.099000', 102, 46),
	(22, 'Hahahaha', 4, '2024-12-21 13:20:46.146000', 141, 44),
	(23, 'Cũng hay mà', 4, '2024-12-21 13:21:02.545000', 135, 44),
	(24, 'Cũng tạm được chắc thế :V', 4, '2024-12-21 14:22:19.864000', 148, 14),
	(25, 'Tập này được', 3, '2024-12-21 14:40:30.855000', 149, 14),
	(26, 'Dở chán như cũ không có gì hay', 3, '2024-12-21 14:41:29.233000', 148, 7),
	(27, 'Hơi chán', 1, '2024-12-21 14:42:02.352000', 149, 7),
	(28, '10/10', 5, '2024-12-21 14:54:43.454000', 52, 7),
	(29, 'tuyet voi', 4, '2024-12-21 16:58:26.349000', 69, 14),
	(31, 'tuyet voi', 4, '2024-12-21 19:04:10.058000', 202, 14),
	(32, 'Buồn quá :<', 2, '2024-12-21 19:04:28.018000', 136, 14),
	(34, 'tuyet voi🙂', 5, '2025-01-10 12:40:14.214000', 68, 50),
	(37, 'Do te', 1, '2025-01-10 13:19:54.763000', 68, 7),
	(40, 'hay', 3, '2025-01-10 19:56:26.419000', 52, 14),
	(41, 'phim hay 😚😙😘', 3, '2025-01-10 20:08:41.388000', 52, 51),
	(43, 'Hay 😅', 4, '2025-01-11 20:43:37.217000', 68, 14),
	(44, 'hay', 3, '2025-01-12 01:36:38.339000', 150, 14),
	(47, 'hay', 5, '2025-01-12 07:52:20.166000', 150, 50),
	(48, 'hay', 4, '2025-01-12 09:54:13.116000', 68, 7);

-- Dumping structure for table moviecartoon.country
CREATE TABLE IF NOT EXISTS `country` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `nameanother` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.country: ~4 rows (approximately)
DELETE FROM `country`;
INSERT INTO `country` (`id`, `name`, `nameanother`) VALUES
	(2, 'Trung Quốc', 'China'),
	(6, 'Nhật Bản', 'Japan'),
	(7, 'Mỹ', 'America'),
	(8, 'Hàn Quốc', 'Korea');

-- Dumping structure for table moviecartoon.dexuat_movie
CREATE TABLE IF NOT EXISTS `dexuat_movie` (
  `dexuat_id` bigint NOT NULL,
  `movie_id` bigint NOT NULL,
  PRIMARY KEY (`dexuat_id`,`movie_id`),
  KEY `FK1ptt6b3erc27bvmbln1j6tefd` (`movie_id`),
  CONSTRAINT `FK1ptt6b3erc27bvmbln1j6tefd` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`),
  CONSTRAINT `FKbkva2tpekv9gchdbo204m5x41` FOREIGN KEY (`dexuat_id`) REFERENCES `recomment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.dexuat_movie: ~38 rows (approximately)
DELETE FROM `dexuat_movie`;
INSERT INTO `dexuat_movie` (`dexuat_id`, `movie_id`) VALUES
	(19, 1),
	(21, 1),
	(22, 1),
	(24, 1),
	(27, 1),
	(28, 1),
	(35, 1),
	(38, 1),
	(19, 4),
	(21, 4),
	(23, 4),
	(24, 4),
	(27, 4),
	(28, 4),
	(22, 6),
	(23, 6),
	(24, 6),
	(27, 6),
	(28, 6),
	(27, 7),
	(28, 7),
	(35, 7),
	(36, 7),
	(21, 12),
	(27, 12),
	(38, 12),
	(35, 13),
	(28, 14),
	(27, 28),
	(34, 28),
	(36, 28),
	(34, 30),
	(35, 30),
	(33, 31),
	(33, 32),
	(35, 32),
	(35, 37),
	(35, 41);

-- Dumping structure for table moviecartoon.episode
CREATE TABLE IF NOT EXISTS `episode` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `episode_link` varchar(1000) DEFAULT NULL,
  `movie_id` bigint DEFAULT NULL,
  `timeupdate` date DEFAULT NULL,
  `state` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsbpb6q6d7t2jvwfwmlp0953e4` (`movie_id`),
  CONSTRAINT `FKsbpb6q6d7t2jvwfwmlp0953e4` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`)
) ENGINE=InnoDB AUTO_INCREMENT=303 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.episode: ~124 rows (approximately)
DELETE FROM `episode`;
INSERT INTO `episode` (`id`, `description`, `episode_link`, `movie_id`, `timeupdate`, `state`) VALUES
	(45, '01', 'https://outputvideos.s3.ap-southeast-2.amazonaws.com/7/7_Test.m3u8', 4, '2024-12-05', b'1'),
	(46, '02', 'https://outputvideos.s3.ap-southeast-2.amazonaws.com/7/7_Test.m3u8', 4, '2024-12-05', b'1'),
	(47, '01', NULL, 8, '2024-12-13', b'1'),
	(49, '01', NULL, 6, '2024-12-16', b'1'),
	(50, '01', NULL, 12, '2024-12-16', b'1'),
	(51, '02', NULL, 12, '2024-12-16', b'0'),
	(52, '01', NULL, 7, '2025-01-17', b'1'),
	(54, '02', NULL, 7, '2024-11-17', b'0'),
	(55, '03', NULL, 4, '2024-12-16', b'0'),
	(56, '04', NULL, 4, '2024-12-16', b'0'),
	(57, '01', NULL, 14, '2024-12-17', b'1'),
	(58, '02', NULL, 14, '2024-12-17', b'0'),
	(59, '03', NULL, 14, '2024-12-17', b'0'),
	(60, '02', NULL, 6, '2024-12-18', b'0'),
	(61, '03', NULL, 6, '2024-12-18', b'0'),
	(62, '03', NULL, 7, '2024-12-18', b'0'),
	(63, '04', NULL, 7, '2024-12-18', b'0'),
	(64, '04', NULL, 6, '2024-12-18', b'0'),
	(65, '05', NULL, 6, '2024-12-18', b'0'),
	(66, '06', NULL, 6, '2024-12-18', b'0'),
	(68, '01', 'https://outputvideos.s3.ap-southeast-2.amazonaws.com/1/68/1_68_6_Test.m3u8', 1, '2024-12-18', b'1'),
	(69, '01', NULL, 21, '2024-12-19', b'1'),
	(84, '1', NULL, 24, '2024-12-19', b'1'),
	(96, '1', NULL, 25, '2024-12-19', b'1'),
	(97, '1', NULL, 26, '2024-12-19', b'1'),
	(99, '1', NULL, 28, '2024-12-19', b'1'),
	(101, '2', NULL, 28, '2024-12-21', b'1'),
	(102, '3', NULL, 28, '2024-12-21', b'1'),
	(103, '4', NULL, 28, '2024-12-21', b'1'),
	(104, '5', NULL, 28, '2024-12-21', b'1'),
	(105, '6', NULL, 28, '2024-12-21', b'1'),
	(106, '7', NULL, 28, '2024-12-21', b'1'),
	(107, '8', NULL, 28, '2024-12-21', b'1'),
	(108, '9', NULL, 28, '2024-12-21', b'1'),
	(109, '10', NULL, 28, '2024-12-21', b'1'),
	(110, '11', NULL, 28, '2024-12-21', b'1'),
	(111, '12', NULL, 28, '2024-12-21', b'1'),
	(112, '13', NULL, 28, '2024-12-21', b'1'),
	(113, '14', NULL, 28, '2024-12-21', b'1'),
	(114, '15', NULL, 28, '2024-12-21', b'1'),
	(115, '16', NULL, 28, '2024-12-21', b'1'),
	(116, '17', NULL, 28, '2024-12-21', b'1'),
	(117, '18', NULL, 28, '2024-12-21', b'1'),
	(118, '19', NULL, 28, '2024-12-21', b'1'),
	(119, '20', NULL, 28, '2024-12-21', b'1'),
	(122, '1', NULL, 29, '2024-12-21', b'1'),
	(123, '2', NULL, 29, '2024-12-21', b'1'),
	(124, '3', NULL, 29, '2024-12-21', b'1'),
	(125, '4', NULL, 29, '2024-12-21', b'1'),
	(126, '5', NULL, 29, '2024-12-21', b'1'),
	(127, '6', NULL, 29, '2024-12-21', b'1'),
	(128, '7', NULL, 29, '2024-12-21', b'1'),
	(129, '8', NULL, 29, '2024-12-21', b'1'),
	(130, '9', NULL, 29, '2024-12-21', b'1'),
	(131, '10', NULL, 29, '2024-12-21', b'1'),
	(132, '11', NULL, 29, '2024-12-21', b'1'),
	(133, '12', NULL, 29, '2024-12-21', b'1'),
	(134, '1', NULL, 35, '2024-12-21', b'1'),
	(135, '1', NULL, 38, '2024-12-21', b'1'),
	(136, '1', NULL, 30, '2024-12-21', b'1'),
	(137, '2', NULL, 30, '2024-12-21', b'1'),
	(138, '3', NULL, 30, '2024-12-21', b'1'),
	(139, '4', NULL, 30, '2024-12-21', b'1'),
	(140, '5', NULL, 30, '2024-12-21', b'1'),
	(141, '6', NULL, 30, '2024-12-21', b'1'),
	(142, '7', NULL, 30, '2024-12-21', b'1'),
	(143, '8', NULL, 30, '2024-12-21', b'1'),
	(144, '9', NULL, 30, '2024-12-21', b'1'),
	(145, '10', NULL, 30, '2024-12-21', b'1'),
	(146, '11', NULL, 30, '2024-12-21', b'1'),
	(147, '12', NULL, 30, '2024-12-21', b'1'),
	(148, '01', 'https://outputvideos.s3.ap-southeast-2.amazonaws.com/41/148/41_148_Hu%20He%20Yao%20Shi%20Lu%2001.m3u8', 41, '2024-12-21', b'1'),
	(149, '02', 'https://outputvideos.s3.ap-southeast-2.amazonaws.com/41/149/41_149_%E3%80%90%F0%9F%A4%A9%C4%90%C3%A3%20c%C3%B3%20ho%E1%BA%A1t%20h%C3%ACnh%203D%20m%E1%BB%9Bi%20ra%20m%E1%BA%AFt%EF%BC%81_%20H%E1%BB%95%20H%E1%BA%A1c%20Y%C3%AAu%20S%C6%B0%20L%E1%BB%A5c%E3%80%91T%E1%BA%ADp%2001%20_%20Theo%20d%C3%B5i%20k%C3%AAnh%20xem%20th%C3%AAm%20phim%20hay.m3u8', 41, '2024-12-21', b'1'),
	(150, '02', 'https://outputvideos.s3.ap-southeast-2.amazonaws.com/1/150/1_150_Test2.m3u8', 1, '2024-12-21', b'1'),
	(151, '01', NULL, 9, '2024-12-21', b'0'),
	(152, '02', NULL, 9, '2024-12-21', b'0'),
	(153, '01', NULL, 13, '2024-12-21', b'0'),
	(154, '02', NULL, 13, '2024-12-21', b'0'),
	(155, '01', NULL, 32, '2024-12-21', b'0'),
	(162, '01', NULL, 36, '2024-12-21', b'0'),
	(164, '01', NULL, 37, '2024-12-21', b'1'),
	(166, '03', NULL, 1, '2024-12-21', b'0'),
	(167, '05', NULL, 4, '2024-12-21', b'0'),
	(168, '03', NULL, 9, '2024-12-21', b'0'),
	(169, '01', NULL, 10, '2024-12-21', b'0'),
	(170, '02', NULL, 10, '2024-12-21', b'0'),
	(171, '03', NULL, 12, '2024-12-21', b'0'),
	(172, '03', NULL, 13, '2024-12-21', b'0'),
	(173, '04', NULL, 14, '2024-12-21', b'0'),
	(174, '02', NULL, 21, '2024-12-21', b'0'),
	(183, '21', NULL, 28, '2024-12-21', b'0'),
	(184, '13', NULL, 29, '2024-12-21', b'0'),
	(185, '13', NULL, 30, '2024-12-21', b'0'),
	(188, '01', NULL, 31, '2024-12-21', b'0'),
	(199, '01', NULL, 39, '2024-12-21', b'0'),
	(202, '01', NULL, 40, '2024-12-21', b'1'),
	(205, '06', NULL, 4, '2024-12-21', b'0'),
	(206, '05', NULL, 7, '2024-12-21', b'0'),
	(207, '02', NULL, 8, '2024-12-21', b'0'),
	(208, '04', NULL, 9, '2024-12-21', b'0'),
	(209, '03', NULL, 10, '2024-12-21', b'0'),
	(210, '04', NULL, 12, '2024-12-21', b'0'),
	(211, '03', NULL, 21, '2024-12-21', b'0'),
	(219, '22', NULL, 28, '2024-12-21', b'0'),
	(253, '04', NULL, 1, '2025-01-07', b'0'),
	(257, '05', NULL, 1, '2025-01-07', b'0'),
	(258, '05', NULL, 9, '2025-01-07', b'0'),
	(263, '06', NULL, 1, '2025-01-08', b'0'),
	(264, '07', NULL, 1, '2025-01-08', b'0'),
	(268, '07', NULL, 4, '2025-01-08', b'0'),
	(269, '07', NULL, 6, '2025-01-08', b'0'),
	(270, '03', NULL, 8, '2025-01-08', b'0'),
	(272, '04', NULL, 8, '2025-01-08', b'0'),
	(274, '05', NULL, 8, '2025-01-08', b'0'),
	(276, '05', NULL, 12, '2025-01-08', b'0'),
	(278, '06', NULL, 12, '2025-01-08', b'0'),
	(279, '07', NULL, 12, '2025-01-08', b'0'),
	(280, '04', NULL, 21, '2025-01-08', b'0'),
	(281, '05', NULL, 21, '2025-01-08', b'0'),
	(283, '01', NULL, 22, '2025-01-08', b'0'),
	(284, '04', NULL, 13, '2025-01-08', b'0'),
	(285, '05', NULL, 13, '2025-01-08', b'0'),
	(287, '04', NULL, 10, '2025-01-08', b'0'),
	(288, '01', NULL, 23, '2025-01-09', b'0'),
	(290, '03', NULL, 23, '2025-01-10', b'0'),
	(294, '05', NULL, 10, '2025-01-12', b'0'),
	(295, '06', NULL, 9, '2025-01-12', b'0'),
	(296, '07', NULL, 9, '2025-01-12', b'0'),
	(298, '06', NULL, 13, '2025-01-12', b'0'),
	(300, '06', NULL, 7, '2025-01-12', b'0'),
	(301, '07', NULL, 7, '2025-01-12', b'0'),
	(302, '06', NULL, 10, '2025-01-12', b'0');

-- Dumping structure for table moviecartoon.forgot_password
CREATE TABLE IF NOT EXISTS `forgot_password` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expired_time` datetime(6) NOT NULL,
  `otp` int NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKss96nm4ed1jmllpxib14p1r7v` (`user_id`),
  CONSTRAINT `FK95rqabtnw8wouua80mbixrq4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.forgot_password: ~0 rows (approximately)
DELETE FROM `forgot_password`;

-- Dumping structure for table moviecartoon.genre
CREATE TABLE IF NOT EXISTS `genre` (
  `genre_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `trans_name` varchar(50) NOT NULL,
  PRIMARY KEY (`genre_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.genre: ~10 rows (approximately)
DELETE FROM `genre`;
INSERT INTO `genre` (`genre_id`, `name`, `trans_name`) VALUES
	(3, 'Huyền Huyễn', 'Mystic'),
	(6, 'Lãng mạn', 'Romantic'),
	(7, 'Hành động', 'Action'),
	(8, 'Phiêu lưu', 'Adventure'),
	(9, 'Hài hước', 'Comedy'),
	(10, 'Kinh dị', 'Horror'),
	(11, 'Tâm lý', 'Drama'),
	(14, 'Hình sự', 'Crime'),
	(15, 'Kỳ ảo', 'Fantasy'),
	(16, 'Học đường', 'School');

-- Dumping structure for table moviecartoon.history
CREATE TABLE IF NOT EXISTS `history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ngayxem` date DEFAULT NULL,
  `episode_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `view_count` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7ygs3aphj5ttbomb695hbu715` (`episode_id`),
  KEY `FKn4gjyu69m6xa5f3bot571imbe` (`user_id`),
  CONSTRAINT `FK7ygs3aphj5ttbomb695hbu715` FOREIGN KEY (`episode_id`) REFERENCES `episode` (`id`),
  CONSTRAINT `FKn4gjyu69m6xa5f3bot571imbe` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.history: ~19 rows (approximately)
DELETE FROM `history`;
INSERT INTO `history` (`id`, `ngayxem`, `episode_id`, `user_id`, `view_count`) VALUES
	(8, '2024-12-18', 47, 14, 6),
	(12, '2024-12-18', 49, 14, 22),
	(13, '2024-12-18', 50, 14, 2),
	(14, '2024-12-19', 69, 44, 2),
	(18, '2024-12-21', 141, 44, 6),
	(26, '2024-12-21', 135, 46, 4),
	(27, '2024-12-21', 102, 46, 4),
	(29, '2024-12-21', 135, 44, 4),
	(30, '2024-12-21', 149, 14, 28),
	(31, '2024-12-21', 149, 7, 8),
	(32, '2024-12-21', 52, 7, 4),
	(33, '2024-12-21', 69, 14, 4),
	(34, '2024-12-21', 99, 14, 2),
	(35, '2024-12-21', 149, 47, 16),
	(36, '2024-12-21', 149, 48, 4),
	(37, '2024-12-21', 52, 48, 6),
	(38, '2025-01-09', 68, 14, 20),
	(39, '2024-12-21', 202, 14, 4),
	(40, '2024-12-21', 136, 14, 4),
	(41, '2025-01-02', 45, 14, 10);

-- Dumping structure for table moviecartoon.lovelist
CREATE TABLE IF NOT EXISTS `lovelist` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ngayluu` date DEFAULT NULL,
  `movie_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs6tr91b1l28whpmav0b7x7ajk` (`movie_id`),
  KEY `FKj87qdot32tyfbuda53xe3f22w` (`user_id`),
  CONSTRAINT `FKj87qdot32tyfbuda53xe3f22w` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKs6tr91b1l28whpmav0b7x7ajk` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.lovelist: ~24 rows (approximately)
DELETE FROM `lovelist`;
INSERT INTO `lovelist` (`id`, `ngayluu`, `movie_id`, `user_id`) VALUES
	(16, '2024-12-12', 14, 33),
	(19, '2024-12-15', 6, 42),
	(20, '2024-12-15', 1, 8),
	(21, '2024-12-15', 4, 8),
	(22, '2024-12-15', 6, 7),
	(24, '2024-12-17', 12, 14),
	(25, '2024-12-17', 6, 14),
	(26, '2024-12-19', 28, 44),
	(27, '2024-12-21', 32, 46),
	(28, '2024-12-21', 31, 46),
	(29, '2024-12-21', 28, 46),
	(30, '2024-12-21', 30, 46),
	(31, '2024-12-21', 41, 14),
	(32, '2024-12-21', 7, 47),
	(35, '2024-12-21', 1, 47),
	(36, '2024-12-21', 22, 47),
	(38, '2025-01-09', 7, 8),
	(39, '2025-01-09', 1, 7),
	(42, '2025-01-09', 7, 7),
	(44, '2025-01-10', 1, 14),
	(47, '2025-01-10', 7, 14),
	(49, '2025-01-10', 31, 51),
	(50, '2025-01-10', 7, 51),
	(59, '2025-01-12', 4, 50),
	(60, '2025-01-12', 31, 7);

-- Dumping structure for table moviecartoon.message
CREATE TABLE IF NOT EXISTS `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `info` varchar(255) DEFAULT NULL,
  `ngaydang` date DEFAULT NULL,
  `movie_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `type` bit(1) DEFAULT NULL,
  `emergency` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp07nrmvk8wfs166s4exmqqcji` (`movie_id`),
  KEY `FKb3y6etti1cfougkdr0qiiemgv` (`user_id`),
  CONSTRAINT `FKb3y6etti1cfougkdr0qiiemgv` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKp07nrmvk8wfs166s4exmqqcji` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`)
) ENGINE=InnoDB AUTO_INCREMENT=317 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.message: ~67 rows (approximately)
DELETE FROM `message`;
INSERT INTO `message` (`id`, `info`, `ngaydang`, `movie_id`, `user_id`, `type`, `emergency`) VALUES
	(17, 'Lỗi trang: Chắc thế', '2024-12-03', 4, 7, b'1', b'1'),
	(23, 'Phản hồi: Update nhanh hơn đi', '2024-12-16', 1, 14, b'1', b'1'),
	(81, 'Phim: Từ trong ra ngoài Đã cập nhật tập: 1', '2024-12-21', 22, 44, b'0', b'0'),
	(84, 'Phim: Chú bọt biển tinh nghịch phần 1 Đã cập nhật tập: 4', '2024-12-21', 28, 44, b'0', b'0'),
	(87, 'Phim: Chú bọt biển tinh nghịch phần 1 Đã cập nhật tập: 7', '2024-12-21', 28, 44, b'0', b'0'),
	(88, 'Phim: Chú bọt biển tinh nghịch phần 1 Đã cập nhật tập: 8', '2024-12-21', 28, 44, b'0', b'0'),
	(95, 'Phim: Chú bọt biển tinh nghịch phần 1 Đã cập nhật tập: 15', '2024-12-21', 28, 44, b'0', b'0'),
	(99, 'Phim: Chú bọt biển tinh nghịch phần 1 Đã cập nhật tập: 19', '2024-12-21', 28, 44, b'0', b'0'),
	(100, 'Phim: Chú bọt biển tinh nghịch phần 1 Đã cập nhật tập: 20', '2024-12-21', 28, 44, b'0', b'0'),
	(102, 'Phim: Chú bọt biển tinh nghịch phần 1 Đã cập nhật tập: 22', '2024-12-21', 28, 44, b'0', b'0'),
	(114, 'Phim: Chú bọt biển tinh nghịch phần 2 Đã cập nhật tập: 12', '2024-12-21', 29, 44, b'0', b'0'),
	(117, 'Phim: Chó Ma Frankenweenie Đã cập nhật tập: 1', '2024-12-21', 35, 44, b'0', b'0'),
	(118, 'Phim: Cô Dâu Ma Đã cập nhật tập: 1', '2024-12-21', 37, 44, b'0', b'0'),
	(119, 'Phim: Ngôi Nhà Ma Quái Đã cập nhật tập: 1', '2024-12-21', 38, 44, b'0', b'0'),
	(120, 'Phim: Family Guy Đã cập nhật tập: 2', '2024-12-21', 30, 44, b'0', b'0'),
	(122, 'Phim: Family Guy Đã cập nhật tập: 2', '2024-12-21', 30, 44, b'0', b'0'),
	(251, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2024-12-21', 1, 14, b'0', b'0'),
	(252, 'Phim: Kẻ Cắp Mặt Trăng Đã cập nhật tập: 02', '2024-12-21', 31, 14, b'0', b'0'),
	(253, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2024-12-21', 1, 14, b'0', b'0'),
	(254, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2024-12-21', 1, 14, b'0', b'0'),
	(255, 'Phim: Kẻ Cắp Mặt Trăng Đã cập nhật tập: 02', '2024-12-21', 31, 14, b'0', b'0'),
	(256, 'Đề xuất cải tiến: Phim tap 2 dang bi loi', '2024-12-21', 1, 14, b'1', b'1'),
	(257, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2025-01-07', 1, 14, b'0', b'0'),
	(258, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2025-01-07', 1, 14, b'0', b'0'),
	(259, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2025-01-07', 1, 14, b'0', b'0'),
	(260, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2025-01-07', 1, 14, b'0', b'0'),
	(261, 'Phim: Kiếm lai Đã cập nhật tập: 05', '2025-01-07', 1, 14, b'0', b'0'),
	(262, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2025-01-07', 1, 14, b'0', b'0'),
	(263, 'Phim: Kiếm lai Đã cập nhật tập: 05', '2025-01-07', 1, 14, b'0', b'0'),
	(264, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2025-01-07', 1, 14, b'0', b'0'),
	(265, 'Phim: Kiếm lai Đã cập nhật tập: 05', '2025-01-07', 1, 14, b'0', b'0'),
	(266, 'Phim: Kiếm lai Đã cập nhật tập: 06', '2025-01-07', 1, 14, b'0', b'0'),
	(267, 'Phim: Kiếm lai Đã cập nhật tập: 04', '2025-01-07', 1, 14, b'0', b'0'),
	(268, 'Phim: Kiếm lai Đã cập nhật tập: 05', '2025-01-07', 1, 14, b'0', b'0'),
	(269, 'Phim: Kiếm lai Đã cập nhật tập: 05', '2025-01-07', 1, 14, b'0', b'0'),
	(270, 'Phim: Kiếm lai Đã cập nhật tập: 06', '2025-01-07', 1, 14, b'0', b'0'),
	(271, 'Phim: Kiếm lai Đã cập nhật tập: 05', '2025-01-07', 1, 14, b'0', b'0'),
	(272, 'Phim: Ở Rể Đã cập nhật tập: 05', '2025-01-07', 9, 14, b'0', b'0'),
	(273, 'Phim: Ở Rể Đã cập nhật tập: 07', '2025-01-07', 9, 14, b'0', b'0'),
	(274, 'Phim: Kiếm lai Đã cập nhật tập: 06', '2025-01-08', 1, 14, b'0', b'0'),
	(275, 'Phim: Kiếm lai Đã cập nhật tập: 06', '2025-01-08', 1, 14, b'0', b'0'),
	(276, 'Phim: Kiếm lai Đã cập nhật tập: 06', '2025-01-08', 1, 14, b'0', b'0'),
	(277, 'Phim: Kiếm lai Đã cập nhật tập: 06', '2025-01-08', 1, 14, b'0', b'0'),
	(278, 'Phim: Kiếm lai Đã cập nhật tập: 07', '2025-01-08', 1, 14, b'0', b'0'),
	(279, 'Phim: Đan Đạo Chí Tôn Đã cập nhật tập: 07', '2025-01-08', 4, 14, b'0', b'0'),
	(280, 'Phim: Đan Đạo Chí Tôn Đã cập nhật tập: 07', '2025-01-08', 4, 14, b'0', b'0'),
	(281, 'Phim: Đan Đạo Chí Tôn Đã cập nhật tập: 07', '2025-01-08', 4, 14, b'0', b'0'),
	(282, 'Phim: Đan Đạo Chí Tôn Đã cập nhật tập: 07', '2025-01-08', 4, 14, b'0', b'0'),
	(283, 'Phim:  Trường Sinh Giới Đã cập nhật tập: 07', '2025-01-08', 6, 14, b'0', b'0'),
	(284, 'Phim:  Đấu Phá Thương Khung Phần 5 Đã cập nhật tập: 03', '2025-01-08', 8, 14, b'0', b'0'),
	(285, 'Phim:  Đấu Phá Thương Khung Phần 5 Đã cập nhật tập: 04', '2025-01-08', 8, 14, b'0', b'0'),
	(286, 'Phim:  Đấu Phá Thương Khung Phần 5 Đã cập nhật tập: 04', '2025-01-08', 8, 14, b'0', b'0'),
	(287, 'Phim:  Đấu Phá Thương Khung Phần 5 Đã cập nhật tập: 05', '2025-01-08', 8, 14, b'0', b'0'),
	(288, 'Phim:  Đấu Phá Thương Khung Phần 5 Đã cập nhật tập: 05', '2025-01-08', 8, 14, b'0', b'0'),
	(289, 'Phim: Bạch Xà: Duyên Khởi Đã cập nhật tập: 05', '2025-01-08', 12, 14, b'0', b'0'),
	(290, 'Phim: Bạch Xà: Duyên Khởi Đã cập nhật tập: 05', '2025-01-08', 12, 14, b'0', b'0'),
	(291, 'Phim: Bạch Xà: Duyên Khởi Đã cập nhật tập: 06', '2025-01-08', 12, 14, b'0', b'0'),
	(292, 'Phim: Bạch Xà: Duyên Khởi Đã cập nhật tập: 06', '2025-01-08', 12, 14, b'0', b'0'),
	(293, 'Phim: Bạch Xà: Duyên Khởi Đã cập nhật tập: 07', '2025-01-08', 12, 14, b'0', b'0'),
	(294, 'Phim:  Ta Có Thể Giác Ngộ Vô Hạn Đã cập nhật tập: 04', '2025-01-08', 21, 14, b'0', b'0'),
	(295, 'Phim:  Ta Có Thể Giác Ngộ Vô Hạn Đã cập nhật tập: 05', '2025-01-08', 21, 14, b'0', b'0'),
	(296, 'Phim: Từ trong ra ngoài Đã cập nhật tập: 01', '2025-01-08', 22, 14, b'0', b'0'),
	(297, 'Phim: Từ trong ra ngoài Đã cập nhật tập: 01', '2025-01-08', 22, 14, b'0', b'0'),
	(298, 'Phim:  Bách Luyện Thành Thần Đã cập nhật tập: 04', '2025-01-08', 13, 14, b'0', b'0'),
	(299, 'Phim:  Bách Luyện Thành Thần Đã cập nhật tập: 05', '2025-01-08', 13, 14, b'0', b'0'),
	(300, 'Phim:  Bách Luyện Thành Thần Đã cập nhật tập: 05', '2025-01-08', 13, 14, b'0', b'0'),
	(301, 'Phim:  Ở Rể Phần 2 Đã cập nhật tập: 04', '2025-01-08', 10, 14, b'0', b'0'),
	(302, 'Phim: Từ trong ra ngoài 2 Đã cập nhật tập: 01', '2025-01-09', 23, 14, b'0', b'0'),
	(303, 'Phim: Chó Ma Frankenweenie Đã cập nhật tập: 03', '2025-01-09', 35, 7, b'0', b'0'),
	(304, 'Phim: Từ trong ra ngoài 2 Đã cập nhật tập: 03', '2025-01-10', 23, 14, b'0', b'0'),
	(305, 'Phim: Từ trong ra ngoài Đã cập nhật tập: 03', '2025-01-10', 22, 14, b'0', b'0'),
	(306, 'Phim: Gấu trúc Kung Fu 2 Đã cập nhật tập: 01', '2025-01-11', 25, 14, b'0', b'0'),
	(307, 'Phim: Gấu trúc Kung Fu 2 Đã cập nhật tập: 05', '2025-01-11', 25, 14, b'0', b'0'),
	(308, 'Phim:  Ở Rể Phần 2 Đã cập nhật tập: 05', '2025-01-12', 10, 14, b'0', b'0'),
	(309, 'Phim: Ở Rể Đã cập nhật tập: 06', '2025-01-12', 9, 14, b'0', b'0'),
	(310, 'Phim: Ở Rể Đã cập nhật tập: 07', '2025-01-12', 9, 14, b'0', b'0'),
	(311, 'Phim:  Bách Luyện Thành Thần Đã cập nhật tập: 06', '2025-01-12', 13, 14, b'0', b'0'),
	(312, 'Phim:  Bách Luyện Thành Thần Đã cập nhật tập: 06', '2025-01-12', 13, 14, b'0', b'0'),
	(313, 'Phim: Tiên Nghịch Đã cập nhật tập: 06', '2025-01-12', 7, 14, b'0', b'0'),
	(314, 'Phim: Tiên Nghịch Đã cập nhật tập: 06', '2025-01-12', 7, 14, b'0', b'0'),
	(315, 'Phim: Tiên Nghịch Đã cập nhật tập: 07', '2025-01-12', 7, 14, b'0', b'0'),
	(316, 'Phim:  Ở Rể Phần 2 Đã cập nhật tập: 06', '2025-01-12', 10, 14, b'0', b'0');

-- Dumping structure for table moviecartoon.movie
CREATE TABLE IF NOT EXISTS `movie` (
  `movie_id` bigint NOT NULL AUTO_INCREMENT,
  `average` double DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `duration` int NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `ismovie` bit(1) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `nameanother` varchar(200) NOT NULL,
  `state` bit(1) DEFAULT NULL,
  `studio` varchar(100) NOT NULL,
  `timeupdate` date NOT NULL,
  `trailerurl` varchar(255) DEFAULT NULL,
  `view` bigint DEFAULT NULL,
  `country_id` bigint DEFAULT NULL,
  `premium` bit(1) DEFAULT NULL,
  PRIMARY KEY (`movie_id`),
  KEY `FK5h5hkyxprvsgpqg69nqsq5p48` (`country_id`),
  CONSTRAINT `FK5h5hkyxprvsgpqg69nqsq5p48` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.movie: ~31 rows (approximately)
DELETE FROM `movie`;
INSERT INTO `movie` (`movie_id`, `average`, `description`, `duration`, `image`, `ismovie`, `name`, `nameanother`, `state`, `studio`, `timeupdate`, `trailerurl`, `view`, `country_id`, `premium`) VALUES
	(1, 3.7, 'Jian Lei', 12, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/1_kiem-lai-300x450.jpg', b'0', 'Kiếm lai', 'Jian Lei', b'0', 'CN Animation', '2024-10-25', 'https://www.youtube.com/embed/qK91oDv02FE?si=gsWJFjsduvzlzD8B', 40, 2, b'1'),
	(4, 0, 'Lửa thần thiêu đốt, nhập thể hồi sinh, nhưng lại bị truy sát đủ đường. Để sống, hắn không ngừng tranh đấu với trời đất, với con người và với đủ loại yêu ma quỷ quái tàn ác, nghịch chuyển vận mệnh, tung hoành dị giới.', 3, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/4_dan-dao-chi-ton-300x450.jpeg', b'0', 'Đan Đạo Chí Tôn', 'Dan Dao Zhi Zun', b'1', 'CN Animation', '2024-10-29', 'https://www.youtube.com/embed/9s7AhPGHn3E?si=ZRtNlR0MAk79twlK', 7, 6, b'0'),
	(6, 0, 'Thế gian ai có thể trường sinh bất tử? Dù có là tuyệt thế giai nhân, khuynh quốc khuynh thành, cuối cùng cũng chỉ còn lại bộ xương khô; dù có là thiên tài kiệt xuất, sở hữu giang sơn muôn dặm', 3, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/6_truong-sinh-gioi.jpg', b'0', ' Trường Sinh Giới', ' Chang Sheng Jie', b'1', 'CN Animation', '2024-10-29', 'https://www.youtube.com/embed/0d5z1BLChtE?si=ajw47mUhfd9_Z43r', 0, 2, b'1'),
	(7, 3.7, 'Cải biên từ tiểu thuyết “Tiên Nghịch” của tác giả Nhĩ Căn, kể về thiếu niên bình phàm Vương Lâm xuất thân nông thôn, mang theo nhiệt huyết, tu luyện nghịch tiên', 4, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/7_tien-nghich-3-300x450.jpg', b'0', 'Tiên Nghịch', ' Xian Ni', b'1', 'CN Animation', '2024-10-29', 'https://www.youtube.com/embed/bgUdqoNpOvE?si=Gn26UkvFQSAx51AZ', 0, 2, b'0'),
	(8, 0, 'Sau hẹn ước 3 năm, Tiêu Viêm cuối cùng cũng gặp được Huân Nhi ở học viện Già Nam, sau đó hắn kết giao nhiều bạn bè, thành lập Bàn Môn; vì tiếp tục nâng cao thực lực để lên Vân Lam Tông lần 3 báo thù cho cha', 4, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/8_dau-pha-thuong-khung-phan-5-gia-nam-hoc-vien-1-1-300x450.jpg', b'0', ' Đấu Phá Thương Khung Phần 5', 'Fights Break Sphere 5', b'1', 'CN Animation', '2024-10-29', 'https://www.youtube.com/embed/eHY7uJpZmKg?si=hdibSmF7Nyt5bVLd', 0, 6, b'0'),
	(9, 0, 'Một nhà buôn tinh anh xuyên không đến Triều Võ, trở thành con rể với địa vị thấp kém bị khinh của gia đình thương nhân họ Ninh tên Nghị, tự Lập Hằng, ngược lại thê tử Tô Đàn Nhi luôn bận rộn với việc làm ăn', 6, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/9_o-re-300x450.png', b'0', 'Ở Rể', 'My Heroic Husband', b'1', 'CN Animation', '2025-01-29', 'https://www.youtube.com/embed/L4E0o5kIEBc?si=KTa0rvdFe1HbjisG', 0, 2, b'0'),
	(10, 0, 'Doãn Ninh đến Vũ triều và trở thành Ninh Nghị, con rể của một nhà thương nhân với địa vị thấp bé', 12, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/10_o-re-phan-2-300x450.jpg', b'1', ' Ở Rể Phần 2', ' My Heroic Husband 2', b'0', 'CN Animation', '2024-10-26', 'https://www.youtube.com/embed/JVDErAjqy9g?si=aYvqUpLM0j_d_gtp', 0, 2, b'0'),
	(12, 0, 'Bạch xà: Duyên khởi – White Snake là bộ phim hoạt hình 3D thuộc thể loại giả tưởng, phép thuật của Trung Quốc năm 2019 do Jiakang Huang chịu trách nhiệm đạo diễn, tác phẩm được sản xuất bởi Light Chaser Animation', 12, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_bach-xa-duyen-khoi-616-3-300x450.jpg', b'1', 'Bạch Xà: Duyên Khởi', ' White Snake (2019)', b'0', 'CN Animation', '2024-10-29', 'https://www.youtube.com/embed/FCY7E0oHW6Y?si=Sxz-ATq3u_QPfIOF', 0, 2, b'0'),
	(13, 0, 'Gia tộc sa sút, người thân gặp nạn, La Chinh rơi từ trên mây xuống trở thành một gia nô thấp hèn, trong một thế giới được thống trị bởi sức mạnh bí ẩn, các gia tộc không ngừng đấu tranh,', 12, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_bach-luyen-thanh-than-3-300x450.jpg', b'1', ' Bách Luyện Thành Thần', ' Elevation to the Status of a God', b'1', 'CN Animation', '2024-10-27', 'https://www.youtube.com/embed/yuJvcf260ys?si=HivWLkRuYsXFDxUo', 0, 2, b'0'),
	(14, 0, 'Để lấy lại bảo vật của thần giới, thần nữ Vô Song hạ phàm thích sát Đai tế tư Nguyên Trọng của Hồ tộc. Nhưng sau đó, nàng lại phát hiện trong chuyện này có ẩn tình. Trong quá trình điều tra, hai người nảy sinh tình cảm với nhau.', 12, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/14_IMG_0534-300x449.png', b'0', ' Niệm Vô Song', ' A Moment But Forever', b'0', 'CN Animation', '2024-11-20', 'https://www.youtube.com/embed/wckaiHLQOSE?si=kYN1XnGxsYdixu5-', 0, 2, b'0'),
	(21, 4, 'Dựa vào khí phách, lòng dũng cảm, cũng như sự trợ giúp của hệ thống giác ngộ, thiếu niên Tiêu Vân với cơ thể phàm trần tham gia giải đấu Thần thể đại năng tranh đoạt tư cách Đại đế. Nhưng mà, sao Đại Đế trăm vạn năm trước lại biết đến hắn? Sao hắn trong tương lai lại để lại bút ký? Lai lịch thực sự của hệ thống giác ngộ lại là gì nữa? Tất cả thế giới bình hành đang đối diện với nguy cơ cực lớn, người duy nhất có thể cứu vớt được thế giới chính là thiếu niên trái đất mà chúng thần triệu hồi đến.', 20, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/21_I%20Can%20Have%20Infinite.jpg', b'0', ' Ta Có Thể Giác Ngộ Vô Hạn', ' I Can Have Infinite', b'0', 'CN Animation', '2024-12-11', 'https://www.youtube.com/embed/pY59TjEr8bQ?si=M1AqdJtkXL2g335F', 0, 2, b'1'),
	(22, 0, 'Sau khi Riley trẻ tuổi bị nhổ khỏi cuộc sống Trung Tây và chuyển đến San Francisco, cảm xúc của cô - Niềm vui, Sợ hãi, Tức giận, Ghê tởm và Buồn bã - xung đột về cách tốt nhất để điều hướng một thành phố, ngôi nhà và trường học mới.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/22_Inside%20out.jpg', b'1', 'Từ trong ra ngoài', 'Inside Out', b'1', 'Pixar Animation', '2024-12-08', 'https://www.youtube.com/embed/yRUAzGQ3nSY?si=F4pSVGZAjQ7KLC_W', 0, 7, b'1'),
	(23, 0, 'Phần tiếp theo có Riley bước vào tuổi dậy thì và kết quả là trải nghiệm những cảm xúc hoàn toàn mới, phức tạp hơn. Khi Riley cố gắng thích nghi với những năm thiếu niên của mình, những cảm xúc cũ của cô ấy cố gắng thích nghi với khả năng bị thay thế.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/23_Inside%20out%202.jpg', b'1', 'Từ trong ra ngoài 2', 'Inside out 2', b'1', 'Pixar Animation', '2025-01-02', 'https://www.youtube.com/embed/LEjhY15eCx0?si=1DmTJMhEi14gFEZm', 0, 7, b'1'),
	(24, 0, 'Trước sự ngạc nhiên của mọi người, bao gồm cả chính mình, Po, một chú gấu trúc thừa cân, vụng về, được chọn làm người bảo vệ Thung lũng Hòa bình. Sự phù hợp của anh ta sẽ sớm được thử thách khi kẻ thù không đội trời chung của thung lũng đang trên đường đi của anh ta.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/24_Kunfu%20panda.jpg', b'1', 'Gấu trúc Kung Fu', 'Kung Fu Panda', b'1', 'DreamWorks Animation', '2024-12-06', 'https://www.youtube.com/embed/NRc-ze7Wrxw?si=1NtnN4T9UDN-s71p', 0, 7, b'0'),
	(25, 0, 'Po và những người bạn của mình chiến đấu để ngăn chặn một nhân vật phản diện con công chinh phục Trung Quốc bằng một vũ khí mới chết người, nhưng trước tiên Chiến binh Rồng phải đối mặt với quá khứ của mình.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/25_Kunfu%20panda%202.jpg', b'1', 'Gấu trúc Kung Fu 2', 'Kung Fu Panda 2', b'1', 'DreamWorks Animation', '2009-07-17', 'https://www.youtube.com/embed/TIA6KNfpRqs?si=p_Gu12Jgo3wlDip9', 0, 7, b'0'),
	(26, 0, 'Tiếp tục "cuộc phiêu lưu huyền thoại tuyệt vời" của mình, Po phải đối mặt với hai mối đe dọa cực kỳ hoành tráng, nhưng khác nhau: một siêu nhiên và một gần nhà hơn một chút.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/26_Kunfu%20panda%203.jpg', b'1', 'Gấu trúc Kungfu 3', 'Kungfu Panda 3', b'1', 'DreamWorks Animation', '2024-12-07', 'https://www.youtube.com/embed/10r9ozshGVE?si=TPOdwJv9R_9Ah0HG', 0, 7, b'1'),
	(27, 0, 'Sau khi Po được khai thác để trở thành Lãnh đạo tinh thần của Thung lũng Hòa bình, anh ta cần tìm và huấn luyện một Chiến binh Rồng mới, trong khi một phù thủy độc ác lên kế hoạch triệu hồi lại tất cả các nhân vật phản diện bậc thầy mà Po đã đánh bại đến cõi linh hồn.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/27_Kunfu%20panda%204.jpg', b'1', 'Gấu trúc Kung Fu 4', 'Kung Fu Panda 4', b'1', 'DreamWorks Animation', '2025-01-02', 'https://www.youtube.com/embed/_inKs4eeHiI?si=6uCVEmMln6MSk8uf', 0, 7, b'1'),
	(28, 3, 'Bộ phim lấy bối cảnh ở thị trấn ngầm dưới biển mang tên Bikini Bottom, nơi sinh sống của các nhân vật độc đáo và hài hước.  Nhân vật chính là SpongeBob SquarePants, một miếng bọt biển màu vàng với tính cách lạc quan, vui vẻ và đôi khi hơi ngớ ngẩn. Anh làm việc tại Krusty Krab, một nhà hàng phục vụ món bánh Krabby Patty nổi tiếng, dưới sự giám sát của ông chủ tham lam Mr. Krabs. SpongeBob thường xuyên phiêu lưu cùng người bạn thân Patrick Star, một chú sao biển tốt bụng nhưng thiếu hiểu biết, và phải đối mặt với Squidward Tentacles, người hàng xóm kiêm đồng nghiệp cáu kỉnh nhưng đầy tham vọng nghệ thuật.', 20, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/28_Spongebob.jpg', b'0', 'Chú bọt biển tinh nghịch phần 1', 'Spongebob Squarepants Season 1', b'1', 'Viacom International', '2024-12-15', 'https://www.youtube.com/embed/47ceXAEr2Oo?si=3k9JWZQuPRS-Pj9x', 0, 7, b'0'),
	(29, 0, 'SpongeBob SquarePants mùa 2 tiếp tục mang đến những cuộc phiêu lưu vui nhộn và sáng tạo của SpongeBob cùng những người bạn ở thị trấn Bikini Bottom. Phát sóng từ năm 2000 đến 2003, mùa này bao gồm 20 tập (40 phân đoạn), được đánh giá cao nhờ nội dung hài hước, cốt truyện độc đáo và sự phát triển của các nhân vật.', 20, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/29_Spongebob%202.jpg', b'0', 'Chú bọt biển tinh nghịch phần 2', 'SpongeBob SquarePants Season 2', b'0', 'Viacom International', '2024-12-07', 'https://www.youtube.com/embed/C4Ds7VYkp-I?si=E3EYAk0s73tIaITf', 0, 7, b'0'),
	(30, 3, 'Family Guy là một series phim hoạt hình hài hước dành cho người lớn, do Seth MacFarlane sáng tạo, tập trung vào những câu chuyện xoay quanh gia đình Griffin ở thị trấn Quahog, Rhode Island. Phim nổi bật với phong cách châm biếm sắc bén, các tình huống hài hước phi lý và những đoạn cắt cảnh ("cutaway") độc đáo, thường mang tính mỉa mai hoặc nhại lại văn hóa đại chúng.', 409, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/30_Family%20guy.jpg', b'0', 'Family Guy', 'Family Guy', b'0', 'Fox', '2024-12-12', 'https://www.youtube.com/embed/drWuVyvrOLU?si=pRzdNxbg4er25DWj', 0, 7, b'0'),
	(31, 4.5, 'Gru, một kẻ chủ mưu tội phạm, nhận nuôi ba đứa trẻ mồ côi làm con tốt để thực hiện vụ trộm lớn nhất trong lịch sử. Cuộc sống của anh ấy có một bước ngoặt bất ngờ khi các cô gái nhỏ coi kẻ ác là người cha tiềm năng của chúng.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/31_Despicable%20Me.jpg', b'1', 'Kẻ Cắp Mặt Trăng', 'Despicable Me', b'1', 'Illumination', '2024-12-14', 'https://www.youtube.com/embed/zzCZ1W_CUoI?si=VwPxXt7da7k02bDj', 0, 7, b'0'),
	(32, 3.7, 'Khi một tên tội phạm mới đến thị trấn, Liên đoàn Chống phản diện quyết định thuê Gru để chống lại anh ta. Trong khi Gru ban đầu từ chối, cuối cùng anh ta đồng ý sau khi phụ tá của anh ta quay trở lại con đường xấu xa của mình.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_Despicable%20Me%202.jpg', b'1', 'Kẻ Cắp Mặt Trăng 2', 'Despicable Me 2', b'1', 'Illumination', '2024-12-21', 'https://www.youtube.com/embed/TlbnGSMJQbQ?si=YjkJGQ4cdixkp82B', 0, 7, b'0'),
	(33, 0, 'Gru gặp người anh em sinh đôi Dru đã mất tích từ lâu, quyến rũ, vui vẻ và thành công hơn, người muốn hợp tác với anh ta trong một vụ trộm tội phạm cuối cùng.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_Despicable%20Me%203.jpg', b'1', 'Kẻ Cắp Mặt Trăng 3', ' Despicable Me 3', b'1', 'Illumination', '2025-01-01', 'https://www.youtube.com/embed/6DBi41reeF0?si=M0CjnzQLZIOQqRf_', 0, 7, b'1'),
	(34, 0, 'Gru, Lucy, Margo, Edith và Agnes chào đón một thành viên mới vào gia đình, Gru Jr., người có ý định hành hạ cha mình. Gru phải đối mặt với một kẻ thù mới là Maxime Le Mal và bạn gái Valentina, và gia đình buộc phải chạy trốn.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_Despicable%20Me%204.jpg', b'1', 'Kẻ Cắp Mặt Trăng 4', 'Despicable Me 4', b'1', 'Illumination', '2025-01-08', 'https://www.youtube.com/embed/qQlr9-rF32A?si=TF3FNurlqdQeDh68', 0, 7, b'1'),
	(35, 0, 'Khi chú chó yêu quý của một cậu bé đột ngột qua đời, anh ta cố gắng đưa con vật trở lại cuộc sống thông qua một thí nghiệm khoa học mạnh mẽ.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_Frankenweenie.jpg', b'1', 'Chó Ma Frankenweenie', 'Frankenweenie', b'1', 'Tim Burton Productions', '2024-12-10', 'https://www.youtube.com/embed/29vIJQohUWE?si=cIA2RlswP5dCA1xP', 0, 7, b'0'),
	(36, 3, 'Cuộc phiêu lưu của Prue McKeel khi cô lên đường đến những khu rừng bên ngoài Portland để tìm người anh trai bị bắt cóc của mình.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_Wildwood.jpg', b'1', 'Rừng Nguyên Sinh', 'Wildwood', b'1', 'LAIKA Studios', '2024-12-17', 'https://www.youtube.com/embed/Prv_FLWLlq8?si=SvsgwNsEctEf6SoH', 0, 7, b'1'),
	(37, 3, 'Trong phim Cô dâu ma của Tim Burton, hoạt hình stop-motion đan xen tuyệt vời với các yếu tố Gothic và siêu nhiên để tạo ra một câu chuyện lãng mạn đen tối khám phá các chủ đề về tình yêu, sự hy sinh và thế giới bên kia. Phong cách hoạt hình biểu cảm của bộ phim mang đến cuộc sống một bối cảnh Victoria chi tiết phong phú với cả người sống và xác sống, pha trộn liền mạch sự hay thay đổi và kinh dị theo cách mà chỉ Burton mới có thể đạt được. Với dàn nhân vật kỳ quặc đáng nhớ và cốt lõi là câu chuyện tình yêu buồn vui lẫn lộn, Cô dâu xác chết đi sâu vào những cảm xúc phức tạp thúc đẩy các nhân vật chính trong khi sử dụng bầu không khí ủ rũ để gợi lên cảm giác sợ hãi lan tỏa. Như một minh chứng cho sức mạnh của hoạt hình trong việc đi sâu vào các chủ đề đen tối hơn, bộ phim tự đứng vững như một ví dụ sáng giá về kinh dị Gothic trong phương tiện.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_corpse%20bride.jpg', b'1', 'Cô Dâu Ma', 'Corpse Bride', b'1', 'Tim Burton Productions', '2024-12-20', 'https://www.youtube.com/embed/AGACeWVdFqo?si=UGoUG-Bd5oq0PAsZ', 0, 7, b'1'),
	(38, 2.5, 'Monster House khéo léo cân bằng giữa kinh dị và hài hước trong khuôn khổ hoạt hình của nó, sử dụng hình ảnh do máy tính tạo ra để thổi hồn vào một câu chuyện vừa đáng sợ vừa ấm áp. Thông qua phong cách hoạt hình sáng tạo và cách kể chuyện bằng hình ảnh sáng tạo, bộ phim tạo ra một thế giới vừa quen thuộc vừa kỳ lạ, chứa đựng các nhân vật thể hiện chiều sâu và phức tạp thường không thấy trong phim hoạt hình kinh dị. Bằng cách đề cập đến các chủ đề về nỗi sợ hãi thời thơ ấu, tình bạn và tầm quan trọng của việc đối đầu với những con quỷ của một người, Monster House vượt qua tiền đề ban đầu của nó để mang đến một câu chuyện sâu sắc, ly kỳ gây được tiếng vang với người xem rất lâu sau khi kết thúc. Đóng vai trò là một ví dụ sáng giá về cách hoạt hình có thể giải quyết kinh dị trong khi vẫn duy trì cảm giác ấm áp và hài hước, Monster House là một viên ngọc quý thực sự trong thể loại này.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_monster%20house.jpg', b'1', 'Ngôi Nhà Ma Quái', 'Monster House', b'1', 'ImageMovers, Columbia Pictures, Sony Pictures Animation', '2024-12-15', 'https://www.youtube.com/embed/yB9vThNAIjs?si=Rx369ypacuhxoXaO', 0, 7, b'0'),
	(39, 0, 'ParaNorman chứng minh cách hoạt hình và kinh dị có thể bổ sung cho nhau trong việc tạo ra một câu chuyện ly kỳ, hấp dẫn về mặt cảm xúc, mang lại nhiều thứ hơn là chỉ đáng sợ. Laika Studios sử dụng một cách thành thạo hoạt hình stop-motion để đưa câu chuyện về một cậu bé có khả năng nói chuyện với người chết trở nên sống động, sử dụng hình ảnh phức tạp, cách điệu để khắc họa một thế giới vừa hay thay đổi vừa đáng sợ. Các nhân vật được phát triển phong phú của bộ phim và các chủ đề phức tạp về mặt đạo đức về sự chấp nhận, tha thứ và đối mặt với nỗi sợ hãi của một người đã nâng nó vượt ra ngoài lĩnh vực kinh dị điển hình, khiến nó trở thành một ví dụ thực sự nổi bật về thể loại hoạt hình. ParaNorman là minh chứng cho tính linh hoạt của phương tiện này, cho thấy cách hoạt hình có thể được sử dụng để dệt nên một câu chuyện sâu sắc, đáng sợ và mê hoặc cùng một lúc.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_ParaNorman.jpg', b'1', 'ParaNorman', 'Cậu Bé Norman', b'1', 'LAIKA Studios', '2024-12-11', 'https://www.youtube.com/embed/hgwSpajMw3s?si=aUDvgrWqE5fD6HXP', 0, 7, b'1'),
	(40, 4, 'Lấy bối cảnh trong một tầm nhìn giàu trí tưởng tượng về Địa ngục, loạt phim hài đen tối này có hoạt hình sống động, các nhân vật khó quên và sự hài hước thông minh sẽ thu hút những khán giả trưởng thành đang tìm kiếm một bước ngoặt sáng tạo về truyền thuyết siêu nhiên.', 1, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_Hazbin%20Hotel.jpg', b'1', 'Khách Sạn Hazbin', 'Hazbin Hotel', b'1', 'A24 Studio', '2024-12-15', 'https://www.youtube.com/embed/OLSWVCwy88g?si=4Zl5UdMe9n8CABqa', 0, 7, b'0'),
	(41, 2.8, 'Hổ Tử và Kỳ Hiểu Hiên ban đầu không hợp nhay đã chuyển lên thân thiết tại môt đội hình đặc biệt Hổ Hạc. Họ liên thủ với một đám bạn ngốc nghếch và háu ăn, tạo ra một đội quân đầy hài hước, độc đáo nhưng không giảm sức mạnh mẽ.  Họ đồng lòng phá bỏ âm mưu của yêu sư, trừ ma vệ đạo và giữ vững bình yên giữa thiên đàng và hỏa ngục. hành trình của họ vì một thế giới ngập tràn sự đoàn kết và chính nghĩa.', 13, 'https://imagesmovie.s3.ap-southeast-2.amazonaws.com/null_ho-hac-yeu-su-luc-3d.webp', b'0', 'Hổ Hạc Yêu Sư Lục', ' Hu He Yao Shi Lu', b'0', 'CN Animation', '2024-12-10', 'https://www.youtube.com/embed/xrht8_orGcM?si=fVnG-CpQJtrQTwFY', 9, 2, b'0');

-- Dumping structure for table moviecartoon.payment_history
CREATE TABLE IF NOT EXISTS `payment_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `money` double NOT NULL,
  `payment_date` datetime(6) NOT NULL,
  `payment_info` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt8sfsadt33419efta06p5c4xi` (`user_id`),
  CONSTRAINT `FKt8sfsadt33419efta06p5c4xi` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.payment_history: ~10 rows (approximately)
DELETE FROM `payment_history`;
INSERT INTO `payment_history` (`id`, `money`, `payment_date`, `payment_info`, `user_id`) VALUES
	(1, 109000, '2024-12-14 20:06:21.885151', 'Goi VIP 1 thang', 14),
	(2, 249000, '2024-12-15 10:24:26.830764', 'Goi VIP 3 thang', 7),
	(3, 990000, '2024-12-15 12:23:38.399912', 'Goi VIP 12 thang', 14),
	(4, 249000, '2024-12-15 13:23:42.949474', 'Goi VIP 3 thang', 7),
	(5, 109000, '2024-12-17 20:26:38.531387', 'Goi VIP 1 thang', 14),
	(6, 990000, '2024-12-19 10:53:24.519999', 'Goi VIP 12 thang', 44),
	(7, 990000, '2024-12-21 13:00:45.667934', 'Goi VIP 12 thang', 45),
	(8, 109000, '2024-12-21 13:05:15.050963', 'Goi VIP 1 thang', 46),
	(9, 249000, '2024-12-21 18:45:29.124767', 'Goi VIP 3 thang', 47),
	(10, 249000, '2024-12-21 19:05:06.799879', 'Goi VIP 3 thang', 14);

-- Dumping structure for table moviecartoon.recomment
CREATE TABLE IF NOT EXISTS `recomment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2rsl238byaa03ujkdhcq1kbra` (`user_id`),
  CONSTRAINT `FK2rsl238byaa03ujkdhcq1kbra` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.recomment: ~12 rows (approximately)
DELETE FROM `recomment`;
INSERT INTO `recomment` (`id`, `name`, `user_id`) VALUES
	(19, 'user', 41),
	(21, 'Nguyễn Nhật Trịnh', 33),
	(22, 'user', 42),
	(23, 'Danh sách mới 2', 42),
	(24, 'Con emo', 7),
	(27, 'user', 14),
	(28, 'Công nghệ thông tin', 14),
	(33, 'Minions', 46),
	(34, 'Phim hài', 46),
	(35, 'Danh sach chinese', 47),
	(36, 'Danh sách mới', 48),
	(38, 'admin 1', 47);

-- Dumping structure for table moviecartoon.role
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(250) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.role: ~2 rows (approximately)
DELETE FROM `role`;
INSERT INTO `role` (`id`, `description`, `name`) VALUES
	(1, NULL, 'ADMIN'),
	(2, NULL, 'USER');

-- Dumping structure for table moviecartoon.theloai_movie
CREATE TABLE IF NOT EXISTS `theloai_movie` (
  `movie_id` bigint NOT NULL,
  `theloai_id` bigint NOT NULL,
  PRIMARY KEY (`movie_id`,`theloai_id`),
  KEY `FK97e4731p20d2rcsbrymxh3x1t` (`theloai_id`),
  CONSTRAINT `FK97e4731p20d2rcsbrymxh3x1t` FOREIGN KEY (`theloai_id`) REFERENCES `genre` (`genre_id`),
  CONSTRAINT `FKt2gxw3jjno1hammvf8y7btlnf` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`movie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.theloai_movie: ~113 rows (approximately)
DELETE FROM `theloai_movie`;
INSERT INTO `theloai_movie` (`movie_id`, `theloai_id`) VALUES
	(1, 3),
	(4, 3),
	(6, 3),
	(8, 3),
	(9, 3),
	(10, 3),
	(12, 3),
	(13, 3),
	(14, 3),
	(21, 3),
	(41, 3),
	(1, 6),
	(7, 6),
	(30, 6),
	(37, 6),
	(40, 6),
	(1, 7),
	(13, 7),
	(14, 7),
	(21, 7),
	(23, 7),
	(24, 7),
	(25, 7),
	(26, 7),
	(27, 7),
	(29, 7),
	(30, 7),
	(31, 7),
	(32, 7),
	(33, 7),
	(34, 7),
	(35, 7),
	(41, 7),
	(1, 8),
	(7, 8),
	(13, 8),
	(21, 8),
	(22, 8),
	(23, 8),
	(24, 8),
	(25, 8),
	(26, 8),
	(27, 8),
	(28, 8),
	(29, 8),
	(30, 8),
	(31, 8),
	(32, 8),
	(33, 8),
	(34, 8),
	(36, 8),
	(38, 8),
	(39, 8),
	(41, 8),
	(1, 9),
	(14, 9),
	(21, 9),
	(22, 9),
	(23, 9),
	(24, 9),
	(25, 9),
	(26, 9),
	(27, 9),
	(28, 9),
	(29, 9),
	(30, 9),
	(31, 9),
	(32, 9),
	(33, 9),
	(34, 9),
	(38, 9),
	(39, 9),
	(40, 9),
	(7, 10),
	(35, 10),
	(36, 10),
	(37, 10),
	(38, 10),
	(39, 10),
	(1, 11),
	(14, 11),
	(22, 11),
	(23, 11),
	(24, 11),
	(30, 11),
	(35, 11),
	(36, 11),
	(37, 11),
	(38, 11),
	(40, 11),
	(7, 14),
	(21, 14),
	(24, 14),
	(25, 14),
	(26, 14),
	(27, 14),
	(28, 14),
	(29, 14),
	(30, 14),
	(31, 14),
	(32, 14),
	(33, 14),
	(34, 14),
	(22, 15),
	(28, 15),
	(29, 15),
	(31, 15),
	(32, 15),
	(33, 15),
	(36, 15),
	(41, 15),
	(22, 16),
	(23, 16),
	(30, 16),
	(35, 16),
	(38, 16),
	(39, 16),
	(40, 16);

-- Dumping structure for table moviecartoon.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT NULL,
  `is_locked` bit(1) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `password` varchar(250) NOT NULL,
  `phone` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tuoi` int NOT NULL,
  `username` varchar(50) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `is_oauth` bit(1) DEFAULT NULL,
  `premium_duration` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.user: ~15 rows (approximately)
DELETE FROM `user`;
INSERT INTO `user` (`id`, `email`, `is_locked`, `name`, `password`, `phone`, `tuoi`, `username`, `image`, `is_oauth`, `premium_duration`) VALUES
	(7, 'xinpro223@gmail.com', b'0', 'user', '$2a$10$ndXzJ4PbX2XPBZGr54kX9.K3y3mX4Hss1s.KMozTzCuYMrn6vFvNy', '0346030360', 28, 'admin1', '/images/f4b330aa-f0d1-4126-97fe-4a154a72beab_worry punch.gif', b'0', '2025-03-15 13:23:42.949474'),
	(8, 'xinpro121@gmail.com', b'0', 'Nguyễn Nhật Trịnh', '$2a$10$5LmwB7/8yYoEAhgxVhG3duMdyBxNmt6hwagqDP5k5dOzmUbjHu3Ji', '0346030360', 18, 'user2', '/images/ef731650-87bb-4afa-a464-55448eef22ec_dan-dao-chi-ton-300x450.jpeg', b'0', '2024-12-14 20:03:13.000000'),
	(14, 'xinpro344@gmail.com', b'0', 'Nguyễn Nhật Trịnh', '$2a$10$tTAGLR6dPEA0D52.EQnX8uWltIDbQ/U.cU5XWhURjnqkHAXdVwkOS', '0346030360', 23, 'admin', '/images/936b07d1-9db7-41d5-9d45-75af8c0f479b_worry wave.gif', b'0', '2025-05-14 20:03:13.000000'),
	(33, 'xinpro232@gmail.com', b'0', '8143_Nguyễn Nhật Trịnh', '$2a$10$XYvwzZCNNLy6tt97MJALSOP.Oc0wZikAZBfcYfuptnhhqyMI999yO', '0123456789', 0, 'xinpro232@gmail.com', '/images/ecef7375-8542-46a2-aa0c-7c82f9f63101_bach-xa-duyen-khoi-616-3-300x450.jpg', b'1', '2024-12-14 20:03:13.000000'),
	(34, 'xinpro348@gmail.com', b'0', 'nguyen huynh', '$2a$10$SSlPSfBzokrYqK5Cm4vJ4uHZNLuaWvGzCLbRWj9Sa601CZybX51dW', '0123456789', 18, 'xinpro348@gmail.com', '/images/3d8d7954-45fa-4d66-bc76-ff58e1f7980a_bach-xa-duyen-khoi-616-3-300x450.jpg', b'1', '2024-12-14 20:03:14.000000'),
	(41, '', b'0', 'Vkko Xin', '$2a$10$u5GiifefkIFrNjOU2NmVfOlJ4mPs/3bia9fwMGUiyM/6aSbKQHY/C', '0123456789', 0, 'xinpro121@gmail.com', '/images/5cc98602-1bcd-4e69-858d-41bc481d1907_bach-luyen-thanh-than-3-300x450.jpg', b'1', '0001-01-01 00:00:00.000000'),
	(42, '', b'0', 'Nhật Trịnh Nguyễn', '$2a$10$If/C4l4.SQZTbSZIsqlr0erLKHo.qeqkVVGiwxYqwoDwrsKUka9Am', '0123456789', 0, 'nhattrinhnguyen1601@gmail.com', '/images/23123f1e-4831-44c7-8f0a-f3995894c712_dxtyehlevro9.GIF', b'1', '0001-01-01 00:00:00.000000'),
	(44, 'trungnguyenduc12@gmail.com', b'0', 'Nguyễn Đức Trung', '$2a$10$h/oc89LU3fJhM1/uDeZdxOJDA0XOB6vs9vFxBu9KVgugYBphmop8W', '0768947003', 21, 'TrungAdmin1', '/images/4372a93f-18d6-4f54-97d5-25669bb56c13_worry-fight.gif', b'0', '2025-12-19 10:53:24.519999'),
	(45, 'trungnguyenduc13@gmail.com', b'0', 'Nguyễn Đức Trung', '$2a$10$YrH7mYnGgtydDIkTm5mJ6OsHiUYsKjdhXn5tigACGLOScjV.5xfgG', '0768947004', 15, 'TrungAdmin2', '/images/ecfe32bc-dde6-4caa-b676-5fb2981aceac_dxtyehlevro9.GIF', b'0', '2025-12-21 13:00:45.667934'),
	(46, 'trungnguyenduc20033@gmail.com', b'0', 'Nguyễn Đức Trung', '$2a$10$X3FSCqg2iaawRLY8/aU5xea2MNw46DqcK5J6YrMDQOvWr6eCHOTl.', '0765160058', 16, 'TrungUser1', '/images/01455028-8b04-4be2-a45c-09b7f52e8207_worry frog walk away.gif', b'0', '2025-01-21 13:05:15.050963'),
	(47, '', b'0', 'Nguyễn Nhật Trịnh', '$2a$10$CQuTaJPUtIdsRj9ROQS3UOq52DgwLsW45mXxRvO6x0SzsU4G1oMYW', '0346030360', 0, 'xinpro344@gmail.com', '/images/4c0b9c5e-42e3-4ba1-9a3b-1d82e9f71acd_worry frog walk away.gif', b'1', '2025-03-21 18:45:29.124767'),
	(48, '', b'0', 'Xinh Diem', '$2a$10$uIMm4VAwRMG5m3IbDplxy.kj81jblH18vPr4gAIhyaGezl/6MosQK', '0123456789', 0, 'xinpro223@gmail.com', '/staticImg/anh-mau-den-1.jpg', b'1', '0001-01-01 00:00:00.000000'),
	(49, 'xinpro111@gmail.com', b'0', 'Nguyen Nhat Trinh', '$2a$10$GQSMe8YU2QNG96BH7Ta9h.A4UIAp2mDOuhgo04WepjffxXpqHugEG', '0346030630', 12, 'user', NULL, b'0', '0001-01-01 00:00:00.000000'),
	(50, 'xinpro@gmail.com', b'0', 'NguyenNhatTrinh', '$2a$10$ijDH/xIIzEuUWpN7XNzfF.BqB05lMvxZLgjqPzPApONLwHrBOurPC', '0346030630', 18, 'user1', NULL, b'0', '0001-01-01 00:00:00.000000'),
	(51, 'xinproo@gmail.com', b'0', 'Nguyen Nhat Trinh', '$2a$10$pN/LMRt4mAFmELle.xWW5.F1tcm1fCJD0820ZgeTHwVRigiTGDugy', '0346030630', 24, 'user3', NULL, b'0', '0001-01-01 00:00:00.000000');

-- Dumping structure for table moviecartoon.user_role
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table moviecartoon.user_role: ~19 rows (approximately)
DELETE FROM `user_role`;
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
	(33, 2),
	(34, 2),
	(41, 2),
	(8, 2),
	(44, 1),
	(45, 1),
	(46, 2),
	(33, 2),
	(34, 2),
	(41, 2),
	(8, 2),
	(44, 1),
	(45, 1),
	(46, 2),
	(7, 1),
	(42, 2),
	(48, 2),
	(47, 2),
	(14, 1),
	(49, 2),
	(50, 2),
	(51, 2);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
