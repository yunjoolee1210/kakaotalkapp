package com.yun.app.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Context ctx = Login.this;
        final MemberExist exist = new MemberExist(ctx);

        //로그인 > ID, PW 클릭시 내장 DB 값 Search하여 처리
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText etID = findViewById(R.id.id_input);
                final EditText etPW = findViewById(R.id.pw_input);
                final String id_input = etID.getText().toString();
                final String pw_input = etPW.getText().toString();
                Intro.SQLiteHelper helper = new Intro.SQLiteHelper(ctx);

                new Intro.LoginService(){

                    @Override
                    public void execute() {
                        boolean loginOk = exist.execute(id_input, pw_input);
                        if(loginOk){
                            Toast.makeText(ctx, "LOGIN SUCCESS", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ctx, MemberList.class));
                        }else {
                            Toast.makeText(ctx, "LOGIN FAIL", Toast.LENGTH_LONG).show();
                            etID.setText("");
                            etPW.setText("");
                        }
                    }
                }.execute();
            }
        });

        //로그인 > Cancel 클릭시 Intro로 다시 리다이렉션
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,Intro.class);
                startActivity(intent);
            }
        });

    }

    private class LoginQuery extends Intro.QueryFactory{

        Intro.SQLiteHelper helper;

        public LoginQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class MemberExist extends LoginQuery{

        public MemberExist(Context ctx) {
            super(ctx);
        }

        public boolean execute(String seq, String pw){
            return super.getDatabase().rawQuery(String.format(" SELECT * FROM %s WHERE %s = '%s' AND " +
                    " %s = '%s';", Intro.MEM_TABLE, Intro.MEM_SEQ, seq, Intro.MEM_PW, pw)

                    ,null).moveToNext();
        }

    }

}
