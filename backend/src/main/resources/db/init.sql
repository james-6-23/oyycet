CREATE TABLE IF NOT EXISTS cet_user (
  id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(255) NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  nickname VARCHAR(50) NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
  status TINYINT NOT NULL DEFAULT 1,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cet_paper (
  id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  year SMALLINT NULL,
  month TINYINT NULL,
  paper_no TINYINT NULL,
  difficulty VARCHAR(20) NULL,
  type VARCHAR(20) NULL,
  duration_min SMALLINT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  attempts INT NOT NULL DEFAULT 0,
  listening_ref_paper_id BIGINT UNSIGNED NULL,
  listening_ref_paper_no TINYINT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT NOT NULL DEFAULT 0,
  INDEX idx_paper_status (status),
  INDEX idx_paper_year_month_no (year, month, paper_no),
  CONSTRAINT fk_paper_listening_ref FOREIGN KEY (listening_ref_paper_id) REFERENCES cet_paper(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cet_paper_question (
  id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  paper_id BIGINT UNSIGNED NOT NULL,
  type VARCHAR(20) NOT NULL,
  sub_type VARCHAR(50) NULL,
  question_no INT NULL,
  score DECIMAL(5,2) NOT NULL DEFAULT 1.00,
  content TEXT NULL,
  options_json JSON NULL,
  correct_answer TEXT NULL,
  explanation TEXT NULL,
  passage TEXT NULL,
  word_bank_json JSON NULL,
  audio_url VARCHAR(500) NULL,
  audio_start_time INT NULL,
  audio_end_time INT NULL,
  passage_group VARCHAR(50) NULL,
  directions TEXT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_question_paper_sort (paper_id, sort_order),
  CONSTRAINT fk_question_paper FOREIGN KEY (paper_id) REFERENCES cet_paper(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cet_practice_record (
  id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT UNSIGNED NOT NULL,
  paper_id BIGINT UNSIGNED NOT NULL,
  score INT NOT NULL,
  duration_seconds INT NOT NULL DEFAULT 0,
  correct_count INT NOT NULL DEFAULT 0,
  total_count INT NOT NULL DEFAULT 0,
  answer_detail_json JSON NULL,
  finish_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT NOT NULL DEFAULT 0,
  INDEX idx_record_user_time (user_id, finish_time),
  INDEX idx_record_paper (paper_id),
  CONSTRAINT fk_record_user FOREIGN KEY (user_id) REFERENCES cet_user(id),
  CONSTRAINT fk_record_paper FOREIGN KEY (paper_id) REFERENCES cet_paper(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
