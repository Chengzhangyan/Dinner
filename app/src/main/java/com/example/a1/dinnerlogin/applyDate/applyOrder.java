package com.example.a1.dinnerlogin.applyDate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.login.login;

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
import com.example.a1.dinnerlogin.releaseDate.orderInfo;
import com.example.a1.dinnerlogin.menu;
import com.example.a1.dinnerlogin.releaseDate.homePage;

/**
 * Created by 1 on 2017/5/4.
 */

public class applyOrder extends Activity {

    long CurTime = System.currentTimeMillis();
    final String ApplyTimeValue = String.valueOf(CurTime);
  private   EditText InputText;
  private   Button btnApply;
    private Button mBack;
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
            if(b.getString("flag").equals("1")){
                Toast.makeText(applyOrder.this,"发送申请成功",Toast.LENGTH_LONG).show();
                System.out.println(" flag is "+b.getString("flag"));
                Intent intent = new Intent(applyOrder.this,menu.class);
                intent.putExtra("object",0);
                startActivity(intent);
                finish();
            }else if(b.getString("flag").equals("0")){
                Toast.makeText(applyOrder.this,"不能重复申请！",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(applyOrder.this,menu.class);
                intent.putExtra("object",0);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(applyOrder.this,"操作失败！",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(applyOrder.this,menu.class);
                intent.putExtra("object",0);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
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
        mBack = (Button)findViewById(R.id.back);
        InputText=(EditText)findViewById(R.id.inputText);
        btnApply=(Button)findViewById(R.id.buttonApply);

        mBack.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(applyOrder.this, orderInfo.class);
                startActivity(intent);
                finish();
            }
        });

        btnApply.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                String ipt=InputText.getText().toString();
                applyThread myThread=new applyThread(ipt);
                new Thread(myThread).start();
            }
        });









    }

    class applyThread implements Runnable {
        private String ipt;

        public applyThread(String ipt ){
            this.ipt=ipt;

        }

        public void run(){

            String url=getString(R.string.server_ip) + "AddOrderApply";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=null;
            httpPost=new HttpPost(url);

            List<NameValuePair> formparams=new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("content",ipt));//申请内容
            formparams.add(new BasicNameValuePair("time",ApplyTimeValue));//申请内容
            formparams.add(new BasicNameValuePair("userId", login.u.getUserid()));//申请人
            formparams.add(new BasicNameValuePair("orderId", homePage.ord.getOrderid()));//饭约号
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
                        Bundle b=new Bundle();
                        System.out.println(" flag is "+jsondata4.getString("flag"));
                        b.putString("flag",jsondata4.getString("flag"));
                        Message msg=new Message();
                        msg.setData(b);
                        applyOrder.this.handler.sendMessage(msg);
                    }
                }


            } catch(Exception e){
                e.printStackTrace();
            }

        }
    }


}
