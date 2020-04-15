package com.volontero.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.volontero.di.Injectable
import com.volontero.main.feedback_adapter.FeedbackAdapter
import com.volontero.main.view_models.FeedbacksViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.volontero.R
import kotlinx.android.synthetic.main.fragment_all_feedbacks.*
import javax.inject.Inject

class AllFeedbacksFragment : BottomSheetDialogFragment(), Injectable {

    private val args: AllFeedbacksFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var adapter: FeedbackAdapter

    private lateinit var viewModel: FeedbacksViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_feedbacks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FeedbacksViewModel::class.java)
        setupRecyclerView()
        viewModel.fetchNotes(args.poiId)

        viewModel.notesList.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                adapter.items = it
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = FeedbackAdapter()
        recycler_view_fragment_all_feedbacks.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view_fragment_all_feedbacks.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
        recycler_view_fragment_all_feedbacks.adapter = adapter
    }
}