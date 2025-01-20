package com.nhnacademy.jdbc.club.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.repository.ClubRepository;

public class ClubRepositoryImpl implements ClubRepository {

    @Override
    public Optional<Club> findByClubId(Connection connection, String clubId) {
        // todo#3 club 조회
        try (PreparedStatement statement = connection.prepareStatement("select * from jdbc_club where club_id=?");) {
            statement.setString(1, clubId);
            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    Club club = new Club(resultSet.getString("club_id"), resultSet.getString("club_name"));

                    return Optional.of(club);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Club club) {
        // todo#4 club 생성, executeUpdate() 결과를 반환
        try (PreparedStatement statement = connection
                .prepareStatement("insert into jdbc_club(club_id, club_name) values(?, ?)");) {
            statement.setString(1, club.getClubId());
            statement.setString(2, club.getClubName());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Connection connection, Club club) {
        // todo#5 club 수정, clubName을 수정합니다. executeUpdate()결과를 반환
        try (PreparedStatement statement = connection
                .prepareStatement("update jdbc_club set club_id=?, club_name=? where club_id=?");) {
            statement.setString(1, club.getClubId());
            statement.setString(2, club.getClubName());
            statement.setString(3, club.getClubId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByClubId(Connection connection, String clubId) {
        // todo#6 club 삭제, executeUpdate()결과 반환
        try (PreparedStatement statement = connection
                .prepareStatement("delete from jdbc_club where club_id=?");) {
            statement.setString(1, clubId);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByClubId(Connection connection, String clubId) {
        int count = 0;
        // todo#7 clubId에 해당하는 club의 count를 반환
        try (PreparedStatement statement = connection
                .prepareStatement("select count(*) from jdbc_club where club_id=?");) {
            statement.setString(1, clubId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }
}
