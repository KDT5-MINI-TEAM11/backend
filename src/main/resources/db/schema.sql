CREATE TABLE `user_tb` (
                           `id`                BIGINT NOT NULL AUTO_INCREMENT,
                           `user_email`        VARCHAR(100) NOT NULL UNIQUE,
                           `user_password`     VARCHAR(100) NOT NULL,
                           `user_name`         VARCHAR(50) NOT NULL,
                           `profile_thumb_url` VARCHAR(255),
                           `position`          VARCHAR(30) NOT NULL,
                           `phone_number`      VARCHAR(20) NOT NULL UNIQUE,
                           `used_vacation`     INT UNSIGNED NOT NULL,
                           `created_at`         DATETIME NOT NULL default now(),
                           `updated_at`         DATETIME NOT NULL  default now() on update now(),
                           PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE `refresh_token_tb` (
                                    `id`       BIGINT NOT NULL AUTO_INCREMENT,
                                    `user_id`  BIGINT NOT NULL UNIQUE,
                                    `refresh_token_id`    VARCHAR(20) NOT NULL,
                                    PRIMARY KEY (`id`, `user_id`),
                                    FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE `signin_log_tb` (
                                 `id`          BIGINT NOT NULL AUTO_INCREMENT,
                                 `user_id`     BIGINT NOT NULL,
                                 `ip_address`  VARCHAR(45) NOT NULL,
                                 `user_agent`  VARCHAR(255) NOT NULL,
                                 `created_at`   DATETIME NOT NULL default now(),
                                 PRIMARY KEY (`id`, `user_id`),
                                 FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE `schedule_tb` (
                               `id`            BIGINT NOT NULL AUTO_INCREMENT,
                               `user_id`       BIGINT NOT NULL,
                               `schedule_type`  VARCHAR(20) NOT NULL,
                               `state`         VARCHAR(20) NOT NULL,
                               `start_date`    TIMESTAMP(9) NOT NULL,
                               `end_date`      TIMESTAMP(9) NOT NULL,
                               `created_at`     DATETIME NOT NULL default now(),
                               `updated_at`     DATETIME NOT NULL  default now() on update now(),
                               PRIMARY KEY (`id`),
                               FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`)
) DEFAULT CHARSET=utf8mb4;