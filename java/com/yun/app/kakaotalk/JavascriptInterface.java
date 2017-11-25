package com.yun.app.kakaotalk;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

public class JavascriptInterface {
    Context ctx;

    public JavascriptInterface(Context ctx) {
        this.ctx = ctx;
    }

    @android.webkit.JavascriptInterface
    public void showToast(String msg){
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
    }

    @android.webkit.JavascriptInterface
    public void sendSMS(String phone, String msg){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone,null, msg,null,null);
    }
}
