package com.example.hearingsupport.ui.calendar;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.kizitonwose.calendar.view.ViewContainer;
import com.example.hearingsupport.R;

public class MonthHeaderContainer extends ViewContainer {
    public final TextView title;
    public MonthHeaderContainer(@NonNull View view) {
        super(view);
        title = view.findViewById(R.id.monthText);
    }
}
