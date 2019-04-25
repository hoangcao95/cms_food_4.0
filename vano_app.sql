/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE IF NOT EXISTS `vano_app` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `vano_app`;

CREATE TABLE IF NOT EXISTS `auth_module` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL DEFAULT '',
  `class` varchar(256) DEFAULT NULL,
  `desc` varchar(512) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `auth_module` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_module` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `auth_perm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `display_name` varchar(256) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `module_id` bigint(11) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `auth_perm` DISABLE KEYS */;
INSERT INTO `auth_perm` (`id`, `name`, `display_name`, `description`, `module_id`, `status`) VALUES
	(1, 'CREATE', 'Quyền thêm mới', NULL, NULL, 1),
	(2, 'READ', 'Quyền đọc', NULL, NULL, 1),
	(3, 'UPDATE', 'Quyền chỉnh sửa', NULL, NULL, 1),
	(4, 'DELETE', 'Quyền xóa', NULL, NULL, 1),
	(5, 'EXECUTE', 'Quyền thực thi', NULL, NULL, 1);
/*!40000 ALTER TABLE `auth_perm` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `auth_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `display_name` varchar(256) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `status` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `auth_role` DISABLE KEYS */;
INSERT INTO `auth_role` (`id`, `name`, `display_name`, `description`, `status`) VALUES
	(3, 'REPORT', 'Báo cáo', 'Báo cáo', 1),
	(4, 'CSKH', 'CSKH', 'Chăm sóc khách hàng', 1),
	(5, 'ADMIN', 'Admin', 'Admin', 1),
	(6, 'SADMIN', 'Supper Admin', 'Supper Admin', 1),
	(7, 'TOPUP_ADMIN', 'Topup Admin', 'Topup Admin', 1),
	(8, 'TOPUP_CSKH', 'Topup CSKH', 'Topup CSKH', 1);
/*!40000 ALTER TABLE `auth_role` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `auth_role_perm` (
  `role_id` int(11) NOT NULL,
  `perm_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`perm_id`),
  KEY `FK_RP_PERM` (`perm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `auth_role_perm` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_role_perm` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `auth_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) DEFAULT NULL,
  `first_name` varchar(15) DEFAULT NULL,
  `middle_name` varchar(15) DEFAULT NULL,
  `last_name` varchar(15) DEFAULT NULL,
  `full_name` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `gender` varchar(1) DEFAULT NULL,
  `salt` varchar(45) DEFAULT NULL,
  `password` varchar(128) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `is_verified` tinyint(4) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `user_type` tinyint(4) DEFAULT NULL COMMENT '0 -  system user; 1 - ; 2- affiliate; 4-sale staff',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `auth_user` DISABLE KEYS */;
