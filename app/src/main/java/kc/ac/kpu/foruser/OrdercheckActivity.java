package kc.ac.kpu.foruser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrdercheckActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordercheck);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Usermenu");
        final TextView usermenu = findViewById(R.id.ordercheckmenu);
        final TextView usercount = findViewById(R.id.ordercheckcount);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String menu = dataSnapshot.child("usermenu").getValue(String.class);
                String count = dataSnapshot.child("usercount").getValue(String.class);

                usermenu.setText(menu);
                usercount.setText(count);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
