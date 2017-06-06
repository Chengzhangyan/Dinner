package com.example.a1.dinnerlogin;

import android.app.ActivityGroup;

/**
 * Created by user on 2017/5/11.
 */

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;

import java.util.HashMap;
import android.widget.SimpleAdapter;

import com.example.a1.dinnerlogin.query.query;
import com.example.a1.dinnerlogin.userInfo.userInfoEdit;

import java.util.ArrayList;
import java.util.List;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.example.a1.dinnerlogin.releaseDate.*;
import com.example.a1.dinnerlogin.applyDate.*;
import com.example.a1.dinnerlogin.login.*;
import com.example.a1.dinnerlogin.userInfo.*;
import com.example.a1.dinnerlogin.order.*;
import com.example.a1.dinnerlogin.query.*;

import android.support.v7.widget.ActionMenuView;

public class menu extends ActivityGroup {

    private int selected = 0;
    public LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

    container = (LinearLayout)findViewById(R.id.center);
    ArrayList<HashMap<String, Object>> menu_data = new ArrayList<HashMap<String,Object>>();
    int[] images = { R.drawable.btn_home,
            R.drawable.btn_msg,R.drawable.btn_write,R.drawable.btn_search, R.drawable.btn_me,};
    String[] menu_texts = { "主页","消息", "发布", "订单","我" };
        for(int i=0;i<images.length;i++){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("menu_image", images[i]);
        map.put("menu_text", menu_texts[i]);
        menu_data.add(map);
    }

    GridView gv = (GridView)findViewById(R.id.grid_view);
    SimpleAdapter adapter = new SimpleAdapter(this, menu_data,
            R.layout.menu_item, new String[] { "menu_image", "menu_text" },
            new int[] { R.id.image, R.id.text });
        gv.setAdapter(adapter);
       Intent object = getIntent();
        Bundle b = object.getExtras();
  if (b!=null&&b.containsKey("object")){
      selected =  b.getInt("object");
  }


    switchActivity(selected);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selected == position){
                    return;}
                selected = position;
                switchActivity(selected);
            }
        });
    }



        public void switchActivity(int selected){
            container.removeAllViews();
            Intent intent = null;
            String tag = "";
            if(selected==0){
                  intent = new Intent(menu.this, homePage.class);
                tag = "tabActivity01";
            }else if(selected==1){
                  intent = new Intent(menu.this, msgMenu.class);

                tag = "tabActivity02";
            }else if(selected==2){
                  intent = new Intent(menu.this,releaseDate.class);
            }else if(selected==3){
                  // intent = new Intent(menu.this, query.class);
                intent = new Intent(menu.this,orderList.class);
                tag = "tabActivity04";
            }else if(selected==4){
                   intent = new Intent(menu.this, userInfoEdit.class);
                tag = "tabActivity05";


            }else if(selected==5){
                intent = new Intent(menu.this, query.class);
                tag = "tabActivity05";


            }else if(selected==6){
                intent = new Intent(menu.this, queryresultActivity.class);
                tag = "tabActivity05";


            }
            Window subActivity = getLocalActivityManager().startActivity(tag,intent);
            //容器添加View
            container.addView(subActivity.getDecorView(),
                    ActionMenuView.LayoutParams.MATCH_PARENT, ActionMenuView.LayoutParams.MATCH_PARENT);

        }




}
