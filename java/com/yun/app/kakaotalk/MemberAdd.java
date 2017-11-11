package com.yun.app.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yun.app.kakaotalk.Intro.QueryFactory;

public class MemberAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_add);

        final Context ctx = MemberAdd.this;
        /*EditText pw_input = findViewById(R.id.pw_input);*/
        final EditText name_input = findViewById(R.id.name_input);
        final EditText email_input = findViewById(R.id.email_input);
        final EditText phone_input = findViewById(R.id.phone_input);
        final EditText addr_input = findViewById(R.id.addr_input);
        Intro.SQLiteHelper helper = new Intro.SQLiteHelper(ctx);

        //친구추가 값 입력 후 등록
        findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final MemberItemAdd memberItemAdd = new MemberItemAdd(ctx);
                final Intro.Member insertMember = new Intro.Member();
                insertMember.name = name_input.getText().toString();
                insertMember.email = email_input.getText().toString();
                insertMember.phone = phone_input.getText().toString();
                insertMember.addr = addr_input.getText().toString();

                new Intro.AddService(){
                    @Override
                    public void execute() {
                        memberItemAdd.insert(insertMember);

                    }
                }.execute();

                startActivity(new Intent(ctx, MemberList.class));
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, MemberList.class);
                startActivity(intent);
            }
        });

    }

    private abstract class InsertQuery extends QueryFactory{
        Intro.SQLiteHelper helper;
        public InsertQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }

    private class MemberItemAdd extends InsertQuery{
        public MemberItemAdd(Context ctx) {
            super(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }

        public void insert(Intro.Member member){
            String sql = String.format(" INSERT INTO %s (%s, %s, %s, %s, %s, %s) " +
                    " VALUES ('%s', '%s', '%s', '%s', '%s', '%s'); ",
                    Intro.MEM_TABLE, Intro.MEM_PW, Intro.MEM_NAME, Intro.MEM_EMAIL,
                    Intro.MEM_PHONE, Intro.MEM_ADDR, Intro.MEM_PHOTO, "1",
                    member.name, member.email, member.phone, member.addr, "human"
            );

            this.getDatabase().execSQL(sql);//Insert는 close하지 않아도 됨

        }
    }
}
