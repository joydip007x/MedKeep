package com.example.medx.ui.wish;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WishViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WishViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is CNAHE IT YOURSLF fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}