package org.kitdroid.library;/**
 * Created by huiyh on 15/1/28.
 */

import android.support.annotation.NonNull;
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
        return mItemCount;
    }

    @Override
    public final Object getItem(int position) {
        GroupIndex itemIndex = getItemIndex(position);
        if(itemIndex.isHeader()){
            return getGroupHeaderItem(itemIndex.groupPosition);
        }
        if(itemIndex.isFooter()){
            return getGroupFooterItem(itemIndex.groupPosition);
        }
        return getGroupChildItem(itemIndex.groupPosition,itemIndex.childPosition);
    }

    @Override
    public final long getItemId(int position) {
        GroupIndex itemIndex = getItemIndex(position);
        if(itemIndex.isHeader()){
            return getGroupHeaderItemId(itemIndex.groupPosition);
        }
        if(itemIndex.isFooter()){
            return getGroupFooterItemId(itemIndex.groupPosition);
        }
        return getGroupChildItemId(itemIndex.groupPosition, itemIndex.childPosition);
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public final int getViewTypeCount() {
        return TYPE_COUNT_OF_HEADER_AND_FOOTER + getChildViewTypeCount();
    }

    @Override
    public final int getItemViewType(int position) {
        GroupIndex itemIndex = getItemIndex(position);
        if (itemIndex.isHeader()) {
            return TYPE_OF_HEADER;
        }
        if (itemIndex.isFooter()) {
            return TYPE_OF_FOOTER;
        }
        return TYPE_COUNT_OF_HEADER_AND_FOOTER + getChildViewType(itemIndex.groupPosition, itemIndex.childPosition);
    }

    /*
     * 为子类提示的方法
     */

    /*
     * for getCount()
     */
    public abstract int getGroupCount();

    public boolean hasGroupHeader(int groupPosition) {
        return false;
    }

    public boolean hasGroupFooter(int groupPosition) {
        return false;
    }

    public abstract int getGroupChildCount(int groupPosition);


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
    public Object getGroupHeaderItem(int groupPosition) {
        return null;
    }

    public Object getGroupFooterItem(int groupPosition) {
        return null;
    }

    public abstract Object getGroupChildItem(int groupPosition, int childPosition);

    /*
     * for getItemId(int position)
     */
    private long getGroupHeaderItemId(int groupPosition) {
        return 0;
    }

    private long getGroupFooterItemId(int groupPosition) {
        return 0;
    }

    public long getGroupChildItemId(int groupPosition, int childPosition) {
        return 0;
    }

    /*
     *for getView(int position, View convertView, ViewGroup parent)
     */
    public View getGroupHeaderView(int groupPosition, View convertView, ViewGroup parent) {
        return null;
    }

    public View getGroupFooterView(int groupPosition, View convertView, ViewGroup parent) {
        return null;
    }

    public abstract int getGroupChildView(int groupPosition, int childPosition, View convertView, ViewGroup parent);

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
            int groupItemCount = hasGroupHeader(i) ? 1 : 0;
            groupItemCount += getGroupChildCount(i);
            groupItemCount += hasGroupFooter(i) ? 1 : 0;
            itemCountTemp += groupItemCount;
            groupCountIndexTemp.add(groupItemCount);
        }

        mItemCount = itemCountTemp;
        mGroupCountIndex.clear();
        mGroupCountIndex.addAll(groupCountIndexTemp);
    }

    private GroupIndex getItemIndex(int position) {
        int size = mGroupCountIndex.size();
        int countTemp = 0;
        for (int i = 0; i < size; i++) {
            int currentGroutCount = mGroupCountIndex.get(i);
            if (position > countTemp + currentGroutCount) {
                countTemp += currentGroutCount;
                continue;
            } else {
                int positionInGroup = position - countTemp;
                GroupIndex groupIndex = new GroupIndex(i);
                if (positionInGroup == 0 && hasGroupHeader(i)) {
                    groupIndex.itemType = ItemType.HEADER;
                    groupIndex.childPosition = 0;
                    return groupIndex;
                }
                if (countTemp + currentGroutCount == position && hasGroupFooter(i)) {
                    groupIndex.itemType = ItemType.FOOTER;
                    groupIndex.childPosition = 0;
                    return groupIndex;
                }
                groupIndex.itemType = ItemType.CHILD;

                groupIndex.childPosition = hasGroupHeader(position) ? positionInGroup - 1 : positionInGroup;
                return groupIndex;
            }
        }

        throw new IndexOutOfBoundsException("groupPosition:" + position + " out of total item count .");
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
    }

    public static enum ItemType {
        HEADER, FOOTER, CHILD
    }
}
