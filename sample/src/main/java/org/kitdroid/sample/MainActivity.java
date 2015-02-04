package org.kitdroid.sample;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.kitdroid.library.GroupAdapter;
import org.kitdroid.library.GroupAdapter.GroupIndex;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.listview)
    ListView listView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        adapter = new MyAdapter();
        listView.setAdapter(adapter);
    }

    @OnClick(R.id.text)
    public void onTextClick(TextView v){
        showToast(v.getText());
    }



    private void showToast(CharSequence  msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        showToast("Menu id:" + id);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupIndex itemIndex = adapter.getItemIndex(position);
        i(itemIndex.toString());
    }




    class MyAdapter extends GroupAdapter implements OnGlobalLayoutListener {

        @Override
        public int getGroupCount() {
            return 20;
        }

        @Override
        public boolean hasHeader(int groupPosition) {
            return true;
        }

        @Override
        public int getChildCount(int groupPosition) {
            return groupPosition + 1;
        }

        @Override
        public boolean hasFooter(int groupPosition) {
            return true;
        }

        @Override
        public Object getHeaderItem(int groupPosition) {
            return groupPosition + "-header";
        }

        @Override
        public Object getChildItem(int groupPosition, int childPosition) {
            return groupPosition +"-" + childPosition;
        }

        @Override
        public Object getFooterItem(int groupPosition) {
            return groupPosition + "-footer";
        }

        @Override
        public View getHeaderView(int groupPosition, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null){
                view = View.inflate(parent.getContext(),R.layout.child_item,null);
                view.setBackgroundColor(Color.CYAN);
                w("getHeaderView" + groupPosition);
                view.getViewTreeObserver().addOnGlobalLayoutListener(this);
            }
            TextView textName = (TextView) view.findViewById(R.id.text_name);
            textName.setText(getHeaderItem(groupPosition).toString());
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null){
                view = View.inflate(parent.getContext(),R.layout.child_item,null);
                w("getChildView" + groupPosition + "-"+childPosition);
            }
            TextView textName = (TextView) view.findViewById(R.id.text_name);
            textName.setText(getChildItem(groupPosition,childPosition).toString());
            return view;
        }

        @Override
        public View getFooterView(int groupPosition, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null){
                view = View.inflate(parent.getContext(),R.layout.child_item,null);
                view.setBackgroundColor(Color.MAGENTA);
                w("getFooterView" + groupPosition);
            }
            TextView textName = (TextView) view.findViewById(R.id.text_name);
            textName.setText(getFooterItem(groupPosition).toString());
            return view;
        }

        @Override
        public void onGlobalLayout() {
            View childAt = listView.getChildAt(3);
            if(childAt != null){
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(childAt.getLayoutParams());
//                layoutParams.setMargins(0,10,0,1);
//                childAt.setLayoutParams(layoutParams);
            }
        }
    }

    private void i(String msg){
        Log.i("Kit-"+getClass().getSimpleName(), msg);
    }
    private void w(String msg){
        Log.w("Kit-"+getClass().getSimpleName(), msg);
    }
}
