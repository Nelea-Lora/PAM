package com.example.hearingsupport.ui.calendar;


import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hearingsupport.R;

import java.time.LocalDate;
import java.util.Calendar;
public class AddEventActivity extends AppCompatActivity {

    private static final String EXTRA_SELECTED_DATE_UTC = "extra_selected_date_utc";

    private DatePicker datePicker;
    private EditText etTitle, etInfo;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_add);

        datePicker = findViewById(R.id.datePicker);
        etTitle    = findViewById(R.id.etTitle);
        etInfo     = findViewById(R.id.etInfo);
        btnSave    = findViewById(R.id.btnSave);
        btnCancel  = findViewById(R.id.btnCancel);

        long dateMillis = getIntent().getLongExtra(EXTRA_SELECTED_DATE_UTC, System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateMillis);
        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText() == null ? "" : etTitle.getText().toString().trim();
            String info  = etInfo.getText() == null ? "" : etInfo.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Введите тему!", Toast.LENGTH_SHORT).show();
                return;
            }

            int y = datePicker.getYear();
            int m = datePicker.getMonth() + 1; // 0-based
            int d = datePicker.getDayOfMonth();

            LocalDate date = LocalDate.of(y, m, d);
            Event event = EventsXmlStore.createNew(date, title, info);

            boolean ok = EventsXmlStore.add(this, event);
            if (ok) {
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
