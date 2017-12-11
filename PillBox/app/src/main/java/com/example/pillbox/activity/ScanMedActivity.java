package com.example.pillbox.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillbox.R;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanMedActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private static final String TAG = "ScanMedActivity";
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 0;

    private String mPrevActivity;
    private TextView mScanMedInstruct;
    private EditText mScanMedDisplay;
    private Uri fileUri;
    private int currState = 0;
    private CropImageView mCropImageView;
    private CropImageView mImgDisplay;
    private LinearLayout mBottomLayout;

    private ProgressDialog mProgressDialog;

    String mLanguage;
    // Tesseract
    private String datapath = ""; //path to folder containing language data file
    private Bitmap mBitmap;

    private String mMedName;
    private String mDosage;
    private String mFreq;
    private String mAmount;
    private String mBeforeAfterFood;

    @Override
    protected void onResume() {
        super.onResume();
        if (currState == 3)
            currState = 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_med);
        mPrevActivity = getIntent().getStringExtra("PREV_ACTIVITY");
        mCropImageView = (CropImageView)  findViewById(R.id.cropImgPreview);
        mScanMedInstruct = (TextView) findViewById(R.id.scan_med_instruct);
        mImgDisplay = (CropImageView) findViewById(R.id.imgDisplay);
        mScanMedDisplay = (EditText) findViewById(R.id.scan_med_display);
        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        // Set up Toolbar
        // child toolbar defined in the layout file
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);
//        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_launcher);
//        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //initialize Tesseract API
        datapath = getFilesDir()+ "/tesseract/";
        //make sure training data has been copied
        checkFile(new File(datapath + "tessdata/"));
        mLanguage = "eng";

        // Access Tesseract's API with TessBaseAPI objectis
        String lang = "eng";

        // initialize state machine
        scanMedicationStateMachine(currState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.camera_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });

    }

    /*
    * Allow us to copy file to device
    * */
    private void copyFiles() {
        try {
            //location we want the file to be at
            String filepath = datapath + "/tessdata/eng.traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();

            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if file is in expected location
     * If not, call copyFile()
     * */
    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
            return true;

        } else if (id == R.id.action_menu_done) {
            currState ++;
            scanMedicationStateMachine(currState);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCropImageClick() {
        //mCropImageView.setCropRect(new Rect(0, 0, 100, 100));
        mBitmap =  mCropImageView.getCroppedImage();
        mCropImageView.setScaleType(CropImageView.ScaleType.CENTER);

        if (mBitmap != null){
            mImgDisplay.setImageBitmap(mBitmap);
            mCropImageView.setVisibility(View.GONE);
        }
    }

    public Rect getScanRect(){
        return mImgDisplay.getCropRect();
    }

    // State Machine
    // Precondition: This method takes in the next state
    // Postcondition: This method sets the next state of the state machine
    /**
     * State Machine
     * @return the next or previous state
     * */
    public void scanMedicationStateMachine(int state){
        switch (state){

            case 0:
                // Crop Image State
                mBottomLayout.setVisibility(View.GONE);
                startingCameraIntent();
                int mClinicId = getIntent().getIntExtra("CLINIC_ID", -1);
                break;

            case 1:
                mImgDisplay.setVisibility(View.VISIBLE);
                mBottomLayout.setVisibility(View.VISIBLE);
                onCropImageClick();
                mScanMedInstruct.setText("Ensure the bounding rectangle encompasses the the drug details");
                break;

            case 2:
                tessTwoScanText(getScanRect());
                mScanMedInstruct.setText("Ensure Label Recognition is similar to Label Text");
                break;

            case 3:
                Intent addMed = new Intent(ScanMedActivity.this, AddMedActivity.class);
                addMed.putExtra("PREV_ACTIVITY", TAG);
                addMed.putExtra("FILL_MED_NAME", mMedName);
                addMed.putExtra("FILL_MED_DOSAGE", mDosage);
                addMed.putExtra("FILL_MED_FREQ", mFreq);
                addMed.putExtra("FILL_MED_AMOUNT", mAmount);
                addMed.putExtra("FILL_MED_BEFORE_AFTER_FOOD", mBeforeAfterFood);
                this.finish();
                startActivity(addMed);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_navigation_scan, menu);
        return true;
    }

    private void startingCameraIntent() {
        // Assume thisActivity is the current activity
        isStoragePermissionGranted();

        String fileName = "img.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        mCropImageView.setVisibility(View.VISIBLE);
        mImgDisplay.setVisibility(View.GONE);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            ContentResolver cr = getContentResolver();
            try {
                Bitmap bitmap = android.provider.MediaStore.Images.Media
                        .getBitmap(cr, fileUri);
                mCropImageView.setGuidelines(CropImageView.Guidelines.ON);
                mCropImageView.setImageBitmap(bitmap);
                mCropImageView.setAspectRatio(2, 1);

            } catch (Exception e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    private String readBoundRect(Rect rect){

        TessBaseAPI tesseract = new TessBaseAPI();
        tesseract.init(datapath, mLanguage);
        String OCRresult = null;
        tesseract.setImage(mBitmap);
        tesseract.setRectangle(rect);
        OCRresult = tesseract.getUTF8Text();
        tesseract.end();
        return OCRresult;
    }

    private String readText(){
        TessBaseAPI tesseract = new TessBaseAPI();
        tesseract.init(datapath, mLanguage);
        String OCRresult = null;
        tesseract.setImage(mBitmap);
        OCRresult = tesseract.getUTF8Text();
        tesseract.end();
        return OCRresult;

    }

    private void tessTwoScanText(final Rect rect){
        if(mProgressDialog == null){
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "Doing OCR...", true);
        } else {
            mProgressDialog.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result = readBoundRect(rect).toUpperCase();
                ScanMedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String updateResult = result;
                        if (updateResult.contains("\n")){
                            String lines[] = updateResult.split("\\r?\\n");
                            mMedName = lines[1].split(" ", 3)[0];
                            for(String line : lines){
                                String dosage = getMedDosage(line);
                                String freq = getMedFreq(line);
                                String amount = getMedAmount(line);
                                String beforeAfterFood = getBeforeAfterFood(line);
                                if(!dosage.equals("-1")){
                                    mDosage = getNumFromString(dosage);
                                }
                                if(!freq.equals("-1")){
                                    mFreq = freq;
                                }
                                if(!amount.equals("-1")){
                                    mAmount = amount;
                                }
                                if(!beforeAfterFood.equals("-1")){
                                    mBeforeAfterFood = beforeAfterFood;
                                }
                            }
                        }
                        if (mBeforeAfterFood == null){
                            mBeforeAfterFood = "BEFORE / AFTER";
                        }

                        updateResult = mMedName + ", " +mDosage + ", " + mFreq + ", " + mAmount + ", " + mBeforeAfterFood;
                        mScanMedDisplay.setText(updateResult);
                        mProgressDialog.dismiss();

//                        Intent addMed = new Intent(ScanMedActivity.this, AddMedActivity.class);
//                        addMed.putExtra("PREV_ACTIVITY", TAG);
//                        addMed.putExtra("FILL_MED_NAME", mMedName);
//                        addMed.putExtra("FILL_MED_DOSAGE", mDosage);
//                        addMed.putExtra("FILL_MED_FREQ", mFreq);
//                        addMed.putExtra("FILL_MED_AMOUNT", mAmount);
//                        addMed.putExtra("FILL_MED_BEFORE_AFTER_FOOD", mBeforeAfterFood);
//                        startActivity(addMed);


                    }

                });
            }
        }).start();
    }

    private String getNumFromString(String value){
        String numValue = "-1";
        switch (value){
            case "ONE":
                numValue = "1";
                break;
            case "TWO":
                numValue = "2";
                break;
            case "THREE":
                numValue = "3";
                break;
            case "FOUR":
                numValue = "4";
                break;
            case "FIVE":
                numValue = "5";
                break;
            case "SIX":
                numValue = "6";
                break;
            case "SEVEN":
                numValue = "7";
                break;
            case "EIGHT":
                numValue = "8";
                break;
            case "NINE":
                numValue = "9";
                break;
        }
        return numValue;
    }


