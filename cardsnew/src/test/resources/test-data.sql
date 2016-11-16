INSERT INTO  _LIMIT  ( id ,  descr ,  f ,  name ,  t ,  version ,  value )
VALUES
	(1, 'limit_test', 50, 'test', 70, 1, NULL),
	(2, 'limit big', 70, 'big', 100, 0, NULL);

INSERT INTO  _GRP  ( id ,  name ,  oper ,  version ,  descr )
VALUES
	(1, 'red1', 0, 8, 'red1 for first'),
	(2, 'green212', 1, 5, 'green2'),
	(4, 'yellow3', 2, 3, 'yellow3'),
	(7, 'mgfrus', 1, 2, 'mgfrus');

INSERT INTO  _BANK  ( id ,  location ,  version ,  name )
VALUES
	(4, 'Serg home 1', 1, '1.1.1.3'),
	(6, 'Serg home 2', 0, '1.1.1.4'),
	(7, 'Serg home 3', 0, '1.1.1.5'),
	(8, 'Oleg home1', 0, '1.1.1.19'),
	(9, 'Oleg home 2', 1, '1.1.1.21');

INSERT INTO _CARD (id, name, number, place, sernumber, version, bank_id, group_id, channelId, active, activation, blockdate, blocked, descr, dlimit, mlimit, limit_id, offnetPos, mskSeparate)
VALUES
(1, 'aaa512', '1111111508', 18, '013065004353197', 13, 8, 7, 0, 1, NULL, NULL, 0, '067670 ', 60, 2581, 1, 1, 1),
(2, 'aaa513', '1111111749', 19, '352846038141057', 14, 8, 7, 0, 1, NULL, NULL, 0, '665051  ', 60, 2581, 1, 1, 1),
(3, 'aaa516', '1111111243', 22, '013065007359829', 14, 8, 7, 0, 1, NULL, NULL, 0, '805151 ', 60, 2581, 1, 1, 1),
(4, 'aaa40d', '1111111519', 29, '868267016047991', 14, 6, 7, 0, 1, NULL, NULL, 0, '123123 ', 60, 2588, 1, 1, 1),
(5, 'aaa411', '1111111596', 17, '358967049568148', 12, 6, 7, 0, 1, NULL, NULL, 0, '516805 ', 60, 2581, 1, 1, 1),
(6, 'aaa31a', '1111111783', 26, '358842009754121', 12, 4, 7, 0, 1, NULL, NULL, 0, '015109 ', 60, 2581, 1, 1, 1),
(7, 'aaa51a', '1111111872', 26, '359717018382453', 12, 8, 7, 0, 1, NULL, NULL, 0, '751510 ', 60, 2581, 1, 1, 1),
(8, 'aaa402', '1111111082', 2, '0130650032881', 13, 6, 7, 0, 1, NULL, NULL, 0, '156805 ', 60, 2581, 1, 1, 1),
(9, 'aaa30b', '11111119', 11, '356095003971553', 13, 4, 7, 0, 1, NULL, NULL, 0, '577989 ', 60, 2581, 1, 1, 1),
(10, 'aaa104', '1111111221', 4, '352787024381549', 13, 7, 7, 0, 1, NULL, NULL, 0, '590965 ', 60, 2581, 1, 1, 1),
(11, 'aaa10e', '1111111654', 14, '355241033875122', 14, 7, 7, 0, 1, NULL, NULL, 0, '167611 ', 60, 2581, 1, 1, 1),
(12, 'aaa111', '1111111173', 17, '355241034998162', 12, 7, 7, 0, 1, NULL, NULL, 0, '997981 ', 60, 2581, 1, 1, 1),
(13, 'aaa112', '11111199', 18, '352921024967714', 12, 7, 7, 0, 1, NULL, NULL, 0, '917658 ', 60, 2581, 1, 1, 1),
(14, 'aaa801', '1111111472', 1, '356810033966310', 12, 9, 7, 0, 1, NULL, NULL, 0, '595878 ', 60, 2581, 1, 1, 1),
(15, 'aaa805', '1111111489', 5, '358842003600213', 11, 9, 7, 0, 1, NULL, NULL, 0, '878905 ', 60, 2581, 1, 1, 1),
(16, 'aaa11a', '1111111095', 26, '4901107049772', 19, 7, 7, 0, 1, NULL, NULL, 0, '180961 ', 60, 2581, 1, 1, 1),
(17, 'aaa807', '1111111465', 7, '354010003881902', 15, 9, 7, 0, 1, NULL, NULL, 0, '981680 ', 60, 2581, 1, 1, 1),
(18, 'aaa809', '1111111463', 9, '3527870206533', 15, 9, 7, 0, 1, NULL, NULL, 0, '787116 ', 60, 2581, 1, 1, 1),
(19, 'aaa40b', '1111111603', 11, '354010009944712', 15, 6, 7, 0, 1, NULL, NULL, 0, 'non ', 60, 2581, 1, 1, 1),
(20, 'aaa309', '1111111218', 9, '868267014188433', 12, 4, 7, 0, 1, NULL, NULL, 0, '585059 ', 60, 2581, 1, 1, 1),
(21, 'aaa315', '1111111719', 21, '013065004871552', 13, 4, 7, 0, 1, NULL, NULL, 0, '105516 ', 60, 2581, 1, 1, 1),
(22, 'aaa410', '1111111939', 16, '359614045879637', 12, 6, 7, 0, 1, NULL, NULL, 0, '501158 ', 60, 2581, 1, 1, 1),
(23, 'aaa414', '1111111639', 20, '358343045311794', 18, 6, 7, 0, 1, NULL, NULL, 0, '670019 ', 60, 2581, 2, 1, 1),
(24, 'aaa41a', '11111135', 26, '358967041976349', 17, 6, 7, 0, 1, NULL, NULL, 0, '598151 ', 60, 2581, 2, 1, 1),
(25, 'aaa41c', '1111111362', 28, '3560950093278', 14, 6, 7, 0, 1, NULL, NULL, 0, '978167 ', 60, 2581, 1, 1, 1),
(26, 'aaa41f', '1111111814', 31, '356095009048810', 13, 6, 7, 0, 1, NULL, NULL, 0, '8fipW6 ', 60, 2581, 1, 1, 1),
(27, 'aaa420', '1111111845', 32, '356810032108799', 18, 6, 7, 0, 1, NULL, NULL, 0, 'tfh7cm' , 60, 2581, 2, 1, 1),
(28, 'aaa102', '1111111459', 2, '450060340840775', 15, 7, 7, 0, 1, NULL, NULL, 0, 'Vd4YAt ', 60, 2581, 2, 1, 1);

