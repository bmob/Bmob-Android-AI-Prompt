package com.bmobapp.bmobchatai.ui.conversation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmobapp.bmobchatai.adapter.CharacterAdapter;
import com.bmobapp.bmobchatai.bean.Character;
import com.bmobapp.bmobchatai.databinding.FragmentHomeBinding;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ConversationFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView prompt_recycle_view = binding.promptRecyclerView;

        //从Bmob上面获取AI信息
        BmobQuery<Character> query = new BmobQuery<>();
        query.findObjects(new FindListener<Character>() {
            @Override
            public void done(List<Character> characterList, BmobException e) {
                CharacterAdapter characterAdapter = new CharacterAdapter(characterList,getContext());
                prompt_recycle_view.setAdapter(characterAdapter);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setStackFromEnd(true);
                prompt_recycle_view.setLayoutManager(llm);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}