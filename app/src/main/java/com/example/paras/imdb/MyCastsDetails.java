package com.example.paras.imdb;

// a pojo class to hold the data of the cast.
public class MyCastsDetails {
    private String characterName;
    private String realName;
    private String castUrl;

    // getters to access the variables.
    public String getCharacterName() {
        return characterName;
    }

    public String getRealName() {
        return realName;
    }

    public String getCastUrl() {
        return castUrl;
    }

    // constructor of the pojo class.
    public MyCastsDetails(String characterName, String realName, String castUrl) {

        this.characterName = characterName;
        this.realName = realName;
        this.castUrl = castUrl;
    }
}
