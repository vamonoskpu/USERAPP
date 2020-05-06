package kc.ac.kpu.foruser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {


    // Intent intent;
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
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String LOG_TAG = "Record_log";
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private int recordNumber = 0;
    private int labelNumber;
    private DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    //음성새로수정
    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FORDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;


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

        mStorage = FirebaseStorage.getInstance().getReference();
        mRecordLabel = findViewById(R.id.recordlabel);
        mrecordBtn = findViewById(R.id.recordbtn);
        mProgress = new ProgressDialog(this);


        bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);


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
                    isRecording = true;
                    startRecording();
                    mRecordLabel.setText("메뉴이름을 말씀해주세요.");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isRecording = false;
                    stopRecording();
                    mRecordLabel.setText("주문 완료");
                }
                return false;
            }
        });


    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FORDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/recored.wav");

    }


    private String getTempFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FORDER);
        if (!file.exists()) {
            file.mkdirs();
        }
        File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE);
        if (tempFile.exists())
            tempFile.delete();
        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private void startRecording() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);

        int i = recorder.getState();
        if (i == 1)
            recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    private void writeAudioDataToFile() {
        byte[] data = new byte[bufferSize];
        String filename = getTempFilename();

        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int read = 0;
        if (null != os) {
            while (isRecording) {
                read = recorder.read(data, 0, bufferSize);

                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void stopRecording() {
        if (null != recorder) {
            isRecording = false;

            int i = recorder.getState();
            if (i == 1)
                recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
            uploadAudio();

        }

        copyWaveFile(getTempFilename(), getFilename());

        deleteTempFile();

    }


    private void deleteTempFile() {
        File file = new File(getTempFilename());
        file.delete();
    }

    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 2;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8;
        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            AppLog.logString("File size:" + totalDataLen);
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);

            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate)
            throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8);
        header[33] = 0;
        header[34] = RECORDER_BPP;
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);


        out.write(header, 0, 44);


    }


    private void uploadAudio() {
        mProgress.setMessage("주문정보를 전송합니다.");
        mProgress.show();


        FirebaseDatabase.getInstance().getReference().child(uid).child("recordNumber").setValue(recordNumber);
        recordNumber += 1;
        labelNumber = recordNumber / 10;
        if (recordNumber % 10 == 7) {
            labelNumber += 1;
        }


        FirebaseDatabase.getInstance().getReference().child(uid).child("learning").setValue("false");
        FirebaseDatabase.getInstance().getReference().child(uid).child("using").setValue("true");
        StorageReference filepath = mStorage.child("/learning/").child(labelNumber + "_" + recordNumber + ".wav");


        Uri uri = Uri.fromFile(new File(getFilename()));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();
                mRecordLabel.setText("주문접수완료! 결제수단을 선택해주세요:)");
            }
        });
    }
}


