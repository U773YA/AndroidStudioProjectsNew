package com.student.ducs.saveimagetutorial;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);

        // Create an actionbar
        ActionBar actionBar = getActionBar();
        actionBar.show();

        // Locate ImageView in activity_main.xml
        ImageView myimage = (ImageView) findViewById(R.id.image);

        // Attach image into ImageView
        myimage.setImageResource(R.drawable.wallpaper);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Create an actionbar menu
        menu.add("Save Image")
                // Add a new Menu Button
                .setOnMenuItemClickListener(this.SaveImageClickListener)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    // Capture actionbar menu item click
    OnMenuItemClickListener SaveImageClickListener = new OnMenuItemClickListener() {

        public boolean onMenuItemClick(MenuItem item) {

            // TODO Auto-generated method stub

            Bitmap bitmap;
            OutputStream output;

            // Retrieve the image from the res folder
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.wallpaper);

            // Find the SD Card path
            File filepath = Environment.getExternalStorageDirectory();

            // Create a new folder in SD Card
            File dir = new File(filepath.getAbsolutePath()
                    + "/Save Image Tutorial/");
            dir.mkdirs();

            // Create a name for the saved image
            File file = new File(dir, "myimage.png");

            // Show a toast message on successful save
            Toast.makeText(MainActivity.this, "Image Saved to SD Card",
                    Toast.LENGTH_SHORT).show();
            try {

                output = new FileOutputStream(file);

                // Compress into png format image from 0% - 100%
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        }
    };
}