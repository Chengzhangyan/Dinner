package com.example.a1.dinnerlogin.query;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by lenovo on 2017/6/7.
 */
public class queryTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public queryTest(){
        super(query.class);
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

        boolean result1 = solo.searchText("搜索饭约");
        assertEquals("文本显示不全",expected,result1);

        boolean result2 = solo.searchButton("查询");
        assertEquals("按钮显示不全",expected,result2);
    }
}