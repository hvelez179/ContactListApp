package com.example.android.contactregapp;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.Uri;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v4.content.FileProvider;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.File;
        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

        import java.util.ArrayList;
        import java.util.List;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;

        import org.w3c.dom.Comment;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "MainActivity_TAG";

    private TextView firstNameTV;
    private EditText firstNameET;
    private TextView lastNameTV;
    private EditText lastNameET;
    private Button openCameraBT;
    private Button saveContactBT;
    private Button viewListBT;
    private ContactsDataSource datasource;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new ContactsDataSource(this);
        datasource.open();

        firstNameET = (EditText) findViewById(R.id.firstName);
        lastNameET = (EditText) findViewById(R.id.lastName);

        openCameraBT = (Button) findViewById(R.id.openCamera_bt);
        openCameraBT.setOnClickListener(this);

        saveContactBT = (Button) findViewById(R.id.saveContact_bt);
        saveContactBT.setOnClickListener(this);

        viewListBT = (Button) findViewById(R.id.viewList_bt);
        viewListBT.setOnClickListener(this);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openCamera_bt:
                dispatchTakePictureIntent();
                break;
            case R.id.saveContact_bt:
                String firstName = firstNameET.getText().toString();
                String lastName = lastNameET.getText().toString();
                Toast.makeText(this, firstName + " " + lastName + " " + mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
                datasource.createContact(firstName, lastName, mCurrentPhotoPath);
                break;
            case R.id.viewList_bt:
                Intent i = new Intent(MainActivity.this, contactListActivity.class);
                startActivity(i);
                break;
            default:
                return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        datasource.close();
    }
}
