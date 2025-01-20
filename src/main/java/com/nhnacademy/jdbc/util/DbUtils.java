package com.nhnacademy.jdbc.util;

import java.time.Duration;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class DbUtils {
    public DbUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final DataSource DATASOURCE;

    static {
        BasicDataSource basicDataSource = new BasicDataSource();
        // todo#0 {ip},{database},{username},{password} 설정 합니다.
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/Students");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("P@ssw0rd");

        // 커넥션 풀 크기 설정
        basicDataSource.setInitialSize(15); // 초기 커넥션 수
        basicDataSource.setMaxTotal(15); // 최대 커넥션 수
        basicDataSource.setMaxIdle(15); // 최대 유휴 커넥션 수
        basicDataSource.setMinIdle(15); // 최소 유휴 커넥션 수

        // 연결 대기 시간 및 유효성 검사 설정
        basicDataSource.setMaxWait(Duration.ofSeconds(2)); // 최대 대기 시간 2초
        basicDataSource.setValidationQuery("select 1"); // 유효성 검사 쿼리
        basicDataSource.setTestOnBorrow(true); // 커넥션 대여 시 유효성 검사

        // DataSource 설정
        DATASOURCE = basicDataSource;
    }

    public static DataSource getDataSource() {
        return DATASOURCE;
    }
}
