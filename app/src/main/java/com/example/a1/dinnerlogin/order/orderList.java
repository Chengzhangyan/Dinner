package com.example.a1.dinnerlogin.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.IntegerRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.SimpleAdapter;
import android.app.ListActivity;
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
import android.widget.Toast;import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.IntegerRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.SimpleAdapter;
import android.app.ListActivity;
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

public class orderList extends Activity {
    private int count = 1;
    private SimpleAdapter adpt;
    private int[] imgIds = new int[]{R.mipmap.head_05, R.mipmap.head_06,
            R.mipmap.head_07,R.mipmap.head_05, R.mipmap.head_06, R.mipmap.head_07,R.mipmap.head_05,R.mipmap.head_05};
    List<Map<String,Object>>datalist=new ArrayList<Map<String,Object>>();
    List<String> list = new ArrayList<>();/*list存储获得的orderid*/
    private boolean is_divPage;//是否进行分页
    public static orderId ord = new orderId();

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
            int listnum = Integer.valueOf(b.getString("listNum"));
            for (int i=1;i<=listnum;i++){
                //  bitmap bm = new bitmap();
                Map<String,Object> map = new HashMap<String,Object>();/*把数据放入item*/
                map.put("orderId",b.getString("orderId"+i));/*把获得的信息放入map*/
                // map.put("nickname",b.getString("nickname"+i));
                map.put("eating_time",b.getString("eatingTime"+i));
                map.put("curr_number",b.getString("currnumber"+i));
                map.put("restaurant",b.getString("restaurant"+i));
                System.out.println(map + "count "+ count);
                //    map.put("head_icon",bmp.base64ToBitmap(b.getString("photo"+i)));
                map.put("head_icon",imgIds[count]);
                datalist.add(map);/*map内的数据放入一个数组list，第一个map存入的位置是datalist.get（0）*/
                System.out.println("datalist is   "+datalist);
                list.add(b.getString("orderId"+i));
//    System.out.println("datalist : "+datalist.get(i).toString());
            }

            count++;
            adpt.notifyDataSetChanged();
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
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

        orderListThread myThread = new orderListThread(count);
        new Thread(myThread).start();
        ListView lv=(ListView) findViewById(R.id.home_lv);
        adpt =  new SimpleAdapter(this,datalist,R.layout.item_item,new String[]{"head_icon","eating_time",
                "curr_number","restaurant",},new int[]{R.id.head_icon,R.id.eating_time,R.id.curr_number,R.id.restaurant,
        });
        lv.setAdapter(adpt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*点击事件*/
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ord.setOrderid(list.get(position));
                Intent intent_to_orderinfo = new Intent(orderList.this,orderInfoForList.class);
                startActivity(intent_to_orderinfo);
                finish();
            }
        });
        lv.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE){
                    Toast.makeText(orderList.this, "正在获取数据......", Toast.LENGTH_SHORT).show();
                    orderListThread myThread = new orderListThread(count);
                    new Thread(myThread).start();


                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount== totalItemCount);
            }
        });

    }


    @Override
    public void onRestart(){
        super.onRestart();
    }






    class orderListThread implements Runnable {

        private String page ;

        public orderListThread(int page){
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
            formparams.add(new BasicNameValuePair("userId",login.u.getUserid()));
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
                        int listnum = Integer.valueOf(jsondata4.getString("listNum"));
                        b4.putString("listNum", jsondata4.getString("listNum"));
                        for (int i=1;i<=listnum;i++) {

                            b4.putString("orderId"+i, jsondata4.getString("orderId"+i));
//                           b4.putString("nickname"+i, jsondata4.getString("nickname"+i));
                            b4.putString("eatingTime"+i, jsondata4.getString("eatingTime"+i));
                            b4.putString("restaurant"+i, jsondata4.getString("restaurant"+i));
                            //     b4.putString("photo"+i, jsondata4.getString("photo"+i));
                            b4.putString("currnumber"+i, jsondata4.getString("number"+i));
//System.out.println();
                        }
                        Message msg4 = new Message();
                        msg4.setData(b4);
                        orderList.this.handler.sendMessage(msg4);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
