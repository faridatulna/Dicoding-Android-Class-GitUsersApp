package gitusersapp.app.com.gitusersapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import gitusersapp.app.com.gitusersapp.R
import gitusersapp.app.com.gitusersapp.adapter.UserAdapter
import gitusersapp.app.com.gitusersapp.databinding.FragmentFollowingBinding
import gitusersapp.app.com.gitusersapp.model.UserModel
import gitusersapp.app.com.gitusersapp.retrofit.APIConfig
import kotlinx.android.synthetic.main.fragment_following.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowingFragment : Fragment() {
    private var followingList: List<UserModel> = arrayListOf()
    private lateinit var binding: FragmentFollowingBinding

    companion object{
        private const val ARG_USERNAME = "username"

        fun newinstance(username: String?): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE
        rv_following.layoutManager = LinearLayoutManager(activity)

        if (username != null) {
            APIConfig().getService()
                .getFollowings(username)
                .enqueue(object : Callback<List<UserModel>> {
                    override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(view.context, "Pastikan Koneksi Internet Stabil", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<UserModel>>,
                        response: Response<List<UserModel>>
                    ) {
                        binding.progressBar.visibility = View.GONE
                        followingList = response.body()!!
                        rv_following.adapter = UserAdapter(followingList)
                        binding.rvFollowing.setHasFixedSize(true)
                    }
                })
        }
    }
}