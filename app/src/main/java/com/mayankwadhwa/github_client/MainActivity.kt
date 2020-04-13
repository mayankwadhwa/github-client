package com.mayankwadhwa.github_client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.repository.RepositoryImpl
import com.mayankwadhwa.github_client.viewmodel.GithubViewModel
import com.mayankwadhwa.github_client.viewmodel.GithubViewModelFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val repository = RepositoryImpl(GithubAPI.create())
        val viewModel = ViewModelProvider(this,GithubViewModelFactory(repository)).get(GithubViewModel::class.java)
    }
}
