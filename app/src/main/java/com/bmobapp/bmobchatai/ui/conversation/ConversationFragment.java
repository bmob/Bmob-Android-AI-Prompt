package com.bmobapp.bmobchatai.ui.conversation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmobapp.bmobchatai.adapter.PromptAdapter;
import com.bmobapp.bmobchatai.bean.Prompt;
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
        BmobQuery<Prompt> query = new BmobQuery<>();
        query.findObjects(new FindListener<Prompt>() {
            @Override
            public void done(List<Prompt> promptList, BmobException e) {
                PromptAdapter promptAdapter = new PromptAdapter(promptList,getContext());
                prompt_recycle_view.setAdapter(promptAdapter);
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