package com.example.a1.dinnerlogin.releaseDate;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageButton;

import com.example.a1.dinnerlogin.R;
import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by admin on 2017/5/10.
 */
public class orderInfoTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public orderInfoTest() {
        super(orderInfo.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testMain() throws Exception {
        boolean expected = true;

        boolean result1 = solo.searchText("饭约信息");
        solo.searchText("text8");
        solo.searchText("text1");
        solo.searchText("text2");
        solo.searchText("text5");
        solo.searchText("text4");
        solo.searchText("text6");
        solo.searchText("text7");
        assertEquals("文本显示不全", expected, result1);

        boolean result2 = solo.searchButton("返回");
        solo.searchButton("加入");
        assertEquals("按钮显示不全", expected, result2);

  /*点击返回按钮*/
        solo.clickOnButton("返回");

        /*检查是否成功跳转*/
        solo.assertCurrentActivity("返回跳转失败", "homePage");

        solo.waitForDialogToOpen();
    }

    public void testisJiaru() throws Exception {

        /*点击加入按钮*/
        solo.clickOnButton("加入");

        /*检查是否成功跳转*/
        solo.assertCurrentActivity("加入跳转失败", "applyOrder");

    }

    public void testHomePage()throws  Exception{
        ImageButton back = (ImageButton)solo.getView(R.id.button_back);
        solo.clickOnView(back);
        solo.assertCurrentActivity("返回跳转失败",homePage.class);
    }
}