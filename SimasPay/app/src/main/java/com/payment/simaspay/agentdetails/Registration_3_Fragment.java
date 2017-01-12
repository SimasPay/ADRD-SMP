package com.payment.simaspay.agentdetails;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import org.w3c.dom.Text;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/4/2016.
 */
public class Registration_3_Fragment extends Fragment {


    TextView textView, textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8;

    Button button, button1;

    EditText editText, editText1, editText2, editText3, editText4, editText5, editText6;

    @Override
    public void onResume() {
        super.onResume();
        Register_to_SimaspayUserActivity.reg_2.setBackgroundColor(getResources().getColor(R.color.reg));
        Register_to_SimaspayUserActivity.reg_3.setBackgroundColor(getResources().getColor(R.color.reg));
        Register_to_SimaspayUserActivity.i = 2;
        Register_to_SimaspayUserActivity.reg_2.setBackgroundColor(getResources().getColor(R.color.reg));
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.reg_3_flag, null);

        textView = (TextView) view2.findViewById(R.id.textview);
        textView1 = (TextView) view2.findViewById(R.id.textview1);
        textView2 = (TextView) view2.findViewById(R.id.textview2);
        textView3 = (TextView) view2.findViewById(R.id.textview3);
        textView4 = (TextView) view2.findViewById(R.id.textview4);
        textView5 = (TextView) view2.findViewById(R.id.textview5);
        textView6 = (TextView) view2.findViewById(R.id.textview6);
        textView7 = (TextView) view2.findViewById(R.id.textview7);
        textView8 = (TextView) view2.findViewById(R.id.textview8);


        textView.setTypeface(Utility.LightTextFormat(getActivity()));
        textView1.setTypeface(Utility.LightTextFormat(getActivity()));
        textView2.setTypeface(Utility.LightTextFormat(getActivity()));
        textView3.setTypeface(Utility.LightTextFormat(getActivity()));
        textView4.setTypeface(Utility.LightTextFormat(getActivity()));
        textView5.setTypeface(Utility.LightTextFormat(getActivity()));
        textView6.setTypeface(Utility.LightTextFormat(getActivity()));
        textView7.setTypeface(Utility.LightTextFormat(getActivity()));
        textView8.setTypeface(Utility.LightTextFormat(getActivity()));

        button = (Button) view2.findViewById(R.id.button);
        button1 = (Button) view2.findViewById(R.id.button1);

        editText = (EditText) view2.findViewById(R.id.editText);
        editText1 = (EditText) view2.findViewById(R.id.editText1);
        editText2 = (EditText) view2.findViewById(R.id.editText2);
        editText3 = (EditText) view2.findViewById(R.id.editText3);
        editText4 = (EditText) view2.findViewById(R.id.editText4);
        editText5 = (EditText) view2.findViewById(R.id.editText5);
        editText6 = (EditText) view2.findViewById(R.id.editText6);

        button.setTypeface(Utility.LightTextFormat(getActivity()));
        button1.setTypeface(Utility.LightTextFormat(getActivity()));
        editText.setTypeface(Utility.LightTextFormat(getActivity()));
        editText1.setTypeface(Utility.LightTextFormat(getActivity()));
        editText2.setTypeface(Utility.LightTextFormat(getActivity()));
        editText3.setTypeface(Utility.LightTextFormat(getActivity()));
        editText4.setTypeface(Utility.LightTextFormat(getActivity()));
        editText5.setTypeface(Utility.LightTextFormat(getActivity()));
        editText6.setTypeface(Utility.LightTextFormat(getActivity()));


        return view2;
    }
}
