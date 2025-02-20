package com.phyxels.phyxels.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class SaveState {
    @JsonProperty("_id")
    @SerializedName("_id")
    private String id;

    @JsonProperty("_rev")
    @SerializedName("_rev")
    private String rev;

    private String save;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private String date;

    // Getters
    public String getId() {
        return id;
    }

    public String getRev() {
        return rev;
    }

    public String getSave() {
        return save;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }
}