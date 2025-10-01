package com.example.hearingsupport.ui.calendar;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.*;

import com.example.hearingsupport.R;

import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;


public class CalendarFragment extends Fragment {

    private static final String EXTRA_SELECTED_DATE_UTC = "extra_selected_date_utc";
    private CalendarView calendarView;
    private ListView listEvents;
    private Button btnAdd, btnUpdate;
    private long selectedDateUtcMillis;
    private LocalDate selectedDate = LocalDate.now();

//    public CalendarFragment() {
//        // Required empty public constructor
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        listEvents   = view.findViewById(R.id.listEvents);
        btnAdd       = view.findViewById(R.id.btnAdd);
        btnUpdate    = view.findViewById(R.id.btnUpdate);

        // 1) Настраиваем диапазон календаря
        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth   = currentMonth.minusMonths(12);
        YearMonth endMonth     = currentMonth.plusMonths(12);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();

        calendarView.setup(startMonth, endMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);

        calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                DayViewContainer container = new DayViewContainer(view);
                view.setOnClickListener(v -> {
                    CalendarDay day = container.day;
                    if (day.getPosition() == DayPosition.MonthDate) {
                        LocalDate old = selectedDate;
                        selectedDate = day.getDate();

                        // Перерисовать старую и новую ячейки
                        calendarView.notifyDateChanged(old);
                        calendarView.notifyDateChanged(selectedDate);

                        // Обновить миллисекунды (UTC, полночь)
                        selectedDateUtcMillis = selectedDate.atStartOfDay(ZoneOffset.UTC)
                                .toInstant().toEpochMilli();

                        // сюда можно подгрузить события за выбранный день
                        // reloadEventsFor(selectedDateUtcMillis);
                    }
                });
                return container;
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day) {
                container.day = day;
                TextView tv = container.textView;
                tv.setText(String.valueOf(day.getDate().getDayOfMonth()));

                if (day.getPosition() == DayPosition.MonthDate) {
                    tv.setAlpha(1f);
                } else {
                    tv.setAlpha(0.3f); // дни не из текущего месяца
                }

                // Подсветка выбранной даты
                boolean isSelected = day.getDate().equals(selectedDate);
                tv.setTypeface(null, isSelected ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
                tv.setBackgroundResource(isSelected ? android.R.drawable.dialog_holo_light_frame : 0);
            }
        });

        // 3) Заголовок месяца (опционально)
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthHeaderContainer>() {
            @NonNull @Override
            public MonthHeaderContainer create(@NonNull View view) {
                return new MonthHeaderContainer(view);
            }

            @Override
            public void bind(@NonNull MonthHeaderContainer container, @NonNull com.kizitonwose.calendar.core.CalendarMonth month) {
                String title = month.getYearMonth().getMonth().toString() + " " + month.getYearMonth().getYear();
                container.title.setText(title.substring(0,1) + title.substring(1).toLowerCase(Locale.getDefault()));
            }
        });

        // Установим selectedDateUtcMillis для старта:
        selectedDateUtcMillis = selectedDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), AddEventActivity.class);
            i.putExtra(EXTRA_SELECTED_DATE_UTC, selectedDateUtcMillis);
            startActivity(i);
        });

        btnUpdate.setOnClickListener(v -> {
//            int pos = listEvents.getCheckedItemPosition();
//                    if (pos != ListView.INVALID_POSITION) {
//                        Event e = adapter.getItem(pos);
//                        Intent i = new Intent(requireContext(), UpdateEventActivity.class);
//                        i.putExtra(UpdateEventActivity.EXTRA_EVENT_ID, e.getId());
//                        startActivity(i);
//                    } else {
//                        Toast.makeText(requireContext(), "Выберите событие", Toast.LENGTH_SHORT).show();
//                    }
        });
    }
    // Вспомогательный контейнер для заголовка месяца
    public static class MonthHeaderContainer extends com.kizitonwose.calendar.view.ViewContainer {
        final TextView title;
        public MonthHeaderContainer(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.monthText);
        }
    }
}