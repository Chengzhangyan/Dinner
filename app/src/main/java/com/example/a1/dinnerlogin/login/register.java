package com.example.a1.dinnerlogin.login;

/**
 * Created by zhanglan on 2017/5/9.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1.dinnerlogin.R;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

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
import com.example.a1.dinnerlogin.releaseDate.*;
import com.example.a1.dinnerlogin.applyDate.*;
import com.example.a1.dinnerlogin.userInfo.*;
import com.example.a1.dinnerlogin.menu;
/*用户已经存在时，返回提示*//*

      }*/

public class register extends AppCompatActivity{
    /*用户名、密码、确认密码编辑框*/
    private EditText mAccount;
    private EditText mPwd;
    private EditText mPwdCheck;
    /*确认、取消按钮*/
    private Button mSureButton;
    private Button mCanselButton;
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Bundle b = msg.getData();/*获得msg中的数据*/
            if (b.getString("flag").equals("1")){/*若flag为true则注册成功*/
                login.u.setUserid(b.getString("userid"));
          /*显示注册成功*/Toast.makeText(register.this, getText(R.string.register_success), Toast.LENGTH_SHORT).show();
          /*转到登陆界面*/Intent intent_register_to_login = new Intent(register.this, menu.class);
                intent_register_to_login.putExtra("object",0);
                startActivity(intent_register_to_login);
                finish();
            }else if(b.getString("flag").equals("1")){
                Toast.makeText(register.this, "不能重复注册！", Toast.LENGTH_SHORT).show();
            }else  {
                Toast.makeText(register.this, "注册失败！", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };



    /* @Override*/
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        /*绑定布局文件中的部件*/
        mAccount = (EditText)findViewById(R.id.resetpwd_edit_phone);
        mPwd = (EditText)findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText)findViewById(R.id.resetpwd_edit_pwd_new);
        mSureButton = (Button)findViewById(R.id.register_btn_sure);
        mCanselButton = (Button)findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_listener);  //设定注册界面确定、取消按钮的监听器
        mCanselButton.setOnClickListener(m_register_listener);



    }


    View.OnClickListener m_register_listener = new View.OnClickListener(){
        public void onClick(View v){
/*不同按钮按下的事件选择*/
            switch (v.getId()){
                case R.id.register_btn_sure:
/*确定按钮的触发事件是启用子线程*/
                    if(isUserNameAndPwdValid()){
                        String username = mAccount.getText().toString();
                        String pwd = mPwd.getText().toString();
                        RegisterThread myThread = new RegisterThread(username,pwd);
                        new Thread(myThread).start();}
                    break;

                case R.id.register_btn_cancel:
/*取消按钮的触发事件是把当前页面调回登录界面*/
                    Intent intent_register_to_login = new Intent(register.this,login.class);
/*Intent函数负责部件间的沟通、调用*/
                    startActivity(intent_register_to_login);
                    finish();
                    break;
            }
        }
    };

    public boolean isUserNameAndPwdValid() {
        String userName = mAccount.getText().toString().trim();
        String userPwd = mPwd.getText().toString().trim();
        String userPwdCheck = mPwdCheck.getText().toString().trim();
        if (userName.equals("")) {
/*若用户民为空*/
            Toast.makeText(this, getText(R.string.account_empty), Toast.LENGTH_SHORT).show();
/*则显示用户名为空的提示*/
            return false;
        } else if (userPwd.equals("")) {
/*若密码为空*/
            Toast.makeText(this, getText(R.string.pwd_empty), Toast.LENGTH_SHORT).show();
/*则提示密码为空的提示*/
            return false;
        } else if (userPwdCheck.equals("")) {
/*若确认密码为空*/
            Toast.makeText(this, getText(R.string.pwd_check_empty), Toast.LENGTH_SHORT).show();
/*则提示确认密码为空*/
            return false;
        }else if(userPwd.equals(userPwdCheck)==false){
            Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    class RegisterThread implements Runnable{
        private String userName;
        private String userPsw;

        public RegisterThread(String userName,String userPsw){
            this.userName = userName;
            this.userPsw = userPsw;
        }

        @Override
        public void run(){
            String url=getString(R.string.server_ip) + "Register";;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = null;
            httpPost = new HttpPost(url);

            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("username",userName));
            formparams.add(new BasicNameValuePair("pwd",userPsw));
            UrlEncodedFormEntity uefEntity;

            try {
                uefEntity=new UrlEncodedFormEntity(formparams,"UTF-8");
                httpPost.setEntity(uefEntity);
                HttpResponse response ;
                response = httpClient.execute(httpPost);/*response为服务端传来的数据*/
                if (response.getStatusLine().getStatusCode()==200){/*返回码为200则连接成功f*/
                    HttpEntity entity = response.getEntity();/*获得服务端传来的实体*/
                    if (entity!=null){
                        String json = EntityUtils.toString(entity,"UTF-8");
                        JSONObject jsonData = new JSONObject(json);/*解码json数据包*/
                        Bundle b = new Bundle();/*用于类之间传递数据的对象*/
                        b.putString("flag",jsonData.getString("flag"));/*获取json数据包中flag的值并放入b中*/
                        b.putString("userid",jsonData.getString("user_id"));
                        Message msg = new Message();
                        msg.setData(b);/*向消息中放入b对象，这样可以发送到别的类*/
                        register.this.handler.sendMessage(msg);/*发送消息到register类*/
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }



    }

}