INSERT INTO `auth_user` (`id`, `user_name`, `first_name`, `middle_name`, `last_name`, `full_name`, `email`, `gender`, `salt`, `password`, `created_date`, `modified_date`, `is_verified`, `status`, `user_type`) VALUES
	(1, 'admin', '', '', '', 'Administrators', 'admin@yo.com', '0', '5876695f8e4e1811', '$2a$10$1XV3cAK00ZiyT5f4dxQ1keyMSBme2NRIzLs4W2jIlk3Cir7jeK2aC', '2018-07-17 15:05:55', NULL, 1, 1, 1),
	(2, 'hoang', NULL, NULL, NULL, 'Cao Hoàng', 'abc@gmail.com', '1', '5876695f8e4e1811', '$2a$10$1XV3cAK00ZiyT5f4dxQ1keyMSBme2NRIzLs4W2jIlk3Cir7jeK2aC', '2019-01-04 09:48:33', NULL, 0, 0, 0),
	(3, 'hienhv', NULL, NULL, NULL, 'Hoàng Hiển', 'hienhv@vano.vn', '1', '5876695f8e4e1811', '$2a$10$1XV3cAK00ZiyT5f4dxQ1keyMSBme2NRIzLs4W2jIlk3Cir7jeK2aC', '2019-01-04 10:25:01', NULL, 0, 1, 0),
	(4, 'vano_cskh', NULL, NULL, NULL, 'CSKH Vano', 'cskh@vano.vn', '1', '5876695f8e4e1811', '$2a$10$1XV3cAK00ZiyT5f4dxQ1keyMSBme2NRIzLs4W2jIlk3Cir7jeK2aC', '2019-01-11 14:04:34', '2019-01-11 14:04:34', 1, 1, 0),
	(8, 'demo2', NULL, NULL, NULL, 'demo2', 'demo2@gmail.com', NULL, '5876695f8e4e1811', '$2a$10$Qu/gTFYzd932Gufs5mZJm..HyeVvB2eKQ46ThRPWEbEQ2Hk3bqtJe', NULL, NULL, 0, 1, 0),
	(14, 'hoang123', NULL, NULL, NULL, 'hoang123', 'hoang@gmail.com', '1', '5876695f8e4e1811', '$2a$10$A1cLlPj1eUGiqlvSW1/SPeph1897dmObCMuNnOmJfMxGkFMW4wmPu', NULL, NULL, 0, 0, 0),
	(17, 'hoang1', NULL, NULL, NULL, 'hoang1', 'hoang1@gmail.com', '1', '5876695f8e4e1811', '$2a$10$KrWiMBkjdMqoiKwJsADAL.QBuUYPusgK36Otis4388TAo77GEqnOi', NULL, NULL, 0, 0, 0),
	(24, 'hoang2', NULL, NULL, NULL, 'hoang1', 'hoang1@gmail.com', '1', '5876695f8e4e1811', '$2a$10$KrWiMBkjdMqoiKwJsADAL.QBuUYPusgK36Otis4388TAo77GEqnOi', NULL, NULL, 0, 0, 0),
	(25, 'hoang3', NULL, NULL, NULL, 'hoang1', 'hoang1@gmail.com', '1', '5876695f8e4e1811', '$2a$10$KrWiMBkjdMqoiKwJsADAL.QBuUYPusgK36Otis4388TAo77GEqnOi', NULL, NULL, 0, 0, 0),
	(26, 'hoang4', NULL, NULL, NULL, 'hoang1', 'hoang1@gmail.com', '1', '5876695f8e4e1811', '$2a$10$KrWiMBkjdMqoiKwJsADAL.QBuUYPusgK36Otis4388TAo77GEqnOi', NULL, NULL, 0, 0, 0),
	(28, 'hoang5', NULL, NULL, NULL, 'hoang1', 'hoang1@gmail.com', '1', '5876695f8e4e1811', '$2a$10$KrWiMBkjdMqoiKwJsADAL.QBuUYPusgK36Otis4388TAo77GEqnOi', NULL, NULL, 0, 0, 0),
	(29, 'hoang6', NULL, NULL, NULL, 'hoang1', 'hoang1@gmail.com', '1', '5876695f8e4e1811', '$2a$10$KrWiMBkjdMqoiKwJsADAL.QBuUYPusgK36Otis4388TAo77GEqnOi', NULL, NULL, 0, 0, 0),
	(30, 'demo3', NULL, NULL, NULL, 'demo33', 'demo3@gmail.com', NULL, '5876695f8e4e1811', '$2a$10$i4LSvxeEvN5R/NtJN11bXeogl51ny7E7y0BpWD/0xeEt8Mnex.GUO', NULL, NULL, 0, 0, 0),
	(31, 'tesst', NULL, NULL, NULL, 'tesst', 'test123@vano.vn', NULL, '5876695f8e4e1811', '$2a$10$xlYEPpO.NYAtGn2ESCZ2..MRCSTn6foAPwQrL4punm/enmHFmRvGC', NULL, NULL, 0, 0, 0),
	(32, 'nhuten', NULL, NULL, NULL, 'Như ', 'nhuten@gmail.com', NULL, '5876695f8e4e1811', '$2a$10$jGnpR0G1FEEuYCQ.b0l4fugoHM5sthbAfsKZ84U7uNLDCB5khnIH2', NULL, NULL, 0, 0, 0),
	(33, 'hehe', NULL, NULL, NULL, 'hehehe', 'hehe@gmail.com', NULL, '5876695f8e4e1811', '$2a$10$UB/hPbzeq37IRGbRaqfyZeg..hNb9Aq.0ShX.AGMVxfTuVLkHgJ0y', NULL, NULL, 0, 0, 0),
	(34, 'haga', NULL, NULL, NULL, 'hahf', 'ha@gmail.com', NULL, '5876695f8e4e1811', '$2a$10$bGUKlo7WvXRr5w5UC3F0me7slPx6XNFUqapQHNthjxft4rlCTpS3u', NULL, NULL, 0, 0, 0),
	(35, 'sssss', NULL, NULL, NULL, 'dfsdfsdfsdfdsf', 'ddd@gmail.com', NULL, '5876695f8e4e1811', '$2a$10$JiQBfowFe0cp4nPklP/nEucfFmJxQjiNaHkiXe.83rMXUpBf99R/C', NULL, NULL, 0, 0, 0),
	(37, 'kaka', NULL, NULL, NULL, 'kakak', 'kaka@gmail.com', NULL, '5876695f8e4e1811', '$2a$10$/bI2VBGfcHsWyJ11MHPEtuOIUz8Xpxa.L7ZIbFScQ0tzWMTTT1ry.', NULL, NULL, 0, 1, 0);
