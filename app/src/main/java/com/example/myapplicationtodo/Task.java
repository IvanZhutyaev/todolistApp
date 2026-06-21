package com.example.myapplicationtodo;

public class Task {
    String text;
    Boolean done;

    public Task(String text, Boolean done) {
        this.text = text;
        this.done = done;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
