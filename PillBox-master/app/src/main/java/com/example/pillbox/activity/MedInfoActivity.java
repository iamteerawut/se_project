package com.example.pillbox.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillbox.Medication;
import com.example.pillbox.MultiSelectionSpinner;
import com.example.pillbox.R;
import com.example.pillbox.TakeMedication;
import com.example.pillbox.data.MedicationContract;
import com.example.pillbox.data.MedicationDbHelper;
import com.example.pillbox.model.PillBoxFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedInfoActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mMedName;
    private EditText mDosage;
    private EditText mAmount;
    private Spinner mUnitsSpinner;
    private Spinner mBeforeFoodSpinner;
    private MultiSelectionSpinner mFreqSpinner;
    private EditText mStartDate;
    private EditText mEndDate;
    private SQLiteDatabase mMedListDb;
    private CoordinatorLayout coordinatorLayout;
    Calendar dateTime = Calendar.getInstance();
    private Button delete_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_info);

        // Set up Database
        // Create a UserListDbHelper instance, pass "this" to constructor as context
        MedicationDbHelper medicationDbHelper = new MedicationDbHelper(this);
        // Get a writable database reference using getWritableDatabase and store it in mMedListDb
        mMedListDb = medicationDbHelper.getWritableDatabase();

        // Set the EditText
        mMedName = (EditText) findViewById(R.id.med_name);
        mDosage = (EditText) findViewById(R.id.dosage);
        mAmount = (EditText) findViewById(R.id.amount);
        mStartDate = (EditText) findViewById(R.id.start_date);
        mEndDate = (EditText) findViewById(R.id.end_date);
        // Start: Set the Spinners
        String[] array = {"Morning", "Afternoon", "Evening", "Night"};
        mFreqSpinner = (MultiSelectionSpinner) findViewById(R.id.frequency_spinner);
        mFreqSpinner.setItems(array);
        mFreqSpinner.setSelection(new int[]{0});
        mUnitsSpinner = (Spinner) findViewById(R.id.units_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mUnitsSpinner.setAdapter(adapter);

        mBeforeFoodSpinner = (Spinner) findViewById(R.id.before_food_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this, R.array.before_food_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBeforeFoodSpinner.setAdapter(adapter);

        // Set up Toolbar
        // child toolbar defined in the layout file
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);
        //delete data from DB
        delete_btn = (Button) findViewById(R.id.delete_btn);

        // Get the MED_ID of the card view
        String string = getIntent().getStringExtra("MED_ID");
        // Store medication data into Medication object medInfo
        Medication medInfo = getMedInfo(string);
        mMedName.setText(medInfo.getMedName());
        mDosage.setText(String.valueOf(medInfo.getDosage()));
        mAmount.setText(String.valueOf(medInfo.getAmount()));
        // Set up mFreqSpinner
        List<String> freqList = new ArrayList<>();
        if(medInfo.getMorning() == 1){
            freqList.add("Morning");
        }
        if (medInfo.getAfternoon() == 1){
            freqList.add("Afternoon");
        }
        if(medInfo.getEvening() == 1){
            freqList.add("Evening");
        }
        if (medInfo.getNight() == 1){
            freqList.add("Night");
        }

        mFreqSpinner.setSelection(freqList);
        mStartDate.setText(medInfo.getStartDate());
        mEndDate.setText(medInfo.getEndDate());

        // Start: Set the Spinners
        mUnitsSpinner = (Spinner) findViewById(R.id.units_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> unitsAdapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mUnitsSpinner.setAdapter(unitsAdapter);

        mBeforeFoodSpinner = (Spinner) findViewById(R.id.before_food_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> beforeFoodAdapter = ArrayAdapter.createFromResource(this, R.array.before_food_array, android.R.layout.simple_spinner_item);
        beforeFoodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBeforeFoodSpinner.setAdapter(beforeFoodAdapter);

        mMedListDb.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_navigation_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            setResult(RESULT_CANCELED);
            this.finish();
            return true;

        } else if (id == R.id.action_menu_done) {
            setResult(RESULT_OK);
            updateMedInfo();

            this.finish();
            // Create custom toast
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("Medication Updated Successfully");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Postcondition: updates the medication DB based on current input values
    public void updateMedInfo(){
        int freqMorning = 0, freqAfternoon = 0, freqEvening = 0, freqNight = 0;
        List<String> freqSpinner = mFreqSpinner.getSelectedStrings();
        if(freqSpinner.contains("Morning")){
            freqMorning = 1;
        }
        if(freqSpinner.contains("Afternoon")){
            freqAfternoon = 1;
        }
        if(freqSpinner.contains("Evening")){
            freqEvening = 1;
        }
        if(freqSpinner.contains("Night")){
            freqNight = 1;
        }
        MedicationDbHelper medicationDbHelper = new MedicationDbHelper(this);
        mMedListDb = medicationDbHelper.getWritableDatabase();
        String table = MedicationContract.MedicationEntry.TABLE_NAME;
        String id = getIntent().getStringExtra("MED_ID");
        ContentValues cv = new ContentValues();
        cv.put(MedicationContract.MedicationEntry.COL_MED_NAME, mMedName.getText().toString());
        cv.put(MedicationContract.MedicationEntry.COL_DOSAGE, mDosage.getText().toString());
        cv.put(MedicationContract.MedicationEntry.COL_MORNING, freqMorning);
        cv.put(MedicationContract.MedicationEntry.COL_AFTERNOON, freqAfternoon);
        cv.put(MedicationContract.MedicationEntry.COL_EVENING, freqEvening);
        cv.put(MedicationContract.MedicationEntry.COL_NIGHT, freqNight);

        cv.put(MedicationContract.MedicationEntry.COL_BEFORE_FOOD, mBeforeFoodSpinner.getSelectedItem().toString());
        cv.put(MedicationContract.MedicationEntry.COL_AMOUNT, mAmount.getText().toString());
        cv.put(MedicationContract.MedicationEntry.COL_UNITS, mUnitsSpinner.getSelectedItem().toString());

        cv.put(MedicationContract.MedicationEntry.COL_START_DATE, mStartDate.getText().toString());
        cv.put(MedicationContract.MedicationEntry.COL_START_DATE, mEndDate.getText().toString());

        mMedListDb.update(table, cv, "_ID="+id, null);
        mMedListDb.close();
    }


    // Precondition: takes in medId as a String
    // Postcondition: returns a Medication object containing the data with the id of medId
    public Medication getMedInfo(String medId) {
        String table = MedicationContract.MedicationEntry.TABLE_NAME;
        String[] columns = null;
        String selection = "_ID =?";
        String[] selectionArgs = {medId};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = "10";

        Cursor cursor = mMedListDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        Medication medInfo = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String medName = cursor.getString(cursor.getColumnIndex("medName"));
                int dosage = cursor.getInt(cursor.getColumnIndex("dosage"));
                int morning = cursor.getInt(cursor.getColumnIndex("morning"));
                int afternoon = cursor.getInt(cursor.getColumnIndex("afternoon"));
                int evening = cursor.getInt(cursor.getColumnIndex("evening"));
                int night = cursor.getInt(cursor.getColumnIndex("night"));

                String beforeFood = cursor.getString(cursor.getColumnIndex("beforeFood"));
                int amount = cursor.getInt(cursor.getColumnIndex("amount"));
                String units = cursor.getString(cursor.getColumnIndex("units"));
                String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                medInfo = new Medication(medId, medName, dosage, morning,afternoon, evening, night, beforeFood, amount, units, startDate, endDate);
            }
        }

        cursor.close();
        return medInfo;
    }

    public void delete_btn(View view) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(MedInfoActivity.this);
        builder.setMessage("Delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //delete DB
                MedicationDbHelper medicationDbHelper = new MedicationDbHelper(MedInfoActivity.this);
                mMedListDb = medicationDbHelper.getWritableDatabase();
                String table = MedicationContract.MedicationEntry.TABLE_NAME;
                String id_del = getIntent().getStringExtra("MED_ID");
                mMedListDb.delete(table, MedicationContract.MedicationEntry._ID + "="+ id_del, null);
                mMedListDb.close();
                setResult(RESULT_OK);
                finish();
                getFragmentManager().popBackStack();
                Toast.makeText(getApplicationContext(),
                        "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();


    }

}
