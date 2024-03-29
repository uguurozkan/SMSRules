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

public class SmsDetailsListAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;

    public SmsDetailsListAdapter(Context context, Cursor cursor) {
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
        convertView = inflater.inflate(R.layout.listview_each_sms, null);

        cursor.moveToPosition(position);

        String sender = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_ADDRESS));
        String body = cursor.getString(cursor.getColumnIndex(SmsDetailsDBHelper.SMS_DETAILS_COLUMN_BODY));

        TextView textViewContactNumber = (TextView) convertView.findViewById(R.id.textViewSMSSender);
        TextView textViewSMSBody = (TextView) convertView.findViewById(R.id.textViewMessageBody);

        textViewContactNumber.setText(sender);
        textViewSMSBody.setText(body);

        if (cursor.isLast()) {
            cursor.close();
        }

        return convertView;
    }
}
