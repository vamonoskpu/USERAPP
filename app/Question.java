package com.example.addminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

         titleedt = (EditText) findViewById(R.id.title1);
        writeredt = (EditText) findViewById(R.id.writer);
       contentsedt = (EditText) findViewById(R.id.contents);
        Button confirm = (Button) findViewById(R.id.questionconfirmbtn);

        database=  FirebaseDatabase.getInstance(); // Firebase database 연동

        databaseReference = FirebaseDatabase.getInstance().getReference();


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleedt.getText().toString();       //작성한 결과값 가져오기
                writer = writeredt.getText().toString();        //작성한 결과값 가져오기
                contents = contentsedt.getText().toString();     //작성한 결과값 가져오기
                users = new Users(title,writer,"진행중",contents,"미답변"); //user class에 데이터 저장


                databaseReference.child("noticeboard").child(writer).setValue(users);
                //firebase에 user class 올리기
                finish();
            }
        });





    }

}
