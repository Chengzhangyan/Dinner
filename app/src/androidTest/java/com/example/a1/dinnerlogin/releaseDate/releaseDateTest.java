package com.example.a1.dinnerlogin.releaseDate;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by admin on 2017/5/10.
 */


public class releaseDateTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public releaseDateTest(){
        super(releaseDate.class);
    }

    @Override
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public  void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    public void testOk()throws  Exception{
        boolean expected = true;

        boolean result1 = solo.searchText("时间");solo.searchText("餐厅名");solo.searchText("发布饭约");
        solo.searchText("菜系");solo.searchText("地区");solo.searchText("街道地址");solo.searchText("人数上限");solo.searchText("联系电话");
        assertEquals("文本显示不全",expected,result1);

        boolean result2 = solo.searchButton("发布");
        assertEquals("按钮显示不全",expected,result2);
//输入信息
        solo.enterText(0,"2017.11.22");
        solo.enterText(1,"家园");
        solo.enterText(2,"粤菜");
        solo.enterText(3,"海淀区");
        solo.enterText(4,"上园村三号");
        solo.enterText(5,"5");
        solo.enterText(6,"18511112568");
        // 点击发布按钮
        solo.clickOnButton("发布");
        //确认是否跳转成功
        // solo.assertCurrentActivity("注册跳转失败","homePage");

    }

    //不输入时间提示
    public void testTimesEmpty()throws Exception{
        solo.enterText(0,"");
        solo.enterText(1,"家园");
        solo.enterText(2,"粤菜");
        solo.enterText(3,"5");
        solo.enterText(4,"18511112568");
        solo.enterText(5,"海淀区");
        solo.enterText(6,"上园村三号");

        solo.clickOnButton("发布");
        assertTrue(solo.waitForText("未填约餐时间"));
    }
    //不输入餐厅提示
    public void testRestaurantEmpty()throws Exception{
        solo.enterText(0,"2017.12.8");
        solo.enterText(1,"");
        solo.enterText(2,"粤菜");
        solo.enterText(3,"5");
        solo.enterText(4,"18511112568");
        solo.enterText(5,"海淀区");
        solo.enterText(6,"上园村三号");

        solo.clickOnButton("发布");
        assertTrue(solo.waitForText("未填餐厅"));
    }
    //不输入菜系提示
    public void testStyleEmpty()throws Exception{
        solo.enterText(0,"2017.12.8");
        solo.enterText(1,"家园");
        solo.enterText(2,"");
        solo.enterText(3,"5");
        solo.enterText(4,"18511112568");
        solo.enterText(5,"海淀区");
        solo.enterText(6,"上园村三号");

        solo.clickOnButton("发布");
        assertTrue(solo.waitForText("未选择菜系"));
    }

    //不输入地区提示
    public void testAreaEmpty()throws Exception{
        solo.enterText(0,"2017.12.8");
        solo.enterText(1,"家园");
        solo.enterText(2,"粤菜");
        solo.enterText(3,"5");
        solo.enterText(4,"18511112568");
        solo.enterText(5,"");
        solo.enterText(6,"上园村三号");

        solo.clickOnButton("发布");
        assertTrue(solo.waitForText("未确定越约餐地址"));
    }


    //不输入具体地址提示
    public void testAddressEmpty()throws Exception{
        solo.enterText(0,"2017.12.8");
        solo.enterText(1,"家园");
        solo.enterText(2,"粤菜");
        solo.enterText(3,"5");
        solo.enterText(4,"18511112568");
        solo.enterText(5,"海淀区");
        solo.enterText(6,"");

        solo.clickOnButton("发布");
        assertTrue(solo.waitForText("未确定越约餐地址"));
    }

    //不输入约餐地址
    public void testPeopleEmpty()throws Exception{
        solo.enterText(0,"2017.12.8");
        solo.enterText(1,"家园");
        solo.enterText(2,"粤菜");
        solo.enterText(3,"");
        solo.enterText(4,"18511112568");
        solo.enterText(5,"海淀区");
        solo.enterText(6,"上园村三号");

        solo.clickOnButton("发布");
        assertTrue(solo.waitForText("未选定约餐人数"));
    }

    //不输入联系电话
    public void testIntroductionEmpty()throws Exception{
        solo.enterText(0,"2017.12.8");
        solo.enterText(1,"家园");
        solo.enterText(2,"粤菜");
        solo.enterText(3,"");
        solo.enterText(4,"18511112568");
        solo.enterText(5,"海淀区");
        solo.enterText(6,"上园村三号");

        solo.clickOnButton("发布");
        assertTrue(solo.waitForText("建议填写约餐介绍"));
    }
}
