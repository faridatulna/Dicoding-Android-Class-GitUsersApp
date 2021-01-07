package gitusersapp.app.com.gitusersapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import gitusersapp.app.com.gitusersapp.adapter.PagerAdapter
import gitusersapp.app.com.gitusersapp.database.UserHelper
import gitusersapp.app.com.gitusersapp.databinding.ActivityDetailBinding
import gitusersapp.app.com.gitusersapp.model.DetailUserModel
import gitusersapp.app.com.gitusersapp.model.UserModel
import gitusersapp.app.com.gitusersapp.retrofit.APIConfig
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

    private fun loadData(){
        val dataUser= intent.getParcelableExtra<UserModel>("userlist")
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
        binding.vpFollow.adapter = pagerAdapter
        binding.tabsFolls.setupWithViewPager(binding.vpFollow)

        userHelper = UserHelper.getDatabase(applicationContext)
        userHelper.open()
        if (userHelper.checkUser(dataUser?.id.toString())){
            binding.btnFav.visibility = GONE
            binding.btnUnfav.visibility = VISIBLE
        }
        //click button to adds favorite user
        binding.btnFav.setOnClickListener {
            if (dataUser != null) {
                favoriteUser(dataUser,"favorite")
            }
            binding.btnFav.visibility = INVISIBLE
            binding.btnUnfav.visibility = VISIBLE
        }

        binding.btnUnfav.setOnClickListener {
            if (dataUser != null) {
                favoriteUser(dataUser,"unfavorite")
            }
            binding.btnFav.visibility = VISIBLE
            binding.btnUnfav.visibility = INVISIBLE
        }

        supportActionBar?.elevation = 0f
    }

    private fun getAPIData(dataUser: UserModel){
        //get data from API
        dataUser.login?.let {
            APIConfig().getService()
                .getDetailUser(it)
                .enqueue(object : Callback<DetailUserModel> {
                    override fun onFailure(call: Call<DetailUserModel>, t: Throwable){
                        Toast.makeText(this@DetailActivity, "Pastikan Koneksi Internet Stabil", Toast.LENGTH_SHORT).show()
                        binding.signNoConnect.visibility = VISIBLE
                    }
                    override fun onResponse(call: Call<DetailUserModel>,@NotNull response: Response<DetailUserModel>) {
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

    private fun favoriteUser(user:UserModel, status:String){
        if(status == "unfavorite"){
            val result = userHelper.deleteById(user.id.toString()).toLong()
            if(result > 0){
                Toast.makeText(this@DetailActivity, "Sukses menghapus user favorite", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@DetailActivity, "Gagal menghapus user favorite", Toast.LENGTH_SHORT).show()
            }
        }else{
            val result = userHelper.insert(user)
            if(result>0){
                Toast.makeText(this@DetailActivity, "Sukses menambah user favorite", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this@DetailActivity,"Gagal menambah user favorite",Toast.LENGTH_SHORT
                ).show()
            }
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
