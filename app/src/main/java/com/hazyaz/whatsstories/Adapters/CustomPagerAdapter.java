package com.hazyaz.whatsstories.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hazyaz.whatsstories.Fragments.StorySaverFragment;
import com.hazyaz.whatsstories.Fragments.WhatsAppBSFragment;


public class CustomPagerAdapter extends FragmentPagerAdapter {


    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                return new StorySaverFragment();
            case 1:
                return new WhatsAppBSFragment();

            default:
                return null;
        }

    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "WHATSAPP";
            case 1:
                return "WHATSAPP BUSINESS";
            default:
                return null;
        }

    }
}