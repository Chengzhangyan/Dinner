package com.example.a1.dinnerlogin.applyDate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.userInfo.userInfoEdit;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.cache.Resource;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.example.a1.dinnerlogin.releaseDate.orderId;
import com.example.a1.dinnerlogin.releaseDate.homePage;
import com.example.a1.dinnerlogin.menu;
//import com.example.a1.dinnerlogin.bitmap;
/**
 * Created by user on 2017/5/10.
 */

public class message extends Activity {
    private boolean is_divPage;//是否进行分页
    private String photo;
   // private bitmap bmp = new bitmap();

    private int[] imgIds = new int[]{R.mipmap.head_05, R.mipmap.head_06,
            R.mipmap.head_07,R.mipmap.head_05,R.mipmap.head_07};
    List<Map<String,Object>> datalist=new ArrayList<Map<String,Object>>();
    List<Map<String,Bitmap>> headlist=new ArrayList<Map<String,Bitmap>>();

    private final static int STYLEOFFOOD_DIA = 2;
    List<String> list = new ArrayList<>();/*list存储获得的orderid*/


    public static orderId ord =new orderId();
    public int count = 0;

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
            for (int i=1;i<6;i++) {
                Map<String, Object> map = new HashMap<String, Object>();/*把数据放入item*/
             //   Map<String, Bitmap> headmap = new HashMap<String, Bitmap>();/*把数据放入item*/
                map.put("nickname", b.getString("nickname"+i));/*把获得的信息放入map*/
                map.put("apply_time", b.getString("apply_time"+i));
                map.put("apply_content", b.getString("apply_content"+i));
                map.put("applyerId", b.getString("applyerId"+i));
               map.put("head_icon", imgIds[count]);
              //  headmap.put("head_icon",bmp.base64ToBitmap(b.getString("photo"+i)));
//headlist.add(headmap);
                datalist.add(map);/*map内的数据放入一个数组list，第一个map存入的位置是datalist.get（0）*/
                list.add(b.getString("orderId"+i));
            }
            count++;
        }

    };

    Handler handler1=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
           refresh();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

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


        ListView lv=(ListView) findViewById(R.id.home_lv);

        //InputStream is = getResources().openRawResource(R.drawable.head_07);
       // Bitmap b = BitmapFactory.decodeStream(is);
        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.head_07);
      //  testhome(bmp.bitmapToBase64(b));
