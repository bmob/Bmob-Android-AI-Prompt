package com.bmobapp.bmobchatai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.bmobapp.bmobchatai.adapter.ChatAdapter;
import com.bmobapp.bmobchatai.bean.Message;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.ai.BmobAI;
import cn.bmob.v3.ai.ChatMessageListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText messageEditText;
    Button sendButton;

    List<Message> messageList = new ArrayList<>();

    ChatAdapter chatAdapter;

    //BmobAI bmobAI;

    String title;

    String session;

    String username = "13800138000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.msg_recycler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_bt);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        session = intent.getStringExtra("name");
        String prompt = intent.getStringExtra("prompt");
        String logo = intent.getStringExtra("logo");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(title);

        //创建Bmob AI实例
        //bmobAI = new BmobAI();
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

        //



        //设置prompt信息
        if(prompt!=null && !prompt.isEmpty())
            BmobApp.bmobAI.setPrompt(prompt);

        //点击发送提问到AI服务器的按钮
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //sendButton.setEnabled(false);

                //获取问题
                String quesion = messageEditText.getText().toString().trim();
                if(quesion.isEmpty() || quesion.trim()=="")
                    return;
                //连接AI服务器（这个代码为了防止AI连接中断）
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
                        //一次性返回全部结果，这个方法需要等待一段时间，友好性较差
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
                        //OpenAI的密钥错误或者超过OpenAI并发时，会返回这个错误
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
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                }else{
                    message.setMessage(message.getMessage() + s);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                }
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

                chatAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
            }
        });
    }
}