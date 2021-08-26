package com.example.capstone.map;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstone.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

//구글 지도를 xml로 커스텀하는 부분.
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    //Recommand 클래스를 context라고 지정
    private Recommend context;

    //TextView, ImageView 이름을 지정
    private TextView placeTitle;
    private TextView placeInfo;
    private TextView preferenceRatio;
    private ImageView placeImage;

    //생성자로 초기화 하는 부분
    public CustomInfoWindowAdapter(Recommend context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    //xml id와 일치하는 TextView, ImageView를 맞춤
    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);

        placeTitle = (TextView) view.findViewById(R.id.tv_title);
        placeInfo = (TextView) view.findViewById(R.id.tv_subtitle);
        placeImage = (ImageView) view.findViewById(R.id.imageView1) ;
        //preferenceRatio = (TextView) view.findViewById(R.id.tv_sunho);

        //LatLng은 지도좌표에 사용되는 타입(int, String과 같은)
        LatLng haeundae = new LatLng(35.158065, 129.160727);    //해운대 해수욕장
        LatLng busanfilmexperience = new LatLng(35.101667, 129.033787); //부산 영화체험 박물관
        LatLng gwanganli = new LatLng(35.153040, 129.118603); //광안리 해수욕장
        LatLng gamcheon = new LatLng(35.153040, 129.010592); //감천 문화마을
        LatLng fourtysteps = new LatLng(35.104863, 129.034844); //40계단 문화관광테마거리
        LatLng huinyeoul = new LatLng(35.078280, 129.045331); //흰여울 문화마을
        LatLng dadaepo = new LatLng(35.046908, 128.966439); //다대포 해수욕장
        LatLng nationalmaritime = new LatLng(35.078443, 129.080377); //국립 해양박물관
        LatLng amisan = new LatLng(35.052593, 128.960761); //아미산 전망대
        LatLng amnam = new LatLng(35.063311, 129.019297); //암남 공원

        if (marker.getPosition().equals(haeundae)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet()); //snippet에서 선호율을 같이 처리함
            placeImage.setImageResource(R.drawable.pic1);
            //직관성용 공백
        } else if (marker.getPosition().equals(busanfilmexperience)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet());
            placeImage.setImageResource(R.drawable.pic2);

        } else if (marker.getPosition().equals(gwanganli)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet());
            placeImage.setImageResource(R.drawable.pic3);

        } else if (marker.getPosition().equals(gamcheon)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet());
            placeImage.setImageResource(R.drawable.pic4);

        } else if (marker.getPosition().equals(fourtysteps)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet());
            placeImage.setImageResource(R.drawable.pic5);

        }else if (marker.getPosition().equals(huinyeoul)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet());
            placeImage.setImageResource(R.drawable.pic6);

        }else if (marker.getPosition().equals(dadaepo)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet());
            placeImage.setImageResource(R.drawable.pic7);

        }else if (marker.getPosition().equals(nationalmaritime)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet());
            placeImage.setImageResource(R.drawable.pic8);

        }else if (marker.getPosition().equals(amisan)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet());
            placeImage.setImageResource(R.drawable.pic9);

        }else if (marker.getPosition().equals(amnam)) {
            placeTitle.setText(marker.getTitle());
            placeInfo.setText(marker.getSnippet());
            placeImage.setImageResource(R.drawable.pic10);

        }
        return view;
    }
}
