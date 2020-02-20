package kc.ac.kpu.foruser;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kc.ac.kpu.wear.R;

public class MenuActivity extends WearableActivity {

    private TextView mTextView;
    Button order = (Button)findViewById(R.id.order_btn); //주문하기버튼
    Button order_check = (Button)findViewById(R.id.ordercheck_btn); //주문확인버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


        order_check.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
            startActivity(intent);
        }



        });








}


}
