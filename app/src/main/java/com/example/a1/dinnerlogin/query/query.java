package com.example.a1.dinnerlogin.query;

/**
 * Created by 1 on 2017/6/2.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import com.example.a1.dinnerlogin.R;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView.OnItemSelectedListener;
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
import com.example.a1.dinnerlogin.menu;
import java.util.ArrayList;
import java.util.List;

public class query extends AppCompatActivity {
    Spinner mfood;
    //Spinner mCity;
    TextView queryText;
    private Button mBack;
    Button queryBtn;
    private  ArrayAdapter<String> adpt2;
    public static queryWord queryWord;
ArrayList<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

mfood = (Spinner)findViewById(R.id.food_spinner);
        queryText=(TextView) findViewById(R.id.querytext);
        queryBtn=(Button) findViewById(R.id.querybtn);

mBack = (Button)findViewById(R.id.btn_back);
        mfood.setSelection(0);

        list.add("鲁菜");
        list.add("川菜");
        list.add("粤菜");
        list.add("苏菜");
        list.add("闽菜");
        list.add("浙菜");
        list.add("湘菜");
        list.add("徽菜");
       adpt2 = new ArrayAdapter<String>(this,R.layout.msg_menu_item,list);
        adpt2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mfood.setAdapter(adpt2);
        mfood.setSelection(0,true);

        mfood.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
        //璁剧疆鎸夐挳鐨勫搷搴斾簨浠讹紝鍗崇偣鍑绘寜閽紑濮嬫墽琛屽瓙绾跨▼

        queryBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                mBack.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent=new Intent(query.this,menu.class);
                        intent.putExtra("object",0);
                        startActivity(intent);
                        finish();
                    }
                });
                String queryipt=queryText.getText().toString();

                if (!queryipt.isEmpty()) {
/*                    QueryThread myThread = new QueryThread(queryipt);
                    new Thread(myThread).start();*/

                Intent intent=new Intent();
                    intent.putExtra("queryipt", queryipt);
                intent.setClass(query.this, queryresultActivity.class);
                startActivity(intent);
                }else {
                    Toast.makeText(query.this,"搜查项不能为空哦！",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

class SpinnerXMLSelectedListener implements OnItemSelectedListener{
 public void onItemSelected(AdapterView<?> arg0, View arg1,int position,long arg3 ){
     queryText.setText(adpt2.getItem(position).toString());
     mfood.setSelection(position,true);
     queryWord.setQueryWord(adpt2.getItem(position).toString());

 }
 public void onNothingSelected(AdapterView<?> arg0){

 }



}


/*


    class QueryThread implements Runnable {
        private String queryipt;
        public QueryThread(String queryipt){
            this.queryipt=queryipt;
        }

        public void run(){

            //鏈嶅姟鍣ㄧ殑璺緞锛屾牴鎹疄闄呮儏鍐垫洿鏀?

            String url="http://10.0.2.2:8080/dinner1/adddinner";
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=null;
            httpPost=new HttpPost(url);

            List<NameValuePair> formparams=new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("queryipt",queryipt));
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
                        JSONObject jsondata=new JSONObject(json);
                        Bundle b=new Bundle();
                        b.putString("flag",jsondata.getString("flag"));
                        Message msg=new Message();
                        msg.setData(b);
                        query.this.handler.sendMessage(msg);
                    }
                }


            } catch(Exception e){
                e.printStackTrace();
            }

        }
    }*/
}
