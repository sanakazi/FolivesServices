package com.callndata.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.folivesservices.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    GroupViewHolder GVholder;
    ChildViewHolder CVHolder;

    private HashMap<Integer, boolean[]> mChildCheckStates;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        mChildCheckStates = new HashMap<Integer, boolean[]>();
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);

            CVHolder = new ChildViewHolder();
            CVHolder.chkListItem = (CheckBox) convertView.findViewById(R.id.chkListItem);

            convertView.setTag(R.layout.list_item, CVHolder);
        } else {
            CVHolder = (ChildViewHolder) convertView.getTag(R.layout.list_item);
        }
        CVHolder.chkListItem.setText(childText);
        CVHolder.chkListItem.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {

            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            CVHolder.chkListItem.setChecked(getChecked[mChildPosition]);

        } else {

            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];
            mChildCheckStates.put(mGroupPosition, getChecked);
            CVHolder.chkListItem.setChecked(false);
        }

        CVHolder.chkListItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);

                } else {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                }
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

            GVholder = new GroupViewHolder();

            GVholder.lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            GVholder.lblListHeader.setTypeface(null, Typeface.BOLD);
            GVholder.lblListHeader.setText(headerTitle);
            GVholder.lblListHeader.setTextColor(_context.getResources().getColor(R.color.white));

            convertView.setTag(GVholder);

        } else {
            GVholder = (GroupViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        TextView lblListHeader;
    }

    class ChildViewHolder {
        CheckBox chkListItem;
    }
}
