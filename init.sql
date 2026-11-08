CREATE TABLE `temperature_record` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `input` DOUBLE NULL DEFAULT NULL,
    `result` DOUBLE NULL DEFAULT NULL,
    `conversion_en_us` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
    `conversion_fi_fi` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
    `conversion_ja_jp` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
    `conversion_sv_se` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
    `conversion_ar_sa` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
    PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8mb4_unicode_ci'
ENGINE=InnoDB;