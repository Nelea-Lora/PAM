package com.example.hearingsupport.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.hearingsupport.R;
import com.example.hearingsupport.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        com.example.hearingsupport.ui.slideshow.SharedPhotoViewModel sharedVm =
                new ViewModelProvider(requireActivity()).get(com.example.hearingsupport.ui.slideshow.SharedPhotoViewModel.class);

        ImageView iv = view.findViewById(R.id.imageView1);
        sharedVm.get().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null && iv != null) {
                iv.setImageURI(uri);
            }
        });
        Button back = view.findViewById(R.id.button);
        if (back != null) {
            back.setOnClickListener(v -> {
                NavController nav = Navigation.findNavController(v);

                boolean popped = nav.popBackStack();

                if (!popped) {
                    nav.navigate(R.id.nav_home);
                }
            });
        }
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}