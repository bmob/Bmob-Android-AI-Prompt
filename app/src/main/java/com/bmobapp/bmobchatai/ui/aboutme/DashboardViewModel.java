package com.bmobapp.bmobchatai.ui.aboutme;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("页面正在建设中");
    }

    public LiveData<String> getText() {
        return mText;
    }
}