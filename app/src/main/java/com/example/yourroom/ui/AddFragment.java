package com.example.yourroom.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.yourroom.R;


public class AddFragment extends Fragment {
    private EditText mEditTextFileAddress, mEditTextFilePrice, mEditTextFileDescription,
            mEditTextFileEmail, mEditTextFilePhone;
    private RadioButton mRadioButtonRent, mRadioButtonDaily, mRadioButtonResidential, mRadioButtonNon_residential;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

}