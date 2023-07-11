package com.bmobapp.bmobchatai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import kotlin.jvm.internal.PropertyReference0Impl;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String code;
    private boolean ifCheck = false;

    private EditText phoneEdit;
    private EditText codeEdit;
    private RadioButton radioButton;
    private Button submitBt;
    private Button getCodeBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        setTitle("快速登录");

        phoneEdit = findViewById(R.id.phone);
        codeEdit = findViewById(R.id.code);
        radioButton = findViewById(R.id.checkRadio);
        getCodeBt = findViewById(R.id.getcode);
        getCodeBt.setOnClickListener(this);
        submitBt = findViewById(R.id.submit);
        submitBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==getCodeBt){
            if(!radioButton.isChecked()){
                Toast.makeText(v.getContext(),"请同意以上服务条款",Toast.LENGTH_SHORT).show();
                return;
            }

            //获取验证码
            String phone = phoneEdit.getText().toString().trim();

            //这里不检查phone的更多合规性问题，自行补上
            if(phone.length()!=11){
                Toast.makeText(v.getContext(),"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                return;
            }

            //发送验证码
            BmobSMS.requestSMSCode(phone, "", new QueryListener<Integer>() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if(e==null){
                        //获取到验证码
                        Toast.makeText(v.getContext(),"验证码发送成功",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if(v==submitBt){
            //注册登录
            String phone = phoneEdit.getText().toString().trim();
            String code = codeEdit.getText().toString().trim();

            if(code.isEmpty() || code.length()!=6){
                Toast.makeText(v.getContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
                return;
            }

            //执行登录操作
            BmobUser.signOrLoginByMobilePhone(phone, code, new LogInListener<BmobUser>() {
                @Override
                public void done(BmobUser o, BmobException e) {
                    if(e==null){
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else{

                        Toast.makeText(v.getContext(),"短信验证失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}