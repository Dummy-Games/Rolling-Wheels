package whsa.rools.wheels.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import whsa.rools.wheels.R

class ghgjgfhgdgfsfsd : Fragment(R.layout.gfdgdfhfdh) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.ndfgxdf).setOnClickListener {
            findNavController().navigate(R.id.ghgfdgh)
        }

        view.findViewById<Button>(R.id.fhdhdtejfgnf).setOnClickListener {
            findNavController().navigate(R.id.ghdfbvhgd)
        }
    }
}
