package kc.ac.kpu.foruser;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    FirebaseDatabase database;
    DatabaseReference reference;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        database=  FirebaseDatabase.getInstance(); // Firebase database 연동
        reference =database.getReference();// DB 테이블 연결




        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // String menuname = dataSnapshot.child("usermenu").getValue(String.class);
                String  ordercheck= dataSnapshot.child("ordercheck").getValue(String.class);
                if(ordercheck.equals("준비 완료")){         //관리자앱에서 준비완료 버튼 클릭 시
                    createNotification(context); //노티피게이션 생성
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home:
                        setFrag1(0);

                        break;
                    case R.id.action_account:
                        setFrag1(1);
                        break;
                }
                return true;
            }
        });




        setFrag1(0);         //첫 화면

    }
    public  void setFrag1(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction(); //프레그멘트 교체가 일어날때
        switch (n) {
            case 0:
                ft.replace(R.id.frame, MenuActivity.newInstance());// MenuAcivity로 교체

                ft.commit();
                break;
            case 1:
                ft.replace(R.id.frame, MyFragment.newInstance());    //frag2 레이아웃으로 교체
                ft.commit();
                break;
        }
    }
    private void createNotification(Context context){ //Notification 생성


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");


        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //버전에 맞혀서

            int importance = NotificationManager.IMPORTANCE_HIGH; //헤드업 알림(우선순위 높임)
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널",importance)); //채널 생성
        }

        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(" 준비됐습니다.");
        builder.setContentText("가져가주세요~~^^");
        builder.setContentInfo("INFO");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        builder.setDefaults(Notification.DEFAULT_VIBRATE);//진동으로 알림

        //화면이 꺼져있는 상태에서도 Notification
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK  |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "My:Tag");
        wakeLock.acquire(5000);



        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build()); //등록

        reference.child("ordercheck").setValue("준비중"); //값 초기화

    }


}
