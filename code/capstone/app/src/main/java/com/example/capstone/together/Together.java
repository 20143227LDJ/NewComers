package com.example.capstone.together;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.capstone.together.listview.CustomWord;
import com.example.capstone.R;

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
import java.util.ArrayList;

//다모임 게시판
public class Together extends Fragment {
    View view;

    String notice_board_title = "";
    String notice_board_contents = "";
    String notice_board_party = "";
    String notice_board_partycount = "";
    //String id = "";
    String area_t = "";
    String time_t = "";
    String sex_t = "";
    //String contents_title = "";
    String contents_text = "";
    String authority = "";

    String no_main = "";
    String phone_main = "";
    String name_main = "";
    String email_main = "";
    String sex_main = "";
    String contents_title_main = "";
    String contents_main = "";
    String area_main = "";
    String time_main = "";

    //ArrayList<String> contents = new ArrayList<String>();
    ArrayList<CustomWord> contents = new ArrayList<CustomWord>();
    ListView contents_listview;
    //ArrayAdapter contents_adapter;
    TogetherAdapter t_adapter;

    Intent gintent;
    String gphonenum = "";
    int result = 0;

    TextView write_button, delete_button;

    TextView tv;
    //Intent gintent = getActivity().getIntent();
    //String gid = gintent.getExtras().getString("phone_num");

