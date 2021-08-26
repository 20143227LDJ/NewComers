package com.example.capstone.api;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class API extends AppCompatActivity {
    Intent intent;

    String title = "";
    String contenttype = "";
    String location = "";
    String area = "";
    String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=RRZuhOlOrDgZ7l6GUJVh3u2PS9sQXljCcjBKi4Uv0cDKBeUw2s9TFscjx6D%2FNlxzkUnc2xW8stIUfqnrK84tPg%3D%3D&MobileApp=AppTest&MobileOS=ETC";
    //String url = getString(R.string.api_url);

    ListView api_contents_listview;
    ApiAdapter api_contents_adapter;
    ArrayList<ApiWord> api_contents = new ArrayList<ApiWord>();

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api);

        api_contents_adapter = new ApiAdapter(this, R.layout.api_simpleitem, api_contents);
        api_contents_listview = (ListView) findViewById(R.id.api_listview);

        Button backbtn = (Button)findViewById(R.id.api_back);

        intent = getIntent();
        area = intent.getStringExtra("area");

        if (area.equals("서울")) {
            url = url + "&areaCode=1";
        } else if (area.equals("대구")) {
            url = url + "&areaCode=4";
        } else if (area.equals("부산")) {
            url = url + "&areaCode=6";
        }

        OpenAPI openapi = new OpenAPI(url);
        openapi.execute();

        backbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

    }

    public class OpenAPI extends AsyncTask<Void, Void, String> {
        private String url;
        public OpenAPI(String url) {
            this.url = url;
        }
        @Override
        protected String doInBackground(Void... params) {

            // parsing할 url 지정(API 키 포함해서)
            try {
                    //StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList"); /*URL*/
                    //urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + "RRZuhOlOrDgZ7l6GUJVh3u2PS9sQXljCcjBKi4Uv0cDKBeUw2s9TFscjx6D%2FNlxzkUnc2xW8stIUfqnrK84tPg%3D%3D"); /*Service Key*/
                //urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));	//현재 페이지 번호
                //urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));	//한 페이지 결과 수
                    //urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("AppTest", "UTF-8"));	//서비스명
                    //urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8"));	//IOS, AND, WIN, ETC
                //urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));	//A=제목순, B=조회순, C=수정일순, D=생성일순
                //urlBuilder.append("&" + URLEncoder.encode("cat1", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));	//대분류 코드
                //urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode("15", "UTF-8"));	//관광타입 ID
                    //urlBuilder.append("&" + URLEncoder.encode("areaCode", "UTF-8") + "=" + URLEncoder.encode("6", "UTF-8"));	//지역 코드
                //urlBuilder.append("&" + URLEncoder.encode("sigunguCode", "UTF-8") + "=" + URLEncoder.encode("4", "UTF-8"));	//시군구 코드
                //urlBuilder.append("&" + URLEncoder.encode("cat2", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));	//중분류 코드
                //urlBuilder.append("&" + URLEncoder.encode("cat3", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));	//소분류 코드
                //urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                //urlBuilder.append("&" + URLEncoder.encode("midifiedtime", "UTF-8") + "=" + URLEncoder.encode("4", "UTF-8"));	//콘텐츠 수정일
                    //URL url = new URL(urlBuilder.toString());

                DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = null;
                try {
                    dBuilder = dbFactoty.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                Document doc = null;
                try {
                    doc = dBuilder.parse(String.valueOf(url));
                } catch (IOException | SAXException e) {
                    e.printStackTrace();
                }

                // root tag
                doc.getDocumentElement().normalize();
                System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); // Root element: result

                // 파싱할 tag
                NodeList nList = doc.getElementsByTagName("item");

                api_contents.clear();
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element eElement = (Element) nNode;
                        //location = getTagValue("addr1", eElement);
                        //contenttype = getTagValue("contenttypeid", eElement);
                        //title = getTagValue("title", eElement);

                        if (getTagValue("title", eElement) != null) {
                            title = getTagValue("title", eElement);
                        } else {
                            title = "제목 없음";
                        }
                        if (getTagValue("contenttypeid", eElement) != null) {
                            contenttype = getTagValue("contenttypeid", eElement);
                        } else {
                            contenttype = "콘텐츠 타입 없음";
                        }
                        if (getTagValue("addr1", eElement) != null) {
                            location = getTagValue("addr1", eElement);
                        } else {
                            location =  "주소 없음";
                        }

                        Log.d("OPEN_API","title  : " + getTagValue("title", eElement));
                        Log.d("OPEN_API","type  : " + getTagValue("contenttypeid", eElement));
                        Log.d("OPEN_API","location : " + getTagValue("addr1", eElement));

                        if (contenttype.equals("12")) {
                            contenttype = "관광지";
                        } else if (contenttype.equals("14")) {
                            contenttype = "문화시설";
                        } else if (contenttype.equals("15")) {
                            contenttype = "축제 / 공연 / 행사";
                        } else if (contenttype.equals("25")) {
                            contenttype = "여행코스";
                        } else if (contenttype.equals("28")) {
                            contenttype = "레포츠";
                        } else if (contenttype.equals("32")) {
                            contenttype = "숙박";
                        } else if (contenttype.equals("38")) {
                            contenttype = "쇼핑";
                        } else if (contenttype.equals("39")) {
                            contenttype = "음식";
                        }

                        api_contents.add(new ApiWord(title, contenttype, location));
                    }    // for end
                }    // if end
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getTagValue(String tag, Element eElement) {
            NodeList nlList;
            Node nValue;
            if (eElement.getElementsByTagName(tag).item(0) == null) {
                return null;
            } else {
                nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
                nValue = (Node) nlList.item(0);
                if (nValue == null) {
                    return null;
                }
            }
            return nValue.getNodeValue();
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            api_contents_listview.setAdapter(api_contents_adapter);
        }
    }
}
