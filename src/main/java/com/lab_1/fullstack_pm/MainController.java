package com.lab_1.fullstack_pm;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    private static final String API_URL = "http://universities.hipolabs.com/search?name=";

    @FXML
    private TextField universityName;

    @FXML
    private TextField countryName;

    @FXML
    private Label result;

    @FXML
    public void getAPIData() {
        String name = universityName.getText();
        String country = countryName.getText();

        try {
            StringBuilder urlBuilder = new StringBuilder(API_URL);
            urlBuilder.append(name);

            if (country != null && !country.isEmpty()) {
                urlBuilder.append("&country=").append(country);
            }

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                List<University> universityList = getUniversities(connection);
                result.setText(getFormattedData(universityList));
                DatabaseUtils.saveUniversities(universityList);

            } else {
                String formattedData = getFormattedData(DatabaseUtils.findUniversitiesByNameAndCountry(name, country));
                result.setText("GET request failed. Response Code: " + responseCode + "\n Getting from database: " + formattedData);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void getDataFromDatabase() {
        String name = universityName.getText();
        String country = countryName.getText();
        result.setText(getFormattedData(DatabaseUtils.findUniversitiesByNameAndCountry(name, country)));
    }

    private String getFormattedData(List<University> universities) {
        return universities.stream()
                .map((University::toString))
                .collect(Collectors.joining("\n"));
    }

    private List<University> getUniversities(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        UniversityParser parser = new UniversityParser();
        return parser.parseUniversityResponse(response.toString());
    }

    @FXML
    private void initialize() {
        DatabaseUtils.tryCreateTable();
    }

}