//    public void getBoundingRect(Bitmap cropBitmap){
//        TessBaseAPI tesseract = new TessBaseAPI();
//        tesseract.init(datapath, mLanguage);
//        tesseract.setImage(cropBitmap);
//        mTessRect = tesseract.getWords().getBoxRects();
//        tesseract.end();
//        //mTess.setRectangle(0,0,370,108);
//    }

    /**
     * @return medication dosage from label
     * */
    public String getMedDosage(String labelInstruction){
        labelInstruction = labelInstruction.toUpperCase();
        String instruction = "-1";
        String regExp = ".*\\bTAKE (ONE|TWO|THREE|FOUR|FIVE|SIX|SEVEN|EIGHT|NINE)\\b.*";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(labelInstruction);
        if (matcher.find()) {
            instruction = labelInstruction.substring(matcher.start(),matcher.end());
            String[] tempInstruct = instruction.split(" ", 3);
            instruction = tempInstruct[1];
        }

        return instruction;
    }

    /**
     * @return medication frequency from label
     * */
    public String getMedFreq(String labelInstruction){
        labelInstruction = labelInstruction.toUpperCase();
        String instruction = "-1";
        String regExp = "(ONE|TWO|THREE|FOUR|FIVE|SIX|SEVEN|EIGHT|NINE) " +
                "TIMES A DAY";

        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(labelInstruction);

        while (matcher.find()) {
            instruction = labelInstruction.substring(matcher.start(),matcher.end());
            String[] tempInstruct = instruction.split(" ", 2);
            instruction = tempInstruct[0];

        }
        return instruction;
    }

    /**
     * @return medication amount from label
     * */
    public String getMedAmount(String labelInstruction){
        labelInstruction = labelInstruction.toUpperCase();
        String instruction = "-1";
        String regExp = "(([0-9_-]){2,5}) " + "TA";

        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(labelInstruction);
        while (matcher.find()) {
            instruction = labelInstruction.substring(matcher.start(),matcher.end());
            String[] tempInstruct = instruction.split(" ", 2);
            instruction = tempInstruct[0];

        }
        return instruction;
    }

    /**
     * @return medication before or after food from label
     * */
    public String getBeforeAfterFood(String labelInstruction){
        labelInstruction = labelInstruction.toUpperCase();
        String instruction = "-1";
        String regExp = "(AFTER|BEFORE) " + "FOOD";

        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(labelInstruction);
        while (matcher.find()) {
            instruction = labelInstruction.substring(matcher.start(),matcher.end());
            String[] tempInstruct = instruction.split(" ");
            instruction = tempInstruct[tempInstruct.length-2];
        }
        return instruction;
    }



}
