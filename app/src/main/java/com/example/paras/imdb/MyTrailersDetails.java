package com.example.paras.imdb;

// a pojo class that holds the details of the trailers of the specific movie.
public class MyTrailersDetails {
    private String trailerUrl;
    private String trailerSize;
    private String trailerType;

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public String getTrailerSize() {
        return trailerSize;
    }

    public String getTrailerType() {
        return trailerType;
    }

    public MyTrailersDetails(String trailerUrl, String trailerSize, String trailerType) {
        this.trailerUrl = trailerUrl;
        this.trailerSize = trailerSize;
        this.trailerType = trailerType;

    }
}
