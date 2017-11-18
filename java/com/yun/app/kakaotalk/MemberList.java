package com.yun.app.kakaotalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.yun.app.kakaotalk.Intro.MEM_ADDR;
import static com.yun.app.kakaotalk.Intro.MEM_EMAIL;
import static com.yun.app.kakaotalk.Intro.MEM_NAME;
import static com.yun.app.kakaotalk.Intro.MEM_PHONE;
import static com.yun.app.kakaotalk.Intro.MEM_PHOTO;
import static com.yun.app.kakaotalk.Intro.MEM_PW;
import static com.yun.app.kakaotalk.Intro.MEM_SEQ;
import static com.yun.app.kakaotalk.Intro.MEM_TABLE;
import static com.yun.app.kakaotalk.Intro.Member;



public class MemberList extends AppCompatActivity {

    //친구 목록 출력, 길게 탭할시 삭제, 친구추가시 회원가입으로 넘어감
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);

        final Context ctx = MemberList.this;

        final ListView listView = findViewById(R.id.listView);

        final MemberItemList items = new MemberItemList(ctx);

        /*ArrayList<Intro.Member> list = new ArrayList<>();
        list = memberItemList.list();
*/
//        String temp = "";
//        for(Member m:list){
//            temp += m.name+", ";
//        }
//        Toast.makeText(ctx,temp,Toast.LENGTH_LONG).show();

        ArrayList<Member> memberList = (ArrayList<Member>) new Intro.ListService() {
            @Override
            public List<?> execute() {
                return items.list();
            }
        }.execute();

        listView.setAdapter(new MemberItem(ctx, memberList));//adapter - 그림 출력

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Member selectedMember = (Member) listView.getItemAtPosition(i);
                Intent intent = new Intent(ctx, MemberDetail.class);
                intent.putExtra("seq", String.valueOf(selectedMember.seq));
                startActivity(intent);
                //Toast.makeText(ctx, "삭제?", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Member selectedMember = (Member) listView.getItemAtPosition(i);
                /*AlertDialog.Builder a = */new AlertDialog.Builder(ctx).setTitle("DELETE").setMessage("정말 삭제할래?").setPositiveButton(
                        android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final MemberItemDelete del = new MemberItemDelete(ctx);
                                new Intro.DeleteService(){

                                    @Override
                                    public void execute() {
                                        del.execute(String.valueOf(selectedMember.seq));
                                    }
                                }.execute();
                                /*MemberList.DeleteQuery(new MemberDelete(ctx).delete(Integer.parseInt(i)));*/

                                startActivity(new Intent(ctx, MemberList.class));

                            }
                        }
                ).setNegativeButton(
                        android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }
                ).show();
                //Toast.makeText(ctx, "정말 삭제 하시겠습니까?", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //친구 추가 addService 실행
        findViewById(R.id.member_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //member_add 클릭시 회원가입 페이지로 redirection
                Intent intent = new Intent(ctx,MemberAdd.class);
                startActivity(intent);
            }
        });


    }

    //추상 팩토리 패턴
    private abstract class ListQuery extends Intro.QueryFactory {
        SQLiteOpenHelper helper;

        public ListQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }

    private class MemberItemList extends ListQuery {

        public MemberItemList(Context ctx) {
            super(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }

        public ArrayList<Intro.Member> list() {

            ArrayList<Intro.Member> members = new ArrayList<>();
            String sql = String.format(" SELECT %s, %s, %s, %s, %s, %s, %s FROM %s ",
                    MEM_SEQ, MEM_NAME, MEM_PW, MEM_EMAIL, MEM_PHONE, MEM_ADDR, MEM_PHOTO, MEM_TABLE
            );

            Cursor cursor = this.getDatabase().rawQuery(sql, null);
            Member member = new Member();

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    member = new Member();
                    member.seq = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEM_SEQ)));
                    member.name = cursor.getString(cursor.getColumnIndex(MEM_NAME));
                    member.pw = cursor.getString(cursor.getColumnIndex(MEM_PW));
                    member.email = cursor.getString(cursor.getColumnIndex(MEM_EMAIL));
                    member.phone = cursor.getString(cursor.getColumnIndex(MEM_PHONE));
                    member.addr = cursor.getString(cursor.getColumnIndex(MEM_ADDR));
                    member.photo = cursor.getString(cursor.getColumnIndex(MEM_PHOTO));
                    members.add(member);
                }
            }

            return members;
        }
    }

    class MemberItem extends BaseAdapter{

        ArrayList<Member> list;
        LayoutInflater inflater;

        public MemberItem(Context ctx, ArrayList<Member> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(ctx);//여기서 작업 해라...
        }

        private int[] photos = {
          R.drawable.cupcake,
                R.drawable.donut,
                R.drawable.eclair,
                R.drawable.froyo,
                R.drawable.gingerbread,
                R.drawable.human
        };

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v=inflater.inflate(R.layout.member_item,null);
                holder = new ViewHolder();
                holder.photo = v.findViewById(R.id.photo);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder)v.getTag();
            }
            holder.photo.setImageResource(photos[i]);
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);
            return v;
        }
    }

    static class ViewHolder{
        ImageView photo;
        TextView name;
        TextView phone;
    }

    private abstract class DeleteQuery extends Intro.QueryFactory{
        SQLiteOpenHelper helper;

        public DeleteQuery(Context ctx) {
            super(ctx);
            helper = new Intro.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase(); //DRUD 중에서 CUD는 WRITABLE 사용함을 정의
        }

    }//추상 팩토리 패턴

    private class MemberItemDelete extends DeleteQuery{

        public MemberItemDelete(Context ctx) {
            super(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }

        public void execute(String seq){
            String sql = String.format(" DELETE FROM %s WHERE %s = '%s'; ",
                    MEM_TABLE, MEM_SEQ, seq
            );
            this.getDatabase().execSQL(sql);
            this.getDatabase().close();
        }

    }




}
