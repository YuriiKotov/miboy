package com.boco.miboy.fragment;

import android.support.v4.app.Fragment;

import com.boco.miboy.activity.MainActivity;

public class BaseFragment extends Fragment{
    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }
}
