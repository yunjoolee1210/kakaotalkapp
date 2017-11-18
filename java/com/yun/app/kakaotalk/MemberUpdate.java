package com.yun.app.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context ctx = MemberUpdate.this;

        final String spec = this.getIntent().getStringExtra("spec");
        final String[] arr = spec.split(",");

        //final String seq = this.getIntent().getStringExtra("seq");


        //속성 객체화
        final EditText update_name = findViewById(R.id.update_name);
        final EditText update_email = findViewById(R.id.update_email);
        final EditText update_phone = findViewById(R.id.update_phone);
        final EditText update_addr = findViewById(R.id.update_addr);
        final ImageView update_image = findViewById(R.id.update_image);

        Log.d("@MemberUpdate seq값 확인: ", arr[0]);
        Log.d("@MemberUpdate name값 확인: ", arr[1]);
        Log.d("@MemberUpdate email값 확인: ", arr[2]);
        Log.d("@MemberUpdate phone값 확인: ", arr[3]);
        Log.d("@MemberUpdate addr값 확인: ", arr[4]);

        update_name.setHint(arr[1]);
        update_email.setHint(arr[2]);
        update_phone.setHint(arr[3]);
        update_addr.setHint(arr[4]);

        //이미지 시퀀스 값에 매칭되는 이미지로 불러오기
        int profile = getResources().getIdentifier(this.getPackageName()
                +":drawable/human", null, null);

        update_image.setImageDrawable(getResources()
                .getDrawable(profile, ctx.getTheme()));


        findViewById(R.id.confirmUpdate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MemberItemUpdate update = new MemberItemUpdate(ctx);
                new Intro.UpdateService(){

                    @Override
                    public void execute() {
                        Intro.Member updateMember = new Intro.Member();


                        /*if(update_name.getText().toString().equals("")){
                            t_name[1];
                        }else{
                            t_name=update_name.getText().toString();
                        }*/
                        updateMember.seq = Integer.parseInt(arr[0]);
                        updateMember.name = (update_name.getText().toString().equals(""))?
                                arr[1]: update_name.getText().toString();
                        updateMember.email = (update_email.getText().toString().equals(""))?
                                arr[2]: update_email.getText().toString();
                        updateMember.phone = (update_phone.getText().toString().equals(""))?
                                arr[3]: update_phone.getText().toString();
                        updateMember.addr = (update_addr.getText().toString().equals(""))?
                                arr[4]: update_addr.getText().toString();
                       update.update(updateMember);
                    }
                }.execute();
                Intent intent = new Intent(ctx, MemberDetail.class);
                intent.putExtra("seq", arr[0]);
                startActivity(intent);
            }
        });

        //Cancel 클릭시 MemberDetail로 리다이렉션
        findViewById(R.id.cancelUpdate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,MemberList.class);
                startActivity(intent);
            }
        });
    }

    private abstract class UpdateQuery extends Intro.QueryFactory{
        Intro.SQLiteHelper helper;

        public UpdateQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }

    }

    private class MemberItemUpdate extends UpdateQuery{

        public MemberItemUpdate(Context ctx) { super(ctx); }

        @Override
        public SQLiteDatabase getDatabase() { return super.getDatabase();}

        public void update(Intro.Member member){

            String sql = String.format(" UPDATE %s " +
                    " SET %s ='%s', %s ='%s', %s ='%s', %s ='%s' " +
                    " WHERE %s = '%s' ; " ,
                    Intro.MEM_TABLE, Intro.MEM_NAME, member.name, Intro.MEM_EMAIL, member.email,
                    Intro.MEM_PHONE, member.phone, Intro.MEM_ADDR, member.addr,
                    Intro.MEM_SEQ, member.seq);

            this.getDatabase().execSQL(sql);
        }


    }
}
