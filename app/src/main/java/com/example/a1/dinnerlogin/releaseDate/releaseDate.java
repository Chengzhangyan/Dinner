package com.example.a1.dinnerlogin.releaseDate;

/**
 * Created by user on 2017/5/9.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.example.a1.dinnerlogin.R;
import  com.example.a1.dinnerlogin.login.*;
import com.example.a1.dinnerlogin.menu;
//import com.example.a1.dinnerlogin.bitmap;

/**
 * Created by 1 on 2017/4/11.
 */

public class releaseDate extends Activity {
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private String photo;

    private static final int CROP_REQUEST_CODE = 4;


    private ImageView mDisplay;
    private ImageButton mSearch;
    private EditText mMinPeople;
    private TextView mTime;
    private TextView mDate;
    private EditText mRestruant;
    private EditText mFoodStyle;
    private EditText mAddress;
    private EditText mStreet;
    private EditText mNumberOfPeople;
    private EditText mPhoneNumber;
    private Button mRelease;
    private View mBack;
    private Button mDateChoose;
    private Button mTimeChoose;
//当前系统时间
    long CurTime = System.currentTimeMillis();
    final String OrderTimeValue = String.valueOf(CurTime);

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
            if(b.getString("flag").equals("true")){
                Toast.makeText(releaseDate.this,"发布成功",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(releaseDate.this,"发布失败",Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(releaseDate.this, menu.class);
            intent.putExtra("object",0);
            startActivity(intent);
            finish();
        }
    };

    private final static int STYLEOFFOOD_DIA = 2;
    private final static int DISPLAY_DIA = 3;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_releasedate);
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

        mSearch = (ImageButton) findViewById(R.id.search);
        mDisplay = (ImageView) findViewById(R.id.display_iv);
        mDate = (TextView)findViewById(R.id.dateChoose);
        mTime = (TextView) findViewById(R.id.timeChoose);
        mDateChoose = (Button)findViewById(R.id.dateBtn);
        mTimeChoose = (Button)findViewById(R.id.timeBtn);
        mRestruant = (EditText) findViewById(R.id.edittextDR);
        mFoodStyle = (EditText) findViewById(R.id.edittextFood);
        mAddress = (EditText) findViewById(R.id.edittextAddress);
        mStreet = (EditText) findViewById(R.id.edittextStreet);
        mNumberOfPeople = (EditText) findViewById(R.id.edittextPeople);
        mPhoneNumber = (EditText) findViewById(R.id.edittextPhone);
        mBack = (View) findViewById(R.id.button_back);
        mRelease = (Button) findViewById(R.id.release);
        mMinPeople = (EditText)findViewById(R.id.edittextMinPeople);

        mFoodStyle.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              showDialog(STYLEOFFOOD_DIA);
                                          }
                                      }
        );

        mRelease.setOnClickListener(mListener);
        mBack.setOnClickListener(mListener);

        mDateChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(releaseDate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDate.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                    }
                },2017,5,11).show();
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          showDialog(DISPLAY_DIA);

                                      }
                                  }
        );

        mTimeChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(releaseDate.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mTime.setText(String.format("%d:%d",hourOfDay,minute));
                    }
                    //0,0指的是时间，true表示是否为24小时，true为24小时制
                },0,0,true).show();
            }
        });

