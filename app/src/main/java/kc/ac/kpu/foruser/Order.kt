package kc.ac.kpu.foruser

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_order.*

class Order: AppCompatActivity() {

    var result: TextView? = null
    val database: FirebaseDatabase? = null
    val databaseReference: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        complete.setOnClickListener{
            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)
        }

        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
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