package com.bmobapp.bmobchatai.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmobapp.bmobchatai.ChatActivity;
import com.bmobapp.bmobchatai.R;
import com.bmobapp.bmobchatai.bean.Character;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    List<Character> characterList;
    Context context;

    public CharacterAdapter(List<Character> characterList, Context context){
        this.characterList = characterList;
        this.context = context;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View promptView = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_item,null);
        CharacterAdapter.CharacterViewHolder promptViewHolder = new CharacterAdapter.CharacterViewHolder(promptView);

        return promptViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Character character = characterList.get(position);
        holder.character_title.setText(character.getTitle());
        holder.character_description.setText(character.getDescription());
        holder.character_user_num.setText(character.getUserNum());

        Picasso.get().load(character.getImg()).into(holder.character_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("title", character.getTitle());
                intent.putExtra("prompt", character.getPrompt());
                intent.putExtra("name", character.getName());
                intent.putExtra("img", character.getImg());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public class CharacterViewHolder extends RecyclerView.ViewHolder{

        TextView character_title;
        TextView character_user_num;
        TextView character_description;

        ImageView character_img;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);

            character_title = itemView.findViewById(R.id.prompt_title);
            character_user_num = itemView.findViewById(R.id.character_user_num);
            character_description = itemView.findViewById(R.id.character_description);
            character_img = itemView.findViewById(R.id.character_img);
        }
    }
}
