package com.example.busfinder.ui.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.busfinder.databinding.FragmentNavigationsBinding;

public class NavigationsFragment extends Fragment {

    private FragmentNavigationsBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NavigationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NavigationsViewModel.class);

        binding = FragmentNavigationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}