package com.example.a1.dinnerlogin.query;

/**
 * Created by 1 on 2017/6/2.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.IntegerRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.app.ListActivity;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Map;
import java.util.HashMap;
import android.widget.SimpleAdapter;
import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.query.query;
import com.example.a1.dinnerlogin.userInfo.userInfoEdit;
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
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.example.a1.dinnerlogin.releaseDate.*;
import com.example.a1.dinnerlogin.applyDate.*;
import com.example.a1.dinnerlogin.login.*;
import com.example.a1.dinnerlogin.userInfo.*;
import android.app.ActivityGroup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.widget.Toast;
import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.releaseDate.homePage;
import com.example.a1.dinnerlogin.releaseDate.orderId;
import com.example.a1.dinnerlogin.menu;
import com.example.a1.dinnerlogin.releaseDate.orderInfo;

/**
 * Created by zhanglan on 2017/5/24.
 */

public class queryresultActivity extends Activity {
    private Button mBack;
    private TextView mResult;

private int count = 1;
    private SimpleAdapter adpt;
    private boolean is_divPage;//是否进行分页
    private final static int REQUEST_CODE=1;
    private int[] imgIds = new int[]{R.mipmap.head_05, R.mipmap.head_06,
            R.mipmap.head_07,R.mipmap.head_05, R.mipmap.head_06, R.mipmap.head_07,R.mipmap.head_05,R.mipmap.head_05};
    List<Map<String,Object>>datalist=new ArrayList<Map<String,Object>>();
    public static orderId ord =new orderId();
    //bitmap bmp = new bitmap();
    List<String> list = new ArrayList<>();/*list存储获得的orderid*/

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            Bundle b=msg.getData();
            int listnum = Integer.valueOf(b.getString("listNum"));
            for (int i=1;i<=listnum;i++){

                Map<String,Object> map = new HashMap<String,Object>();/*把数据放入item*/
                map.put("orderId",b.getString("orderId"+i));/*把获得的信息放入map*/

                map.put("eating_time",b.getString("eatingTime"+i));
                map.put("curr_number",b.getString("currnumber"+i));
                map.put("restaurant",b.getString("restaurant"+i));
                System.out.println(map + "count "+ count);

                map.put("head_icon",imgIds[count]);
                datalist.add(map);/*map内的数据放入一个数组list，第一个map存入的位置是datalist.get（0）*/
                System.out.println("datalist is   "+datalist);
                list.add(b.getString("orderId"+i));
//    System.out.println("datalist : "+datalist.get(i).toString());
            }

            count++;
            adpt.notifyDataSetChanged();
        }

    };


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.queryresult);

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
        ListView lv=(ListView) findViewById(R.id.home_lv);
        mBack  = (Button)findViewById(R.id.btn_back) ;
        mResult=(TextView)findViewById(R.id.querytext) ;


//testhome();

        adpt =  new SimpleAdapter(this,datalist,R.layout.item_item,new String[]{"head_icon","eating_time",
                "curr_number","restaurant",},new int[]{R.id.head_icon,R.id.eating_time,R.id.curr_number,R.id.restaurant,
        });

        lv.setAdapter(adpt);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*点击事件*/
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ord.setOrderid(list.get(position));
                Intent intent_to_orderinfo = new Intent(queryresultActivity.this,OrderInfo.class);
                startActivity(intent_to_orderinfo);
                finish();
            }
        });

        lv.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    Toast.makeText(queryresultActivity.this, "正在获取数据......", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount== totalItemCount);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent=new Intent(queryresultActivity.this,query.class);
                startActivity(intent);
                finish();
            }
        });

        mResult.setText(query.queryWord.getQueryWord());
