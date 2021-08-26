package com.example.capstone.setting;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.MainPage;
import com.example.capstone.R;
import com.example.capstone.api.API;
import com.example.capstone.api.ApiWord;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Area extends AppCompatActivity {
    //CustomAdapter mAdapter; //안쓰임

    //지역 메뉴를 어레이 리스트로 출력
    ArrayList<String> area_menu;
    ArrayList<String> gu_menu;
    ArrayList<String> time_menu;
    ArrayList<String> sex_menu;
    //ArrayList<String> sex_menu = new ArrayList<String>();

    //리스트 뷰
    ListView area_listview;
    ListView gu_listview;
    ListView time_listview;
    ListView sex_listview;

    //텍스트 뷰
    TextView area_tv;

    //어댑터
    ArrayAdapter area_adapter;
    ArrayAdapter gu_adapter;
    ArrayAdapter time_adapter;
    ArrayAdapter sex_adapter;

    //선택된 지역, 시간, 성별을 저장하는 변수
    String area_text = "";
    String gu_text = "";
    String time_text = "";
    String sex_text = "";

    //파싱된 데이터를 저장하는 변수
    String title = "";
    String contenttype = "";
    String location = "";

    ArrayList<ApiWord> apicontents = new ArrayList<ApiWord>();

    //인텐트로 받은 phone_num
    String id = "";
    String idt = "";

    /*
    지역 설정란 입니다.
    임시로 어레이 리스트에 데이터를 직접 입력하였으며
    데이터베이스에서 가져와서 사용할 수도 있습니다.
    */
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area);

        Intent gintent = getIntent();
        id = gintent.getExtras().getString("phone_num");
        idt = gintent.getExtras().getString("phone_num");

        area_menu = new ArrayList<String>(Arrays.asList("부산", "서울", "강원도", "대구"));
        //gu_menu = new ArrayList<String>(Arrays.asList("사상구", "중구", "해운대구"));
        time_menu = new ArrayList<String>(Arrays.asList("당일", "1박 2일", "2박 3일", "3박 4일", "4일 이상"));
        sex_menu = new ArrayList<String>(Arrays.asList("남", "여"));

        area_adapter = new ArrayAdapter(this, R.layout.area_simpleitem, area_menu);
        //gu_adapter = new ArrayAdapter(this, R.layout.simpleitem, gu_menu);
        time_adapter = new ArrayAdapter(this, R.layout.area_simpleitem, time_menu);
        sex_adapter = new ArrayAdapter(this, R.layout.area_simpleitem, sex_menu);

        area_listview = (ListView) findViewById(R.id.area);
        //gu_listview = (ListView) findViewById(R.id.gu);
        time_listview = (ListView) findViewById(R.id.a_time);
        sex_listview = (ListView) findViewById(R.id.sex);

        Button backbtn = (Button) findViewById(R.id.area_back);

        Button test_button = (Button) findViewById(R.id.test);

        test_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Area.this, API.class);
                intent.putExtra("phone_num", id);
                intent.putExtra("area", area_text);
                startActivity(intent);
            }
        });

        backbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        area_tv = (TextView) findViewById(R.id.area_explanation);
        area_tv.setText("지역을 선택해 주세요.");

        area_listview.setAdapter(area_adapter);



        //지역 리스트 클릭 시 일정 리스트가 보이도록 함
        area_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //gu_text = (String) parent.getItemAtPosition(position);
                area_text = (String) parent.getItemAtPosition(position);
                if (area_text.equals("서울")) {
                    area_tv.setText("서울\n\n대한민국의 수도로서 도시적인 느낌을 받을 수 있습니다.");
                } else if (area_text.equals("부산")) {
                    area_tv.setText("부산\n\n해양도시로서 해운대, 광안리, 다대포 등이 유명하지만 이외에도 감천 문화마을, 영화 박물관 등 그 시대의 풍경을 감상할 수 있습니다.");
                } else if (area_text.equals("대구")) {
                    area_tv.setText("대구\n\n분지 지형의 도시로서 수성못, 김광석 거리 등이 유명합니다.");
                } else if (area_text.equals("강원도")) {
                    area_tv.setText("강원도\n\n산을 끼고 있는 도시로서 도시적인 느낌보다는 자연적이고 광활한 느낌을 받을 수 있습니다.");
                }
                //gu_listview.setAdapter(gu_adapter);
                time_listview.setAdapter(time_adapter);
            }
        });

        /*
        gu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        */

        //일정 리스트 클릭 시 성별 리스트가 보이도록 함
        time_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                time_text = (String) parent.getItemAtPosition(position);
                if (time_text.equals("당일")) {
                    area_tv.setText("당일\n\n주변만 잠시 돌아보거나 가까운 거리일 경우에 적합합니다.");
                } else if (time_text.equals("1박 2일")) {
                    area_tv.setText("1박 2일\n\n여러 군데를 돌아다니지 않을 경우에 적합합니다.");
                } else if (time_text.equals("2박 3일")) {
                    area_tv.setText("2박 3일\n\n여러 군데를 관광하고 싶은 경우에 적합합니다.");
                } else if (time_text.equals("3박 4일")) {
                    area_tv.setText("3박 4일\n\n여러 군데를 자세하게 관광하고 싶은 경우에 적합합니다.");
                } else if (time_text.equals("4일 이상")) {
                    area_tv.setText("4일 이상\n\n관광지 이외에 해당 도시의 교통이나 문화를 즐기고 싶은 경우에 적합합니다.");
                }
                sex_listview.setAdapter(sex_adapter);
            }
        });

        //성별 리스트 클릭 시 setDB를 시작함
        sex_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                sex_text = (String) parent.getItemAtPosition(position);

                SettingDB setDB = new SettingDB();
                setDB.execute();
                /*Intent intent = new Intent(Area.this, MainPage.class);
                area_text = "테스트용";
                intent.putExtra("area_text", area_text);
                //intent.putExtra("gu_text", gu_text);
                intent.putExtra("time_text", time_text);
                intent.putExtra("sex_text", sex_text);
                intent.putExtra("phone_num", id);   //테스트용
                startActivity(intent);*/
            }
        });

    }

    //사용자 설정에 대한 데이터베이스 연결
    public class SettingDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "phone_text=" + id + "&area_text=" + area_text + "&time_text=" + time_text + "&sex_text=" + sex_text + "";
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/AreaSetting.php"); //이거 바꿔라
                //URL url = new URL("http://172.30.1.19:9090/area_setting/");
                URL url = new URL(getString(R.string.ip) + getString(R.string.area_addr));
                //URL url = new URL(getString(R.string.area_addr));
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
                //출력된 지역(토픽)을 영어로 변환하여 구독
                if (area_text.equals("서울")) {
                    area_text = "seoul";
                } else if (area_text.equals("부산")) {
                    area_text = "busan";
                } else if (area_text.equals("대구")) {
                    area_text = "daegu";
                }
                FirebaseMessaging.getInstance().unsubscribeFromTopic(data);
                FirebaseMessaging.getInstance().subscribeToTopic(area_text);
                System.out.println("unsub : " + data);
                System.out.println("sub : " + area_text);
                /*area_menu.clear();

                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    //area_text = content.getString("area_text");
                    //gu_text = content.getString("gu_text");
                    //time_text = content.getString("time_text");
                    //sex_text = content.getString("sex_text");
                    //BUILDING_MENU.add(building_name);
                    //building_adapter.notifyDataSetChanged();
                }*/
            } catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(Area.this, MainPage.class);
            //intent.putExtra("area_text", area_text);
            //intent.putExtra("gu_text", gu_text);
            //intent.putExtra("time_text", time_text);
            //intent.putExtra("sex_text", sex_text);
            intent.putExtra("phone_num", id);   //테스트용
            startActivity(intent);
            //overridePendingTransition(R.anim.alphaout_activity, R.anim.alphain_activity);
        }
    }
}
