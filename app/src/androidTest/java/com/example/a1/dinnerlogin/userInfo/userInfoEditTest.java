package com.example.a1.dinnerlogin.userInfo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.example.a1.dinnerlogin.R;
import com.example.a1.dinnerlogin.login.login;
import com.robotium.solo.Solo;

/**
 * Created by user on 2017/5/9.
 */
public class userInfoEditTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    public userInfoEditTest() {
        super(login.class);
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


        Button editGenderBtn = (Button)solo.getView(R.id.user_gender_edit_btn);
        Button editAreaBtn = (Button)solo.getView(R.id.user_address_edit_btn);
        Button editSchoolBtn = (Button)solo.getView(R.id.user_school_edit_btn);

        boolean expected = true;

        /*检查UI是否完整*/

/*robotium 语法*/
        boolean actual1 = solo.searchText("昵称");solo.searchText("性别");solo.searchText("地区");solo.searchText("学校");
        assertEquals("文本显示不全",expected,actual1);


        /*点击登录按钮*/

        solo.assertCurrentActivity("编辑昵称跳转失败",userNameEdit.class);

        solo.clickOnView(editGenderBtn);
        solo.clickOnView(editAreaBtn);
        solo.clickOnView(editSchoolBtn);

        /*检查是否成功跳转*/
        //  solo.assertCurrentActivity("登陆跳转失败",);
        /*
        solo.waitForDialogToOpen();
        solo.clickOnText("qa");
        solo.clickOnButton("OK");
        */

        //assert home screen finished loading.
        // assertTrue(solo.waitForText("Diapering"));
    }

    public void testEditName() throws Exception{
        Button editNameBtn = (Button)solo.getView(R.id.user_name_edit_btn);
        solo.clickOnView(editNameBtn);
        solo.assertCurrentActivity("编辑姓名跳转失败",userNameEdit.class);
    }

    public void testEditGender(){
        Button editGenderBtn = (Button)solo.getView(R.id.gender_edit_sure_btn);
        solo.clickOnView(editGenderBtn);
        solo.assertCurrentActivity("编辑性别跳转失败",userGenderEdit.class);
    }
    public void testEditAddress(){
        Button editAddressBtn = (Button)solo.getView(R.id.area_edit_sure_btn);
        solo.clickOnView(editAddressBtn);
        solo.assertCurrentActivity("编辑地区跳转失败",userAddressEdit.class);
    }

    public void testEditOrderSchool(){
        Button editSchool = (Button)solo.getView(R.id.gender_edit_sure_btn);
        solo.clickOnView(editSchool);
        solo.assertCurrentActivity("编辑学校跳转失败",userSchoolEdit.class);
    }
}