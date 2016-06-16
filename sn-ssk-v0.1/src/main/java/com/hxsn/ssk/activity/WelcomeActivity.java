package com.hxsn.ssk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hxsn.ssk.R;
import com.hxsn.ssk.base.BaseBanner;

public class WelcomeActivity extends Activity {

    // @InjectView(R.id.viewPager) ViewPager viewPager;
    // @InjectView(R.id.round_group) ViewGroup roundGroup;
    private BaseBanner banner;

    private int[] ids = {R.drawable.index01, R.drawable.index02, R.drawable.index03,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);

        ImageView imageView = (ImageView) findViewById(R.id.img_background);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // addBanner();
    }

   /* private void addBanner() {
        banner = new BaseBanner(this,this,viewPager,roundGroup);
        banner.init();
        banner.start();
    }*/

    @Override
    protected void onStop() {
        // banner.stop();
        super.onStop();
    }
}
