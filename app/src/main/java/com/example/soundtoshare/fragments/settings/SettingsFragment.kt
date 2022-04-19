package com.example.soundtoshare.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.soundtoshare.main.MainActivity
import com.example.soundtoshare.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)

        val currentMode = (this.activity as MainActivity).incognito
        if (currentMode) binding.buttonIncognito.text = "Incognito Mode: ON"
        else binding.buttonIncognito.text = "Incognito Mode: OFF"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignOut.setOnClickListener {
            (this.activity as MainActivity).vkSignOut()
        }
        binding.buttonIncognito.setOnClickListener {
            val currentMode = (this.activity as MainActivity).incognito
            (this.activity as MainActivity).incognito = !currentMode
            (this.activity as MainActivity).setIncognitoMode(!currentMode)
            if (currentMode) binding.buttonIncognito.text = "Incognito Mode: OFF"
            else binding.buttonIncognito.text = "Incognito Mode: ON"
            Toast.makeText(this.activity, "TODO", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
