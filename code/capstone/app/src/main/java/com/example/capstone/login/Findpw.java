package com.example.capstone.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Findpw extends AppCompatActivity {
    String name = "";
    String num = "";
    EditText findname, findnum;
    Button findbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpw);

        findname = (EditText)findViewById(R.id.findpw_name);
        findnum = (EditText)findViewById(R.id.findpw_phone);
        findbtn = (Button)findViewById(R.id.findpw_btn);

        findbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                name = findname.getText().toString();
                num = findnum.getText().toString();
                FindDB fdb = new FindDB();
                fdb.execute();
            }
        });
    }

    void show(String data){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 확인 창");
        if (data.equals("") || data == null) {
            builder.setMessage("찾는 계정이 없습니다.");
        } else {
            builder.setMessage("비밀번호는 " + data + " 입니다.");
        }
        builder.setNegativeButton("로그인 창으로", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
                overridePendingTransition(R.anim.leftin_activity, R.anim.rightout_activity);
            }
        });
        builder.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private class FindDB extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings){
            // 인풋 파라메터값 생성
            String param = "u_name=" + name + "&u_num=" + num + "";
            //String param = getString(R.string.login_param);
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/Login.php");
                URL url = new URL(getString(R.string.ip) + getString(R.string.find_pw));
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
                show(data);
            } catch (Exception e){
                e.printStackTrace();
            }
            //overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
        }
    }
}


