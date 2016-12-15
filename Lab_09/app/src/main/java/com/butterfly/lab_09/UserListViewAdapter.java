package com.butterfly.lab_09;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserListViewAdapter extends ArrayAdapter<UserListView> {
    private ArrayList<UserListView> userList;

    public UserListViewAdapter(Context context, int resource, ArrayList<UserListView> userList) {
        super(context, resource, userList);
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.items, parent, false);
        }
        UserListView userListView = userList.get(position);
        if (userListView != null) {
            TextView item = (TextView) v.findViewById(R.id.item);
            TextView subitem = (TextView) v.findViewById(R.id.subitem);
            if (item != null) {
                item.setText(userListView.item);
            }
            if (subitem != null) {
                subitem.setText(userListView.subitem);
            }
        }
        return v;
    }
}
