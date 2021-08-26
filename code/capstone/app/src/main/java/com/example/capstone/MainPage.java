package com.example.capstone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.capstone.ar.AR;
import com.example.capstone.ar.UnityPlayerActivity;
import com.example.capstone.login.LogIn;
import com.example.capstone.map.Recommend;
import com.example.capstone.qr.Tourpass;
import com.example.capstone.setting.Qna;
import com.example.capstone.together.Together;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kakao.auth.Session;
import com.kakao.util.helper.log.Logger;

import me.relex.circleindicator.CircleIndicator3;

public class MainPage extends AppCompatActivity {
    private long backPressedTime = 0;
    private final long FINISH_INTERVAL_TIME = 2000;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 4개 메뉴에 들어갈 프레그먼트
    private Together chatFragment = new Together();
    private Recommend mapFragment = new Recommend();
    private Tourpass createQR = new Tourpass();
    private AR arFragment_btn = new AR();
    private UnityPlayerActivity arFragment = new UnityPlayerActivity();
    private Qna optionFragment = new Qna();

    int requestCode;
    int resultCode;
    Intent data;

    double longitude;
    double latitude;

    TextView LogO;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(getLocalClassName(), "KAKAO_API onActivityResult");
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Logger.d(getLocalClassName(), "KAKAO_API handleActivityResult");
            return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        //다른 액티비티에서의 변화가 있을 경우 즉시 반영시키기 위함
        onActivityResult(requestCode, resultCode, data);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        //위치정보 서비스
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            //ActivityCompat.requestPermissions( MainPage.this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, 0);
            Toast.makeText(MainPage.this, "위치정보 권한 거부 시 기능 사용에 제약이 있을 수 있습니다.", Toast.LENGTH_SHORT).show();
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
        }


        LogO = (TextView)findViewById(R.id.logout);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, chatFragment).commitAllowingStateLoss();

        //bottomNavigationView 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_chat: {
                        transaction.replace(R.id.frame_layout, chatFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_map: {
                        transaction.replace(R.id.frame_layout, mapFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_ar: {
                        //하단 버튼 클릭 시 사용자 위치정보 확인
                        if ( Build.VERSION.SDK_INT >= 23 &&
                                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                            //ActivityCompat.requestPermissions( MainPage.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  }, 0 );
                        } else {
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
                            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
                        }

                        //Toast.makeText(MainPage.this, "현재 위치 \n위도 " + latitude + "\n경도" + longitude, Toast.LENGTH_SHORT).show();
                        transaction.replace(R.id.frame_layout, arFragment_btn).commitAllowingStateLoss();
                        break;
                        //AR이라 프래그먼트로 구현이 어려움
                        //Intent intent = new Intent(MainPage.this, UnityPlayerActivity.class);
                        //startActivity(intent);
                    }
                    case R.id.navigation_tourpass: {
                        transaction.replace(R.id.frame_layout, createQR).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_options: {
                        transaction.replace(R.id.frame_layout, optionFragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });

        //로그아웃 버튼 클릭 시
        LogO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
    }

    //로그아웃 팝업 알람
    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃");
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainPage.this, LogIn.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    //위치정보 확인 리스너
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            double altitude = location.getAltitude();
            float accuracy = location.getAccuracy();
            String provider = location.getProvider();

            //Toast.makeText(MainPage.this, "현재 위치 \n위도 " + latitude + "\n경도" + longitude, Toast.LENGTH_SHORT).show();

            Bundle result = new Bundle();
            result.putDouble("longitude", longitude);
            result.putDouble("latitude", latitude);
            createQR.setArguments(result);
            mapFragment.setArguments(result);

            /*
            Intent intent = new Intent(MainPage.this, UnityPlayerActivity.class);
            intent.putExtra("longitude", longitude);
            intent.putExtra("latitude", latitude);
            startActivity(intent);
            */
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    //뒤로가기 버튼 두번 누를 시 종료
    public void onBackPressed(){
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "종료를 원하시면 한번 더 누르세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
