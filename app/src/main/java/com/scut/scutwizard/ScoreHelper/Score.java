package com.scut.scutwizard.ScoreHelper;

import java.util.ArrayList;
import java.util.Date;

public class Score {
    private int id;
    private String description; // des text
    private Category category; // category integer
    private double value; // value real
    private Date createDate, lastModifiedDate, eventDate; // ~Date real
    private String specificCategory; // detail text
    private int subtable; // subtable integer
    private ArrayList<String> imagePaths; // images text

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubtable() {
        return subtable;
    }

    public void setSubtable(int subtable) {
        this.subtable = subtable;
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Score() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        if (this.createDate == null)
            this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        if (!lastModifiedDate.before(this.createDate))
            this.lastModifiedDate = lastModifiedDate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getSpecificCategory() {
        return specificCategory;
    }

    public void setSpecificCategory(String specificCategory) {
        this.specificCategory = specificCategory;
    }

    public enum Category {DEYU, ZHIYU, WENTI}
}
