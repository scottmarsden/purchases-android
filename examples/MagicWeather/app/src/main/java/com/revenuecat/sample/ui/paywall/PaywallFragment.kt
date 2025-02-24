package com.revenuecat.sample.ui.paywall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.revenuecat.purchases.*
import com.revenuecat.sample.R
import com.revenuecat.sample.extensions.buildError

class PaywallFragment : Fragment() {
    private lateinit var root: View
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PaywallAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_paywall, container, false)

        recyclerView = root.findViewById(R.id.paywall_list)

        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        adapter = PaywallAdapter(null, didChoosePackage = { item: Package ->
            purchasePackage(item)
        })

        recyclerView.adapter = adapter

        /*
        Load offerings when the paywall is displayed
         */
        fetchOfferings()

        return root
    }

    private fun fetchOfferings() {
        Purchases.sharedInstance.getOfferingsWith { offerings: Offerings ->
            adapter.offering = offerings.current
            adapter.notifyDataSetChanged()
        }
    }

    private fun purchasePackage(item: Package) {
        Purchases.sharedInstance.purchasePackageWith(requireActivity(), item,
                onError = { error, userCancelled ->
                    if (!userCancelled) {
                        buildError(context, error.message)
                    }
                },
                onSuccess = { _, _ ->
                    activity?.finish()
                })
    }
}
