--
-- MySQL 5.1.44
-- Thu, 10 Feb 2011 20:15:07 +0000
--

CREATE TABLE `accessgroups` (
   `id` int(11) not null,
   `name` varchar(50),
   `commands` longtext,
   PRIMARY KEY (`id`),
   UNIQUE KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


CREATE TABLE `accesslevels` (
   `id` int(11) not null,
   `name` varchar(50),
   `usernameformat` varchar(255) default '^0{$username}',
   `accessgroups` varchar(255),
   `admingroup` tinyint(1) default '0',
   `canbuild` tinyint(1) default '1',
   PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


CREATE TABLE `homes` (
   `id` int(10) unsigned not null auto_increment,
   `name` varchar(50),
   `userid` int(11),
   `username` varchar(50) not null,
   `world` varchar(50) not null,
   `x` double not null,
   `y` double not null,
   `z` double not null,
   `rotX` float not null,
   `rotY` float not null,
   PRIMARY KEY (`id`),
   UNIQUE KEY (`name`,`userid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=3;


CREATE TABLE `kits` (
   `id` int(11) not null,
   `name` varchar(50) not null,
   `items` longtext not null,
   `minaccesslevel` int(11) default '0',
   `delay` int(11) default '0',
   PRIMARY KEY (`id`),
   UNIQUE KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


CREATE TABLE `users` (
   `id` int(11) not null auto_increment,
   `username` varchar(255) not null,
   `usernameformat` varchar(255),
   `password` varchar(255) ,
   `email` varchar(255) ,
   `firstlogin` int(11) not null default '0',
   `lastlogin` int(11) not null default '0',
   `onlinetime` int(11) not null default '0',
   `bantime` int(11) not null default '0',
   `mutetime` int(11) not null default '0',
   `commands` longtext ,
   `canbuild` tinyint(1) not null default '0',
   `isadmin` tinyint(1) not null default '0',
   `accesslevel` int(11) not null default '0',
   PRIMARY KEY (`id`),
   UNIQUE KEY (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;


CREATE TABLE `warps` (
   `id` int(10) unsigned not null auto_increment,
   `name` varchar(50) not null,
   `groupname` varchar(255) default 'default',
   `world` varchar(50) not null,
   `x` double not null,
   `y` double not null,
   `z` double not null,
   `rotX` float not null,
   `rotY` float not null,
   `minaccesslevel` int(11) default '0',
   PRIMARY KEY (`id`),
   UNIQUE KEY (`name`,`groupname`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=6;s