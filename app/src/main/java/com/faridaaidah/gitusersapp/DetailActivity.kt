package com.faridaaidah.gitusersapp

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.faridaaidah.gitusersapp.adapter.PagerAdapter
import com.faridaaidah.gitusersapp.database.UserContract
import com.faridaaidah.gitusersapp.database.UserContract.UserColumns.Companion.CONTENT_URI
import com.faridaaidah.gitusersapp.database.UserHelper
import com.faridaaidah.gitusersapp.databinding.ActivityDetailBinding
import com.faridaaidah.gitusersapp.model.DetailUserModel
import com.faridaaidah.gitusersapp.model.UserModel
import com.faridaaidah.gitusersapp.retrofit.APIConfig
import org.jetbrains.annotations.NotNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {

    private var dataDetailUser: DetailUserModel? = null
    private lateinit var binding: ActivityDetailBinding
    private lateinit var userHelper: UserHelper

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

        userHelper = UserHelper.getDatabase(applicationContext)
        userHelper.open()
        if (userHelper.checkUser(dataUser?.id.toString())) {
            binding.btnFav.visibility = GONE
            binding.btnUnfav.visibility = VISIBLE
        }
        //click button to adds favorite user
        binding.btnFav.setOnClickListener {
            if (dataUser != null) {
                favoriteUser(dataUser, "favorite")
            }
            binding.btnFav.visibility = INVISIBLE
            binding.btnUnfav.visibility = VISIBLE
        }

        binding.btnUnfav.setOnClickListener {
            if (dataUser != null) {
                favoriteUser(dataUser, "unfavorite")
            }
            binding.btnFav.visibility = VISIBLE
            binding.btnUnfav.visibility = INVISIBLE
        }

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

    private fun favoriteUser(user: UserModel, status: String) {
        val args = ContentValues()
        args.put(UserContract.UserColumns.ID, user.id)
        args.put(UserContract.UserColumns.USERNAME, user.login)
        args.put(UserContract.UserColumns.AVATAR_URL, user.avatar)
        args.put(UserContract.UserColumns.URL, user.url)

        if (status == "unfavorite") {
            contentResolver.delete(Uri.parse(CONTENT_URI.toString() + "/" + user.id), null, null)
            Toast.makeText(
                this@DetailActivity,
                getString(R.string.del_user_success),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            contentResolver.insert(CONTENT_URI, args)
            Toast.makeText(
                this@DetailActivity,
                getString(R.string.add_user_success),
                Toast.LENGTH_SHORT
            ).show()
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
                finish()
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
