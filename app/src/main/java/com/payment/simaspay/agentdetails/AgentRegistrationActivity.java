package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.payment.simaspay.PojoClasses.ArealData;
import com.payment.simaspay.R;
import com.payment.simaspay.services.Constants;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.payment.simaspay.services.XMLParser;
import com.payment.simaspay.userdetails.SessionTimeOutActivity;
import com.payment.simaspay.constants.EncryptedResponseDataContainer;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pubdialog.DialogGroup;
import pubdialog.DialogObject;
import pubdialog.PubDialogFragment;


/**
 * Created by Nagendra P on 1/7/2016.
 */
public class AgentRegistrationActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView stage1_textView, stage1_textView1, stage1_textView2, stage1_textView3, stage1_textView4, stage1_textView5, stage1_textView6, stage1_textView7, stage1_textView8, stage1_textView9, stage1_textView10, stage1_textView11, stage1_textView12, stage1_textView13, stage1_textView14, stage1_textView15, stage1_textView16;

    TextView tab_1_text, tab_2_text, tab_3_text;

    Button stage1_date, stage1_tanggal;
    boolean dob_or_limit;

    EditText stage1_Name, stage1_number, stage1_nomorHp;

    LinearLayout limited, lifelong;

    String ktpDocument, supportingDocument, subscriberFormDocument;


    int documnetSelection = 0;

    Intent intent;

    ImageView manual_selection_checkbox, manual_selection_checkbox2;

    String Subrscriber_work_otherThanlainnya, subscriberwork, incomeAmount, openingAccount, sourceFund, emailId;


    TextView stage2_textView, stage2_textView1, stage2_textView2, stage2_textView3, stage2_textView4, stage2_textView5, stage2_textView6, stage2_textView7, stage2_textView8, stage2_textView9, stage2_textView10, stage2_textView11, stage2_textView12, stage2_textView13, stage2_textView14, stage2_textView15, stage2_textView16, stage2_textView17, stage2_textView18, stage2_textView19, stage2_textView20, stage2_textView21, stage2_textView22;


    Button stage2_provinsi_1, stage2_kota_kabu_1, stage2_kecamatan_1, stage2_desa_kelu_1, stage2_provinsi_2, stage2_kota_kabu_2, stage2_kecamatan_2, stage2_desa_kelu_2;

    EditText stage2_rt_rw_1, stage2_kode_pos_1, stage2_rt_rw_2, stage2_kode_pos_2, stage2_alamat_sesuai_ktp_edit, stage2_different_alamat_sesuai_ktp_edit, stage2_name_edit, stage2_tempat_edit, stage2_rw_edit, stage2_rw_different_edit;

    ImageView stage2_same_Address, stage2_Diff_Address;

    boolean stage2_sameaddress = true;

    String mothersName;

    int selectedProvice = -1, value = 0, selected_region = 0, selectedDistrict = -1, selectedVillage = -1,selected_work=0;


    TextView stage3_textView, stage3_textView1, stage3_textView2, stage3_textView3, stage3_textView4, stage3_textView5, stage3_textView6, stage3_textView7, stage3_textView8, stage3_textview9;

    Button stage3_button, stage3_button1;

    EditText stage3_editText, stage3_editText1, stage3_editText2, stage3_editText3, stage3_editText4, stage3_editText5, stage3_editText6;


    TextView Rp;

    public static Button next;

    LinearLayout reg_1, reg_2, reg_3;
    LinearLayout back;

    LinearLayout stage_1, stage_2, stage_3;

    int i = 0;

    String TransactionID = "";

    boolean limited_period = true;

    LinearLayout document_1, document_2, document_3;

    PubDialogFragment pubDialogFragment;
    TimePickerDialog timePickerDialog;

    Calendar calendar;
    DatePickerDialog datePickerDialog, datePickerDialog_1;

    public String areasString, otherLocationsString, workString;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    ArrayList<ArealData> provinceList = new ArrayList<>();
    ArrayList<ArealData> districtList = new ArrayList<>();
    ArrayList<ArealData> regionList = new ArrayList<>();
    ArrayList<ArealData> villagesList = new ArrayList<>();

    ImageView document_image_1, document_image_2, document_image_3;

    SharedPreferences sharedPreferences, transferData;

    TextView textView;


    public String ktp_bithPlace, ktp_Name, ktp_AddressCode, ktp_Provinsi, ktp_City, ktp_district, ktp_village, ktp_rt, ktp_rw, ktp_postalcode, diff_AddressCode, diff_Province, diff_City, diff_disctrict, diff_village, diff_Rt, diff_Rw, diff_PostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_register);

        calendar = Calendar.getInstance();
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        transferData = getSharedPreferences("transferData", MODE_PRIVATE);

        datePickerDialog = DatePickerDialog.newInstance(AgentRegistrationActivity.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        datePickerDialog_1 = DatePickerDialog.newInstance(AgentRegistrationActivity.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

        progressDialog = new ProgressDialog(AgentRegistrationActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }


        stage_1 = (LinearLayout) findViewById(R.id.stage1);
        stage_2 = (LinearLayout) findViewById(R.id.stage2);
        stage_3 = (LinearLayout) findViewById(R.id.stage3);

        textView = (TextView) findViewById(R.id.titled);

        reg_1 = (LinearLayout) findViewById(R.id.reg_1);
        reg_2 = (LinearLayout) findViewById(R.id.reg_2);
        reg_3 = (LinearLayout) findViewById(R.id.reg_3);

        textView.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));


        next = (Button) findViewById(R.id.next);
        next.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));

        back = (LinearLayout) findViewById(R.id.back_layout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        stage1_textView = (TextView) findViewById(R.id.textview);
        stage1_textView1 = (TextView) findViewById(R.id.textview1);
        stage1_textView2 = (TextView) findViewById(R.id.textview2);
        stage1_textView3 = (TextView) findViewById(R.id.textview3);
        stage1_textView4 = (TextView) findViewById(R.id.textview4);
        stage1_textView5 = (TextView) findViewById(R.id.textview5);
        stage1_textView6 = (TextView) findViewById(R.id.textview6);
        stage1_textView7 = (TextView) findViewById(R.id.textview7);
        stage1_textView8 = (TextView) findViewById(R.id.textview8);
        stage1_textView9 = (TextView) findViewById(R.id.textview9);
        stage1_textView10 = (TextView) findViewById(R.id.textview10);
        stage1_textView11 = (TextView) findViewById(R.id.manual_enter_from);
        stage1_textView12 = (TextView) findViewById(R.id.stage1_text);
        stage1_textView13 = (TextView) findViewById(R.id.textviewtanggal_lahir_text);
        stage1_textView14 = (TextView) findViewById(R.id.textviewtanggal_lahir_alert_text);
        stage1_textView15 = (TextView) findViewById(R.id.textview11);
        stage1_textView16 = (TextView) findViewById(R.id.textview12);

        Rp=(TextView)findViewById(R.id.Rp);

        tab_1_text = (TextView) findViewById(R.id.tab_1_text);
        tab_2_text = (TextView) findViewById(R.id.tab_2_text);
        tab_3_text = (TextView) findViewById(R.id.tab_3_text);

        tab_1_text.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));
        tab_2_text.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));
        tab_3_text.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));

        stage1_textView.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView2.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView3.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView4.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView5.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView6.setTypeface(Utility.HelveticaNeue_Medium(AgentRegistrationActivity.this));
        stage1_textView7.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView8.setTypeface(Utility.HelveticaNeue_Medium(AgentRegistrationActivity.this));
        stage1_textView9.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView10.setTypeface(Utility.HelveticaNeue_Medium(AgentRegistrationActivity.this));
        stage1_textView11.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView12.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView13.setTypeface(Utility.HelveticaNeue_Medium(AgentRegistrationActivity.this));
        stage1_textView14.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_textView15.setTypeface(Utility.HelveticaNeue_Medium(AgentRegistrationActivity.this));
        stage1_textView16.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));

        stage1_textView14.setVisibility(View.GONE);
        stage1_textView16.setVisibility(View.GONE);

        stage1_Name = (EditText) findViewById(R.id.editText1);
        stage1_number = (EditText) findViewById(R.id.editText2);
        stage1_nomorHp = (EditText) findViewById(R.id.nomor_hp);
        stage1_Name.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_number.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage1_nomorHp.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));

        limited = (LinearLayout) findViewById(R.id.limited);
        lifelong = (LinearLayout) findViewById(R.id.lifelong);

        manual_selection_checkbox = (ImageView) findViewById(R.id.manual_selection_checkbox);
        manual_selection_checkbox2 = (ImageView) findViewById(R.id.manual_selection_checkbox2);

        stage1_date = (Button) findViewById(R.id.Dob);
        stage1_date.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));

        stage1_tanggal = (Button) findViewById(R.id.dateselect);
        stage1_tanggal.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        Rp.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));

        stage1_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dob_or_limit = true;
                if (!datePickerDialog.isAdded()) {
                    datePickerDialog.setVibrate(false);
                    datePickerDialog.setYearRange(1930, calendar.get(Calendar.YEAR));
                    datePickerDialog.setCloseOnSingleTapDay(true);
                    datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                }
            }
        });


        stage1_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dob_or_limit = false;
                if (!datePickerDialog_1.isAdded()) {
                    datePickerDialog_1.setVibrate(false);
                    datePickerDialog_1.setYearRange(calendar.get(Calendar.YEAR), 2050);
                    datePickerDialog_1.setCloseOnSingleTapDay(true);
                    datePickerDialog_1.show(getSupportFragmentManager(), DATEPICKER_TAG);
                }
            }
        });
        limited_period = true;
        manual_selection_checkbox.setImageDrawable(getResources().getDrawable(R.drawable.selected));
        manual_selection_checkbox2.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
        manual_selection_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limited_period = true;
                stage1_date.setClickable(true);
                stage1_date.setFocusable(true);
                manual_selection_checkbox.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                manual_selection_checkbox2.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }
        });

        manual_selection_checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limited_period = false;
                stage1_date.setClickable(false);
                stage1_date.setFocusable(false);
