package id.kasrt.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import id.kasrt.face.FaceRecognitionActivity
import id.kasrt.R

class Tab4 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab4, container, false)

        val buttonEditProfile = view.findViewById<Button>(R.id.buttonEditProfile)
        val buttonFaceRecognition = view.findViewById<Button>(R.id.tv_to_face)

        buttonEditProfile.setOnClickListener { v: View? -> }

        buttonFaceRecognition.setOnClickListener { v: View? ->
            val intent = Intent(
                activity,
                FaceRecognitionActivity::class.java
            )
            intent.putExtra("ACTION_TYPE", "REGISTER")
            startActivity(intent)
        }

        return view
    }
}