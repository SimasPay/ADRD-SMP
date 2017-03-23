package com.payment.simaspay.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;
import com.payment.simaspay.PaymentPurchaseAccount.PaymentConfirmationActivity;
import com.payment.simaspay.userdetails.SecondLoginActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import simaspay.payment.com.simaspay.R;
import simaspay.payment.com.simaspay.UserHomeActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by widy on 3/5/17.
 * 05
 */

public class Functions {
    private SharedPreferences sharedPreferences;
    private Context context;
    private String rsaKey;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    private String selectedLanguage;

    public Functions(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
        SharedPreferences languageSettings = context.getSharedPreferences("LANGUAGE_PREFERECES", 0);
        selectedLanguage = languageSettings.getString("LANGUAGE", "BAHASA");
    }

    public String generateRSA(String pin){
        String module = sharedPreferences.getString("MODULE", "NONE");
        String exponent = sharedPreferences.getString("EXPONENT", "NONE");

        try {
            rsaKey = CryptoService.encryptWithPublicKey(module, exponent,
                    pin.getBytes());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return rsaKey;
    }

    public void initiatedToolbar(AppCompatActivity myActivityReference){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = myActivityReference.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(myActivityReference.getResources().getColor(R.color.dark_red));
        }
    }

    public static class CropOption {
        public CharSequence title;
        public Drawable icon;
        public Intent appIntent;
    }

    public static class CropOptionAdapter extends ArrayAdapter<CropOption> {
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
            super(context, R.layout.crop_selector, options);

            mOptions 	= options;

            mInflater	= LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup group) {
            if (convertView == null)
                convertView = mInflater.inflate(R.layout.crop_selector, null);

            CropOption item = mOptions.get(position);

            if (item != null) {
                ((ImageView) convertView.findViewById(R.id.iv_icon)).setImageDrawable(item.icon);
                TextView txv = ((TextView) convertView.findViewById(R.id.tv_name));
                txv.setText(item.title);
                txv.setTextColor(convertView.getResources().getColor(R.color.black));

                return convertView;
            }

            return convertView;
        }
    }

    public void imageSelect(){
        final String [] items			= new String [] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter	= new ArrayAdapter<String> (context, android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder		= new AlertDialog.Builder(context);

        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) { //pick from camera
                if (item == 0) {
                    Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        ((Activity) context).startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );

        final AlertDialog dialog = builder.create();
    }

    public void errorEmptyOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        if (selectedLanguage.equalsIgnoreCase("ENG")) {
            builder.setTitle(context.getResources().getString(R.string.eng_otpfailed));
            builder.setMessage(context.getResources().getString(R.string.en_otp_empty_failed)).setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        //((Activity) context).finish();
                    });
        } else {
            builder.setTitle(context.getResources().getString(R.string.bahasa_otpfailed));
            builder.setMessage(context.getResources().getString(R.string.id_otp_empty_failed)).setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        //((Activity) context).finish()
                    });
        }
        AlertDialog alertError = builder.create();
        alertError.show();
    }

    public void errorNullResponseConfirmation(){
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alertbox.setTitle("ErrorMessage");
        alertbox.setMessage(context.getResources().getString(
                R.string.bahasa_serverNotRespond));
        alertbox.setNeutralButton("OK", (arg0, arg1) -> {
            ((Activity) context).finish();
            Intent intent = new Intent(context, UserHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });
        alertbox.show();
    }

    public void errorElseResponseConfirmation(String message){
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alertbox.setTitle("ErrorMessage");
        alertbox.setMessage(message);
        alertbox.setNeutralButton("OK", (arg0, arg1) -> {
            ((Activity) context).finish();
            Intent intent = new Intent(context, UserHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });
        alertbox.show();
    }

    public void errorTimeoutResponseConfirmation(String message){
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        alertbox.setTitle("ErrorMessage");
        alertbox.setMessage(message);
        alertbox.setNeutralButton("OK", (arg0, arg1) -> {
            ((Activity) context).finish();
            Intent intent = new Intent(context, SecondLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });
        alertbox.show();
    }

    public void errorOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setCancelable(false);
        if (selectedLanguage.equalsIgnoreCase("ENG")) {
            builder.setTitle(context.getResources().getString(R.string.eng_otpfailed));
            builder.setMessage(context.getResources().getString(R.string.eng_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        dialog.dismiss();
                    });
        } else {
            builder.setTitle(context.getResources().getString(R.string.bahasa_otpfailed));
            builder.setMessage(context.getResources().getString(R.string.bahasa_desc_otpfailed)).setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        dialog.dismiss();
                    });
        }
        AlertDialog alertError = builder.create();
        if (!((Activity) context).isFinishing()) {
            alertError.show();
        }
    }

    public static String formatRupiah(String harga){
        double hx = Double.parseDouble(harga);

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(hx);
    }

    private void pickImage() {
        final String[] items = new String[] { context.getResources().getString(R.string.id_camera),
                context.getResources().getString(R.string.id_galeri) };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

        builder.setTitle(context.getResources().getString(R.string.id_pilih_foto));
        builder.setAdapter(adapter, (dialog, item) -> {
            if (item == 0) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Uri picUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "tmp_avatar_"
                        + String.valueOf(System.currentTimeMillis())
                        + ".jpg"));

                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        picUri);

                try {
                    intent.putExtra("return-data", true);
                    ((Activity) context).startActivityForResult(intent, PICK_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            } else { // pick from file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ((Activity) context).startActivityForResult(Intent.createChooser(intent,
                        "Complete action using"), PICK_FROM_FILE);
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }



}
