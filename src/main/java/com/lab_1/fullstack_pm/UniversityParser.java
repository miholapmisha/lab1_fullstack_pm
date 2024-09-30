package com.lab_1.fullstack_pm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UniversityParser {

    public List<University> parseUniversityResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type universityListType = new TypeToken<List<University>>(){}.getType();

        return gson.fromJson(jsonResponse, universityListType);
    }
}