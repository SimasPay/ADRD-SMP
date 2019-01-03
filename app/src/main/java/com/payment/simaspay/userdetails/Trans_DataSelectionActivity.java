package com.payment.simaspay.userdetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.payment.simaspay.R;
import com.payment.simaspay.UangkuTransfer.UangkuTransferDetailsActivity;
import com.payment.simaspay.services.Utility;
import com.payment.simaspay.services.WebServiceHttp;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Nagendra P on 12/30/2015.
 */
public class Trans_DataSelectionActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    LinearLayout back_layout;
    TextView title, heading, firstmonth, secondmonth, last2months, datefrom_text, dateto_text;
    Button datefrom_button, dateto_button, next;
    boolean booleanthisMonth, booleanlastmonth, booleantwomonths, booleanmanualSelection;
    LinearLayout current_month, onemonth, twomonths_layout, manual_selection_layout;
    ImageView currentMonthImage, oneMonthImage, twoMonthsImage, manualSelectionImage;
    String fromDate, toDate;
    Calendar calendar;
    SimpleDateFormat sdf, sdf1;
    public static final String DATEPICKER_TAG = "datepicker";
    DatePickerDialog from_datePickerDialog, to_datePickerDialog;
    int dateSelectionValue = -1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dwnloaddetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_red));
        }
        back_layout = (LinearLayout) findViewById(R.id.back_layout);

        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        calendar = Calendar.getInstance();

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);

        sdf = new SimpleDateFormat("ddMMyyyy");
        sdf1 = new SimpleDateFormat("dd/MM/yyyy");

        from_datePickerDialog = DatePickerDialog.newInstance(Trans_DataSelectionActivity.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        to_datePickerDialog = DatePickerDialog.newInstance(Trans_DataSelectionActivity.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

        title = (TextView) findViewById(R.id.title);
        heading = (TextView) findViewById(R.id.sample);
        firstmonth = (TextView) findViewById(R.id.current_month_textView);
        secondmonth = (TextView) findViewById(R.id.onemonth_textView);
        last2months = (TextView) findViewById(R.id.twomonths_textView);
        datefrom_text = (TextView) findViewById(R.id.manual_enter_from);
        dateto_text = (TextView) findViewById(R.id.manual_enter_to);

        datefrom_button = (Button) findViewById(R.id.fromButton);
        dateto_button = (Button) findViewById(R.id.toButton);
        next = (Button) findViewById(R.id.downLoad_ss);

        title.setTypeface(Utility.Robot_Regular(Trans_DataSelectionActivity.this));
        heading.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        firstmonth.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        secondmonth.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        last2months.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        datefrom_text.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        dateto_text.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        datefrom_button.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        dateto_button.setTypeface(Utility.Robot_Light(Trans_DataSelectionActivity.this));
        next.setTypeface(Utility.Robot_Regular(Trans_DataSelectionActivity.this));

        datefrom_button.setEnabled(false);
        dateto_button.setEnabled(false);
        datefrom_button.clearFocus();
        dateto_button.clearFocus();

        current_month = (LinearLayout) findViewById(R.id.current_month);
        onemonth = (LinearLayout) findViewById(R.id.onemonth);
        twomonths_layout = (LinearLayout) findViewById(R.id.twomonths_layout);
        manual_selection_layout = (LinearLayout) findViewById(R.id.manual_selection_layout);

        currentMonthImage = (ImageView) findViewById(R.id.current_month_checkbox);
        oneMonthImage = (ImageView) findViewById(R.id.onemonth_checkbox);
        twoMonthsImage = (ImageView) findViewById(R.id.twomonths_checkbox);
        manualSelectionImage = (ImageView) findViewById(R.id.manual_selection_checkbox);

        booleanthisMonth = true;
        booleanlastmonth = false;
        booleantwomonths = false;
        booleanmanualSelection = false;

        current_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booleanthisMonth = true;
                booleanlastmonth = false;
                booleantwomonths = false;
                booleanmanualSelection = false;
                currentMonthImage.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                oneMonthImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                twoMonthsImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                manualSelectionImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                datefrom_button.setClickable(false);
                datefrom_button.setEnabled(false);
                dateto_button.setClickable(false);
                dateto_button.setEnabled(false);
                datefrom_button.clearFocus();
                dateto_button.clearFocus();
                datefrom_button.setText("");
                dateto_button.setText("");

                datefrom_button.setHint("DD-MM-YYYY");
                dateto_button.setHint("DD-MM-YYYY");

            }
        });

        onemonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booleanthisMonth = false;
                booleanlastmonth = true;
                booleantwomonths = false;
                booleanmanualSelection = false;
                currentMonthImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                oneMonthImage.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                twoMonthsImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                manualSelectionImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));

                datefrom_button.setClickable(false);
                datefrom_button.setEnabled(false);
                dateto_button.setClickable(false);
                dateto_button.setEnabled(false);
                datefrom_button.clearFocus();
                dateto_button.clearFocus();

                datefrom_button.setText("");
                dateto_button.setText("");

                datefrom_button.setHint("DD-MM-YYYY");
                dateto_button.setHint("DD-MM-YYYY");
            }
        });

        progressDialog = new ProgressDialog(Trans_DataSelectionActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.bahasa_loading));
        progressDialog.setTitle(getResources().getString(R.string.dailog_heading));
        Drawable drawable = new ProgressBar(Trans_DataSelectionActivity.this).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(Trans_DataSelectionActivity.this, R.color.red_sinarmas),
                PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(drawable);

        twomonths_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booleanthisMonth = false;
                booleanlastmonth = false;
                booleantwomonths = true;
                booleanmanualSelection = false;
                currentMonthImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                oneMonthImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                twoMonthsImage.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                manualSelectionImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));

                datefrom_button.setClickable(false);
                datefrom_button.setEnabled(false);
                dateto_button.setClickable(false);
                dateto_button.setEnabled(false);
                datefrom_button.clearFocus();
                dateto_button.clearFocus();

                datefrom_button.setText("");
                dateto_button.setText("");

                datefrom_button.setHint("DD-MM-YYYY");
                dateto_button.setHint("DD-MM-YYYY");
            }
        });

        manual_selection_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booleanthisMonth = false;
                booleanlastmonth = false;
                booleantwomonths = false;
                booleanmanualSelection = true;
                currentMonthImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                oneMonthImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                twoMonthsImage.setImageDrawable(getResources().getDrawable(R.drawable.dwnunselected));
                manualSelectionImage.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                datefrom_button.setTextColor(getResources().getColor(R.color.text_color_black));
                dateto_button.setTextColor(getResources().getColor(R.color.text_color_black));
                datefrom_button.requestFocus();
                datefrom_button.setClickable(true);
                datefrom_button.setEnabled(true);
                dateto_button.requestFocus();
                dateto_button.setClickable(true);
                dateto_button.setEnabled(true);

                fromDate = "";
                toDate = "";
            }
        });

        datefrom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelectionValue = 1;
                if (!from_datePickerDialog.isAdded()) {
                    from_datePickerDialog.setVibrate(false);
                    from_datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
                    from_datePickerDialog.setCloseOnSingleTapDay(true);
                    from_datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                }
            }
        });

        dateto_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelectionValue = 2;
                if (!to_datePickerDialog.isAdded()) {
                    to_datePickerDialog.setVibrate(false);
                    to_datePickerDialog.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
                    to_datePickerDialog.setCloseOnSingleTapDay(true);
                    to_datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar1 = Calendar.getInstance();
                if (booleanthisMonth) {

                    toDate = sdf.format(calendar1.getTime());
                    calendar1.set(Calendar.DAY_OF_MONTH, 1);
                    fromDate = sdf.format(calendar1.getTime());
//                    new DwnLoadAsynTask().execute();
                    Intent intent = new Intent(Trans_DataSelectionActivity.this, TransactionsListActivity.class);
                    intent.putExtra("fromDate", fromDate);
                    intent.putExtra("toDate", toDate);
                    Log.e("=-------", toDate + "========" + fromDate);
                    startActivityForResult(intent, 10);
                } else if (booleanlastmonth) {
                    calendar1.add(Calendar.MONTH, -1);
                    int max = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
                    int min = calendar1.getActualMinimum(Calendar.DAY_OF_MONTH);
                    calendar1.set(Calendar.DAY_OF_MONTH, max);
                    toDate = sdf.format(calendar1.getTime());
                    calendar1.set(Calendar.DAY_OF_MONTH, min);
                    fromDate = sdf.format(calendar1.getTime());
                    Intent intent = new Intent(Trans_DataSelectionActivity.this, TransactionsListActivity.class);
                    intent.putExtra("fromDate", fromDate);
                    intent.putExtra("toDate", toDate);
                    startActivityForResult(intent, 10);
                } else if (booleantwomonths) {
                    calendar1.add(Calendar.MONTH, -1);
                    int max = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calendar1.set(Calendar.DAY_OF_MONTH, max);
                    toDate = sdf.format(calendar1.getTime());
                    calendar1.add(Calendar.MONTH, -1);
                    int min = calendar1.getActualMinimum(Calendar.DAY_OF_MONTH);
                    calendar1.set(Calendar.DAY_OF_MONTH, min);
                    fromDate = sdf.format(calendar1.getTime());
                    Intent intent = new Intent(Trans_DataSelectionActivity.this, TransactionsListActivity.class);
                    intent.putExtra("fromDate", fromDate);
                    intent.putExtra("toDate", toDate);
                    startActivityForResult(intent, 10);
                } else if (booleanmanualSelection) {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    if (fromDate.equalsIgnoreCase("")) {
                        Utility.displayDialog("Harap masukkan periode tanggal awal yang Anda inginkan. Periode yang Anda masukkan maksimal 90 hari dari tanggal hari ini.", Trans_DataSelectionActivity.this);
                    } else if (toDate.equalsIgnoreCase("")) {
                        Utility.displayDialog("Harap masukkan periode tanggal akhir yang Anda inginkan.", Trans_DataSelectionActivity.this);
                    } else {

                        try {
                            date1 = sdf1.parse(datefrom_button.getText().toString().replace("-","/"));
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            date2 = sdf1.parse(dateto_button.getText().toString().replace("-","/"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int days = Integer.parseInt(Utility
                                .getDateDiffString(datefrom_button.getText().toString().replace("-","/")));


                        int days1 = Integer.parseInt(Utility
                                .getDateDiffString(dateto_button.getText().toString().replace("-","/")));

                        if (date1.compareTo(date2) > 0) {
                            datefrom_button.requestFocus();
                            Utility.displayDialog("Periode tanggal yang dipilih tidak valid. Tanggal akhir tidak boleh lebih kecil dari tanggal awal.", Trans_DataSelectionActivity.this);
                        } else if (days - days1 > 90) {
                            datefrom_button.requestFocus();
                            Utility.displayDialog("Periode tanggal yang dipilih tidak valid. Periode yang Anda masukkan maksimal 90 hari dari tanggal hari ini.", Trans_DataSelectionActivity.this);
                        } else {
                            Intent intent = new Intent(Trans_DataSelectionActivity.this, TransactionsListActivity.class);
                            intent.putExtra("fromDate", fromDate);
                            intent.putExtra("toDate", toDate);
                            startActivityForResult(intent, 10);
                        }
                    }
                }
            }
        });


    }

    Date date1;
    Date date2;
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
            showDay = "" + month;
        }
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        if (dateSelectionValue == 1) {
            Log.e("=====" + mYear + "----" + mMonth + "=====" + mDay, "=====" + year + "=====" + month + "-----" + day);
            if ((mYear > year)
                    || (((mMonth + 1) > month) && (mYear == year))
                    || ((mDay >= day) && (mYear == year) && ((mMonth + 1) == month))) {
                datefrom_button.setText(showDay + "-" + showMonth + "-" + year);
                fromDate = showDay + "" + showMonth + "" + year;
            } else {
//                Toast.makeText(Trans_DataSelectionActivity.this, "Tanggal yang Anda pilih tidak boleh melebihi tanggal hari ini.", Toast.LENGTH_LONG).show();
                Utility.displayDialog("Tanggal yang Anda pilih tidak boleh melebihi tanggal hari ini.", Trans_DataSelectionActivity.this);
            }
        } else if (dateSelectionValue == 2) {
            if ((mYear > year)
                    || (((mMonth + 1) > month) && (mYear == year))
                    || ((mDay >= day) && (mYear == year) && ((mMonth + 1) == month))) {
                dateto_button.setText(showDay + "-" + showMonth + "-" + year);
                toDate = showDay + "" + showMonth + "" + year;
            } else {
//                Toast.makeText(Trans_DataSelectionActivity.this, "Tanggal yang Anda pilih tidak boleh melebihi tanggal hari ini.", Toast.LENGTH_LONG).show();
                Utility.displayDialog("Tanggal yang Anda pilih tidak boleh melebihi tanggal hari ini.", Trans_DataSelectionActivity.this);
            }
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

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

    String response;
    WebServiceHttp webServiceHttp;
    ProgressDialog progressDialog;
    int msgCode;


}
