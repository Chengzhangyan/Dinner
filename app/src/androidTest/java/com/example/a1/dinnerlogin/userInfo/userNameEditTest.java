package com.example.a1.dinnerlogin.userInfo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.example.a1.dinnerlogin.R;
import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by lenovo on 2017/6/5.
 */
public class userNameEditTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public userNameEditTest(){
        super(userNameEdit.class);
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

        boolean result1 = solo.searchText("用户中心");
        assertEquals("文本显示不全",expected,result1);

        boolean result2 = solo.searchButton("确定");solo.searchButton("返回");
        assertEquals("按钮显示不全",expected,result2);

        Button Button1 = (Button) solo.getView(R.id.name_edit_cansel_btn);
        solo.clickOnView(Button1);
        solo.assertCurrentActivity("返回个人信息跳转失败",userInfoEdit.class);
    }
    //正确输入（汉字）
    public void testValid()throws Exception{
        solo.enterText(0,"测试");
        solo.clickOnButton("确定");
    }

    //正确输入（英文）（首大写中间大写末端大写）
    public void testEnglish()throws Exception{
        solo.enterText(0,"TestTesT");
        solo.clickOnButton("确定");
    }
    //正确输入（数字）
    public void testNum()throws Exception{
        solo.enterText(0,"01234560");
        solo.clickOnButton("确定");
    }
    //正确输入（混杂）
    public void testMix()throws Exception{
        solo.enterText(0,"我12sdsd");
        solo.clickOnButton("确定");
    }
    //未输入（为空测试）
    public void testNull()throws Exception{
        solo.enterText(0,"");
        solo.clickOnButton("确定");
        assertTrue(solo.waitForText("昵称不能为空！"));
    }
}