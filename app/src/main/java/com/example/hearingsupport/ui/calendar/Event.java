package com.example.hearingsupport.ui.calendar;

import java.time.LocalDate;

public class Event {
    private String id;
    private LocalDate date;
    private String title;
    private String info;

    public Event(String id, LocalDate date, String title, String info) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.info = info;
    }

    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getTitle() { return title; }
    public String getInfo() { return info; }

    public void setDate(LocalDate date) { this.date = date; }
    public void setTitle(String title) { this.title = title; }
    public void setInfo(String info) { this.info = info; }
}
