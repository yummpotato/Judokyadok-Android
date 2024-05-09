package com.example.nunettine.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nunettine.CircleTransform
import com.example.nunettine.R
import com.example.nunettine.data.remote.dto.study.StudyCategoryRes
import com.example.nunettine.data.remote.service.library_study.QuizService
import com.example.nunettine.data.remote.view.study.StudyCategoryView
import com.example.nunettine.databinding.FragmentHomeBinding
import com.example.nunettine.ui.home.viewmodel.HomeViewModel
import com.example.nunettine.ui.main.MainActivity
import com.squareup.picasso.Picasso

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        var spf = requireContext().getSharedPreferences("kakao", AppCompatActivity.MODE_PRIVATE)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.profileML.observe(viewLifecycleOwner, Observer {
            homeViewModel.profileML.postValue(spf.getString("profile", ""))
            Picasso.get().load(homeViewModel.profileML.value).transform(CircleTransform()).into(binding.homeProfileIv)
        })

        clickListener()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    fun saveData(text_type: String) {
        // 데이터 저장
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("type", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("type", text_type)
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun clickListener() = with(binding) {
        home1Btn.setOnClickListener {
            saveData("PREVTEXT")
            moveFragment(TypeFragment())
        }

        home2Btn.setOnClickListener {
            saveData("MYTEXT")
            moveFragment(TypeFragment())
        }
    }
    private fun moveFragment(fragment: Fragment) {
        val mainActivity = context as MainActivity
        val mainFrmLayout = mainActivity.findViewById<FrameLayout>(R.id.main_frm) as FrameLayout?
        if (mainFrmLayout != null) {
            val transaction = mainActivity.supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            transaction.replace(mainFrmLayout.id, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}