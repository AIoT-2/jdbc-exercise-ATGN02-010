package com.nhnacademy.jdbc.bank.service.impl;

import java.sql.Connection;
import java.util.Optional;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.exception.AccountAlreadyExistException;
import com.nhnacademy.jdbc.bank.exception.AccountNotFoundException;
import com.nhnacademy.jdbc.bank.exception.BalanceNotEnoughException;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;
import com.nhnacademy.jdbc.bank.service.BankService;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository;

    public BankServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccount(Connection connection, long accountNumber) {
        // todo#11 계좌-조회
        Optional<Account> account = accountRepository.findByAccountNumber(connection, accountNumber);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountNumber);
        }

        return account.get();
    }

    @Override
    public void createAccount(Connection connection, Account account) {
        // todo#12 계좌-등록
        if (isExistAccount(connection, account.getAccountNumber())) {
            throw new AccountAlreadyExistException(account.getAccountNumber());
        }

        int result = accountRepository.save(connection, account);
        if (result < 1) {
            throw new RuntimeException("do not save account");
        }
    }

    @Override
    public boolean depositAccount(Connection connection, long accountNumber, long amount) {
        // todo#13 예금, 계좌가 존재하는지 체크 -> 예금실행 -> 성공 true, 실패 false;
        if (!isExistAccount(connection, accountNumber)) {
            throw new AccountNotFoundException(accountNumber);
        }

        int result = accountRepository.deposit(connection, accountNumber, amount);

        return result > 0;
    }

    @Override
    public boolean withdrawAccount(Connection connection, long accountNumber, long amount) {
        // todo#14 출금, 계좌가 존재하는지 체크 -> 출금가능여부 체크 -> 출금실행, 성공 true, 실폐 false 반환
        if (!isExistAccount(connection, accountNumber)) {
            throw new AccountNotFoundException(accountNumber);
        }

        Account account = getAccount(connection, accountNumber);
        if (!account.isWithdraw(amount)) {
            throw new BalanceNotEnoughException(accountNumber);
        }

        int result = accountRepository.withdraw(connection, accountNumber, amount);

        return result > 0;
    }

    @Override
    public void transferAmount(Connection connection, long accountNumberFrom, long accountNumberTo, long amount) {
        // todo#15 계좌 이체 accountNumberFrom -> accountNumberTo 으로 amount만큼 이체
        if (!isExistAccount(connection, accountNumberFrom)) {
            throw new AccountNotFoundException(accountNumberFrom);
        }
        if (!isExistAccount(connection, accountNumberTo)) {
            throw new AccountNotFoundException(accountNumberTo);
        }

        Optional<Account> accountFromOptional = accountRepository.findByAccountNumber(connection, accountNumberFrom);
        if (accountFromOptional.isEmpty()) {
            throw new AccountNotFoundException(accountNumberFrom);
        }

        Optional<Account> accountToOptional = accountRepository.findByAccountNumber(connection, accountNumberTo);
        if (accountToOptional.isEmpty()) {
            throw new AccountNotFoundException(accountNumberTo);
        }

        Account accountFrom = accountFromOptional.get();

        if (!accountFrom.isWithdraw(amount)) {
            throw new BalanceNotEnoughException(accountNumberFrom);
        }

        int result1 = accountRepository.withdraw(connection, accountNumberFrom, amount);

        if (result1 < 1) {
            throw new RuntimeException("fail - withdraw :" + accountNumberFrom);
        }

        int result2 = accountRepository.deposit(connection, accountNumberTo, amount);

        if (result2 < 1) {
            throw new RuntimeException("fail - deposit : " + accountNumberTo);
        }

    }

    @Override
    public boolean isExistAccount(Connection connection, long accountNumber) {
        // todo#16 Account가 존재하면 true , 존재하지 않다면 false
        return accountRepository.countByAccountNumber(connection, accountNumber) == 1;
    }

    @Override
    public void dropAccount(Connection connection, long accountNumber) {
        // todo#17 account 삭제
        if (!isExistAccount(connection, accountNumber)) {
            throw new AccountNotFoundException(accountNumber);
        }

        int result = accountRepository.deleteByAccountNumber(connection, accountNumber);
        if (result < 1) {
            throw new RuntimeException("deleteByAccountNumber is fail");
        }
    }

}