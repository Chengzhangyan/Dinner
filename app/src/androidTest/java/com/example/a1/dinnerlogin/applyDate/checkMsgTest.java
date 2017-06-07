package com.example.a1.dinnerlogin.applyDate;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by lenovo on 2017/6/7.
 */
public class checkMsgTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public checkMsgTest(){
        super(checkMsg.class);
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

    }
}