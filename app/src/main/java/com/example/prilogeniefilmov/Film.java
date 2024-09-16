package com.example.prilogeniefilmov;

import java.io.Serializable;

public class Film implements Serializable {
    public String title, year, genre, director, producer, rating;

    public Film() {
    }

    public Film(String title, String year, String genre, String director, String producer,
                String rating) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.director = director;
        this.producer = producer;
        this.rating = rating;
    }


    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getProducer() {
        return producer;
    }

    public String getRating() {
        return rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return title + " " + year;
    }

}