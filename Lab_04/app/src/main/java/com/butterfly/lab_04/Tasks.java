package com.butterfly.lab_04;

import java.util.ArrayList;

public class Tasks {
    private static ArrayList<Task> listOfTasks;

    static {
        listOfTasks = new ArrayList<>();
    }

    public Tasks () {}

    public Tasks(ArrayList<Task> listOfTasks) {
        this.listOfTasks = listOfTasks;
    }

    public static ArrayList<Task> getListOfTasks() {
        return listOfTasks;
    }

    public void setListOfTasks(ArrayList<Task> listOfTasks) {
        this.listOfTasks = listOfTasks;
    }

    static boolean isTaskForToday(int date, int month, int year) {
        for (Task tsk: listOfTasks) {
            if (tsk.getDate() == date && tsk.getMonth() == month && tsk.getYear() == year) {
                return true;
            }
        }
        return false;
    }

    static String getTaskForTodayString(int date, int month, int year) {
        for (Task t: listOfTasks) {
            if (t.getDate() == date && t.getMonth() == month && t.getYear() == year) {
                return t.getTaskName();
            }
        }
        return String.valueOf(-1);
    }

    static Task getTaskForToday(int date, int month, int year) {
        for (Task tsk: listOfTasks) {
            if (tsk.getDate() == date && tsk.getMonth() == month && tsk.getYear() == year) {
                return tsk;
            }
        }
        return null;
    }

    static void add(Task task) {
        listOfTasks.add(task);
    }

    static void remove(Task task) {
        listOfTasks.remove(task);
    }
}
