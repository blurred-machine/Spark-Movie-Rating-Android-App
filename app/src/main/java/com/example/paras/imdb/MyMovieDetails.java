package com.example.paras.imdb;

// a pojo class that holds the data of all the movies.
public class MyMovieDetails {

    private String id;
    private String imageUrl;
    private String name;
    private String releaseDate;
    private Float popularity;
    private String votesAverage;
    private String votesCount;

    // constructor of the movie details class which initializes the variables.
    public MyMovieDetails(String id, String imageUrl, String name, String releaseDate, Float popularity,
                   String votesAverage, String votesCount) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
        this.votesAverage = votesAverage;
        this.votesCount = votesCount;

    }

    // creating the getters of all the variables of the class.
    public String getId() { return id; }

    public String getImageUrl() { return imageUrl; }

    public String getName() { return name; }

    public String getReleaseDate() { return releaseDate; }

    public Float getPopularity() { return popularity; }

    public String getVotesAverage() { return votesAverage; }

    public String getVotesCount() { return votesCount; }

}
