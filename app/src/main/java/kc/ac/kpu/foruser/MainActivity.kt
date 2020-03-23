package kc.ac.kpu.foruser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main.*
import org.jetbrains.anko.toast
import java.io.FileInputStream
import java.io.FileOutputStream
import org.jetbrains.anko.toast

import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    private var frame: FrameLayout? = null;

    var result: TextView? = null
    val database: FirebaseDatabase? = null
    val databaseReference: DatabaseReference? = null


    private val monNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {

            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                when (p0.itemId) {
                    R.id.action_home -> {
                        var main = Main.Companion.newInstance()
                        addFrag(main)

                        return true


                    }
                    R.id.action_account -> {
                        val my = MY()
                        addFrag(my)
                        return true
                    }
                }
                return false
            }
        }

    private fun addFrag(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        frame = findViewById(R.id.frame) as FrameLayout
        val navigationView = findViewById(R.id.bottom_navigation) as BottomNavigationView
        navigationView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener)

        val fragment = Main.Companion.newInstance()
        addFrag(fragment)
/*
        button1.setOnClickListener {
            startActivity(Intent(fragment.context, Order::class.java))
        }
*/
    }
}
}
