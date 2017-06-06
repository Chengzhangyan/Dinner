package com.example.a1.dinnerlogin.userInfo;

/**
 * Created by zhanglan on 2017/5/9.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.login.login;
import android.graphics.Bitmap;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import com.example.a1.dinnerlogin.photoChange;


/**
 * Created by user on 2017/4/19.
 */

/*个人中心功能实现类*/


public class userInfoEdit extends Activity{

    private static final String IMAGE_UNSPECIFIED = "image/*";
  //  private static final int PHOTO_RESOULT = 4;
  photoChange photoChange = new photoChange();
  //  private Button album, camera;
    private ImageView mDisplay;
    private Button mSearch;

    private static final String TAG = "MyActivity";

    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;

    private Button genderBtn;
    private Button nameBtn;
    private Button addressBtn;
    private Button schoolBtn;
    private Button exitBtn;

    public TextView mName;
    private TextView mGender;
    private TextView mArea;
    private TextView mSchool;





    Handler handler = new Handler(){
        public void handleMessage(Message msg){

            Bundle b = msg.getData();/*获得msg中的数据*/

          //  if (b.getString("flag").equals("true")){/*若flag为true则登陆成功*/
                Toast.makeText(userInfoEdit.this,"获取用户信息成功！",Toast.LENGTH_SHORT).show();
          //  mName.setText(b.getString("nickname").toString());
            //}
            super.handleMessage(msg);
        }
    };


    private final static int DISPLAY_DIA = 3;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        /*绑定布局文件中的部件*/
        mSearch = (Button) findViewById(R.id.change_head) ;
        mDisplay = (ImageView)findViewById(R.id.user_image);
        mName = (TextView)findViewById(R.id.user_edit_name);
        mGender = (TextView) findViewById(R.id.user_edit_gender);
        mArea = (TextView) findViewById(R.id.user_edit_address);
        mSchool = (TextView) findViewById(R.id.user_edit_school);

        nameBtn = (Button)findViewById(R.id.user_name_edit_btn);
        genderBtn = (Button)findViewById(R.id.user_gender_edit_btn);
        schoolBtn = (Button)findViewById(R.id.user_school_edit_btn);
        addressBtn = (Button)findViewById(R.id.user_address_edit_btn);
        exitBtn = (Button)findViewById(R.id.returnback);


        getInfo();//发送userid到服务端获取用户信息并显示到ui上

        mSearch.setOnClickListener(mListener);
        nameBtn.setOnClickListener(mListener);  //
        genderBtn.setOnClickListener(mListener);
        addressBtn.setOnClickListener(mListener);
        schoolBtn.setOnClickListener(mListener);
        exitBtn.setOnClickListener(mListener);

        exitBtn.setOnClickListener(mListener);

//修改头像监听器
        mSearch.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          showDialog(DISPLAY_DIA);
                                      }
                                  }
        );
    }

    /*不同按钮按下的监听器的选择*/
    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent_user_to_name_edit = new Intent(userInfoEdit.this,userNameEdit.class);
            Intent intent_user_to_gender_edit = new Intent(userInfoEdit.this,userGenderEdit.class);
            Intent intent_user_to_address_edit = new Intent(userInfoEdit.this,userAddressEdit.class);
            Intent intent_user_to_school_edit = new Intent(userInfoEdit.this,userSchoolEdit.class);
            //Intent intent_user_to_orderlist = new Intent(userInfoEdit.this,userOrderList.class);

            switch (v.getId()){

                case R.id.user_name_edit_btn:    /*修改用户名*/
                    startActivity(intent_user_to_name_edit);
                    finish();
                    break;
                case R.id.user_gender_edit_btn:    /*修改性别按钮*/
                    startActivity(intent_user_to_gender_edit);
                    finish();
                    break;
                case R.id.user_address_edit_btn:/*修改地区页面*/
                    startActivity(intent_user_to_address_edit);
                    finish();
                    break;
                case R.id.user_school_edit_btn:/*修改学校*/
                    startActivity(intent_user_to_school_edit);
                    finish();
                    break;

                case R.id.returnback:
                    Intent intent_to_login = new Intent(userInfoEdit.this,login.class);
                    startActivity(intent_to_login);
                    finish();
                    break;
            }}    };


    public void getInfo(){


        System.out.println("the userid i got is :"+login.u.getUserid());
        //接收到userid然后发送到客户端获取用户info
        InfoThread myThread = new InfoThread(login.u.getUserid());
        new Thread(myThread).start();
    }

