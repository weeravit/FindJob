package com.weeravit_it.findjob.findjob.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.joanzapata.iconify.widget.IconTextView;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;
import com.weeravit_it.findjob.findjob.fragment.FeedFragment;
import com.weeravit_it.findjob.findjob.fragment.NearFragment;
import com.weeravit_it.findjob.findjob.fragment.AccountFragment;
import com.weeravit_it.findjob.findjob.fragment.SuggestFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    PagerSlidingTabStrip tabs;
    ViewPager pagers;
    FloatingActionButton floatingActionButton;

    List<Fragment> fragments;
    List<String> titles;
    List<String> tabsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();
    }

    private void initInstances() {
        pagers = (ViewPager) findViewById(R.id.pagers);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_fragment_home));

        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        tabsTitle = new ArrayList<>();

        authorizeMember();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragsWithTitles(fragments, tabsTitle);
        pagers.setAdapter(adapter);

        tabs.setViewPager(pagers);
        tabs.setOnPageChangeListener(onPageChangeListener);
//        floatingActionButton.setOnClickListener(onClickListener);
    }

    private void authorizeMember() {
        Member member = LocalStorage.getInstance().getMember();

        if (member != null) {
            // Near
            fragments.add(NearFragment.newInstance());
            titles.add(getString(R.string.title_fragment_near));
            tabsTitle.add(getString(R.string.font_location));

            // Suggest
            fragments.add(SuggestFragment.newInstance());
            titles.add(getString(R.string.title_fragment_suggest));
            tabsTitle.add(getString(R.string.font_suggest));

            // Profile
            fragments.add(AccountFragment.newInstance());
            titles.add(getString(R.string.title_fragment_account));
            tabsTitle.add(getString(R.string.font_nav));
        }

        // Feed
        fragments.add(0, FeedFragment.newInstance());
        titles.add(0, getString(R.string.title_fragment_home));
        tabsTitle.add(0, getString(R.string.font_home));
    }

//    View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            final EditText editText = new EditText(MainActivity.this);
//            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
////            editText.setRawInputType(Configuration.KEYBOARD_12KEY);
//            builder.setTitle(R.string.dialog_nearby_title);
//            builder.setView(editText);
//            builder.setPositiveButton(R.string.dialog_nearby_btn_positive, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(MainActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }
//            });
//            builder.create().show();
//        }
//    };

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(titles.get(position).equals(getString(R.string.title_fragment_near)))
                floatingActionButton.setVisibility(View.VISIBLE);
            else
                floatingActionButton.setVisibility(View.GONE);
            setTitle(titles.get(position));
            appBarLayout.setExpanded(true, true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.CustomTabProvider {

        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragsWithTitles(List<Fragment> fragments, List<String> titles) {
            mFragmentList = fragments;
            mFragmentTitleList = titles;
        }

        @Override
        public View getCustomTabView(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tab_main, viewGroup, false);
            IconTextView tab = (IconTextView) view.findViewById(R.id.tab);
            tab.setText(mFragmentTitleList.get(i));
            return view;
        }

        @Override
        public void tabSelected(View view) {
            IconTextView tab = (IconTextView) view.findViewById(R.id.tab);
            tab.setTextColor(getResources().getColor(R.color.white));
        }

        @Override
        public void tabUnselected(View view) {
            IconTextView tab = (IconTextView) view.findViewById(R.id.tab);
            tab.setTextColor(getResources().getColor(R.color.black_soft));
        }
    }

}
