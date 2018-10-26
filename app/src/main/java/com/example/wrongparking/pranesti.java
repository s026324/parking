package com.example.wrongparking;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import im.delight.android.location.SimpleLocation;

import static android.content.ContentValues.TAG;

public class pranesti extends Activity {
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
    int PLACE_PICKER_REQUEST = 1;


    private StorageReference mStorageRef;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Button btnChoose, btnUpload, btnGetPlace;
    private ImageView imageView;
    private EditText mEditTextFileName;
    private EditText mValstnum;
    public Date mTime;
    Date mDate = Calendar.getInstance().getTime();
    private boolean mPatvirtintas;
    private boolean mPerziuretas;
    private TextView tvPlace;

    String mAddress = "";

    private Uri filePath;

    private final int CAMERA_REQUEST = 1888;

    double longitude;
    double latitude;

    //Firebase
    // FirebaseStorage storage;
    // StorageReference storageReference;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private SimpleLocation location;
    Geocoder geocoder;
    List<Address> addressesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pranesti);
        //  System.out.println(mDate);

        mTime = new Date();
        // mStorageRef = FirebaseStorage.getInstance().getReference();
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);

        //   mEditTextFileName = findViewById(R.id.edit_text_file_name);
        btnChoose = (Button) findViewById(R.id.choose);
        btnUpload = (Button) findViewById(R.id.upload);
        btnGetPlace = findViewById(R.id.btn_get_place);
        tvPlace = findViewById(R.id.tv_place_adress);
        imageView = (ImageView) findViewById(R.id.imageView2);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mValstnum = findViewById(R.id.valstnum);


        boolean mPatvirtintas = false;
        boolean mPerziuretas = false;


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }

        });
        btnGetPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlaceAndSetPlaceText();
            }
        });

    }

    private void getPlaceAndSetPlaceText() {

        // construct a new instance of SimpleLocation
        location = new SimpleLocation(this);

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressesList = geocoder.getFromLocation(latitude,longitude,1);

            mAddress = addressesList.get(0).getAddressLine(0);

            tvPlace.setText(mAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //tvPlace.setText(String.valueOf(latitude + "   " + longitude));


    }


    private void uploadImage() {


        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Keliama.");
            progressDialog.show();



/*            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");
            myRef.setValue(filePath);*/
            String randomPath = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/" + randomPath);
            final long timeStamp = mTime.getTime();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(pranesti.this, "Upload successful", Toast.LENGTH_LONG).show();

                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
//                                   Intent intent = new Intent (pranesti.this, Redaktorius.class);
//                                    startActivity(intent);

                                    //   boolean mPatvirtintas = false;
                                    Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), mValstnum.getText().toString().trim(), uri.toString(),
                                            timeStamp, mPatvirtintas, mPerziuretas, mAddress);
                                    String uploadId = mDatabase.push().getKey();
                                    mDatabase.child(uploadId).setValue(upload);

                                }
                            });


                            Date data = new Date();
                            String dateString = new SimpleDateFormat("MM/dd/yyyy").format(timeStamp);

                            Toast.makeText(pranesti.this, dateString, Toast.LENGTH_LONG).show();




/*                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                    taskSnapshot.getDownloadUrl().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);*/

                        }
                    })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(pranesti.this, "Klaida" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * 0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded" + (int) progress + "%");
                        }
                    });
        }

    }

    private void chooseImage() {
        //    Intent intent = new Intent();
        //  intent.setType("image/*");
        //   intent.setAction(Intent.ACTION_GET_CONTENT);
        //   startActivityForResult(intent.createChooser(intent, "Pasirinkti"),PICK_IMAGE_REQUEST);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //    pranesti callbackManager = null;
        //  callbackManager.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED){
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                filePath =  getImageUri(getApplicationContext(), photo);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                String realPath = getRealPathFromURI(filePath);

                System.out.println(realPath);
                imageView.setImageBitmap(photo);
            }
        }

    }

    // if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
    //        && data != null && data.getData() != null) {
    //  filePath = data.getData();
    //  try {
    //      Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
    //      imageView.setImageBitmap(bitmap);
    //  } catch (IOException e) {
    //      e.printStackTrace();

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

}
