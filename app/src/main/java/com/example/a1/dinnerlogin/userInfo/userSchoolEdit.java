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
import android.widget.EditText;
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

public class userSchoolEdit extends Activity {
    private Button mReturnButton;
    private Button mSureButton;
    private EditText mSchool;

    Handler handler = new Handler(){
        public void handleMessage(Message msg){

            Bundle b = msg.getData();/*获得msg中的数据*/

            if (b.getString("flag").equals("true")){/*若flag为true则登陆成功*/
                Toast.makeText(userSchoolEdit.this,"修改学校成功！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(userSchoolEdit.this,menu.class);
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
        setContentView(R.layout.activity_school_edit);

        mReturnButton = (Button)findViewById(R.id.school_edit_cansel_btn);
        mSureButton = (Button)findViewById(R.id.school_edit_sure_btn);
        mSchool = (EditText)findViewById(R.id.school_edit);

        mReturnButton.setOnClickListener(mListener);
        mSureButton.setOnClickListener(mListener);


    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent this_to_info = new Intent(userSchoolEdit.this,menu.class);
            this_to_info.putExtra("object",4);
            switch (v.getId()) {
                case R.id.school_edit_sure_btn:
                    editSchool();
                    break;
                case R.id.school_edit_cansel_btn:

                    startActivity(this_to_info);
                    finish();
                    break;
            }
        }
    };

    public boolean isSchoolValid(){
        if (mSchool.getText().toString().trim().equals("")) {
            Toast.makeText(this,getString(R.string.school_empty),Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    public void editSchool(){

        String name = mSchool.getText().toString().trim();/*获得当前输入的新昵称*/
        System.out.println("userid is "+login.u.getUserid());
        NameThread myThread = new NameThread(login.u.getUserid(),name);
        new Thread(myThread).start();

        return;




    }

    class NameThread implements Runnable{
        private String userid;
        private String school;

        public NameThread(String userid,String school){
            this.userid = userid;
            this.school = school;
        }
        @Override
        public void run() {
            String url = getString(R.string.server_ip) + "UpdateUserInfo";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = null;
            httpPost = new HttpPost(url);

            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("user_id", userid));
            formparams.add(new BasicNameValuePair("school", school));
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
                        b.putString("school", jsonData.getString("school"));
                        System.out.println("flag is " + jsonData.getString("flag"));
                        System.out.println("school is " + jsonData.getString("school"));
                        Message msg = new Message();
                        msg.setData(b);/*向消息中放入b对象，这样可以发送到别的类*/
                        userSchoolEdit.this.handler.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
