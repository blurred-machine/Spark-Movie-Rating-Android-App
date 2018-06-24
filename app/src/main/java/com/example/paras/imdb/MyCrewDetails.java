package com.example.paras.imdb;

public class MyCrewDetails {
    private String crewImage;
    private String crewName;
    private String crewJob;

    public String getCrewName() {
        return crewName;
    }

    public String getCrewJob() {
        return crewJob;
    }

    public String getCrewImage() {
        return crewImage;
    }

    public MyCrewDetails(String crewImage, String crewName, String crewJob) {
        this.crewName = crewName;
        this.crewImage = crewImage;
        this.crewJob = crewJob;
    }
}
