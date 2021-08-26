package com.example.capstone;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.capstone.login.LogIn;
import com.example.capstone.pushmessage.UndeadService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageVIew;
    private int nBefore = 0;

    Intent foregroundServiceIntent;

    /*
    애플리케이션 시작 시 첫 화면 및
    백그라운드에서의 푸시알림 기능을 위한 코드
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        memory over run..
        if want only once subscribed remark(//) the subscribeToTopic(~) line
        and go onNewToken of MyFirebaseMessagingService.class
         */
        FirebaseMessaging.getInstance().subscribeToTopic("newcomers_fcm");
        FirebaseInstanceId.getInstance().getToken();
        getAppKeyHash();

        final ConstraintLayout CL = (ConstraintLayout)findViewById(R.id.constraintLayout);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }

        //화면 터치 시 다음 페이지로 이동하도록 함
        CL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Intent intent = new Intent(MainActivity.this, LogIn.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                }
                return true;
            }
        });

        if (null == UndeadService.serviceIntent) {
            foregroundServiceIntent = new Intent(this, UndeadService.class);
            startService(foregroundServiceIntent);

        } else {
            foregroundServiceIntent = UndeadService.serviceIntent;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected  void onDestroy() {
        super.onDestroy();

        if (null != foregroundServiceIntent) {
            stopService(foregroundServiceIntent);
            foregroundServiceIntent = null;
        }
    }
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
}

