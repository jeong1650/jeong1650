package com.example.kaon.ims;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kaon.ims.Listitem;
import com.example.kaon.ims.R;

import java.util.List;

public class NoticeAdapter extends BaseAdapter  {

    Context applicationContext;
    int list_gonggo;
    List<Listitem> noticeList;
    private LayoutInflater mInflater;

    public NoticeAdapter(Context applicationContext, int list_gonggo, List<Listitem> noticeList) {
        this.applicationContext = applicationContext;
        this.list_gonggo = list_gonggo;
        this.noticeList = noticeList;
    }

    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            mInflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = mInflater.inflate(R.layout.list_gonggo,parent,false);
        }



        TextView name = (TextView) v.findViewById(R.id.gongo);
        name.setText(noticeList.get(position).getNAME());

        TextView s_date = (TextView) v.findViewById(R.id.s_date);

        s_date.setText(noticeList.get(position).getSTART_DATE());

        TextView e_date = (TextView) v.findViewById(R.id.f_date);

        e_date.setText(noticeList.get(position).getEND_DATE());

//        TextView type = (TextView) v.findViewById(R.id.type);
//        type.setText(noticeList.get(position).getTYPE());



        return  v;


    }


}
