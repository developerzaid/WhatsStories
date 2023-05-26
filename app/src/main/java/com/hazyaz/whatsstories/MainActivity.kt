package com.hazyaz.whatsstories

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.documentfile.provider.DocumentFile
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.hazyaz.whatsstories.Adapters.CustomPagerAdapter
import com.hazyaz.whatsstories.Utils.DepthPageTransformer
import com.hazyaz.whatsstories.Utils.SharePreferencesManager
import com.hazyaz.whatsstories.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityMainBinding? = null
    var mToolbar: Toolbar? = null
    var mViewPager: ViewPager? = null
    var mTabLayout: TabLayout? = null

    private var mInterstitialAd: InterstitialAd? = null
    private var sharedPreferences: SharePreferencesManager? = null

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setAdds()
        initAds()
        initViews()
    }

    @RequiresApi(Build.VERSION_CODES.M)




    @SuppressLint("NonConstantResourceId")
    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.more_apps -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=Hazyaz++Technologies&hl=en_IN")
                )
                startActivity(intent)
            }

            R.id.Share -> try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Whats Status | Save Whatsapp Status")
                var shareMessage = "\nDownload Whats status, it lets you download video status\n\n"
                shareMessage =
                    """
                    ${shareMessage}https://play.google.com/store/apps/details?id=com.hazyaz.whatsstatus
                    
                    
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to share", Toast.LENGTH_SHORT).show()
            }


            R.id.contact_us -> try{
                val intent34 = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("mailto:" + "contact@hazyaztechnologies.in")
                )
                intent34.putExtra(Intent.EXTRA_SUBJECT, "your_subject")
                intent34.putExtra(Intent.EXTRA_TEXT, "your_text")
                startActivity(intent34)
            }
            catch (e: Exception) {
                Toast.makeText(applicationContext, "Mailing Problem", Toast.LENGTH_LONG).show();
            }
        }
        val drawer = binding!!.drawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    private fun initAds() {
        MobileAds.initialize(this) { }
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        val Inter1 = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-2675887677224394/3412098377", Inter1,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    Log.i(TAG, "onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.i(TAG, loadAdError.message)
                    mInterstitialAd = null
                }
            })
    }

    private fun initViews() {
//        RestarterBroadcastReceiver.startWorker(applicationContext)

        sharedPreferences = SharePreferencesManager(applicationContext)

        mToolbar = binding!!.mainPageToolbar.mainAppBar
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = "Whats Status"
        mViewPager = binding!!.viewPagerMain

//        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
//        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager!!.setPageTransformer(true, DepthPageTransformer())
        mTabLayout = binding!!.tabBarMain
        mTabLayout!!.setupWithViewPager(mViewPager)
        val drawer = binding!!.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            mToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)



//      For showing Interstial Ads while swiping between tabs and banner ad below screen
        val pagerAdapter = CustomPagerAdapter(supportFragmentManager)
        mViewPager!!.adapter = pagerAdapter
        //change Tab selection when swipe ViewPager
        mViewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
        //change ViewPager page when tab selected
        mTabLayout!!.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 1 && mInterstitialAd != null) {
                    mInterstitialAd!!.show(this@MainActivity)
                }
                mViewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
//        isReadStoragePermissionGranted
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(this@MainActivity)
            finish()
        } else {
            finish()
        }
    }

    companion object {
        private const val TAG = "MainActivity__"
        private const val OPEN_STATUS_FOLDER_REQUEST_CODE = 2
        private const val OPEN_BS_STATUS_FOLDER_REQUEST_CODE = 3
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_STATUS_FOLDER_REQUEST_CODE -> {
                    val directoryUri = data?.data ?: return
                    Log.d("DATA____", directoryUri.toString())
                    contentResolver.takePersistableUriPermission(
                        directoryUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    val documentsTree =
                        DocumentFile.fromTreeUri(application, directoryUri) ?: return
                    val childDocuments = documentsTree.listFiles().asList()
                    Log.d("DATA____", documentsTree.name.toString())
                    Log.d("DATA____", childDocuments.toString())
                    Log.d("DATA____", childDocuments.size.toString())

                    if (directoryUri.toString() == "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
                    ) {
                        sharedPreferences?.statusFolder = "status"
                        Toast.makeText(
                            this@MainActivity,
                            "Permission granted",
                            Toast.LENGTH_SHORT
                        ).show()
                        sendBroadcastMsg("status")
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Pls Grant Access to Whatsapp Status Folder",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                OPEN_BS_STATUS_FOLDER_REQUEST_CODE -> {
                    val directoryUri = data?.data ?: return
                    Log.d("DATA____", directoryUri.toString())
                    contentResolver.takePersistableUriPermission(
                        directoryUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    val documentsTree =
                        DocumentFile.fromTreeUri(application, directoryUri) ?: return
                    val childDocuments = documentsTree.listFiles().asList()
                    Log.d("DATA____", documentsTree.name.toString())
                    Log.d("DATA____", childDocuments.toString())
                    Log.d("DATA____", childDocuments.size.toString())

                    if (directoryUri.toString() == "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp%20Business%2FMedia%2F.Statuses"
                    ) {
                        sharedPreferences?.bsStatusFolder = "status"
                        Toast.makeText(
                            this@MainActivity,
                            "Permission granted",
                            Toast.LENGTH_SHORT
                        ).show()
                        sendBroadcastMsg("status")
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Pls Grant Access to Whatsapp Business Status Folder",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            }
        }
    }

    private fun sendBroadcastMsg(name: String) {
        val intent = Intent(name)
        intent.setPackage(packageName)
        intent.putExtra("message", name)
        applicationContext.sendBroadcast(intent)
    }


    private fun setAdds() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-2675887677224394/3412098377",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })

    }


    fun showAdd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    Log.d(TAG, "Ad dismissed fullscreen content.")
                    mInterstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.")
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.")
                }
            }
            mInterstitialAd?.show(this)
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.")
        }
    }

    // Permissions

    fun openFolderForStatus() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        val wa_status_uri =
            Uri.parse(
                "content://com.android.externalstorage.documents/tree/primary%3" +
                        "AAndroid%2Fmedia/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, wa_status_uri)
        }
        startActivityForResult(intent, OPEN_STATUS_FOLDER_REQUEST_CODE)
    }
    fun openFolderForBSStatus() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        val wa_status_uri =
            Uri.parse(
                "content://com.android.externalstorage.documents/tree/primary%3" +
                        "AAndroid%2Fmedia/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp%20Business%2FMedia%2F.Statuses"
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, wa_status_uri)
        }
        startActivityForResult(intent, OPEN_BS_STATUS_FOLDER_REQUEST_CODE)
    }


}


