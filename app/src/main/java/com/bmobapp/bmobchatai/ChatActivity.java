package com.bmobapp.bmobchatai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.bmobapp.bmobchatai.adapter.ChatAdapter;
import com.bmobapp.bmobchatai.bean.Message;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.ai.BmobAI;
import cn.bmob.v3.ai.ChatMessageListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.realtime.Client;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    EditText messageEditText;
    Button sendButton;

    List<Message> messageList = new ArrayList<>();

    ChatAdapter chatAdapter;

    //BmobAI bmobAI;

    String title;

    String session;

    String username = "default";

    String startMsg="";

    String logo ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获取这个机器人的标题、名称、prompt和头像信息
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        session = intent.getStringExtra("name");
        String prompt = intent.getStringExtra("prompt");
        logo = intent.getStringExtra("img");
        startMsg = intent.getStringExtra("startMsg");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //设置头部标题栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //获取用户信息，这里没有做用户权限的判定
        BmobUser user= BmobUser.getCurrentUser();
        if(user!=null)
            username = user.getUsername();

        //初始化view
        recyclerView = findViewById(R.id.msg_recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        //输入框获取光标
        messageEditText.requestFocus();
        sendButton = findViewById(R.id.send_bt);
        sendButton.setOnClickListener(this);
        ImageButton clear_bt = findViewById(R.id.clearSession);
        clear_bt.setOnClickListener(this);
        ImageButton keyboard_bt = findViewById(R.id.keyboardbt);
        keyboard_bt.setOnClickListener(this);
        ImageButton voice_bt = findViewById(R.id.voicebt);
        voice_bt.setOnClickListener(this);

        //设置prompt信息
        if(prompt!=null && !prompt.isEmpty())
            BmobApp.bmobAI.setPrompt(prompt);

        //加载历史聊天记录
        initHistoryChatList();
    }

    /**
     * 初始化历史的聊天记录
     */
    private void initHistoryChatList(){
        //添加开场白
        Message start = new Message(startMsg, Message.SEND_BY_BOT,session,username);
        messageList.add(0,start);

        //初始化AI内容问答存储
        BmobQuery<Message> query = new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.addWhereEqualTo("session",session);
        query.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if(e==null && list!=null){
                    messageList.addAll(list);
                }

                chatAdapter = new ChatAdapter(messageList,logo);
                recyclerView.setAdapter(chatAdapter);
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setStackFromEnd(true);
                recyclerView.setLayoutManager(llm);
            }
        });
    }

    /**
     * 支持流的形式呈现内容到界面
     * @param s
     */
    public void addToLastMessage(String s)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(messageList.size()<=0) return;
                Message message =  messageList.get(messageList.size()-1);

                if(message.getSendBy()==Message.SEND_BY_ME){
                    Message newmessage = new Message(s,Message.SEND_BY_BOT,session,username);
                    messageList.add(newmessage);
                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                }else{
                    message.setMessage(message.getMessage() + s);
                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                }

                //更新界面
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 一次性将全部内容呈现到界面
     * @param message
     * @param sendBy
     */
    void addToChat(String message,String sendBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message(message,sendBy,session,username);
                //存储到Bmob后端云上面
                msg.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {

                    }
                });

                //添加到本地
                messageList.add(msg);
                //更新界面
                chatAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
            }
        });
    }

    /**
     * 清除会话信息
     */
    void ClearSession(){
        //内置的清空会话
        BmobApp.bmobAI.Clear(session);
        //清空界面信息
        messageList.clear();
        chatAdapter.notifyDataSetChanged();
        //真实进行删除操作
        Message message = new Message();
        BmobQuery<Message> query = new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.addWhereEqualTo("session",session);
        query.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if(e==null && list!=null){
                    for (int i=0;i<list.size();i++){
                        Message m = list.get(i);
                        message.delete(m.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {

                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        //删除会话信息
        if (v.getId()==R.id.clearSession){
            new AlertDialog.Builder(v.getContext())
                    .setIcon(R.mipmap.chatgpt)
                    .setTitle("系统提示")
                    .setMessage("你真的要删除会话信息吗？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClearSession();
                        }
                    }).create().show();;
        }
        //发送消息到AI服务器
        else if (v.getId()==R.id.send_bt){
            //获取问题
            String quesion = messageEditText.getText().toString().trim();
            if(quesion.isEmpty() || quesion.trim()=="")
                return;

            //连接AI服务器（这个代码为了防止AI连接中断，因为可能会存在某些情况下，比如网络切换、中断等，导致心跳连接失败）
            BmobApp.bmobAI.Connect();

            //显示问题
            addToChat(quesion,Message.SEND_BY_ME);
            messageEditText.setText("");

            //发送内容到AI中
            BmobApp.bmobAI.Chat(quesion, session, new ChatMessageListener() {
                @Override
                public void onMessage(String s) {
                    //消息流的形式返回AI的结果
                    addToLastMessage(s);
                    Log.d("ai",s);
                }
                @Override
                public void onFinish(String s) {
                    //一次性返回全部结果，结果回来之后，同步将信息保存到Bmob后端云上面
                    //addToChat(s,Message.SEND_BY_BOT);
                    Message newmessage = new Message(s,Message.SEND_BY_BOT,session,username);
                    newmessage.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                        }
                    });

                    sendButton.setEnabled(true);
                }

                @Override
                public void onError(String s) {
                    //OpenAI的密钥错误或者超过OpenAI并发时，会返回这个错误，你也可以toast这个信息给用户
                    Log.d("ai",s);
                    sendButton.setEnabled(true);
                }

                @Override
                public void onClose() {
                    //连接关闭了
                    Log.d("ai","close");
                    sendButton.setEnabled(true);
                }
            });
        }
        //切换对话方式为语音对话
        else if (v.getId()==R.id.keyboardbt){
            RelativeLayout key = findViewById(R.id.bottom_layout);
            key.setVisibility(View.VISIBLE);

            RelativeLayout voice = findViewById(R.id.bottom_layout_voice);
            voice.setVisibility(View.INVISIBLE);
        }
        //切换对话方式为文字对话
        else if(v.getId()==R.id.voicebt){
            RelativeLayout key = findViewById(R.id.bottom_layout);
            key.setVisibility(View.INVISIBLE);

            RelativeLayout voice = findViewById(R.id.bottom_layout_voice);
            voice.setVisibility(View.VISIBLE);
        }
    }
}