/*!40000 ALTER TABLE `auth_user` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `auth_usermeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `meta_key` varchar(255) DEFAULT NULL,
  `meta_value` longtext,
  PRIMARY KEY (`id`),
  KEY `FK_UM_USER_idx` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `auth_usermeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_usermeta` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `auth_user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_UR_ROLE_idx` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `auth_user_role` DISABLE KEYS */;
INSERT INTO `auth_user_role` (`user_id`, `role_id`) VALUES
	(2, 3),
	(4, 4),
	(30, 4),
	(3, 5),
	(1, 6),
	(4, 7);
/*!40000 ALTER TABLE `auth_user_role` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_cdr_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(50) DEFAULT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL COMMENT 'Dau so dich vu',
  `group_code` varchar(50) DEFAULT NULL COMMENT 'Ma dich vu',
  `package_code` varchar(50) DEFAULT NULL COMMENT 'Goi cuoc',
  `reg_datetime` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian dang ky',
  `start_datetime` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian hieu luc',
  `end_datetime` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian huy goi',
  `expire_datetime` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian het lieu luc goi cuoc',
  `status` varchar(20) DEFAULT NULL COMMENT '0: Gia han, 1: Dang ky, 3: Huy',
  `channel` varchar(50) DEFAULT NULL COMMENT 'Kenh thuc hien',
  `command` varchar(50) DEFAULT NULL COMMENT 'Cu phap thuc hien',
  `amount` int(11) DEFAULT NULL COMMENT 'So tien',
  `mt_content` text COMMENT 'MT gui ve khi DK, GH',
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `REG_DATETIME_IDX` (`reg_datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Bang luu log CDR tu cac nha mang';

/*!40000 ALTER TABLE `core_cdr_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_cdr_log` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_charge_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) NOT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL,
  `package_code` varchar(50) DEFAULT NULL COMMENT 'Ma goi cuoc',
  `action` varchar(50) DEFAULT NULL COMMENT 'Tac dong REG: Dang ky moi, EXTEND: Gia han, UNREG: Huy, RENEW: Dang ky lai, BUY: Tai le',
  `amount` int(11) DEFAULT NULL COMMENT 'Gia tien',
  `channel` varchar(50) DEFAULT NULL COMMENT 'Kenh charge cuoc',
  `created_date` date DEFAULT NULL COMMENT 'Ngay tao',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao',
  `call_result` int(1) DEFAULT NULL COMMENT 'Trang thai CCSP tra ve',
  `result_message` varchar(100) DEFAULT NULL COMMENT 'Thong tin CCSP tra ve',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT 'Trang thai',
  `cpid` varchar(50) DEFAULT NULL COMMENT 'Ma CPID',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu',
  `promo_id` bigint(20) DEFAULT NULL COMMENT 'Ma CTKM',
  `trans_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Bang luu thong tin log charge cuoc';

/*!40000 ALTER TABLE `core_charge_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_charge_log` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_charge_log_201903` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) NOT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL,
  `package_code` varchar(50) DEFAULT NULL COMMENT 'Ma goi cuoc',
  `action` varchar(50) DEFAULT NULL COMMENT 'Tac dong REG: Dang ky moi, EXTEND: Gia han, UNREG: Huy, RENEW: Dang ky lai, BUY: Tai le',
  `amount` int(11) DEFAULT NULL COMMENT 'Gia tien',
  `channel` varchar(50) DEFAULT NULL COMMENT 'Kenh charge cuoc',
  `created_date` date DEFAULT NULL COMMENT 'Ngay tao',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao',
  `call_result` int(1) DEFAULT NULL COMMENT 'Trang thai CCSP tra ve',
  `result_message` varchar(100) DEFAULT NULL COMMENT 'Thong tin CCSP tra ve',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT 'Trang thai',
  `cpid` varchar(50) DEFAULT NULL COMMENT 'Ma CPID',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu',
  `promo_id` bigint(20) DEFAULT NULL COMMENT 'Ma CTKM',
  `register_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Bang luu thong tin log charge cuoc';

/*!40000 ALTER TABLE `core_charge_log_201903` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_charge_log_201903` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_charge_log_201904` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) NOT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL,
  `package_code` varchar(50) DEFAULT NULL COMMENT 'Ma goi cuoc',
  `action` varchar(50) DEFAULT NULL COMMENT 'Tac dong REG: Dang ky moi, EXTEND: Gia han, UNREG: Huy, RENEW: Dang ky lai, BUY: Tai le',
  `amount` int(11) DEFAULT NULL COMMENT 'Gia tien',
  `channel` varchar(50) DEFAULT NULL COMMENT 'Kenh charge cuoc',
  `created_date` date DEFAULT NULL COMMENT 'Ngay tao',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao',
  `call_result` int(1) DEFAULT NULL COMMENT 'Trang thai CCSP tra ve',
  `result_message` varchar(100) DEFAULT NULL COMMENT 'Thong tin CCSP tra ve',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT 'Trang thai',
  `cpid` varchar(50) DEFAULT NULL COMMENT 'Ma CPID',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu',
  `promo_id` bigint(20) DEFAULT NULL COMMENT 'Ma CTKM',
  `register_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Bang luu thong tin log charge cuoc';

/*!40000 ALTER TABLE `core_charge_log_201904` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_charge_log_201904` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_mo_queue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(50) NOT NULL,
  `service_code` varchar(50) DEFAULT NULL,
  `command_code` varchar(50) DEFAULT NULL,
  `org_request` varchar(50) DEFAULT NULL,
  `channel` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `group_code` varchar(50) DEFAULT NULL,
  `package_code` varchar(50) DEFAULT NULL,
  `charge_price` varchar(50) DEFAULT NULL,
  `reg_datetime` varchar(50) DEFAULT NULL,
  `sta_datetime` varchar(50) DEFAULT NULL,
  `end_datetime` varchar(50) DEFAULT NULL,
  `expire_datetime` varchar(50) DEFAULT NULL,
  `message_send` text,
  `process_status` int(1) NOT NULL DEFAULT '0' COMMENT '0: Chua xu ly; 1: Da xu ly; 9: Loi',
  `note` varchar(20) DEFAULT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `trans_id` varchar(50) DEFAULT NULL,
  `keyword` varchar(50) DEFAULT NULL,
  `promo_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_mo_queue` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_mo_queue` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_mo_sms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) NOT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL COMMENT 'Dau so dich vu',
  `message` varchar(100) NOT NULL COMMENT 'Noi dung thue bao gui len da UPPER',
  `message_original` varchar(50) NOT NULL COMMENT 'Noi dung thue bao gui len nguyen ban',
  `command` varchar(50) DEFAULT NULL COMMENT 'Command xac dinh tac dong cua thue bao',
  `channel` varchar(50) NOT NULL COMMENT 'Kenh gui tin xuong',
  `created_date` date DEFAULT NULL COMMENT 'Thoi gian tao',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu neu co loi xay ra',
  `promo_id` bigint(20) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai tin nhan',
  `trans_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_mo_sms` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_mo_sms` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_mo_sms_201903` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) NOT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL COMMENT 'Dau so dich vu',
  `message` varchar(100) NOT NULL COMMENT 'Noi dung thue bao gui len da UPPER',
  `message_original` varchar(50) NOT NULL COMMENT 'Noi dung thue bao gui len nguyen ban',
  `command` varchar(50) DEFAULT NULL COMMENT 'Command xac dinh tac dong cua thue bao',
  `channel` varchar(50) NOT NULL COMMENT 'Kenh gui tin xuong',
  `created_date` date DEFAULT NULL COMMENT 'Thoi gian tao',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu neu co loi xay ra',
  `promo_id` bigint(20) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai tin nhan',
  `trans_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_mo_sms_201903` DISABLE KEYS */;
