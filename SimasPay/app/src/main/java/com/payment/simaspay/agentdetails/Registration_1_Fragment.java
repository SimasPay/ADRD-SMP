package com.payment.simaspay.agentdetails;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class Registration_1_Fragment extends Fragment {


    TextView textView, textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11, textView12, textView13, textView14;

    Button date, text;

    EditText Name,number;

    @Override
    public void onResume() {
        super.onResume();
        Register_to_SimaspayUserActivity.i=0;

        Register_to_SimaspayUserActivity.reg_2.setClickable(false);
        Register_to_SimaspayUserActivity.reg_3.setClickable(false);
        Register_to_SimaspayUserActivity.reg_2.setFocusable(false);
        Register_to_SimaspayUserActivity.reg_3.setFocusable(false);
        Register_to_SimaspayUserActivity.reg_2.setBackgroundColor(getResources().getColor(R.color.reg_nonfocus));
        Register_to_SimaspayUserActivity.reg_3.setBackgroundColor(getResources().getColor(R.color.reg_nonfocus));



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
        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.reg_1_frag, null);

        textView = (TextView) view2.findViewById(R.id.textview);
        textView1 = (TextView) view2.findViewById(R.id.textview1);
        textView2 = (TextView) view2.findViewById(R.id.textview2);
        textView3 = (TextView) view2.findViewById(R.id.textview3);
        textView4 = (TextView) view2.findViewById(R.id.textview4);
        textView5 = (TextView) view2.findViewById(R.id.textview5);
        textView6 = (TextView) view2.findViewById(R.id.textview6);
        textView7 = (TextView) view2.findViewById(R.id.textview7);
        textView8 = (TextView) view2.findViewById(R.id.textview8);
        textView9 = (TextView) view2.findViewById(R.id.textview9);
        textView10 = (TextView) view2.findViewById(R.id.textview10);
        textView11 = (TextView) view2.findViewById(R.id.manual_enter_from);


        textView.setTypeface(Utility.LightTextFormat(getActivity()));
        textView1.setTypeface(Utility.LightTextFormat(getActivity()));
        textView2.setTypeface(Utility.LightTextFormat(getActivity()));
        textView3.setTypeface(Utility.LightTextFormat(getActivity()));
        textView4.setTypeface(Utility.LightTextFormat(getActivity()));
        textView5.setTypeface(Utility.LightTextFormat(getActivity()));
        textView6.setTypeface(Utility.BoldTextFormat(getActivity()));
        textView7.setTypeface(Utility.LightTextFormat(getActivity()));
        textView8.setTypeface(Utility.BoldTextFormat(getActivity()));
        textView9.setTypeface(Utility.LightTextFormat(getActivity()));
        textView10.setTypeface(Utility.BoldTextFormat(getActivity()));
        textView11.setTypeface(Utility.LightTextFormat(getActivity()));

        Name=(EditText)view2.findViewById(R.id.editText1);
        number=(EditText)view2.findViewById(R.id.editText2);
        Name.setTypeface(Utility.LightTextFormat(getActivity()));
        number.setTypeface(Utility.LightTextFormat(getActivity()));

        date=(Button)view2.findViewById(R.id.Dob);
        date.setTypeface(Utility.LightTextFormat(getActivity()));



        return view2;
    }
}
