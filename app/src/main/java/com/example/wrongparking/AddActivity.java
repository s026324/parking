package com.example.wrongparking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mindorks.paracamera.Camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import es.dmoral.prefs.Prefs;

public class AddActivity extends AppCompatActivity {
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
    int PLACE_PICKER_REQUEST = 9871;

    boolean isAprasymasSet= false, isValsNrSet= false, isVietasSet= false, isNuotraukaSet = false;

    private StorageReference mStorageRef;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView btnChoose;
    private Button btnUpload;
    private ImageView imageView;
    private EditText mEditTextFileName, btnGetPlace, mValstnum;

    public Date mTime;
    Date mDate = Calendar.getInstance().getTime();
    private boolean mPatvirtintas;
    private boolean mPerziuretas;
    String mAnswer = "";

    String mAddress = "";

    private Uri filePath;

    private final int CAMERA_REQUEST = 1888;

    double mLongitude;
    double mLatitude;

    //Firebase
    // FirebaseStorage storage;
    // StorageReference storageReference;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    private static final String TAG = AddActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    Gson gson;
    public static final String PREFS_KEY_PAZEIDIMAI = "pazeidimai_list";
    ArrayList<Upload> pranesimaiList;
    private String pazeidimaiJson = "";

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    private String mLatitudeLabel;
    private String mLongitudeLabel;

    Geocoder geocoder;
    List<Address> addressesList;


    Camera camera;

    int returnable = 0;
    int clicks = 0;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pazeidimai_nav:

                    Intent i = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(i);

                    return true;
                case R.id.pranesti_nav:


                    return true;
                case R.id.manopranesimai_nav:
                    Intent k = new Intent(AddActivity.this, ManoPranesimaiActivity.class);
                    startActivity(k);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        gson = new Gson();
        pranesimaiList = new ArrayList<>();



        pazeidimaiJson = Prefs.with(this).read(PREFS_KEY_PAZEIDIMAI, "");
        if (pazeidimaiJson.equals("")) {
            // Toast.makeText(this,"Jus neturite pranesimu",Toast.LENGTH_LONG).show();
        } else {

            Type founderListType = new TypeToken<ArrayList<Upload>>() {
            }.getType();

            pranesimaiList = gson.fromJson(pazeidimaiJson, founderListType);
            Log.e("pranesimaiList", "json: ->>>>> " + pazeidimaiJson);
        }

        checkTime();




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        View view = navigation.findViewById(R.id.pranesti_nav);
        view.performClick();
        /*        navigation.setSelectedItemId(R.id.pranesti_nav);*/

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mTime = new Date();


        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);

        //   mEditTextFileName = findViewById(R.id.edit_text_file_name);


        btnChoose = (ImageView) findViewById(R.id.imageView2);
        btnUpload = (Button) findViewById(R.id.upload);



        btnUpload.setActivated(false);

        /*        btnUpload.setVisibility(View.GONE);*/
        btnGetPlace = findViewById(R.id.vietaID);
        imageView = (ImageView) findViewById(R.id.imageView2);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);



        mValstnum = findViewById(R.id.valstnum);


       // mValstnum.addTextChangedListener(bandommm);


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        final SharedPreferences ClickLimit = this.getSharedPreferences("clicks", Context.MODE_PRIVATE);

        clicks = ClickLimit.getInt("score", 0);
        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!btnUpload.isActivated()){
/*                    btnUpload.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));*/
                    Toast.makeText(AddActivity.this, "Užpildykite pilnai formą",Toast.LENGTH_LONG).show();
                    return;
                }else if(clicks >= 10) {
                    Toast.makeText(AddActivity.this, "Į dieną galime pranešti apie dešimt pažeidimus!", Toast.LENGTH_SHORT).show();

                } else {

                    clicks += 1;
                    SharedPreferences ClickLimit = getSharedPreferences("clicks", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = ClickLimit.edit();
                    editor.putInt("score", clicks);
                    editor.putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.HOURS.toMillis(12));
                    editor.apply();

                    upload();
                }
            }

        });


        btnGetPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getPlaceAndSetPlaceText(mLatitude, mLongitude);

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AddActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        setOnTextChangeListeners();

    }

    private void checkTime() {

        SharedPreferences ClickLimit = getSharedPreferences("clicks", Context.MODE_PRIVATE);
        if (ClickLimit.getLong("ExpiredDate", -1) < System.currentTimeMillis()) {
            SharedPreferences.Editor editor = ClickLimit.edit();
            editor.clear();
            editor.apply();
        }

    }

