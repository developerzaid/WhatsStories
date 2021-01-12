package com.hazyaz.whatsstories.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.hazyaz.whatsstories.R;

import java.util.ArrayList;

public class CustomStoryAdapter extends ArrayAdapter {

    private Context mContext;

    public String[] files;
    private LayoutInflater inflater;
    int x=0;

    public CustomStoryAdapter(Context c, String[] files){
        super(c, R.layout.story_img,files);
        this.mContext = c;
        this.files=files;
        inflater = LayoutInflater.from(c);
    }

    ArrayList<String> Images = new ArrayList<>();


    @Override
    public int getCount() {
        if (files!=null){
            return files.length;}
        else { return 0;}
    }

    @Override
    public Object getItem(int position)
    {
        Log.d("format","aaaaaaaaaaaaaaaaaaaaaab");
        return position;
    }

    @Override
    public long getItemId(int position)
    {
//      Log.d("format",files[position]);
        return position;
    }



    public View getView(int position, View convertView, ViewGroup parent) {


        if (files != null) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.story_img, parent, false);
            }

            Glide
                    .with(mContext)
                    .load(files[position])
                    .into((ImageView) convertView);
            x++;
            Log.d("valx",Integer.toString(x));
        }

            return convertView;


    }



}
