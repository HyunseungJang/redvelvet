package com.lx.red

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lx.red.databinding.FragmentVoiceOneBinding
import com.lx.red.databinding.FragmentVoiceTwoBinding
import kotlinx.android.synthetic.main.activity_help_request.*

class VoiceTwoFragment : Fragment() {

    var _binding: FragmentVoiceTwoBinding? = null
    val binding get() = _binding!!

    val TAG : String = "MainActivity"
    var mediaplayer1 : MediaPlayer?= null
    var mediaplayer2 : MediaPlayer?= null
    var mediaplayer3 : MediaPlayer?= null
    var mediaplayer4 : MediaPlayer?= null
    var mediaplayer5 : MediaPlayer?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVoiceTwoBinding.inflate(inflater, container, false)

        mediaplayer1 = MediaPlayer.create(context, R.raw.nugu)
        mediaplayer2 = MediaPlayer.create(context, R.raw.siren)
        mediaplayer3 = MediaPlayer.create(context, R.raw.gun)
        mediaplayer4 = MediaPlayer.create(context, R.raw.ne)
        mediaplayer5 = MediaPlayer.create(context, R.raw.moon)


        // 아저씨 목소리
        binding.firstButton1.setOnClickListener {
            mediaplayer1?.start()
        }
        binding.firstButton2.setOnClickListener {
            mediaplayer1?.stop()
            mediaplayer1?.reset()
        }

        binding.firstButton3.setOnClickListener {
            mediaplayer5?.start()
        }
        binding.firstButton4.setOnClickListener {
            mediaplayer5?.stop()
            mediaplayer5?.reset()
        }

        binding.firstButton5.setOnClickListener {
            mediaplayer4?.start()
        }
        binding.firstButton6.setOnClickListener {
            mediaplayer4?.stop()
            mediaplayer4?.reset()
        }

        // 사이렌 + 총
        binding.sirenButton.setOnClickListener {
            mediaplayer2?.start()
            binding.sirenButton.setOnClickListener {
                mediaplayer2?.stop()
                mediaplayer2?.reset()
            }
        }

        binding.gunButton.setOnClickListener {
            mediaplayer3?.start()
            binding.gunButton.setOnClickListener {
                mediaplayer3?.stop()
                mediaplayer3?.reset()
            }
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