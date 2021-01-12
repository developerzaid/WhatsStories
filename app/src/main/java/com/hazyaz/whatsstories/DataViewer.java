package com.hazyaz.whatsstories;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;

public class DataViewer extends AppCompatActivity {

    Toolbar mToolbar;
    ImageView imageView;
    ImageButton mDownloadButton;
    ImageButton mShareButton;
    ImageButton mDeleteButton;
    File file;
    ImageButton mPlayButton;
    private ProgressDialog mProgressBar;
    private Context mContext;
//    private InterstitialAd mInterstitialAd;
//    private InterstitialAd mInterstitialAd2;

    private static void refreshGallery(String mCurrentPhotoPath, Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        Intent intent = getIntent();

//        mToolbar = findViewById(R.id.main_page_toolbar);
//        setSupportActionBar(mToolbar);


        mProgressBar = new ProgressDialog(this);
        mContext = getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        final String imageUrl = intent.getStringExtra("IMAGEURL");


        imageView = findViewById(R.id.imageViewer);
        mDownloadButton = findViewById(R.id.imageDownload);
        mShareButton = findViewById(R.id.share);
//        mDeleteButton = findViewById(R.id.delete);
        mPlayButton = findViewById(R.id.playButton);

//        intetstial ads loading after user downloads somethinf
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-2675887677224394/4913995380");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
////        interstial ads 2
//
//        mInterstitialAd2 = new InterstitialAd(this);
//        mInterstitialAd2.setAdUnitId("ca-app-pub-2675887677224394/5155949211");
//        mInterstitialAd2.loadAd(new AdRequest.Builder().build());

        if (!imageUrl.endsWith(".mp4")) {
            mPlayButton.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
        } else {
            mPlayButton.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);

            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(imageUrl), "video/mp4");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }

            });
        }


        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mProgressBar.setTitle("Downloading File");
                mProgressBar.setMessage("Your file is being downloaded...");
                mProgressBar.setCanceledOnTouchOutside(false);
                mProgressBar.show();

                file = new File(imageUrl);
                final String mDirpath = file.getParent();
                final String mFileName = file.getName();
                final File mOutFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/Whats Stories/");


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        copyFile(mDirpath, mFileName, mOutFile.toString());
//                        Log.d("opopop", "asdyuasduyasdasdas");

                    }
                }).start();

//                to dismiss progress bar in 2 seconds


                Handler mHand = new Handler();
                mHand.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        mProgressBar.dismiss();
                        refreshGallery(mOutFile + "/" + mFileName, getApplicationContext());
                        Toast.makeText(getApplicationContext(), "File Downloaded", Toast.LENGTH_SHORT).show();


                    }
                }, 3000);


//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    Log.d("TAG", "The interstitial wasn't loaded yet.");
//                }

            }


        });


        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Please Wait", Toast.LENGTH_SHORT).show();

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.parse(imageUrl);
                sharingIntent.setType("image/png");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));

            }
        });


//        mDeleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                    mProgressBar.setTitle("Deleting");
//                    mProgressBar.setMessage("Your file in being deleted...");
//                    mProgressBar.setCanceledOnTouchOutside(false);
//                    mProgressBar.show();
//
////                to dismiss progress bar in 2 seconds
//
//                final File files = new File(imageUrl);
//
//
//                if (files.exists()) {
//                    if (files.delete()) {
//                        Handler mHand = new Handler();
//                        mHand.postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                mProgressBar.dismiss();
//                                Toast.makeText(getApplicationContext(), "File Deleted : Restart app to reflect changes", Toast.LENGTH_SHORT).show();
//                                refreshGallery(files.toString(), getApplicationContext());
//
//                            }
//                        }, 1500);
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                finish();
//
//                    if (mInterstitialAd.isLoaded()) {
//                        mInterstitialAd.show();
//                        loadAnotherAd();
//                    } else {
//                        Log.d("TAG", "The interstitial wasn't loaded yet.");
//                    }
//
//
//            }
//        });

// banner ads


    }

//    private void loadAnotherAd() {
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
//    }
//

    private void copyFile(String inputPath, String filename, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + "/" + filename);
            out = new FileOutputStream(outputPath + "/" + filename);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

            Log.d("opopop", "asd");

        } catch (FileNotFoundException fnfe1) {
            Log.e("tagdddd", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tagdddd", e.getMessage());
        }

    }


}