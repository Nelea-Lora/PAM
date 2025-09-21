package com.example.hearingsupport.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hearingsupport.NotificationReceiver;
import com.example.hearingsupport.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button btn1 = binding.btn1;
        EditText input = binding.etInput;

        btn1.setOnClickListener(v->{
            String query = input.getText().toString();
            Intent browsIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/search?q=" + Uri.encode(query)));
            startActivity(browsIntent);
//            Intent applicationIntent  = new Intent();
//            PackageManager packageManager = getPackageManager();
//            ComponentName componentName = applicationIntent.resolveActivity(packageManager);
//            if (componentName == null) {
//                String query = input.getText().toString();
//                Intent browsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" +
//                        Uri.encode(query)));
//                if (browsIntent.resolveActivity(packageManager) != null) {
//                    startActivity(browsIntent);
//                } else {
//                    startActivity(applicationIntent);
//
//                }
//            }
        });

        binding.btn2.setOnClickListener(v -> {
//            if (android.os.Build.VERSION.SDK_INT >= 33) {
//                if (requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
//                        != android.content.pm.PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
//                    return;
//                }
//            }
            long triggerAt = System.currentTimeMillis() + 10_000L;

            android.content.Intent i = new android.content.Intent(requireContext(), NotificationReceiver.class);
            i.putExtra("title", "Напоминание");
            i.putExtra("text", "Прошло 10 секунд");

            android.app.PendingIntent pi = android.app.PendingIntent.getBroadcast(
                    requireContext(),
                    1001,
                    i,
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE
            );

            android.app.AlarmManager am = (android.app.AlarmManager) requireContext().getSystemService(android.content.Context.ALARM_SERVICE);
            try {
                am.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, triggerAt, pi);
                android.widget.Toast.makeText(requireContext(),
                        "Уведомление будет показано через 10 секунд",
                        android.widget.Toast.LENGTH_SHORT).show();
            } catch (SecurityException se) {
                am.set(android.app.AlarmManager.RTC_WAKEUP, triggerAt, pi);
                android.widget.Toast.makeText(requireContext(),
                        "Точная тревога недоступна, использую обычную — может сработать с небольшой задержкой",
                        android.widget.Toast.LENGTH_SHORT).show();
            }
//                    am.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, triggerAt, pi);
//
//                    android.widget.Toast.makeText(requireContext(), "Уведомление будет показано через 10 секунд", android.widget.Toast.LENGTH_SHORT).show();
        });
//        final TextView textView = binding.btn1;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}