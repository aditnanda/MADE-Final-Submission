package com.nand_project.moviecatalogue.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultMovieModel implements Parcelable {
    private String vote_count;
    private String id;
    private String vote_average;
    private String title;
    private String popularity;
    private String poster_path;
    private String original_title;
    private String backdrop_path;
    private String overview;
    private String release_date;

    public ResultMovieModel() {
    }


    protected ResultMovieModel(Parcel in) {
        vote_count = in.readString();
        id = in.readString();
        vote_average = in.readString();
        title = in.readString();
        popularity = in.readString();
        poster_path = in.readString();
        original_title = in.readString();
        backdrop_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }

    public static final Creator<ResultMovieModel> CREATOR = new Creator<ResultMovieModel>() {
        @Override
        public ResultMovieModel createFromParcel(Parcel in) {
            return new ResultMovieModel(in);
        }

        @Override
        public ResultMovieModel[] newArray(int size) {
            return new ResultMovieModel[size];
        }
    };

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(vote_count);
        dest.writeString(id);
        dest.writeString(vote_average);
        dest.writeString(title);
        dest.writeString(popularity);
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeString(release_date);
    }
}
