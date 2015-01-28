package org.kitdroid.sample;

import android.view.View;
import android.view.ViewGroup;

import org.kitdroid.library.GroupAdapter;

/**
 * Created by huiyh on 15/1/28.
 */
public class TestGroupAdapter extends GroupAdapter{

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getGroupChildCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroupChildItem(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupChildItemId(int groupPosition, int childPosition) {
        return super.getChildViewTypeCount();
    }

    @Override
    public int getGroupChildView(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        return 0;
    }

    @Override
    public int getChildViewTypeCount() {
        return super.getChildViewTypeCount();
    }

    @Override
    public int getChildViewType(int groupPosition, int childPosition) {
        return super.getChildViewType(groupPosition, childPosition);
    }

}
