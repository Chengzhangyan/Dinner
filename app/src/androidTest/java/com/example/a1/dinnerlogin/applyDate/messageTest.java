package com.example.a1.dinnerlogin.applyDate;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import com.robotium.*;

import static org.junit.Assert.*;

public class messageTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public messageTest() {
        super(message.class);
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


    public void testLogin() throws Exception {

        boolean expected = true;

        /*检查UI是否完整*/

/*robotium 语法*/
        boolean actual3 = solo.searchText("手机号:");solo.searchText("密码:");solo.searchText("约饭吧");
        assertEquals("文本显示不全",expected,actual3);

        boolean actual4 = solo.searchEditText("");solo.searchEditText("");
        assertEquals("文本框",expected,actual4);

        boolean actual1 = solo.searchButton("重置",0);solo.searchButton("登录",0);
        assertEquals("重置登录按钮未找到",expected,actual1);

        boolean actual2 = solo.searchButton("没有账号？注册",0);
        assertEquals("注册按钮未找到",expected,actual2);



        /*点击登录按钮*/
        solo.clickOnButton("登录");

        /*检查是否成功跳转*/
        solo.assertCurrentActivity("登陆跳转失败","userInfoEdit");



        /*
        solo.waitForDialogToOpen();
        solo.clickOnText("qa");
        solo.clickOnButton("OK");
*/

        //assert home screen finished loading.
        // assertTrue(solo.waitForText("Diapering"));
    }

    public void testTelIsEmpty()throws Exception{
        solo.enterText(0,"");
        solo.enterText(1,"123456");

        solo.clickOnButton("登录");
        assertTrue(solo.waitForText("账号不能为空！"));
    }

    public void testPwdIsEmpty() throws Exception{
        /*输入手机号，密码为空，检查是否提示为空*/
        solo.enterText(0,"14301039");
        solo.enterText(1,"");

        solo.clickOnButton("登录");
        assertTrue(solo.waitForText("密码不能为空！"));
    }


}