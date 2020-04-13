package kc.ac.kpu.foruser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private Button join;
    private Button login;
    private EditText email_login;
    private EditText pwd_login;
    private CheckBox chk_auto;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        join = findViewById(R.id.join_btn);
        login = findViewById(R.id.login_btn);
        email_login = findViewById(R.id.login_email);
        pwd_login = findViewById(R.id.login_pwd);
        chk_auto = findViewById(R.id.chk_auto);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();


        firebaseAuth = FirebaseAuth.getInstance();

        if (setting.getBoolean("chk_auto", false)) {
            email_login.setText(setting.getString("ID", ""));
            pwd_login.setText(setting.getString("PW", ""));
            chk_auto.setChecked(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chk_auto.isChecked()) {
                    String email = email_login.getText().toString().trim();
                    String pwd = pwd_login.getText().toString().trim();

                    editor.putString("ID", email);
                    editor.putString("PW", pwd);
                    editor.putBoolean("chk_auto", true);
                    editor.commit();

                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    editor.clear();
                    editor.commit();

                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String email = email_login.getText().toString().trim();
                            String pwd = pwd_login.getText().toString().trim();


                            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(LoginActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                    });
                }

                join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}