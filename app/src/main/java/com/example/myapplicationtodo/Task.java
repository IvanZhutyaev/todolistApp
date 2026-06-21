package com.example.myapplicationtodo;

public class Task {
    String text;
    Boolean done;

    public void setHistoryReason(String historyReason) {
        this.historyReason = historyReason;
    }

    String historyReason;

    public Task(String text, Boolean done) {
        this.text = text;
        this.done = done;
        this.historyReason=null;
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

    public String getHistoryReason(){
        return historyReason;
    }
}
