package com.payment.simaspay.agentdetails;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.payment.simaspay.R;
import com.payment.simaspay.services.Utility;

import org.w3c.dom.Text;


/**
 * Created by Nagendra P on 1/4/2016.
 */
public class Registration_2_Fragment extends Fragment {

    TextView textView, textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11, textView12, textView13, textView14, textView15, textView16;

    Button provinsi_1, kota_kabu_1, kecamatan_1, desa_kelu_1, provinsi_2, kota_kabu_2, kecamatan_2, desa_kelu_2;

    EditText rt_rw_1, kode_pos_1, rt_rw_2, kode_pos_2,alamat_sesuai_ktp_edit;

    ImageView same_Address, Diff_Address;

    @Override
    public void onResume() {
        super.onResume();
        Register_to_SimaspayUserActivity.reg_2.setBackgroundColor(getResources().getColor(R.color.reg));
        Register_to_SimaspayUserActivity.i = 1;
        Register_to_SimaspayUserActivity.reg_3.setClickable(false);
        Register_to_SimaspayUserActivity.reg_3.setFocusable(false);
        Register_to_SimaspayUserActivity.reg_2.setBackgroundColor(getResources().getColor(R.color.reg));
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
        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.reg_2_frag, null);

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
        textView11 = (TextView) view2.findViewById(R.id.textview11);
        textView12 = (TextView) view2.findViewById(R.id.textview12);
        textView13 = (TextView) view2.findViewById(R.id.textview13);
        textView14 = (TextView) view2.findViewById(R.id.textview14);
        textView15 = (TextView) view2.findViewById(R.id.textview15);
        textView16 = (TextView) view2.findViewById(R.id.textview16);


        textView.setTypeface(Utility.LightTextFormat(getActivity()));
        textView1.setTypeface(Utility.LightTextFormat(getActivity()));
        textView2.setTypeface(Utility.LightTextFormat(getActivity()));
        textView3.setTypeface(Utility.LightTextFormat(getActivity()));
        textView4.setTypeface(Utility.LightTextFormat(getActivity()));
        textView5.setTypeface(Utility.LightTextFormat(getActivity()));
        textView6.setTypeface(Utility.LightTextFormat(getActivity()));
        textView7.setTypeface(Utility.LightTextFormat(getActivity()));
        textView8.setTypeface(Utility.LightTextFormat(getActivity()));
        textView9.setTypeface(Utility.LightTextFormat(getActivity()));
        textView10.setTypeface(Utility.LightTextFormat(getActivity()));
        textView11.setTypeface(Utility.LightTextFormat(getActivity()));
        textView12.setTypeface(Utility.LightTextFormat(getActivity()));
        textView13.setTypeface(Utility.LightTextFormat(getActivity()));
        textView14.setTypeface(Utility.LightTextFormat(getActivity()));
        textView15.setTypeface(Utility.LightTextFormat(getActivity()));
        textView16.setTypeface(Utility.LightTextFormat(getActivity()));

        provinsi_1 = (Button) view2.findViewById(R.id.provisi_1);
        kota_kabu_1 = (Button) view2.findViewById(R.id.kota_keb_1);
        kecamatan_1 = (Button) view2.findViewById(R.id.kecamatan_1);
        desa_kelu_1 = (Button) view2.findViewById(R.id.desa_kelu_1);
        provinsi_2 = (Button) view2.findViewById(R.id.provisi_2);
        kota_kabu_2 = (Button) view2.findViewById(R.id.kota_keb_2);
        kecamatan_2 = (Button) view2.findViewById(R.id.kecamatan_2);
        desa_kelu_2 = (Button) view2.findViewById(R.id.desa_kelu_2);

        provinsi_1.setTypeface(Utility.LightTextFormat(getActivity()));
        kota_kabu_1.setTypeface(Utility.LightTextFormat(getActivity()));
        kecamatan_1.setTypeface(Utility.LightTextFormat(getActivity()));
        desa_kelu_1.setTypeface(Utility.LightTextFormat(getActivity()));
        provinsi_2.setTypeface(Utility.LightTextFormat(getActivity()));
        kota_kabu_2.setTypeface(Utility.LightTextFormat(getActivity()));
        kecamatan_2.setTypeface(Utility.LightTextFormat(getActivity()));
        desa_kelu_2.setTypeface(Utility.LightTextFormat(getActivity()));

        rt_rw_1 = (EditText) view2.findViewById(R.id.rt_rw_1);
        kode_pos_1 = (EditText) view2.findViewById(R.id.kode_pos_1);
        rt_rw_2 = (EditText) view2.findViewById(R.id.rt_rw_2);
        kode_pos_2 = (EditText) view2.findViewById(R.id.kode_pos_2);
        alamat_sesuai_ktp_edit=(EditText)view2.findViewById(R.id.alamat_sesuai_ktp_edit);

        rt_rw_1.setTypeface(Utility.LightTextFormat(getActivity()));
        rt_rw_2.setTypeface(Utility.LightTextFormat(getActivity()));
        kode_pos_1.setTypeface(Utility.LightTextFormat(getActivity()));
        kode_pos_2.setTypeface(Utility.LightTextFormat(getActivity()));
        alamat_sesuai_ktp_edit.setTypeface(Utility.LightTextFormat(getActivity()));


        return view2;
    }
}
