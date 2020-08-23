package kc.ac.kpu.foruser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MyFragment extends Fragment {

    View view;

    private Button buttonLogout;
    private TextView textivewDelete;
    private TextView textViewUserEmail;
    private FirebaseAuth firebaseAuth;

    public  static MyFragment newInstance(){
        MyFragment myFragment=new MyFragment();
        return myFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my,container,false);


        buttonLogout = (Button) view.findViewById(R.id.logout_btn);
        textivewDelete = (TextView) view.findViewById(R.id.del_text);
        textViewUserEmail = (TextView) view.findViewById(R.id.textviewUserEmail);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            // finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail.setText("ID: " + user.getEmail());


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                // finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


        textivewDelete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"계정이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                        // finish();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });

            }

        });
        return view;
    }
}
