package com.example.paras.imdb;

// a pojo class that holds the data of the specific movie details.
public class MyMovieDetailSpecificOne {

    private String id;
    private String imageUrl;
    private String title;
    private Float popularity;
    private String description;
    private String releaseDate;
    private String budget;
    private String revenue;
    private String status;
    private String votesAverage;
    private String votesCount;
    private String descriptionBig;

    // constructor of the movie details class which initializes the variables.
    public MyMovieDetailSpecificOne(String id, String imageUrl, String title, Float popularity,
                                    String description, String releaseDate, String budget, String revenue,
                                    String status, String votesAverage, String votesCount, String descriptionBig) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.popularity = popularity;
        this.description = description;
        this.releaseDate = releaseDate;
        this.budget = budget;
        this.revenue = revenue;
        this.status = status;
        this.votesAverage = votesAverage;
        this.votesCount = votesCount;
        this.descriptionBig = descriptionBig;
    }

    // creating the getters of all the variables of the class.
    public String getId() { return id; }

    public String getImageUrl() { return imageUrl; }

    public String getTitle() { return title; }

    public Float getPopularity() { return popularity; }

    public String getDescription() { return description; }

    public String getReleaseDate() { return releaseDate; }

    public String getBudget() { return budget; }

    public String getRevenue() { return revenue; }

    public String getStatus() { return status; }

    public String getVotesAverage() { return votesAverage; }

    public String getVotesCount() { return votesCount; }

    public String getDescriptionBig() { return descriptionBig; }
}
