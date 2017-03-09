package com.example.jia.personlistwithbmob;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by jia on 2017/3/6.
 */

public class PersionAdapter extends RecyclerView.Adapter<PersionAdapter.ViewHolder> {
        private List<Persion> mPersions;
        private DeleteListener listener;
        private Bitmap bitmap;
    public void change(List<Persion> list1) {
            mPersions=list1;
            notifyDataSetChanged();
    }

    static  class ViewHolder extends RecyclerView.ViewHolder{
         TextView name,age,address;
         Button btn_del;
         ImageView headView;
           public ViewHolder(View itemView) {
               super(itemView);
                name= (TextView) itemView.findViewById(R.id.list_name);
                age= (TextView) itemView.findViewById(R.id.list_age);
               address=(TextView) itemView.findViewById(R.id.list_adress);
               headView= (ImageView) itemView.findViewById(R.id.head_img);
               btn_del= (Button) itemView.findViewById(R.id.btn_delete);
           }
       }
    public PersionAdapter(List<Persion> list1,DeleteListener listener){
        mPersions=list1;
        this.listener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.persion_list,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Persion persion = mPersions.get(position);

        String ImageUrl=persion.getHeadImg().getFileUrl();
        if(persion.getHeadImg()!=null){
            Glide.with(holder.headView.getContext()).load(ImageUrl).into(holder.headView);
        }

        /*OkHttpClient client=new OkHttpClient();读取网上图片的另外一种方法
        Request request=new Request.Builder().url(ImageUrl).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("PersionAdapter",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes=response.body().bytes();
                bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);

            }
        });*/



        holder.name.setText(persion.getName());
        holder.age.setText(String.valueOf(persion.getAge()));
        holder.address.setText(persion.getAddress());




        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                persion.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            listener.refresh();
                            Log.i("PersionAdapter","删除数据成功");
                        }else{
                            Log.i("PersionAdapter","删除数据失败"+e.toString());
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPersions.size();
    }
}

