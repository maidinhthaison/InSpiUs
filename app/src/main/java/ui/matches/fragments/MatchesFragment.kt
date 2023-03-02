package ui.matches.fragments

import android.content.Intent
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
import androidx.work.*
import com.jetpack.demo.R
import com.jetpack.demo.databinding.FragmentMatchesBinding
import dagger.hilt.android.AndroidEntryPoint
import data.remote.workers.DownloadFileWorker
import data.response.Matches
import domain.matches.viewmodel.MatchViewModel
import timber.log.Timber
import ui.exoplayer.PlayingActivity
import ui.matches.adapter.MatchesAdapter
import utils.constant.*


@AndroidEntryPoint
class MatchesFragment : Fragment(), MatchesAdapter.HightlightsClickListener,
    MatchesAdapter.DownloadClickListener {
    private var _binding: FragmentMatchesBinding? = null
    private val binding get() = _binding!!

    private lateinit var matchesAdapter: MatchesAdapter
    private val matchesViewModel: MatchViewModel by viewModels()

    private lateinit var matches: Matches

    companion object {
        fun newInstance(): MatchesFragment {
            val matchesFragment = MatchesFragment()
            val args = Bundle()
            matchesFragment.arguments = args
            return matchesFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchesBinding.inflate(inflater, container, false)
        matchesAdapter = MatchesAdapter(context = requireContext(), this, this)
        setupRecyclerView()
        setUpView()
        matchesViewModel.getMatchesResult.asLiveData().observe(viewLifecycleOwner, Observer {
            val getMatchesResult = it ?: return@Observer
            matches = getMatchesResult.success?.matchesResponse?.matches!!
            matchesAdapter.submitList(matches?.listPrevious)
            hideProgressDialog()
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressDialog()
        val teamId = arguments?.getString(KEY_TEAM_ID)

        if (teamId == null) {
            matchesViewModel.getListMatches()

        } else {
            matchesViewModel.getListMatchesPerTeam(teamId)
        }

    }

    private fun setupRecyclerView() {
        binding.rvMatch.apply {
            adapter = matchesAdapter
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

    private fun setUpView() {
        binding.btnPrevious.setOnClickListener {
            matchesAdapter.submitList(matches?.listPrevious)
        }
        binding.btnUpComing.setOnClickListener {
            matchesAdapter.submitList(matches?.listUpcoming)
        }

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

    private fun startDownloadingFile(
        fileName: String, fileUrl: String, fileType: String
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()
        val data = Data.Builder()

        data.apply {
            putString(KEY_INPUT_FILE_NAME, fileName)
            putString(KEY_INPUT_FILE_URL, fileUrl)
            putString(KEY_INPUT_FILE_TYPE, fileType)
        }

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(DownloadFileWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(requireContext()).enqueue(oneTimeWorkRequest)

        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this) { info ->
                info?.let {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> {

                            showToast(
                                requireContext(),"Saved:${it.outputData.getString(KEY_OUTPUT_FILE_URI)}",
                                1
                            )
                        }
                        WorkInfo.State.FAILED -> {
                            showToast(
                                requireContext(),
                                it.outputData.getString(KEY_OUTPUT_MESSAGE).toString(),
                                1
                            )
                        }
                        WorkInfo.State.RUNNING -> {
                            showToast(requireContext(), "DOWNLOAD IN PROGRESS!!!", 1)

                        }
                        else -> {

                        }
                    }
                }
            }
    }


    override fun onHightLights(url: String) {
        if ("null" != url) {
            val intent = Intent(activity, PlayingActivity::class.java)
            intent.putExtra(KEY_URL_HIGHTLIGHTS, url)
            startActivity(intent)
        } else {
            showToast(requireContext(), "The video is upcoming", 0)
        }
    }


    override fun onDownloadClick(fileName: String, fileUrl: String, fileType: String) {
        if ("null" != fileUrl) {
            Timber.d("${fileName}-${fileUrl}-${fileType}")
            startDownloadingFile(fileName, fileUrl, fileType)
        } else {
            showToast(requireContext(), "The video is upcoming", 0)
        }
    }

}