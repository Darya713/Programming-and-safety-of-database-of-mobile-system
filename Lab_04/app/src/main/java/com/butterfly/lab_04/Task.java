package com.butterfly.lab_04;

public class Task {
    private String taskName;
    private int date;
    private int month;
    private int year;

    public Task() {}

    public Task(String taskName, int date, int month, int year) {
        this.taskName = taskName;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isForToday(Task t) {
        return t.date == this.date && t.month == this.month && t.year == this.year;
    }
}
