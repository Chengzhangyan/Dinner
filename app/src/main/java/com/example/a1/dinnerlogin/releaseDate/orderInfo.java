package com.example.a1.dinnerlogin.releaseDate;

/**
 * Created by zhanglan on 2017/5/9.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.a1.dinnerlogin.applyDate.applyOrder;
import com.example.a1.dinnerlogin.R;

import com.example.a1.dinnerlogin.releaseDate.homePage;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.a1.dinnerlogin.menu;

public class orderInfo extends Activity {

    //获得OrderId
 List<Map<String,Object>>datalist=new ArrayList<Map<String,Object>>();

    public String OrderId;
    List<String> getDatalist = new ArrayList<String>();
    private Button mBack;
    private Button mJoin;

    Handler handler4=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
for (int i=0;i<11;i++) {
    Map<String, Object> map = new HashMap<String, Object>();/*把数据放入item*/
    map.put(getDatalist.get(i),"info");
    datalist.add(map);/*map内的数据放入一个数组list，第一个map存入的位置是datalist.get（0）*/
}
            OrderId = b.getString("orderId");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        infoThread myThread=new infoThread(homePage.ord.getOrderd());
        new Thread(myThread).start();

        mBack=(Button) findViewById(R.id.back);
        mJoin=(Button) findViewById(R.id.join);

        ListView lv=(ListView) findViewById(R.id.order_lv);
        lv.setAdapter(new SimpleAdapter(this,datalist,R.layout.order_info_item,new String[]{"eatingTime"},new int[]{R.id.info_detail}));

        mBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent=new Intent(orderInfo.this,menu.class);
                intent.putExtra("object",0);
                startActivity(intent);
            }
        });
        mJoin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //
                Intent addintent=new Intent(orderInfo.this,applyOrder.class);
                startActivity(addintent);
            }
        });




    } class infoThread implements Runnable {
        private String orderId;

        public infoThread(String orderId){
            this.orderId=orderId;
        }

        @Override
        public void run(){
            String url=getString(R.string.server_ip) + "ShowOrderInfo";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=null;
            httpPost=new HttpPost(url);

            List<NameValuePair> formparams=new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("orderId",orderId));
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
                        b4.putString("orderId",jsondata4.getString("orderId"));
//                        getDatalist.add(jsondata4.getString("nickname"));
                        getDatalist.add(jsondata4.getString("eatingTime"));
                        getDatalist.add(jsondata4.getString("restaurant"));
                        getDatalist.add(jsondata4.getString("minnumber"));
                        getDatalist.add(jsondata4.getString("maxnumber"));
                        getDatalist.add(jsondata4.getString("city"));
                        getDatalist.add(jsondata4.getString("address"));
                        getDatalist.add(jsondata4.getString("style"));
                        getDatalist.add(jsondata4.getString("telephone"));
                        getDatalist.add(jsondata4.getString("situation"));
                        getDatalist.add(jsondata4.getString("allNickname"));

/*                        b4.putString("orderId",jsondata4.getString("orderId"));
                        b4.putString("1",jsondata4.getString("nickname"));
                        b4.putString("2",jsondata4.getString("eatingTime"));
                        b4.putString("3",jsondata4.getString("restaurant"));
                        b4.putString("4",jsondata4.getString("minnumber"));
                        b4.putString("5",jsondata4.getString("maxnumber"));
                        b4.putString("6",jsondata4.getString("city"));
                        b4.putString("7",jsondata4.getString("address"));
                        b4.putString("8",jsondata4.getString("style"));
                        b4.putString("9",jsondata4.getString("telephone"));
                        b4.putString("10",jsondata4.getString("situation"));*/


                        Message msg4 = new Message();
                        msg4.setData(b4);
                        orderInfo.this.handler4.sendMessage(msg4);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
