package com.jpes707.masterdetailflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MasterFragment.MasterFragmentListener, DetailFragment.DetailFragmentListener {

    /* ------------------------*/
    /*    member variables     */

    private static final String STORAGE_KEY = "goals";
    ViewPager2 mViewPager2;
    MyViewPagerAdapter mMyViewPagerAdapter;

    /* ------------------------------------------*/
    /*    LIFECYCLE METHODS                      */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        mViewPager2 = findViewById(R.id.container);          // assign the instance of ViewPager2
        mMyViewPagerAdapter = new MyViewPagerAdapter(this);
        mViewPager2.setAdapter(mMyViewPagerAdapter);         // bind the adapter to the viewpager2

        mViewPager2.setUserInputEnabled(false);              // disable swiping

    }

    /* --------------------------------------------- */
    /*  This interface method is called when the     */
    /*  user taps on a view in the master fragment   */
    /* --------------------------------------------- */
    @Override
    public void onMasterFragmentData(int i) {

        // tell my adapter what the string should be
        mMyViewPagerAdapter.setGoalNumber(i + 1);

        // change the position
        mViewPager2.setCurrentItem(1, false);

    }


    /* --------------------------------------------- */
    /*  This interface method is called when the     */
    /*  user taps back in the detail fragment        */
    /* --------------------------------------------- */
    @Override
    public void onDetailFragmentAction() {
        // change the position
        mViewPager2.setCurrentItem(0, false);
    }



    /* --------------------------------------------- */
    /*  This class is responsible for loading        */
    /*  fragments into the ViewPager2                */
    /* --------------------------------------------- */

    private class MyViewPagerAdapter extends FragmentStateAdapter {

        MyViewPagerAdapter(MainActivity ma) {
            super(ma);
        }

        void setGoalNumber(int goalNumber) {
            SharedPreferences.Editor mEditor = getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE).edit();
            mEditor.putInt("goalNumber", goalNumber);
            mEditor.apply();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment res;
            if (position == 1) {
                res = DetailFragment.newInstance();
            } else {// retain references to the fragments in the adapter
                res = MasterFragment.newInstance();
            }
            return res;
        }

        @Override
        public int getItemCount() {
            return 2;       // there are only two fragments, the master and the detail
        }
    }
}