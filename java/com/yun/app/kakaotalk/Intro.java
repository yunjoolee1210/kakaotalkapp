package com.yun.app.kakaotalk;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        final Context ctx = Intro.this;

        SQLiteHelper helper = new SQLiteHelper(ctx);

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,Login.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.join_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,Join.class);
                startActivity(intent);
            }
        });

    }

    static String DB_Name = "hanbit.db";
    static String MEM_TABLE = "Member";
    static String MEM_SEQ = "seq";
    static String MEM_NAME = "name";
    static String MEM_PW = "pw";
    static String MEM_EMAIL = "email";
    static String MEM_PHONE = "phone";
    static String MEM_ADDR = "addr";
    static String MEM_PHOTO = "photo";
    static class Member{int seq; String name, pw, email, phone, addr, photo;}
    static interface LoginService{public void execute();}
    static interface AddService{public void execute();} //Join
    static interface ListService{public void execute();}
    static interface DetailService{public void execute();}
    static interface UpdateService{public void execute();}
    static interface DeleteService{public void execute();}

    static abstract class QueryFactory{
        Context ctx;
        public QueryFactory(Context ctx) {this.ctx = ctx;}
        public abstract SQLiteDatabase getDatabase();
    }

    static class SQLiteHelper extends SQLiteOpenHelper{

        public SQLiteHelper(Context ctx) {
            super(ctx, DB_Name, null, 1);
            this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(String.format(" CREATE TABLE IF NOT EXISTS %s " +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT ); )",
                    MEM_TABLE,MEM_SEQ,MEM_NAME,MEM_PW,MEM_EMAIL,MEM_PHONE,MEM_ADDR,MEM_PHOTO
                    ));

 /*           for(int i=0;i<5;i++){
                db.execSQL(String.format(" INSERT INTO %s (%s,%s,%s,%s,%s,%s) " +
                        " VALUES ('%s','%s','%s','%s','%s','%s')",
                        MEM_TABLE,MEM_NAME,MEM_PW,MEM_EMAIL,MEM_PHONE,MEM_ADDR,MEM_PHOTO,
                        "홍길동"+i,"1","hong"+i+"@test.com", "010-1111-111"+i,"서울"+i,
                        "hong"+i));*/ //테이블 생성시 dummy

            }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    }


