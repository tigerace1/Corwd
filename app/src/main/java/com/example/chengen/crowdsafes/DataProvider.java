package com.example.chengen.crowdsafes;

import android.graphics.Bitmap;

public class DataProvider {
    private String titleData;
    private String situationData;
    private Bitmap imagesData1;
    private Bitmap imagesData2;
    private Bitmap imagesData3;
    private String videoData;
    private String locationData;
    private String locationDesData;
    private String responseTo;
    private String contact;
    private String reportType;
    private String missingType;

    public DataProvider(String titleData, String situationData, Bitmap imagesData1, Bitmap imagesData2, Bitmap imagesData3,
                        String videoData, String locationData, String locationDesData, String responseTo,
                        String contact, String reportType, String missingType) {
        this.titleData = titleData;
        this.situationData = situationData;
        this.imagesData1 = imagesData1;
        this.imagesData2 = imagesData2;
        this.imagesData3 = imagesData3;
        this.videoData = videoData;
        this.locationData = locationData;
        this.locationDesData = locationDesData;
        this.responseTo = responseTo;
        this.contact = contact;
        this.reportType = reportType;
        this.missingType = missingType;

    }

    public Bitmap getImagesData2() {
        return imagesData2;
    }

    public void setImagesData2(Bitmap imagesData2) {
        this.imagesData2 = imagesData2;
    }

    public Bitmap getImagesData3() {
        return imagesData3;
    }

    public void setImagesData3(Bitmap imagesData3) {
        this.imagesData3 = imagesData3;
    }

    public String getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(String responseTo) {
        this.responseTo = responseTo;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getMissingType() {
        return missingType;
    }

    public void setMissingType(String missingType) {
        this.missingType = missingType;
    }

    public String getTitleData() {
        return titleData;
    }

    public void setTitleData(String titleData) {
        this.titleData = titleData;
    }

    public String getSituationData() {
        return situationData;
    }

    public void setSituationData(String situationData) {
        this.situationData = situationData;
    }

    public Bitmap getImagesData1() {
        return imagesData1;
    }

    public void setImagesData(Bitmap imagesData) {
        this.imagesData1 = imagesData;
    }

    public String getLocationData() {
        return locationData;
    }

    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }

    public String getLocationDesData() {
        return locationDesData;
    }

    public void setLocationDesData(String locationDesData) {
        this.locationDesData = locationDesData;
    }
    public String getVideoData() {
        return videoData;
    }

    public void setVideoData(String videoData) {
        this.videoData = videoData;
    }
}
