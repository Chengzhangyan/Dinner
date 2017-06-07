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
    public static FoodWord queryWord;
ArrayList<String> list = new ArrayList<>();
private static final String[] m= {"鲁菜","川菜","粤菜"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

mfood = (Spinner)findViewById(R.id.food_spinner);
        queryText=(TextView) findViewById(R.id.querytext);
        queryBtn=(Button) findViewById(R.id.querybtn);

mBack = (Button)findViewById(R.id.btn_back);
        mfood.setSelection(0);
       // list.add("鲁菜");
     /*   list.add("鲁菜");
        list.add("川菜");
        list.add("粤菜");
        list.add("苏菜");
        list.add("闽菜");
        list.add("浙菜");
        list.add("湘菜");
        list.add("徽菜");*/
       adpt2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
        adpt2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mfood.setAdapter(adpt2);
     //   mfood.setSelection(0,true);

    mfood.setOnItemSelectedListener(new SpinnerSelectedListener());

        mfood.setVisibility(View.VISIBLE);
            /*new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position,long arg3 ){
                String posi=list.get(position);
                queryText.setText(posi);
                System.out.println(posi);
                queryWord.setQueryWord(adpt2.getItem(position));
                System.out.println("queryword:"+queryWord.getQueryWord());
                //mfood.setSelection(position,true);


            }
            public void onNothingSelected(AdapterView<?> arg0){

            }




        }*/
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

    class SpinnerSelectedListener implements OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2,long arg3){
            queryText.setText("your choose is "+ m[arg2]);
        //    queryWord.setQueryWord(m[arg2]);
        }
        public void onNothingSelected(AdapterView<?> arg0){

        }

    }





}

