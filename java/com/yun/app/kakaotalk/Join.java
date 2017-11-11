package com.yun.app.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Join extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        final Context ctx = Join.this;

        //1) 회원가입 값 입력 후 등록
        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText pw_input = findViewById(R.id.pw_input);
                EditText name_input = findViewById(R.id.name_input);
                EditText

                Intro.SQLiteHelper helper = new Intro.SQLiteHelper(ctx);

                new Intro.AddService(){
                    @Override
                    public void execute() {

                    }
                }.execute();


            }
        });



        //2) 회원가입 > Cancel 클릭시 Intro로 다시 리다이렉션
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,Intro.class);
                startActivity(intent);
            }
        });
    }

    private class JoinQuery extends Intro.QueryFactory{

        Intro.SQLiteHelper helper;

        public JoinQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }

    private class JoinMember extends JoinQuery{
        public JoinMember(Context ctx) {
            super(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }

        public void execute(){
            String sql = String.format("");
            this.getDatabase().execSQL(sql);
        }
    }
}
