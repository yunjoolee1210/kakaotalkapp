package com.yun.app.kakaotalk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

public class Phone {
    private Context ctx;
    private Activity act;

    public Phone(Context ctx, Activity act) {
        this.ctx = ctx;
        this.act = act;
    }

    public void dial(String phoneNo){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNo));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    //전화걸기 메서드
    public void call(String phoneNo){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNo));
        if(ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE)
            !=PackageManager.PERMISSION_GRANTED){
    ActivityCompat.requestPermissions(act, new String[]{}, 2);
    return;
        }
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
}
