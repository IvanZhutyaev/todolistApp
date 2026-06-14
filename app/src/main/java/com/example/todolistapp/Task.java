package com.example.todolistapp;

public class Task {
    public Task(String text, Boolean done) {
        this.text = text;
        this.done = done;
    }

    public String getText() {
        return text;
    }

    public Boolean getDone() {
        return done;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    String text;
    Boolean done;

}
