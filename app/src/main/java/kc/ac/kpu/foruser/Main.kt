package kc.ac.kpu.foruser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.main.*

class Main: Fragment() {

    companion object{
        fun newInstance() : Main {
            var main = Main()
            var args = Bundle()
            main.arguments = args
            return main
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater!!.inflate(R.layout.main,container,false)

        var button6 = view.findViewById<Button>(R.id.button6);

        button6.setOnClickListener(View.OnClickListener { v ->
            startActivity(Intent(v.context,QuestionList::class.java))
        })


        return view;

        var button = view.findViewById<Button>(R.id.button1)

        button.setOnClickListener(View.OnClickListener { v ->
           /* Toast.makeText(
                v.context,
                "dd",
                Toast.LENGTH_LONG
            ).show()
            */
            startActivity(Intent(v.context,Order::class.java))

        })
        return view

    }

}