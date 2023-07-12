package com.bmobapp.bmobchatai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmobapp.bmobchatai.R;
import com.bmobapp.bmobchatai.bean.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 聊天内容的适配器类
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    List<Message> messageList;
    String logo;
    public ChatAdapter(List<Message> messageList,String logo) {
        this.messageList = messageList;
        this.logo = logo;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        ChatViewHolder myViewHolder = new ChatViewHolder(chatView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messageList.get(position);
        if(message.getSendBy().equals(Message.SEND_BY_ME)){
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightTextView.setText(message.getMessage());
        }else{
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftTextView.setText(message.getMessage());
            //设置头像
            Picasso.get().load(logo).into(holder.logo);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftChatView,rightChatView;
        TextView leftTextView,rightTextView;

        ImageView logo;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            leftChatView = itemView.findViewById(R.id.left_chat_view);
            rightChatView = itemView.findViewById(R.id.right_chat_view);
            logo = itemView.findViewById(R.id.chatgpt);

            leftTextView = itemView.findViewById(R.id.left_chat_text_view);
            rightTextView = itemView.findViewById(R.id.right_chat_text_view);

            
        }
    }
}
