package com.example.a1.dinnerlogin.applyDate;

import android.app.Activity;
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

/**
 * Created by 1 on 2017/5/4.
 */

public class applyOrder extends Activity {

    EditText InputText;
    Button btnApply;
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
            if(b.getString("flag").equals("true")){
                Toast.makeText(applyOrder.this,"发送成功",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(applyOrder.this,"发送失败",Toast.LENGTH_LONG).show();
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

        InputText=(EditText)findViewById(R.id.inputText);
        btnApply=(Button)findViewById(R.id.buttonApply);

        btnApply.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                String ipt=InputText.getText().toString();
                AddDinnerThread myThread=new AddDinnerThread(ipt);
                new Thread(myThread).start();
            }
        });









    }

    class AddDinnerThread implements Runnable {
        private String ipt;
        public AddDinnerThread(String ipt){
            this.ipt=ipt;
        }

        public void run(){

            String url=getString(R.string.server_ip) + "adddinner";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=null;
            httpPost=new HttpPost(url);

            List<NameValuePair> formparams=new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("inputText",ipt));
            formparams.add(new BasicNameValuePair("userid", login.u.getUserid()));
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
                        JSONObject jsondata=new JSONObject(json);
                        Bundle b=new Bundle();
                        b.putString("flag",jsondata.getString("flag"));
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
