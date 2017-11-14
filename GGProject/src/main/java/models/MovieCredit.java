package models;

//# 	array of Actor:
//        #		name: string
//        #		biography: string
//        #		birthday: string
//        #		deathday: string or null
//        #		gender: string
//        #		imdb_profile: string
//        #		images: array of string
//        #		movie_credits: array of MovieCredit
//        #			title: string
//        #			genres: array of string
//        #			vote_average: number
//        #			poster: string

import java.util.ArrayList;

public class MovieCredit {
    private String title;
    private ArrayList<String> genres;
    private float vote_average;
    private String poster;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
