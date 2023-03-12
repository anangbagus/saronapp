package com.example.saronapp.ui.score

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.saronapp.R
import com.example.saronapp.databinding.ActivityScoreFragmentBinding
import com.example.saronapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreFragment : DialogFragment() {
    private var _binding: ActivityScoreFragmentBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityScoreFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val score = arguments?.getFloat(EXTRA_SCORE, 0F)

        binding?.tvScore?.text = getString(R.string.score_template, score)

        binding?.btnOk?.setOnClickListener {
            Intent(activity, MainActivity::class.java).also {
                activity?.startActivity(it)
            }
            activity?.finish()
        }
        return binding?.root
    }

    override fun onResume() {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_rectangle)
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_SCORE = "extra_score"
    }
}