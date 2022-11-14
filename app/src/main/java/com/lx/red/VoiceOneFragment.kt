package com.lx.red

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lx.red.databinding.FragmentVoiceOneBinding

class VoiceOneFragment : Fragment() {

    var _binding: FragmentVoiceOneBinding? = null
    val binding get() = _binding!!

    val TAG : String = "MainActivity"
    var mediaplayer1 : MediaPlayer?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVoiceOneBinding.inflate(inflater, container, false)

        mediaplayer1 = MediaPlayer.create(context, R.raw.sample1)

        binding.firstButton1.setOnClickListener {
            mediaplayer1?.start()
        }
        binding.firstButton2.setOnClickListener {
            mediaplayer1?.stop()
            mediaplayer1?.reset()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showToast(message:String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}