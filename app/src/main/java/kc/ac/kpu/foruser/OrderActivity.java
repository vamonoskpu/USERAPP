package kc.ac.kpu.foruser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {


    // Intent intent;
    TextView result;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button completebtn;

    ImageView money;
    ImageView card;
    ImageView clickmoney;
    ImageView clickcard;


    TextView textView;




    //음성입력부분

    private Button mrecordBtn;
    private TextView mRecordLabel;

    private MediaRecorder recorder;
    private String fileName = null;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String LOG_TAG = "Record_log";
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private int recordNumber = 0;
    private int labelNumber;
    private DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //코드도 여기 넣으심 됩니당
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        } //권한 허용


        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Usermenu");


        textView = findViewById(R.id.textview);
        card = findViewById(R.id.card);
        money = findViewById(R.id.money);
        clickcard = findViewById(R.id.clickcard);
        clickmoney = findViewById(R.id.clickmoney);

        completebtn = findViewById(R.id.completebtn);

        mStorage  = FirebaseStorage.getInstance().getReference();
        mRecordLabel = findViewById(R.id.recordlabel);
        mrecordBtn = findViewById(R.id.recordbtn);
        mProgress = new ProgressDialog(this);

        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();

        fileName += "/recorded_audio.wav";


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MenuData menuData = dataSnapshot.getValue(MenuData.class);
                textView.setText(menuData.getUsermenu());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        completebtn.setOnClickListener(new View.OnClickListener() {  //주문완료 버튼 터치 시
            @Override
            public void onClick(View v) {
                if (clickmoney.getVisibility() == View.VISIBLE) {  //money를 선택했다면
                    Map<String, Object> update = new HashMap<>();
                    update.put("payment", R.drawable.money);
                    mdatabase.updateChildren(update); //Firebase에 돈 이미지 전송
                    Toast.makeText(getApplicationContext(), "주문완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();

                } else if (clickcard.getVisibility() == View.VISIBLE) {  //card를 선택했다면
                    Map<String, Object> update = new HashMap<>();
                    update.put("payment", R.drawable.card);
                    mdatabase.updateChildren(update);      //Firebase에 카드 이미지 전송
                    Toast.makeText(getApplicationContext(), "주문완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "제대로 선택해주세요..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        card.setOnClickListener(new View.OnClickListener() { //카드 이미지 터치 시
            @Override
            public void onClick(View v) {
                clickmoney.setVisibility(View.INVISIBLE);
                if (clickcard.getVisibility() == View.VISIBLE) {
                    clickcard.setVisibility(View.INVISIBLE);

                } else {
                    clickcard.setVisibility(View.VISIBLE);
                }

            }
        });
        money.setOnClickListener(new View.OnClickListener() {  //돈 이미지 터치 시
            @Override
            public void onClick(View v) {
                clickcard.setVisibility(View.INVISIBLE);
                if (clickmoney.getVisibility() == View.VISIBLE) {
                    clickmoney.setVisibility(View.INVISIBLE);

                } else {

                    clickmoney.setVisibility(View.VISIBLE);

                }

            }
        });




        //여기까지가 파이어베이스에서 값 가져오는 코드입니다.


        mrecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                    mRecordLabel.setText("메뉴이름을 말씀해주세요.");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                    mRecordLabel.setText("주문 완료");
                }
                return false;
            }
        });


    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 오디오 입력 지정(마이크)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 출력 형식 지정
        //마이크로 들어오는 음성데이터는 용량이 크기 때문에 압축이 필요
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); // 인코딩
        recorder.setOutputFile(fileName); // 음성 데이터를 저장할 파일 지정

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();


        recorder.release();
        recorder = null;

        uploadAudio();
    }


    private void uploadAudio() {
        mProgress.setMessage("주문정보를 전송합니다.");
        mProgress.show();

        FirebaseDatabase.getInstance()
                .getReference().child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseDatabase.getInstance().getReference().child(uid).child("learning").setValue("false");
                FirebaseDatabase.getInstance().getReference().child(uid).child("using").setValue("true");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child(uid).child("recordNumber").setValue(recordNumber);
        recordNumber += 1;
        labelNumber = recordNumber / 10;
        if (recordNumber % 10 == 7) {
            labelNumber += 1;
        }


        FirebaseDatabase.getInstance().getReference().child(uid).child("learning").setValue("false");
        FirebaseDatabase.getInstance().getReference().child(uid).child("using").setValue("true");
        StorageReference filepath = mStorage.child("/learning/").child(labelNumber + "_" + recordNumber + ".wav");



        Uri uri = Uri.fromFile(new File(fileName));

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();
                mRecordLabel.setText("주문접수완료! 결제수단을 선택해주세요:)");
            }
        });
    }
}


