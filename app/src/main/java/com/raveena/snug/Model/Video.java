package com.raveena.snug.Model;

public class Video {
    private String category;
    private String filePath;
    private String videoId;
    private String videoUri;

    public Video(String category, String filePath, String videoId, String videoUri) {
        this.category = category;
        this.filePath = filePath;
        this.videoId = videoId;
        this.videoUri = videoUri;
    }

    public Video() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }
}
