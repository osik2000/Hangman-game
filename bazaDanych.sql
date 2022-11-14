-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jul 23, 2022 at 11:42 AM
-- Server version: 8.0.13-4
-- PHP Version: 7.2.24-0ubuntu0.18.04.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aeCwkdZjzZ`
--

-- --------------------------------------------------------

--
-- Table structure for table `Hasla`
--

CREATE TABLE `Hasla` (
  `idHasla` int(4) NOT NULL,
  `trescHasla` varchar(50) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `kategoriaHasla` varchar(50) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Dumping data for table `Hasla`
--

INSERT INTO `Hasla` (`idHasla`, `trescHasla`, `kategoriaHasla`) VALUES
(1, 'Elliot Alderson', 'Postacie fikcyjne'),
(2, 'Ferdynand Kiepski', 'Postacie fikcyjne'),
(3, 'Harry Potter', 'Postacie fikcyjne'),
(4, 'Katniss Everdeen', 'Postacie fikcyjne'),
(5, 'Michael De Santa', 'Postacie fikcyjne'),
(6, 'Tony Stark', 'Postacie fikcyjne'),
(7, 'Walter White', 'Postacie fikcyjne'),
(8, 'Bez pracy nie ma kołaczy', 'Powiedzenia i przysłowia'),
(9, 'Ciekawość to pierwszy stopień do piekła', 'Powiedzenia i przysłowia'),
(10, 'Gdyby kózka nie skakała, to by nóżki nie złamała', 'Powiedzenia i przysłowia'),
(11, 'Gdzie kucharek sześć, tam nie ma co jeść', 'Powiedzenia i przysłowia'),
(12, 'Głupi ma zawsze szczęście', 'Powiedzenia i przysłowia'),
(13, 'I wilk syty, i owca cała', 'Powiedzenia i przysłowia'),
(14, 'Jak sobie pościelesz, tak się wyśpisz', 'Powiedzenia i przysłowia'),
(15, 'Kto mieczem wojuje, od miecza ginie', 'Powiedzenia i przysłowia'),
(16, 'Kto pod kim dołki kopie, ten sam w nie wpada', 'Powiedzenia i przysłowia'),
(17, 'Nie chwal dnia przed zachodem słońca', 'Powiedzenia i przysłowia'),
(18, 'Niedaleko pada jabłko od jabłoni', 'Powiedzenia i przysłowia'),
(19, 'Szewc bez butów chodzi', 'Powiedzenia i przysłowia'),
(20, 'Wszystko, co dobre, szybko się kończy', 'Powiedzenia i przysłowia'),
(21, 'Breaking Bad', 'Filmy i seriale'),
(22, 'Gra o Tron', 'Filmy i seriale'),
(23, 'Mr. Robot', 'Filmy i seriale'),
(24, 'Stranger Things', 'Filmy i seriale'),
(25, 'Szkoła dla Elity', 'Filmy i seriale'),
(26, 'Trzynaście powodów', 'Filmy i seriale'),
(27, 'Futbol amerykański', 'Sport'),
(28, 'Lekkoatletyka', 'Sport'),
(29, 'Piłka nożna', 'Sport'),
(30, 'Piłka ręczna', 'Sport'),
(31, 'Tenis stołowy', 'Sport'),
(32, 'Wspinaczka sportowa‎', 'Sport'),
(33, 'Bohemian Rhapsody', 'Filmy i seriale'),
(34, 'Chłopaki nie płaczą', 'Filmy i seriale'),
(35, 'Forrest Gump', 'Filmy i seriale'),
(36, 'Jak rozpętałem drugą wojnę światową', 'Filmy i seriale'),
(37, 'Leon Zawodowiec', 'Filmy i seriale'),
(38, 'Lot nad kukułczym gniazdem', 'Filmy i seriale'),
(39, 'Milczenie Owiec', 'Filmy i seriale'),
(40, 'Ojciec Chrzestny', 'Filmy i seriale'),
(41, 'Requiem dla snu', 'Filmy i seriale'),
(42, 'Skazani na Shawshank', 'Filmy i seriale'),
(43, 'Stowarzyszenie Umarłych Poetów', 'Filmy i seriale'),
(44, 'Backstreet Boys', 'Zespoły muzyczne'),
(45, 'Fleetwood Mac', 'Zespoły muzyczne'),
(46, 'Guns N\' Roses', 'Zespoły muzyczne'),
(47, 'Led Zeppelin', 'Zespoły muzyczne'),
(48, 'Pink Floyd', 'Zespoły muzyczne'),
(49, 'The Beatles', 'Zespoły muzyczne'),
(50, 'The Rolling Stones', 'Zespoły muzyczne'),
(51, 'Złote jabłko', 'Minecraft'),
(52, 'Smok endu', 'Minecraft'),
(53, 'Świecąca kałamarnica', 'Minecraft'),
(54, 'Osadnik', 'Minecraft'),
(55, 'Żelazny golem', 'Minecraft'),
(56, 'Mroczny szkielet', 'Minecraft'),
(57, 'Wither', 'Minecraft'),
(58, 'Złote jabłko', 'Minecraft'),
(59, 'Nether', 'Minecraft'),
(60, 'Enderman', 'Minecraft'),
(61, 'Diamentowy miecz', 'Minecraft'),
(62, 'Diamentowy kilof', 'Minecraft'),
(63, 'Miasto Endu', 'Minecraft'),
(64, 'Spiderman', 'Postacie fikcyjne'),
(65, 'Thor', 'Postacie fikcyjne'),
(66, 'Hulk', 'Postacie fikcyjne'),
(67, 'Iron Man', 'Postacie fikcyjne'),
(68, 'Kapitan Ameryka', 'Postacie fikcyjne'),
(69, 'Doktor Strange', 'Postacie fikcyjne'),
(70, 'Czarna Wdowa', 'Postacie fikcyjne'),
(71, 'Venom', 'Postacie fikcyjne'),
(72, 'Doktor Octopus', 'Postacie fikcyjne'),
(73, 'Venom', 'Postacie fikcyjne'),
(74, 'Kapitan Marvel', 'Postacie fikcyjne'),
(75, 'Mysterio', 'Postacie fikcyjne'),
(76, 'Procesor', 'Podzespoły komputerowe'),
(77, 'Karta graficzna', 'Podzespoły komputerowe'),
(78, 'Karta dźwiękowa', 'Podzespoły komputerowe'),
(79, 'Płyta główna', 'Podzespoły komputerowe'),
(80, 'Pamięć RAM', 'Podzespoły komputerowe'),
(81, 'Dysk twardy', 'Podzespoły komputerowe'),
(82, 'Zasilacz', 'Podzespoły komputerowe'),
(83, 'Obudowa', 'Podzespoły komputerowe'),
(84, 'Radiator', 'Podzespoły komputerowe'),
(85, 'Karta sieciowa', 'Podzespoły komputerowe'),
(86, 'Bedoes', 'Znani muzycy'),
(87, 'Mata', 'Znani muzycy'),
(88, 'Białas', 'Znani muzycy'),
(89, 'Doja Cat', 'Znani muzycy'),
(90, 'Kanye West', 'Znani muzycy'),
(91, 'Bedoes', 'Znani muzycy'),
(92, 'Kizo', 'Znani muzycy'),
(93, 'Taco Hemingway', 'Znani muzycy'),
(94, 'Quebonafide', 'Znani muzycy'),
(95, 'Masny Ben', 'Znani muzycy'),
(96, 'Malik Montana', 'Znani muzycy'),
(97, 'Lil Pump', 'Znani muzycy'),
(98, 'Vito Bambino', 'Znani muzycy'),
(99, 'Sanah', 'Znani muzycy'),
(100, 'Young Leosia', 'Znani muzycy'),
(105, 'test', 'test');

-- --------------------------------------------------------

--
-- Table structure for table `Ranking`
--

CREATE TABLE `Ranking` (
  `loginUzytk` varchar(20) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `liczbaPkt` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Dumping data for table `Ranking`
--

INSERT INTO `Ranking` (`loginUzytk`, `liczbaPkt`) VALUES
('admin', 0),
('mrDziekan', 5),
('Stefan', 0),
('Romek', 0),
('odolczykd', 8),
('osik2000', 1),
('Zyrek', 1),
('bartek123', 0),
('przyklad', 2);

-- --------------------------------------------------------

--
-- Table structure for table `RozwiazaneHasla`
--

CREATE TABLE `RozwiazaneHasla` (
  `loginUzytk` varchar(20) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `idHasla` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Dumping data for table `RozwiazaneHasla`
--

INSERT INTO `RozwiazaneHasla` (`loginUzytk`, `idHasla`) VALUES
('Stefan', 1),
('mrDziekan', 1),
('Romek', 1),
('mrDziekan', 2),
('mrDziekan', 3),
('mrDziekan', 4),
('mrDziekan', 5),
('mrDziekan', 6),
('mrDziekan', 7),
('mrDziekan', 8),
('mrDziekan', 9),
('mrDziekan', 10),
('mrDziekan', 11),
('mrDziekan', 12),
('mrDziekan', 13),
('mrDziekan', 14),
('mrDziekan', 15),
('mrDziekan', 16),
('mrDziekan', 17),
('mrDziekan', 18),
('mrDziekan', 19),
('mrDziekan', 20),
('mrDziekan', 21),
('mrDziekan', 22),
('mrDziekan', 23),
('mrDziekan', 24),
('mrDziekan', 25),
('mrDziekan', 26),
('mrDziekan', 27),
('mrDziekan', 28),
('mrDziekan', 29),
('mrDziekan', 30),
('mrDziekan', 31),
('mrDziekan', 32),
('mrDziekan', 33),
('mrDziekan', 34),
('mrDziekan', 35),
('mrDziekan', 36),
('mrDziekan', 37),
('mrDziekan', 38),
('mrDziekan', 39),
('mrDziekan', 40),
('mrDziekan', 41),
('mrDziekan', 42),
('mrDziekan', 43),
('mrDziekan', 44),
('mrDziekan', 45),
('mrDziekan', 46),
('mrDziekan', 47),
('mrDziekan', 48),
('mrDziekan', 49),
('mrDziekan', 50),
('mrDziekan', 51),
('mrDziekan', 52),
('mrDziekan', 53),
('mrDziekan', 54),
('mrDziekan', 55),
('mrDziekan', 56),
('mrDziekan', 57),
('mrDziekan', 58),
('mrDziekan', 59),
('mrDziekan', 60),
('mrDziekan', 61),
('mrDziekan', 62),
('mrDziekan', 63),
('mrDziekan', 64),
('mrDziekan', 65),
('mrDziekan', 66),
('mrDziekan', 67),
('mrDziekan', 68),
('mrDziekan', 69),
('mrDziekan', 70),
('mrDziekan', 71),
('mrDziekan', 72),
('mrDziekan', 73),
('mrDziekan', 74),
('mrDziekan', 75),
('mrDziekan', 76),
('mrDziekan', 77),
('mrDziekan', 78),
('mrDziekan', 79),
('mrDziekan', 80),
('mrDziekan', 81),
('mrDziekan', 82),
('mrDziekan', 83),
('mrDziekan', 84),
('mrDziekan', 85),
('mrDziekan', 86),
('mrDziekan', 87),
('mrDziekan', 88),
('mrDziekan', 89),
('mrDziekan', 90),
('mrDziekan', 91),
('mrDziekan', 92),
('mrDziekan', 93),
('mrDziekan', 94),
('mrDziekan', 95),
('mrDziekan', 96),
('mrDziekan', 97),
('mrDziekan', 98),
('mrDziekan', 99),
('mrDziekan', 100),
('odolczykd', 36),
('odolczykd', 65),
('osik2000', 41),
('przyklad', 38);

-- --------------------------------------------------------

--
-- Table structure for table `Uzytkownicy`
--

CREATE TABLE `Uzytkownicy` (
  `loginUzytk` varchar(20) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `hasloUzytk` varchar(20) CHARACTER SET utf8 COLLATE utf8_polish_ci NOT NULL,
  `opisUzytk` varchar(420) CHARACTER SET utf8 COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Dumping data for table `Uzytkownicy`
--

INSERT INTO `Uzytkownicy` (`loginUzytk`, `hasloUzytk`, `opisUzytk`) VALUES
('admin', 'admin', 'administrator (mogę wszystko EZ)'),
('bartek123', 'kasztan12', 'szukam laski chetne pisza w opisie \"123\"'),
('mrDziekan', 'UMK', 'nic....'),
('odolczykd', 'password123', 'jestem top graczem w tę grę EZ'),
('osik2000', 'ilovejava', 'Paweł Osiński :)'),
('przyklad', 'aaaaa', 'abcde'),
('Romek', 'atomek', 'Zagrajmy w wisielca!'),
('Stefan', 'haslo...', 'Jestem Stefek :D'),
('Zyrek', 'zyrek11', 'jestem fanem kebaba Jaśmin w Toruniu');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Hasla`
--
ALTER TABLE `Hasla`
  ADD PRIMARY KEY (`idHasla`);

