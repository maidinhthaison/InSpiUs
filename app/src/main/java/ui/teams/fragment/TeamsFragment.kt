package ui.teams.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ui.teams.adapter.TeamsAdapter
import domain.teams.viewmodel.TeamsViewModel
import utils.constant.*
import com.jetpack.demo.R
import com.jetpack.demo.databinding.FragmentTeamsBinding


@AndroidEntryPoint
class TeamsFragment : Fragment() {
    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    private lateinit var teamsAdapter: TeamsAdapter
    private val teamsViewModel: TeamsViewModel by viewModels()

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
        teamsAdapter = TeamsAdapter(context = requireContext())
        setupRecyclerView()

        teamsViewModel.getTeamsResult.asLiveData().observe(viewLifecycleOwner, Observer {
            val getTeamsResult = it ?: return@Observer
            val list = getTeamsResult.success?.teamsResponse?.listTeams
            teamsAdapter.submitList(list)
            hideProgressDialog()
        })

        return binding.root
    }
    private fun setupRecyclerView(){
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
}