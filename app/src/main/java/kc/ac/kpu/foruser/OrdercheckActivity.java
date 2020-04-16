package kc.ac.kpu.foruser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrdercheckActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordercheck);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Usermenu");

        final TextView usermenu = findViewById(R.id.ordercheckmenu);
        final TextView usercount = findViewById(R.id.ordercheckcount);
        payment = findViewById(R.id.payment);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String menu = dataSnapshot.child("usermenu").getValue(String.class); //메뉴
                String count = dataSnapshot.child("usercount").getValue(String.class); //수량
                int payment1 = dataSnapshot.child("payment").getValue(int.class);    //결제수단


                usermenu.setText(menu);
                usercount.setText(count);
                payment.setImageResource(payment1);
                payment.setVisibility(View.VISIBLE);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
