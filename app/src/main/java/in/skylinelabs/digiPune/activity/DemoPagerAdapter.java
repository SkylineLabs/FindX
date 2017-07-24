package in.skylinelabs.digiPune.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Random;

public class DemoPagerAdapter extends FragmentPagerAdapter {

    private int pagerCount = 5;

    private Random random = new Random();

    public DemoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0)
            return ColorFragment.newInstance(Color.parseColor("#0d47a1"), i);//red

        if(i==1)
        return ColorFragment.newInstance(Color.parseColor("#00958a"), i);//pink

        if(i==2)
            return ColorFragment.newInstance(Color.parseColor("#ff5252"), i);//green
        if(i==3)
            return ColorFragment.newInstance(Color.parseColor("#ffab40"), i);

        if(i==4)
            return ColorFragment.newInstance(Color.parseColor("#0d47a1"), i);

        else
            return null;

    }

    @Override
    public int getCount() {
        return pagerCount;
    }
}