package com.faridaaidah.consumerapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.faridaaidah.consumerapp.adapter.PagerAdapter
import com.faridaaidah.consumerapp.databinding.ActivityDetailBinding
import com.faridaaidah.consumerapp.model.DetailUserModel
import com.faridaaidah.consumerapp.model.UserModel
import com.faridaaidah.consumerapp.retrofit.APIConfig
import org.jetbrains.annotations.NotNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {

    private var dataDetailUser: DetailUserModel? = null
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
    }

    private fun loadData() {
        val dataUser = intent.getParcelableExtra<UserModel>("userlist")
        if (dataUser != null) {
            getAPIData(dataUser)
        }

        Glide.with(this)
            .load(dataUser?.avatar)
            .into(binding.imgAvatar)

        val nameUser = dataUser?.login
        supportActionBar?.title = nameUser

        //tab and view pager
        val pagerAdapter = PagerAdapter(this, supportFragmentManager)
        pagerAdapter.username = dataUser?.login
        binding.vpFolls.adapter = pagerAdapter
        binding.tabsFolls.setupWithViewPager(binding.vpFolls)

        supportActionBar?.elevation = 0f
    }

    private fun getAPIData(dataUser: UserModel) {
        //get data from API
        dataUser.login?.let {
            APIConfig().getService()
                .getDetailUser(it)
                .enqueue(object : Callback<DetailUserModel> {
                    override fun onFailure(call: Call<DetailUserModel>, t: Throwable) {
                        binding.signNoconnect.visibility = VISIBLE
                    }

                    override fun onResponse(
                        call: Call<DetailUserModel>,
                        @NotNull response: Response<DetailUserModel>
                    ) {
                        dataDetailUser = response.body()
                        binding.tvUsername.text = dataDetailUser?.login
                        binding.tvLocation.text = dataDetailUser?.location
                        binding.tvName.text = dataDetailUser?.name
                        binding.tvRepo.text = dataDetailUser?.repository
                        binding.tvCompany.text = dataDetailUser?.company
                    }
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_user -> {
                startActivity(Intent(this, MainActivity::class.java))
                return true
            }
            R.id.nav_set -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return true
    }
}
