--http://stackoverflow.com/questions/11610850/convert-mysql-script-to-h2
--Here is a short list of steps, to convert from mysql to h2:
--
--Fix up single quotes
--CREATE TABLE `user` ( `name` varchar(20) NOT NULL,
--convert to
--CREATE TABLE user (  name varchar(20) NOT NULL,
--
--Fix up hex numbers
--Fix up bits
--Don't include ranges in keys
--Remove character sets (remove CHARACTER SET ...)
--Remove COLLATE settings (f.e. COLLATE utf8_unicode_ci)
--Remove indexes on BLOBS, CLOBS and TEXT fields
--Make all index names unique
--Use the MySQL compatibility mode (jdbc:h2:~/test;MODE=MySQL)
DROP TABLE if EXISTS _BANK;
CREATE TABLE  _BANK  (
   id  bigint(20) NOT NULL AUTO_INCREMENT,
   location  varchar(255)  NOT NULL,
   version  bigint(20) NOT NULL,
   name  varchar(255) DEFAULT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  UK_pob4j2cq8phjbqbvi2hkj79ta  ( location ),
  UNIQUE KEY  UK_ci45miw0qwvswhtgr3d6t4ajs  ( name )
) ENGINE=InnoDB AUTO_INCREMENT=11 ;

DROP TABLE if EXISTS _GRP;
CREATE TABLE  _GRP  (
   id  bigint(20) NOT NULL AUTO_INCREMENT,
   name  varchar(255) NOT NULL,
   oper  int(11) DEFAULT NULL,
   version  bigint(20) NOT NULL,
   descr  varchar(255) DEFAULT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  UK_4h2pb0vx4x8wbx9u7ykpwiq8e  ( name )
) ENGINE=InnoDB AUTO_INCREMENT=12 ;

DROP TABLE if EXISTS _LIMIT;
CREATE TABLE _LIMIT  (
   id  bigint(20) NOT NULL AUTO_INCREMENT,
   descr  varchar(255) DEFAULT NULL,
   f  int(11) NOT NULL,
   name  varchar(255) DEFAULT NULL,
   t  int(11) NOT NULL,
   version  bigint(20) NOT NULL,
--   value  varchar(255) DEFAULT NULL,
  PRIMARY KEY ( id )
) ENGINE=InnoDB AUTO_INCREMENT=3;

DROP TABLE if EXISTS _CARD;
CREATE TABLE  _CARD  (
   id  bigint(20) NOT NULL AUTO_INCREMENT,
   name  varchar(20) NOT NULL,
   number  varchar(10) DEFAULT NULL,
   place  int(11) DEFAULT NULL,
   sernumber  varchar(20) NOT NULL,
   version  bigint(20) NOT NULL,
   bank_id  bigint(20) DEFAULT NULL,
   group_id  bigint(20) DEFAULT NULL,
   channelId  int(11) NOT NULL,
   active  bit(1) NOT NULL,
   activation  datetime DEFAULT NULL,
   blockdate  datetime DEFAULT NULL,
   blocked  bit(1) NOT NULL,
   descr  varchar(255) DEFAULT NULL,
   dlimit  int(11) NOT NULL,
   mlimit  int(11) NOT NULL,
   limit_id  bigint(20) DEFAULT NULL,
   offnetPos bit(1) NOT NULL,
   mskSeparate bit(1) NOT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  UK_3o760n3nclfuodpsr7dywfso6  ( name ),
  UNIQUE KEY  UK_mo5o7okef60go9skvfs2ab8ue  ( sernumber ),
  UNIQUE KEY  UK_2jy6lvkmo9ksehe81hm0r1qwf  ( bank_id , place ),
  UNIQUE KEY  UK_agi3qve42789fb2cpkbdy0pqe  ( number ),
 
  CONSTRAINT  FK_5s24x4pqeq8y3bqhm5qy7sl7o  FOREIGN KEY ( bank_id ) REFERENCES  _BANK  ( id ),
  CONSTRAINT  FK_fbg18qyrq29oc4cpbrxxqqwgo  FOREIGN KEY ( group_id ) REFERENCES  _GRP  ( id ),
  CONSTRAINT  FK_mjw84130h3p7ljclyv7bfwc5g  FOREIGN KEY ( limit_id ) REFERENCES  _LIMIT  ( id )
) ENGINE=InnoDB AUTO_INCREMENT=99 ;

DROP TABLE if EXISTS _BOX;
CREATE TABLE _BOX (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  capacity int(11) NOT NULL,
  descr varchar(255)  DEFAULT NULL,
  disposition varchar(255)  DEFAULT NULL,
  ip varchar(255)  NOT NULL,
  version bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_r59ndyn87p6979sy3i2wakjfg (ip),
  UNIQUE KEY UK_9cjw5ax9u5ynkh21t415gpsx9 (disposition)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT ;

DROP TABLE if EXISTS _CHANNEL;
CREATE TABLE _CHANNEL (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  enabled bit(1) NOT NULL,
  line int(11) DEFAULT NULL,
  name varchar(20) NOT NULL,
  version bigint(20) NOT NULL,
  box_id bigint(20) NOT NULL,
  card bigint(20) DEFAULT NULL,
  group_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_h281mn1a65exl7qcg1jk4kxit (name),
  UNIQUE KEY UK_4x56qbtt93yr61ty357p3j521 (box_id,line),
  UNIQUE KEY UK_m4rnxpbpjmy0uhj6jje0rl7v8 (card),
  CONSTRAINT FK_9s89uj3tv3mwk6gq3jfnch9dx FOREIGN KEY (group_id) REFERENCES _GRP (id),
  CONSTRAINT FK_em0rt76ep3sg78o2whydlhs37 FOREIGN KEY (box_id) REFERENCES _BOX (id),
  CONSTRAINT FK_m4rnxpbpjmy0uhj6jje0rl7v8 FOREIGN KEY (card) REFERENCES _CARD (id)
) ENGINE=InnoDB AUTO_INCREMENT=148;

DROP TABLE if EXISTS _TRUNK;
CREATE TABLE _TRUNK (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  descr varchar(255) DEFAULT NULL,
  name varchar(255) NOT NULL,
  version bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_9rc70ctuyf600xl39mt14qkcr (name),
  UNIQUE KEY UK_9jkkstux5m9k4rupwtvew3f6p (name)
) ENGINE=InnoDB AUTO_INCREMENT=5;

DROP TABLE if EXISTS _CHANNEL__TRUNK;
CREATE TABLE _CHANNEL__TRUNK (
  channels_id bigint(20) NOT NULL,
  trunks_id bigint(20) NOT NULL,
  PRIMARY KEY (channels_id,trunks_id),
  CONSTRAINT FK_5nl7mw5moqxaf9t3335pw1pul FOREIGN KEY (channels_id) REFERENCES _CHANNEL (id),
  CONSTRAINT FK_5pib1fihst04tikff5uui3o7n FOREIGN KEY (trunks_id) REFERENCES _TRUNK (id)
) ENGINE=InnoDB ;	

DROP TABLE if EXISTS numberplan;
CREATE TABLE numberplan (
  fromd bigint(20) DEFAULT NULL,
  tod bigint(20) DEFAULT NULL,
  owner varchar(20) DEFAULT NULL,
  regcode int(2) DEFAULT NULL,
  mnc int(2) DEFAULT NULL
) ENGINE=InnoDB;

DROP TABLE if EXISTS cdr;
CREATE TABLE cdr (
  calldate datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  clid varchar(80) NOT NULL DEFAULT '',
  src varchar(80) NOT NULL DEFAULT '',
  dst varchar(80) NOT NULL DEFAULT '',
  dcontext varchar(80) NOT NULL DEFAULT '',
  channel varchar(80) NOT NULL DEFAULT '',
  dstchannel varchar(80) NOT NULL DEFAULT '',
  lastapp varchar(80) NOT NULL DEFAULT '',
  lastdata varchar(80) NOT NULL DEFAULT '',
  duration int(11) NOT NULL DEFAULT '0',
  billsec int(11) NOT NULL DEFAULT '0',
  disposition varchar(45) NOT NULL DEFAULT '',
  amaflags int(11) NOT NULL DEFAULT '0',
  accountcode varchar(20) NOT NULL DEFAULT '',
  userfield varchar(255) NOT NULL DEFAULT '',
  uniqueid varchar(32) NOT NULL DEFAULT '',
  peerip varchar(16) NOT NULL,
  fromsip varchar(25) NOT NULL,
  uri varchar(25) NOT NULL,
  useragent varchar(45) NOT NULL,
  codec1 varchar(15) NOT NULL,
  codec2 varchar(15) NOT NULL,
  llp int(6) NOT NULL,
  rlp int(6) NOT NULL,
  ljitt int(10) NOT NULL,
  rjitt int(10) NOT NULL,
  trunk varchar(15) NOT NULL,
  hgcode int(6) unsigned NOT NULL,
  gateip varchar(15) NOT NULL DEFAULT '',
  regcode int(2) DEFAULT NULL,
) ENGINE=InnoDB;

DROP TABLE if EXISTS _BALANCE;
CREATE TABLE _BALANCE (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  balance float DEFAULT NULL,
  date datetime DEFAULT NULL,
  encodedmsg varchar(255) DEFAULT NULL,
  payment bit(1) NOT NULL,
  smsNeeded bit(1) NOT NULL,
  card_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_eo4p2ralgphpfv39p2fuisx5g FOREIGN KEY (card_id) REFERENCES _CARD (id)
) ENGINE=InnoDB;
