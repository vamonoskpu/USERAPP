package kc.ac.kpu.foruser


import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kc.ac.kpu.wear.R


class MainActivity : WearableActivity() {
    var result: TextView? = null
    val database: FirebaseDatabase? = null
    val databaseReference: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on

        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("message")
        var tv = findViewById(R.id.examtext) as TextView

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val option = dataSnapshot.exists()
                if(option==true) {
                    val value = dataSnapshot.children.elementAt(0).value

                    tv.text = "$value"
                }
                else {
                    tv.text = "메세지 전송 중입니다."
                }

            }
        })
    }


}
