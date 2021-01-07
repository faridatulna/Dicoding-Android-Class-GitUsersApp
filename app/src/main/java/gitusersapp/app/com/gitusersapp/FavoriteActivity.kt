package gitusersapp.app.com.gitusersapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import gitusersapp.app.com.gitusersapp.adapter.FavoriteAdapter
import gitusersapp.app.com.gitusersapp.database.UserHelper
import gitusersapp.app.com.gitusersapp.databinding.ActivityFavoriteBinding
import gitusersapp.app.com.gitusersapp.helper.MappingHelper
import gitusersapp.app.com.gitusersapp.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        favoriteAdapter = FavoriteAdapter()
        binding.rvUsers.adapter = favoriteAdapter

        userHelper = UserHelper.getDatabase(applicationContext)
        userHelper.open()

        if (savedInstanceState == null) {
            loadData()
        }
    }

    private fun loadData(){
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val users: ArrayList<UserModel>
            users = deferredUsers.await()
            if (users.isNotEmpty()) {
                favoriteAdapter.listFavorite = users
            } else {
                favoriteAdapter.listFavorite = ArrayList()
                binding.signNoData.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
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