//testhome();
            msgThread myThread = new msgThread(count);
            new Thread(myThread).start();

        final SimpleAdapter head_adpt = new SimpleAdapter(this,headlist,R.layout.news_item,new String[]{"head_icon" },new int[]{R.id.head_icon });
        final SimpleAdapter adpt =new SimpleAdapter(this,datalist,R.layout.news_item,new String[]{ "head_icon","nickname","apply_time",
                "apply_content"},new int[]{ R.id.head_icon,R.id.nickname,R.id.apply_time,R.id.apply_content});
        lv.setAdapter(adpt);
        lv.setAdapter(head_adpt);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    /*点击事件*/
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent_to_orderinfo = new Intent(message.this,menu.class);
                        intent_to_orderinfo.putExtra("object",1);
                        switch (position){
                            case 0:
                               // ord.setOrderid(list.get(0));/*把第一个item的orderid存储*/
                                onCreateDialog( datalist.get(0).get("nickname").toString(),datalist.get(0).get("apply_time").toString(),
                                        datalist.get(0).get("apply_content").toString(),datalist.get(0).get("applyerId").toString());
                                break;
                            case 1:
                                onCreateDialog( datalist.get(1).get("nickname").toString(),datalist.get(1).get("apply_time").toString(),
                                        datalist.get(1).get("apply_content").toString(),datalist.get(0).get("applyerId").toString());
                                break;
                            case 2:
                                onCreateDialog( datalist.get(2).get("nickname").toString(),datalist.get(2).get("apply_time").toString(),
                                        datalist.get(2).get("apply_content").toString(),datalist.get(0).get("applyerId").toString());
                                break;
                            case 3:
                                onCreateDialog( datalist.get(3).get("nickname").toString(),datalist.get(3).get("apply_time").toString(),
                                        datalist.get(3).get("apply_content").toString(),datalist.get(3).get("applyerId").toString());
                                break;
                            case 4:
                                onCreateDialog( datalist.get(4).get("nickname").toString(),datalist.get(4).get("apply_time").toString(),
                                        datalist.get(4).get("apply_content").toString(),datalist.get(4).get("applyerId").toString());
                                break;

                        }}
                });

        lv.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE){
                    Toast.makeText(message.this, "正在获取数据......", Toast.LENGTH_SHORT).show();
                    msgThread myThread = new msgThread(count);
                    new Thread(myThread).start();
                    adpt.notifyDataSetChanged();
                    Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.head_07);
                    //testhome(bmp.bitmapToBase64(b));
                   // testhome();
                 //   msgThread myThread = new msgThread(count);
                    new Thread(myThread).start();
                }else if (!is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE){
                    Toast.makeText(message.this, "meila......", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount== totalItemCount);
            }
        });

        }

    public void testhome(){
        for(int i =0;i<5;i++) {
            Map<String,Object> map = new HashMap<String,Object>(); /*把数据放入item*/
            //Map<String, Bitmap> headmap = new HashMap<String, Bitmap>();/*把数据放入item*/
            map.put("nickname",i); /*把获得的信息放入map*/
            map.put("apply_time","apply_time");
            map.put("apply_content","apply_content");
            map.put("head_icon",imgIds[i]);
//            headmap.put("head_icon",bmp.base64ToBitmap(b));
            list.add("1314234235");
        //    headlist.add(headmap);
            datalist.add(map);
        }

    }

    protected Dialog onCreateDialog(String nickname,String apply_time,String apply_content,final String  applyerId) {
        Dialog dialog ;
        // String styleoffood = "";

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                // 设置对话框的标题
             builder1.setTitle("查看申请");
        builder1.setMessage("申请人："+nickname);
        builder1.setMessage("申请时间："+apply_time);
        builder1.setMessage("申请信息："+apply_content);

                // 添加一个确定按钮
                builder1.setPositiveButton(" 允许通过 ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        agreeThread myThread = new agreeThread("true",applyerId);
                        new Thread(myThread).start();
                   // refresh();
                    }
                });

                builder1.setNegativeButton("拒绝加入",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        agreeThread myThread = new agreeThread("false",applyerId);
                        new Thread(myThread).start();
                     //   refresh();
                    }
                });
                // 创建一个单选按钮对话框
                dialog = builder1.create();


        return dialog;
    }


    public void refresh(){
        onCreate(null);
    }

    class msgThread implements Runnable {
        private String getMsgInfo = "true" ;
        private String page;



        public msgThread(int page){

            this.page = String.valueOf(page);
        }

        @Override
        public void run(){
            String url=getString(R.string.server_ip) + "ShowOrderInfo";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=null;
            httpPost=new HttpPost(url);

            List<NameValuePair> formparams=new ArrayList<NameValuePair>();
           // formparams.add(new BasicNameValuePair("getOrderInfo",getMsgInfo));
            formparams.add(new BasicNameValuePair("page",page));
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
                        Bundle b4=new Bundle();
                        for (int i=1;i<7;i++) {
                            b4.putString("orderId"+i, jsondata4.getString("orderId"+i));
                            b4.putString("nickname"+i, jsondata4.getString("nickname"+i));//申请人的名字
                            b4.putString("applyerId"+i, jsondata4.getString("applyerId"+i));//申请人id
                            //b4.putString("orderTime",jsondata4.getString("orderTime"));
                            b4.putString("apply_time"+i, jsondata4.getString("apply_time"+i));
                            b4.putString("photo"+i, jsondata4.getString("photo"+i));
                            b4.putString("apply_content"+i, jsondata4.getString("apply_content"+i));
                        }
                        //  b4.putString("SOrderSituation",jsondata4.getString("SOrderSituation"));
                        //给每个listview赋值

                        Message msg4 = new Message();
                        msg4.setData(b4);
                        message.this.handler.sendMessage(msg4);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    class agreeThread implements Runnable {

        private String applyResult ;
        private String applyerId ;
        public agreeThread(String applyResult,String applyerId){
        this.applyResult = applyResult;
            this.applyerId = applyerId;
        }

        @Override
        public void run(){
            String url=getString(R.string.server_ip) + "ShowOrderInfo";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=null;
            httpPost=new HttpPost(url);

            List<NameValuePair> formparams=new ArrayList<NameValuePair>();
          // formparams.add(new BasicNameValuePair("applyId",applyId));
            formparams.add(new BasicNameValuePair("applyResult",applyResult));
            formparams.add(new BasicNameValuePair("applyerId",applyerId));
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
                        Bundle b4=new Bundle();
                        b4.putString("applyerId",jsondata4.getString("applyerId"));
                        b4.putString("orderId",jsondata4.getString("orderId"));
                        b4.putString("nickname",jsondata4.getString("nickname"));
                        //b4.putString("orderTime",jsondata4.getString("orderTime"));
                        b4.putString("apply_time",jsondata4.getString("apply_time"));
                        b4.putString("apply_content",jsondata4.getString("apply_content"));

                        //  b4.putString("SOrderSituation",jsondata4.getString("SOrderSituation"));
                        //给每个listview赋值

                        Message msg4 = new Message();
                        msg4.setData(b4);
                        message.this.handler1.sendMessage(msg4);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    }







