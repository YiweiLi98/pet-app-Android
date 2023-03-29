package comp5216.sydney.edu.au.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.finalproject.adapter.HomePageAdapter;
import comp5216.sydney.edu.au.finalproject.pojo.Post;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    String sessionId = null;
    // layout setting
    private ViewPager viewPager;
    private HomeFragment homeFragment;
    private NewFragment newFragment;
    private FriendFragment friendFragment;
    private MeFragment meFragment;
    private List<Fragment> fragmentList;
    private List<Post> postList;
    private String mePageData;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(
                MainActivity.this,
                ImagePipelineConfig.newBuilder(MainActivity.this)
                        .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                        .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                        .experiment().setNativeCodeDisabled(true)
                        .build());
        viewPager = findViewById(R.id.viewPager);
        homeFragment = new HomeFragment();
        newFragment = new NewFragment();
        friendFragment = new FriendFragment();
        meFragment = new MeFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(homeFragment);
        fragmentList.add(newFragment);
        fragmentList.add(friendFragment);
        fragmentList.add(meFragment);
        postList = new ArrayList<>();
        mePageData = "CHECK FROM MAIN";
        turnOnWifi();
        initView();
//        login();
    }

    public void turnOnWifi(){
        WifiManager wifiManager = (WifiManager)this.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()){
            return;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//            startActivity(new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY));
            Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
            startActivityForResult(panelIntent,1);
        }else{

            wifiManager.setWifiEnabled(true);
            Log.i("WIFI", Boolean.toString(wifiManager.isWifiEnabled()));
        }
    }

    @Override
    public void onClick(View view) {

    }


    private void initView(){
        viewPager.setAdapter(new HomePageAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setOffscreenPageLimit(3);

        // Tab layout setting
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTab("Home", R.drawable.ic_baseline_home_24)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTab("New", R.drawable.ic_baseline_add_circle_outline_24)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTab("Friend", R.drawable.ic_baseline_supervised_user_circle_24)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTab("Me", R.drawable.ic_baseline_account_circle_24)));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv_title);
                tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
                ImageView iv = tab.getCustomView().findViewById(R.id.iv_icon);
                iv.setColorFilter(Color.RED);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv_title);
                ImageView iv = tab.getCustomView().findViewById(R.id.iv_icon);
                iv.setColorFilter(Color.BLACK);
                tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                System.out.println("CHANGE: ");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                TextView tv =  tabLayout.getTabAt(0).getCustomView().findViewById(R.id.tv_title);
                ImageView iv =  tabLayout.getTabAt(0).getCustomView().findViewById(R.id.iv_icon);
                tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
                iv.setColorFilter(Color.RED);
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
    private View createTab(String name, int iconId) {
        View newTabItem = LayoutInflater.from(this).inflate(R.layout.my_tab, null);
        TextView tx = (TextView) newTabItem.findViewById(R.id.tv_title);
        tx.setText(name);
        ImageView im = newTabItem.findViewById(R.id.iv_icon);
        im.setImageResource(iconId);
        return newTabItem;
    }

}