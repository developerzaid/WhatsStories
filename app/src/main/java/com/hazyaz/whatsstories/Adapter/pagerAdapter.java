package com.hazyaz.whatsstories.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.hazyaz.whatsstories.whatsapp.WhatsAppBSFragment;
import com.hazyaz.whatsstories.whatsapp.WhatsAppFragment;


public class pagerAdapter extends FragmentPagerAdapter {

    private InterstitialAd mInterstitialAd;

    public pagerAdapter(FragmentManager fm, InterstitialAd Ad) {
        super(fm);
        mInterstitialAd = Ad;
    }


    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                return new WhatsAppFragment();


            case 1:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        mInterstitialAd.show();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }

                    @Override
                    public void onAdOpened() {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());

                    }

                    @Override
                    public void onAdClosed() {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                });

                return new WhatsAppBSFragment();


            default:
                return null;

        }

    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position)
        {
            case 0:
                return "WhatsApp";

            case 1:
                return "WhatsApp Business";

            default:
                return null;

        }

    }
}
