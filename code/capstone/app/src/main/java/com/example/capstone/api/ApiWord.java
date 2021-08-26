package com.example.capstone.api;

public class ApiWord {
    private String title;
    private String contents;
    private String location;

    public String getTitle() {
        return title;
    }
    public String getContents() {
        return contents;
    }
    public String getLocation() {
        return location;
    }


    /*
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    */

    public ApiWord(String t, String c, String l) {
        title = t;
        contents = c;
        location = l;
    }
}
