package com.example.a1.dinnerlogin.userInfo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.a1.dinnerlogin.R;
import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by lenovo on 2017/6/5.
 */
public class userGenderEditTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public userGenderEditTest(){
        super(userGenderEdit.class);
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

        boolean result1 = solo.searchText("修改性别");solo.searchText("汉子");solo.searchText("妹子");
        assertEquals("文本显示不全",expected,result1);

        boolean result2 = solo.searchButton("确定");solo.searchButton("返回");
        assertEquals("按钮显示不全",expected,result2);

        Button Button1 = (Button) solo.getView(R.id.gender_edit_cansel_btn);
        solo.clickOnView(Button1);
        solo.assertCurrentActivity("返回个人信息跳转失败",userInfoEdit.class);
    }
    //选择汉子
    public void testMan()throws Exception{
//        solo.clickOnRadioButton(0);
        solo.clickOnButton("汉子");
        solo.clickOnButton("确定");
    }
    //选择妹子
    public void testWoman()throws Exception{
//        solo.clickOnRadioButton(1);
        solo.clickOnButton("妹子");
        solo.clickOnButton("确定");
    }

}