INSERT INTO _BOX (id, capacity, descr, disposition, ip, version)
VALUES
	(1, 4, 'first 1', 'at home', '192.168.200.45', 1),
	(3, 4, 'balk', 'balk', '192.168.200.46', 0),
	(4, 8, '4323111', 'sdfadsf', '192.168.4.138', 4),
	(5, 8, 'ffddasdfasd', 'ffsdf', '192.168.5.102', 1),
	(6, 4, '0987', 'gggg', '192.168.5.206', 4),
	(7, 4, '??', '???', '192.168.5.230', 0),
	(8, 4, 'ff', 'dd', '192.168.6.70', 0),
	(9, 8, 'fake', 'ppdadfadf', '1.1.1.1', 0),
	(10, 8, '172.17.1.34', 'sfsdf', '172.17.1.34', 1),
	(11, 8, '172.17.1.36', 'vova', '172.17.1.36', 0),
	(14, 8, '172.17.1.38', 'gfadfg', '172.17.1.38', 0);
	
INSERT INTO _CHANNEL (id, enabled, line, name, version, box_id, card, group_id)
VALUES
	(1, 1, 5, 'mgf92', 3, 10, NULL, 7),
	(2, 1, 5, 'mgf73', 5, 11, NULL, 7),
	(3, 1, 5, 'mgf71', 3, 14, NULL, 7),
	(4, 1, 7, 'mgf46', 3, 14, NULL, 7),
	(5, 1, 1, 'mgf36', 3, 1, NULL, 7),
	(6, 1, 2, 'mgf30', 3, 1, NULL, 7),
	(7, 1, 3, 'mgf34', 2, 1, NULL, 7),
	(8, 1, 0, 'mgf43', 2, 3, NULL, 7),
	(9, 1, 3, 'mgf81', 2, 3, NULL, 7),
	(135, 1, 0, 'mgf87', 11, 4, NULL, 7),
	(136, 1, 3, 'mgf80', 11, 4, NULL, 7),
	(137, 1, 4, 'mgf49', 6, 4, NULL, 7),
	(138, 1, 7, 'mgf35', 1, 4, NULL, 7),
	(139, 1, 0, 'mgf85', 1, 5, NULL, 7),
	(140, 1, 3, 'mgf86', 1, 5, NULL, 7),
	(141, 1, 4, 'mgf75', 1, 5, NULL, 7),
	(142, 1, 7, 'mgf70', 1, 5, NULL, 7),
	(143, 1, 0, 'mgf37', 1, 6, NULL, 7),
	(144, 1, 2, 'mgf38', 3, 6, NULL, 7),
	(145, 1, 3, 'mgf93', 3, 7, NULL, 7),
	(146, 1, 2, 'mgf74', 14, 8, NULL, 7);	
	
INSERT INTO _TRUNK (id, descr, name, version)
VALUES
	(1, 'trnk_mgfrus', 'trnk_mgfrus', 5),
	(2, 'descr1', 'tr2', 1),
	(3, 'descr3', 'tr3', 0),
	(4, 'test meg trunk', 'test_meg_trunk', 1);
	
INSERT INTO _CHANNEL__TRUNK (channels_id, trunks_id)
VALUES
	(1, 1),
	(2, 1),
	(3, 1),
	(4, 1),
	(5, 1),
	(6, 1),
	(7, 1),
	(8, 1),
	(9, 1),
	(135, 1),
	(136, 1),
	(137, 1),
	(136, 2);	

INSERT INTO numberplan (fromd, tod, owner, regcode, mnc)
VALUES
	(9160000000, 9169999999, 'mMTS', 77, 1),
	(9170000000, 9170029999, 'mMTS', 33, 1),
	(9250000000, 9250999999, 'mMEGAFON', 77, 2),
	(9204700000, 9204999999, 'mMEGAFON', 69, 2),
	(9030000000, 9030199999, 'mBEELINE', 77, 99),
	(9618200000, 9618259999, 'mBEELINE', 15, 99);