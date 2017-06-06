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
 //List<Map<String,Object>>datalist=new ArrayList<Map<String,Object>>();

   //public String OrderId;

    private Button mBack;
    private Button mJoin;
    private TextView mNickName;
    private TextView mEatingTime;
    private TextView mCanteen;
    private TextView mMinnumber;
    private TextView mMaxnumber;
    private TextView mCity;
    private TextView mAddress;
    private TextView mStyle;
    private TextView mTel;
    private TextView mSit;

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Bundle b5=msg.getData();

            inputData(b5.getString("eatingTime"),b5.getString("restaurant"),b5.getString("minnumber"),
                    b5.getString("maxnumber"), b5.getString("city"),b5.getString("address"),b5.getString("style"),b5.getString("telephone"),
                    b5.getString("situation") );

            //OrderId = b.getString("orderId");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);
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
System.out.println("order id is "+homePage.ord.getOrderid());



        infoThread myThread=new infoThread(homePage.ord.getOrderid());
       // od.setOrderid(homePage.ord.getOrderid());
        new Thread(myThread).start();

        mBack=(Button) findViewById(R.id.back);
        mJoin=(Button) findViewById(R.id.join);
        mNickName = (TextView)findViewById(R.id.order_owner);
        mEatingTime = (TextView)findViewById(R.id.eating_time);
        mCanteen  = (TextView)findViewById(R.id.canteen);
        mMinnumber = (TextView)findViewById(R.id.minnumber);
        mMaxnumber = (TextView)findViewById(R.id.maxnumber);
        mCity = (TextView)findViewById(R.id.city);
        mAddress = (TextView)findViewById(R.id.address);
        mStyle = (TextView)findViewById(R.id.style);
        mTel = (TextView)findViewById(R.id.telephone);
        mSit = (TextView)findViewById(R.id.situation);
      //  inputData("John",homePage.ord.getOrderid(),"Canteen","minnumber","maxnumber","city","address","style","tel","situation");
        mBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent=new Intent(orderInfo.this,menu.class);
                intent.putExtra("object",0);
                startActivity(intent);
            }
        });
        mJoin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent addintent=new Intent(orderInfo.this,applyOrder.class);
                startActivity(addintent);
            }
        });

    }

    private void inputData(String eatingTime,String restaurant,String minnumber,String maxnumber,String city,String address,String style,String telephone,String situation){
       // mNickName.setText(nickname);String nickname,
        mEatingTime.setText(eatingTime);
        mCanteen.setText(restaurant);
        mMinnumber.setText(minnumber);
        mMaxnumber.setText(maxnumber);
        mCity.setText(city);
        mAddress.setText(address);
        mStyle.setText(style);
        mTel.setText(telephone);
        mSit.setText(situation);
    }



    class infoThread implements Runnable {
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
                       // b4.putString("nickname",jsondata4.getString("nickname"));//new
                        b4.putString("orderTime",jsondata4.getString("orderTime"));
                        b4.putString("eatingTime",jsondata4.getString("eatingTime"));
                        b4.putString("restaurant",jsondata4.getString("restaurant"));
                        b4.putString("minnumber",jsondata4.getString("minnumber"));
                        b4.putString("maxnumber",jsondata4.getString("maxnumber"));
                        b4.putString("city",jsondata4.getString("city"));
                        b4.putString("address",jsondata4.getString("address"));
                        b4.putString("style",jsondata4.getString("style"));
                        b4.putString("telephone",jsondata4.getString("telephone"));
                        b4.putString("situation",jsondata4.getString("situation"));

                        Message msg4 = new Message();
                        msg4.setData(b4);
                        orderInfo.this.handler.sendMessage(msg4);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
