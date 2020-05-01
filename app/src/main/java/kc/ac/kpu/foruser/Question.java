package kc.ac.kpu.foruser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Question extends AppCompatActivity {

    DatabaseReference databaseReference;

    EditText titleedt;
    EditText writeredt;
    EditText contentsedt;
    String title;     //제목
     String writer;     //작성자
     String contents;    //내용
    Users users;
    FirebaseDatabase database;
    TextView date;
    String time1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

         titleedt = (EditText) findViewById(R.id.title1);
        writeredt = (EditText) findViewById(R.id.writer);
       contentsedt = (EditText) findViewById(R.id.contents);
        Button confirm = (Button) findViewById(R.id.questionconfirmbtn);
        date = (TextView) findViewById(R.id.date);

        database=  FirebaseDatabase.getInstance(); // Firebase database 연동

        databaseReference = FirebaseDatabase.getInstance().getReference();


        date = findViewById(R.id.date);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date(); //DATE 객체선언

       time1 = format1.format(time); //날짜 시간 출력

        date.setText(time1); // 년 날짜 시간 출력


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleedt.getText().toString();       //작성한 결과값 가져오기
                writer = writeredt.getText().toString();        //작성한 결과값 가져오기
                contents = contentsedt.getText().toString();     //작성한 결과값 가져오기
                users = new Users(title,writer,"진행중",contents,"미답변"); //user class에 데이터 저장

                databaseReference.child("noticeboard").child(writer).setValue(users);
                databaseReference.child("noticeboard").child(writer).child("date").setValue(time1);
                //firebase에 user class 올리기
                finish();
            }
        });





    }

}
