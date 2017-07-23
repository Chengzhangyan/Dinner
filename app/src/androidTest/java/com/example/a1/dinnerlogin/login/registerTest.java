package com.example.a1.dinnerlogin.login;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import com.robotium.*;
import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * Created by user on 2017/5/9.
 */
public class registerTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public registerTest() {
        super(register.class);
    }

    @Override
    public void setUp() throws Exception {
        // super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());

    }

    @Override
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    public void testRegister() throws Exception {
        boolean expected = true;

        /*检查UI是否完整*/

/*robotium 语法*/
        boolean actual3 = solo.searchText("手机号:");solo.searchText("密码:");solo.searchText("确认密码：");
        assertEquals("文本显示不全",expected,actual3);

        boolean actual4 = solo.searchEditText("");solo.searchEditText("");solo.searchEditText("");
        assertEquals("文本框不全",expected,actual4);

        boolean actual1 = solo.searchButton("取消",0);solo.searchButton("确认",0);
        assertEquals("取消和确认按钮未找到",expected,actual1);

 /*输入正确手机号和密码*/
        solo.enterText(0,"13822737783");
        solo.enterText(1,"123456");
        solo.enterText(2,"123456");

        /*点击登录按钮*/
        solo.clickOnButton("确认");

        /*检查是否成功跳转*/
        solo.assertCurrentActivity("注册跳转失败","menu");

        /*
        solo.waitForDialogToOpen();
        solo.clickOnText("qa");
        solo.clickOnButton("OK");
*/

        //assert home screen finished loading.
        // assertTrue(solo.waitForText("Diapering"));


    }

    public void isCanselWork() throws Exception {

        /*点击登录按钮*/
        solo.clickOnButton("取消");

        /*检查是否成功跳转*/
        solo.assertCurrentActivity("返回跳转失败","login");

    }
}