package kc.ac.kpu.foruser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {


    //여기넣으심됩니당
    TextView textView;
    Button order_btn;
    Button menu_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        order_btn = findViewById(R.id.order_wear);
        menu_btn = findViewById(R.id.menu_wear);


        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), wearOrderActivity.class);
                startActivity(intent);
                finish();

            }
        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),wearMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}