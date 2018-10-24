package com.example.wrongparking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class pranesti extends Activity {

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";


    //   private EditText mEditTextFileName;
    private StorageReference mStorageRef;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Button btnChoose, btnUpload;
    private ImageView imageView;
    private EditText mEditTextFileName;
    private EditText mValstnum;
    public Date mTime;
    Date mDate = Calendar.getInstance().getTime();
    private boolean mPatvirtintas;
    private boolean mPerziuretas;
    private TextView vieta;




    private Uri filePath;

    private final int CAMERA_REQUEST = 1888;

    //Firebase
    // FirebaseStorage storage;
    // StorageReference storageReference;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

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
        imageView = (ImageView) findViewById(R.id.imageView2);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mValstnum = findViewById(R.id.valstnum);

        vieta = (TextView) findViewById(R.id.Vieta);
        final int PLACE_PICKER_REQUEST = 1;

        vieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent;
                try {
                    intent = builder.build((Activity) getApplicationContext());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
                catch (GooglePlayServicesNotAvailableException e){
                    e.printStackTrace();
                }
            }
        });


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

    }


    private void uploadImage() {


        if (filePath !=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Keliama.");
            progressDialog.show();



/*            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");
            myRef.setValue(filePath);*/
            String randomPath = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+ randomPath);
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
                                    Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), mValstnum.getText().toString().trim(), uri.toString() ,
                                            timeStamp, mPatvirtintas, mPerziuretas);
                                    String uploadId = mDatabase.push().getKey();
                                    mDatabase.child(uploadId).setValue(upload);

                                }
                            });


                            Date data = new Date();
                            String dateString = new SimpleDateFormat("MM/dd/yyyy").format(timeStamp);

                        Toast.makeText(pranesti.this,dateString,Toast.LENGTH_LONG).show();




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
                            Toast.makeText (pranesti.this, "Klaida"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * 0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress+"%");
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

    @Override
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
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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