QueryThread myThread = new QueryThread(count);
        new Thread(myThread).start();

    }

    @Override
    public void onRestart(){
        super.onRestart();
    }


    class QueryThread implements Runnable {

        private int page;
        public QueryThread( int page){

            this.page = page;
        }

        public void run(){

            //鏈嶅姟鍣ㄧ殑璺緞锛屾牴鎹疄闄呮儏鍐垫洿鏀?

            String url=getString(R.string.server_ip) + "OrderList2";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=null;
            httpPost=new HttpPost(url);

            List<NameValuePair> formparams=new ArrayList<NameValuePair>();
            //formparams.add(new BasicNameValuePair("style",query.queryWord.getQueryWord()));
            formparams.add(new BasicNameValuePair("page",String.valueOf(page)));
            //formparams.add(new BasicNameValuePair("userid",login.u.getUserid()));
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
                        int listnum = Integer.valueOf(jsondata4.getString("listNum"));
                        b4.putString("listNum", jsondata4.getString("listNum"));
                        for (int i=1;i<=listnum;i++) {

                            b4.putString("orderId"+i, jsondata4.getString("orderId"+i));
//                           b4.putString("nickname"+i, jsondata4.getString("nickname"+i));
                            b4.putString("eatingTime"+i, jsondata4.getString("eatingTime"+i));
                            b4.putString("restaurant"+i, jsondata4.getString("restaurant"+i));
                            //     b4.putString("photo"+i, jsondata4.getString("photo"+i));
                            b4.putString("currnumber"+i, jsondata4.getString("number"+i));
//System.out.println();
                        }
                        Message msg=new Message();
                        msg.setData(b4);
                        queryresultActivity.this.handler.sendMessage(msg);
                    }
                }


            } catch(Exception e){
                e.printStackTrace();
            }

        }
    }




    class ShowResultThread implements Runnable {

        //private String ipt;
        public ShowResultThread(){

        }

        public void run(){


            //瀵瑰簲鐨勬湇鍔″櫒鍦板潃锛屼笉鍚屾満鍣ㄤ箣闂磋杩涜鐩稿簲鐨勬洿鏀?
                  String url="http://10.0.2.2:8080/dinner1/adddinner";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=null;
            httpPost=new HttpPost(url);

            List<NameValuePair> formparams=new ArrayList<NameValuePair>();
            //formparams.add(new BasicNameValuePair("inputText",ipt));

            //姝ゅ鍙紶鍙笉浼?            //formparams.add(new BasicNameValuePair("userid",login.u.getUserid()));
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

                        //从服务器端接收数据
                        /*
                        b.putString("orderId1",jsondata.getString("orderId1"));
                        b.putString("orderName1",jsondata.getString("orderName1"));
                        b.putString("orderContent1",jsondata.getString("orderContent1"));

                        b.putString("orderId2",jsondata.getString("orderId2"));
                        b.putString("orderName2",jsondata.getString("orderName2"));
                        b.putString("orderContent2",jsondata.getString("orderContent2"));

                        b.putString("orderId3",jsondata.getString("orderId3"));
                        b.putString("orderName3",jsondata.getString("orderName3"));
                        b.putString("orderContent3",jsondata.getString("orderContent3"));

                        */
                        //查询返回来的结果数
                        int listResultNum = Integer.valueOf(jsondata.getString("listResultNum"));
                        b.putString("listResultNum", jsondata.getString("listResultNum"));

                        //使用循环依次接受的数据
                        for (int i=1;i<=listResultNum;i++) {

                            b.putString("orderId"+i, jsondata.getString("orderId"+i));
                            b.putString("nickname"+i, jsondata.getString("nickname"+i));
                            b.putString("orderContent"+i, jsondata.getString("orderContent"+i));
                           // b4.putString("restaurant"+i, jsondata4.getString("restaurant"+i));
                            //     b4.putString("photo"+i, jsondata4.getString("photo"+i));
                           // b4.putString("currnumber"+i, jsondata4.getString("number"+i));
//System.out.println();
                        }


                        Message msg=new Message();
                        msg.setData(b);
                        queryresultActivity.this.handler.sendMessage(msg);
                    }
                }


            } catch(Exception e){
                e.printStackTrace();
            }

        }
    }


}
