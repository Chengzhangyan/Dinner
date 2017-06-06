package com.example.a1.dinnerlogin.userInfo;

/**
 * Created by zhanglan on 2017/5/9.
 */

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
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
import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.login.login;
import com.example.a1.dinnerlogin.*;

/**
 * Created by user on 2017/4/20.
 */

public class userGenderEdit extends Activity{

    private Button mReturnButton;
    private Button mSureButton;
    private RadioGroup mgenderGroup;
    private RadioButton mManCheck;
    private RadioButton mWomenCheck;
    private String gender = "";

    Handler handler = new Handler(){
        public void handleMessage(Message msg){

            Bundle b = msg.getData();/*获得msg中的数据*/

            if (b.getString("flag").equals("true")){/*若flag为true则登陆成功*/
                Toast.makeText(userGenderEdit.this,"修改gender成功！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(userGenderEdit.this,menu.class);
                intent.putExtra("object",4);
                startActivity(intent);
                finish();
            }
            super.handleMessage(msg);
        }
    };


    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_edit);

        mReturnButton = (Button)findViewById(R.id.gender_edit_cansel_btn);
        mSureButton = (Button)findViewById(R.id.gender_edit_sure_btn);
        mgenderGroup = (RadioGroup) findViewById(R.id.gender_group);

        mgenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mManCheck = (RadioButton)findViewById(R.id.man_check);
                mWomenCheck = (RadioButton)findViewById(R.id.women_check);

            }
        });


        View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent this_to_info = new Intent(userGenderEdit.this,menu.class);
                this_to_info.putExtra("object",4);
                Intent newGenderIntent = new Intent();
                newGenderIntent.putExtra("object",4);
                newGenderIntent.setClass(userGenderEdit.this,menu.class);
                switch (v.getId()) {
                    case R.id.gender_edit_sure_btn:
                        if (isGenderValid()){
                            editGender();
                            startActivity(this_to_info);
                            finish();}else{
                            Toast.makeText(userGenderEdit.this,"您未选择您的性别！",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.gender_edit_cansel_btn:
                        Toast.makeText(userGenderEdit.this,getString(R.string.back),Toast.LENGTH_SHORT).show();
                        startActivity(this_to_info);
                        finish();
                        break;
                }
            }

        };

        mReturnButton.setOnClickListener(mListener);
        mSureButton.setOnClickListener(mListener);
        mgenderGroup.setOnClickListener(mListener);


    }

    public boolean isGenderValid(){
        if (mManCheck.isChecked()){
            gender = "汉子";
            return true;
        }else if (mWomenCheck.isChecked()){
            gender = "妹子";
            return true;
        }else{
            return false;
        }
    }

    public void editGender(){

        System.out.println("userid is "+login.u.getUserid());
        GenderThread myThread = new GenderThread(login.u.getUserid(),gender);
        new Thread(myThread).start();

        return;
    }
    class GenderThread implements Runnable {
        private String userid;
        private String gender;

        public GenderThread(String userid, String gender) {
            this.userid = userid;
            this.gender = gender;
        }

        @Override
        public void run() {
            String url = getString(R.string.server_ip) + "UpdateUserInfo";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = null;
            httpPost = new HttpPost(url);

            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("user_id", userid));
            formparams.add(new BasicNameValuePair("gender", gender));
            UrlEncodedFormEntity uefEntity;

            try {
                //把数据封装到实体，发送到服务端
                uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
                httpPost.setEntity(uefEntity);

                //获得回应
                HttpResponse response;
                response = httpClient.execute(httpPost);/*response为服务端传来的数据*/
                if (response.getStatusLine().getStatusCode() == 200) {/*返回码为200则连接成功*/
                    System.out.println("back Success!");
                    HttpEntity entity = response.getEntity();/*获得服务端传来的实体*/
                    if (entity != null) {
                        String json = EntityUtils.toString(entity, "UTF-8");
                        JSONObject jsonData = new JSONObject(json);/*解码json数据包*/
                        Bundle b = new Bundle();/*用于类之间传递数据的对象*/

                        b.putString("flag", jsonData.getString("flag"));/*获取json数据包中flag的值并放入b中*/
                       // b.putString("gender", jsonData.getString("gender"));
                        System.out.println("flag is " + jsonData.getString("flag"));
                     //   System.out.println("gender is " + jsonData.getString("gender"));
                        Message msg = new Message();
                        msg.setData(b);/*向消息中放入b对象，这样可以发送到别的类*/
                        userGenderEdit.this.handler.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
