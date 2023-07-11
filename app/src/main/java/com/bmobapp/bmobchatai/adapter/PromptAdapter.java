package com.bmobapp.bmobchatai.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmobapp.bmobchatai.ChatActivity;
import com.bmobapp.bmobchatai.R;
import com.bmobapp.bmobchatai.bean.Message;
import com.bmobapp.bmobchatai.bean.Prompt;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PromptAdapter extends RecyclerView.Adapter<PromptAdapter.PromptViewHolder> {
    List<Prompt> promptList;
    Context context;

    public PromptAdapter(List<Prompt> promptList, Context context){
        this.promptList = promptList;
        this.context = context;
    }

    @NonNull
    @Override
    public PromptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View promptView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prompt_item,null);
        PromptAdapter.PromptViewHolder promptViewHolder = new PromptAdapter.PromptViewHolder(promptView);

        return promptViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull PromptViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Prompt prompt = promptList.get(position);
        holder.prompt_title.setText(prompt.getTitle());
        holder.description.setText(prompt.getDescription());
        holder.user_num.setText(prompt.getUserNum());

        Picasso.get().load(prompt.getImg()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("title",prompt.getTitle());
                intent.putExtra("prompt",prompt.getPrompt());
                intent.putExtra("name",prompt.getName());
                intent.putExtra("logo",prompt.getImg());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return promptList.size();
    }

    public class PromptViewHolder extends RecyclerView.ViewHolder{

        TextView prompt_title;
        TextView user_num;
        TextView description;

        ImageView img;

        public PromptViewHolder(@NonNull View itemView) {
            super(itemView);

            prompt_title = itemView.findViewById(R.id.prompt_title);
            user_num = itemView.findViewById(R.id.prompt_nums);
            description = itemView.findViewById(R.id.prompt_description);
            img = itemView.findViewById(R.id.header_pic);
        }
    }
}
