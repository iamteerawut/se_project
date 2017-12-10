package com.example.pillbox.activity;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillbox.MultiSelectionSpinner;
import com.example.pillbox.R;
import com.example.pillbox.data.MedicationContract;
import com.example.pillbox.data.MedicationDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMedActivity extends AppCompatActivity {
    private String mPrevActivity;
    private Toolbar mToolbar;
    private EditText mMedName;
    private EditText mDosage;
    private EditText mAmount;

    private Spinner mUnitsSpinner;
    private Spinner mBeforeFoodSpinner;
    private MultiSelectionSpinner mFreqSpinner;

    private SQLiteDatabase mMedListDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        // Set up Database
        // Create a UserListDbHelper instance, pass "this" to constructor as context
        MedicationDbHelper medicationDbHelper = new MedicationDbHelper(this);
        // Get a writable database reference using getWritableDatabase and store it in mMedListDb
        mMedListDb = medicationDbHelper.getWritableDatabase();

        // Set the EditText
        mMedName = (EditText) findViewById(R.id.med_name);
        mDosage = (EditText) findViewById(R.id.dosage);
        mAmount = (EditText) findViewById(R.id.amount);

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

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);
        setUpFloatingActionButton();

        mPrevActivity = getIntent().getStringExtra("PREV_ACTIVITY");
        if(mPrevActivity.equals("ScanMedActivity")){
            autoFillInput();
        }

    }

    public void autoFillInput(){
        String medName = getIntent().getStringExtra("FILL_MED_NAME");
        String dosage = getIntent().getStringExtra("FILL_MED_DOSAGE");
        String freq = getIntent().getStringExtra("FILL_MED_FREQ");
        String amount = getIntent().getStringExtra("FILL_MED_AMOUNT");
        String beforeAfterFood = getIntent().getStringExtra("FILL_MED_BEFORE_AFTER_FOOD");
        mMedName.setText(medName);
        mDosage.setText(dosage);
        mAmount.setText(amount);
        autoFillFreqSpinner(freq);
        autoFillBeforeFoodSpinner(beforeAfterFood);


    }

    private void autoFillFreqSpinner(String freq){
        if(freq.equals("THREE")){
            mFreqSpinner.setSelection(new int[]{0,1,2});
        }
    }

    private void autoFillBeforeFoodSpinner(String beforeAfterFood){
        if (beforeAfterFood.equals("BEFORE / AFTER")){
            mBeforeFoodSpinner.setSelection(0);
        } else if (beforeAfterFood.equals("BEFORE")){
            mBeforeFoodSpinner.setSelection(1);
        } else {
            mBeforeFoodSpinner.setSelection(2);
        }
    }

    private void setUpFloatingActionButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.camera_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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

        if(id == android.R.id.home){
            setResult(RESULT_OK);
            this.finish();
            return true;

        } else if (id == R.id.action_menu_done) {
        //noinspection SimplifiableIfStatement
            List<String> freqSpinner = mFreqSpinner.getSelectedStrings();
            int freqMorning = 0, freqAfternoon = 0, freqEvening = 0, freqNight = 0;

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

            String medName = mMedName.getText().toString();
            String dosage = mDosage.getText().toString();
            String beforeFood = mBeforeFoodSpinner.getSelectedItem().toString();
            String amount = mAmount.getText().toString();
            String units = mUnitsSpinner.getSelectedItem().toString();

            if (medName.matches("") || dosage.matches("") || amount.matches(""))   {
                // Create custom toast
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_container));
                layout.setBackgroundColor(getResources().getColor(R.color.danger));
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText("Ensure all fields are filled");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                return false;
            }

            // Add to DB
            addDb(mMedListDb, medName, dosage, freqMorning,freqAfternoon, freqEvening, freqNight, beforeFood, amount, units);
            setResult(RESULT_OK);
            mMedListDb.close();
            // Ends the AddMedActivity
            this.finish();
            getFragmentManager().popBackStack();
            // Create custom toast
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText("New Medication Added");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This method adds data into the medication d
    public static void addDb(SQLiteDatabase db, String medName, String dosage, int freqMorning,
                             int freqAfternoon, int freqEvening, int freqNight, String beforeFood, String amount, String units){
        if(db == null){
            return;
        }
        // Get Start and End Date
        Calendar calendar = Calendar.getInstance();
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startDate = dateFormat.format(calendar.getTime());
        int amt = Integer.parseInt(amount);
        int freq = freqMorning + freqAfternoon + freqEvening + freqNight;
        int dos = Integer.parseInt(dosage);
        int daysAdd = amt / (freq * dos);
        calendar.add(Calendar.DAY_OF_YEAR, daysAdd);
        String endDate = dateFormat.format(calendar.getTime());
        Log.d("START", startDate);
        Log.d("END", endDate);
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(MedicationContract.MedicationEntry.COL_MED_NAME, medName);               // String
        cv.put(MedicationContract.MedicationEntry.COL_DOSAGE, dos);                     // Integer

        cv.put(MedicationContract.MedicationEntry.COL_MORNING, freqMorning);            // Integer (Bool)
        cv.put(MedicationContract.MedicationEntry.COL_AFTERNOON, freqAfternoon);        // Integer (Bool)
        cv.put(MedicationContract.MedicationEntry.COL_EVENING, freqEvening);            // Integer (Bool)
        cv.put(MedicationContract.MedicationEntry.COL_NIGHT, freqNight);                // Integer (Bool)

        cv.put(MedicationContract.MedicationEntry.COL_BEFORE_FOOD, beforeFood);         // String
        cv.put(MedicationContract.MedicationEntry.COL_AMOUNT, amt);                     // Integer
        cv.put(MedicationContract.MedicationEntry.COL_UNITS, units);                    // String
        cv.put(MedicationContract.MedicationEntry.COL_START_DATE, startDate);           // String
        cv.put(MedicationContract.MedicationEntry.COL_END_DATE, endDate);               // String

        list.add(cv);

        //insert Data in one transaction
        try
        {
            db.beginTransaction();
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(MedicationContract.MedicationEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }
    public static void deleteDb(SQLiteDatabase db, String medId){
        try
        {
            db.beginTransaction();
            db.delete(MedicationContract.MedicationEntry.TABLE_NAME, MedicationContract.MedicationEntry._ID + "=" + medId, null);
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }
    }

}
