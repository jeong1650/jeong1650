package com.example.kaon.ims;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableListviewAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<String> mParentList;
    private ArrayList<ChildListData> mChildList;
    private HashMap<String, ArrayList<ChildListData>> mChildHashMap;
    private ChildListViewHolder mChildListViewHolder;



    public ExpandableListviewAdapter(Context context, ArrayList<String>parentList,HashMap<String,ArrayList<ChildListData>>ChildHashMap){
        this.mContext = context;
        this.mParentList = parentList;
        this.mChildHashMap = ChildHashMap;
    }


    @Override
    public int getGroupCount() {
        return mParentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mChildHashMap.get(this.mParentList.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mParentList.get(groupPosition);
    }

    @Override
    public ChildListData getChild(int groupPosition, int childPosition) {
        return this.mChildHashMap.get(this.mParentList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater groupinfla = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = groupinfla.inflate(R.layout.group_items,parent,false);
        }
        TextView parentText = (TextView)convertView.findViewById(R.id.heading);
        parentText.setText(getGroup(groupPosition));




//        RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.expand_radio);
//        radioButton.setId(R.id.eva_2);
////
//        RadioButton radioButton3 = (RadioButton) convertView.findViewById(R.id.expand_radio);
//        radioButton3.setId(R.id.eva_3);
//
//        RadioButton radioButton4 = (RadioButton) convertView.findViewById(R.id.expand_radio);
//        radioButton4.setId(R.id.eva_4);
//
//        RadioButton radioButton5 = (RadioButton) convertView.findViewById(R.id.expand_radio);
//        radioButton5.setId(R.id.eva_5);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildListData childData = (ChildListData)getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater childInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInfla.inflate(R.layout.child_items,null);

            mChildListViewHolder = new ChildListViewHolder();
            mChildListViewHolder.childText = (TextView)convertView.findViewById(R.id.childItem);
            convertView.setTag(mChildListViewHolder);
        } else{
            mChildListViewHolder= (ChildListViewHolder)convertView.getTag();
        }

        mChildListViewHolder.childText.setText(getChild(groupPosition,childPosition).Contents);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

     class ChildListViewHolder {
        public TextView childText;
    }
}
