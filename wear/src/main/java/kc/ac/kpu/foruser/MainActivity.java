package kc.ac.kpu.foruser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kc.ac.kpu.wear.R;

public class MainActivity extends Activity {



    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference reference1;
    TextView textView;
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Usermenu");
        reference1 = database.getReference("8VZm145EEvgpOKfyjWzghGP4zji2");




        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView2);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MenuData menuData = dataSnapshot.getValue(MenuData.class);
                textView.setText(menuData.getUsermenu());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Result result1 = dataSnapshot.getValue(Result.class);
                textView1.setText(result1.getResult());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}