package com.example.a1.dinnerlogin.applyDate;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageButton;

import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.releaseDate.homePage;
import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by lenovo on 2017/6/7.
 */
public class applyMsgTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public applyMsgTest(){
        super(applyMsg.class);
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