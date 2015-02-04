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
    public int getChildCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getChildItem(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        return null;
    }
}