/*    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        // String styleoffood = "";
        switch (id) {
            case DISPLAY_DIA:
                final String TAG = "上传图片窗口";
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                AlertDialog.Builder setTitle2 = builder2.setTitle("请选择上传方式");
                builder2.setPositiveButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "相册");
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);

                        startActivityForResult(intent, CROP_REQUEST_CODE);
                    }

                });
                builder2.setNegativeButton("相机", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "相机");
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                                getExternalStorageDirectory(), "temp.jpg")));
                        startActivityForResult(intent2, CROP_REQUEST_CODE);
                    }

                });
                dialog = builder2.create();
                break;
        }

        return dialog;
    }*/
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String TAG = "上传拖";
        Bitmap bitmap = null;
        ContentResolver resolver = getContentResolver();
        switch (requestCode) {

            case CROP_REQUEST_CODE:

                Log.i(TAG, "裁剪以后 [ " + data + " ]");
                if (data == null) {
                    // TODO 如果之前以后有设置过显示之前设置的图片 否则显示默认的图片
                    return;
                }
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);//获得图

                }catch (IOException e){
                    e.printStackTrace();
                }
                System.out.println("show success");
                mDisplay.setImageBitmap(bitmap);
                photoChange.setPhotoString(bitmap);
                System.out.println("getPhotoString is "+photoChange.getPhotoString());
               // headThread myThread = new headThread(login.u.getUserid(),photoChange.getPhotoString());
               // new Thread(myThread).start();

            default:
                break;
        }
    }*/

  /*  public void getPhotoString(Bitmap bitmap){
        photoChange bm = new photoChange();
       String photoStr = bm.bitmapToBase64(bitmap);//通过base64编码转成string

    }*/

    private void updateUI( final String n,final String g,final String a,final String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
           //     mDisplay.setImageBitmap(head);
                mName.setText(n);
                mGender.setText(g);
                mArea.setText(a);
                mSchool.setText(s);
            }
        });
    }



    class InfoThread implements Runnable{
        private String userid;


        public InfoThread(String userid){
            this.userid = userid;
        }
        @Override
        public void run(){
            String url=getString(R.string.server_ip) +"ShowUserInfo";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = null;
            httpPost = new HttpPost(url);

            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("user_id",userid));
          //  formparams.add(new BasicNameValuePair("head",photoChange.getPhotoString()));
            UrlEncodedFormEntity uefEntity;

            try {
                //把数据封装到实体，发送到服务端
                uefEntity=new UrlEncodedFormEntity(formparams,"UTF-8");
                httpPost.setEntity(uefEntity);

                //获得回应
                HttpResponse response ;
                response = httpClient.execute(httpPost);/*response为服务端传来的数据*/
                if (response.getStatusLine().getStatusCode()==200){/*返回码为200则连接成功*/
                    HttpEntity entity = response.getEntity();/*获得服务端传来的实体*/
                    if (entity!=null){
                        String json = EntityUtils.toString(entity,"UTF-8");
                        JSONObject jsonData = new JSONObject(json);/*解码json数据包*/
                        Bundle b = new Bundle();/*用于类之间传递数据的对象*/

                     //   b.putString("flag",jsonData.getString("flag"));/*获取json数据包中flag的值并放入b中*/
                        b.putString("nickname",jsonData.getString("nickname"));
                        b.putString("gender",jsonData.getString("gender"));
                        b.putString("area",jsonData.getString("area"));
                        b.putString("school",jsonData.getString("school"));
                       // b.putString("head",jsonData.getString("head"));

                       //  Bitmap head_icon = photoChange.base64ToBitmap(jsonData.getString("head"));
                      //  updateUI(head_icon,jsonData.getString("nickname"),jsonData.getString("gender"),jsonData.getString("area"),jsonData.getString("school"));
                        updateUI(jsonData.getString("nickname"),jsonData.getString("gender"),jsonData.getString("area"),jsonData.getString("school"));
                        Message msg = new Message();
                        msg.setData(b);/*向消息中放入b对象，这样可以发送到别的类*/
                        userInfoEdit.this.handler.sendMessage(msg);/*发送消息到register类*/
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }


    }

/*    class headThread implements Runnable{
        private String userid;
        private String head;


        public headThread(String userid,String head){
            this.userid = userid;
            this.head = head;
        }
        @Override
        public void run(){
            String url=getString(R.string.server_ip) +"ShowUserInfo";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = null;
            httpPost = new HttpPost(url);

            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("user_id",userid));
            formparams.add(new BasicNameValuePair("head",photoChange.getPhotoString()));
            UrlEncodedFormEntity uefEntity;

            try {
                //把数据封装到实体，发送到服务端
                uefEntity=new UrlEncodedFormEntity(formparams,"UTF-8");
                httpPost.setEntity(uefEntity);

                //获得回应
                HttpResponse response ;
                response = httpClient.execute(httpPost);*//*response为服务端传来的数据*//*
                if (response.getStatusLine().getStatusCode()==200){*//*返回码为200则连接成功*//*
                    HttpEntity entity = response.getEntity();*//*获得服务端传来的实体*//*
                    if (entity!=null){
                        String json = EntityUtils.toString(entity,"UTF-8");
                        JSONObject jsonData = new JSONObject(json);*//*解码json数据包*//*
                        Bundle b = new Bundle();*//*用于类之间传递数据的对象*//*

                        //   b.putString("flag",jsonData.getString("flag"));*//*获取json数据包中flag的值并放入b中*//*
                        b.putString("flag",jsonData.getString("flag"));

                        Message msg = new Message();
                        msg.setData(b);*//*向消息中放入b对象，这样可以发送到别的类*//*
                        userInfoEdit.this.handler.sendMessage(msg);*//*发送消息到register类*//*
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }


    }*/

}
