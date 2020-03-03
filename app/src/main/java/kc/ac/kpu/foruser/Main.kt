package kc.ac.kpu.foruser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

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
    }
}