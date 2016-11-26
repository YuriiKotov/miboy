package com.boco.miboy.model;

import java.util.Map;

public class Questionnaire {
    private String token;
    private Map<Integer, Integer> answers;

    public Questionnaire(String token, Map<Integer, Integer> answers) {
        this.token = token;
        this.answers = answers;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<Integer, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, Integer> answers) {
        this.answers = answers;
    }
}