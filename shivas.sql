/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50520
Source Host           : localhost:3306
Source Database       : shivas

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2012-05-26 11:32:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `accounts`
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `question` varchar(255) NOT NULL,
  `answer` varchar(255) NOT NULL,
  `rights` tinyint(1) NOT NULL,
  `banned` tinyint(1) NOT NULL,
  `muted` tinyint(1) NOT NULL,
  `community` int(11) NOT NULL,
  `points` int(11) NOT NULL,
  `subscription_end` datetime NOT NULL,
  `connected` tinyint(1) NOT NULL,
  `channels` varchar(255) NOT NULL DEFAULT 'i*#$p%',
  `last_connection` datetime NOT NULL,
  `last_address` varchar(15) NOT NULL,
  `nb_connections` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `accounts_name_index` (`name`) USING BTREE,
  UNIQUE KEY `accounts_nickname_index` (`nickname`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of accounts
-- ----------------------------
INSERT INTO `accounts` VALUES ('1', 'test', 'a94a8fe5ccb19ba61c4c0873d391e987982fbbd3', '[TEST]Blackrush', 'DELETE?', 'DELETE', '1', '0', '0', '0', '0', '2013-05-19 13:43:59', '0', '!#$%:?i@*', '2012-05-19 17:50:38', '127.0.0.1', '8');
INSERT INTO `accounts` VALUES ('2', 'test2', 'a94a8fe5ccb19ba61c4c0873d391e987982fbbd3', '[TEST]Blackrush2', 'DELETE?', 'DELETE', '0', '0', '0', '0', '0', '2012-05-19 17:27:07', '0', 'i*#$p%:', '2012-05-19 20:31:16', '127.0.0.1', '13');

-- ----------------------------
-- Table structure for `items`
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items` (
  `id` bigint(20) NOT NULL,
  `template` int(11) NOT NULL,
  `owner` int(11) NOT NULL,
  `effects` text NOT NULL,
  `position` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_items_owner` (`owner`) USING BTREE,
  CONSTRAINT `fk_items_owner` FOREIGN KEY (`owner`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of items
-- ----------------------------

-- ----------------------------
-- Table structure for `players`
-- ----------------------------
DROP TABLE IF EXISTS `players`;
CREATE TABLE `players` (
  `id` int(11) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `breed_id` int(11) NOT NULL,
  `gender` tinyint(1) NOT NULL,
  `skin` smallint(6) NOT NULL,
  `size` smallint(6) NOT NULL,
  `color1` int(11) NOT NULL,
  `color2` int(11) NOT NULL,
  `color3` int(11) NOT NULL,
  `level` smallint(6) NOT NULL,
  `experience` bigint(20) NOT NULL,
  `kamas` bigint(20) NOT NULL,
  `map_id` int(11) NOT NULL,
  `cell` smallint(6) NOT NULL,
  `orientation` tinyint(4) NOT NULL,
  `stat_points` smallint(6) NOT NULL,
  `spell_points` smallint(6) NOT NULL,
  `energy` int(11) NOT NULL,
  `life` int(11) NOT NULL,
  `action_points` smallint(6) NOT NULL,
  `movement_points` smallint(6) NOT NULL,
  `vitality` smallint(6) NOT NULL,
  `wisdom` smallint(6) NOT NULL,
  `strength` smallint(6) NOT NULL,
  `intelligence` smallint(6) NOT NULL,
  `chance` smallint(6) NOT NULL,
  `agility` smallint(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `players_name_index` (`name`) USING BTREE,
  KEY `players_owner_id_fk` (`owner_id`),
  CONSTRAINT `players_owner_id_fk` FOREIGN KEY (`owner_id`) REFERENCES `accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of players
-- ----------------------------
INSERT INTO `players` VALUES ('1', '1', 'Vjya', '9', '0', '90', '100', '-1', '-1', '-1', '200', '7407232000', '0', '7411', '355', '1', '995', '199', '10000', '44', '6', '3', '0', '0', '0', '0', '0', '0');
INSERT INTO `players` VALUES ('2', '2', 'Oqsyk', '8', '0', '80', '100', '-1', '-1', '-1', '200', '7407232000', '0', '7411', '355', '1', '995', '199', '10000', '48', '6', '3', '0', '0', '1', '0', '0', '0');
