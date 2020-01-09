package com.lambdaschool.hackathon_portal.ui.fragments.account


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.lambdaschool.hackathon_portal.R
import com.lambdaschool.hackathon_portal.ui.MainActivity

class AccountFragment : Fragment() {

    private val fragmentComponent by lazy {
        (activity as MainActivity)
            .activityComponent
            .getFragmentComponentBuilder()
            .bindFragment(this)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent.injectAccountFragment(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
}
