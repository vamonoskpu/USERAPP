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

import org.w3c.dom.Text;


public class MenuActivity extends Fragment {

    View view;

    private Button order;
    private Button ordercheck;
    private Button menulistbtn;
    private Button questions;




    public static MenuActivity newInstance(){
        MenuActivity menuActivity = new MenuActivity();
        return menuActivity;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_menu,container,false);


        order = (Button) view.findViewById(R.id.orderbtn);
        ordercheck = (Button)view.findViewById(R.id.ordercheckbtn);
        menulistbtn = (Button)view.findViewById(R.id.menulist);
        questions = (Button) view.findViewById(R.id.questionbtn);





        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),OrderActivity.class);
                startActivity(intent);
            }
        });

        ordercheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),OrdercheckActivity.class);
                startActivity(intent);
            }
        });

        menulistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MenuListActivity.class);
                startActivity(intent);
            }
        });



        questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),QuestionList.class);
                startActivity(intent);
            }
        });









        return view;
    }


}