//                stage1_date.setText("");
                stage1_date.setText("DD-MM-YYYY");
                manual_selection_checkbox.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                manual_selection_checkbox2.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            }
        });


        stage2_textView = (TextView) findViewById(R.id.stage2_textview);
        stage2_textView1 = (TextView) findViewById(R.id.stage2_textview1);
        stage2_textView2 = (TextView) findViewById(R.id.stage2_textview2);
        stage2_textView3 = (TextView) findViewById(R.id.stage2_textview3);
        stage2_textView4 = (TextView) findViewById(R.id.stage2_textview4);
        stage2_textView5 = (TextView) findViewById(R.id.stage2_textview5);
        stage2_textView6 = (TextView) findViewById(R.id.stage2_textview6);
        stage2_textView7 = (TextView) findViewById(R.id.stage2_textview7);
        stage2_textView8 = (TextView) findViewById(R.id.stage2_textview8);
        stage2_textView9 = (TextView) findViewById(R.id.stage2_textview9);
        stage2_textView10 = (TextView) findViewById(R.id.stage2_textview10);
        stage2_textView11 = (TextView) findViewById(R.id.stage2_textview11);
        stage2_textView12 = (TextView) findViewById(R.id.stage2_textview12);
        stage2_textView13 = (TextView) findViewById(R.id.stage2_textview13);
        stage2_textView14 = (TextView) findViewById(R.id.stage2_textview14);
        stage2_textView15 = (TextView) findViewById(R.id.stage2_textview15);
        stage2_textView16 = (TextView) findViewById(R.id.stage2_textview16);

        stage2_textView17 = (TextView) findViewById(R.id.stage_2_text);
        stage2_textView18 = (TextView) findViewById(R.id.stage_2_text_2);
        stage2_textView19 = (TextView) findViewById(R.id.stage2_textview_rw);

        stage2_textView20 = (TextView) findViewById(R.id.stage_2_text_1);
        stage2_textView21 = (TextView) findViewById(R.id.stage_2_text_3);
        stage2_textView22 = (TextView) findViewById(R.id.stage2_rw_textview15);

        stage2_Diff_Address = (ImageView) findViewById(R.id.stage2_diff_address);
        stage2_same_Address = (ImageView) findViewById(R.id.stage2_same_address);

        findViewById(R.id.different_address).setVisibility(View.GONE);
        stage2_sameaddress = true;
        findViewById(R.id.different_address).setVisibility(View.GONE);
        stage2_same_Address.setImageDrawable(getResources().getDrawable(R.drawable.selected));
        stage2_Diff_Address.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
        stage2_same_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage2_sameaddress = true;
                findViewById(R.id.different_address).setVisibility(View.GONE);
                stage2_same_Address.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                stage2_Diff_Address.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }
        });

        stage2_Diff_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage2_sameaddress = false;
                findViewById(R.id.different_address).setVisibility(View.VISIBLE);
                stage2_same_Address.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                stage2_Diff_Address.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            }
        });


        stage1_textView7.setVisibility(View.GONE);
        stage1_textView9.setVisibility(View.GONE);
        stage1_textView12.setVisibility(View.GONE);

        stage2_textView20.setVisibility(View.GONE);
        stage2_textView21.setVisibility(View.GONE);

        stage2_textView20.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_textView21.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));


        stage2_textView.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_textView2.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView3.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView4.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView5.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView6.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView7.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView8.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView9.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_textView10.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_textView11.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView12.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView13.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView14.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView15.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView16.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));

        stage2_textView17.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView18.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView19.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage2_textView22.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));

        stage2_provinsi_1 = (Button) findViewById(R.id.stage2_provisi_1);
        stage2_kota_kabu_1 = (Button) findViewById(R.id.stage2_kota_keb_1);
        stage2_kecamatan_1 = (Button) findViewById(R.id.stage2_kecamatan_1);
        stage2_desa_kelu_1 = (Button) findViewById(R.id.stage2_desa_kelu_1);
        stage2_provinsi_2 = (Button) findViewById(R.id.stage2_provisi_2);
        stage2_kota_kabu_2 = (Button) findViewById(R.id.stage2_kota_keb_2);
        stage2_kecamatan_2 = (Button) findViewById(R.id.stage2_kecamatan_2);
        stage2_desa_kelu_2 = (Button) findViewById(R.id.stage2_desa_kelu_2);

        stage2_provinsi_1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_kota_kabu_1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_kecamatan_1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_desa_kelu_1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_provinsi_2.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_kota_kabu_2.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_kecamatan_2.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_desa_kelu_2.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));

        stage2_rt_rw_1 = (EditText) findViewById(R.id.stage2_rt_rw_1);
        stage2_kode_pos_1 = (EditText) findViewById(R.id.stage2_kode_pos_1);
        stage2_rt_rw_2 = (EditText) findViewById(R.id.stage2_rt_rw_2);
        stage2_kode_pos_2 = (EditText) findViewById(R.id.stage2_kode_pos_2);
        stage2_alamat_sesuai_ktp_edit = (EditText) findViewById(R.id.stage2_alamat_sesuai_ktp_edit);
        stage2_different_alamat_sesuai_ktp_edit = (EditText) findViewById(R.id.stage2_different_alamat_sesuai_ktp_edit);
        stage2_name_edit = (EditText) findViewById(R.id.stage_2_editText);
        stage2_tempat_edit = (EditText) findViewById(R.id.stage_2_editText_1);
        stage2_rw_edit = (EditText) findViewById(R.id.stage2_rw_1);
        stage2_rw_different_edit = (EditText) findViewById(R.id.stage2_rw_2);

        stage2_rt_rw_1.setClickable(false);
        stage2_kode_pos_1.setClickable(false);
        stage2_rw_edit.setClickable(false);

        stage2_rt_rw_1.setFocusable(false);
        stage2_kode_pos_1.setFocusable(false);
        stage2_rw_edit.setFocusable(false);


        stage2_rt_rw_1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_rt_rw_2.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_kode_pos_1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_kode_pos_2.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_alamat_sesuai_ktp_edit.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_different_alamat_sesuai_ktp_edit.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_name_edit.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_tempat_edit.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_rw_edit.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage2_rw_different_edit.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        stage2_provinsi_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value = 4;
                new ProvinceAsynTask().execute();

            }
        });

        stage2_kota_kabu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diff_Province = stage2_provinsi_2.getText().toString();
                if (stage2_provinsi_2.getText().toString().length() <= 0) {
                    stage2_provinsi_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                } else {
                    stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                    value = 1;
                    new OtherLocationsAsynTask().execute();
                }
            }
        });

        stage2_kecamatan_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diff_Province = stage2_provinsi_2.getText().toString();
                diff_City = stage2_kota_kabu_2.getText().toString();
                if (stage2_provinsi_2.getText().toString().length() <= 0) {
                    stage2_provinsi_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                } else if (stage2_kota_kabu_2.getText().toString().length() <= 0) {
                    stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                    stage2_kota_kabu_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                } else {
                    stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                    stage2_kota_kabu_2.setBackgroundResource(R.drawable.edittext_background);
                    value = 2;
                    new OtherLocationsAsynTask().execute();
                }
            }
        });

        stage2_desa_kelu_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diff_Province = stage2_provinsi_2.getText().toString();
                diff_City = stage2_kota_kabu_2.getText().toString();
                diff_disctrict = stage2_kecamatan_2.getText().toString();
                if (stage2_provinsi_2.getText().toString().length() <= 0) {
                    stage2_provinsi_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                } else if (stage2_kota_kabu_2.getText().toString().length() <= 0) {
                    stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                    stage2_kota_kabu_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                } else if (stage2_kecamatan_2.getText().toString().length() <= 0) {
                    stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                    stage2_kota_kabu_2.setBackgroundResource(R.drawable.edittext_background);
                    stage2_kecamatan_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                } else {
                    stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                    stage2_kota_kabu_2.setBackgroundResource(R.drawable.edittext_background);
                    stage2_kecamatan_2.setBackgroundResource(R.drawable.edittext_background);
                    value = 3;
                    new OtherLocationsAsynTask().execute();
                }
            }
        });


