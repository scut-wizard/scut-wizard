package com.scut.scutwizard.ScoreHelper;

import java.util.Date;

public class Score {
    private String description;
    private Category category;
    private double value;
    private Date createDate, lastModifiedDate, eventDate;
    private String specificCategory;
    // TODO: 存证明材料图片

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
