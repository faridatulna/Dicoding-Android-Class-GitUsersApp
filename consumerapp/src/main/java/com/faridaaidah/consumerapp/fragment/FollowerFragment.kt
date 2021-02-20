package com.faridaaidah.consumerapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.faridaaidah.consumerapp.R
import com.faridaaidah.consumerapp.adapter.UserAdapter
import com.faridaaidah.consumerapp.databinding.FragmentFollowerBinding
import com.faridaaidah.consumerapp.model.UserModel
import com.faridaaidah.consumerapp.retrofit.APIConfig
import kotlinx.android.synthetic.main.fragment_follower.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {
    private var followerList: List<UserModel> = arrayListOf()
    private lateinit var binding: FragmentFollowerBinding

    companion object {
        private const val ARG_USERNAME = "username"

        fun newinstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
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
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)
        binding = FragmentFollowerBinding.inflate(layoutInflater)

        binding.progressBar.visibility = View.VISIBLE
        rv_follower.layoutManager = LinearLayoutManager(activity)

        if (username != null) {
            APIConfig().getService()
                .getFollowers(username)
                .enqueue(object : Callback<List<UserModel>> {
                    override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                    }

                    override fun onResponse(
                        call: Call<List<UserModel>>,
                        response: Response<List<UserModel>>
                    ) {
                        binding.progressBar.visibility = View.GONE
                        followerList = response.body()!!
                        rv_follower.adapter = UserAdapter(followerList)
                        binding.rvFollower.setHasFixedSize(true)
                    }
                })
        }
    }
}