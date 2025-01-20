package com.nhnacademy.jdbc.club.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.nhnacademy.jdbc.club.domain.ClubStudent;
import com.nhnacademy.jdbc.club.repository.ClubRegistrationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClubRegistrationRepositoryImpl implements ClubRegistrationRepository {

    @Override
    public int save(Connection connection, String studentId, String clubId) {
        // todo#11 - 학생 -> 클럽 등록, executeUpdate() 결과를 반환
        try (PreparedStatement statement = connection
                .prepareStatement("insert into jdbc_club_registrations set student_id=?, club_id=?");) {
            statement.setString(1, studentId);
            statement.setString(2, clubId);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByStudentIdAndClubId(Connection connection, String studentId, String clubId) {
        // todo#12 - 학생 -> 클럽 탈퇴, executeUpdate() 결과를 반환
        try (PreparedStatement statement = connection
                .prepareStatement("delete from jdbc_club_registrations where student_id=?, and club_id=?");) {
            statement.setString(1, studentId);
            statement.setString(2, clubId);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClubStudent> findClubStudentsByStudentId(Connection connection, String studentId) {
        // todo#13 - 학생 -> 클럽 등록, executeUpdate() 결과를 반환
        try (PreparedStatement statement = connection
                .prepareStatement(
                        "select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a inner join jdbc_club_registrations b on a.id=b.student_id inner join jdbc_club c on b.club_id=c.club_id where a.id=?")) {
            statement.setString(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<ClubStudent> clubStudentList = new ArrayList<>();

                while (resultSet.next()) {
                    clubStudentList.add(new ClubStudent(resultSet.getString("student_id"),
                            resultSet.getString("student_name"),
                            resultSet.getString("club_id"),
                            resultSet.getString("club_name")));
                }

                return clubStudentList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClubStudent> findClubStudents(Connection connection) {
        // todo#21 - join
        String sql = "select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id order by c.club_id asc,a.id asc";
        try {
            return getClubStudentList(connection, sql);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_left_join(Connection connection) {
        // todo#22 - left join
        String sql = "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id order by a.id asc, b.club_id asc";
        try {
            return getClubStudentList(connection, sql);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_right_join(Connection connection) {
        // todo#23 - right join
        String sql = "select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id order by c.club_id asc,a.id asc";
        try {
            return getClubStudentList(connection, sql);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_full_join(Connection connection) {
        // todo#24 - full join = left join union right join
        StringBuilder sb = new StringBuilder();
        // left join
        sb.append(
                "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id");
        sb.append(System.lineSeparator());
        sb.append("union");
        sb.append(System.lineSeparator());
        // right join
        sb.append(
                "select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id");

        try {
            return getClubStudentList(connection, sb.toString());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_left_excluding_join(Connection connection) {
        // todo#25 - left excluding join
        String sql = "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id where c.club_id is null order by a.id asc";
        try {
            return getClubStudentList(connection, sql);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_right_excluding_join(Connection connection) {
        // todo#26 - right excluding join
        String sql = "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id where a.id is null order by b.club_id asc ";
        try {
            return getClubStudentList(connection, sql);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_outher_excluding_join(Connection connection) {
        // todo#27 - outher_excluding_join = left excluding join union right excluding
        // join
        StringBuilder sb = new StringBuilder();
        // left join
        sb.append(
                "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id where c.club_id is null");
        sb.append(System.lineSeparator());
        sb.append("union");
        sb.append(System.lineSeparator());
        // right join
        sb.append(
                "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id where a.id is null");

        try {
            return getClubStudentList(connection, sb.toString());
        } catch (Exception e) {
            throw e;
        }
    }

    private List<ClubStudent> getClubStudentList(Connection connection, String sql) {
        ResultSet rs = null;

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            rs = psmt.executeQuery();

            List<ClubStudent> clubStudentList = new ArrayList<>();
            while (rs.next()) {
                clubStudentList.add(
                        new ClubStudent(
                                rs.getString("student_id"),
                                rs.getString("student_name"),
                                rs.getString("club_id"),
                                rs.getString("club_name")));
            }
            return clubStudentList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (Objects.nonNull(rs)) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}