package com.silence.account.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Silence on 2016/3/28 0028.
 */
public abstract class BaseFragment extends Fragment {

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        RefWatcher refWatcher = AccountApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResId(), container, false);
        ButterKnife.bind(getViewRoot(), view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(getViewRoot());
    }

    protected abstract int getResId();

    protected abstract Fragment getViewRoot();
}
