package com.example.soundtoshare.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.soundtoshare.databinding.FragmentSettingsBinding
import com.example.soundtoshare.external.ObservableUserSongInfo
import com.example.soundtoshare.main.MainActivity


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsFragmentViewModel
    private var incognitoMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        viewModel = SettingsFragmentViewModel(this.requireContext())

        initIncognitoModeButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignOut.setOnClickListener {
            (this.activity as MainActivity).vkSignOut()
        }
        binding.buttonIncognito.setOnClickListener {
            toggleIncognitoMode()
        }
        binding.buttonCheckStatus.setOnClickListener{
            Toast.makeText(
                this.requireContext(),
                "TODO",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun toggleIncognitoMode() {
        incognitoMode = !incognitoMode
        viewModel.setIncognitoMode(incognitoMode)
        if (incognitoMode) binding.buttonIncognito.text = incognitoOn
        else binding.buttonIncognito.text = incognitoOff
    }

    private fun initIncognitoModeButton(){
        ObservableUserSongInfo.incognitoMode.observe(activity as LifecycleOwner) {
            incognitoMode = it
            if (incognitoMode) binding.buttonIncognito.text = incognitoOn
            else binding.buttonIncognito.text = incognitoOff
        }
    }

    companion object {
        const val incognitoOff = "Incognito Mode: OFF"
        const val incognitoOn = "Incognito Mode: ON"
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
