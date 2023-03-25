package ru.tinkoff.edu.java.scrapper.dto.response;

public class LinkResponse {
    private int id;
    private String url;

    public LinkResponse() {
    }

    public LinkResponse(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
