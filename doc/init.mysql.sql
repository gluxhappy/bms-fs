CREATE TABLE `omd_user` (
  `id` bigint(20) NOT NULL,
  `username` char(30) NOT NULL,
  `password` char(128) NOT NULL,
  `alias` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `omd_user_role` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role` enum('USER','REDIS','ADMIN') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_userid_role` (`user_id`,`role`),
  KEY `idx_userid` (`user_id`),
  CONSTRAINT `fk_ref_ur_userid` FOREIGN KEY (`user_id`) REFERENCES `omd_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `omd_redis_pattern` (
  `id` bigint(20) NOT NULL,
  `pattern` varchar(200) NOT NULL,
  `db` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `desp` text NOT NULL,
  `creator` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
