package com.yun.app.kakaotalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Email {

    private Context ctx;
    private Activity act;

    public Email(Context ctx, Activity act){
        this.ctx = ctx;
        this.act = act;
    }

    public void sendEmail(String email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"+email));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hello!");
        intent.putExtra(Intent.EXTRA_TEXT,"Good buy!");
        ctx.startActivity(intent.createChooser(intent,"example"));

    }


}
