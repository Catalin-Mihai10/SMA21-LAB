package com.upt.cti.smartwallet;

import static java.lang.Float.parseFloat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import model.MonthlyExpenses;

public class MainActivity extends AppCompatActivity implements View.OnCreateContextMenuListener, AdapterView.OnItemSelectedListener{

    private TextView tStatus;
    private EditText eIncome, eExpenses;
    private Spinner mSpinner;
    private DatabaseReference databaseReference;
    private String currentMonth;
    private ValueEventListener databaseListener;
    private List<String> monthsArray;
    private final static String PREFERENCES_SETTINGS = "prefs_settings";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tStatus = (TextView) findViewById(R.id.textView);
        eIncome = (EditText) findViewById(R.id.eIncome);
        eExpenses = (EditText) findViewById(R.id.eExpenses);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-27310-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference();

        monthsArray = new ArrayList<String>();
        mSpinner = (Spinner) findViewById(R.id.Mspinner);
        sharedPreferences =  getSharedPreferences(PREFERENCES_SETTINGS, Context.MODE_PRIVATE);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        databaseReference.child("calendar").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("adauga");
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {

                            MonthlyExpenses monthlyExpenses = ds.getValue(MonthlyExpenses.class);
                            monthlyExpenses.month = ds.getKey();
                            System.out.println("Value" + monthlyExpenses.getMonth());
                            if(monthlyExpenses.getMonth() != null) monthsArray.add(ds.getKey());
                    }
                    if(currentMonth != null) mSpinner.setSelection(monthsArray.indexOf(currentMonth));

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });

            currentMonth=sharedPreferences.getString("CurrentMonth", null);
    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bUpdate:
                if(!eIncome.getText().toString().isEmpty() && !eExpenses.getText().toString().isEmpty()){
                    tStatus.setText("Searching ...");
                    createnewUpdateDbListener();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object selectedMonth = parent.getItemAtPosition(position);
        currentMonth=selectedMonth.toString();
        sharedPreferences.edit().putString("CurrentMonth", currentMonth).apply();
        createNewDBListener();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        createNewDBListener();
    }

    private void createNewDBListener() {
        // remove previous databaseListener
        if (databaseReference != null && currentMonth != null && databaseListener != null)
            databaseReference.child("calendar").child(currentMonth).removeEventListener(databaseListener);

        databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                MonthlyExpenses monthlyExpense = dataSnapshot.getValue(MonthlyExpenses.class);
                // explicit mapping of month name from entry key
                monthlyExpense.month = dataSnapshot.getKey();

                eIncome.setText(String.valueOf(monthlyExpense.getIncome()));
                eExpenses.setText(String.valueOf(monthlyExpense.getExpenses()));
                tStatus.setText("Found entry for " + currentMonth);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };
        databaseReference.child("calendar").child(currentMonth).addValueEventListener(databaseListener);
    }

    private void createnewUpdateDbListener() {
        // remove previous databaseListener
        if (databaseReference != null && currentMonth != null && databaseListener != null)
            databaseReference.child("calendar").child(currentMonth).removeEventListener(databaseListener);

        databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                MonthlyExpenses monthlyExpense = new MonthlyExpenses(currentMonth, parseFloat(eIncome.getText().toString()),  parseFloat(eExpenses.getText().toString()));
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (currentMonth == dataSnapshot.getKey()) {
                        dataSnapshot.child("income").getRef().setValue(monthlyExpense.getIncome());
                        dataSnapshot.child("expenses").getRef().setValue(monthlyExpense.getExpenses());
                        // explicit mapping of month name from entry key
                    }
                }
                tStatus.setText("Found entry for " + currentMonth);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };

        // set new databaseListener
        databaseReference.child("calendar").child(currentMonth).addValueEventListener(databaseListener);
    }

}