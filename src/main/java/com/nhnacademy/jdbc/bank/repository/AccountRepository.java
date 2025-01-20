package com.nhnacademy.jdbc.bank.repository;

import java.sql.Connection;
import java.util.Optional;

import com.nhnacademy.jdbc.bank.domain.Account;

public interface AccountRepository {
    // 계좌조회
    Optional<Account> findByAccountNumber(Connection connection, long accountNumber);

    // 계좌등록
    int save(Connection connection, Account account);

    // 계좌 count -> 존재하면 1 return
    int countByAccountNumber(Connection connection, long accountNumber);

    // 입금
    int deposit(Connection connection, long accountNumber, long amount);

    // 출금
    int withdraw(Connection connection, long accountNumber, long amount);

    // 계좌삭제
    int deleteByAccountNumber(Connection connection, long accountNumber);

}
