package com.example.a1.dinnerlogin.applyDate;

import android.app.Activity;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import com.example.a1.dinnerlogin.releaseDate.orderInfo;
import com.example.a1.dinnerlogin.menu;
import com.example.a1.dinnerlogin.releaseDate.homePage;
import com.example.a1.dinnerlogin.releaseDate.orderId;


/**
 * Created by 1 on 2017/5/4.
 */

public class checkApply extends Activity {

    private TextView mEatingTime;

    private TextView mContent;
    private TextView mResult;

    private TextView mApplyer;
    private Button mOrder;
    private Button mRefuse;
    private Button mSure;
    private Button mBack;

//发送饭约审核结果
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
            if(b.getString("flag").equals("true")){
                Toast.makeText(checkApply.this,"审核成功",Toast.LENGTH_LONG).show();
                System.out.println(" the check flag is "+b.getString("flag"));
                Intent intent = new Intent(checkApply.this,menu.class);
                intent.putExtra("object",1);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(checkApply.this,"审核失败",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(checkApply.this,menu.class);
                intent.putExtra("object",1);
                startActivity(intent);
                finish();
            }
        }
    };

    //加载饭约信息
    Handler handler1=new Handler() {
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();

            inputData(b.getString("nickname"), b.getString("time"), b.getString("content"), b.getString("read"));

        }

    };

        @Override
        protected void onCreate(Bundle saveInstanceState) {
            super.onCreate(saveInstanceState);
            setContentView(R.layout.activity_checkapply);
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

            getInfoThread myThread=new getInfoThread(msgMenu.ord.getOrderid());
            // od.setOrderid(homePage.ord.getOrderid());
            new Thread(myThread).start();


            mResult = (TextView) findViewById(R.id.status);
            mApplyer = (TextView) findViewById(R.id.applyer);

            mEatingTime = (TextView) findViewById(R.id.time);

            mContent = (TextView) findViewById(R.id.content);
            mOrder = (Button) findViewById(R.id.orderinfo);
            mSure = (Button) findViewById(R.id.sure_btn);
            mRefuse = (Button) findViewById(R.id.refuse_btn);
            mBack = (Button) findViewById(R.id.back_btn);

          //  inputData(message.ord.getOrderid(),"15:30", "明湖餐厅", "给我加入吧，拜托拜托！", "未批准");

            mBack.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(checkApply.this, menu.class);
                    intent.putExtra("object", 1);
                    startActivity(intent);
                    finish();
                }
            });

            mOrder.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(checkApply.this, checkOrderInfo.class);
                    intent.putExtra("object", 0);
                    startActivity(intent);
                    finish();
                }
            });

            mSure.setOnClickListener(mListener);
            mRefuse.setOnClickListener(mListener);
        }

        View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkThread myThread;
                switch (v.getId()) {
                    case R.id.sure_btn:    /*登录界面的诸恶按钮*/
                          myThread=new checkThread("1");
                        new Thread(myThread).start();
                        //testGet("OK!");
                        //mResult.postInvalidate();
                        break;
                    case R.id.refuse_btn:
                          myThread=new checkThread("-1");
                        new Thread(myThread).start();
                        //testGet("拒绝!");
                       // mResult.postInvalidate();
                        break;
                }
            }
        };



        private void inputData(String nickname, String eatingTime,  String content, String result) {
            // mNickName.setText(nickName);String nickName,
            mEatingTime.setText(eatingTime);

            mApplyer.setText(nickname);
            mResult.setText(result);
            mContent.setText(content);



        }

        class checkThread implements Runnable {
            private String read;

            public checkThread(String read) {
                this.read = read;

            }

            public void run() {

                String url = getString(R.string.server_ip) + "DealApply";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = null;
                httpPost = new HttpPost(url);

                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("read", read));//审批结果 1同意 -1拒绝 false操作失败
                formparams.add(new BasicNameValuePair("userId", checkMsg.applyer.getUserid()));//我
                formparams.add(new BasicNameValuePair("orderId", "0755ba2b-a391-4663-95be-7f4aaa20b5b5"));//饭约号
                UrlEncodedFormEntity uefEntity;

                try {
                    uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
                    httpPost.setEntity(uefEntity);
                    HttpResponse response;
                    response = httpClient.execute(httpPost);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();

                        if (entity != null) {
                            String json = EntityUtils.toString(entity, "UTF-8");
                            JSONObject jsondata4 = new JSONObject(json);
                            Bundle b = new Bundle();
                            System.out.println(" flag is " + jsondata4.getString("flag"));
                            b.putString("flag", jsondata4.getString("flag"));
                            Message msg = new Message();
                            msg.setData(b);
                            checkApply.this.handler.sendMessage(msg);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        class getInfoThread implements Runnable {
            private String orderId;

            public getInfoThread(String orderId) {
                this.orderId = orderId;

            }

            public void run() {

                String url = getString(R.string.server_ip) + "ShowApply";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = null;
                httpPost = new HttpPost(url);

                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                // formparams.add(new BasicNameValuePair("result", result));//审批结果
                formparams.add(new BasicNameValuePair("userId", checkMsg.applyer.getUserid()));//申请人id
                formparams.add(new BasicNameValuePair("orderId", orderId));//饭约号
                UrlEncodedFormEntity uefEntity;

                try {
                    uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
                    httpPost.setEntity(uefEntity);
                    HttpResponse response;
                    response = httpClient.execute(httpPost);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();

                        if (entity != null) {
                            String json = EntityUtils.toString(entity, "UTF-8");
                            JSONObject jsondata4 = new JSONObject(json);
                            Bundle b = new Bundle();
                            // System.out.println(" flag is "+jsondata4.getString("flag"));
                            //b.putString("flag",jsondata4.getString("flag"));

                            b.putString("time", jsondata4.getString("time"));
                            b.putString("content", jsondata4.getString("content"));
                          //  b.putString("canteen", jsondata4.getString("canteen"));
                          //  b.putString("flag", jsondata4.getString("flag"));
                             b.putString("nickname",jsondata4.getString("nickname"));
                            b.putString("read",jsondata4.getString("read"));
                            Message msg1 = new Message();
                            msg1.setData(b);
                            checkApply.this.handler1.sendMessage(msg1);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }



    }
}