INSERT INTO `core_mo_sms_201903` (`id`, `msisdn`, `service_code`, `message`, `message_original`, `command`, `channel`, `created_date`, `created_time`, `note`, `promo_id`, `status`, `trans_id`) VALUES
	(37, '934538590', '9666', 'DK XX', 'dk xx', 'REG', 'SMS', '2019-03-29', '2019-03-29 13:31:07', NULL, 2, 1, 'H6uvPJYE1StV'),
	(38, '934538590', '9666', 'HUY XX', 'huy xx', 'CAN', 'SMS', '2019-03-29', '2019-03-29 13:31:11', NULL, 2, 1, '8QNVTNTpkiH5'),
	(39, '934538590', '9666', 'DK XX', 'dk xx', 'REG', 'SMS', '2019-03-29', '2019-03-29 13:31:21', NULL, 2, 1, 'JJSWaN4rtHPh'),
	(40, '934538590', '9666', 'DK XX', 'dk xx', 'REG', 'SMS', '2019-03-29', '2019-03-29 13:41:39', NULL, 2, 1, 'ikzcB76SchmN'),
	(41, '934538590', '9666', 'HUY XX', 'huy xx', 'CAN', 'SMS', '2019-03-29', '2019-03-29 13:41:39', NULL, 2, 1, '8glmXJJU4T6Z'),
	(42, '934538590', '9666', 'DK XX', 'dk xx', 'REG', 'SMS', '2019-03-29', '2019-03-29 13:41:58', NULL, 2, 1, 'W05lUuNHea6W');
