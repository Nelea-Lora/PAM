package com.example.hearingsupport.ui.slideshow;
import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class SharedPhotoViewModel extends ViewModel{
    private final MutableLiveData<Uri> photoUri = new MutableLiveData<>();
    public void set(Uri uri) { photoUri.setValue(uri); }
    public LiveData<Uri> get() { return photoUri; }
}
