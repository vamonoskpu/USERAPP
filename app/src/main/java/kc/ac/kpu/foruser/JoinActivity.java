package kc.ac.kpu.foruser;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class JoinActivity extends AppCompatActivity {


    private EditText email_join;
    private EditText pwd_join;
    private EditText pwd_check;
    private EditText nameText;
    private Button btn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        email_join = (EditText) findViewById(R.id.sign_up_email);
        pwd_join = (EditText) findViewById(R.id.sign_up_pwd);
        pwd_check = (EditText) findViewById(R.id.check_pwd);
        nameText = (EditText) findViewById(R.id.name);

        btn = (Button) findViewById(R.id.sign_up_btn);


        firebaseAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_join.getText().toString().trim();
                String pwd = pwd_join.getText().toString().trim();
                String checkpwd = pwd_check.getText().toString().trim();
                String name = nameText.getText().toString().trim();


                if (email.length() > 0 && pwd.length() > 0 && checkpwd.length() > 0 && name.length() > 0) {
                    if (pwd.equals(checkpwd)) {

                        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                                .addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(JoinActivity.this,"회원가입에 성공하였습니다",Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(JoinActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(JoinActivity.this, "비밀번호를 정확하게 입력해주세요", Toast.LENGTH_SHORT).show();
                    }

                }else
                {
                    Toast.makeText(JoinActivity.this,"이메일 비밀번호 오류",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


