-- phpMyAdmin SQL Dump
-- version 2.11.9.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generato il: 13 Mar, 2009 at 03:17 PM
-- Versione MySQL: 5.0.67
-- Versione PHP: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `vanet`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `logs`
--

CREATE TABLE IF NOT EXISTS `logs` (
  `log_id` int(11) unsigned zerofill NOT NULL auto_increment,
  `level` varchar(255) NOT NULL,
  `class_name` varchar(255) NOT NULL,
  `method_name` varchar(255) NOT NULL,
  `message` text NOT NULL,
  PRIMARY KEY  (`log_id`),
  KEY `level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dump dei dati per la tabella `logs`
--

