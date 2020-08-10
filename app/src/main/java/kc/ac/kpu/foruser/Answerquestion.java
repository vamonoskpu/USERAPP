package kc.ac.kpu.foruser;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Answerquestion extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    TextView answercontents;
    TextView title;  //제목
    TextView writer;  //작성자
    TextView contents;   //내용
    String existingtitle;    // 기존제목
    String existingwriter;   //기존작성자
    String existingcontents; //기존내용
    String existinganswercontents; //답변내용
    TextView dateTv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerquestion);

        title = (TextView) findViewById(R.id.title1);
        writer = (TextView) findViewById(R.id.writer);
        contents = (TextView) findViewById(R.id.contents);
        answercontents = (TextView) findViewById(R.id.answercontents);
        database=  FirebaseDatabase.getInstance(); // Firebase database 연동
        reference =database.getReference().child("noticeboard");// DB 테이블 연결
        dateTv =(TextView) findViewById(R.id.date);




        Bundle extras = getIntent().getExtras(); //QuestionList에서 받아온 정보를 가져옴
        if(extras != null){
            existingtitle = extras.getString("title");
            existingwriter = extras.getString("writer");
            existingcontents = extras.getString("contents");
            existinganswercontents=extras.getString("answercontents");

            title.setText(existingtitle);
            writer.setText(existingwriter);
            contents.setText(existingcontents);
            answercontents.setText(existinganswercontents);

        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String date = dataSnapshot.child(existingwriter).child("date").getValue(String.class);
                dateTv.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
