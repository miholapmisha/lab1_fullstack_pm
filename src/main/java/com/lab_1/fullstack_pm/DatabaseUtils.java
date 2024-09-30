package com.lab_1.fullstack_pm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseUtils {

    private static final String url = "jdbc:postgresql://localhost:5432/universities_data";
    private static final String user = "postgres";
    private static final String password = "1234";

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS universities (
                id SERIAL PRIMARY KEY,
                alpha_two_code TEXT,
                state_province TEXT,
                name TEXT,
                country TEXT,
                domain TEXT
            );""";

    private static final String FIND_UNIVERSITIES_SQL = """
            SELECT * FROM universities
            WHERE name ILIKE ? AND country ILIKE ?;
            """;

    private static final String SAVE_UNIVERSITY_SQL = """
            INSERT INTO universities (alpha_two_code, state_province, name, country, domain, web_pages)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

    private static final String FIND_UNIVERSITY_BY_NAME_SQL = """
            SELECT COUNT(*) FROM universities WHERE name = ?;
            """;


    public static void tryCreateTable() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<University> findUniversitiesByNameAndCountry(String name, String country) {
        List<University> universities = new ArrayList<>();


        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(FIND_UNIVERSITIES_SQL)) {

            stmt.setString(1, "%" + name + "%");
            stmt.setString(2, "%" + country + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                University university = new University();
                university.setCountry(rs.getString("country"));
                university.setName(rs.getString("name"));
                university.setAlpha_two_code(rs.getString("alpha_two_code"));
                university.setDomains(getParsedListFromArray(rs.getString("domain")));
                university.setWeb_pages(getParsedListFromArray(rs.getString("web_pages")));
                university.setState_province(rs.getString("state_province"));

                universities.add(university);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return universities;
    }

    public static void saveUniversities(List<University> universities) {
        Gson gson = new GsonBuilder().create();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            for (University university : universities) {
                if (!isUniversityExists(conn, university.getName())) {
                    try (PreparedStatement stmt = conn.prepareStatement(SAVE_UNIVERSITY_SQL)) {
                        stmt.setString(1, university.getAlpha_two_code());
                        stmt.setString(2, university.getState_province());
                        stmt.setString(3, university.getName());
                        stmt.setString(4, university.getCountry());
                        stmt.setString(5, gson.toJson(university.getDomains()));
                        stmt.setString(6, gson.toJson(university.getWeb_pages()));

                        stmt.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException("Error saving university: " + university.getName(), e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isUniversityExists(Connection conn, String name) {
        try (PreparedStatement stmt = conn.prepareStatement(FIND_UNIVERSITY_BY_NAME_SQL)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private static List<String> getParsedListFromArray(String stringToParse) {
        String trimmed = stringToParse.replace("[", "").replace("]", "");
        String withoutQuotes = trimmed.replace("\"", "");
        return Arrays.stream(withoutQuotes.split(",\\s*")).toList();
    }

}