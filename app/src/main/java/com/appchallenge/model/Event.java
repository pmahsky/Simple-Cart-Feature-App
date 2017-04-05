package com.appchallenge.model;

/**
 * Created by Prashant on 04/04/17.
 */

public class Event {

    private String title;
    private String thumbnailUrl;
    private String endDate;
    private int rowId;
    private String eventCount;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getRowId() {

        return rowId;
    }

    public String getEventCount() {

        return eventCount;

    }

    public void setEventCount(String eventCount) {

        this.eventCount = eventCount;
    }
}
