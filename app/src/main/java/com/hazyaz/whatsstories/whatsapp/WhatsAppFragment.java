package com.hazyaz.whatsstories.whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hazyaz.whatsstories.Adapter.CustomStoryAdapter;
import com.hazyaz.whatsstories.DataViewer;
import com.hazyaz.whatsstories.R;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class WhatsAppFragment extends Fragment {


    View mMainView;
    GridView mGridview;
    String[] files;
    File[] Lfiles;
    private LinearLayout mNoData;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.whatsapp_fragment, null, false);

        mGridview = mMainView.findViewById(R.id.gridview);
        mNoData = mMainView.findViewById(R.id.NoDataRecyclerView);


        File recent = new File(Environment.getExternalStorageDirectory(), "/WhatsApp/Media/.Statuses");
        Lfiles = recent.listFiles();
if(Lfiles != null) {
    for (File f : Lfiles) {

        f.getName();
        if (f.getName().trim().equalsIgnoreCase(".nomedia")) {
            f.delete();
            continue;
        }
    }

    if (Lfiles != null) {
        Arrays.sort(Lfiles, new Comparator() {
            public int compare(Object o1, Object o2) {

                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });
        files = new String[Lfiles.length];


        int i = 0;

        for (File f : Lfiles) {
            files[i] = f.getAbsolutePath();
            i++;

        }

    } else {
        files = null;
    }
}


        if (files!=null){

            mNoData.setVisibility(View.GONE);
            mGridview.setVisibility(View.VISIBLE);
            mGridview.setAdapter(new CustomStoryAdapter(getContext(), files));

            mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(getActivity(), DataViewer.class);
                    intent.putExtra("IMAGEURL", files[position]);
                    startActivity(intent);

                }
            });
        } else {
            mNoData.setVisibility(View.VISIBLE);
            mGridview.setVisibility(View.GONE);
        }


        mGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "You are Awesome and we love you", Toast.LENGTH_SHORT).show();
                return false;

            }
        });

        // Inflate the layout for this fragment
        return mMainView;

    }
}
