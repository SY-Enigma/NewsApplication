package com.example.news.fragment;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.news.util.AppContext;

public class BaseFragment extends Fragment {
    private Activity activity;

    @Nullable
    @Override
    public Context getContext() {
        if (activity == null) {
            return AppContext.getInstance();
        }
        return activity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
}
