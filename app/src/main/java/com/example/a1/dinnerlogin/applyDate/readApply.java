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

import java.util.ArrayList;
import java.util.List;
import com.example.a1.dinnerlogin.releaseDate.orderInfo;
import com.example.a1.dinnerlogin.menu;
import com.example.a1.dinnerlogin.releaseDate.homePage;
import com.example.a1.dinnerlogin.releaseDate.orderId;


/**
 * Created by 1 on 2017/5/4.
 */

public class readApply extends Activity {

    private TextView mEatingTime;
    private TextView mCanteen;
    private TextView mContent;
    private TextView mResult;
    private ImageView mHead;
    private Button mApplyer;
    private Button mOrder;

    private Button mBack;


    //加载饭约信息
    Handler handler1=new Handler() {
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            String result;
            if (b.getString("flag").equals("-1")){
                result = "被拒绝";
            }else if(b.getString("flag").equals("1")){
                result = "已允许";
            }else{
                result = "待审批";
            }
            inputData(b.getString("userId"), b.getString("eatingTime"), b.getString("canteen"), b.getString("content"),result);

        }

    };

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_checkapply2);
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
        mApplyer = (Button) findViewById(R.id.applyer);
        mHead = (ImageView) findViewById(R.id.applyer_head);
        mEatingTime = (TextView) findViewById(R.id.time);
        mCanteen = (TextView) findViewById(R.id.canteen);
        mContent = (TextView) findViewById(R.id.content);
        mOrder = (Button) findViewById(R.id.orderinfo);

        mBack = (Button) findViewById(R.id.back_btn);

        //  inputData(message.ord.getOrderid(),"15:30", "明湖餐厅", "给我加入吧，拜托拜托！", "未批准");

        mBack.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(readApply.this, menu.class);
                intent.putExtra("object", 1);
                startActivity(intent);
                finish();
            }
        });

        mOrder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(readApply.this, checkOrderInfo.class);
                intent.putExtra("object", 0);
                startActivity(intent);
                finish();
            }
        });


    }






    private void inputData(String nickname, String eatingTime, String canteen, String content, String result) {
        // mNickName.setText(nickName);String nickName,
        mEatingTime.setText(eatingTime);
        mCanteen.setText(canteen);
        mApplyer.setText(nickname);
        mResult.setText(result);
        mContent.setText(content);

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
            formparams.add(new BasicNameValuePair("userId", applyMsg.applyer.getUserid()));//申请人id
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
                        b.putString("userId", jsondata4.getString("userId"));
                        b.putString("time", jsondata4.getString("eatingTime"));
                        b.putString("content", jsondata4.getString("content"));
                        b.putString("canteen", jsondata4.getString("canteen"));
                        b.putString("flag", jsondata4.getString("flag"));
                        //  b.putString("head",jsondata4.getString("head"));

                        Message msg1 = new Message();
                        msg1.setData(b);
                        readApply.this.handler1.sendMessage(msg1);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }



    }
}
