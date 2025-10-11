package com.example.hearingsupport.ui.calendar;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.hearingsupport.R;
import com.example.hearingsupport.ui.calendar.EventsXmlStore;
import com.example.hearingsupport.ui.calendar.Event;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.Calendar;


public class UpdateEventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_ID = "extra_event_id";

    private DatePicker datePicker;
    private TextInputEditText etTitle, etInfo;
    private Button btnSave, btnCancel;

    private Event event; // редактируемое событие

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_update);

        datePicker = findViewById(R.id.datePicker);
        etTitle    = findViewById(R.id.etTitle);
        etInfo     = findViewById(R.id.etInfo);
        btnSave    = findViewById(R.id.btnSave);
        btnCancel  = findViewById(R.id.btnCancel);

        // Получаем ID из интента
        String eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Нет ID события", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Загружаем событие
        event = EventsXmlStore.getById(this, eventId);
        if (event == null) {
            Toast.makeText(this, "Событие не найдено", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Предзаполняем UI
        Calendar cal = Calendar.getInstance();
        cal.set(event.getDate().getYear(),
                event.getDate().getMonthValue() - 1,
                event.getDate().getDayOfMonth());
        datePicker.updateDate(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        etTitle.setText(event.getTitle());
        etInfo.setText(event.getInfo() == null ? "" : event.getInfo());

        // Кнопка "Выйти"
        btnCancel.setOnClickListener(v -> finish());

        // Кнопка "Подтвердить"
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText() == null ? "" : etTitle.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "Тема обязательна", Toast.LENGTH_SHORT).show();
                return;
            }

            int y = datePicker.getYear();
            int m = datePicker.getMonth(); // 0-based
            int d = datePicker.getDayOfMonth();

            event.setDate(LocalDate.of(y, m + 1, d));
            event.setTitle(title);
            String info = etInfo.getText() == null ? "" : etInfo.getText().toString().trim();
            event.setInfo(info.isEmpty() ? null : info);

            boolean ok = EventsXmlStore.update(this, event);
            if (ok) {
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
            }
        });
        NestedScrollView scroll = findViewById(R.id.scrollContainer);
        View root = scroll.getChildAt(0);
        root.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            root.getWindowVisibleDisplayFrame(r);
            int screenHeight = root.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;
            
            if (keypadHeight > screenHeight * 0.15) {
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    scroll.post(() -> scroll.scrollTo(0, currentFocus.getBottom()));
                }
            }
        });
    }
}