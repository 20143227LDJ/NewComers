package com.example.capstone.together;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.MainPage;
import com.example.capstone.R;
import com.example.capstone.login.LogIn;
import com.example.capstone.together.listview.CustomWord;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TogetherContents extends AppCompatActivity {
    TextView recommend_text;

    String user_phone = "";
    String no_main = "";
    String phone_main = "";
    String name_main = "";
    String email_main = "";
    String sex_main = "";
    String contents_title_main = "";
    String contents_main = "";
    String area_main = "";
    String time_main = "";

    TextView tv_phone;
    TextView tv_name;
    TextView tv_email;
    TextView tv_sex;
    TextView tv_contents_title;
    TextView tv_contents;
    TextView tv_area;
    TextView tv_time;



    //intent로 받은 게시글 내용 출력
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.togethercontents);

        Button backbtn = (Button) findViewById(R.id.togethercontents_back);
        TextView partybtn = (TextView) findViewById(R.id.together_party);

        backbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        partybtn.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        user_phone = getIntent().getExtras().getString("user_phone");
        no_main = getIntent().getExtras().getString("no");
        phone_main = getIntent().getExtras().getString("phone_num");
        name_main = getIntent().getExtras().getString("name");
        email_main = getIntent().getExtras().getString("email");
        sex_main = getIntent().getExtras().getString("sex");
        contents_title_main = getIntent().getExtras().getString("title");
        contents_main = getIntent().getExtras().getString("content");
        area_main = getIntent().getExtras().getString("area");
        time_main = getIntent().getExtras().getString("time_t");

        tv_phone = (TextView) findViewById(R.id.together_phone);
        tv_name = (TextView) findViewById(R.id.together_name);
        tv_email = (TextView) findViewById(R.id.together_email);
        tv_sex = (TextView) findViewById(R.id.together_sex);
        tv_contents_title = (TextView)findViewById(R.id.together_contents_title);
        tv_contents = (TextView) findViewById(R.id.together_contents);
        tv_area = (TextView) findViewById(R.id.together_area);
        tv_time = (TextView) findViewById(R.id.together_time);

        tv_phone.setText(phone_main);
        tv_name.setText(name_main);
        tv_email.setText(email_main);
        tv_sex.setText(sex_main);
        tv_contents_title.setText(contents_title_main);
        tv_contents.setText(contents_main);
        tv_area.setText(area_main);
        tv_time.setText(time_main);
    }

    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("파티 참가");
        builder.setMessage("이 파티에 참여 하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PartyDB pdb = new PartyDB();
                pdb.execute();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public class PartyDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "no=" + no_main + "&phone_num=" + phone_main + "&user_phone=" + user_phone + ""; //"phone_num=" + id + ""; 추가
            Log.e("POST", param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/notice_board_find.php"); //PHP 코드
                //URL url = new URL("http://172.30.1.19:9090/noticeboard_find/");    //GO 코드
                URL url = new URL(getString(R.string.ip) + getString(R.string.partification_addr));
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
                while ((line = in.readLine()) != null) {
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
                FirebaseMessaging.getInstance().subscribeToTopic(no_main);
                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
