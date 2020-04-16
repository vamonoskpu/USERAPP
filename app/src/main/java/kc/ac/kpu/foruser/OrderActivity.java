package kc.ac.kpu.foruser;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {


    // Intent intent;
    TextView result;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button completebtn;

    MediaRecorder recorder;
    String fileName;
    ImageView money;
    ImageView card;
    ImageView clickmoney;
    ImageView clickcard;


    TextView textView;

    //음성입력부분
    String[] PERMISSION = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};
    private static final String TAG = "VoiceChangerSample";
    private static final int SAMPLE_RATE = 8000;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;



    private Object recordNumber;
    private int labelNumber;
    private com.google.firebase.auth.FirebaseAuth FirebaseAuth;
    private FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();


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
        clickcard= findViewById(R.id.clickcard);
        clickmoney = findViewById(R.id.clickmoney);

        completebtn = findViewById(R.id.completebtn);


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
                if (clickmoney.getVisibility() == View.VISIBLE){  //money를 선택했다면
                    Map<String,Object> update = new HashMap<>();
                    update.put("payment", R.drawable.money);
                    reference.updateChildren(update); //Firebase에 돈 이미지 전송
                    Toast.makeText(getApplicationContext(),"주문완료 되었습니다.",Toast.LENGTH_SHORT).show();

                }else if(clickcard.getVisibility() == View.VISIBLE){  //card를 선택했다면
                    Map<String,Object> update = new HashMap<>();
                    update.put("payment", R.drawable.card);
                    reference.updateChildren(update);      //Firebase에 카드 이미지 전송
                    Toast.makeText(getApplicationContext(),"주문완료 되었습니다.",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(),"제대로 선택해주세요..",Toast.LENGTH_SHORT).show();
                }
            }
        });

        card.setOnClickListener(new View.OnClickListener() { //카드 이미지 터치 시
            @Override
            public void onClick(View v) {
                if(clickcard.getVisibility() == View.VISIBLE){
                    clickcard.setVisibility(View.INVISIBLE);
                }else {
                    clickcard.setVisibility(View.VISIBLE);

                }


            }
        });
        money.setOnClickListener(new View.OnClickListener() {  //돈 이미지 터치 시
            @Override
            public void onClick(View v) {
                if(clickmoney.getVisibility() == View.VISIBLE){
                    clickmoney.setVisibility(View.INVISIBLE);
                }else{

                    clickmoney.setVisibility(View.VISIBLE);

                }

            }
        });


    //여기까지가 파이어베이스에서 값 가져오는 코드입니다.


//기존 녹음 부분

            // SD 카드 폴더 지정
            File file = new File(Environment.getExternalStorageDirectory(), "recorded.wav");
            fileName = file.getAbsolutePath(); // 파일 위치 가져옴
            Toast.makeText(getApplicationContext(), "주문시 녹음시작버튼, 주문끝날 시 녹음중지 버튼을 눌러주세요.", Toast.LENGTH_LONG).show();


            findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 녹음 시작
                    if (recorder == null) {
                        recorder = new MediaRecorder(); // 미디어리코더 객체 생성
                    }
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 오디오 입력 지정(마이크)
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 출력 형식 지정
                    //마이크로 들어오는 음성데이터는 용량이 크기 때문에 압축이 필요
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); // 인코딩
                    recorder.setOutputFile(fileName); // 음성 데이터를 저장할 파일 지정
                    try {
                        recorder.prepare();
                        recorder.start();
                        Toast.makeText(getApplicationContext(), "녹음시작", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 녹음 중지
                    if (recorder != null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;

                        //firebase에 파일 업로드 메서드 호출
                        uploadFirebaseStroage(fileName);
                    }
                    Toast.makeText(getApplicationContext(), "녹음중지", Toast.LENGTH_SHORT).show();
                }
            });






    }



        void uploadFirebaseStroage(String filePath) {

            Uri file = Uri.fromFile(new File(filePath)); //업로드할 파일 정보(?)
            UploadTask uploadTask; //업로드를 수행할 객체
            StorageReference soundRef; //파일을 업로드할 firebase 스토리지의 경로를 참조

            soundRef = storageRef.child("/learning/" + file.getLastPathSegment()); //스토리지 경로 지정
            uploadTask = soundRef.putFile(file); //upload

            uploadTask.addOnFailureListener(new OnFailureListener() { //upload 수행의 결과를 확인하는 리스너
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(OrderActivity.this, e.getMessage() + "실패", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(OrderActivity.this, "FileUpload Success", Toast.LENGTH_SHORT).show();
                }
            });

        }
}

/*
    private void configureEventListener() {

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    startRecording();

                }

            }
        });

    }

    private void startRecording() {
        Log.i(TAG, "start recording");
        setButtonEnable(true);
        try {
            recordTask = new MicRecordTask(progressBar, displayView, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_ENCODING);
            recordTask.setMax(3 * getDataBytesPerSecond(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_ENCODING));

        } catch (IllegalAccessException ex) {
            Log.w(TAG, "Fail to create MicRecordTask.", ex);
        }
        recordTask.start();
        waitEndTask(recordTask);

    }

    private void waitEndTask(final Thread t) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t.join();
                } catch (InterruptedException e) {

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setButtonEnable(false);
                        stopRecording();
                    }
                });
            }
        }).start();
    }


    private void stopRecording() {
        stopTask(recordTask);
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(user.getUid())
                .child("recordNumber")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        recordNumber = dataSnapshot.getValue();
                        labelNumber = Integer.parseInt(recordNumber.toString()) / 50;

                        if (Integer.parseInt(recordNumber.toString()) % 10 == 9) {
                            countText.setText(String.valueOf(labelNumber + 1));
                        } else
                            countText.setText(String.valueOf(labelNumber));


                        if (Integer.parseInt(recordNumber.toString()) > 498) {
                            mdatabase.child("users").child(user.getUid()).child("learning").setValue("true");
                            mdatabase.child("users").child(user.getUid()).child("NewUser").setValue("no");

                            Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        Toast.makeText(OrderActivity.this, recordNumber.toString(), Toast.LENGTH_SHORT).show();
                        final File file = new File(getSavePath(), String.valueOf(labelNumber) + "_" + recordNumber.toString() + saveSoundFile(file, true));
                        recordNumber = Integer.parseInt(recordNumber.toString()) + 1;
                        mdatabase.child("users").child(user.getUid()).child("recordNumber").setValue(recordNumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "ERROR DataBase");
                    }
                });
        Log.i(TAG, "stop recording");
    }

    private boolean saveSoundFile(File savefile, boolean isWavFile){
        Uri file;
        StorageReference wavRef;
        UploadTask uploadTask;
        byte[] data = displayView.getAllWaveData();
        if(data.length==0){
            Lo
        }
    }
}*/