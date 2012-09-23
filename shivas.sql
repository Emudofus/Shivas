/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50524
Source Host           : localhost:3306
Source Database       : shivas_prod

Target Server Type    : MYSQL
Target Server Version : 50524
File Encoding         : 65001

Date: 2012-09-23 16:20:58
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
  `salt` varchar(32) NOT NULL,
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
  `refreshed` tinyint(1) NOT NULL,
  `friend_notification_listener` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `accounts_name_index` (`name`) USING BTREE,
  UNIQUE KEY `accounts_nickname_index` (`nickname`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of accounts
-- ----------------------------

-- ----------------------------
-- Table structure for `contacts`
-- ----------------------------
DROP TABLE IF EXISTS `contacts`;
CREATE TABLE `contacts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner` int(11) NOT NULL,
  `target` int(11) NOT NULL,
  `type` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_contact_owner_to_account` (`owner`),
  KEY `fk_contact_target_to_account` (`target`),
  CONSTRAINT `fk_contact_owner_to_account` FOREIGN KEY (`owner`) REFERENCES `accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_contact_target_to_account` FOREIGN KEY (`target`) REFERENCES `accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of contacts
-- ----------------------------

-- ----------------------------
-- Table structure for `gifts`
-- ----------------------------
DROP TABLE IF EXISTS `gifts`;
CREATE TABLE `gifts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner` int(11) NOT NULL,
  `item` int(11) NOT NULL,
  `quantity` smallint(6) NOT NULL,
  `title` varchar(100) NOT NULL,
  `message` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_gift_owner` (`owner`),
  CONSTRAINT `gifts_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `accounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of gifts
-- ----------------------------

-- ----------------------------
-- Table structure for `guilds`
-- ----------------------------
DROP TABLE IF EXISTS `guilds`;
CREATE TABLE `guilds` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `leader_id` int(11) NOT NULL,
  `emblem_background_id` int(11) NOT NULL,
  `emblem_background_color` int(11) NOT NULL,
  `emblem_foreground_id` int(11) NOT NULL,
  `emblem_foreground_color` int(11) NOT NULL,
  `level` smallint(6) NOT NULL,
  `experience` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_guilds_name` (`name`) USING BTREE,
  KEY `fk_leader_guilds` (`leader_id`),
  CONSTRAINT `fk_leader_guilds` FOREIGN KEY (`leader_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of guilds
-- ----------------------------

-- ----------------------------
-- Table structure for `guild_members`
-- ----------------------------
DROP TABLE IF EXISTS `guild_members`;
CREATE TABLE `guild_members` (
  `id` bigint(20) NOT NULL,
  `guild_id` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_guild_guild_members` (`guild_id`),
  KEY `fk_player_guild_members` (`player_id`),
  CONSTRAINT `fk_guild_guild_members` FOREIGN KEY (`guild_id`) REFERENCES `guilds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_player_guild_members` FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of guild_members
-- ----------------------------

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
  `saved_map_id` int(11) NOT NULL,
  `saved_cell` smallint(6) NOT NULL,
  `waypoints` varchar(255) NOT NULL,
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

-- ----------------------------
-- Records of spells
-- ----------------------------

-- ----------------------------
-- Table structure for `stored_items`
-- ----------------------------
DROP TABLE IF EXISTS `stored_items`;
CREATE TABLE `stored_items` (
  `id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_stored_items_id` FOREIGN KEY (`id`) REFERENCES `items` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of stored_items
-- ----------------------------
