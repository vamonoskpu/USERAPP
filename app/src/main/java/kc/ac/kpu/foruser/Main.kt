package kc.ac.kpu.foruser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return view;
    }




}