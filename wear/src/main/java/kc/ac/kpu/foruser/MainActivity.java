package kc.ac.kpu.foruser;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;



public class MainActivity extends Activity {


    // Intent intent;
    TextView result;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    DatabaseReference reference;

    MediaRecorder recorder;
    String fileName;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();










    //여기넣으심됩니당
    TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //코드도 여기 넣으심 됩니당



        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Usermenu");
        textView = findViewById(R.id.textview);

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

        //여기까지가 파이어베이스에서 값 가져오는 코드입니다.


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        } //권한 허용



        // SD 카드 폴더 지정
        File file = new File("gs://foruser-86c2a.appspot.com/", "recorded.wav");
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


    /**
     * @param filePath 파일 경로 매개변수
     * firebase에 파일을 업로드 하는 함수
     */
    void uploadFirebaseStroage(String filePath) {

        Uri file = Uri.fromFile(new File(filePath)); //업로드할 파일 정보(?)
        UploadTask uploadTask; //업로드를 수행할 객체
        StorageReference soundRef; //파일을 업로드할 firebase 스토리지의 경로를 참조

        soundRef = storageRef.child("/learning/" + file.getLastPathSegment()); //스토리지 경로 지정
        uploadTask = soundRef.putFile(file); //upload

        uploadTask.addOnFailureListener(new OnFailureListener() { //upload 수행의 결과를 확인하는 리스너
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage()+ "실패", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "FileUpload Success", Toast.LENGTH_SHORT).show();
            }
        });




    }


}