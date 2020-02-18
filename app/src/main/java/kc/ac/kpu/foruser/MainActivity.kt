package kc.ac.kpu.foruser

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.FileInputStream
import java.io.FileOutputStream
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    var result: TextView? = null
    val database: FirebaseDatabase? = null
    val databaseReference: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress_bar.visibility = View.VISIBLE

        // Bottom Navigation View
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottom_navigation.selectedItemId = R.id.action_home


        wow.setOnClickListener{
            val nextIntent = Intent(this, ProfileActivity::class.java)
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
        if (option == true) {
            val value = dataSnapshot.children.elementAt(0).value

            tv.text = "$value"
            //나중에 저장해서 워치로 보내려면 임시로 저장해야함
            //   string = tv.text as String?

        } else {
            tv.text = "메세지 전송 중입니다."
        }

    }


})
}

}