    //변화되는 내용을 즉시 반영하기 위함
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == result) {
            refresh();
        } else {
            
        }
    }

    /*
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.together, container, false);

        gintent = getActivity().getIntent();
        gphonenum = gintent.getExtras().getString("phone_num");

        //Button write_button = (Button) view.findViewById(R.id.Write);
        //Button delete_button = (Button) view.findViewById(R.id.Delete);
        write_button = (TextView) view.findViewById(R.id.Write);
        delete_button = (TextView) view.findViewById(R.id.Delete);

        //contents_adapter = new ArrayAdapter(getActivity(), R.layout.simpleitem, contents);
        //contents_adapter = new ArrayAdapter(getActivity(), R.layout.custom_layout, contents);
        t_adapter = new TogetherAdapter(getActivity(), R.layout.together_simpleitem, contents);
        contents_listview = (ListView) view.findViewById(R.id.together_listview);

        CheckDB chkDB = new CheckDB();
        chkDB.execute();

        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(authority == null || authority.equals("")) {
                    Toast.makeText(getActivity(), "권한이 없습니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent writeintent = new Intent(getActivity(), WriteContents.class);
                    writeintent.putExtra("phone_num", gphonenum);
                    //startActivity(writeintent);
                    startActivityForResult(writeintent, result);
                    getActivity().overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity);
                }
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent deleteintent = new Intent(getActivity(), DeleteContents.class);
                deleteintent.putExtra("phone_num", gphonenum);
                startActivityForResult(deleteintent, result);
                getActivity().overridePendingTransition(R.anim.rightin_activity, R.anim.leftout_activity);
            }
        });

        return view;
    }

    public class CheckDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "chkphonenum=" + gphonenum + "";
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/notice_board_check.php");
                //URL url = new URL("http://172.30.1.19:9090/noticeboard_check/");
                URL url = new URL(getString(R.string.ip) + getString(R.string.together_addr1));
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
                //지역 설정 값이 없을 경우.
                if (data.equals("{\"results\":null}")) {
                    contents_text = "다모임 설정 값이 없습니다.";
                    TextView tv = (TextView) view.findViewById(R.id.together_text);
                    tv.setText(contents_text);
                } else {
                    contents.clear();
                    JSONObject root = new JSONObject(data);
                    JSONArray results = new JSONArray(root.getString("results"));
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject content = results.getJSONObject(i);
                        //여기서 필요한 부분만 가져오고 조건식 입력
                        area_t = content.getString("area");
                        time_t = content.getString("time_t");
                        sex_t = content.getString("sex");
                        authority = content.getString("authority");

                        if (authority == null || authority.equals("")) {
                            contents_text = "권한이 없습니다. 관리자에게 문의하세요.";
                            TextView tv = (TextView) view.findViewById(R.id.together_text);
                            tv.setText(contents_text);
                        } else {
                            if (area_t == null || area_t.equals("") || time_t == null || time_t.equals("") || sex_t == null || sex_t.equals("")) {
                                contents_text = "설정창에서 다모임 설정을 지정해주세요.";
                                TextView tv = (TextView) view.findViewById(R.id.together_text);
                                tv.setText(contents_text);
                            } else {
                                //위에서 가져온 area_t, time_t, sex_t, authority를 기반으로 데이터베이스 객체 실행
                                SettingDB setDB = new SettingDB();
                                setDB.execute();
                                //setDB에서 나온 아이템 리스트를 클릭 시 동작
                                contents_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        CustomWord word = (CustomWord) parent.getItemAtPosition(position);
                                        contents_text = word.getTitle();
                                        //contents_text = (String) parent.getItemAtPosition(position);
                                        //선택된 아이템에 대한 내용 출력 데이터베이스 실행
                                        SelectDB selDB = new SelectDB();
                                        selDB.execute();
                                    }
                                });
                            }
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //게시글 리스트 출력
    public class SettingDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "phone_num=" + gphonenum + "&area_text=" + area_t + "&time_text=" + time_t + "&sex_text=" + sex_t + ""; //"phone_num=" + id + ""; 추가
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/notice_board_find.php"); //PHP 코드
                //URL url = new URL("http://172.30.1.19:9090/noticeboard_find/");    //GO 코드
                URL url = new URL(getString(R.string.ip) + getString(R.string.together_addr2));
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
                System.out.println("test :" + data);
                contents.clear();

                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++) {
                    JSONObject content = results.getJSONObject(i);
                    /*
                    여기서 필요한 부분만 가져오고 조건식 입력
                    게시글의 제목만 가지고 와서 보여줌
                     */
                    //notice_board_image = content.getString("image");
                    //getBlob = StringToBitMap(content.getString("image"));
                    notice_board_title = content.getString("title");
                    notice_board_contents = content.getString("content");
                    notice_board_party = content.getString("partyno");
                    notice_board_partycount = content.getString("partycount");
                    contents.add(new CustomWord( notice_board_title, notice_board_contents, notice_board_party, notice_board_partycount));
                }

            } catch (Exception e){
                e.printStackTrace();
            }
            //contents_listview.setAdapter(contents_adapter);
            contents_listview.setAdapter(t_adapter);
            //contents_adapter.notifyDataSetChanged();
        }
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

    //출력된 제목 중 하나가 선택되었을 때 실행
    public class SelectDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "u_contents_title=" + contents_text + "";
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/notice_board_contents.php");
                //URL url = new URL("http://172.30.1.19:9090/noticeboard_contents/");
                URL url = new URL(getString(R.string.ip) + getString(R.string.together_addr3));
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
                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    //나온 정보들을 각 변수에 저장
                    no_main = content.getString("no");
                    phone_main = content.getString("phone_num");
                    name_main = content.getString("name");
                    email_main = content.getString("email");
                    sex_main = content.getString("sex");
                    contents_title_main = content.getString("title");
                    contents_main = content.getString("content");
                    area_main = content.getString("area");
                    time_main = content.getString("time_t");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            //저장된 정보들을 intent로 전송
            Intent tintent = new Intent(getActivity(), TogetherContents.class);
            tintent.putExtra("user_phone", gphonenum);
            tintent.putExtra("no", no_main);
            tintent.putExtra("phone_num", phone_main);
            tintent.putExtra("name", name_main);
            tintent.putExtra("email", email_main);
            tintent.putExtra("sex", sex_main);
            tintent.putExtra("title", contents_title_main);
            tintent.putExtra("content", contents_main);
            tintent.putExtra("area", area_main);
            tintent.putExtra("time_t", time_main);
            startActivity(tintent);
            //startActivityForResult(tintent, result);
        }
    }

    //프래그먼트 갱신 시 필요한 부분에 호출하여 사용
    private void refresh(){
        FragmentTransaction transcation = getFragmentManager().beginTransaction();
        transcation.detach(this).attach(this).commit();
    }
}
