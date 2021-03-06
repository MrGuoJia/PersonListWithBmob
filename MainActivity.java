package com.example.jia.personlistwithbmob;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends AppCompatActivity implements DeleteListener{
    private android.support.v7.widget.RecyclerView mView;
    private EditText name,age,address;
    private Button btn_save;
    private List<Persion> list1=new ArrayList<Persion>();
    private PersionAdapter adapter;
    private EditText edt_search;
    private Button btn_search;
    private Button btn_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bmob.initialize(this,"e3de5628cbe1d7a61ad73c433495a472");
        initViews();
        initList();
        getAllMessage();

    }



    private void initList() {

        LinearLayoutManager Manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mView.setLayoutManager(Manager);
        adapter=new PersionAdapter(list1,MainActivity.this);
        mView.setAdapter(adapter);
    }

    private void getAllMessage() {

        BmobQuery<Persion> query=new BmobQuery<>();
        query.findObjects(new FindListener<Persion>() {
            @Override
            public void done(List<Persion> list, BmobException e) {
                if(e==null){
                    Toast.makeText(MainActivity.this,"共有"+list.size()+"条数据",Toast.LENGTH_SHORT).show();
                        list1=list;
                        adapter.change(list1);


                }else {
                    Toast.makeText(MainActivity.this,"查询失败"+e.toString(),Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void initViews() {
        mView= (RecyclerView) findViewById(R.id.rv);
        name= (EditText) findViewById(R.id.edit_name);
        age= (EditText) findViewById(R.id.edit_age);
        address= (EditText) findViewById(R.id.edit_address);
        btn_save= (Button) findViewById(R.id.add);
        edt_search= (EditText) findViewById(R.id.edt_search);
        btn_search= (Button) findViewById(R.id.btn_search);
        btn_pass= (Button) findViewById(R.id.btn_pass);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String Name=name.getText().toString().trim();
                final int Age=Integer.parseInt(age.getText().toString().trim());
                final String Address=address.getText().toString().trim();
                //路径是夜神模拟器的路径
                String PATH="storage/emulated/legacy/Pictures/Screenshots/dog1.jpg";
                String PATH1="storage/emulated/legacy/Pictures/Screenshots/dog3.jpg";

                final BmobFile bmobFile=new BmobFile(new File(PATH));
                bmobFile.uploadblock(new UploadFileListener() {//上传图片路径
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //下边是需要在图片上传成功后才能开始在后台数据库存储
                            final Persion  p=new Persion();
                            p.setHeadImg(bmobFile);
                            p.setName(Name);
                            p.setAge(Age);
                            p.setAddress(Address);
                            p.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
                                        list1.add(p);
                                        adapter.notifyDataSetChanged();
                                    }else{
                                        Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(MainActivity.this,"图片上传失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() { //用于搜索
            @Override

            public void onClick(View v) {
                int msg=Integer.parseInt(edt_search.getText().toString()) ;
                Toast.makeText(MainActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                String bql="select * from Persion where age = "+ msg;//用 name 在Bmob 中调用貌似不可以
                new BmobQuery<Persion>().doSQLQuery(bql, new SQLQueryListener<Persion>() {
                    @Override
                    public void done(BmobQueryResult<Persion> bmobQueryResult, BmobException e) {
                        if(e==null){
                            List<Persion> results=bmobQueryResult.getResults();
                            adapter.change(results);

                        }else{
                            Log.i("MainActivity----------",e.toString());
                        }
                    }
                });
            }
        });

        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    public void refresh() {
            getAllMessage();
    }
}
