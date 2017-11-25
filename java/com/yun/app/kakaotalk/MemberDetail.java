package com.yun.app.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import static com.yun.app.kakaotalk.Intro.MEM_ADDR;
import static com.yun.app.kakaotalk.Intro.MEM_EMAIL;
import static com.yun.app.kakaotalk.Intro.MEM_NAME;
import static com.yun.app.kakaotalk.Intro.MEM_PHONE;
import static com.yun.app.kakaotalk.Intro.MEM_PHOTO;
import static com.yun.app.kakaotalk.Intro.MEM_PW;
import static com.yun.app.kakaotalk.Intro.MEM_SEQ;
import static com.yun.app.kakaotalk.Intro.MEM_TABLE;
import com.yun.app.kakaotalk.Intro.Member;

import org.w3c.dom.Text;

public class MemberDetail extends AppCompatActivity {

    Phone phone;
    Email email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);

        final Context ctx = MemberDetail.this;

        //속성 객체화
      ImageView detail_image = findViewById(R.id.detail_image);
        TextView detail_name = findViewById(R.id.detail_name);
       TextView detail_email = findViewById(R.id.detail_email);
       TextView detail_phone = findViewById(R.id.detail_phone);
        TextView detail_addr = findViewById(R.id.detail_addr);

        phone = new Phone(ctx, this);
        email = new Email(ctx, this);

        //seq 값 받아서 할당해 넘기기
       final String seq = this.getIntent().getStringExtra("seq");

        Log.d("넘어온 seq값 확인: ", seq);

        final MemberItemDetail detail = new MemberItemDetail(ctx);

        final Member result = (Member) new Intro.DetailService() {
            @Override
            public Object execute() {
                return detail.detail(seq);
            }
        }.execute();
        detail_name.setText(result.name);
        detail_email.setText(result.email);
        detail_phone.setText(result.phone);
        detail_addr.setText(result.addr);

       final String spec = result.seq+","+result.name+","+result.email+","+
               result.phone+","+result.addr+","+"profile";

        Log.d("@MemberDetail spec값 확인: ", spec);

        //Lambda식 버튼 이벤트 거는 객체화 템플릿
        findViewById(R.id.dial_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone.dial(result.phone);
            }
        });

        findViewById(R.id.call_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone.call(result.phone);
            }
        });

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, MemberUpdate.class);
                intent.putExtra("spec", spec);
                startActivity(intent);
            }
        });


        findViewById(R.id.list_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, MemberList.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.sms_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx, Network.class));

            }
        });

        findViewById(R.id.email_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.sendEmail(result.email);
            }
        });

        findViewById(R.id.album_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx, Album.class));
            }
        });

     /*   findViewById(R.id.music_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        findViewById(R.id.map_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, Maps.class);
                intent.putExtra("addr",result.addr);
                startActivity(intent);
            }
        });
    }

    //추상 팩토리 패턴
    private abstract class DetailQuery extends Intro.QueryFactory{
        Intro.SQLiteHelper helper;

        public DetailQuery(Context ctx){
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class MemberItemDetail extends DetailQuery{


        public MemberItemDetail(Context ctx) {
            super(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }

        public Member detail(String seq){

            Member member = null;

            String sql = String.format(
                    "SELECT %s, %s, %s, %s, %s, %s, %s " +
                            " FROM %s " +
                            "WHERE %s = '%s' ",
                    MEM_SEQ, MEM_NAME, MEM_PW, MEM_EMAIL, MEM_ADDR, MEM_PHOTO, MEM_PHONE,
                    MEM_TABLE, MEM_SEQ, seq
            );

            Cursor cursor = this.getDatabase().rawQuery(sql, null);

            if(cursor.moveToNext()){
                member = new Member();
                member.seq = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEM_SEQ)));
                member.name = cursor.getString(cursor.getColumnIndex(MEM_NAME));
                member.pw = cursor.getString(cursor.getColumnIndex(MEM_PW));
                member.email = cursor.getString(cursor.getColumnIndex(MEM_EMAIL));
                member.addr = cursor.getString(cursor.getColumnIndex(MEM_ADDR));
                member.photo = cursor.getString(cursor.getColumnIndex(MEM_PHOTO));
                member.phone = cursor.getString(cursor.getColumnIndex(MEM_PHONE));
            }

            return member;
        }


    }

}
