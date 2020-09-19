package com.example.swapnilaudichya.tasklist;

public class Task {
    private Integer id;
    private String title;
    private String date;
    private String desc;

    public Task() {
    }

    public Task(String title, String date, String desc) {
        this.id = -1;
        this.title = title;
        this.date = date;
        this.desc = desc;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

