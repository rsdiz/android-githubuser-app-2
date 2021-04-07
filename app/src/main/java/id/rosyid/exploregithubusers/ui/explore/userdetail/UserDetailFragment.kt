package id.rosyid.exploregithubusers.ui.explore.userdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import id.rosyid.exploregithubusers.R
import id.rosyid.exploregithubusers.data.entities.UserDetailResponse
import id.rosyid.exploregithubusers.databinding.UserDetailFragmentBinding
import id.rosyid.exploregithubusers.ui.explore.follow.FollowFragment
import id.rosyid.exploregithubusers.utils.Resource
import id.rosyid.exploregithubusers.utils.autoCleared
import kotlin.math.abs

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private var binding: UserDetailFragmentBinding by autoCleared()
    private val viewModel: UserDetailViewModel by viewModels()
    private lateinit var baseActivity: AppCompatActivity
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var dataUsername: String
    private lateinit var intentShare: Intent

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title_follower,
            R.string.title_following
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserDetailFragmentBinding.inflate(inflater, container, false)
        dataUsername = UserDetailFragmentArgs.fromBundle(arguments as Bundle).username
        baseActivity = activity as AppCompatActivity

        setAppBar()
        setTabLayout()
        binding.fabShare.setOnClickListener { performShareUser() }

        return binding.root
    }

    private fun setAppBar() {
        navHostFragment = baseActivity.supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        baseActivity.setSupportActionBar(binding.toolbar)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        setHasOptionsMenu(true)
    }

    private fun setTabLayout() {
        val sectionsPagerAdapter = SectionsPagerAdapter(baseActivity, dataUsername)
        binding.viewPagerLayout.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerLayout) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers(dataUsername)
    }

    private fun setupObservers(username: String) {
        viewModel.userDetailObserver(viewLifecycleOwner, username) { data, status ->
            onObserveSuccess(data, status)
        }
    }

    private fun onObserveSuccess(userDetailResponse: UserDetailResponse?, status: Resource.Status) {
        when (status) {
            Resource.Status.SUCCESS -> {
                if (userDetailResponse != null) {
                    intentShare = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            resources.getString(
                                R.string.share_text,
                                userDetailResponse.username,
                                userDetailResponse.repositories,
                                userDetailResponse.followers,
                                userDetailResponse.following
                            )
                        )
                        type = "text/plain"
                    }

                    binding.apply {
                        Glide.with(requireContext())
                            .load(Uri.parse(userDetailResponse.avatarUrl))
                            .circleCrop()
                            .placeholder(R.mipmap.ic_github)
                            .into(userImage)
                        baseActivity.supportActionBar?.title = userDetailResponse.name
                        userName.text = userDetailResponse.name ?: getString(R.string.empty_value)
                        baseActivity.supportActionBar?.subtitle = userDetailResponse.username
                        userUsername.text = userDetailResponse.username
                        userFollowers.text = userDetailResponse.followers.toString()
                        userFollowings.text = userDetailResponse.following.toString()
                        userCompany.text =
                            userDetailResponse.company ?: getString(R.string.empty_value)
                        userLocation.text =
                            userDetailResponse.location ?: getString(R.string.empty_value)
                        userRepository.text = userDetailResponse.repositories.toString()
                        userBio.text = userDetailResponse.bio ?: getString(R.string.empty_value)
                        userBlog.text = userDetailResponse.blog ?: getString(R.string.empty_value)
                        userEmail.text = userDetailResponse.email ?: getString(R.string.empty_value)
                        userTwitter.text = userDetailResponse.twitterUsername ?: getString(R.string.empty_value)
                    }

                    showLoading(false)

                    viewModel.removeUserDetailObserver(
                        viewLifecycleOwner, userDetailResponse.username
                    )
                }
            }
            Resource.Status.ERROR -> showError(true)
            Resource.Status.LOADING -> showLoading(true)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.apply {
                layoutLoading.visibility = View.VISIBLE
                layoutDetailUser.visibility = View.GONE
                layoutError.visibility = View.GONE
            }
        } else {
            binding.apply {
                layoutLoading.visibility = View.GONE
                layoutDetailUser.visibility = View.VISIBLE
                layoutError.visibility = View.GONE
            }
        }
    }

    private fun showError(state: Boolean) {
        if (state) {
            binding.apply {
                layoutLoading.visibility = View.GONE
                layoutDetailUser.visibility = View.GONE
                layoutError.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                layoutError.visibility = View.GONE
                layoutLoading.visibility = View.VISIBLE
                layoutDetailUser.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
        setupFAB(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language_settings -> {
                val settingIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(settingIntent)
            }
            R.id.share -> performShareUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun performShareUser() {
        val shareIntent = Intent.createChooser(intentShare, null)
        startActivity(shareIntent)
    }

    private fun setupFAB(menu: Menu) {
        binding.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                    baseActivity.supportActionBar?.setDisplayShowTitleEnabled(true)
                    menu.setGroupVisible(R.id.other_group, true)
                } else if (verticalOffset == 0) {
                    baseActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
                    menu.setGroupVisible(R.id.other_group, false)
                }
            }
        )
    }
}

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowFragment.newInstance(FollowFragment.Type.FOLLOWER, username)
            1 -> FollowFragment.newInstance(FollowFragment.Type.FOLLOWING, username)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = 2
}
