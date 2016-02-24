package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

/**
 * Created by Nagendra P on 1/5/2016.
 */
public class Register_To_SimaspayUserSuccessActivity extends Activity {

    TextView title, textView, name, name_field, nomor_ktp, nomor_ktp_field, ktp_berlaku_hingga,
            ktp_berlaku_hingga_field, alamat_sesuai_ktp, alamat_sesuai_ktp_field, alamat_domisili,
            alamat_domisili_field, nama_lengkap, nama_lengkap_field, tanggal_lahir, tanggal_lahir_field,
            pekerjaan, pekerjaan_field, pendapatan_per_Bulan, pendapatan_per_Bulan_field, tujuan_pembukaan_rekening,
            tujuan_pembukaan_rekening_field, sumber_dana, sumber_dana_field, nomor_hp, nomor_hp_field, e_mail, e_mail_field,tempat_lahir,tempat_lahir_field;

    Button confirmation;
    LinearLayout btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_simaspay_success);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }

        title = (TextView) findViewById(R.id.title);
        textView = (TextView) findViewById(R.id.textview);
        name = (TextView) findViewById(R.id.name);
        name_field = (TextView) findViewById(R.id.name_field);
        nomor_ktp = (TextView) findViewById(R.id.nomor_ktp);
        nomor_ktp_field = (TextView) findViewById(R.id.nomor_ktp_field);
        ktp_berlaku_hingga = (TextView) findViewById(R.id.ktp_berlaku_hingga);
        ktp_berlaku_hingga_field = (TextView) findViewById(R.id.ktp_berlaku_hingga_field);
        alamat_sesuai_ktp = (TextView) findViewById(R.id.alamat_sesuai_ktp);
        alamat_sesuai_ktp_field = (TextView) findViewById(R.id.alamat_sesuai_ktp_field);
        alamat_domisili = (TextView) findViewById(R.id.alamat_domisili);
        alamat_domisili_field = (TextView) findViewById(R.id.alamat_domisili_field);
        nama_lengkap = (TextView) findViewById(R.id.nama_lengkap);
        nama_lengkap_field = (TextView) findViewById(R.id.nama_lengkap_field);
        tanggal_lahir = (TextView) findViewById(R.id.tanggal_lahir);
        tanggal_lahir_field = (TextView) findViewById(R.id.tanggal_lahir_field);
        pekerjaan = (TextView) findViewById(R.id.pekerjaan);
        pekerjaan_field = (TextView) findViewById(R.id.pekerjaan_field);
        pendapatan_per_Bulan = (TextView) findViewById(R.id.pendapatan_per_Bulan);
        pendapatan_per_Bulan_field = (TextView) findViewById(R.id.pendapatan_per_Bulan_field);
        tujuan_pembukaan_rekening = (TextView) findViewById(R.id.tujuan_pembukaan_rekening);
        tujuan_pembukaan_rekening_field = (TextView) findViewById(R.id.tujuan_pembukaan_rekening_field);
        sumber_dana = (TextView) findViewById(R.id.sumber_dana);
        sumber_dana_field = (TextView) findViewById(R.id.sumber_dana_field);
        nomor_hp = (TextView) findViewById(R.id.nomor_hp);
        nomor_hp_field = (TextView) findViewById(R.id.nomor_hp_field);
        e_mail = (TextView) findViewById(R.id.e_mail);
        e_mail_field = (TextView) findViewById(R.id.e_mail_field);
        tempat_lahir = (TextView) findViewById(R.id.tempat_lahir);
        tempat_lahir_field = (TextView) findViewById(R.id.tempat_lahir_field);

        confirmation = (Button) findViewById(R.id.next);
        confirmation.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));


        btnBack = (LinearLayout) findViewById(R.id.back_layout);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        title.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        textView.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        name.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        name_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        nomor_ktp.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        nomor_ktp_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        ktp_berlaku_hingga.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        ktp_berlaku_hingga_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        alamat_sesuai_ktp.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        alamat_sesuai_ktp_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        alamat_domisili.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        alamat_domisili_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        nama_lengkap.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        nama_lengkap_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        tanggal_lahir.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        tanggal_lahir_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        pekerjaan.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        pekerjaan_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        pendapatan_per_Bulan.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        pendapatan_per_Bulan_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        tujuan_pembukaan_rekening.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        tujuan_pembukaan_rekening_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        sumber_dana.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        sumber_dana_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        nomor_hp.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        nomor_hp_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        e_mail.setTypeface(Utility.BoldTextFormat(Register_To_SimaspayUserSuccessActivity.this));
        e_mail_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));
        tempat_lahir.setTypeface(Utility.Robot_Regular(Register_To_SimaspayUserSuccessActivity.this));
        tempat_lahir_field.setTypeface(Utility.Robot_Light(Register_To_SimaspayUserSuccessActivity.this));

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
