package com.scut.scutwizard.ScoreHelper;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import androidx.annotation.NonNull;

public class Score implements Serializable {
    private int      id;
    private String   description; // des text
    private Category category; // category integer
    private double   value; // value real
    private Date     createDate, lastModifiedDate, eventDate; // ~Date integer
    private String specificCategory; // detail text
    private String comment; // ps text
    private int    subtable; // subtable integer
    private String imagePaths; // images text "fn1;fn2;..."

    public Score() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

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

    public String getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(String imagePaths) {
        this.imagePaths = imagePaths;
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

    public void setCategory(int category) {
        this.category = Category.values()[category];
    }

    public int getCategoryInt() {
        return category.ordinal();
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

    public void setLastModifiedDate(@NonNull Date lastModifiedDate) {
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

    public static class ScoreComparator implements Comparator<Score> {
        @Override
        public int compare(Score s1, Score s2) {
            final int dateCmp = s2.getEventDate().compareTo(s1.getEventDate());
            return dateCmp != 0 ? dateCmp : Double.compare(s2.getValue(), s1.getValue());
        }
    }
}
