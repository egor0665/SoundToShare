package com.example.soundtoshare.fragments.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.soundtoshare.databinding.FragmentSettingsBinding
import com.example.soundtoshare.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class Settings : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)

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
            val fragment = this
            viewModel.checkUserStatus {
                val result = if (this != null)
                    "Успешно! Ваша текущая музыка: " + this.artist + "-" + this.title
                else
                    "Проверьте включили ли Вы музыку и транлируите ли Вы ее в статус"
                Toast.makeText(
                    fragment.requireContext(),
                    result,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initIncognitoModeButton(){
        viewModel.getObservableIncognitoMode().observe(activity as LifecycleOwner) {
            setIncognitoModeButton(it)
        }
    }

    private fun setIncognitoModeButton(mode: Boolean){
        if (mode) binding.buttonIncognito.text = incognitoOn
        else binding.buttonIncognito.text = incognitoOff
    }

    private fun toggleIncognitoMode() {
        val newMode = !viewModel.getIncognitoMode()
        Log.d("SharedPreference",newMode.toString())
        viewModel.setIncognitoMode(newMode)
        setIncognitoModeButton(newMode)
    }

    companion object {
        const val incognitoOff = "Incognito Mode: OFF"
        const val incognitoOn = "Incognito Mode: ON"
        fun newInstance(): Settings {
            return Settings()
        }
    }
}
