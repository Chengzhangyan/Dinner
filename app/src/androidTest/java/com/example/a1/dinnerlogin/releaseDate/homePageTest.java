package com.example.a1.dinnerlogin.releaseDate;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageButton;

import static org.junit.Assert.*;

import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.userInfo.userInfoEdit;
import com.robotium.solo.Solo;
/**
 * Created by admin on 2017/5/10.
 */
public class homePageTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public homePageTest(){
        super(homePage.class);
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

        boolean result1 = solo.searchText("首页");solo.searchText("goodsinfoListView");
        assertEquals("文本显示不全",expected,result1);

        ImageButton imageButton2 = (ImageButton)solo.getView(R.id.imageButton2);
        solo.clickOnView(imageButton2);
        solo.assertCurrentActivity("主页跳转失败",homePage.class);
    }

    public void testRelease()throws  Exception{
        ImageButton imageButton4 = (ImageButton)solo.getView(R.id.imageButton4);
        solo.clickOnView(imageButton4);
        solo.assertCurrentActivity("发布页面跳转失败",releaseDate.class);
    }

    public void testUserInfo()throws  Exception{
        ImageButton imageButton6 = (ImageButton)solo.getView(R.id.imageButton6);
        solo.clickOnView(imageButton6);
        solo.assertCurrentActivity("个人中心跳转失败",userInfoEdit.class);
    }
}