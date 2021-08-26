package com.example.capstone.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.capstone.R;
import com.example.capstone.qr.Tourpass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//QnA로 만들었으나 실제로는 설정부분
public class Qna extends Fragment {
    View view;
    Button buy_button;
    Button setting_button;

    String id = "";
    String tp = "";

    Intent gintent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.qna, container, false);

        gintent = getActivity().getIntent();
        id = gintent.getExtras().getString("phone_num");

        //프래그먼트끼리 값 전달을 활용해서 해결할 것
        buy_button = (Button) view.findViewById(R.id.buy_tourpass);
        setting_button = (Button) view.findViewById(R.id.setting);

        //투어패스 구매 버튼 클릭 시 알림 띄우기
        buy_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                show();
            }
        });

        //지역, 일정 설정 버튼
        setting_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), Area.class);
                intent.putExtra("phone_num", id);
                startActivity(intent);
            }
        });
        return view;
    }

    //알림 설정
    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("투어패스 구매");
        builder.setMessage("투어패스를 구매하시겠습니까?");
        builder.setPositiveButton("구매", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tp = "TRUE";
                TourpassDB TDB = new TourpassDB();
                TDB.execute();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public class TourpassDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "phone_text=" + id + "&tourpass_text=" + tp + "";
            Log.e("POST",param);
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/TourpassSetting.php"); //이거 바꿔라
                //URL url = new URL("http://172.30.1.19:9090/tourpass_setting/");
                URL url = new URL(getString(R.string.ip) + getString(R.string.qna_addr));
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

        //프래그먼트, 알림 팝업으로 처리하였으므로 Bundle을 사용하여 프래그먼트끼리의 값 전달로 처리
        @Override
        protected void onPostExecute(String data) {
            try{
                if(data.equals("buy")){
                    Fragment fragment = new Tourpass();
                    Bundle bundle = new Bundle();
                    bundle.putString("param 1", "buy");
                    Toast.makeText(getActivity(), "투어패스 구매가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    fragment.setArguments(bundle);
                } else {
                    Toast.makeText(getActivity(), "투어패스 구매에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //프래그먼트 갱신 시 필요한 부분에 호출하여 사용
    private void refresh(){
        FragmentTransaction transcation = getFragmentManager().beginTransaction();
        transcation.detach(this).attach(this).commit();
    }
}
