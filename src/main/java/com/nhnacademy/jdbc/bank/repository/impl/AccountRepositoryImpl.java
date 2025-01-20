package com.nhnacademy.jdbc.bank.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;

public class AccountRepositoryImpl implements AccountRepository {

    @Override
    public Optional<Account> findByAccountNumber(Connection connection, long accountNumber) {
        // todo#1 계좌-조회
        try (PreparedStatement statement = connection
                .prepareStatement("select * from jdbc_account where account_number=?")) {
            statement.setLong(1, accountNumber);

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    Account account = new Account(resultSet.getLong("account_number"), resultSet.getString("name"),
                            resultSet.getLong("balance"));

                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Account account) {
        // todo#2 계좌-등록, executeUpdate() 결과를 반환 합니다.
        try (PreparedStatement statement = connection
                .prepareStatement("insert into jdbc_account(account_number, name, balance) values(?, ?, ?)")) {
            statement.setLong(1, account.getAccountNumber());
            statement.setString(2, account.getName());
            statement.setLong(3, account.getBalance());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByAccountNumber(Connection connection, long accountNumber) {
        int count = 0;
        // todo#3 select count(*)를 이용해서 계좌의 개수를 count해서 반환
        try (PreparedStatement statement = connection
                .prepareStatement("select count(*) from jdbc_account where account_number=?")) {
            statement.setLong(1, accountNumber);

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

    @Override
    public int deposit(Connection connection, long accountNumber, long amount) {
        // todo#4 입금, executeUpdate() 결과를 반환 합니다.
        try (PreparedStatement statement = connection
                .prepareStatement("update jdbc_account set balance=balance+? where account_number=?")) {
            statement.setLong(1, amount);
            statement.setLong(2, accountNumber);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int withdraw(Connection connection, long accountNumber, long amount) {
        // todo#5 출금, executeUpdate() 결과를 반환 합니다.
        try (PreparedStatement statement = connection
                .prepareStatement("update jdbc_account set balance=balance-? where account_number=?")) {
            statement.setLong(1, amount);
            statement.setLong(2, accountNumber);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByAccountNumber(Connection connection, long accountNumber) {
        // todo#6 계좌 삭제, executeUpdate() 결과를 반환 합니다.
        try (PreparedStatement statement = connection
                .prepareStatement("delete from jdbc_account where account_number=?");) {
            statement.setLong(1, accountNumber);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