/*orderid在服务端自动加入*/
    }
    /*不同按钮按下的监听器的选择*/
    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.release:
                    if (inputIsValid()) {
                        actionRelease();
                    }
                    break;
                case R.id.button_back:
                    Intent intent = new Intent(releaseDate.this, menu.class);
                    intent.putExtra("object",0);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };


        public void actionRelease() {
            String EatingTime =mDate.getText().toString()+ mTime.getText().toString();
            String Restaurant = mRestruant.getText().toString();
            String foodStyle = mFoodStyle.getText().toString();
            String city = mAddress.getText().toString();
            String maxPeople = mNumberOfPeople.getText().toString();
            String street = mStreet.getText().toString();
            String holderTel = mPhoneNumber.getText().toString();
           String myUserId = login.u.getUserid();
            //String myUserId = "14301039";
            String minPeople = mMinPeople.getText().toString();

            releaseThread myThread = new releaseThread(myUserId, EatingTime, Restaurant, foodStyle,
                    city, street, maxPeople,minPeople, holderTel, OrderTimeValue,photo);
            new Thread(myThread).start();

        }

        public boolean inputIsValid() {
            if (mTime.getText().toString().trim().equals("")) {
                //时间为空提示
                Toast.makeText(getApplicationContext(), "未填约餐时间", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mRestruant.getText().toString().trim().equals("")) {
                //餐厅为空提示
                Toast.makeText(getApplicationContext(), "未填餐厅", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mFoodStyle.getText().toString().trim().equals("")) {
                //菜系为空提示
                Toast.makeText(getApplicationContext(), "未选择菜系", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mAddress.getText().toString().trim().equals("")) {
                //地址为空提示
                Toast.makeText(getApplicationContext(), "未确定约餐地址", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mStreet.getText().toString().trim().equals("")) {
                //街道为空提示
                Toast.makeText(getApplicationContext(), "未确定约餐地址", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mNumberOfPeople.getText().toString().trim().equals("")) {
                //人数选择为空提示
                Toast.makeText(getApplicationContext(), "未选定约餐人数", Toast.LENGTH_SHORT).show();
                return false;
            } else if (mPhoneNumber.getText().toString().trim().equals("")) {
                //介绍为空提示
                Toast.makeText(getApplicationContext(), "建议填写约餐介绍", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }


        //菜系选择弹出框
        protected Dialog onCreateDialog(int id) {
            Dialog dialog = null;
           // String styleoffood = "";
            switch (id) {
                case STYLEOFFOOD_DIA:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    // 设置对话框的标题
                    AlertDialog.Builder setTitle1 = builder1.setTitle("菜系");
                    builder1.setSingleChoiceItems(R.array.StyleOfFood, 0, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           mFoodStyle.setText(getResources().getStringArray(R.array.StyleOfFood)[which]);
                        }
                    });

                    // 添加一个确定按钮
                    builder1.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    // 创建一个单选按钮对话框
                    dialog = builder1.create();
                    break;
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
        }



/*       @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
String TAG = "上传拖";
        Bitmap bitmap = null;
        ContentResolver resolver = getContentResolver();
        switch (requestCode) {

            case CROP_REQUEST_CODE:
                Log.i(TAG, "相册裁剪成功");
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
                bitmap bm = new bitmap();
                photo = bm.bitmapToBase64(bitmap);//通过base64编码转成string

           default:
               break;
        }
    }*/



    class releaseThread implements Runnable {

            String userId;
            String EatingTime;
            String Restaurant;
String minPeople;
            String maxPeople;
            String city;
            String street;
            String foodStyle;
            String holderTel;
            String OrderTimeValue;
        String photo;



            public releaseThread(String userId, String EatingTime, String Restaurant,
                               String foodStyle, String city, String street, String maxPeople, String minPeople, String holderTel, String OrderTimeValue,String photo) {
                this.userId = userId;
                this.EatingTime = EatingTime;
                this.Restaurant = Restaurant;
                this.maxPeople = maxPeople;
                this.city = city;
                this.minPeople = minPeople;
                this.street = street;
                this.foodStyle = foodStyle;
                this.holderTel = holderTel;
                this.OrderTimeValue = OrderTimeValue;
                this.photo= photo;
               }

            @Override
            public void run() {
                String url = getString(R.string.server_ip) + "AddOrder";
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = null;
                httpPost = new HttpPost(url);

                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("userId", userId));
                formparams.add(new BasicNameValuePair("eatingTime", EatingTime));
                formparams.add(new BasicNameValuePair("restanurant", Restaurant));
                formparams.add(new BasicNameValuePair("minNumber", minPeople));
                formparams.add(new BasicNameValuePair("maxNumber", maxPeople));
                formparams.add(new BasicNameValuePair("city", city));
                formparams.add(new BasicNameValuePair("address", street));
                formparams.add(new BasicNameValuePair("style", foodStyle));
                formparams.add(new BasicNameValuePair("telephone", holderTel));
                formparams.add(new BasicNameValuePair("orderTime", OrderTimeValue));
           //     formparams.add(new BasicNameValuePair("city", photo));


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
                            JSONObject jsondata = new JSONObject(json);
                            Bundle b = new Bundle();
                            b.putString("flag", jsondata.getString("flag"));
                            System.out.println(jsondata.getString("flag"));
                            Message msg = new Message();
                            msg.setData(b);
                            releaseDate.this.handler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }


}


