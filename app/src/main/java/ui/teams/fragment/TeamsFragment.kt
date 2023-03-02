package ui.teams.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetpack.demo.R
import com.jetpack.demo.databinding.FragmentTeamsBinding
import dagger.hilt.android.AndroidEntryPoint
import data.local.db.dao.TeamItem
import domain.teams.viewmodel.TeamDBViewModel
import domain.teams.viewmodel.TeamsViewModel
import main.MainApplication
import ui.main.MainActivity
import ui.matches.fragments.MatchesFragment
import ui.teams.adapter.TeamsAdapter
import utils.constant.*


@AndroidEntryPoint
class TeamsFragment : Fragment(), TeamsAdapter.ItemClickListener {
    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    private lateinit var teamsAdapter: TeamsAdapter
    private val teamsViewModel: TeamsViewModel by viewModels()

    //Database
    private val teamTblViewModel: TeamDBViewModel by viewModels {
        TeamDBViewModel.TeamItemViewModelFactory(MainApplication.instance.repository)
    }

    companion object {

        fun newInstance(): TeamsFragment {
            val teamsFragment = TeamsFragment()
            val args = Bundle()
            teamsFragment.arguments = args
            return teamsFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        teamsAdapter = TeamsAdapter(context = requireContext(), this)
        setupRecyclerView()

        teamsViewModel.getTeamsResult.asLiveData().observe(viewLifecycleOwner, Observer { it ->
            val getTeamsResult = it ?: return@Observer
            val list = getTeamsResult.success?.teamsResponse?.listTeams
            teamsAdapter.submitList(list)
            hideProgressDialog()
            //Save to DB (if necessary)
//            list?.forEach {
//                teamTblViewModel.insert(TeamItem(id.toString(), it.name, it.logo))
//            }
//            teamTblViewModel.allItems.observe(viewLifecycleOwner) { listItem ->
//                listItem?.let {
//
//                }
//            }
        })

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvTeams.apply {
            adapter = teamsAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            hasFixedSize()

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressDialog()
        teamsViewModel.getListTeams()

    }

    private fun showProgressDialog() {
        if (isAdded) {
            childFragmentManager.showProgressDialog(
                getString(R.string.processing),
                getString(R.string.please_wait)
            )
        }
    }

    private fun hideProgressDialog() {
        if (isAdded) {
            childFragmentManager.hideProgressDialog()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onItemClick(id: String) {
        val fragment = MatchesFragment.newInstance()
        val bundle = Bundle()
        bundle.putString(KEY_TEAM_ID, id)
        fragment.arguments = bundle
        (activity as MainActivity).replaceFragment(fragment,R.id.mainContainer,MainActivity.MatchFragmentTAG)
    }
}