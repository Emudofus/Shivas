/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50520
Source Host           : localhost:3306
Source Database       : shivas

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2012-08-10 14:42:34
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for `contacts`
-- ----------------------------
DROP TABLE IF EXISTS `contacts`;
CREATE TABLE `contacts` (
  `id` bigint(20) NOT NULL,
  `owner` int(11) NOT NULL,
  `target` int(11) NOT NULL,
  `type` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_contact_owner_to_account` (`owner`),
  KEY `fk_contact_target_to_account` (`target`),
  CONSTRAINT `fk_contact_target_to_account` FOREIGN KEY (`target`) REFERENCES `accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_contact_owner_to_account` FOREIGN KEY (`owner`) REFERENCES `accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
-- Table structure for `spells`
-- ----------------------------
DROP TABLE IF EXISTS `spells`;
CREATE TABLE `spells` (
  `id` bigint(20) NOT NULL,
  `player` int(11) NOT NULL,
  `spell` smallint(6) NOT NULL,
  `level` tinyint(4) NOT NULL,
  `position` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_spells_player` (`player`),
  CONSTRAINT `fk_spells_player` FOREIGN KEY (`player`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
