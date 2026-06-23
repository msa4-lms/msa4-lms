package com.msa4lms.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MigrationRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("=== DB Migration Start ===");

        // 1. lectures 테이블 확장
        try {
            jdbcTemplate.execute("ALTER TABLE `lectures` " +
                    "ADD COLUMN `midterm_ratio` INT NOT NULL DEFAULT 30, " +
                    "ADD COLUMN `final_ratio` INT NOT NULL DEFAULT 30, " +
                    "ADD COLUMN `assignment_ratio` INT NOT NULL DEFAULT 30, " +
                    "ADD COLUMN `attendance_ratio` INT NOT NULL DEFAULT 10");
            log.info("lectures 테이블 컬럼 추가 성공 (midterm_ratio, final_ratio, assignment_ratio, attendance_ratio)");
        } catch (Exception e) {
            log.info("lectures 테이블 컬럼 추가 스킵 (이미 컬럼이 존재할 수 있음): {}", e.getMessage());
        }



        // 2. lectures 테이블 강의계획서(syllabus) 컬럼 추가
        try {
            jdbcTemplate.execute("ALTER TABLE `lectures` ADD COLUMN `syllabus` TEXT DEFAULT NULL");
            log.info("lectures 테이블 컬럼 추가 성공 (syllabus)");
        } catch (Exception e) {
            log.info("lectures 테이블 컬럼 추가 스킵 (이미 컬럼이 존재할 수 있음): {}", e.getMessage());
        }

        log.info("=== DB Migration End ===");
    }
}
