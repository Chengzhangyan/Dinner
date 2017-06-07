package com.example.a1.dinnerlogin.query;

import android.test.ActivityInstrumentationTestCase2;
import com.example.a1.dinnerlogin.query.queryWord;
import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by lenovo on 2017/6/7.
 */
public class queryresultActivityTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public queryresultActivityTest(){
        super(queryresultActivity.class);
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