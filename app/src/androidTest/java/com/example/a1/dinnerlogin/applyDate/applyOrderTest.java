package com.example.a1.dinnerlogin.applyDate;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import com.robotium.*;

import static org.junit.Assert.*;

public class applyOrderTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public applyOrderTest() {
        super(applyOrder.class);
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


        solo.enterText(0,"14301039");


        /*点击登录按钮*/
        solo.clickOnButton("申请");

        /*检查是否成功跳转*/
        solo.assertCurrentActivity("登陆跳转失败","menu");



        /*
        solo.waitForDialogToOpen();
        solo.clickOnText("qa");
        solo.clickOnButton("OK");
*/

        //assert home screen finished loading.
        // assertTrue(solo.waitForText("Diapering"));
    }





}