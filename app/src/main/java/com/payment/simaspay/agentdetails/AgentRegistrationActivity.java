package com.payment.simaspay.agentdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.payment.simaspay.PojoClasses.ArealData;
import com.payment.simaspay.services.Utility;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pubdialog.DialogGroup;
import pubdialog.DialogObject;
import pubdialog.PubDialogFragment;
import simaspay.payment.com.simaspay.R;


/**
 * Created by Nagendra P on 1/7/2016.
 */
public class AgentRegistrationActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView stage1_textView, stage1_textView1, stage1_textView2, stage1_textView3, stage1_textView4, stage1_textView5, stage1_textView6, stage1_textView7, stage1_textView8, stage1_textView9, stage1_textView10, stage1_textView11, stage1_textView12, stage1_textView13, stage1_textView14, stage1_textView15, stage1_textView16;

    TextView tab_1_text, tab_2_text, tab_3_text;

    Button stage1_date, stage1_tanggal;

    EditText stage1_Name, stage1_number, stage1_nomorHp;

    LinearLayout limited, lifelong;

    ImageView manual_selection_checkbox, manual_selection_checkbox2;


    TextView stage2_textView, stage2_textView1, stage2_textView2, stage2_textView3, stage2_textView4, stage2_textView5, stage2_textView6, stage2_textView7, stage2_textView8, stage2_textView9, stage2_textView10, stage2_textView11, stage2_textView12, stage2_textView13, stage2_textView14, stage2_textView15, stage2_textView16, stage2_textView17, stage2_textView18, stage2_textView19, stage2_textView20, stage2_textView21, stage2_textView22;


    Button stage2_provinsi_1, stage2_kota_kabu_1, stage2_kecamatan_1, stage2_desa_kelu_1, stage2_provinsi_2, stage2_kota_kabu_2, stage2_kecamatan_2, stage2_desa_kelu_2;

    EditText stage2_rt_rw_1, stage2_kode_pos_1, stage2_rt_rw_2, stage2_kode_pos_2, stage2_alamat_sesuai_ktp_edit, stage2_different_alamat_sesuai_ktp_edit, stage2_name_edit, stage2_tempat_edit, stage2_rw_edit, stage2_rw_different_edit;

    ImageView stage2_same_Address, stage2_Diff_Address;

    boolean stage2_same_address = false, stage2_diff_address = false;


    TextView stage3_textView, stage3_textView1, stage3_textView2, stage3_textView3, stage3_textView4, stage3_textView5, stage3_textView6, stage3_textView7, stage3_textView8, stage3_textview9;

    Button stage3_button, stage3_button1;

    EditText stage3_editText, stage3_editText1, stage3_editText2, stage3_editText3, stage3_editText4, stage3_editText5, stage3_editText6;


    TextView textView;

    public static Button next;

    LinearLayout reg_1, reg_2, reg_3;
    LinearLayout back;

    LinearLayout stage_1, stage_2, stage_3;

    int i = 0;

    boolean limited_period = false, lifelong_period = false;

    LinearLayout document_1;

    PubDialogFragment pubDialogFragment;
    TimePickerDialog timePickerDialog;

    Calendar calendar;
    DatePickerDialog datePickerDialog, datePickerDialog_1;

    public String areasString;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    ArrayList<ArealData> provinceList = new ArrayList<>();
    ArrayList<ArealData> districtList = new ArrayList<>();
    ArrayList<ArealData> regionList = new ArrayList<>();
    ArrayList<ArealData> villagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_register);

        calendar = Calendar.getInstance();

        datePickerDialog = DatePickerDialog.newInstance(AgentRegistrationActivity.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        datePickerDialog_1 = DatePickerDialog.newInstance(AgentRegistrationActivity.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);


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

        stage1_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1950, calendar.get(Calendar.YEAR));
                datePickerDialog.setCloseOnSingleTapDay(true);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });


        stage1_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog_1.setVibrate(false);
                datePickerDialog_1.setYearRange(calendar.get(Calendar.YEAR), 2050);
                datePickerDialog_1.setCloseOnSingleTapDay(true);
                datePickerDialog_1.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        limited_period = true;
        lifelong_period = false;
        manual_selection_checkbox.setImageDrawable(getResources().getDrawable(R.drawable.selected));
        manual_selection_checkbox2.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
        limited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limited_period = true;
                lifelong_period = false;
                manual_selection_checkbox.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                manual_selection_checkbox2.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }
        });

        lifelong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limited_period = false;
                lifelong_period = true;
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
        stage2_same_address = true;
        stage2_diff_address = false;
        findViewById(R.id.different_address).setVisibility(View.GONE);
        stage2_same_Address.setImageDrawable(getResources().getDrawable(R.drawable.selected));
        stage2_Diff_Address.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
        stage2_same_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage2_same_address = true;
                stage2_diff_address = false;
                findViewById(R.id.different_address).setVisibility(View.GONE);
                stage2_same_Address.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                stage2_Diff_Address.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
            }
        });

        stage2_Diff_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage2_same_address = false;
                stage2_diff_address = true;
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
        stage2_rt_rw_2.setClickable(false);

        stage2_rt_rw_1.setFocusable(false);
        stage2_kode_pos_1.setFocusable(false);
        stage2_rt_rw_2.setFocusable(false);


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

        stage2_provinsi_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areasString = loadJSONFromAsset();
                JSONObject obj = null;

                try {
                    obj = new JSONObject(areasString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (obj == null) {

                } else {
                    JSONObject jsonObject = null;
                    JSONArray Province_jsonArray = null;
                    JSONArray Regions_jsonArray = null;
                    JSONArray District_jsonArray = null;
                    JSONArray Village_jsonArray = null;
                    try {
                        jsonObject = obj.getJSONObject("Indonesia");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonObject != null) {
                        try {
                            Province_jsonArray = jsonObject.getJSONArray("Province");
                            for (int i = 0; i < Province_jsonArray.length(); i++) {
                                ArealData arealData = new ArealData();
                                arealData.setId_province(Province_jsonArray.getJSONObject(i).getString("id_province"));
                                arealData.setProvince_name(Province_jsonArray.getJSONObject(i).getString("province_name"));
                                provinceList.add(arealData);

                                try {
                                    Regions_jsonArray = Province_jsonArray.getJSONObject(i).getJSONArray("Regions");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (Regions_jsonArray.length() > 0) {
                                    for (int j = 0; j < Regions_jsonArray.length(); j++) {
                                        ArealData arealData1 = new ArealData();
                                        try {
                                            arealData1.setId_province(Regions_jsonArray.getJSONObject(j).getString("id_province"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            arealData1.setId_region(Regions_jsonArray.getJSONObject(j).getString("id_region"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            arealData1.setRegion_name(Regions_jsonArray.getJSONObject(j).getString("region_name"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        regionList.add(arealData1);

                                        try {
                                            District_jsonArray = Regions_jsonArray.getJSONObject(i).getJSONArray("Districts");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if (District_jsonArray.length() > 0) {
                                            for (int k = 0; k < District_jsonArray.length(); k++) {
                                                ArealData arealData2 = new ArealData();
                                                try {
                                                    arealData2.setId_region(District_jsonArray.getJSONObject(j).getString("id_region"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    arealData2.setId_district(District_jsonArray.getJSONObject(j).getString("id_district"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    arealData2.setDistrict_name(District_jsonArray.getJSONObject(j).getString("district_name"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                districtList.add(arealData2);
                                                try {
                                                    Village_jsonArray = District_jsonArray.getJSONObject(i).getJSONArray("Villages");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                if (Village_jsonArray.length() > 0) {
                                                    for (int l = 0; l < Village_jsonArray.length(); l++) {
                                                        ArealData arealData3 = new ArealData();
                                                        try {
                                                            arealData3.setId_district(Village_jsonArray.getJSONObject(j).getString("id_district"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            arealData3.setId_village(Village_jsonArray.getJSONObject(j).getString("id_village"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            arealData3.setVillage_name(Village_jsonArray.getJSONObject(j).getString("village_name"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                        villagesList.add(arealData3);

                                                    }
                                                }

                                            }
                                        }

                                    }
                                }

                                Log.e("---province-------", "======" + provinceList.size());
                                Log.e("----region------", "======" + regionList.size());
                                Log.e("---district-------", "======" + districtList.size());
                                Log.e("----village------", "======" + villagesList.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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

        stage_1.setVisibility(View.VISIBLE);
        stage_2.setVisibility(View.GONE);
        stage_3.setVisibility(View.GONE);

        stage3_editText2.setText("Rp ");
        Selection.setSelection(stage3_editText2.getText(), stage3_editText2.getText().length());


        stage3_editText2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("Rp ")) {
                    stage3_editText2.setText("Rp ");
                    Selection.setSelection(stage3_editText2.getText(), stage3_editText2.getText().length());

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
                List<String> list4 = new ArrayList<>(4);
                list4.add("Add apps & widgets");
                list4.add("Home screen settings");
                list4.add("Lock screen settings");
                list4.add("System settings");
                DialogGroup group4_1 = new DialogGroup(list4);

                DialogObject title = new DialogObject("Settings");
                title.setBgId(R.drawable.title_bg);
                title.setTextColor(getResources().getColor(android.R.color.white));
                //add to first as title
                group4_1.add(0, title);

                DialogGroup group4_2 = new DialogGroup("Cancel");

                pubDialogFragment = PubDialogFragment.newInstance(group4_1, group4_2);
/*if (pubDialogFragment != null && !pubDialogFragment.isAdded()) {
                    pubDialogFragment.setItemClickListener(new PubDialogFragment.ItemClickListener() {
                        @Override
                        public void onItemClick(View clickedView, DialogObject dialogObject, int groupIndex, int itemIndex) {

                        }
                    });
                    pubDialogFragment.show(getFragmentManager(), "setting");
                }*/

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

                Log.e("-------i Value", "-----" + i);

                if (i == 0) {

//                    if (stage1_Name.getText().toString().replace(" ", "").length() == 0) {
//
//                        stage1_textView7.setVisibility(View.VISIBLE);
//                        stage1_Name.setBackgroundResource(R.drawable.edit_text_alert_background);
//
//                    } else if (stage1_number.getText().toString().replace(" ", "").length() < 6) {
//                        stage1_textView7.setVisibility(View.GONE);
//                        stage1_Name.setBackgroundResource(R.drawable.edittext_background);
//                        stage1_textView9.setVisibility(View.VISIBLE);
//                        stage1_number.setBackgroundResource(R.drawable.edit_text_alert_background);
//
//                    } else if (stage1_number.getText().toString().replace(" ", "").length() > 14) {
//                        stage1_textView7.setVisibility(View.GONE);
//                        stage1_Name.setBackgroundResource(R.drawable.edittext_background);
//                        stage1_textView9.setVisibility(View.VISIBLE);
//                        stage1_number.setBackgroundResource(R.drawable.edit_text_alert_background);
//                    } else if (!(limited_period || lifelong_period)) {
//                        stage1_textView12.setVisibility(View.VISIBLE);
//                    } else {
                    stage1_textView7.setVisibility(View.GONE);
                    stage1_Name.setBackgroundResource(R.drawable.edittext_background);
                    stage1_textView9.setVisibility(View.GONE);
                    stage1_number.setBackgroundResource(R.drawable.edittext_background);
                    stage1_textView12.setVisibility(View.GONE);
                    i++;

                    tab_2_text.setTextColor(getResources().getColor(R.color.reg));
                    tab_3_text.setTextColor(getResources().getColor(R.color.reg_nonfocus));
                    stage_1.setVisibility(View.GONE);
                    stage_2.setVisibility(View.VISIBLE);
                    stage_3.setVisibility(View.GONE);
                    reg_2.setEnabled(true);
                    reg_3.setEnabled(false);
//                    }
                } else if (i == 1) {
                    tab_2_text.setTextColor(getResources().getColor(R.color.reg));
                    tab_3_text.setTextColor(getResources().getColor(R.color.reg));
                    stage_1.setVisibility(View.GONE);
                    stage_2.setVisibility(View.GONE);
                    stage_3.setVisibility(View.VISIBLE);
                    reg_2.setEnabled(true);
                    reg_3.setEnabled(true);
                    i++;
                } else if (i == 2) {

                    Intent intent = new Intent(AgentRegistrationActivity.this, Register_To_SimaspayUserConfirmationActivity.class);
                    startActivityForResult(intent, 10);
                }


            }
        });
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
//        Toast.makeText(AgentRegistrationActivity.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
//        Toast.makeText(AgentRegistrationActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }

    public String loadJSONFromAsset() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
                    "category.geographical.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String myjsonstring = sb.toString();
        return myjsonstring;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
