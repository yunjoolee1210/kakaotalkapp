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

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Context ctx = Login.this;

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etID = findViewById(R.id.id_input);
                EditText etPW = findViewById(R.id.pw_input);
                final String id_input = etID.getText().toString();
                final String pw_input = etPW.getText().toString();
                Intro.SQLiteHelper helper = new Intro.SQLiteHelper(ctx);
                new Intro.LoginService(){

                    @Override
                    public void execute() {

                    }
                }.execute();
            }
        });

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
            return super.getDatabase().rawQuery(String.format(" SELECT * FROM %s WHERE %s = '&s' AND " +
                    " %s = '%s';", Intro.MEM_TABLE, Intro.MEM_SEQ, seq, Intro.MEM_PW, pw)

                    ,null).moveToNext();
        }

    }

}
