package com.example.capstone.together;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.together.listview.CustomWord;
import com.example.capstone.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//게시글 삭제. 게시글 번호와 게시글 제목을 출력하기 위해 ArrayList를 <CustomWord>로 설정
public class DeleteContents extends AppCompatActivity {
    Intent gintent;
    String phone_num = "";
    String notice_board = "";
    String no = "";
    String party = "";
    String count = "";
    Bitmap bmap;
    //String contents_text = "";

    CustomWord contents_text;
    ArrayList<String> nos = new ArrayList<String>();
    //ArrayList<String> contents = new ArrayList<String>();
    ArrayList<CustomWord> contents = new ArrayList<CustomWord>();
    ListView contents_listview;
    TogetherAdapter contents_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticeboard_delete);

        gintent = getIntent();
        phone_num = gintent.getStringExtra("phone_num");

        contents_adapter = new TogetherAdapter(this, R.layout.together_simpleitem, contents);
        contents_listview = (ListView) findViewById(R.id.delete_listview);
        Button backbtn = (Button) findViewById(R.id.del_back);

        //로그인 된 사용자가 작성한 게시글을 모두 불러옴
        DeleteFindDB dfdb = new DeleteFindDB();
        dfdb.execute();

        //게시글 클릭 시 삭제 알림을 띄움
        contents_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //contents_text = (String)parent.getItemAtPosition(position);
                contents_text = (CustomWord)parent.getItemAtPosition(position);
                no = nos.get(position);

                show(); //show함수를 부름(알림창 띄움)
            }
        });
        backbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }

    //알림 설정 내용
    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시글 삭제");
        builder.setMessage(no + "번 게시글을 삭제하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //클릭된 데이터 삭제
                DeleteDB ddb = new DeleteDB();
                ddb.execute();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    //로그인 한 사용자의 모든 게시글을 불러옴
    public class DeleteFindDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "phone_num=" + phone_num + ""; //"phone_num=" + id + ""; 추가
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/notice_board_find.php"); //이거 바꿔라!
                //URL url = new URL("http://172.30.1.19:9090/noticeboard_find/");
                URL url = new URL(getString(R.string.ip) + getString(R.string.delete_addr1));
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
            try{
                contents.clear();
                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    //여기서 필요한 부분만 가져오고 조건식 입력
                    //img = content.getString(("img"));
                    //bmap = StringToBitMap(content.getString("image"));
                    no = content.getString("no");
                    notice_board = content.getString("title");
                    party = content.getString("partyno");
                    count = content.getString("partycount");
                    contents.add(new CustomWord(no, notice_board, party, count));
                    nos.add(no.toString());
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            contents_listview.setAdapter(contents_adapter);
            //contents_adapter.notifyDataSetChanged();
        }
    }

    //선택된 게시글을 삭제함
    public class DeleteDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "num=" + no + ""; //"phone_num=" + id + ""; 추가
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/notice_board_delete.php"); //이거 바꿔라!
                //URL url = new URL("http://172.30.1.19:9090/noticeboard_delete/");
                URL url = new URL(getString(R.string.ip) + getString(R.string.delete_addr2));
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
            try{
                if(data.equals("delete")) {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void onBackPressed() {
        Intent deleteintent = new Intent();
        deleteintent.putExtra("1", "result");
        setResult(RESULT_OK, deleteintent);
        finish();
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