/*!40000 ALTER TABLE `core_mo_sms_201903` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_mo_sms_201904` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) NOT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL COMMENT 'Dau so dich vu',
  `message` varchar(100) NOT NULL COMMENT 'Noi dung thue bao gui len da UPPER',
  `message_original` varchar(50) NOT NULL COMMENT 'Noi dung thue bao gui len nguyen ban',
  `command` varchar(50) DEFAULT NULL COMMENT 'Command xac dinh tac dong cua thue bao',
  `channel` varchar(50) NOT NULL COMMENT 'Kenh gui tin xuong',
  `created_date` date DEFAULT NULL COMMENT 'Thoi gian tao',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu neu co loi xay ra',
  `promo_id` bigint(20) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai tin nhan',
  `trans_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_mo_sms_201904` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_mo_sms_201904` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_mt_sms` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) NOT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL COMMENT 'Dau so dich vu',
  `message` varchar(100) DEFAULT NULL COMMENT 'Message gui di',
  `command` varchar(50) DEFAULT NULL COMMENT 'Command tach rieng tung loai message',
  `channel` varchar(50) NOT NULL COMMENT 'Kenh gui tin',
  `created_date` date DEFAULT NULL COMMENT 'Thoi gian tao',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mo_id` varchar(50) DEFAULT NULL COMMENT 'Id MO xu ly gui ban tin MT nay',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu neu co loi xay ra',
  `promo_id` bigint(20) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai tin',
  `trans_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_mt_sms` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_mt_sms` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_mt_sms_201903` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) NOT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL COMMENT 'Dau so dich vu',
  `message` varchar(100) DEFAULT NULL COMMENT 'Message gui di',
  `command` varchar(50) DEFAULT NULL COMMENT 'Command tach rieng tung loai message',
  `channel` varchar(50) NOT NULL COMMENT 'Kenh gui tin',
  `created_date` date DEFAULT NULL COMMENT 'Thoi gian tao',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mo_id` varchar(50) DEFAULT NULL COMMENT 'Id MO xu ly gui ban tin MT nay',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu neu co loi xay ra',
  `promo_id` bigint(20) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai tin',
  `trans_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_mt_sms_201903` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_mt_sms_201903` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_mt_sms_201904` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) NOT NULL COMMENT 'So thue bao',
  `service_code` varchar(50) DEFAULT NULL COMMENT 'Dau so dich vu',
  `message` varchar(100) DEFAULT NULL COMMENT 'Message gui di',
  `command` varchar(50) DEFAULT NULL COMMENT 'Command tach rieng tung loai message',
  `channel` varchar(50) NOT NULL COMMENT 'Kenh gui tin',
  `created_date` date DEFAULT NULL COMMENT 'Thoi gian tao',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `mo_id` varchar(50) DEFAULT NULL COMMENT 'Id MO xu ly gui ban tin MT nay',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu neu co loi xay ra',
  `promo_id` bigint(20) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai tin',
  `trans_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_mt_sms_201904` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_mt_sms_201904` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_package` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `code` varchar(50) NOT NULL COMMENT 'Ma goi cuoc',
  `name` varchar(100) DEFAULT NULL COMMENT 'Ten goi cuoc',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao',
  `note` varchar(100) DEFAULT NULL COMMENT 'Ghi chu',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT 'Trang thai bang',
  PRIMARY KEY (`id`),
  UNIQUE KEY `CODE_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Bang luu thong tin goi cuoc';

/*!40000 ALTER TABLE `core_package` DISABLE KEYS */;
INSERT INTO `core_package` (`id`, `code`, `name`, `created_date`, `note`, `status`) VALUES
	(1, 'XX', 'Gói XX', '2019-03-19 11:00:20', 'Gói XX', 1);
