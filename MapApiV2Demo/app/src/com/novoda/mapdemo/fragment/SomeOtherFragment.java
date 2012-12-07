package com.novoda.mapdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.novoda.mapdemo.R;

public class SomeOtherFragment extends Fragment implements View.OnClickListener {

    private static final String FRAGMENT_TOAST_MESSAGE = "Some other fragment logic!";

    public SomeOtherFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.some_other_fragment, container, false);
        view.findViewById(R.id.fragment_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        showToast();
    }

    private void showToast() {
        Toast.makeText(getActivity(), FRAGMENT_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
    }
}
