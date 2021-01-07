package com.faridaaidah.gitusersapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.faridaaidah.gitusersapp.adapter.UserAdapter
import com.faridaaidah.gitusersapp.databinding.ActivityMainBinding
import com.faridaaidah.gitusersapp.model.UserModel
import com.faridaaidah.gitusersapp.retrofit.APIConfig
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var list: List<UserModel> = arrayListOf()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Github Users"
        loadDataAPI()
    }

    private fun loadDataAPI() {
        binding.progressBar.visibility = View.VISIBLE
        rv_group.layoutManager = LinearLayoutManager(this)
        APIConfig().getService()
            .getSearchUser("username")
            .enqueue(object : Callback<UserModel> {
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.signNoConnect.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
                ) {
                    binding.progressBar.visibility = View.GONE
                    list = response.body()?.getItems()!!
                    rv_group.adapter = UserAdapter(list)
                    binding.rvGroup.setHasFixedSize(true)
                }
            })
    }

    private fun getDataOnline(query: String) {
        binding.progressBar.visibility = View.VISIBLE

        APIConfig().getService()
            .getSearchUser(query)
            .enqueue(object : Callback<UserModel> {
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
                ) {
                    binding.progressBar.visibility = View.GONE
                    list = response.body()?.getItems()!!
                    rv_group.adapter = UserAdapter(list)
                    binding.rvGroup.setHasFixedSize(true)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_search -> {
                val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchView = item.actionView as SearchView

                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = resources.getString(R.string.username)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    /*
                    Gunakan method ini ketika search selesai atau OK
                     */
                    override fun onQueryTextSubmit(query: String): Boolean {
                        Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                        getDataOnline(query)
                        return true
                    }

                    /*
                    Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
                     */
                    override fun onQueryTextChange(newText: String): Boolean {
                        return false
                    }
                })
                return true
            }
            R.id.nav_fav -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
                return true
            }
            R.id.nav_set -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            else -> return true
        }
    }
}