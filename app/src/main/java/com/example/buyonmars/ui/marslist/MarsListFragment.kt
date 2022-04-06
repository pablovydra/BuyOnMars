package com.example.buyonmars.ui.marslist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buyonmars.databinding.FragmentMarsListBinding
import com.example.buyonmars.models.dto.MarsProperty
import com.example.buyonmars.ui.marslist.adapter.MarsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarsListFragment : Fragment(), MarsAdapter.MarsAdapterActions {

    private lateinit var binding: FragmentMarsListBinding
    private val viewModel: MarsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarsListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.setAdapterOnView.observe(viewLifecycleOwner, Observer {
            binding.recycler.adapter =
                viewModel.properties?.value?.let { properties ->
                    viewModel.favorites.value?.let { favorites -> MarsAdapter(properties, favorites, this, requireContext()) }
                }
        })

        binding.recycler.layoutManager = GridLayoutManager(this.context, 2)
        binding.recycler.setHasFixedSize(true)

        return binding.root
    }

    override fun addToFavorite(marsProperty: MarsProperty, callback: (() -> Unit)?) {
        viewModel.insert(marsProperty)
        callback?.invoke()
    }

    override fun removeFavorite(marsProperty: MarsProperty) {
        viewModel.delete(marsProperty)
    }

    override fun navigatToProperty(id: Int) {

    }

}