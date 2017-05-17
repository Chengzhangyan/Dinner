package com.example.a1.dinnerlogin.releaseDate;

/**
 * Created by zhanglan on 2017/5/9.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Map;
import java.util.HashMap;
import android.widget.SimpleAdapter;
import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.userInfo.userInfoEdit;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.example.a1.dinnerlogin.releaseDate.*;
import com.example.a1.dinnerlogin.applyDate.*;
import com.example.a1.dinnerlogin.login.*;
import com.example.a1.dinnerlogin.userInfo.*;
import android.app.ActivityGroup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.widget.Toast;
//import com.example.a1.dinnerlogin.bitmap;

/*
 * Created by bj on 2017/4/11.
 */

public class homePage extends Activity {
    private boolean is_divPage;//是否进行分页

    private int[] imgIds = new int[]{R.mipmap.head_05, R.mipmap.head_06,
            R.mipmap.head_07,R.mipmap.head_05, R.mipmap.head_06, R.mipmap.head_07};
    List<Map<String,Object>>datalist=new ArrayList<Map<String,Object>>();

//bitmap bmp = new bitmap();
    List<String> list = new ArrayList<>();/*list存储获得的orderid*/
    public static orderId ord =new orderId();
    public int count = 1;
    Handler handler4=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
for (int i=1;i<7;i++){
  //  bitmap bm = new bitmap();
            Map<String,Object> map = new HashMap<String,Object>();/*把数据放入item*/
            map.put("order_owner",b.getString("nickname"+i));/*把获得的信息放入map*/
            map.put("eating_time",b.getString("eatingTime"+i));
            map.put("curr_number",b.getString("currnumber"+i));
            map.put("restaurant",b.getString("restaurant"+i));
    System.out.println(b + "count "+ count);
        //    map.put("head_icon",bmp.base64ToBitmap(b.getString("photo"+i)));
            map.put("head_icon",imgIds[count]);
            datalist.add(map);/*map内的数据放入一个数组list，第一个map存入的位置是datalist.get（0）*/
            list.add(b.getString("orderId"+i));
}
            count++;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());



        ListView lv=(ListView) findViewById(R.id.home_lv);

        homePageThread myThread = new homePageThread(count);
        new Thread(myThread).start();
//testhome();

        final SimpleAdapter adpt =  new SimpleAdapter(this,datalist,R.layout.item_item,new String[]{"head_icon","order_owner","eating_time",
                "curr_number","restaurant",},new int[]{R.id.head_icon,R.id.order_owner,R.id.eating_time,R.id.curr_number,R.id.restaurant,
        });
        /*为listview设置适配器*/
        lv.setAdapter(adpt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*点击事件*/
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_to_orderinfo = new Intent(homePage.this,orderInfo.class);
                switch (position){
                    case 0:
                        ord.setOrderid(list.get(0));/*把第一个item的orderid存储*/
                        startActivity(intent_to_orderinfo);
                        finish();
                        break;
                    case 1:
                        ord.setOrderid(list.get(1));/*把item的orderid存储*/
                        startActivity(intent_to_orderinfo);
                        finish();
                        break;
                    case 2:
                        ord.setOrderid(list.get(2));
                        startActivity(intent_to_orderinfo);
                        finish();
                        break;
                    case 3:
                        ord.setOrderid(list.get(3));
                        startActivity(intent_to_orderinfo);
                        finish();
                        break;
                    case 4:
                        ord.setOrderid(list.get(4));
                        startActivity(intent_to_orderinfo);
                        finish();
                        break;
                    case 5:
                        ord.setOrderid(list.get(5));
                        startActivity(intent_to_orderinfo);
                        finish();
                        break;
                    case 6:
                        ord.setOrderid(list.get(6));
                        startActivity(intent_to_orderinfo);
                        finish();
                        break;
            }}
        });

        lv.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE){
                    Toast.makeText(homePage.this, "正在获取数据......", Toast.LENGTH_SHORT).show();
                    homePageThread myThread = new homePageThread(count);
                    new Thread(myThread).start();
                    adpt.notifyDataSetChanged();
                    //testhome();
                }else if (!is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE){
                    Toast.makeText(homePage.this, "meila......", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
              is_divPage = (firstVisibleItem + visibleItemCount== totalItemCount);
            }
        });




    }


public void testhome(){
    for(int i =0;i<6;i++) {
        Map<String, Object> map = new HashMap<String, Object>();/*把数据放入item*/
        map.put("order_owner", i);/*把获得的信息放入map*/
        map.put("order_time", "3:00");
        map.put("curr_number", "3");
        map.put("order_number", "3");
        map.put("restaurant", "anteeeee");
        map.put("head_icon", imgIds[i]);
        list.add("1314234235");
        datalist.add(map);
    }
}

    class homePageThread implements Runnable {

       private String page ;

        public homePageThread(int page){
            this.page = String.valueOf(page);
        }

        @Override
        public void run(){
            String url=getString(R.string.server_ip) + "showOrderList";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=null;
            httpPost=new HttpPost(url);

            List<NameValuePair> formparams=new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("page",page));
            UrlEncodedFormEntity uefEntity;

            try{
                uefEntity=new UrlEncodedFormEntity(formparams,"UTF-8");
                httpPost.setEntity(uefEntity);
                HttpResponse response;
                response=httpClient.execute(httpPost);
                if(response.getStatusLine().getStatusCode()==200){
                    HttpEntity entity=response.getEntity();
                    if(entity!=null){
                        String json= EntityUtils.toString(entity,"UTF-8");

                        JSONObject jsondata4=new JSONObject(json);
                        Bundle b4=new Bundle();
                        for (int i=1;i<7;i++) {

                            b4.putString("orderId"+i, jsondata4.getString("orderId"+i));
                          //  b4.putString("nickname"+i, jsondata4.getString("nickname"+i));
                            b4.putString("eatingTime"+i, jsondata4.getString("eatingTime"+i));
                            b4.putString("restaurant"+i, jsondata4.getString("restaurant"+i));
                       //     b4.putString("photo"+i, jsondata4.getString("photo"+i));
                            b4.putString("currnumber"+i, jsondata4.getString("number"+i));
//System.out.println();
                        }
                        Message msg4 = new Message();
                        msg4.setData(b4);
                        homePage.this.handler4.sendMessage(msg4);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
