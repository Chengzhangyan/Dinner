package com.example.a1.dinnerlogin.applyDate;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.order.orderList;
import com.example.a1.dinnerlogin.releaseDate.homePage;
import com.example.a1.dinnerlogin.releaseDate.releaseDate;
import com.example.a1.dinnerlogin.userInfo.userInfoEdit;

import java.util.ArrayList;
import java.util.HashMap;

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

import android.support.v7.widget.ActionMenuView;

public class msgMenu extends ActivityGroup {
    public static orderId ord =new orderId();
    private int selected = 0;
    public LinearLayout container;
    private GridView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        container = (LinearLayout)findViewById(R.id.center);
        ArrayList<HashMap<String, Object>> menu_data = new ArrayList<HashMap<String,Object>>();

        String[] menu_texts = { "我的申请","待我审批" };
        for(int i=0;i<2;i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("menu_text", menu_texts[i]);
            menu_data.add(map);
        }

         gv = (GridView)findViewById(R.id.grid_view);
        SimpleAdapter adapter = new SimpleAdapter(this, menu_data,
                R.layout.msg_menu_item, new String[] {  "menu_text" },
                new int[] { R.id.text });
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
            gv.setBackgroundColor(Color.WHITE);
            intent = new Intent(msgMenu.this, applyMsg.class);
            tag = "tabActivity01";

        }else if(selected==1){
            gv.setBackgroundColor(Color.GREEN);
            intent = new Intent(msgMenu.this, checkMsg.class);

            tag = "tabActivity02";
        }
        Window subActivity = getLocalActivityManager().startActivity(tag,intent);
        //容器添加View
        container.addView(subActivity.getDecorView(),
                ActionMenuView.LayoutParams.MATCH_PARENT, ActionMenuView.LayoutParams.MATCH_PARENT);

    }




}