/*    TextInputLayout til = (TextInputLayout) findViewById(R.id.text_input_layout);

        if(til == null){
        til.setError("You need to enter a name");
    }else {
        til.setError(null);
    }*/

    private void setOnTextChangeListeners(){

        mEditTextFileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                TextInputLayout tilAprasymas = (TextInputLayout) findViewById(R.id.til_aprasymas);

                if(s.length() != 0){
                    tilAprasymas.setError(null);
                    isAprasymasSet = true;
                } else {
                    isAprasymasSet = false;
                    tilAprasymas.setError(getString(R.string.til_empty_text_edit));
                }
                shouldEnableUpload();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mValstnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                TextInputLayout tilValstnum = (TextInputLayout) findViewById(R.id.til_valstnum);

                if(s.length() != 0){
                    tilValstnum.setError(null);
                    isValsNrSet = true;
                } else {
                    tilValstnum.setError(getString(R.string.til_empty_text_edit));
                    isValsNrSet = false;
                }
                shouldEnableUpload();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void shouldEnableUpload(){
        if(isAprasymasSet && isValsNrSet && isVietasSet && isNuotraukaSet){
            btnUpload.setActivated(true);
        } else {
           btnUpload.setActivated(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }


    private void getPlaceAndSetPlaceText(LatLng latLng) {


        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressesList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

            if (addressesList.size() == 0) {
                Toast.makeText(AddActivity.this, "Prašome patikslinti pasirinkta adresą", Toast.LENGTH_LONG).show();
                isVietasSet = false;
                return;
            }
            isVietasSet = true;
/*
            mAddress = addressesList.get(0).getSubAdminArea();
*/


            mAddress = addressesList.get(0).getAddressLine(0);

            btnGetPlace.setText(mAddress);
            shouldEnableUpload();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private int getLastLocation() {
        returnable = 0;
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();


                            mLatitude = mLastLocation.getLatitude();
                            mLongitude = mLastLocation.getLongitude();
                            mLatitudeLabel = (String.format(Locale.ENGLISH, "%s: %f",
                                    mLatitudeLabel,
                                    mLastLocation.getLatitude()));
                            mLongitudeLabel = (String.format(Locale.ENGLISH, "%s: %f",
                                    mLongitudeLabel,
                                    mLastLocation.getLongitude()));
                            returnable =  1;
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar("Jūsų vietovė nenustatyta");
                            returnable = 0;
                        }
                    }
                });
        return returnable;
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(AddActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                isStoragePermissionGranted();
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private void upload() {


        if (filePath != null) {


            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Keliama.");
            progressDialog.show();

            String randomPath = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/" + randomPath);
            final long timeStamp = mTime.getTime();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(AddActivity.this, "Jūsų pranešimas bus rodomas, kaip tik redaktorius jį patvirtins.", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(AddActivity.this, MainActivity.class);
                            startActivity(i);

                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    SharedPreferences sharedPref;
                                    SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);


                                    Upload upload = new Upload(mEditTextFileName.getText().toString().trim(), mValstnum.getText().toString().trim(), uri.toString(),
                                            timeStamp, mPatvirtintas, mPerziuretas, mAddress, mAnswer);
                                    String uploadId = mDatabase.push().getKey();
                                    mDatabase.child(uploadId).setValue(upload);

                                    pranesimaiList.add(upload);

                                    pazeidimaiJson = gson.toJson(pranesimaiList);
                                    Log.e("naujasJson", pazeidimaiJson);
                                    Prefs.with(AddActivity.this).write(PREFS_KEY_PAZEIDIMAI,pazeidimaiJson);

/*                                    ////ISSAUGOME KEY I SHAREDPREFERENCUS
                                    sharedPref = getPreferences(MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("firebasekey", String.valueOf(timeStamp));
                                    editor.apply();


                                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(upload);
                                        prefsEditor.putString("manopranesimai", json);
                                        prefsEditor.apply();*/
                                    }
                            });


                         //   Date data = new Date();
                         //   String dateString = new SimpleDateFormat("MM/dd/yyyy").format(timeStamp);

                      //      Toast.makeText(AddActivity.this, dateString, Toast.LENGTH_LONG).show();



                        }
                    })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddActivity.this, "Klaida" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * 0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Keliama:" + (int) progress + "%");
                        }
                    });
        }
        else {
            shouldEnableUpload();
           /* Toast.makeText(this.getApplicationContext(),"Užpildykite visą formą!",Toast.LENGTH_LONG).show();*/
        }

    }

    private void chooseImage() {


        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);

        try {
            camera.takePicture();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
              //  Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                getPlaceAndSetPlaceText(place.getLatLng());
            }
        }

        if(resultCode != RESULT_CANCELED){
            if(requestCode == Camera.REQUEST_TAKE_PHOTO){
                Bitmap bitmap = camera.getCameraBitmap();
                if(bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    filePath = getImageUri(this,bitmap);
                    isNuotraukaSet = true;
                    shouldEnableUpload();
/*                    btnUpload.setVisibility(View.VISIBLE);*/
                }else{
                    Toast.makeText(this.getApplicationContext(),"Klaida!",Toast.LENGTH_LONG).show();
                    isNuotraukaSet = false;
                }
            }
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_pranesti, menu);
        setTitle("Pranešimo forma");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

/*        if (id == R.id.item1) {
            Toast.makeText(this, "bambo", Toast.LENGTH_LONG).show();
        }*/
        if (id == R.id.apie) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_pranesti, null);
            Button ok = (Button) mView.findViewById(R.id.ok);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            /*                Toast.makeText(this, "bambo", Toast.LENGTH_LONG).show();*/

/*            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    //set icon
                    .setIcon(android.R.drawable.ic_menu_info_details)
                    //set title
                    .setTitle("Pranešimo forma")
                    //set message
                    .setMessage("Šiame lange yra matoma pranešimo apie netaisyklingai priparkuota transporto priemonę formą. Jeigu matote parkavimo pažeidimą ir norite pranešti apie tai, turite užpildyti pilmai šia formą ir nufotografuoti pražeidimą taip, kad būtų aiškiai matomas pažeidimas ir transporto priemonės valstybiniai registracijos numeriai.")
                    //set positive button
                    .setPositiveButton("Supratau", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked

                        }
                    })
                    //set negative button
                    .show();*/
        }
        if (id == R.id.item2) {
            Intent i = new Intent(AddActivity.this, RedLogin.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


}
