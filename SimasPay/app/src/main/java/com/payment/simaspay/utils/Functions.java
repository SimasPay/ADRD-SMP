package com.payment.simaspay.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfino.handset.security.CryptoService;

import java.io.File;
import java.util.ArrayList;

import simaspay.payment.com.simaspay.R;

import static android.content.Context.MODE_PRIVATE;
import static com.payment.simaspay.services.Constants.LOG_TAG;

/**
 * Created by widy on 3/5/17.
 * 05
 */

public class Functions {
    SharedPreferences sharedPreferences;
    Context context;
    String rsaKey;
    private Uri mImageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    public Functions(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_prefvalue), MODE_PRIVATE);
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

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(950, 950, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 650, 650);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(300, 300, 300, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
            Log.d(LOG_TAG, "error: "+e.toString());
        } catch (OutOfMemoryError o) {
            Log.d(LOG_TAG, "error: "+o.toString());
        }
        return result;
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

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
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

            return null;
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


}
