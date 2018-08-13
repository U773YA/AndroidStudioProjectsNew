package com.student.ducs.stego;

import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    Button b1, b7, b8;
    ImageView im;
    private ProgressDialog load;

    private Bitmap bmp;
    private Bitmap operation;

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button);
        b7 = (Button) findViewById(R.id.chooser);
        b8 = (Button) findViewById(R.id.save);
        im = (ImageView) findViewById(R.id.imageView);

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageToExternalStorage(operation);
            }
        });


    }

    public void gray(View view) {
        load = ProgressDialog.show(MainActivity.this, "", "loading");
        load.setCancelable(true);
        operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        double red = 0.2989;
        double green = 0.587;
        double blue = 0.114;

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int x = (int) (red * r + green * g + blue * b);
                operation.setPixel(i, j, Color.argb(Color.alpha(p), x, x, x));
            }
        }
        setPic(operation, im);
    }

    public void bright(View view) {
        operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = 100 + r;
                g = 100 + g;
                b = 100 + b;
                alpha = 100 + alpha;
                operation.setPixel(i, j, Color.argb(alpha, r, g, b));
            }
        }
        im.setImageBitmap(operation);
    }

    public void dark(View view) {
        operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = r - 50;
                g = g - 50;
                b = b - 50;
                alpha = alpha - 50;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
    }

    public void gama(View view) {
        load = ProgressDialog.show(MainActivity.this, "", "loading");
        load.setCancelable(true);
        operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = r;
                g = 0;
                b = 0;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        setPic(operation, im);
    }

    public void green(View view) {
        load = ProgressDialog.show(MainActivity.this, "", "loading");
        load.setCancelable(true);
        operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = 0;
                g = g;
                b = 0;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        setPic(operation, im);
    }

    public void blue(View view) {
        load = ProgressDialog.show(MainActivity.this, "", "loading");
        load.setCancelable(true);
        operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = 0;
                g = 0;
                b = b;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        setPic(operation, im);
    }

    public void openImageChooser(View view) {
        load = ProgressDialog.show(MainActivity.this, "", "loading");
        load.setCancelable(true);
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            bmp = BitmapFactory.decodeFile(picturePath);
            bmp = setPic(bmp, im);

            //im.setImageBitmap(bmp);
            //bmp=((BitmapDrawable)im.getDrawable()).getBitmap();
        }
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public Bitmap setPic(Bitmap bmp, ImageView im) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int bounding = dpToPx(im.getWidth());
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap scaledBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));
        im.setImageDrawable(result);
            /*Image.LayoutParams params = (LinearLayout.LayoutParams) im.getLayoutParams();
            params.width = width;
            params.height = height;
            im.setLayoutParams(params);*/

        Log.i("Test", "done");
        load.cancel();
        return scaledBitmap;
    }

    public final static String APP_PATH_SD_CARD = "Stego";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    public boolean saveImageToExternalStorage(Bitmap image) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;

        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStream fOut = null;
            File file = new File(fullPath, "desiredFilename.png");
            file.createNewFile();
            fOut = new FileOutputStream(file);

// 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            return true;

        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }
}
