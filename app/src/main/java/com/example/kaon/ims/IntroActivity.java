package com.example.kaon.ims;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

public class IntroActivity  extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    /** 처음 액티비티가 생성될때 불려진다. */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
          @Override
          public void run() {
              Intent Loginintent = new Intent(IntroActivity.this, LoginActivity.class);
              IntroActivity.this.startActivity(Loginintent);
              IntroActivity.this.finish();
          }
      },SPLASH_DISPLAY_LENGTH);
    }
}
