package com.example.hearingsupport.ui.calendar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hearingsupport.R;
import com.example.hearingsupport.ui.calendar.EventsXmlStore;
import com.example.hearingsupport.ui.calendar.Event;

import java.util.Calendar;
public class UpdateEventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_ID = "extra_event_id";

    private DatePicker datePicker;
    private EditText etTitle, etInfo;
    private Button btnSave, btnCancel;

    private Event event; // текущая модель

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_event); // твой layout из сообщения

        datePicker = findViewById(R.id.datePicker);
        etTitle    = findViewById(R.id.etTitle);
        etInfo     = findViewById(R.id.etInfo);
        btnSave    = findViewById(R.id.btnSave);
        btnCancel  = findViewById(R.id.btnCancel);

        String eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Нет ID события", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

//        // 1) Загрузить событие по ID
//        event = EventsXmlStore.getById(this, eventId);
//        if (event == null) {
//            Toast.makeText(this, "Событие не найдено", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // 2) Предзаполнить UI
//        // дата
//        Calendar cal = Calendar.getInstance();
//        cal.set(event.getDate().getYear(), event.getDate().getMonthValue() - 1, event.getDate().getDayOfMonth());
//        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//
//        // текстовые поля
//        etTitle.setText(event.getTitle());
//        etInfo.setText(event.getInfo() == null ? "" : event.getInfo());
//
//        // 3) Кнопки
//        btnCancel.setOnClickListener(v -> finish());
//
//        btnSave.setOnClickListener(v -> {
//            // валидация
//            String title = etTitle.getText().toString().trim();
//            if (title.isEmpty()) {
//                Toast.makeText(this, "Тема обязательна", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // собрать новые значения
//            int y = datePicker.getYear();
//            int m = datePicker.getMonth(); // 0-based
//            int d = datePicker.getDayOfMonth();
//
//            // обновить модель
//            event.setDate(java.time.LocalDate.of(y, m + 1, d));
//            event.setTitle(title);
//            String info = etInfo.getText().toString().trim();
//            event.setInfo(info.isEmpty() ? null : info);
//
//            // сохранить
//            boolean ok = EventsXmlStore.update(this, event);
//            if (ok) {
//                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Не удалось сохранить", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
