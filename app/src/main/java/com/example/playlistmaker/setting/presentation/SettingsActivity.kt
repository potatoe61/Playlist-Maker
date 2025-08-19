package com.example.playlistmaker.setting.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.setting.presentation.viewModel.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : Fragment() {

    private val viewModelSetting by viewModel<SettingViewModel>()

    private var _binding : ActivitySettingsBinding? = null
    val binding : ActivitySettingsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivitySettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val backButton = binding.tvHeader




        binding.themeSwitch.isChecked = viewModelSetting.getTheme()
        binding.themeSwitch.setOnCheckedChangeListener { switcher, checked ->
            //viewModelSetting.editTheme(checked)
            viewModelSetting.switchTheme(checked)
        }


        //backButton.setNavigationOnClickListener{
            //parentFragmentManager.popBackStack()
        //}

        binding.support.setOnClickListener{
            viewModelSetting.supportSend()
        }

        binding.agreement.setOnClickListener{
            viewModelSetting.openTerm()
        }

        binding.share.setOnClickListener{
            viewModelSetting.shareApp()
        }
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}