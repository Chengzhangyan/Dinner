package com.example.a1.dinnerlogin.applyDate;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.menu;
import com.example.a1.dinnerlogin.userInfo.userAddressEdit;
import com.example.a1.dinnerlogin.userInfo.userInfoEdit;
import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by lenovo on 2017/6/5.
 */
public class checkApplyTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public checkApplyTest(){
        super(checkApply.class);
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

        boolean result1 = solo.searchText("申请信息");solo.searchText("申请人：");solo.searchText("餐厅：");solo.searchText("Canteen");
        solo.searchText("饭约时间：");solo.searchText("time");solo.searchText("申请信息：");solo.searchText("content");
        solo.searchText("申请状态：");solo.searchText("status");
        assertEquals("文本显示不全",expected,result1);

        boolean result2 = solo.searchButton("nickname");solo.searchButton("查看饭约信息");solo.searchButton("拒绝");solo.searchButton("允许加入");
        assertEquals("按钮显示不全",expected,result2);

        Button Button1 = (Button) solo.getView(R.id.back_btn);
        solo.clickOnView(Button1);
        solo.assertCurrentActivity("返回首页跳转失败",menu.class);
    }
    public void testSure()throws Exception{
        solo.clickOnButton("允许加入");
    }
    public void testRefuse()throws Exception{
        solo.clickOnButton("拒绝");
    }
    public void testName()throws Exception{
        solo.clickOnButton("nickname");
    }
    public void testView()throws Exception{
        solo.clickOnButton("查看饭约信息");
        solo.assertCurrentActivity("跳转详情失败",checkOrderInfo.class);
    }
}