package com.lab_1.fullstack_pm;

import java.util.List;

public class University {
    private List<String> domains;
    private String alpha_two_code;
    private List<String> web_pages;
    private String name;
    private String state_province;
    private String country;

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public String getAlpha_two_code() {
        return alpha_two_code;
    }

    public void setAlpha_two_code(String alpha_two_code) {
        this.alpha_two_code = alpha_two_code;
    }

    public List<String> getWeb_pages() {
        return web_pages;
    }

    public void setWeb_pages(List<String> web_pages) {
        this.web_pages = web_pages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState_province() {
        return state_province;
    }

    public void setState_province(String state_province) {
        this.state_province = state_province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "University: " + "\n" +
                "domains=" + domains + "\n" +
                "alpha_two_code='" + alpha_two_code + '\'' + "\n" +
                "web_pages=" + web_pages + "\n" +
                "name='" + name + '\'' + "\n" +
                "state_province='" + state_province + '\'' + "\n" +
                "country='" + country + '\'' + "\n";
    }
}

