package com.upt.cti.smartwallet;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.MonthlyExpenses;
import ui.Payment;
import ui.PaymentAdapter;

public class OtherMainActivity  extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private int currentMonth;
    private List<Payment> payments = new ArrayList<>();
    private TextView tStatus;
    private Button bPrevious;
    private Button bNext;
    private Button bAdd;
    private ListView listPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tStatus = (TextView) findViewById(R.id.tV);
        bPrevious = (Button) findViewById(R.id.bPrevious);
        bNext = (Button) findViewById(R.id.bNext);
        bAdd = (Button) findViewById(R.id.bAdd);
        listPayments = (ListView) findViewById(R.id.iV);
        final PaymentAdapter adapter = new PaymentAdapter(this, R.layout.item_payment, payments);
        listPayments.setAdapter(adapter);

        // setup firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        databaseReference.child("wallet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try{
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        Payment payment = ds.getValue(Payment.class);
                        payments.add(payment);
                    }
                }catch (Exception e){}
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }
}
