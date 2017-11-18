package com.yun.app.kakaotalk;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class Album extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album);

        final Context ctx = Album.this;


        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new AlbumItem(ctx, photos()));/*  gridView.setOnClickListener(new AdapterView.OnItemClickListener());*/
      gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Toast.makeText(ctx, "선택 사진"+i, Toast.LENGTH_LONG).show();
          }
      });


    }


    public String[] photos(){
        int count = 6;
        String[] arr = new String[count];
        for(int i=0; i<arr.length; i++){
            arr[i]="mov0"+(i+1);
        }
        return arr;
    }

    private class AlbumItem extends BaseAdapter{
        private Context ctx;
        private String[] photos;



        public AlbumItem(Context ctx, String[] photos) {
            this.ctx = ctx;
            this.photos = photos;
        }

        @Override
        public int getCount() {
            return photos.length;
        }

        @Override
        public Object getItem(int i) {
            return photos[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            LayoutInflater inflater
                    = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if(v==null){
                gridView = new View(ctx);
                gridView = inflater.inflate(R.layout.album_item, null);
                ImageView imageView = gridView.findViewById(R.id.imageView);
                String photo = photos[i];

                switch (photo){
                    case "mov01":imageView.setImageResource(R.drawable.mov01);break;
                    case "mov02":imageView.setImageResource(R.drawable.mov02);break;
                    case "mov03":imageView.setImageResource(R.drawable.mov03);break;
                    case "mov04":imageView.setImageResource(R.drawable.mov04);break;
                    case "mov05":imageView.setImageResource(R.drawable.mov05);break;
                    case "mov06":imageView.setImageResource(R.drawable.mov06);break;
                }

            }else{
                gridView = v;
            }
            return gridView;
        }
    }

}
