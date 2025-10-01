package com.example.hearingsupport.ui.calendar;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.hearingsupport.R;
import com.kizitonwose.calendar.view.ViewContainer;
import com.kizitonwose.calendar.core.CalendarDay;

public class DayViewContainer extends ViewContainer {
    public final TextView textView;
    public CalendarDay day; // присваиваем в bind()

    public DayViewContainer(@NonNull View view) {
        super(view);
        textView = view.findViewById(R.id.calendarDayText);
    }
}
