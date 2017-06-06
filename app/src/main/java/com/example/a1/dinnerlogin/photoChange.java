package com.example.a1.dinnerlogin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by user on 2017/5/27.
 */

public class photoChange {
private Bitmap bm;
    private String str;
    //bitmap to base64

    public static String bitmapToBase64(Bitmap bitmap){
        String result = "";
        ByteArrayOutputStream bos = null;
        try{
            if (bitmap!=null){
                bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,90,bos);
                bos.flush();
                bos.close();

                byte[] bitmapByte = bos.toByteArray();
                result = Base64.encodeToString(bitmapByte,Base64.DEFAULT);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null!=null){
                try {
                    bos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return result;

    }

public static Bitmap base64ToBitmap(String base64String){
    byte[] bytes = Base64.decode(base64String,Base64.DEFAULT);
    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    return bitmap;

}

public void setPhotoString(Bitmap bm){
    str = bitmapToBase64(bm);

}

public String getPhotoString(){
    return str;
}


}