//        Stage-3
        stage3_textView = (TextView) findViewById(R.id.stage3_textview);
        stage3_textView1 = (TextView) findViewById(R.id.stage3_textview1);
        stage3_textView2 = (TextView) findViewById(R.id.stage3_textview2);
        stage3_textView3 = (TextView) findViewById(R.id.stage3_textview3);
        stage3_textView4 = (TextView) findViewById(R.id.stage3_textview4);
        stage3_textView5 = (TextView) findViewById(R.id.stage3_textview5);
        stage3_textView6 = (TextView) findViewById(R.id.stage3_textview6);
//        stage3_textView7 = (TextView) findViewById(R.id.stage3_textview7);
        stage3_textView8 = (TextView) findViewById(R.id.stage3_textview8);
        stage3_textview9 = (TextView) findViewById(R.id.stage3_textview9);


        stage3_textView.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));
        stage3_textView1.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));
        stage3_textView2.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));
        stage3_textView3.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage3_textView4.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage3_textView5.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage3_textView6.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
//        stage3_textView7.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage3_textView8.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        stage3_textview9.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));

        stage3_button = (Button) findViewById(R.id.stage3_button);
        stage3_button1 = (Button) findViewById(R.id.stage3_button1);

        stage3_editText = (EditText) findViewById(R.id.stage3_editText);
        stage3_editText1 = (EditText) findViewById(R.id.stage3_editText1);
        stage3_editText2 = (EditText) findViewById(R.id.stage3_editText2);
        stage3_editText3 = (EditText) findViewById(R.id.stage3_editText3);
        stage3_editText4 = (EditText) findViewById(R.id.stage3_editText4);
//        stage3_editText5 = (EditText) findViewById(R.id.stage3_editText5);
        stage3_editText6 = (EditText) findViewById(R.id.stage3_editText6);

        stage3_button.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));
        stage3_button1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage3_editText.setTypeface(Utility.LightTextFormat(AgentRegistrationActivity.this));
        stage3_editText1.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage3_editText2.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage3_editText3.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage3_editText4.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