/*!40000 ALTER TABLE `core_package` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_promotion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `code` varchar(50) DEFAULT NULL COMMENT 'Ma CTKM',
  `name` varchar(100) DEFAULT NULL COMMENT 'Ten CTKM',
  `start_time` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian bat dau CTKM',
  `end_time` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian ket thuc CTKM',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao',
  `is_main` int(1) DEFAULT '0' COMMENT 'Neu =1 la CTKM chinh cua dich vu',
  `note` varchar(100) DEFAULT NULL COMMENT 'Ghi chu',
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai ban ghi',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Bang luu thong tin CTKM';

/*!40000 ALTER TABLE `core_promotion` DISABLE KEYS */;
INSERT INTO `core_promotion` (`id`, `code`, `name`, `start_time`, `end_time`, `created_date`, `is_main`, `note`, `status`) VALUES
	(1, 'CMTK1', 'Chương trình 1', '2019-03-01 00:00:00', NULL, '2019-03-19 11:27:45', 1, NULL, 1),
	(2, 'CTKM2', 'Chương trình 2', '2019-03-19 00:00:00', NULL, '2019-03-19 11:37:26', 0, NULL, 1);
/*!40000 ALTER TABLE `core_promotion` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_promotion_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `promo_id` bigint(20) DEFAULT NULL COMMENT 'Id CTKM',
  `sms_syntax_id` bigint(20) DEFAULT NULL COMMENT 'Id cu phap tin nhan',
  `sms_syntax` varchar(50) DEFAULT NULL COMMENT 'Cu phap tin nhan',
  `send_mt1` varchar(100) DEFAULT NULL COMMENT 'Link send MT qua API',
  `send_mt2` varchar(100) DEFAULT NULL COMMENT 'Link send MT qua API',
  `send_mt3` varchar(100) DEFAULT NULL COMMENT 'Link send MT qua API',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao',
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai ban ghi',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Bang luu thong tin CTKM bao gom nhung cu phap nhan tin nao';

/*!40000 ALTER TABLE `core_promotion_detail` DISABLE KEYS */;
INSERT INTO `core_promotion_detail` (`id`, `promo_id`, `sms_syntax_id`, `sms_syntax`, `send_mt1`, `send_mt2`, `send_mt3`, `created_date`, `status`) VALUES
	(1, 2, 1, 'DK XX', 'http://localhost:9999/', NULL, NULL, '2019-03-19 11:35:27', 1),
	(2, 2, 2, 'HUY XX', '', NULL, NULL, '2019-03-19 11:35:27', 1);
/*!40000 ALTER TABLE `core_promotion_detail` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `status` int(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `CODE_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='Bang luu thong tin dich vu';

/*!40000 ALTER TABLE `core_service` DISABLE KEYS */;
INSERT INTO `core_service` (`id`, `code`, `name`, `description`, `status`) VALUES
	(1, 'VTV', 'VTVShowbiz', 'Dịch vụ VTVShowbiz', 1),
	(2, 'YM', 'Yomi', 'Dịch vụ Yomi', 1),
	(3, 'IB', 'IBid 345', 'Dịch vụ IBid 123', 1),
	(9, 'HL', 'Hula', 'Dịch vụ Hula', 0),
	(10, 'FR', 'Funring', 'Dịch vụ Funring', 0),
	(14, 'abc', 'abc123', 'dịch vụ abc', 0);
/*!40000 ALTER TABLE `core_service` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_sms_syntax` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `syntax` varchar(50) DEFAULT NULL COMMENT 'Cu phap tin nhan',
  `regex` varchar(50) DEFAULT NULL COMMENT 'Regex bat cu phap',
  `command` varchar(50) DEFAULT NULL COMMENT 'Command dinh nghia cu phap; REG, UNREG, CONTENT',
  `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao',
  `note` varchar(50) DEFAULT NULL,
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai ban ghi',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Cau hinh cac cu phap nhan tin len cua TB';

/*!40000 ALTER TABLE `core_sms_syntax` DISABLE KEYS */;
INSERT INTO `core_sms_syntax` (`id`, `syntax`, `regex`, `command`, `created_date`, `note`, `status`) VALUES
	(1, 'DK XX', '(?i)^DK XX$', 'REG', '2019-03-18 23:13:26', 'Dang ky goi XX', 1),
	(2, 'HUY XX', '(?i)^HUY XX$', 'CAN', '2019-03-18 23:13:26', 'Huy goi XX', 1);
