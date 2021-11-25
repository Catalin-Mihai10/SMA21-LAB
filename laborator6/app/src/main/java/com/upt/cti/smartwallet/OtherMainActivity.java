package com.upt.cti.smartwallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.AppState;
import ui.Payment;
import ui.PaymentAdapter;

public class OtherMainActivity  extends AppCompatActivity {

    private static final String TAG_MONTH = "";

    public enum Month {
        January, February, March, April, May, June, July, August,
        September, October, November, December;

        public static int monthNameToInt(Month month) {
            return month.ordinal();
        }

        public static Month intToMonthName(int index) {
            return Month.values()[index];
        }

        public static int monthFromTimestamp(String timestamp) {
            int month = Integer.parseInt(timestamp.substring(5, 7));
            return month - 1;
        }
    }
    private DatabaseReference databaseReference;
    private List<Payment> payments = new ArrayList<>();
    private TextView tStatus;
    private ListView listPayments;
    private final static String PREFERENCES_SETTINGS = "prefs_settings";
    private SharedPreferences sharedPreferences;
    private Integer currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_main);

        tStatus = (TextView) findViewById(R.id.textView);
        listPayments = (ListView) findViewById(R.id.listView);
        final PaymentAdapter adapter = new PaymentAdapter(this, R.layout.item_payment, payments);
        listPayments.setAdapter(adapter);
        sharedPreferences =  getSharedPreferences(PREFERENCES_SETTINGS, Context.MODE_PRIVATE);

        currentMonth = sharedPreferences.getInt(TAG_MONTH, -1);
        if (currentMonth == -1)
            currentMonth = Month.monthFromTimestamp(AppState.getCurrentTimeDate());

        // setup firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-27310-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference();

        System.out.println("Aici");
        databaseReference.child("wallet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("onChild");
                Payment payment = dataSnapshot.getValue(Payment.class);
                if (payment != null) {
                    if(currentMonth == Month.monthFromTimestamp(dataSnapshot.getKey())) {
                        payment.timestamp = dataSnapshot.getKey();
                        if (!payments.contains(payment)) {
                            payments.add(payment);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
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

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bAdd:
                AppState.get().setDatabaseReference(databaseReference);
                AppState.get().setCurrentPayment(null);
                startActivity(new Intent(this, addPaymentActivity.class));
                break;
            case R.id.bNext:
                ++currentMonth;
                if(currentMonth == 12) currentMonth = 11;
                sharedPreferences.edit().putInt(TAG_MONTH, currentMonth).apply();
                System.out.println(Month.intToMonthName(currentMonth));
                recreate();
                break;
            case R.id.bPrevious:
                --currentMonth;
                if(currentMonth == -1) currentMonth = 0;
                sharedPreferences.edit().putInt(TAG_MONTH, currentMonth).apply();
                System.out.println(Month.intToMonthName(currentMonth));
                recreate();
                break;
        }
    }


}