//        stage3_editText5.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));
        stage3_editText6.setTypeface(Utility.Robot_Light(AgentRegistrationActivity.this));

        document_image_1 = (ImageView) findViewById(R.id.document_image_1);
        document_image_2 = (ImageView) findViewById(R.id.document_image_2);
        document_image_3 = (ImageView) findViewById(R.id.document_image_3);

        stage_1.setVisibility(View.VISIBLE);
        stage_2.setVisibility(View.GONE);
        stage_3.setVisibility(View.GONE);
        new UserWorkAsynTask().execute();
        stage3_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size() > 0) {
                    WorkDisplay();
                }
            }
        });

        tab_2_text.setTextColor(getResources().getColor(R.color.reg_nonfocus));
        tab_3_text.setTextColor(getResources().getColor(R.color.reg_nonfocus));
        reg_2.setClickable(false);
        reg_3.setClickable(false);
        reg_2.setFocusable(false);
        reg_3.setFocusable(false);
        reg_2.setEnabled(false);
        reg_3.setEnabled(false);

        document_1 = (LinearLayout) findViewById(R.id.document_1);

        document_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documnetSelection = 1;
                List<String> list4 = new ArrayList<>(4);
                list4.add("Ambil Foto");
                list4.add("Pilih dari Pustaka Foto");
                final DialogGroup group4_1 = new DialogGroup(list4);
                DialogObject title = new DialogObject("Settings");
                title.setBgId(R.drawable.title_bg);
                title.setTextColor(getResources().getColor(android.R.color.white));
                DialogGroup group4_2 = new DialogGroup("Batal");
                pubDialogFragment = PubDialogFragment.newInstance(group4_1, group4_2);
                if (pubDialogFragment != null && !pubDialogFragment.isAdded()) {
                    pubDialogFragment.setItemClickListener(new PubDialogFragment.ItemClickListener() {
                        @Override
                        public void onItemClick(View clickedView, DialogObject dialogObject, int groupIndex, int itemIndex) {
                            if (groupIndex == 0) {
                                if (itemIndex == 1) {
                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                                } else if (itemIndex == 0) {
                                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                                    if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
                                        if (checkCallingOrSelfPermission(android.Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    109);
                                        } else {
                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent, REQUEST_CAMERA);
                                        }
                                    } else {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, REQUEST_CAMERA);
                                    }

                                }
                            } else if (groupIndex == 1) {
                                if (itemIndex == 0) {
                                    pubDialogFragment.dismiss();
                                }
                            }
                        }
                    });
                    pubDialogFragment.setCancelable(false);
                    pubDialogFragment.show(getSupportFragmentManager(), "setting");

                }

            }
        });
        document_2 = (LinearLayout) findViewById(R.id.document_2);

        document_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documnetSelection = 2;
                List<String> list4 = new ArrayList<>(4);
                list4.add("Ambil Foto");
                list4.add("Pilih dari Pustaka Foto");
                final DialogGroup group4_1 = new DialogGroup(list4);
                DialogObject title = new DialogObject("Settings");
                title.setBgId(R.drawable.title_bg);
                title.setTextColor(getResources().getColor(android.R.color.white));
                DialogGroup group4_2 = new DialogGroup("Batal");
                pubDialogFragment = PubDialogFragment.newInstance(group4_1, group4_2);
                if (pubDialogFragment != null && !pubDialogFragment.isAdded()) {
                    pubDialogFragment.setItemClickListener(new PubDialogFragment.ItemClickListener() {
                        @Override
                        public void onItemClick(View clickedView, DialogObject dialogObject, int groupIndex, int itemIndex) {
                            if (groupIndex == 0) {
                                if (itemIndex == 1) {
                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                                } else if (itemIndex == 0) {
                                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                                    if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
                                        if (checkCallingOrSelfPermission(android.Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    109);
                                        } else {
                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent, REQUEST_CAMERA);
                                        }
                                    } else {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, REQUEST_CAMERA);
                                    }

                                }
                            } else if (groupIndex == 1) {
                                if (itemIndex == 0) {
                                    pubDialogFragment.dismiss();
                                }
                            }
                        }
                    });
                    pubDialogFragment.setCancelable(false);
                    pubDialogFragment.show(getSupportFragmentManager(), "setting");

                }

            }
        });

        document_3 = (LinearLayout) findViewById(R.id.document_3);

        document_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documnetSelection = 3;
                List<String> list4 = new ArrayList<>(4);
                list4.add("Ambil Foto");
                list4.add("Pilih dari Pustaka Foto");
                final DialogGroup group4_1 = new DialogGroup(list4);
                DialogObject title = new DialogObject("Settings");
                title.setBgId(R.drawable.title_bg);
                title.setTextColor(getResources().getColor(android.R.color.white));
                DialogGroup group4_2 = new DialogGroup("Batal");
                pubDialogFragment = PubDialogFragment.newInstance(group4_1, group4_2);
                if (pubDialogFragment != null && !pubDialogFragment.isAdded()) {
                    pubDialogFragment.setItemClickListener(new PubDialogFragment.ItemClickListener() {
                        @Override
                        public void onItemClick(View clickedView, DialogObject dialogObject, int groupIndex, int itemIndex) {
                            if (groupIndex == 0) {
                                if (itemIndex == 1) {
                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                                } else if (itemIndex == 0) {
                                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                                    if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
                                        if (checkCallingOrSelfPermission(android.Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED) {

                                            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    109);
                                        } else {
                                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent, REQUEST_CAMERA);
                                        }
                                    } else {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, REQUEST_CAMERA);
                                    }

                                }
                            } else if (groupIndex == 1) {
                                if (itemIndex == 0) {
                                    pubDialogFragment.dismiss();
                                }
                            }
                        }
                    });
                    pubDialogFragment.setCancelable(false);
                    pubDialogFragment.show(getSupportFragmentManager(), "setting");

                }

            }
        });

        reg_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab_2_text.setTextColor(getResources().getColor(R.color.reg));
                tab_3_text.setTextColor(getResources().getColor(R.color.reg_nonfocus));
                stage_1.setVisibility(View.GONE);
                stage_2.setVisibility(View.VISIBLE);
                stage_3.setVisibility(View.GONE);
                reg_3.setEnabled(false);
                i = 1;
            }
        });

        reg_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage_1.setVisibility(View.GONE);
                stage_2.setVisibility(View.GONE);
                stage_3.setVisibility(View.VISIBLE);
            }
        });
        reg_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 0;
                tab_2_text.setTextColor(getResources().getColor(R.color.reg_nonfocus));
                tab_3_text.setTextColor(getResources().getColor(R.color.reg_nonfocus));
                stage_1.setVisibility(View.VISIBLE);
                stage_2.setVisibility(View.GONE);
                stage_3.setVisibility(View.GONE);
                reg_2.setEnabled(false);
                reg_3.setEnabled(false);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uptoValid = stage1_date.getText().toString().replace("-", "");
                dateofbirth = stage1_tanggal.getText().toString().replace("-", "");
                if (i == 0) {

                    if (stage1_Name.getText().toString().replace(" ", "").length() == 0) {

                        stage1_textView7.setVisibility(View.VISIBLE);
                        stage1_Name.setBackgroundResource(R.drawable.edit_text_alert_background);

                    } else if (dateofbirth.equals("") || dateofbirth.equals("DDMMYYYY")) {
                        stage1_textView14.setVisibility(View.VISIBLE);
                        stage1_tanggal.setBackgroundResource(R.drawable.edit_text_alert_background);
                        stage1_textView7.setVisibility(View.GONE);
                        stage1_Name.setBackgroundResource(R.drawable.edittext_background);
                    } /*else if (stage1_number.getText().toString().replace(" ", "").length() < 6) {
                        stage1_textView7.setVisibility(View.GONE);
                        stage1_Name.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView14.setVisibility(View.GONE);
                        stage1_tanggal.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView9.setVisibility(View.VISIBLE);
                        stage1_number.setBackgroundResource(R.drawable.edit_text_alert_background);

                    }*/ else if (stage1_number.getText().toString().replace(" ", "").length() != 16) {
                        stage1_textView7.setVisibility(View.GONE);
                        stage1_Name.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView14.setVisibility(View.GONE);
                        stage1_tanggal.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView9.setVisibility(View.VISIBLE);
                        stage1_number.setBackgroundResource(R.drawable.edit_text_alert_background);
                    } else if (limited_period && (uptoValid.equals("") || uptoValid.equals("DDMMYYYY"))) {

                        stage1_textView7.setVisibility(View.GONE);
                        stage1_Name.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView14.setVisibility(View.GONE);
                        stage1_tanggal.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView9.setVisibility(View.GONE);
                        stage1_number.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView12.setVisibility(View.VISIBLE);
                        stage1_date.setBackgroundResource(R.drawable.edit_text_alert_background);


                    } else if (stage1_nomorHp.getText().toString().replace(" ", "").length() < 10) {
                        stage1_textView7.setVisibility(View.GONE);
                        stage1_Name.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView14.setVisibility(View.GONE);
                        stage1_tanggal.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView9.setVisibility(View.GONE);
                        stage1_number.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView12.setVisibility(View.GONE);
                        stage1_date.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView16.setVisibility(View.VISIBLE);
                        stage1_nomorHp.setBackgroundResource(R.drawable.edit_text_alert_background);
                        Utility.displayDialog("Nomor handphone yang Anda masukkan harus 10-14 angka.",AgentRegistrationActivity.this);

                    } else if (stage1_nomorHp.getText().toString().replace(" ", "").length() > 14) {
                        stage1_textView7.setVisibility(View.GONE);
                        stage1_Name.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView14.setVisibility(View.GONE);
                        stage1_tanggal.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView9.setVisibility(View.GONE);
                        stage1_number.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView12.setVisibility(View.GONE);
                        stage1_date.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView16.setVisibility(View.VISIBLE);
                        stage1_nomorHp.setBackgroundResource(R.drawable.edit_text_alert_background);

                    } else {
                        stage1_textView7.setVisibility(View.GONE);
                        stage1_Name.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView14.setVisibility(View.GONE);
                        stage1_tanggal.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView9.setVisibility(View.GONE);
                        stage1_number.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView12.setVisibility(View.GONE);
                        stage1_date.setBackgroundResource(R.drawable.edittext_background);
                        stage1_textView16.setVisibility(View.GONE);
                        stage1_nomorHp.setBackgroundResource(R.drawable.edittext_background);
                        new AgentRegistrationAsynTask_1().execute();

                    }
                } else if (i == 1) {
                    ktp_Name = stage1_Name.getText().toString();
                    if (stage2_sameaddress) {

                        tab_2_text.setTextColor(getResources().getColor(R.color.reg));
                        tab_3_text.setTextColor(getResources().getColor(R.color.reg));
                        stage_1.setVisibility(View.GONE);
                        stage_2.setVisibility(View.GONE);
                        stage_3.setVisibility(View.VISIBLE);
                        reg_2.setEnabled(true);
                        reg_3.setEnabled(true);

                        ktp_Provinsi = stage2_provinsi_1.getText().toString();
                        ktp_City = stage2_kota_kabu_1.getText().toString();
                        ktp_district = stage2_kecamatan_1.getText().toString();
                        ktp_village = stage2_desa_kelu_1.getText().toString();
                        ktp_rt = stage2_rt_rw_1.getText().toString();
                        ktp_rw = stage2_rw_edit.getText().toString();
                        ktp_postalcode = stage2_kode_pos_1.getText().toString();
                        ktp_AddressCode = stage2_alamat_sesuai_ktp_edit.getText().toString();


                        i++;
                    } else {
                        diff_AddressCode = stage2_different_alamat_sesuai_ktp_edit.getText().toString();
                        diff_Province = stage2_provinsi_2.getText().toString();
                        diff_City = stage2_kota_kabu_2.getText().toString();
                        diff_disctrict = stage2_kecamatan_2.getText().toString();
                        diff_village = stage2_desa_kelu_2.getText().toString();
                        diff_Rt = stage2_rt_rw_2.getText().toString();
                        diff_Rw = stage2_rw_different_edit.getText().toString();
                        diff_PostalCode = stage2_kode_pos_2.getText().toString();

                        ktp_Provinsi = stage2_provinsi_1.getText().toString();
                        ktp_City = stage2_kota_kabu_1.getText().toString();
                        ktp_district = stage2_kecamatan_1.getText().toString();
                        ktp_village = stage2_desa_kelu_1.getText().toString();
                        ktp_rt = stage2_rt_rw_1.getText().toString();
                        ktp_rw = stage2_rw_edit.getText().toString();
                        ktp_postalcode = stage2_kode_pos_1.getText().toString();
                        ktp_AddressCode = stage2_alamat_sesuai_ktp_edit.getText().toString();

                        if (diff_AddressCode.replace(" ", "").length() == 0) {
                            stage2_different_alamat_sesuai_ktp_edit.setBackgroundResource(R.drawable.edit_text_alert_background);
                        } else if (diff_Province.length() <= 0) {
                            stage2_different_alamat_sesuai_ktp_edit.setBackgroundResource(R.drawable.edittext_background);
                            stage2_provinsi_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                        } else if (diff_City.length() <= 0) {
                            stage2_different_alamat_sesuai_ktp_edit.setBackgroundResource(R.drawable.edittext_background);
                            stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kota_kabu_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                        } else if (diff_disctrict.length() <= 0) {
                            stage2_different_alamat_sesuai_ktp_edit.setBackgroundResource(R.drawable.edittext_background);
                            stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kota_kabu_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kecamatan_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                        } else if (diff_village.length() <= 0) {
                            stage2_different_alamat_sesuai_ktp_edit.setBackgroundResource(R.drawable.edittext_background);
                            stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kota_kabu_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kecamatan_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_desa_kelu_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                        } /*else if (diff_Rt.length() <= 0) {
                            stage2_different_alamat_sesuai_ktp_edit.setBackgroundResource(R.drawable.edittext_background);
                            stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kota_kabu_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kecamatan_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_desa_kelu_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_rt_rw_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                        } else if (diff_Rw.length() <= 0) {
                            stage2_different_alamat_sesuai_ktp_edit.setBackgroundResource(R.drawable.edittext_background);
                            stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kota_kabu_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kecamatan_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_desa_kelu_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_rt_rw_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_rw_different_edit.setBackgroundResource(R.drawable.edit_text_alert_background);
                        }*/ else if (diff_PostalCode.length() <= 0) {
                            stage2_different_alamat_sesuai_ktp_edit.setBackgroundResource(R.drawable.edittext_background);
                            stage2_provinsi_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kota_kabu_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kecamatan_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_desa_kelu_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_rt_rw_2.setBackgroundResource(R.drawable.edittext_background);
                            stage2_rw_different_edit.setBackgroundResource(R.drawable.edittext_background);
                            stage2_kode_pos_2.setBackgroundResource(R.drawable.edit_text_alert_background);
                        } else {
                            tab_2_text.setTextColor(getResources().getColor(R.color.reg));
                            tab_3_text.setTextColor(getResources().getColor(R.color.reg));
                            stage_1.setVisibility(View.GONE);
                            stage_2.setVisibility(View.GONE);
                            stage_3.setVisibility(View.VISIBLE);
                            reg_2.setEnabled(true);
                            reg_3.setEnabled(true);
                            stage_3.invalidate();
                            i++;
                        }
                    }

                } else if (i == 2) {
                    Subrscriber_work_otherThanlainnya = stage3_button1.getText().toString();
                    subscriberwork = stage3_editText1.getText().toString();
                    incomeAmount = stage3_editText2.getText().toString().replace("Rp ", "");
                    openingAccount = stage3_editText3.getText().toString();
                    sourceFund = stage3_editText4.getText().toString();
                    emailId = stage3_editText6.getText().toString();
                    if (Subrscriber_work_otherThanlainnya.length() <= 0) {
                        stage3_button1.setBackgroundResource(R.drawable.edit_text_alert_background);
                    } else if ((Subrscriber_work_otherThanlainnya.equalsIgnoreCase("Lainnya") && subscriberwork.length() <= 0)) {
                        stage3_button1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText1.setBackgroundResource(R.drawable.edit_text_alert_background);
                        stage3_editText2.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText3.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText4.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText6.setBackgroundResource(R.drawable.edittext_background);
                    } else if (incomeAmount.replace(" ", "").length() <= 0) {
                        stage3_button1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText2.setBackgroundResource(R.drawable.edit_text_alert_background);
                        stage3_editText3.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText4.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText6.setBackgroundResource(R.drawable.edittext_background);
                    } /*else if (openingAccount.replace(" ", "").length() <= 0) {
                        stage3_button1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText2.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText3.setBackgroundResource(R.drawable.edit_text_alert_background);
                    } else if (sourceFund.replace(" ", "").length() <= 0) {
                        stage3_button1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText2.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText3.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText4.setBackgroundResource(R.drawable.edit_text_alert_background);
                    }*/ else if (!(emailId.length() <= 0 || Utility.emailValidator(emailId))) {
                        stage3_button1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText2.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText3.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText4.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText6.setBackgroundResource(R.drawable.edit_text_alert_background);
                    } else if (ktpDocument == null) {
                        stage3_button1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText2.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText3.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText4.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText6.setBackgroundResource(R.drawable.edittext_background);
                        document_1.setBackgroundResource(R.drawable.red_dottedlines);
                    } else if (subscriberFormDocument == null) {
                        stage3_button1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText2.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText3.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText4.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText6.setBackgroundResource(R.drawable.edittext_background);
                        document_1.setBackgroundResource(R.drawable.dottedlines);
                        document_2.setBackgroundResource(R.drawable.red_dottedlines);
                    } else {
                        stage3_button1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText1.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText2.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText3.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText4.setBackgroundResource(R.drawable.edittext_background);
                        stage3_editText6.setBackgroundResource(R.drawable.edittext_background);
                        document_1.setBackgroundResource(R.drawable.dottedlines);
                        document_2.setBackgroundResource(R.drawable.dottedlines);
//                        new AgentRegistrationAsynTask().execute();

                        HashMap<String, String> mapContainer = new HashMap<String, String>();
                        mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                                Constants.CONSTANT_CHANNEL_ID);
                        mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                                Constants.SERVICE_AGENT);
                        mapContainer.put(Constants.PARAMETER_TRANSACTIONID, TransactionID);

                        mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
                        mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
                        if (limited_period) {
                            mapContainer.put(Constants.PARAMETER_KTPVALIDUNTIL, uptoValid);
                            mapContainer.put(Constants.PARAMETER_KTPLIFETIME, "false");
                        } else {
                            mapContainer.put(Constants.PARAMETER_KTPVALIDUNTIL, "");
                            mapContainer.put(Constants.PARAMETER_KTPLIFETIME, "true");
                        }
                        mapContainer.put(Constants.PARAMETER_SOURCEOFFUNDS, sourceFund);
                        mapContainer.put(Constants.PARAMETER_INCOME, incomeAmount);
                        mapContainer.put(Constants.PARAMETER_OPENINGACCOUNT, openingAccount);
                        mapContainer.put(Constants.PARAMETER_EMAIL, emailId);
                        if (Subrscriber_work_otherThanlainnya.equalsIgnoreCase("Lainnya")) {
                            mapContainer.put(Constants.PARAMETER_WORK, Subrscriber_work_otherThanlainnya);
                            mapContainer.put(Constants.PARAMETER_OTHER_WORK, subscriberwork);
                        } else {
                            mapContainer.put(Constants.PARAMETER_WORK, Subrscriber_work_otherThanlainnya);
                        }
                        mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_SUBSCRIBERREGISTRATION);
                        mapContainer.put(Constants.PARAMETER_SUB_FIRSTNAME, ktp_Name.toString());
                        mapContainer.put(Constants.PARAMETER_DEST_MDN, destNumber);
                        mapContainer.put(Constants.PARAMETER_KTPID, ktpID);
                        mapContainer.put(Constants.PARAMETER_DOB, dateofbirth);
                        mapContainer.put(Constants.PARAMETER_MOTHERSMAIDENNAME, mothersName);
                        if (stage2_sameaddress) {
                            mapContainer.put(Constants.PARAMETER_DOMESTIC_IDENTITY, "1");


                            mapContainer.put(Constants.PARAMETER_KTPRW, ktp_rw);
                            mapContainer.put(Constants.PARAMETER_KTPLINE1, ktp_AddressCode);
                            mapContainer.put(Constants.PARAMETER_KTPRT, ktp_rt);
                            mapContainer.put(Constants.PARAMETER_KTPREGIONNAME, ktp_Provinsi);
                            mapContainer.put(Constants.PARAMETER_KTPSTATE, ktp_district);
                            mapContainer.put(Constants.PARAMETER_KTPZIPCODE, ktp_postalcode);
                            mapContainer.put(Constants.PARAMETER_KTPCITY, ktp_City);
                            mapContainer.put(Constants.PARAMETER_KTPSUBSTATE, ktp_village);
                            mapContainer.put(Constants.PARAMETER_KTPCOUNTRY, "");
                        } else {
                            mapContainer.put(Constants.PARAMETER_DOMESTIC_IDENTITY, "2");

                            mapContainer.put(Constants.PARAMETER_KTPRW, ktp_rw);
                            mapContainer.put(Constants.PARAMETER_KTPLINE1, ktp_AddressCode);
                            mapContainer.put(Constants.PARAMETER_KTPRT, ktp_rt);
                            mapContainer.put(Constants.PARAMETER_KTPREGIONNAME, ktp_Provinsi);
                            mapContainer.put(Constants.PARAMETER_KTPSTATE, ktp_district);
                            mapContainer.put(Constants.PARAMETER_KTPZIPCODE, ktp_postalcode);
                            mapContainer.put(Constants.PARAMETER_KTPCITY, ktp_City);
                            mapContainer.put(Constants.PARAMETER_KTPSUBSTATE, ktp_village);
                            mapContainer.put(Constants.PARAMETER_KTPCOUNTRY, "");

                            mapContainer.put(Constants.PARAMETER_DIFF_STATE, diff_disctrict);
                            mapContainer.put(Constants.PARAMETER_DIFF_CITY, diff_City);
                            mapContainer.put(Constants.PARAMETER_DIFF_ZIPCODE, diff_PostalCode);
                            mapContainer.put(Constants.PARAMETER_DIFF_COUNTRY, "");
                            mapContainer.put(Constants.PARAMETER_DIFF_RT, diff_Rt);
                            mapContainer.put(Constants.PARAMETER_DIFF_RW, diff_Rw);
                            mapContainer.put(Constants.PARAMETER_DIFF_SUB_STATE, diff_village);
                            mapContainer.put(Constants.PARAMETER_DIFF_LINE1, diff_AddressCode);
                            mapContainer.put(Constants.PARAMETER_DIFF_REGIONNAME, diff_Province);

                        }
                        intent = new Intent(AgentRegistrationActivity.this, Register_To_SimaspayUserConfirmationActivity.class);
                        JSONObject jsonObject = new JSONObject(mapContainer);
                        String jsonString = jsonObject.toString();
                        SharedPreferences.Editor editor = transferData.edit();
                        editor.remove("My_map").commit();
                        editor.putString("My_map", jsonString);
                        editor.commit();
                        if (ktpDocument == null) {
                            editor.putString("ktpBitmap", "");
                        } else {
                            editor.putString("ktpBitmap", (ktpDocument));

                        }
                        if (subscriberFormDocument == null) {
                            editor.putString("subscriberBitmap", "");
                        } else {
                            editor.putString("subscriberBitmap", (subscriberFormDocument));
                        }
                        if (supportingDocument == null) {
                            editor.putString("supportedBitmap", "");
                        } else {
                            editor.putString("supportedBitmap", (supportingDocument));
                        }
                        editor.commit();

                        intent.putExtra("KTPName", stage1_Name.getText().toString());
                        intent.putExtra("birthplace", ktp_bithPlace);
                        intent.putExtra("ktp_addressline", stage2_alamat_sesuai_ktp_edit.getText().toString());
                        startActivityForResult(intent, 10);
                    }


                }


            }
        });
    }


    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String showDay = "", showMonth = "";
        if (day <= 9) {
            showDay = "0" + day;
        } else {
            showDay = "" + day;
        }
        month = month + 1;
        if (month <= 9) {
            showMonth = "0" + month;
        } else {
            showMonth = "" + month;
        }
        if (dob_or_limit) {
            long milli = milliseconds(year + "-" + month + "-" + showDay);
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            int ageValue = getAge(milli);
//            if (ageValue < 18) {
//                Toast.makeText(AgentRegistrationActivity.this, "Age should not be less than 18 years", Toast.LENGTH_LONG).show();
//            } else {
//            }
            Log.e("=====" + mYear + "----" + mMonth + "=====" + mDay, "=====" + year + "=====" + month + "-----" + day);
            if ((mYear > year)
                    || (((mMonth + 1) > month) && (mYear == year))
                    || ((mDay >= day) && (mYear == year) && ((mMonth + 1) == month))) {
                stage1_tanggal.setText(showDay + "-" + showMonth + "-" + year);
            } else {
                Toast.makeText(AgentRegistrationActivity.this, "Silakan pilih tanggal yang valid", Toast.LENGTH_LONG).show();
            }

        } else {
            stage1_date.setText(showDay + "-" + showMonth + "-" + year);
        }


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
//        Toast.makeText(AgentRegistrationActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }


    class ProvinceAsynTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETTHIRDPARTYDATA);
            mapContainer.put(Constants.PARAMETER_CATEGORY, Constants.TRANSACTION_PROVICE_DATA);
            mapContainer.put(Constants.PARAMETER_VERSION, Constants.CONSTANT_VALUE_ZERO);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, AgentRegistrationActivity.this);

            areasString = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (areasString != null) {
                Log.e("========", "======" + areasString);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(areasString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject != null) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = jsonObject.getJSONObject("indonesia").getJSONArray("province");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    provinceList.clear();
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ArealData arealData = new ArealData();
                            try {
                                arealData.setProvince_name(jsonArray.getJSONObject(i).getString("province_name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            provinceList.add(arealData);
                        }

                        Log.e("=======", "=----" + provinceList.size());
                        if (provinceList.size() > 0) {
                            LocationsDisplay();
                        }
                    }
                }
            } else {
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), AgentRegistrationActivity.this);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class OtherLocationsAsynTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETTHIRDPARTYLCOATION);
            if (diff_Province != null && value == 1) {
                mapContainer.put(Constants.PARAMETER_STATE, diff_Province);
            }
            if (diff_City != null && value == 2) {
                mapContainer.put(Constants.PARAMETER_STATE, diff_Province);
                mapContainer.put(Constants.PARAMETER_REGIONNAME, diff_City);
            } else {
                mapContainer.put(Constants.PARAMETER_REGIONNAME, "");
            }
            if (diff_disctrict != null && value == 3) {
                mapContainer.put(Constants.PARAMETER_STATE, diff_Province);
                mapContainer.put(Constants.PARAMETER_REGIONNAME, diff_City);
                mapContainer.put(Constants.PARAMETER_CITY, diff_disctrict);
            } else {
                mapContainer.put(Constants.PARAMETER_CITY, "");
            }


            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, AgentRegistrationActivity.this);

            otherLocationsString = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (otherLocationsString != null) {
                Log.e("========", "======" + otherLocationsString);

                if (otherLocationsString.startsWith("<?xml")) {
                    XMLParser obj = new XMLParser();
                    EncryptedResponseDataContainer responseContainer = null;
                    try {
                        responseContainer = obj.parse(otherLocationsString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        msgCode = Integer.parseInt(responseContainer
                                .getMsgCode());
                    } catch (Exception e) {
                        msgCode = 0;
                    }
                    Utility.networkDisplayDialog(responseContainer.getMsg(), AgentRegistrationActivity.this);
                } else {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(otherLocationsString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonObject != null) {
                        JSONArray jsonArray = null;
                        if (value == 1) {
                            try {
                                jsonArray = jsonObject.getJSONArray("region");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            regionList.clear();
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ArealData arealData = new ArealData();
                                    try {
                                        arealData.setRegion_name(jsonArray.getJSONObject(i).getString("region_name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    regionList.add(arealData);
                                }

                                Log.e("=======", "=----" + regionList.size());
                                if (regionList.size() > 0) {
                                    LocationsDisplay();
                                }
                            }
                        } else if (value == 2) {
                            try {
                                jsonArray = jsonObject.getJSONArray("district");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            districtList.clear();
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ArealData arealData = new ArealData();
                                    try {
                                        arealData.setDistrict_name(jsonArray.getJSONObject(i).getString("district_name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    districtList.add(arealData);
                                }

                                Log.e("=======", "=----" + districtList.size());
                                if (districtList.size() > 0) {
                                    LocationsDisplay();
                                }
                            }
                        } else if (value == 3) {
                            try {
                                jsonArray = jsonObject.getJSONArray("village");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            districtList.clear();
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ArealData arealData = new ArealData();
                                    try {
                                        arealData.setVillage_name(jsonArray.getJSONObject(i).getString("village_name"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    villagesList.add(arealData);
                                }

                                Log.e("=======", "=----" + villagesList.size());
                                if (villagesList.size() > 0) {
                                    LocationsDisplay();
                                }
                            }
                        }

                    }
                }
            } else {
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), AgentRegistrationActivity.this);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    ArrayList<ArealData> arrayList = new ArrayList<ArealData>();

    class UserWorkAsynTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_BILLPAYMENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_GETTHIRDPARTYDATA);
            mapContainer.put(Constants.PARAMETER_CATEGORY, Constants.TRANSACTION_WORK_DATA);
            mapContainer.put(Constants.PARAMETER_VERSION, Constants.CONSTANT_VALUE_ZERO);

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, AgentRegistrationActivity.this);

            workString = webServiceHttp.getResponseSSLCertificatation();
//            JSONParser jParser = new JSONParser();
//            workString =  jParser.getJsonData("https://dl.dropboxusercontent.com/u/93708740/b%20(1).json");

            Log.e("=====", "=====" + workString);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (workString != null) {
                if (workString.startsWith("<")) {

                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(workString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonObject != null) {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = jsonObject.getJSONArray("work_list");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        arrayList.clear();
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ArealData arealData = new ArealData();
                                try {
                                    arealData.setWork(jsonArray.getJSONObject(i).getString("workName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                arrayList.add(arealData);
                            }

                            if(arrayList.size()>0){
                                stage3_button1.setText(arrayList.get(0).getWork());
                            }
                        }


                    } else {
                        Utility.networkDisplayDialog(sharedPreferences.getString(
                                "ErrorMessage",
                                getResources().getString(
                                        R.string.bahasa_serverNotRespond)), AgentRegistrationActivity.this);
                    }
                }
            }else{
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), AgentRegistrationActivity.this);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }


    public void WorkDisplay() {

        dialogCustomWish = new Dialog(AgentRegistrationActivity.this);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        dialogCustomWish.setContentView(R.layout.province_list);

        Button button = (Button) dialogCustomWish.findViewById(R.id.ok);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.title);
        ListView listView = (ListView) dialogCustomWish.findViewById(R.id.locationsList);
        button.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        button1.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        textView.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        textView.setText("Pekerjaan");

        productsAdapter = new ProductsAdapter();
        listView.setAdapter(productsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selected_work = position;
                productsAdapter.notifyDataSetChanged();
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_work =0;
                dialogCustomWish.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();

                if (selected_work != -1) {
                    stage3_button1.setText(arrayList.get(selected_work).getWork());
                    if (arrayList.get(selected_work).getWork().equals("Lainnya")) {
                        stage3_editText1.setFocusableInTouchMode(true);
                        stage3_editText1.setClickable(true);
                        stage3_editText1.setFocusable(true);
                    } else {
                        stage3_editText1.setText("");
                        stage3_editText1.setClickable(false);
                        stage3_editText1.setFocusable(false);
                    }
                }else{
                    stage3_editText1.setText("");
                    stage3_editText1.setClickable(false);
                    stage3_editText1.setFocusable(false);
                }

            }
        });
        dialogCustomWish.show();


    }

    ProductsAdapter productsAdapter;

    class ProductsAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(AgentRegistrationActivity.this).inflate(R.layout.location_row, null);
            TextView textView = (TextView) view.findViewById(R.id.location_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_location);

            if (selected_work == position) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }

            textView.setText(arrayList.get(position).getWork());

            return view;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }


    String imgDecodableString;
    int RESULT_LOAD_IMG = 100;
    int REQUEST_CAMERA = 101;
    String response, agentRegistrationResponse;
    ProgressDialog progressDialog;
    int msgCode;

    Bitmap ktpBitmap, subscriberBitmap, supportedBitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        } else if (requestCode == RESULT_LOAD_IMG) {
            if (pubDialogFragment != null) {
                pubDialogFragment.dismiss();
            }
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (documnetSelection == 1) {
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    File imgFile = new File(imgDecodableString);
                    if ((imgFile.length() / 1024) < 80) {
                        document_image_1.setVisibility(View.VISIBLE);
                        findViewById(R.id.document_1_linearlayout).setVisibility(View.GONE);
                        document_image_1.setImageBitmap(BitmapFactory
                                .decodeFile(imgDecodableString));
                        ktpDocument = imgDecodableString;
                    } else {
                        document_image_1.setVisibility(View.GONE);
                        findViewById(R.id.document_1_linearlayout).setVisibility(View.VISIBLE);
                        Toast.makeText(AgentRegistrationActivity.this, "Iamge Size Should be  lessthan 80 KB", Toast.LENGTH_SHORT).show();
                        ktpDocument = "";
                    }
                } else if (documnetSelection == 2) {
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    File imgFile = new File(imgDecodableString);
                    Log.e("=====", "=====" + imgFile.length() / 1024);
                    if ((imgFile.length() / 1024) < 80) {
                        document_image_2.setVisibility(View.VISIBLE);
                        findViewById(R.id.document_2_linearlayout).setVisibility(View.GONE);
                        document_image_2.setImageBitmap(BitmapFactory
                                .decodeFile(imgDecodableString));
                        subscriberFormDocument = imgDecodableString;
                    } else {
                        document_image_2.setVisibility(View.GONE);
                        findViewById(R.id.document_2_linearlayout).setVisibility(View.VISIBLE);
                        Toast.makeText(AgentRegistrationActivity.this, "Iamge Size Should be  lessthan 80 KB", Toast.LENGTH_SHORT).show();
                        subscriberFormDocument = "";
                    }

                } else if (documnetSelection == 3) {
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    File imgFile = new File(imgDecodableString);
                    Log.e("=====", "=====" + imgFile.length() / 1024);
                    if ((imgFile.length() / 1024) < 80) {
                        document_image_3.setVisibility(View.VISIBLE);
                        findViewById(R.id.document_3_linearlayout).setVisibility(View.GONE);
                        document_image_3.setImageBitmap(BitmapFactory
                                .decodeFile(imgDecodableString));
                        supportingDocument = imgDecodableString;
                        Log.e("=====", "=====" + supportingDocument);
                    } else {
                        document_image_3.setVisibility(View.GONE);
                        findViewById(R.id.document_3_linearlayout).setVisibility(View.VISIBLE);
                        supportingDocument = "";
                        Toast.makeText(AgentRegistrationActivity.this, "Iamge Size Should be  lessthan 80 KB", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (pubDialogFragment != null) {
                pubDialogFragment.dismiss();
            }
            Log.e("========","======="+data);
            if (data != null && data.getExtras() != null) {
                if (documnetSelection == 1) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    Uri tempUri = getImageUri(getApplicationContext(), thumbnail);
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    byte[] imageInByte = bytes.toByteArray();
                    long lengthbmp = imageInByte.length;
                    Log.e("====camera="+lengthbmp/1024, "=====" );
                    if((lengthbmp/1024)<80) {
                        document_image_1.setVisibility(View.VISIBLE);
                        findViewById(R.id.document_1_linearlayout).setVisibility(View.GONE);
                        document_image_1.setImageBitmap(thumbnail);
                        ktpDocument = "" + finalFile;
                    }else{
                        document_image_1.setVisibility(View.GONE);
                        findViewById(R.id.document_1_linearlayout).setVisibility(View.VISIBLE);
                        ktpDocument="";
                        Toast.makeText(AgentRegistrationActivity.this, "Iamge Size Should be  lessthan 80 KB", Toast.LENGTH_SHORT).show();
                    }



                } else if (documnetSelection == 2) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    Uri tempUri = getImageUri(getApplicationContext(), thumbnail);
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    byte[] imageInByte = bytes.toByteArray();
                    long lengthbmp = imageInByte.length;
                    Log.e("====camera="+lengthbmp/1024, "=====" );
                    if((lengthbmp/1024)<80) {
                        document_image_2.setVisibility(View.VISIBLE);
                        findViewById(R.id.document_2_linearlayout).setVisibility(View.GONE);
                        document_image_2.setImageBitmap(thumbnail);
                        subscriberFormDocument = "" + finalFile;
                    }else{
                        document_image_2.setVisibility(View.GONE);
                        findViewById(R.id.document_2_linearlayout).setVisibility(View.VISIBLE);
                        subscriberFormDocument="";
                        Toast.makeText(AgentRegistrationActivity.this, "Iamge Size Should be  lessthan 80 KB", Toast.LENGTH_SHORT).show();
                    }

                } else if (documnetSelection == 3) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    Uri tempUri = getImageUri(getApplicationContext(), thumbnail);
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    byte[] imageInByte = bytes.toByteArray();
                    long lengthbmp = imageInByte.length;
                    Log.e("====camera="+lengthbmp/1024, "=====" );
                    if((lengthbmp/1024)<80) {
                        document_image_3.setVisibility(View.VISIBLE);
                        findViewById(R.id.document_3_linearlayout).setVisibility(View.GONE);
                        document_image_3.setImageBitmap(thumbnail);
                        supportingDocument = "" + finalFile;
                    }else{
                        document_image_3.setVisibility(View.GONE);
                        findViewById(R.id.document_3_linearlayout).setVisibility(View.VISIBLE);
                        supportingDocument="";
                        Toast.makeText(AgentRegistrationActivity.this, "Iamge Size Should be  lessthan 80 KB", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        }
    }



    public Uri getImageUri(Context inContext, Bitmap mBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), mBitmap, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
//        Log.e("=======","-------"+uri);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    String Name, destNumber, ktpID, dateofbirth = "", uptoValid = "";

    class AgentRegistrationAsynTask_1 extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            /*destMDN=6212345108&ktpId=12345678&dob=22011979
                   subFirstName=Sun&txnName=SubscriberKTPValidation*/
            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_AGENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME,
                    Constants.TRANSACTION_KTPVALIDATION);
            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            mapContainer.put(Constants.PARAMETER_SUB_FIRSTNAME, Name.toString());
            mapContainer.put(Constants.PARAMETER_DEST_MDN, destNumber);
            mapContainer.put(Constants.PARAMETER_KTPID, ktpID);
            mapContainer.put(Constants.PARAMETER_DOB, dateofbirth);
            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, AgentRegistrationActivity.this);

            response = webServiceHttp.getResponseSSLCertificatation();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Name = stage1_Name.getText().toString();
            destNumber = stage1_nomorHp.getText().toString();
            ktpID = stage1_number.getText().toString();


            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (response != null) {
                Log.e("-------", "---------" + response);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
                if (msgCode == 2126) {
                    progressDialog.dismiss();

                    mothersName = responseContainer.getMothersMaidenName();
                    stage2_provinsi_1.setText(responseContainer.getProvince());
                    stage2_name_edit.setText(responseContainer.getMothersMaidenName());
                    stage2_alamat_sesuai_ktp_edit.setText(responseContainer.getAddressLine());
                    stage2_kota_kabu_1.setText(responseContainer.getCity());
                    stage2_kecamatan_1.setText(responseContainer.getDistrict());
                    stage2_desa_kelu_1.setText(responseContainer.getSubDistrict());
                    stage2_rt_rw_1.setText(responseContainer.getRt());
                    stage2_rw_edit.setText(responseContainer.getRw());
                    stage2_kode_pos_1.setText(responseContainer.getPostalCode());
                    TransactionID = responseContainer.getTransactionId();
                    stage2_provinsi_1.setClickable(false);
                    stage2_provinsi_1.setFocusable(false);

                    stage2_name_edit.setClickable(false);
                    stage2_name_edit.setFocusable(false);

                    stage2_alamat_sesuai_ktp_edit.setClickable(false);
                    stage2_alamat_sesuai_ktp_edit.setFocusable(false);

                    stage2_kota_kabu_1.setClickable(false);
                    stage2_kota_kabu_1.setFocusable(false);

                    stage2_kecamatan_1.setClickable(false);
                    stage2_kecamatan_1.setFocusable(false);

                    stage2_desa_kelu_1.setClickable(false);
                    stage2_desa_kelu_1.setFocusable(false);

                    stage2_rt_rw_1.setClickable(false);
                    stage2_rt_rw_1.setFocusable(false);

                    stage2_rw_edit.setClickable(false);
                    stage2_rw_edit.setFocusable(false);

                    stage2_kode_pos_1.setClickable(false);
                    stage2_kode_pos_1.setFocusable(false);
                    ktp_bithPlace = responseContainer.getBirthPlace();
                    stage2_tempat_edit.setText(ktp_bithPlace);
                    stage2_tempat_edit.setClickable(false);
                    stage2_tempat_edit.setFocusable(false);


                    i++;

                    tab_2_text.setTextColor(getResources().getColor(R.color.reg));
                    tab_3_text.setTextColor(getResources().getColor(R.color.reg_nonfocus));
                    stage_1.setVisibility(View.GONE);
                    stage_2.setVisibility(View.VISIBLE);
                    stage_3.setVisibility(View.GONE);
                    reg_2.setEnabled(true);
                    reg_3.setEnabled(false);
                }else if (msgCode == 631) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(AgentRegistrationActivity.this, SessionTimeOutActivity.class);
                    startActivityForResult(intent, 40);
                } else {
                    progressDialog.dismiss();
                    if (responseContainer.getMsg() == null) {
                        Utility.networkDisplayDialog(
                                sharedPreferences.getString(
                                        "ErrorMessage",
                                        getResources()
                                                .getString(
                                                        R.string.server_error_message)),
                                AgentRegistrationActivity.this);
                    } else {
                        Utility.networkDisplayDialog(
                                responseContainer.getMsg(), AgentRegistrationActivity.this);
                    }
                }

            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), AgentRegistrationActivity.this);
            }
        }
    }


    /*class AgentRegistrationAsynTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> mapContainer = new HashMap<String, String>();
            mapContainer.put(Constants.PARAMETER_CHANNEL_ID,
                    Constants.CONSTANT_CHANNEL_ID);
            mapContainer.put(Constants.PARAMETER_SERVICE_NAME,
                    Constants.SERVICE_AGENT);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONID, TransactionID);

            mapContainer.put(Constants.PARAMETER_SOURCE_MDN, sharedPreferences.getString("mobileNumber", ""));
            mapContainer.put(Constants.PARAMETER_SOURCE_PIN, sharedPreferences.getString("password", ""));
            if (limited_period) {
                mapContainer.put(Constants.PARAMETER_KTPVALIDUNTIL, uptoValid);
                mapContainer.put(Constants.PARAMETER_KTPLIFETIME, "false");
            } else {
                mapContainer.put(Constants.PARAMETER_KTPVALIDUNTIL, "");
                mapContainer.put(Constants.PARAMETER_KTPLIFETIME, "true");
            }
            mapContainer.put(Constants.PARAMETER_SOURCEOFFUNDS, sourceFund);
            mapContainer.put(Constants.PARAMETER_INCOME, incomeAmount);
            mapContainer.put(Constants.PARAMETER_OPENINGACCOUNT, openingAccount);
            mapContainer.put(Constants.PARAMETER_EMAIL, emailId);
            mapContainer.put(Constants.PARAMETER_WORK, subscriberwork);
            mapContainer.put(Constants.PARAMETER_TRANSACTIONNAME, Constants.TRANSACTION_SUBSCRIBERREGISTRATION);
            mapContainer.put(Constants.PARAMETER_SUB_FIRSTNAME, ktp_Name.toString());
            mapContainer.put(Constants.PARAMETER_DEST_MDN, destNumber);
            mapContainer.put(Constants.PARAMETER_KTPID, ktpID);
            mapContainer.put(Constants.PARAMETER_DOB, dateofbirth);
            mapContainer.put(Constants.PARAMETER_MOTHERSMAIDENNAME, mothersName);
            if (ktpDocument == null) {
                mapContainer.put(Constants.PARAMETER_KTPDOCUMENT, "");
            } else {
                mapContainer.put(Constants.PARAMETER_KTPDOCUMENT, ktpDocument);
            }
            if (subscriberFormDocument == null) {
                mapContainer.put(Constants.PARAMETER_SUBSCRIBER_DOCUMENT, "");
            } else {
                mapContainer.put(Constants.PARAMETER_SUBSCRIBER_DOCUMENT, subscriberFormDocument);
            }
            if (supportingDocument == null) {
                mapContainer.put(Constants.PARAMETER_SUPPORTDOCUMENT, "");
            } else {
                mapContainer.put(Constants.PARAMETER_SUPPORTDOCUMENT, supportingDocument);
            }


            if (stage2_sameaddress) {
                mapContainer.put(Constants.PARAMETER_DOMESTIC_IDENTITY, "1");
            } else {
                mapContainer.put(Constants.PARAMETER_DOMESTIC_IDENTITY, "2");

                mapContainer.put(Constants.PARAMETER_KTPRW, ktp_rw);
                mapContainer.put(Constants.PARAMETER_KTPLINE1, ktp_AddressCode);
                mapContainer.put(Constants.PARAMETER_KTPRT, ktp_rt);
                mapContainer.put(Constants.PARAMETER_KTPSTATE, ktp_Provinsi);
                mapContainer.put(Constants.PARAMETER_KTPREGIONNAME, ktp_district);
                mapContainer.put(Constants.PARAMETER_KTPZIPCODE, ktp_postalcode);
                mapContainer.put(Constants.PARAMETER_KTPCITY, ktp_state);
                mapContainer.put(Constants.PARAMETER_KTPCOUNTRY, "");

                mapContainer.put(Constants.PARAMETER_DIFF_STATE, diff_Province);
                mapContainer.put(Constants.PARAMETER_DIFF_CITY, diff_State);
                mapContainer.put(Constants.PARAMETER_DIFF_ZIPCODE, diff_PostalCode);
                mapContainer.put(Constants.PARAMETER_DIFF_COUNTRY, "");
                mapContainer.put(Constants.PARAMETER_DIFF_RT, diff_Rt);
                mapContainer.put(Constants.PARAMETER_DIFF_RW, diff_Rw);
                mapContainer.put(Constants.PARAMETER_DIFF_LINE1, diff_AddressCode);
                mapContainer.put(Constants.PARAMETER_DIFF_REGIONNAME, diff_disctrict);

            }

            WebServiceHttp webServiceHttp = new WebServiceHttp(mapContainer, AgentRegistrationActivity.this);

            agentRegistrationResponse = webServiceHttp.getResponseSSLCertificatation();
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AgentRegistrationActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
            progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (agentRegistrationResponse != null) {
                Log.e("-------", "---------" + agentRegistrationResponse);
                XMLParser obj = new XMLParser();
                EncryptedResponseDataContainer responseContainer = null;
                try {
                    responseContainer = obj.parse(agentRegistrationResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    msgCode = Integer.parseInt(responseContainer
                            .getMsgCode());
                } catch (Exception e) {
                    msgCode = 0;
                }
            } else {
                progressDialog.dismiss();
                Utility.networkDisplayDialog(sharedPreferences.getString(
                        "ErrorMessage",
                        getResources().getString(
                                R.string.bahasa_serverNotRespond)), AgentRegistrationActivity.this);
            }
        }
    }*/

    Dialog dialogCustomWish;

    public void LocationsDisplay() {

        dialogCustomWish = new Dialog(AgentRegistrationActivity.this);
        dialogCustomWish.setCancelable(false);

        dialogCustomWish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomWish.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        dialogCustomWish.setContentView(R.layout.province_list);

        Button button = (Button) dialogCustomWish.findViewById(R.id.ok);
        Button button1 = (Button) dialogCustomWish.findViewById(R.id.Cancel);
        TextView textView = (TextView) dialogCustomWish.findViewById(R.id.title);
        ListView listView = (ListView) dialogCustomWish.findViewById(R.id.locationsList);
        button.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        button1.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));
        textView.setTypeface(Utility.RegularTextFormat(AgentRegistrationActivity.this));

        if (value == 1) {
            textView.setText("Kota/Kabupaten");
            regionsAdapter = new RegionsAdapter();
            listView.setAdapter(regionsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    stage2_kecamatan_2.setText("");
                    stage2_desa_kelu_2.setText("");
                    selected_region = position;
                    regionsAdapter.notifyDataSetChanged();
                }
            });
        } else if (value == 2) {
            textView.setText("Kecamatan");
            districtsAdapter = new DistrictsAdapter();
            listView.setAdapter(districtsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    stage2_desa_kelu_2.setText("");
                    selectedDistrict = position;
                    districtsAdapter.notifyDataSetChanged();
                }
            });
        } else if (value == 3) {
            textView.setText("Desa/Kelurahan");
            villagesAdapter = new VillagesAdapter();
            listView.setAdapter(villagesAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedVillage = position;
                    villagesAdapter.notifyDataSetChanged();
                }
            });
        } else {
            textView.setText("Provinsi");
            locationsAdapter = new LocationsAdapter();
            listView.setAdapter(locationsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    stage2_kota_kabu_2.setText("");
                    stage2_kecamatan_2.setText("");
                    stage2_desa_kelu_2.setText("");
                    selectedProvice = position;
                    locationsAdapter.notifyDataSetChanged();
                }
            });
        }


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedProvice = -1;
                dialogCustomWish.dismiss();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomWish.dismiss();
                if (value == 1) {
                    if (selected_region != -1) {
                        stage2_kota_kabu_2.setText(regionList.get(selected_region).getRegion_name());
                    }
                } else if (value == 2) {
                    if (selectedDistrict != -1) {
                        stage2_kecamatan_2.setText(districtList.get(selectedDistrict).getDistrict_name());
                    }
                } else if (value == 3) {
                    if (selectedVillage != -1) {
                        stage2_desa_kelu_2.setText(villagesList.get(selectedVillage).getVillage_name());
                    }
                } else {
                    if (selectedProvice != -1) {
                        stage2_provinsi_2.setText(provinceList.get(selectedProvice).getProvince_name());
                    }
                }
            }
        });
        dialogCustomWish.show();


    }


    LocationsAdapter locationsAdapter;
    RegionsAdapter regionsAdapter;
    DistrictsAdapter districtsAdapter;
    VillagesAdapter villagesAdapter;

    class LocationsAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(AgentRegistrationActivity.this).inflate(R.layout.location_row, null);
            TextView textView = (TextView) view.findViewById(R.id.location_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_location);

            if (selectedProvice == position) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }

            textView.setText(provinceList.get(position).getProvince_name());

            return view;
        }

        @Override
        public int getCount() {
            return provinceList.size();
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    class RegionsAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(AgentRegistrationActivity.this).inflate(R.layout.location_row, null);
            TextView textView = (TextView) view.findViewById(R.id.location_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_location);

            if (selected_region == position) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }

            textView.setText(regionList.get(position).getRegion_name());

            return view;
        }

        @Override
        public int getCount() {
            return regionList.size();
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    class DistrictsAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(AgentRegistrationActivity.this).inflate(R.layout.location_row, null);
            TextView textView = (TextView) view.findViewById(R.id.location_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_location);

            if (selectedDistrict == position) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }

            textView.setText(districtList.get(position).getDistrict_name());

            return view;
        }

        @Override
        public int getCount() {
            return districtList.size();
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    class VillagesAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(AgentRegistrationActivity.this).inflate(R.layout.location_row, null);
            TextView textView = (TextView) view.findViewById(R.id.location_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_location);

            if (selectedVillage == position) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }

            textView.setText(villagesList.get(position).getVillage_name());

            return view;
        }

        @Override
        public int getCount() {
            return villagesList.size();
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    int str_age;

    private int getAge(long selectedMilli) {
        Date dateOfBirth = new Date(selectedMilli);
        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dob
                .get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        if (age < 18) {

        } else {

        }

        str_age = age;
        Log.d("", ": Age in year= " + age);
        return str_age;
    }

    public long milliseconds(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {

            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            } else {

            }
        }
    }

    /*public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }*/
}
