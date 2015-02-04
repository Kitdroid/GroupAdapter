package org.kitdroid.library;/**
 * Created by huiyh on 15/1/28.
 */

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huiyh on 15/1/28.
 */
public abstract class GroupAdapter extends BaseAdapter {

    public static final int TYPE_COUNT_OF_HEADER_AND_FOOTER = 2;
    public static final int TYPE_OF_HEADER = 0;
    public static final int TYPE_OF_FOOTER = 1;

    private int mItemCount;
    private List<Integer> mGroupCountIndex = new ArrayList<Integer>();

    // TODO group的marginTop or marginBottom
    // 可以ViewTreeObserver.OnGlobalLayoutListener监听



    /*
     * 实现父类的方法
     */

    @Override
    public final int getCount() {
        i("getCount "+mItemCount);
        return mItemCount;
    }

    @Override
    public final Object getItem(int position) {
        GroupIndex itemIndex = getItemIndex(position);
        Object item = null;
        if(itemIndex.isHeader()){
            item = getHeaderItem(itemIndex.groupPosition);
        } else if(itemIndex.isFooter()){
            item = getFooterItem(itemIndex.groupPosition);
        } else {
            item = getChildItem(itemIndex.groupPosition, itemIndex.childPosition);
        }
        return item;
    }

    @Override
    public final long getItemId(int position) {
        GroupIndex itemIndex = getItemIndex(position);
        if(itemIndex.isHeader()){
            return getHeaderItemId(itemIndex.groupPosition);
        }
        if(itemIndex.isFooter()){
            return getFooterItemId(itemIndex.groupPosition);
        }
        return getChildItemId(itemIndex.groupPosition, itemIndex.childPosition);
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        i(position + " getItem:"+getItem(position));
        GroupIndex itemIndex = getItemIndex(position);
        if(itemIndex.isHeader()){
            return getHeaderView(itemIndex.groupPosition, convertView, parent);
        }
        if(itemIndex.isFooter()){
            return getFooterView(itemIndex.groupPosition, convertView, parent);
        }
        return getChildView(itemIndex.groupPosition, itemIndex.childPosition, convertView, parent);
    }

    @Override
    public final int getViewTypeCount() {
        return TYPE_COUNT_OF_HEADER_AND_FOOTER + getChildViewTypeCount();
    }

    @Override
    public final int getItemViewType(int position) {
        GroupIndex itemIndex = getItemIndex(position);
        int type = -1;
        if (itemIndex.isHeader()) {
            type = TYPE_OF_HEADER;
        } else if (itemIndex.isFooter()) {
            type = TYPE_OF_FOOTER;
        } else {
            type = TYPE_COUNT_OF_HEADER_AND_FOOTER + getChildViewType(itemIndex.groupPosition, itemIndex.childPosition);
        }
        i(position + " getItemViewType:"+ type);
        return type;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        initIndex();
    }

    /*
     * 为子类提示的方法
     */

    /*
     * for getCount()
     */
    public abstract int getGroupCount();

    public boolean hasHeader(int groupPosition) {
        return false;
    }

    public boolean hasFooter(int groupPosition) {
        return false;
    }

    public abstract int getChildCount(int groupPosition);


    /**
     * for getViewTypeCount()
     */
    public int getChildViewTypeCount() {
        return 1;
    }

    /**
     * for getItemViewType()
     */
    public int getChildViewType(int groupPosition, int childPosition) {
        return 0;
    }

    /*
     * for getItem(int position)
     */
    public Object getHeaderItem(int groupPosition) {
        return null;
    }

    public Object getFooterItem(int groupPosition) {
        return null;
    }

    public abstract Object getChildItem(int groupPosition, int childPosition);

    /*
     * for getItemId(int position)
     */
    private long getHeaderItemId(int groupPosition) {
        return 0;
    }

    private long getFooterItemId(int groupPosition) {
        return 0;
    }

    public long getChildItemId(int groupPosition, int childPosition) {
        return 0;
    }

    /*
     *for getView(int position, View convertView, ViewGroup parent)
     */
    public View getHeaderView(int groupPosition, View convertView, ViewGroup parent) {
        return null;
    }

    public View getFooterView(int groupPosition, View convertView, ViewGroup parent) {
        return null;
    }

    public abstract View getChildView(int groupPosition, int childPosition, View convertView, ViewGroup parent);

    @Override
    public void notifyDataSetChanged() {
        initIndex();
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        initIndex();
        super.notifyDataSetInvalidated();
    }

    /*
     * 功能方法
     */

    private void initIndex() {
        int itemCountTemp = 0;
        ArrayList<Integer> groupCountIndexTemp = new ArrayList<>();

        int groupCount = getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            int groupItemCount = hasHeader(i) ? 1 : 0;
            groupItemCount += getChildCount(i);
            groupItemCount += hasFooter(i) ? 1 : 0;
            itemCountTemp += groupItemCount;
            groupCountIndexTemp.add(groupItemCount);
        }

        mItemCount = itemCountTemp;
        mGroupCountIndex.clear();
        mGroupCountIndex.addAll(groupCountIndexTemp);
    }

    public GroupIndex getItemIndex(int position) {
        int size = mGroupCountIndex.size();
        int countTemp = 0;
        for (int i = 0; i < size; i++) {
            int currentGroutCount = mGroupCountIndex.get(i);
            if (position >= countTemp + currentGroutCount) {
                countTemp += currentGroutCount;
                continue;
            } else {
                int positionInGroup = position - countTemp;
                GroupIndex groupIndex = new GroupIndex(i);
                if (positionInGroup == 0 && hasHeader(i)) {
                    groupIndex.itemType = ItemType.HEADER;
                    groupIndex.childPosition = 0;
                    return groupIndex;
                }
                if (countTemp + currentGroutCount - 1 == position && hasFooter(i)) {
                    groupIndex.itemType = ItemType.FOOTER;
                    groupIndex.childPosition = 0;
                    return groupIndex;
                }
                groupIndex.itemType = ItemType.CHILD;

                groupIndex.childPosition = hasHeader(position) ? positionInGroup - 1 : positionInGroup;
                return groupIndex;
            }
        }

        throw new IndexOutOfBoundsException("groupPosition:" + position + " out of total item count .");
    }

    private void i(String msg){
//        Log.i(getClass().getSimpleName(), msg);
    }
    /*
     * 内部类
     */

    public static final class GroupIndex {
        int groupPosition;
        int childPosition;
        ItemType itemType;

        public GroupIndex() {
        }

        public GroupIndex(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        public GroupIndex(int groupPosition, int childPosition, @NonNull ItemType itemType) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
            this.itemType = itemType;
        }

        public int getGroupPosition() {
            return groupPosition;
        }

        public int getChildPosition() {
            return childPosition;
        }

        public ItemType getItemType() {
            return itemType;
        }

        public boolean isHeader() {
            return itemType == ItemType.HEADER;
        }

        public boolean isFooter() {
            return itemType == ItemType.FOOTER;
        }

        public boolean isChild() {
            return itemType == ItemType.CHILD;
        }

        @Override
        public String toString() {
            return groupPosition + "-" + childPosition + "-" + itemType;
        }
    }

    public static enum ItemType {
        HEADER, FOOTER, CHILD
    }

}
