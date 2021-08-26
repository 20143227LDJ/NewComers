package com.example.capstone.map;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//구글 지도를 설정하는 부분. 디자인(html에서의 div, 컨테이너와 같은 부분)을 설정하는 곳은 CustomInfoWindowAdapter.Class 임!!
public class Recommend extends Fragment implements OnMapReadyCallback {
    CustomInfoWindowAdapter adapter;

    View view;
    TextView recommend_text;
    private MapView mapView = null;
    private GoogleMap mMap = null;
    private Button btn;

    /*
    Java는 동적배열 생성이 안되므로 정적배열로 여유있게 100개의 배열을 생성함
    동적배열 생성 방법이 있긴 하지만 복잡해서 일단은 정적배열로 생성
     */
    String[] title = new String[100];
    String[] contents = new String[100];
    String[] image = new String[100];
    String[] preferenceratio = new String[100];

    public Recommend()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //뷰가 처음 생성될 때
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.recommend, container, false);
        mapView = (MapView)view.findViewById(R.id.map);
        //recommend_text = (TextView) view.findViewById(R.id.recommend_tv);

        //지역정보를 데이터베이스에 접속하여 가져옴
        AreaInfoDB aidb = new AreaInfoDB();
        aidb.execute();

        //도움말 팝업
        btn = view.findViewById(R.id.btn);
        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(v.getContext());

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.recommend_popup_layout);
                dialog.show();

                final Button cancel = (Button) dialog.findViewById(R.id.confirm);

                // 확인 버튼
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });
            }
        });

        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체 불러옴
        mMap = googleMap;

        Bundle bundle = getArguments();
        double latitude = bundle.getDouble("latitude", 1);
        double longitude = bundle.getDouble("longitude", 1);

        Toast.makeText(getContext(), "현재 위치 \n위도 " + latitude + "\n경도" + longitude, Toast.LENGTH_SHORT).show();


        //마커를 어레이 리스트로 생성하여 계속해서 추가 가능
        List<MarkerItem> markerItemList = new ArrayList<MarkerItem>();

        //뷰가 처음 생성될 때 실행한 데이터베이스의 정보를 가지고 어레이 리스트에 추가
        markerItemList.add(new MarkerItem(35.158065, 129.160727, title[0], contents[0], preferenceratio[0]));   //해운대
        markerItemList.add(new MarkerItem(35.101667, 129.033787, title[1], contents[1], preferenceratio[1]));   //부산 영화체험 박물관
        markerItemList.add(new MarkerItem(35.153040, 129.118603, title[2], contents[2], preferenceratio[2]));   //광안리 해수욕장
        markerItemList.add(new MarkerItem(35.153040, 129.010592, title[3], contents[3], preferenceratio[3]));   //감천 문화마을
        markerItemList.add(new MarkerItem(35.104863, 129.034844, title[4], contents[4], preferenceratio[4]));   //40계단 문화관광테마거리
        markerItemList.add(new MarkerItem(35.078280, 129.045331, title[5], contents[5], preferenceratio[5]));   //흰여울 문화마을
        markerItemList.add(new MarkerItem(35.046908, 128.966439, title[6], contents[6], preferenceratio[6]));   //다대포 해수욕장
        markerItemList.add(new MarkerItem(35.078443, 129.080377, title[7], contents[7], preferenceratio[7]));   //국립 해양박물관
        markerItemList.add(new MarkerItem(35.052593, 128.960761, title[8], contents[8], preferenceratio[8]));   //아미산 전망대
        markerItemList.add(new MarkerItem(35.063311, 129.019297, title[9], contents[9], preferenceratio[9]));   //암남 공원
        markerItemList.add(new MarkerItem(latitude, longitude, "현재 위치", "테스트", "테스트"));   //현재 위치

        //마커를 생성
        for(int i = 0; i < 11; i++) {
            LatLng location = new LatLng(markerItemList.get(i).getLat(), markerItemList.get(i).getLon());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(markerItemList.get(i).getPlaceTitle());
            markerOptions.snippet(markerItemList.get(i).getPlaceInfo() + "\n\n" + "선호율 : " + markerItemList.get(i).getPreferenceRatio() + "%");
            if(i==0 || i==2 || i==6) markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)); // 바닷가(파란색)
            if(i==1 || i==3 || i==4 || i==5 || i==7) markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)); // 문화, 역사(빨간색)
            if(i==8 || i==9) markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)); // 산책로(초록색)
            if(i==10) markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.current_lotation)));
            //markerOptions.snippet("선호율 : " + String.valueOf(markerItemList.get(i).getPreferenceRatio()) + "%");

            /*
            int drawableId = getResources().getIdentifier("pic" + (i + 1), "drawable", "com.example.capstone");

            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(drawableId);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 150, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
             */

            mMap.addMarker(markerOptions);
        }
        adapter = new CustomInfoWindowAdapter(this);
        mMap.setInfoWindowAdapter(adapter);

        //기본 좌표 및 카메라 줌
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(35.078280, 129.045331)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    //지역정보를 가져오기 위한 데이터베이스
    private class AreaInfoDB extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                // 서버연결
                //URL url = new URL("http://192.168.0.53/capstone/Signup.php");
                //URL url = new URL("http:/172.30.1.19:9090/area_info/");
                URL url = new URL(getString(R.string.ip) + getString(R.string.recommand_addr));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

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

        //전역변수로 선언한 String 배열에 삽입
        @Override
        protected void onPostExecute(String data) {
            try {
                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++) {
                    JSONObject content = results.getJSONObject(i);
                    title[i] = content.getString("title");
                    contents[i] = content.getString("content");
                    image[i] = content.getString("image");
                    preferenceratio[i] = content.getString("preference");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
        }
    }
}
