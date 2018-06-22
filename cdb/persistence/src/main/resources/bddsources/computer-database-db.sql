-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 22, 2018 at 01:51 PM
-- Server version: 5.7.22-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `computer-database-db`
--

-- --------------------------------------------------------

--
-- Table structure for table `Authorities`
--

CREATE TABLE `Authorities` (
  `id` bigint(20) NOT NULL,
  `authority` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Authorities`
--

INSERT INTO `Authorities` (`id`, `authority`) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

-- --------------------------------------------------------

--
-- Table structure for table `Company`
--

CREATE TABLE `Company` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `image` varchar(2083) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Company`
--

INSERT INTO `Company` (`id`, `name`, `description`, `image`) VALUES
(1, 'Apple Inc.', NULL, NULL),
(2, 'Thinking Machines', NULL, NULL),
(3, 'RCA', NULL, NULL),
(4, 'Netronics', NULL, NULL),
(5, 'Tandy Corporation', NULL, NULL),
(6, 'Commodore International', NULL, NULL),
(7, 'MOS Technology', NULL, NULL),
(8, 'Micro Instrumentation and Telemetry Systems', NULL, NULL),
(9, 'IMS Associates, Inc.', NULL, NULL),
(10, 'Digital Equipment Corporation', NULL, NULL),
(11, 'Lincoln Laboratory', NULL, NULL),
(12, 'Moore School of Electrical Engineering', NULL, NULL),
(13, 'IBM', NULL, NULL),
(14, 'Amiga Corporation', NULL, NULL),
(15, 'Canon', NULL, NULL),
(16, 'Nokia', NULL, NULL),
(17, 'Sony', NULL, NULL),
(18, 'OQO', NULL, NULL),
(19, 'NeXT', NULL, NULL),
(20, 'Atari', NULL, NULL),
(22, 'Acorn Computer', NULL, NULL),
(23, 'Timex Sinclair', NULL, NULL),
(24, 'Nintendo', NULL, NULL),
(25, 'Sinclair Research Ltd', NULL, NULL),
(26, 'Xerox', NULL, NULL),
(27, 'Hewlett-Packard', NULL, NULL),
(28, 'Zemmix', NULL, NULL),
(29, 'ACVS', NULL, NULL),
(30, 'Sanyo', NULL, NULL),
(31, 'Cray', NULL, NULL),
(32, 'Evans & Sutherland', NULL, NULL),
(33, 'E.S.R. Inc.', NULL, NULL),
(34, 'OMRON', NULL, NULL),
(35, 'BBN Technologies', NULL, NULL),
(36, 'Lenovo Group', NULL, NULL),
(37, 'ASUS', NULL, NULL),
(38, 'Amstrad', NULL, NULL),
(39, 'Sun Microsystems', NULL, NULL),
(40, 'Texas Instruments', NULL, NULL),
(41, 'HTC Corporation', NULL, NULL),
(42, 'Research In Motion', NULL, NULL),
(43, 'Samsung Electronics', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `Computer`
--

CREATE TABLE `Computer` (
  `id` bigint(20) NOT NULL,
  `discontinued` date DEFAULT NULL,
  `introduced` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Computer`
--

INSERT INTO `Computer` (`id`, `discontinued`, `introduced`, `name`, `company_id`) VALUES
(1, NULL, NULL, 'MacBook Pro 15.4 inch', 1),
(2, NULL, NULL, 'CM-2a', 2),
(3, NULL, NULL, 'CM-200', 2),
(4, NULL, NULL, 'CM-5e', 2),
(5, NULL, '1991-01-01', 'CM-5', 2),
(6, NULL, '2006-01-10', 'MacBook Pro', 1),
(7, NULL, NULL, 'Apple IIe', NULL),
(8, NULL, NULL, 'Apple IIc', NULL),
(9, NULL, NULL, 'Apple IIGS', NULL),
(10, NULL, NULL, 'Apple IIc Plus', NULL),
(11, NULL, NULL, 'Apple II Plus', NULL),
(12, '1984-04-01', '1980-05-01', 'Apple III', 1),
(13, NULL, NULL, 'Apple Lisa', 1),
(14, NULL, NULL, 'CM-2', 2),
(15, NULL, '1987-01-01', 'Connection Machine', 2),
(16, '1993-10-01', '1977-04-01', 'Apple II', 1),
(17, '1984-04-01', '1983-12-01', 'Apple III Plus', 1),
(18, NULL, NULL, 'COSMAC ELF', 3),
(19, NULL, '1977-01-01', 'COSMAC VIP', 3),
(20, NULL, '1977-01-01', 'ELF II', 4),
(21, NULL, '1984-01-24', 'Macintosh', 1),
(22, NULL, NULL, 'Macintosh II', NULL),
(23, '1990-10-15', '1986-01-16', 'Macintosh Plus', 1),
(24, NULL, NULL, 'Macintosh IIfx', NULL),
(25, NULL, '1998-01-01', 'iMac', 1),
(26, NULL, '2005-01-22', 'Mac Mini', 1),
(27, NULL, '2006-08-07', 'Mac Pro', 1),
(28, '2006-08-01', '1994-03-01', 'Power Macintosh', 1),
(29, '2006-01-01', '1991-01-01', 'PowerBook', 1),
(30, NULL, NULL, 'Xserve', NULL),
(31, NULL, NULL, 'Powerbook 100', NULL),
(32, NULL, NULL, 'Powerbook 140', NULL),
(33, NULL, NULL, 'Powerbook 170', NULL),
(34, NULL, NULL, 'PowerBook Duo', NULL),
(35, NULL, NULL, 'PowerBook 190', NULL),
(36, NULL, '1991-01-01', 'Macintosh Quadra', 1),
(37, NULL, NULL, 'Macintosh Quadra 900', NULL),
(38, NULL, NULL, 'Macintosh Quadra 700', NULL),
(39, NULL, '1990-01-01', 'Macintosh LC', 1),
(40, NULL, '1990-01-01', 'Macintosh LC II', 1),
(41, NULL, '1993-01-01', 'Macintosh LC III', 1),
(42, NULL, NULL, 'Macintosh LC III+', NULL),
(43, NULL, '1993-10-21', 'Macintosh Quadra 605', 1),
(44, NULL, NULL, 'Macintosh LC 500 series', NULL),
(45, NULL, '1980-01-01', 'TRS-80 Color Computer', 5),
(46, '2000-04-08', '1986-04-08', 'nouveaunomvalid', 22),
(47, NULL, NULL, 'Dragon 32/64', NULL),
(48, NULL, NULL, 'MEK6800D2', NULL),
(49, NULL, NULL, 'Newbear 77/68', NULL),
(50, NULL, NULL, 'Commodore PET', 6),
(51, '1994-01-01', '1982-08-01', 'Commodore 64', 6),
(52, NULL, NULL, 'Commodore 64C', NULL),
(53, NULL, NULL, 'Commodore SX-64', 6),
(54, NULL, NULL, 'Commodore 128', 6),
(55, '1977-10-01', '1976-04-01', 'Apple I', 1),
(56, NULL, '1975-01-01', 'KIM-1', 7),
(57, '2000-04-08', '1986-04-08', 'nouveaunomvalid', 22),
(58, NULL, '1975-08-01', 'IMSAI 8080', 9),
(59, NULL, NULL, 'IMSAI Series Two', NULL),
(60, NULL, '1977-10-25', 'VAX', 10),
(61, NULL, '1977-10-25', 'VAX 11/780', 10),
(62, NULL, '1980-10-01', 'VAX 11/750', 10),
(63, NULL, '1958-01-01', 'TX-2', 11),
(64, NULL, '1956-01-01', 'TX-0', 11),
(65, NULL, '1951-04-20', 'Whirlwind', 11),
(66, '1955-10-02', '1946-02-15', 'ENIAC', 12),
(67, NULL, '1981-08-12', 'IBM PC', 13),
(68, NULL, NULL, 'Macintosh Classic', NULL),
(69, NULL, '1991-01-01', 'Macintosh Classic II', 1),
(628, NULL, NULL, 'nouveaunomvalid', NULL),
(71, '2000-04-08', '1986-04-08', 'nouveaunomvalid', 22),
(72, NULL, '1987-01-01', 'Amiga 500', 6),
(73, NULL, NULL, 'Amiga 500+', NULL),
(74, '2000-04-08', '1986-04-08', 'nouveaunomvalid', 22),
(75, NULL, NULL, 'modifier', 22),
(76, NULL, '1992-03-01', 'Amiga 600', 6),
(77, NULL, '1984-01-01', 'Macintosh 128K', 1),
(78, '1986-04-14', '1984-09-10', 'Macintosh 512K', 1),
(79, '1989-08-01', '1987-03-02', 'Macintosh SE', 1),
(80, '1991-10-21', '1989-01-19', 'Macintosh SE/30', 1),
(81, NULL, '1987-01-01', 'Canon Cat', 15),
(82, NULL, NULL, 'Nokia 770', 16),
(83, NULL, '2007-01-01', 'Nokia N800', 16),
(84, NULL, '2006-09-21', 'Mylo', 17),
(85, NULL, '2007-01-01', 'OQO 02', 18),
(86, NULL, NULL, 'OQO 01+', NULL),
(87, NULL, NULL, 'Pinwheel calculator', NULL),
(88, NULL, NULL, 'iBook', 1),
(89, NULL, '2006-05-16', 'MacBook', 1),
(90, '1993-01-01', '1990-01-01', 'NeXTstation', 19),
(91, '1993-01-01', '1988-01-01', 'NeXTcube', 19),
(92, NULL, NULL, 'NeXTstation Color Turbo', NULL),
(93, NULL, NULL, 'NeXTstation Color', NULL),
(94, NULL, NULL, 'NeXTstation Turbo', NULL),
(95, NULL, NULL, 'NeXTcube Turbo', 19),
(96, NULL, NULL, 'NeXTcube 040', 19),
(97, NULL, NULL, 'NeXTcube 030', 19),
(98, NULL, NULL, 'Tinkertoy Tic-Tac-Toe Computer', NULL),
(99, NULL, NULL, 'Z3', NULL),
(100, NULL, NULL, 'Z4', NULL),
(101, NULL, NULL, 'Z1', NULL),
(102, NULL, NULL, 'Z2', NULL),
(103, NULL, '1973-05-01', 'Wang 2200', NULL),
(104, NULL, NULL, 'Wang VS', NULL),
(105, NULL, NULL, 'Wang OIS', NULL),
(106, NULL, NULL, 'BBC Micro', 22),
(107, '1962-01-01', '1953-01-01', 'IBM 650', 13),
(108, NULL, NULL, 'Cray-1', NULL),
(109, NULL, NULL, 'Cray-3', NULL),
(110, NULL, NULL, 'Cray-2', NULL),
(111, NULL, NULL, 'Cray-4', NULL),
(112, NULL, NULL, 'Cray X1', NULL),
(113, NULL, NULL, 'Cray XD1', NULL),
(114, NULL, '1993-01-01', 'Cray T3D', NULL),
(115, NULL, '1995-01-01', 'Cray T3E', NULL),
(116, NULL, NULL, 'Cray C90', NULL),
(117, NULL, NULL, 'Cray T90', NULL),
(118, NULL, NULL, 'Cray SV1', NULL),
(119, NULL, NULL, 'Cray J90', NULL),
(120, NULL, NULL, 'Cray XT3', NULL),
(121, NULL, NULL, 'Cray CS6400', NULL),
(122, '1993-01-01', '1985-01-01', 'Atari ST', 20),
(123, '2000-04-08', '1986-04-08', 'nouveaunomvalid', 22),
(125, NULL, NULL, 'Amiga 4000', 6),
(126, NULL, NULL, 'Amiga 3000UX', 6),
(127, NULL, NULL, 'Amiga 3000T', 6),
(128, NULL, NULL, 'Amiga 4000T', 6),
(129, '1996-01-01', '1992-10-01', 'modifier', 22),
(130, NULL, '1986-01-01', 'Atari 1040 STf', NULL),
(131, NULL, '1985-01-01', 'Atari 520 ST', NULL),
(132, NULL, '1986-01-01', 'Atari 520 STfm', NULL),
(133, NULL, '1989-01-01', 'Atari 1040 STe', NULL),
(134, NULL, '1991-01-01', 'Atari MEGA STe', NULL),
(135, NULL, '1985-01-01', 'Atari 520 ST+', NULL),
(136, NULL, '1985-01-01', 'Atari 520 STm', NULL),
(137, NULL, '1985-01-01', 'Atari 130 ST', NULL),
(138, NULL, '1985-01-01', 'Atari 260 ST', NULL),
(139, NULL, '1987-01-01', 'Atari MEGA ST', NULL),
(140, NULL, '1986-01-01', 'Atari 520 STf', NULL),
(141, NULL, '1986-01-01', 'Atari 1040 STfm', NULL),
(142, NULL, '1986-01-01', 'Atari 2080 ST', NULL),
(143, NULL, '1985-01-01', 'Atari 260 ST+', NULL),
(144, NULL, '1988-01-01', 'Atari 4160 STe', NULL),
(145, NULL, NULL, 'TRS-80 Color Computer 2', NULL),
(146, NULL, NULL, 'TRS-80 Color Computer 3', NULL),
(147, NULL, '1977-01-01', 'TRS-80 Model 1', 5),
(148, '1984-04-01', '1983-11-01', 'Timex Sinclair 2068', 23),
(149, NULL, '1982-01-01', 'ZX Spectrum', 25),
(150, NULL, '1981-01-01', 'Xerox Star', 26),
(151, NULL, NULL, 'Xerox Alto', NULL),
(152, '2000-04-08', '1986-04-08', 'nouveaunomvalid', 22),
(153, NULL, NULL, 'Nintendo Entertainment System', 24),
(154, '1999-01-01', '1991-08-01', 'Super Nintendo Entertainment System', 24),
(155, NULL, NULL, 'Super Famicom', NULL),
(156, NULL, NULL, 'Nintendo GameCube', 24),
(157, NULL, NULL, 'Game Boy line', NULL),
(158, NULL, '1994-12-03', 'PlayStation', 17),
(159, NULL, '2000-03-24', 'PlayStation 2', 17),
(160, NULL, NULL, 'Game & Watch', 24),
(161, NULL, NULL, 'EDSAC', NULL),
(162, NULL, NULL, 'IBM System/4 Pi', NULL),
(163, NULL, NULL, 'IBM AP-101', NULL),
(164, NULL, NULL, 'IBM TC-1', NULL),
(165, NULL, NULL, 'IBM AP-101B', NULL),
(166, NULL, NULL, 'IBM AP-101S', 13),
(167, NULL, NULL, 'ProLiant', 27),
(168, NULL, NULL, 'Http://nepomuk.semanticdesktop.org/xwiki/', NULL),
(169, '1986-01-01', '1984-01-01', 'Sinclair QL', 25),
(170, NULL, '1981-01-01', 'Sinclair ZX81', 25),
(171, NULL, NULL, 'Sinclair ZX80', 25),
(172, NULL, NULL, 'Atari 65XE', 20),
(173, NULL, NULL, 'Deep Blue', NULL),
(174, NULL, NULL, 'Macintosh Quadra 650', NULL),
(175, NULL, NULL, 'Macintosh Quadra 610', NULL),
(176, NULL, NULL, 'Macintosh Quadra 800', NULL),
(177, NULL, NULL, 'Macintosh Quadra 950', NULL),
(178, NULL, NULL, 'PowerBook 160', NULL),
(179, NULL, NULL, 'PowerBook 145B', NULL),
(180, NULL, NULL, 'PowerBook 170', NULL),
(181, NULL, NULL, 'PowerBook 145', NULL),
(182, NULL, NULL, 'PowerBook G3', NULL),
(183, NULL, NULL, 'PowerBook 140', NULL),
(184, NULL, NULL, 'Macintosh IIcx', NULL),
(185, NULL, NULL, 'Powerbook 180', NULL),
(186, NULL, NULL, 'PowerBook G4', NULL),
(187, NULL, NULL, 'Macintosh XL', NULL),
(188, NULL, NULL, 'PowerBook 100', NULL),
(189, NULL, NULL, 'PowerBook 2400c', NULL),
(190, NULL, NULL, 'PowerBook 1400', NULL),
(191, NULL, NULL, 'Macintosh Quadra 630', NULL),
(192, NULL, NULL, 'Macintosh Quadra 660AV', NULL),
(193, NULL, NULL, 'Macintosh Quadra 840AV', NULL),
(194, NULL, NULL, 'PowerBook 5300', NULL),
(195, NULL, NULL, 'PowerBook 3400c', NULL),
(196, NULL, NULL, 'Macintosh Color Classic', NULL),
(197, NULL, NULL, 'Macintosh 512Ke', NULL),
(198, NULL, NULL, 'Macintosh IIsi', NULL),
(199, NULL, NULL, 'Macintosh IIx', NULL),
(200, NULL, NULL, 'PowerBook 500 series', NULL),
(201, NULL, NULL, 'Power Macintosh G3', NULL),
(202, NULL, NULL, 'Macintosh IIci', NULL),
(203, NULL, '2004-08-31', 'iMac G5', 1),
(204, NULL, NULL, 'Power Mac G4', NULL),
(205, NULL, NULL, 'Power Macintosh 7100', NULL),
(206, NULL, NULL, 'Power Macintosh 9600', NULL),
(207, NULL, NULL, 'Power Macintosh 7200', NULL),
(208, NULL, NULL, 'Power Macintosh 7300', NULL),
(209, NULL, NULL, 'Power Macintosh 8600', NULL),
(210, NULL, NULL, 'Power Macintosh 6200', NULL),
(211, NULL, NULL, 'Power Macintosh 8100', NULL),
(212, NULL, NULL, 'Compact Macintosh', NULL),
(213, NULL, NULL, 'Power Macintosh 4400', NULL),
(214, NULL, NULL, 'Power Macintosh 9500', NULL),
(215, NULL, NULL, 'Macintosh Portable', NULL),
(216, NULL, NULL, 'EMac', NULL),
(217, NULL, NULL, 'Power Macintosh 7600', NULL),
(218, NULL, NULL, 'Power Mac G5', NULL),
(219, NULL, NULL, 'Power Macintosh 7500', NULL),
(220, NULL, NULL, 'Power Macintosh 6100', NULL),
(221, NULL, NULL, 'Power Macintosh 8500', NULL),
(222, NULL, NULL, 'Macintosh IIvi', NULL),
(223, NULL, NULL, 'Macintosh IIvx', NULL),
(224, NULL, NULL, 'IMac G3', NULL),
(225, NULL, NULL, 'IMac G4', NULL),
(226, NULL, NULL, 'Power Mac G4 Cube', 1),
(227, NULL, NULL, 'Intel iMac', NULL),
(228, NULL, NULL, 'Deep Thought', 13),
(229, NULL, '2006-11-19', 'Wii', 24),
(230, NULL, NULL, 'IBM System x', NULL),
(231, NULL, '2006-01-01', 'IBM System i', 13),
(232, NULL, '2006-01-01', 'IBM System z', 13),
(233, NULL, '2000-01-01', 'IBM System p', 13),
(234, NULL, NULL, 'LC 575', NULL),
(235, NULL, NULL, 'Macintosh TV', NULL),
(236, NULL, NULL, 'Macintosh Performa', NULL),
(237, NULL, NULL, 'Macintosh II series', NULL),
(238, NULL, NULL, 'Power Macintosh 6400', NULL),
(239, NULL, NULL, 'Power Macintosh 6500', NULL),
(240, NULL, NULL, 'Apple PenLite', NULL),
(241, NULL, NULL, 'Wallstreet', NULL),
(242, NULL, NULL, 'Twentieth Anniversary Macintosh', NULL),
(243, NULL, NULL, 'Power Macintosh 5500', NULL),
(244, NULL, NULL, 'iBook G3', 1),
(245, NULL, NULL, 'Power Macintosh 5200 LC', NULL),
(246, NULL, NULL, 'Power Macintosh 5400', NULL),
(247, NULL, NULL, 'CM-1', NULL),
(248, '1995-01-01', '1983-01-01', 'MSX', 28),
(249, NULL, NULL, 'PlayStation 3', 17),
(250, NULL, '1986-01-01', 'MSX2', 29),
(251, NULL, '1988-01-01', 'MSX2+', 30),
(252, NULL, '1990-01-01', 'MSX turbo R', NULL),
(253, NULL, NULL, 'Panasonic FS A1GT', NULL),
(254, NULL, NULL, 'Panasonic FS A1ST', NULL),
(255, NULL, NULL, 'PDP-11', 10),
(256, NULL, NULL, 'PDP-1', 10),
(257, NULL, NULL, 'PDP-10', 10),
(258, NULL, NULL, 'PDP-8', 10),
(259, NULL, NULL, 'PDP-6', 10),
(260, NULL, NULL, 'DECSYSTEM-20', 10),
(261, NULL, NULL, 'PDP-7', 10),
(262, NULL, NULL, 'PDP-5', 10),
(263, NULL, NULL, 'PDP-12', 10),
(264, NULL, NULL, 'LINC', 10),
(265, NULL, NULL, 'PDP-14', 10),
(266, NULL, NULL, 'PDP-15', 10),
(267, NULL, NULL, 'PDP-16', 10),
(268, NULL, '2007-01-01', 'Cray X2', 31),
(269, NULL, '1982-01-01', 'Cray X-MP', 31),
(270, NULL, NULL, 'Evans & Sutherland ES-1', 32),
(271, NULL, '1980-01-01', 'Commodore VIC-20', 6),
(272, NULL, NULL, 'PowerBook 150', NULL),
(273, NULL, '2008-01-15', 'MacBook Air', 1),
(274, NULL, '1963-01-01', 'Digi-Comp I', 33),
(275, NULL, NULL, 'Digi-Comp', NULL),
(276, NULL, NULL, 'Digi-Comp II', 33),
(277, NULL, '1949-01-01', 'Manchester Mark I', NULL),
(278, NULL, '1948-01-01', 'Small-Scale Experimental Machine', NULL),
(279, NULL, NULL, 'Nintendo 64', 24),
(280, NULL, NULL, 'Game Boy Advance', 24),
(281, NULL, NULL, 'Game Boy', 24),
(282, NULL, NULL, 'Nintendo DS Lite', 24),
(283, NULL, '2004-01-01', 'Nintendo DS', 24),
(284, NULL, NULL, 'Game Boy Color', 24),
(285, NULL, NULL, 'Game Boy Advance SP', 24),
(286, NULL, NULL, 'Virtual Boy', 24),
(287, NULL, NULL, 'Game Boy Micro', 24),
(288, NULL, NULL, 'Roadrunner', 13),
(289, NULL, NULL, 'HP 9000', NULL),
(290, NULL, NULL, 'OMRON Luna-88K2', NULL),
(291, NULL, NULL, 'OMRON Luna-88K', 34),
(292, NULL, NULL, 'Motorola series 900', NULL),
(293, NULL, NULL, 'Motorola M8120', NULL),
(294, NULL, NULL, 'Triton Dolphin System 100', NULL),
(295, NULL, '1989-08-01', 'BBN TC2000', 35),
(296, NULL, NULL, 'WRT54G', NULL),
(297, NULL, '1992-01-01', 'ThinkPad', 36),
(298, '1998-01-01', '1993-01-01', 'Apple Newton', 1),
(299, NULL, '1937-01-01', 'Atanasoff-Berry Computer', NULL),
(300, '1974-01-01', '1962-01-01', 'Atlas Computer', NULL),
(301, NULL, NULL, 'ASUS Eee PC 901', 37),
(302, NULL, NULL, 'ASUS Eee PC 701', NULL),
(303, NULL, '1961-01-01', 'IBM 7030', 13),
(304, NULL, '1979-01-01', 'System/38', 13),
(305, '2000-01-01', '1983-01-01', 'System/36', 13),
(306, NULL, '1959-01-01', 'IBM 7090', 13),
(307, NULL, NULL, 'IBM RT', 13),
(308, NULL, '1964-01-01', 'System/360', 13),
(309, NULL, '1980-01-01', 'IBM 801', 13),
(310, NULL, '1959-01-01', 'IBM 1401', 13),
(311, '2006-01-01', '2001-01-01', 'ASCI White', 13),
(312, NULL, NULL, 'Blue Gene', 13),
(313, NULL, '1998-01-01', 'ASCI Blue Pacific', 13),
(314, NULL, '2007-06-01', 'iPhone', 1),
(315, NULL, '2007-10-17', 'Nokia N810', 16),
(316, NULL, NULL, 'EDSAC 2', NULL),
(317, NULL, NULL, 'Titan', NULL),
(318, NULL, NULL, 'Pilot ACE', NULL),
(319, NULL, '2008-10-29', 'HP Mini 1000', 27),
(320, NULL, '2008-04-15', 'HP 2133 Mini-Note PC', 27),
(321, NULL, '2008-12-04', 'Kogan Agora Pro', NULL),
(322, NULL, NULL, 'D-Series Machines', NULL),
(323, NULL, '1982-01-01', 'ZX Spectrum 48K', 25),
(324, NULL, '1982-01-01', 'ZX Spectrum 16K', 25),
(325, NULL, '1985-09-01', 'ZX Spectrum 128', 25),
(326, NULL, NULL, 'ZX Spectrum +3', 38),
(327, NULL, '1986-01-01', 'ZX Spectrum +2', 38),
(328, NULL, '1987-01-01', 'ZX Spectrum +2A', 38),
(329, NULL, '1984-06-01', 'ZX Spectrum +', 25),
(330, '2000-04-08', '1986-04-08', 'nouveaunomvalid', 22),
(331, NULL, NULL, 'modifier', 22),
(332, NULL, NULL, 'Dell Latitude', NULL),
(333, NULL, NULL, 'Toshiba Satellite', NULL),
(334, NULL, NULL, 'Timex Sinclair 2048', 23),
(335, NULL, NULL, 'Sprinter', NULL),
(336, NULL, NULL, 'Timex Computer 2048', NULL),
(337, NULL, NULL, 'Pentagon', NULL),
(338, NULL, NULL, 'Belle', NULL),
(339, NULL, NULL, 'Loki', 25),
(340, NULL, NULL, 'Hobbit', NULL),
(341, NULL, NULL, 'NeXT Computer', 19),
(342, NULL, NULL, 'TRS-80', NULL),
(343, NULL, '1980-01-01', 'TRS-80 Model 2', 5),
(344, NULL, NULL, 'TRS-80 Model 3', 5),
(345, NULL, '1989-01-01', 'STacy', NULL),
(346, NULL, '1990-01-01', 'ST BOOK', NULL),
(347, NULL, '1989-01-01', 'Atari 520 STE', NULL),
(348, NULL, NULL, 'modifier', NULL),
(653, NULL, NULL, 'nouveaunomvalid', NULL),
(350, '2000-04-08', '1986-04-08', 'nouveaunomvalid', 22),
(351, NULL, NULL, 'IBM 3270', NULL),
(352, NULL, NULL, 'CALDIC', NULL),
(353, NULL, NULL, 'Modbook', NULL),
(354, NULL, NULL, 'Compaq SystemPro', NULL),
(355, NULL, NULL, 'ARRA', NULL),
(356, NULL, NULL, 'IBM System Cluster 1350', NULL),
(357, NULL, NULL, 'Finite element machine', NULL),
(358, NULL, NULL, 'ES7000', NULL),
(359, NULL, NULL, 'HP MediaSmart Server', NULL),
(360, NULL, NULL, 'HP Superdome', NULL),
(361, NULL, '2008-01-01', 'IBM Power Systems', 13),
(362, NULL, NULL, 'Oslo Analyzer', NULL),
(363, NULL, NULL, 'Microsoft Softcard', NULL),
(364, NULL, NULL, 'WITCH', NULL),
(365, NULL, NULL, 'Analytical engine', NULL),
(366, NULL, NULL, 'EDVAC', NULL),
(367, NULL, NULL, 'BINAC', NULL),
(368, NULL, NULL, 'Earth Simulator', NULL),
(369, NULL, NULL, 'BARK', NULL),
(370, NULL, '1944-01-01', 'Harvard Mark I', 13),
(371, NULL, NULL, 'ILLIAC IV', NULL),
(372, NULL, NULL, 'ILLIAC II', NULL),
(373, NULL, NULL, 'ILLIAC III', NULL),
(374, NULL, NULL, 'Water integrator', NULL),
(375, NULL, NULL, 'CSIRAC', NULL),
(376, NULL, NULL, 'System X', NULL),
(377, NULL, NULL, 'Harvest', NULL),
(378, NULL, NULL, 'ChipTest', NULL),
(379, NULL, NULL, 'HiTech', NULL),
(380, NULL, NULL, 'Bomba', NULL),
(582, NULL, NULL, 'nouveaunomvalid', NULL),
(382, NULL, NULL, 'ASCI Red', NULL),
(383, NULL, NULL, 'ASCI Thors Hammer', NULL),
(384, NULL, '2005-01-01', 'ASCI Purple', 13),
(385, NULL, NULL, 'ASCI Blue Mountain', NULL),
(386, NULL, NULL, 'Columbia', NULL),
(387, NULL, NULL, 'HP Integrity', NULL),
(388, NULL, NULL, 'APEXC', NULL),
(389, NULL, NULL, 'Datasaab D2', NULL),
(390, NULL, NULL, 'BRLESC', NULL),
(391, NULL, NULL, 'DYSEAC', NULL),
(392, NULL, '1948-01-01', 'SSEC', 13),
(393, NULL, NULL, 'Hydra', NULL),
(394, NULL, NULL, 'FUJIC', NULL),
(395, NULL, NULL, 'RAYDAC', NULL),
(396, NULL, NULL, 'Harvard Mark III', NULL),
(397, NULL, NULL, 'DATAR', NULL),
(398, NULL, NULL, 'ReserVec', NULL),
(399, NULL, NULL, 'DASK', NULL),
(400, NULL, NULL, 'UTEC', NULL),
(401, NULL, NULL, 'DRTE Computer', NULL),
(402, NULL, NULL, 'PowerEdge', NULL),
(403, NULL, NULL, 'Apple Network Server', NULL),
(404, NULL, NULL, 'Goodyear MPP', NULL),
(405, NULL, NULL, 'Macintosh 128K technical details', NULL),
(406, NULL, NULL, 'Power Macintosh G3', NULL),
(407, NULL, NULL, 'CER-10', NULL),
(408, NULL, NULL, 'CER-20', NULL),
(409, NULL, '2002-01-01', 'IBM BladeCenter', 13),
(410, NULL, NULL, 'Wisconsin Integrally Synchronized Computer', NULL),
(411, NULL, NULL, 'Amstrad CPC', 38),
(412, NULL, NULL, 'Amstrad CPC 6128', 38),
(413, NULL, NULL, 'Amstrad CPC 664', 38),
(414, NULL, NULL, 'Amstrad CPC 464', 38),
(415, NULL, NULL, 'Intergraph', NULL),
(416, NULL, NULL, 'Enterprise', NULL),
(417, NULL, NULL, 'MTX500', NULL),
(419, NULL, '2009-02-01', 'Sony Vaio P', 17),
(420, NULL, NULL, 'VAIO', 17),
(421, NULL, NULL, 'Sony Vaio P VGN-P588E/Q', NULL),
(422, NULL, NULL, 'Sony Vaio P VGN-P530H/G', NULL),
(423, NULL, NULL, 'Sony Vaio P VGN-P530H/W', NULL),
(424, NULL, NULL, 'Sony Vaio P VGN-P530H/Q', NULL),
(425, NULL, NULL, 'Sony Vaio P VGN-P530H/R', NULL),
(426, NULL, NULL, 'Sony Vaio P VGN-P588E/R', NULL),
(427, NULL, NULL, 'Sony Vaio P VGN-P598E/Q', NULL),
(428, NULL, '1982-07-01', 'Timex Sinclair 1000', 23),
(429, NULL, NULL, 'Komputer 2086', NULL),
(430, NULL, NULL, 'Galaksija', NULL),
(431, NULL, NULL, 'Vector-06C', NULL),
(432, NULL, NULL, 'Elektronika BK', NULL),
(433, NULL, NULL, 'Sun386i', 39),
(434, '1989-01-01', '1985-01-01', 'Xerox Daybreak', NULL),
(435, NULL, NULL, 'Xerox NoteTaker', 26),
(436, NULL, '1965-01-01', 'D4a', NULL),
(437, NULL, NULL, 'LGP-30', NULL),
(438, NULL, NULL, 'LGP-21', NULL),
(439, NULL, '2008-05-01', 'ASUS Eee PC 900', 37),
(440, NULL, NULL, 'Atari TT030', NULL),
(441, NULL, NULL, 'Bi Am ZX-Spectrum 48/64', NULL),
(442, NULL, NULL, 'Bi Am ZX-Spectrum 128', NULL),
(443, NULL, NULL, 'PlayStation Portable', NULL),
(444, NULL, NULL, 'MSI Wind Netbook', NULL),
(445, NULL, '2009-04-21', 'Sharp Mebius NJ70A', NULL),
(446, NULL, NULL, 'HTC Snap', 41),
(447, NULL, NULL, 'Commodore Educator 64', 6),
(640, NULL, NULL, 'nouveaunomvalid', NULL),
(449, NULL, NULL, 'Commodore 65', 6),
(450, NULL, NULL, 'Commodore 16', 6),
(451, NULL, NULL, 'Commodore CBM-II', 6),
(452, NULL, NULL, 'Commodore Plus/4', 6),
(453, NULL, NULL, 'Commodore LCD', 6),
(454, NULL, NULL, 'Commodore MAX Machine', 6),
(455, NULL, NULL, 'Aster CT-80', NULL),
(456, '2009-01-01', '2009-01-01', 'Test', NULL),
(457, NULL, NULL, 'MSI GX723', NULL),
(458, NULL, '2009-05-22', 'Eee PC 1000HV', NULL),
(459, NULL, '1983-01-01', 'VTech Laser 200', NULL),
(460, NULL, NULL, 'CrunchPad', NULL),
(461, NULL, '1990-01-01', 'Neo Geo', NULL),
(462, NULL, NULL, 'Sega Mega Drive', NULL),
(463, NULL, NULL, 'Sega Master System', NULL),
(464, NULL, NULL, 'TurboGrafx-16', NULL),
(465, NULL, NULL, 'Sun-3', 39),
(466, NULL, NULL, 'Pleiades', NULL),
(467, NULL, NULL, 'IBM Sequoia', NULL),
(468, NULL, NULL, 'Inves Spectrum 48k plus', NULL),
(469, NULL, NULL, 'iPhone 3G', NULL),
(470, NULL, NULL, 'iPhone 3GS', NULL),
(471, NULL, NULL, 'Beagle Board', 40),
(472, NULL, NULL, 'HP nPar', NULL),
(473, NULL, NULL, 'MacBook Family', NULL),
(474, NULL, NULL, 'Reservisor', NULL),
(475, NULL, NULL, 'BladeSystem', NULL),
(476, NULL, NULL, 'lenovo thinkpad t60p', NULL),
(477, NULL, NULL, 'lenovo thinkpad x200', 36),
(478, NULL, NULL, 'lenovo thinkpad t60', NULL),
(479, NULL, NULL, 'lenovo thinkpad w700', NULL),
(480, NULL, NULL, 'lenovo thinkpad t41', NULL),
(481, NULL, NULL, 'lenovo thinkpad z61p', NULL),
(482, NULL, NULL, 'lenovo thinkpad x61s', NULL),
(483, NULL, NULL, 'lenovo thinkpad t43', NULL),
(484, NULL, NULL, 'lenovo thinkpad r400', NULL),
(485, NULL, NULL, 'lenovo thinkpad x60s', NULL),
(486, NULL, NULL, 'lenovo thinkpad x301', NULL),
(487, NULL, NULL, 'lenovo thinkpad t42', NULL),
(488, NULL, NULL, 'lenovo thinkpad r61', NULL),
(489, NULL, NULL, 'lenovo thinkpad w500', NULL),
(490, NULL, NULL, 'lenovo thinkpad sl400', NULL),
(491, NULL, NULL, 'lenovo thinkpad x40', NULL),
(492, NULL, NULL, 'lenovo thinkpad x200 tablet', 36),
(493, NULL, NULL, 'lenovo thinkpad t400s', NULL),
(494, NULL, '2009-10-01', 'Nokia N900', 16),
(495, NULL, NULL, 'Internet Tablet', NULL),
(496, '1993-01-01', '1986-01-01', 'Meiko Computing Surface', NULL),
(497, NULL, NULL, 'CS-2', NULL),
(498, NULL, '1952-01-01', 'IBM 701', 13),
(499, NULL, '1975-01-01', 'IBM 5100', 13),
(500, NULL, '1958-01-01', 'AN/FSQ-7', 13),
(501, NULL, '1960-01-01', 'AN/FSQ-32', 13),
(502, NULL, '1949-01-01', 'IBM CPC', 13),
(503, '1983-01-01', '1978-01-01', 'System/34', 13),
(504, NULL, '1975-01-01', 'System/32', 13),
(505, '1985-01-01', '1969-01-01', 'System/3', 13),
(506, NULL, '1956-01-01', 'IBM 305', 13),
(507, NULL, NULL, 'English Electric DEUCE', NULL),
(508, NULL, NULL, 'CER-203', NULL),
(509, NULL, NULL, 'CER-22', NULL),
(510, NULL, NULL, 'Kentucky Linux Athlon Testbed', NULL),
(511, NULL, NULL, 'QNAP TS-101', NULL),
(512, '2011-03-02', '2010-01-01', 'iPad', 1),
(513, NULL, NULL, 'iPhone 2G', NULL),
(514, NULL, NULL, 'Inslaw', NULL),
(515, NULL, '2010-07-01', 'WePad', NULL),
(516, NULL, NULL, 'MacBook Parts', 1),
(517, NULL, NULL, 'MacBook 13-inch Core 2 Duo 2.13GHz (MC240LL/A) DDR2 Model', 1),
(518, NULL, NULL, 'MacBook 13-inch Core 2 Duo 2.13GHz (MC240T/A) DDR2 Model', NULL),
(519, NULL, NULL, 'MacBook 13-inch Core 2 Duo 2.13GHz (MC240X/A) DDR2 Model', NULL),
(520, NULL, NULL, 'MacBook 13-inch Core 2 Duo 2.26GHz (Unibody MC207LL/A) DDR3 Model', NULL),
(521, NULL, NULL, 'MC240LL/A', NULL),
(522, NULL, NULL, 'D.K.COMMUNICATION', NULL),
(523, NULL, NULL, 'iPhone 4', 1),
(524, NULL, '2010-03-23', 'Nintendo 3DS', 24),
(525, NULL, '2010-01-01', 'ASUS Eee PC 1005PE', 37),
(526, NULL, NULL, 'National Law Enforcement System', NULL),
(527, NULL, NULL, 'BlackBerry PlayBook', 42),
(528, NULL, '2009-10-20', 'Barnes & Noble nook', NULL),
(529, NULL, NULL, 'SAM Coupé', NULL),
(530, NULL, '2008-10-22', 'HTC Dream', 41),
(531, NULL, '2010-09-02', 'Samsung Galaxy Tab', 43),
(532, NULL, '2010-09-27', 'BlackBerry PlayBook', 42),
(533, NULL, NULL, 'Tianhe-I', NULL),
(534, NULL, NULL, 'Kno', NULL),
(535, NULL, NULL, 'ThinkPad 701 C', NULL),
(536, NULL, NULL, 'ThinkPad 340 CSE', NULL),
(537, NULL, NULL, 'ThinkPad 755 CX', NULL),
(538, NULL, NULL, 'ThinkPad 755 CE', NULL),
(539, NULL, NULL, 'ThinkPad 370 C', NULL),
(540, NULL, '1983-01-01', 'Coleco Adam', NULL),
(541, NULL, NULL, 'Nebulae', NULL),
(617, NULL, NULL, 'nouveaunomvalid', NULL),
(544, NULL, NULL, 'Archos 101', NULL),
(545, NULL, NULL, 'Fujitsu Lifebook T900', NULL),
(546, NULL, NULL, 'Motorola Xoom', NULL),
(547, NULL, NULL, 'ViewSonic G Tablet', NULL),
(548, NULL, '1982-01-01', 'DEC Professional', 10),
(549, NULL, '1994-11-07', 'DEC Multia', 10),
(550, NULL, NULL, 'DEC Firefly', 10),
(551, NULL, NULL, 'DEC 3000 AXP', 10),
(552, NULL, '1993-05-25', 'DEC 2000 AXP', 10),
(553, NULL, '1992-11-10', 'DEC 4000 AXP', 10),
(554, NULL, '1992-11-10', 'DEC 7000/10000 AXP', 10),
(555, NULL, NULL, 'DEC Professional 350', NULL),
(556, NULL, NULL, 'DEC Rainbow 100', NULL),
(557, NULL, NULL, 'DEC Professional 325', NULL),
(558, NULL, NULL, 'DECmate II', 10),
(559, NULL, NULL, 'DECmate', 10),
(560, NULL, NULL, 'DECsystem', 10),
(561, NULL, NULL, 'NetApp Filer', NULL),
(562, NULL, NULL, 'DEC GT40', 10),
(563, NULL, NULL, 'ecoATM', NULL),
(564, NULL, NULL, 'MindWave BrainCubed Education Bundle', NULL),
(565, NULL, NULL, 'PalmPilot', NULL),
(566, NULL, NULL, 'Upcoming iPhone 5', 1),
(567, NULL, NULL, 'Dell Inspiron 560 Desktop Computer ', NULL),
(568, NULL, NULL, 'IPad 2', 1),
(569, NULL, '2011-02-09', 'HP TouchPad', 27),
(570, NULL, '2011-02-09', 'HP Veer', 27),
(571, NULL, NULL, 'Lenovo Thinkpad Edge 11', 36),
(572, NULL, NULL, 'Dell Vostro', NULL),
(573, NULL, '2008-01-01', 'Gateway LT3103U', NULL),
(574, NULL, '2011-10-14', 'iPhone 4S', 1);

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `id` bigint(20) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `lastPasswordResetDate` datetime DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`id`, `enabled`, `lastPasswordResetDate`, `password`, `username`) VALUES
(1, b'1', '2018-06-01 00:00:00', '$2a$10$hbxecwitQQ.dDT4JOFzQAulNySFwEpaFLw38jda6Td.Y/cOiRzDFu', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `User_authority`
--

CREATE TABLE `User_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `User_authority`
--

INSERT INTO `User_authority` (`user_id`, `authority_id`) VALUES
(1, 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Authorities`
--
ALTER TABLE `Authorities`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Company`
--
ALTER TABLE `Company`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Computer`
--
ALTER TABLE `Computer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKd6xe05bfevcnsbkrkrngmcra6` (`company_id`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `User_authority`
--
ALTER TABLE `User_authority`
  ADD PRIMARY KEY (`user_id`,`authority_id`),
  ADD KEY `FKax5lsyregto6c1v0bhpdytfn3` (`authority_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Authorities`
--
ALTER TABLE `Authorities`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `Company`
--
ALTER TABLE `Company`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=92;
--
-- AUTO_INCREMENT for table `Computer`
--
ALTER TABLE `Computer`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=667;
--
-- AUTO_INCREMENT for table `User`
--
ALTER TABLE `User`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