--
-- Indexes for table `Ranking`
--
ALTER TABLE `Ranking`
  ADD KEY `loginUzytk` (`loginUzytk`);

--
-- Indexes for table `RozwiazaneHasla`
--
ALTER TABLE `RozwiazaneHasla`
  ADD KEY `idHasla` (`idHasla`),
  ADD KEY `loginUzytkk` (`loginUzytk`);

--
-- Indexes for table `Uzytkownicy`
--
ALTER TABLE `Uzytkownicy`
  ADD PRIMARY KEY (`loginUzytk`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Hasla`
--
ALTER TABLE `Hasla`
  MODIFY `idHasla` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=106;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Ranking`
--
ALTER TABLE `Ranking`
  ADD CONSTRAINT `loginUzytk` FOREIGN KEY (`loginUzytk`) REFERENCES `Uzytkownicy` (`loginuzytk`);

--
-- Constraints for table `RozwiazaneHasla`
--
ALTER TABLE `RozwiazaneHasla`
  ADD CONSTRAINT `idHasla` FOREIGN KEY (`idHasla`) REFERENCES `Hasla` (`idhasla`),
  ADD CONSTRAINT `loginUzytkk` FOREIGN KEY (`loginUzytk`) REFERENCES `Uzytkownicy` (`loginuzytk`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