/*!40000 ALTER TABLE `core_sms_syntax` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_subscriber` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) DEFAULT NULL COMMENT 'So thue bao',
  `mpin` varchar(50) DEFAULT NULL COMMENT 'MK dang nhap wap',
  `package_code` varchar(50) DEFAULT NULL COMMENT 'Goi cuoc',
  `created_date` date DEFAULT NULL COMMENT 'Ngay tao',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian ket thuc goi cuoc',
  `active_time` timestamp NULL DEFAULT NULL COMMENT 'Ngay kick hoat goi cuoc',
  `deactive_time` timestamp NULL DEFAULT NULL COMMENT 'Ngay huy goi cuoc',
  `active_channel` varchar(50) DEFAULT NULL COMMENT 'Kenh dang ky goi cuoc',
  `deactive_channel` varchar(50) DEFAULT NULL COMMENT 'Kenh huy goi cuoc',
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai thue bao',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu',
  `cpid` varchar(50) DEFAULT NULL COMMENT 'Ma CPID',
  `promo_id` bigint(20) DEFAULT NULL COMMENT 'Ma CTKM',
  `ip` varchar(50) DEFAULT NULL,
  `message` varchar(50) DEFAULT NULL COMMENT 'Noi dung thue bao nt DK len',
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `CREATED_DATE_IDX` (`created_date`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_subscriber` DISABLE KEYS */;
INSERT INTO `core_subscriber` (`id`, `msisdn`, `mpin`, `package_code`, `created_date`, `created_time`, `expire_time`, `active_time`, `deactive_time`, `active_channel`, `deactive_channel`, `status`, `note`, `cpid`, `promo_id`, `ip`, `message`) VALUES
	(21, '934538590', '93038', 'XX', '2019-03-29', '2019-03-29 13:41:39', NULL, '2019-03-29 13:41:58', NULL, 'SMS', NULL, 1, 'W05lUuNHea6W', NULL, 2, NULL, NULL);
/*!40000 ALTER TABLE `core_subscriber` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_subscriber_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) DEFAULT NULL COMMENT 'So thue bao',
  `mpin` varchar(50) DEFAULT NULL COMMENT 'MK dang nhap wap',
  `package_code` varchar(50) DEFAULT NULL COMMENT 'Goi cuoc',
  `created_date` date DEFAULT NULL COMMENT 'Ngay tao',
  `created_time` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian tao',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian ket thuc goi cuoc',
  `active_time` timestamp NULL DEFAULT NULL COMMENT 'Ngay kick hoat goi cuoc',
  `deactive_time` timestamp NULL DEFAULT NULL COMMENT 'Ngay huy goi cuoc',
  `active_channel` varchar(50) DEFAULT NULL COMMENT 'Kenh dang ky goi cuoc',
  `deactive_channel` varchar(50) DEFAULT NULL COMMENT 'Kenh huy goi cuoc',
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai thue bao',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu',
  `cpid` varchar(50) DEFAULT NULL COMMENT 'Ma CPID',
  `promo_id` bigint(20) DEFAULT NULL COMMENT 'Ma CTKM',
  `ip` varchar(50) DEFAULT NULL,
  `message` varchar(50) DEFAULT NULL,
  `log_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao ban ghi log',
  `username` varchar(50) DEFAULT NULL COMMENT 'User tac dong tao log',
  `action` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `LOG_TIME_IDX` (`log_time`),
  KEY `USERNAME_IDX` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_subscriber_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_subscriber_log` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_subscriber_log_201903` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) DEFAULT NULL COMMENT 'So thue bao',
  `mpin` varchar(50) DEFAULT NULL COMMENT 'MK dang nhap wap',
  `package_code` varchar(50) DEFAULT NULL COMMENT 'Goi cuoc',
  `created_date` date DEFAULT NULL COMMENT 'Ngay tao',
  `created_time` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian tao',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian ket thuc goi cuoc',
  `active_time` timestamp NULL DEFAULT NULL COMMENT 'Ngay kick hoat goi cuoc',
  `deactive_time` timestamp NULL DEFAULT NULL COMMENT 'Ngay huy goi cuoc',
  `active_channel` varchar(50) DEFAULT NULL COMMENT 'Kenh dang ky goi cuoc',
  `deactive_channel` varchar(50) DEFAULT NULL COMMENT 'Kenh huy goi cuoc',
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai thue bao',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu',
  `cpid` varchar(50) DEFAULT NULL COMMENT 'Ma CPID',
  `promo_id` bigint(20) DEFAULT NULL COMMENT 'Ma CTKM',
  `ip` varchar(50) DEFAULT NULL,
  `message` varchar(50) DEFAULT NULL,
  `log_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao ban ghi log',
  `username` varchar(50) DEFAULT NULL COMMENT 'User tac dong tao log',
  `action` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `LOG_TIME_IDX` (`log_time`),
  KEY `USERNAME_IDX` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_subscriber_log_201903` DISABLE KEYS */;
