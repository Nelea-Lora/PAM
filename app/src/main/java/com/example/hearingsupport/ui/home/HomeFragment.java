package com.example.hearingsupport.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hearingsupport.R;
import com.example.hearingsupport.databinding.FragmentHomeBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import com.example.hearingsupport.ui.slideshow.SharedPhotoViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private int lensFacing = CameraSelector.LENS_FACING_FRONT;
    private com.example.hearingsupport.ui.slideshow.SharedPhotoViewModel sharedVm;


    private final ActivityResultLauncher<String> camPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    startCamera();
                } else {
                    boolean neverAskAgain =
                            !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                    if (neverAskAgain) {
                        android.widget.Toast.makeText(requireContext(),
                                "Разрешите доступ к камере в настройках приложения", android.widget.Toast.LENGTH_LONG).show();
                        openAppSettings();
                    } else {
                        android.widget.Toast.makeText(requireContext(),
                                "Без разрешения камера не работает", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        sharedVm = new ViewModelProvider(requireActivity()).get(com.example.hearingsupport.ui.slideshow.SharedPhotoViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RadioButton rbFront = root.findViewById(R.id.rbFront);
        RadioButton rbBack = root.findViewById(R.id.rbBack);
        Button btn1 = root.findViewById(R.id.btn1);
        previewView         = root.findViewById(R.id.previewView);

        rbFront.setOnCheckedChangeListener((b, checked) -> {
            if (checked) {
                lensFacing = CameraSelector.LENS_FACING_FRONT;
                startCamera();
            }
        });
        rbBack.setOnCheckedChangeListener((b, checked) -> {
            if (checked) {
                lensFacing = CameraSelector.LENS_FACING_BACK;
                startCamera();
            }
        });

        btn1.setOnClickListener(v -> takePhoto());

        ensurePermissionAndStart();

        Button btn2 = root.findViewById(R.id.btn2);
        btn2.setOnClickListener(v -> {
            androidx.navigation.NavController nav =
                    androidx.navigation.Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            nav.navigate(R.id.nav_slideshow);
        });
        return root;
    }
    private void ensurePermissionAndStart() {
        int state = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        android.widget.Toast.makeText(requireContext(),
                "CAMERA perm = " + (state == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"),
                android.widget.Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            camPermission.launch(Manifest.permission.CAMERA);
        } else {
            startCamera();
        }
    }
    private void startCamera() {
        if (binding != null) binding.btn1.setEnabled(false);

        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(requireContext());

        future.addListener(() -> {
            try {
                ProcessCameraProvider provider = future.get();
                provider.unbindAll();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector selector = new CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build();

                provider.bindToLifecycle(this, selector, preview, imageCapture);

                if (binding != null) binding.btn1.setEnabled(true);

            } catch (Exception e) {
                android.widget.Toast.makeText(requireContext(),
                        "Camera init error: " + e.getMessage(),
                        android.widget.Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }
    private void takePhoto() {
        if (imageCapture == null) {
            android.widget.Toast.makeText(requireContext(),
                    "Камера ещё не готова", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(requireContext().getCacheDir(),
                "shot_" + System.currentTimeMillis() + ".jpg");

        ImageCapture.OutputFileOptions opts =
                new ImageCapture.OutputFileOptions.Builder(file).build();

        imageCapture.takePicture(opts, ContextCompat.getMainExecutor(requireContext()),
                new ImageCapture.OnImageSavedCallback() {
                    @Override public void onImageSaved(@NonNull ImageCapture.OutputFileResults out) {
                        Uri uri = Uri.fromFile(file);
                        android.widget.ImageView header = requireActivity().findViewById(R.id.imageView);
                        if (header != null) {
                            header.setImageURI(uri);
                        }
                        sharedVm.set(uri);
                        android.widget.Toast.makeText(requireContext(), "Фото сохранено",
                                android.widget.Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onError(@NonNull ImageCaptureException ex) {
                        android.widget.Toast.makeText(requireContext(),
                                "Capture error: " + ex.getMessage(),
                                android.widget.Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void openAppSettings() {
        android.content.Intent intent = new android.content.Intent(
                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                android.net.Uri.fromParts("package", requireContext().getPackageName(), null));
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}