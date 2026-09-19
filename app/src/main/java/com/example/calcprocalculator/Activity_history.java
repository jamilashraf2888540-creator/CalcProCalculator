package com.example.calcprocalculator;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calcprocalculator.DataBase.HistoryAdapter;
import com.example.calcprocalculator.DataBase.HistoryModel;
import com.example.calcprocalculator.DataBase.MyDatabase;

import java.util.List;

public class Activity_history extends AppCompatActivity {

    RecyclerView rv_history;
    ImageButton btn_back, btn_delete_all;
    HistoryAdapter adapter;
    MyDatabase db;
    List<HistoryModel> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // ربط العناصر
        rv_history     = findViewById(R.id.rv_history);
        btn_back       = findViewById(R.id.btn_back);
        btn_delete_all = findViewById(R.id.btn_delete_all);

        // إنشاء قاعدة البيانات
        db = new MyDatabase(this);

        // جلب البيانات
        historyList = db.getAllHistory();

        // إنشاء الـ Adapter وربطه بالـ RecyclerView
        adapter = new HistoryAdapter(historyList);
        rv_history.setLayoutManager(new LinearLayoutManager(this));
        rv_history.setAdapter(adapter);


        // زر الرجوع
        btn_back.setOnClickListener(v -> {
            finish();
        });

// زر حذف كل السجلات
        btn_delete_all.setOnClickListener(v -> {
            db.deleteAllHistory();
            historyList.clear();
            adapter.notifyDataSetChanged();
        });





    }
}