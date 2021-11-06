package com.upt.cti.smartwallet;

import static java.lang.Float.parseFloat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

import model.MonthlyExpenses;

public class MainActivity extends AppCompatActivity {

    private TextView tStatus;
    private EditText eSearch, eIncome, eExpenses;
    private Spinner mSpinner;
    // firebase
    private DatabaseReference databaseReference;
    private String currentMonth;
    private ValueEventListener databaseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tStatus = (TextView) findViewById(R.id.textView);
        eSearch = (EditText) findViewById(R.id.eSearch);
        eIncome = (EditText) findViewById(R.id.eIncome);
        eExpenses = (EditText) findViewById(R.id.eExpenses);
        mSpinner = (Spinner) findViewById(R.id.Mspinner);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-27310-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference();

        System.out.println("creaza spinner");
        createNewSpinnerDbListener();
    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bSearch:
                if (!eSearch.getText().toString().isEmpty()) {
                    // save text to lower case (all our months are stored online in lower case)
                    currentMonth = eSearch.getText().toString().toLowerCase();

                    tStatus.setText("Searching ...");
                    createNewDBListener();
                } else {
                    Toast.makeText(this, "Search field may not be empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bUpdate:
                if(!eIncome.getText().toString().isEmpty() && !eExpenses.getText().toString().isEmpty()){
                    System.out.println("aici");
                    currentMonth = eSearch.getText().toString().toLowerCase();

                    tStatus.setText("Searching ...");
                    createnewUpdateDbListener();
                }
                break;
        }
    }

    private void createNewDBListener() {
        // remove previous databaseListener
        if (databaseReference != null && currentMonth != null && databaseListener != null)
            databaseReference.child("calendar").child(currentMonth).removeEventListener(databaseListener);

        databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
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

        // set new databaseListener
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

    public void createNewSpinnerDbListener(){

        if (databaseReference != null && currentMonth != null && databaseListener != null)
            databaseReference.child("calendar").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
//                MonthlyExpenses monthlyExpense = dataSnapshot.getValue(MonthlyExpenses.class);

                    System.out.println("adauga");
                    ArrayList<String> monthsArray = new ArrayList<String>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        monthsArray.add(ds.child("calendar").getKey());
                    }
                    System.out.println(monthsArray);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, monthsArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner.setAdapter(adapter);
                    // explicit mapping of month name from entry key
//                monthlyExpense.month = dataSnapshot.getKey();

//                eIncome.setText(String.valueOf(monthlyExpense.getIncome()));
//                eExpenses.setText(String.valueOf(monthlyExpense.getExpenses()));
//                tStatus.setText("Found entry for " + currentMonth);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });


        // set new databaseListener
//        databaseReference.child("calendar").child(currentMonth).addValueEventListener(databaseListener);
    }
}