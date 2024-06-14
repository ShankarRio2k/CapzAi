package com.capztone.capzai.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.capztone.capzai.R
import com.capztone.capzai.databinding.FragmentIdeaBinding


class IdeaFragment : Fragment() {

    private var _binding: FragmentIdeaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIdeaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tryItLabel.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("promptText", "Graphical Style: Robot Building")
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, homeFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
        binding.label2.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("promptText", "Realistic Style: Animal Study")
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, homeFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
        binding.label3.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("promptText", "Cyberpunk Style: Future City")
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, homeFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
        binding.label5.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("promptText", "Vintage Style: Historical Event")
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, homeFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
        binding.label6.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("promptText", "Graphical Style: Robot Building")
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, homeFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }

        binding.label7.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("promptText", "Graphical Style: Robot Building")
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, homeFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }

        binding.label8.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("promptText", "Graphical Style: Robot Building")
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, homeFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