INSERT INTO `core_subscriber_log_201903` (`id`, `msisdn`, `mpin`, `package_code`, `created_date`, `created_time`, `expire_time`, `active_time`, `deactive_time`, `active_channel`, `deactive_channel`, `status`, `note`, `cpid`, `promo_id`, `ip`, `message`, `log_time`, `username`, `action`) VALUES
	(38, '934538590', '08407', 'XX', '2019-03-29', '2019-03-29 13:41:39', NULL, '2019-03-29 13:41:39', NULL, 'SMS', NULL, 1, 'ikzcB76SchmN', NULL, 2, NULL, 'DK XX', '2019-03-29 13:41:39', 'SYS', 'REGNEW'),
	(39, '934538590', '08407', 'XX', '2019-03-29', '2019-03-29 13:41:39', NULL, '2019-03-29 13:41:39', '2019-03-29 13:41:39', 'SMS', 'SMS', 2, '8glmXJJU4T6Z', NULL, 2, NULL, 'HUY XX', '2019-03-29 13:41:39', 'SYS', 'CANCEL'),
	(40, '934538590', '93038', 'XX', '2019-03-29', '2019-03-29 13:41:39', NULL, '2019-03-29 13:41:58', NULL, 'SMS', NULL, 1, 'W05lUuNHea6W', NULL, 2, NULL, 'DK XX', '2019-03-29 13:41:58', 'SYS', 'RENEW');
/*!40000 ALTER TABLE `core_subscriber_log_201903` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `core_subscriber_log_201904` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id bang',
  `msisdn` varchar(50) DEFAULT NULL COMMENT 'So thue bao',
  `mpin` varchar(50) DEFAULT NULL COMMENT 'MK dang nhap wap',
  `package_code` varchar(50) DEFAULT NULL COMMENT 'Goi cuoc',
  `created_date` date DEFAULT NULL COMMENT 'Ngay tao',
  `created_time` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian tao',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT 'Thoi gian ket thuc goi cuoc',
  `active_time` timestamp NULL DEFAULT NULL COMMENT 'Ngay kick hoat goi cuoc',
  `deactive_time` timestamp NULL DEFAULT NULL COMMENT 'Ngay huy goi cuoc',
  `active_channel` varchar(50) DEFAULT NULL COMMENT 'Kenh dang ky goi cuoc',
  `deactive_channel` varchar(50) DEFAULT NULL COMMENT 'Kenh huy goi cuoc',
  `status` int(1) DEFAULT '1' COMMENT 'Trang thai thue bao',
  `note` varchar(500) DEFAULT NULL COMMENT 'Ghi chu',
  `cpid` varchar(50) DEFAULT NULL COMMENT 'Ma CPID',
  `promo_id` bigint(20) DEFAULT NULL COMMENT 'Ma CTKM',
  `ip` varchar(50) DEFAULT NULL,
  `message` varchar(50) DEFAULT NULL,
  `log_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thoi gian tao ban ghi log',
  `username` varchar(50) DEFAULT NULL COMMENT 'User tac dong tao log',
  `action` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `MSISDN_IDX` (`msisdn`),
  KEY `LOG_TIME_IDX` (`log_time`),
  KEY `USERNAME_IDX` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `core_subscriber_log_201904` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_subscriber_log_201904` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `sys_param` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `_key` varchar(128) NOT NULL DEFAULT '',
  `_value` text,
  `_type` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  `status` int(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `KEY_UNIQ` (`_key`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

/*!40000 ALTER TABLE `sys_param` DISABLE KEYS */;
INSERT INTO `sys_param` (`id`, `_key`, `_value`, `_type`, `created_time`, `modified_time`, `status`) VALUES
	(1, 'REG', 'Nội dung', 'JSON', '2018-12-25 15:33:13', '2018-12-25 15:33:14', 1),
	(55, 'ABC', '600000', 'ABS', '2019-01-30 10:56:14', '2019-01-30 10:56:38', 0),
	(56, 'HOANGCAO1', '1000000', 'AAA', '2019-01-30 11:37:49', NULL, 1);
/*!40000 ALTER TABLE `sys_param` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
