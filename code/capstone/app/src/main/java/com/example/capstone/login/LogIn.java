package com.example.capstone.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.MainPage;
import com.example.capstone.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LogIn extends AppCompatActivity {

    private LogIn.SessionCallback sessionCallback;

    private long backPressedTime = 0;
    private final long FINISH_INTERVAL_TIME = 2000;

    String id = "";
    String pass = "";

    Button log, sign, kakao;
    LoginButton btn_kakao_login;
    TextView tvid, tvpw, tvsignup;

    EditText etid, etpw;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    로그인 입니다.
    전역변수로 아이디와 비밀번호를 설정하고
    로그인 버튼을 눌렀을 시 에디트 박스에 있는
    아이디와 비밀번호의 내용을 가져와서
    데이터베이스와 통신합니다.
     */
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sessionCallback = new LogIn.SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

        etid = (EditText)findViewById(R.id.editid);
        etpw = (EditText)findViewById(R.id.editpw);
        tvid = (TextView)findViewById(R.id.id_find);
        tvpw = (TextView)findViewById(R.id.pw_find);
        tvsignup = (TextView)findViewById(R.id.sign_up);

        log = (Button)findViewById(R.id.login);
        kakao = (Button)findViewById(R.id.kakao_btn);

        //비밀번호 찾기 클릭 시
        tvpw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LogIn.this, Findpw.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity);
            }
        });

        //회원가입 버튼 클릭 시
        tvsignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LogIn.this, Signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity);
            }
        });

        //로그인 버튼 클릭 시
        log.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id = etid.getText().toString();
                pass = etpw.getText().toString();
                LoginDB LDB = new LoginDB();
                LDB.execute();
                //Intent intent = new Intent(LogIn.this, MainPage.class);
                //intent.putExtra("phone_num", id);
                //startActivity(intent);
                //overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
            }
        });

        kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_kakao_login.performClick();
                Session.getCurrentSession().checkAndImplicitOpen(); //자동 로그인
            }
        });
        btn_kakao_login = (LoginButton) findViewById(R.id.btn_kakao_login);
    }

    //로그인 데이터베이스 연동
    private class LoginDB extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings){
            // 인풋 파라메터값 생성
            String param = "u_id=" + id + "&u_pass=" + pass + "";
            //String param = getString(R.string.login_param);
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/Login.php");
                URL url = new URL(getString(R.string.ip) + getString(R.string.login_addr));
                //URL url = new URL("http://172.30.1.19:9090/login/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                // 안드로이드 -> 서버 파라메터값 전달
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                // 서버 -> 안드로이드 파라메터값 전달
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                // 서버에서 응답
                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            try {
                if (data.equals("true")) {
                    //Toast.makeText(getApplicationContext(), "로그인!", Toast.LENGTH_SHORT).show();
                    //여기서 인텐트를 통해서 로그인 후 메인페이지로 이동시키면 됨
                    Intent dbintent = new Intent(LogIn.this, MainPage.class);
                    dbintent.putExtra("phone_num", id);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("newcomers_fcm");
                    FirebaseMessaging.getInstance().subscribeToTopic("busan");
                    startActivity(dbintent);
                    overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
                } else {
                    Toast.makeText(getApplicationContext(), "아이디(전화번호) 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            //overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
        }
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();
                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Toast.makeText(getApplicationContext(),"Success Test",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                    intent.putExtra("name", result.getNickname());
                    intent.putExtra("profile", result.getProfileImagePath());
                    setResult(RESULT_OK, intent);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
