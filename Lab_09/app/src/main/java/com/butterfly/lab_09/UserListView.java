package com.butterfly.lab_09;

public class UserListView {
    public String item;
    public String subitem;

    public UserListView(String name) {
        this.item = name;
    }

    public UserListView(String item, String subitem) {
        this.item = item;
        this.subitem = subitem;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSubitem() {
        return subitem;
    }

    public void setSubitem(String subitem) {
        this.subitem = subitem;
    }
}
