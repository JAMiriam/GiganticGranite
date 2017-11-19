package models;

import java.util.ArrayList;

public class Actor {
    private String name;
    private String biography;
    private String birthday;
    private String deathday;
    private String gender;
    private String imdb_profile;
    private ArrayList<String> images;
    private ArrayList<MovieCredit> movie_credits;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImdb_profile() {
        return imdb_profile;
    }

    public void setImdb_profile(String imdb_profile) {
        this.imdb_profile = imdb_profile;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<MovieCredit> getMovie_credits() {
        return movie_credits;
    }

    public void setMovie_credits(ArrayList<MovieCredit> movie_credits) {
        this.movie_credits = movie_credits;
    }
}
