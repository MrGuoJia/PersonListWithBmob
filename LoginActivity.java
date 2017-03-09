package com.example.jia.personlistwithbmob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_userName,edit_passWord,edit_phoneNum,edit_code;
    private Button btn_getCode,btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        edit_userName= (EditText) findViewById(R.id.login_userName);
        edit_passWord= (EditText) findViewById(R.id.login_PassWord);
        edit_phoneNum= (EditText) findViewById(R.id.edt_phoneNum);
        edit_code= (EditText) findViewById(R.id.edt_getCode);
        btn_getCode= (Button) findViewById(R.id.btn_code);
        btn_register= (Button) findViewById(R.id.btn_register);

        btn_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=edit_phoneNum.getText().toString().trim();
                BmobSMS.requestSMSCode(phone, "BombTest", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if(e==null){
                            Toast.makeText(LoginActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userCode=edit_code.getText().toString();
                final String phone=edit_phoneNum.getText().toString();
                String    Name=edit_userName.getText().toString();
                String   PassWord=edit_passWord.getText().toString();
                BmobUser user=new BmobUser();
                user.setUsername(Name);
                user.setPassword(PassWord);
                user.setMobilePhoneNumber(phone);



                BmobSMS.verifySmsCode(phone, userCode, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            String    Name=edit_userName.getText().toString().trim();
                            String   PassWord=edit_passWord.getText().toString().trim();
                            BmobUser user=new BmobUser();
                            user.setUsername(Name);
                            user.setPassword(PassWord);
                            user.setMobilePhoneNumber(phone);
                            user.setMobilePhoneNumberVerified(true);
                            Log.i("smile0", "验证成功");
                            user.signUp(new SaveListener<BmobUser>() {
                                @Override
                                public void done(BmobUser bmobUser, BmobException e) {
                                    if (e==null){
                                        Toast.makeText(LoginActivity.this,"成功注册",Toast.LENGTH_SHORT).show();
                                        Log.i("smile", "验证成功");
                                    }else {
                                        Log.i("smile", "验证失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
