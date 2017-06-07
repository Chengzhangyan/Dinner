package com.example.a1.dinnerlogin.query;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.applyDate.applyOrder;
import com.example.a1.dinnerlogin.query.queryresultActivity;
import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by lenovo on 2017/6/7.
 */
public class OrderInfoTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public OrderInfoTest(){
        super(OrderInfo.class);
    }

    @Override
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public  void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
    public void testMain()throws  Exception{
        boolean expected = true;

        boolean result1 = solo.searchText("饭约信息");solo.searchText("发起人");solo.searchText("nickname");solo.searchText("饭约时间");
        solo.searchText("time");solo.searchText("餐厅");solo.searchText("canteen");solo.searchText("人数下限");solo.searchText("minnumber");
        solo.searchText("人数上限");solo.searchText("maxnumber");solo.searchText("地区");solo.searchText("city");
        solo.searchText("详细地址");solo.searchText("address");solo.searchText("菜系");solo.searchText("style");
        solo.searchText("tel");solo.searchText("当前已加入用户");solo.searchText("style");
        assertEquals("文本显示不全",expected,result1);


        Button Button2 = (Button) solo.getView(R.id.join);
        solo.clickOnView(Button2);
        solo.assertCurrentActivity("加入跳转失败",applyOrder.class);
    }
}