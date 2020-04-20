package com.mayankwadhwa.github_client

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mayankwadhwa.github_client.adapter.TrendingListAdapter
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.persistence.GithubDatabase
import com.mayankwadhwa.github_client.repository.GithubRepositoryImpl
import com.mayankwadhwa.github_client.viewmodel.GithubViewModel
import com.mayankwadhwa.github_client.viewmodel.GithubViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val progressDialog by lazy {
        ProgressDialog.show(this, "Loading", "Please Wait")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dao = GithubDatabase.getDatabase(this).githubDao()
        val repository = GithubRepositoryImpl(GithubAPI.create(), dao, lifecycleScope)
        val viewModel = ViewModelProvider(
            this,
            GithubViewModelFactory(repository)
        ).get(GithubViewModel::class.java)


        viewModel.getLoading().observe(this, Observer {
            it?.let { loading ->
                showLoading(loading)
            }
        })

        viewModel.getTrendingList()?.observe(this, Observer {
            it?.let { trendingList ->
                showTrendingList(trendingList)
            }
        })

        viewModel.getError().observe(this, Observer {
            showError(it)
        })

        button_retry.setOnClickListener {
            viewModel.init()
        }

        viewModel.init()

    }

    private fun showError(error: Throwable?) {
        if (error != null) {
            layout_error.visibility = View.VISIBLE
            Snackbar.make(layout_parent, "${error.message}", Snackbar.LENGTH_SHORT).show()
        } else
            layout_error.visibility = View.GONE
    }

    private fun showTrendingList(trendingList: List<RepoModel>) {
        list_trending.adapter = TrendingListAdapter(trendingList)
    }

    private fun showLoading(loading: Boolean) {
        if (loading)
            progressDialog.show()
        else
            progressDialog.hide()
    }
}
