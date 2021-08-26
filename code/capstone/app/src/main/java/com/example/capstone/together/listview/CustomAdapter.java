package com.example.capstone.together.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capstone.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    ArrayList<CustomItem> listViewItemList = new ArrayList<>();

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public CustomItem getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context mContext = viewGroup.getContext();
        //추가한 xml에서 위젯들을 연결해주는 역할을 하는 메소드
        ViewHolder holder;

        if(view == null){
            holder = new ViewHolder();

            //레이아웃 위에 신규 레이아웃을 덮어씌우는 서비스
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_layout, null);

            //아래 코드에는 위에서 생성한 xml의 위젯을 findviewById로 연결해줌
            holder.custom_view1 = view.findViewById(R.id.sex);

            //마지막으로 각 뷰에 태그를 부여해줌. 각 리스트 뷰 아이템의 변별력을 위해서
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }

        //커스텀한 xml에 Parse 한 데이터베이스 정보들을 설정해주는 코드를 작성함
        CustomItem mData = getItem(i);
        //위젯에 각 아이템에 맞는 데이터를 설정
        holder.custom_view1.setText(mData.getAreaname());

        return view;
    }

    class ViewHolder{
        public TextView custom_view1;
    }
    public void AddItem(String _areaname){
        //Parse 후 전달받은 데이터들을 CustomItem 객체로 만들어주고 ArrayList에 추가해주는 코드
        CustomItem ci = new CustomItem(_areaname);
        listViewItemList.add(ci);
    }
}
