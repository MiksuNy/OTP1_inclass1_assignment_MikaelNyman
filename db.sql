CREATE DATABASE IF NOT EXISTS tempconverter
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tempconverter;

CREATE TABLE temperature_record (
	id         INT AUTO_INCREMENT PRIMARY KEY,
	input      DOUBLE,
	result     DOUBLE,
	conversion VARCHAR(50)
);