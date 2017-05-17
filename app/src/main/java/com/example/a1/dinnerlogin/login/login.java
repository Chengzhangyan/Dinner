package com.example.a1.dinnerlogin.login;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

import com.example.a1.dinnerlogin.releaseDate.*;
import com.example.a1.dinnerlogin.applyDate.*;
import com.example.a1.dinnerlogin.userInfo.*;
import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.menu;

/*登录界面的activity*/
public class login extends Activity{

    private EditText mAccount; /*用户名输入框*/
    private EditText mPwd;/*密码输入框*/
    private Button mRegisterButton;
    private Button mLoginButton;
    private Button mClearButton;
    private CheckBox mRemenberCheck;

    private SharedPreferences login_sp;       /*用作保存配置参数，即是否记住密码*/



    private TextView mChangepswText;   /*修改密码文字*/
    public String userid;
    public static userId u = new userId();


    Handler handler = new Handler(){
        public void handleMessage(Message msg){

            Bundle b = msg.getData();/*获得msg中的数据*/
            //用于传送userid 到userinfo

            if (b.getString("flag").equals("true")){/*若flag为true则登陆成功*/
                Intent intent = new Intent(login.this, menu.class);  /*从login类转换到userInfoEdit类*/
                intent.putExtra("object",0);
                //sendUserIdToInfo(b.getString("user_id"));
                savePwd(b.getString("flag"));//保存密码
                u.setUserid(b.getString("userid"));
                Toast.makeText(login.this,"login success",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);       /*加载界面*/
//Exception();

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

        /*绑定XML文件中的部件*/
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText)findViewById(R.id.login_edit_pwd);
        mRegisterButton = (Button)findViewById(R.id.login_btn_register);
        mClearButton = (Button)findViewById(R.id.login_btn_clear);
        mLoginButton = (Button)findViewById(R.id.login_btn_login);
        mChangepswText = (TextView)findViewById(R.id.login_text_change_pwd);
        mRemenberCheck = (CheckBox)findViewById(R.id.Login_Remember);



        login_sp = getSharedPreferences("userInfo",0);  /*login_sp能从userInfo文件中提取数据；userInfo是xml文件的名称，0是这个文件的操作模式*/
        String name = login_sp.getString("USER_NAME","");  /*把用户名从login_sp中取出并存到name中*/
        String pwd = login_sp.getString("PASSWORD","");    /*作用同上，提取密码并存入pwd中*/

        boolean choseRemember = login_sp.getBoolean("mRememberCheck",false);  /*获取记住密码的选择值*/
        //  boolean choseAutoLogin = login_sp.getBoolean("mAutoLoginCheck",false);/*获取自动登录的选择值*/

        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRemenberCheck.setChecked(true);
        }

        /*为按钮设置监听器*/
        mLoginButton.setOnClickListener(mListener);
        mClearButton.setOnClickListener(mListener);
        mRegisterButton.setOnClickListener(mListener);
        mChangepswText.setOnClickListener(mListener);

    }

    /*不同按钮按下的监听器的选择*/
    OnClickListener mListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent_login_to_register = new Intent(login.this,register.class);

            switch (v.getId()){

                case R.id.login_btn_register:    /*登录界面的诸恶按钮*/
                    startActivity(intent_login_to_register);
                    finish();
                    break;

                case R.id.login_btn_login:    /*登陆界面的登录按钮*/
                 /*确定按钮的触发事件是启用子线程*/
                    if (isUserNameAndPwdValid()) {
                        String username = mAccount.getText().toString();
                        String pwd = mPwd.getText().toString();
                        LoginThread myThread = new LoginThread(username, pwd);
                        new Thread(myThread).start();
                    }
                    break;
                case R.id.login_btn_clear:
                    clear();
                    break;
                case R.id.login_text_change_pwd:    /*点击修改密码文本*/

                    finish();
                    break;
            }}    };


    /*登录*/
    public void savePwd(String flag){

        String username = mAccount.getText().toString();
        String pwd = mPwd.getText().toString();//获取当前输入的登录名、密码

        SharedPreferences.Editor editor = login_sp.edit();  //创建editor对象并且通过login_sp写入数据

        if(flag == "true") {    //result等于1即账号、密码正确
            //   保存用户密码到login_sp
            editor.putString("USER_NAME", username);  //通过editor向login_sp写入用户名和密码
            editor.putString("PASSWORD", pwd);

            //是否记住密码
            if (mRemenberCheck.isChecked()) {
                editor.putBoolean("mRemember", true);
            }   else {
                editor.putBoolean("mRemember", false);
            }
            editor.commit();

        }
    }

    /*重置*/
    public void clear(){
        mAccount.setText("");
        mPwd.setText("");

    }

    /*判断用户密码是否有效（不是判断是否正确）*/
    public boolean isUserNameAndPwdValid(){
        if (mAccount.getText().toString().trim().equals("")){
            /*用户名为空的提示*/
            //Toast.makeText(this,getString(R.string.account_empty),Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwd.getText().toString().trim().equals("")){
            /*密码为空的提示*/
            //Toast.makeText(this,getString(R.string.pwd_empty),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    class LoginThread implements Runnable{
        private String userName;
        private String userPsw;

        public LoginThread(String userName,String psw){
            this.userName = userName;
            this.userPsw = psw;
        }
        @Override
        public void run(){
            String url=getString(R.string.server_ip) + "Login";
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
                if (response.getStatusLine().getStatusCode()==200){/*返回码为200则连接成功*/
                    HttpEntity entity = response.getEntity();/*获得服务端传来的实体*/
                    if (entity!=null){
                        String json = EntityUtils.toString(entity,"UTF-8");
                        JSONObject jsonData = new JSONObject(json);/*解码json数据包*/
                        Bundle b = new Bundle();/*用于类之间传递数据的对象*/
                        System.out.println(jsonData.getString("flag"));
                        b.putString("flag",jsonData.getString("flag"));/*获取json数据包中flag的值并放入b中*/
                        b.putString("userid",jsonData.getString("user_id"));
                        Message msg = new Message();
                        msg.setData(b);/*向消息中放入b对象，这样可以发送到别的类*/
                        login.this.handler.sendMessage(msg);/*发送消息到register类*/
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }



    }


}


