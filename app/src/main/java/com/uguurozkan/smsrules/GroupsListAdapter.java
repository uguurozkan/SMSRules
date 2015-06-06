/*
 * Copyright (c) 2015.
 * This code belongs to Uğur Özkan
 * ugur.ozkan@ozu.edu.tr
 */

package com.uguurozkan.smsrules;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupsListAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;

    public GroupsListAdapter(Context context, Cursor cursor) {
        super();
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_each_group, null);

        cursor.moveToPosition(position);

        String groupName = cursor.getString(cursor.getColumnIndex(GroupsDBHelper.SMS_RULES_COLUMN_GROUP));

        TextView textViewGroupName = (TextView) convertView.findViewById(R.id.textViewGroupName);

        textViewGroupName.setText(groupName);

        if (cursor.isLast()) {
            cursor.close();
        }

        return convertView;
    }

}
