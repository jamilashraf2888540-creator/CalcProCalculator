package com.example.calcprocalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.calcprocalculator.DataBase.MyDatabase;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btn_clear, btn_sign, btn_percent, btn_divide, btn_7, btn_8, btn_9,
            btn_6, btn_5, btn_4, btn_3, btn_2, btn_1, btn_multiply, btn_subtract, btn_add,
            btn_0, btn_dot, btn_equals;

    ImageButton btn_menu, btn_settings;
    TextView tv_toolbar_title, tv_expression, tv_result;
    CardView card_display;

    String first_number = "";
    String second_number = "";
    String operator = "";
    boolean justCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setListeners();
        Intent();

        btn_menu.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Activity_history.class);
            startActivity(intent);
        });

    }

    void Intent(){

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Activity_settings.class);
                startActivity(intent);

            }
        });

    }

    // ===== ربط العناصر =====
    void initViews() {
        btn_clear        = findViewById(R.id.btn_clear);
        btn_sign         = findViewById(R.id.btn_sign);
        btn_percent      = findViewById(R.id.btn_percent);
        btn_divide       = findViewById(R.id.btn_divide);
        btn_7            = findViewById(R.id.btn_7);
        btn_8            = findViewById(R.id.btn_8);
        btn_9            = findViewById(R.id.btn_9);
        btn_6            = findViewById(R.id.btn_6);
        btn_5            = findViewById(R.id.btn_5);
        btn_4            = findViewById(R.id.btn_4);
        btn_3            = findViewById(R.id.btn_3);
        btn_2            = findViewById(R.id.btn_2);
        btn_1            = findViewById(R.id.btn_1);
        btn_multiply     = findViewById(R.id.btn_multiply);
        btn_subtract     = findViewById(R.id.btn_subtract);
        btn_add          = findViewById(R.id.btn_add);
        btn_0            = findViewById(R.id.btn_0);
        btn_dot          = findViewById(R.id.btn_dot);
        btn_equals       = findViewById(R.id.btn_equals);
        btn_menu         = findViewById(R.id.btn_menu);
        btn_settings     = findViewById(R.id.btn_settings);
        tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        tv_expression    = findViewById(R.id.tv_expression);
        tv_result        = findViewById(R.id.tv_result);
        btn_settings = findViewById(R.id.btn_settings);
    }

    // ===== جميع الـ Listeners =====
    void setListeners() {

        // ----- أزرار الأرقام -----
        btn_0.setOnClickListener(v -> appendNumber("0"));
        btn_1.setOnClickListener(v -> appendNumber("1"));
        btn_2.setOnClickListener(v -> appendNumber("2"));
        btn_3.setOnClickListener(v -> appendNumber("3"));
        btn_4.setOnClickListener(v -> appendNumber("4"));
        btn_5.setOnClickListener(v -> appendNumber("5"));
        btn_6.setOnClickListener(v -> appendNumber("6"));
        btn_7.setOnClickListener(v -> appendNumber("7"));
        btn_8.setOnClickListener(v -> appendNumber("8"));
        btn_9.setOnClickListener(v -> appendNumber("9"));

        // ----- النقطة العشرية -----
        btn_dot.setOnClickListener(v -> {
            // لو حسب للتو ابدأ من جديد
            if (justCalculated) {
                first_number = "";
                second_number = "";
                operator = "";
                tv_expression.setText("0.");
                justCalculated = false;
                return;
            }
            if (operator.isEmpty()) {
                // في الرقم الأول
                if (!first_number.contains(".")) {
                    if (first_number.isEmpty()) first_number = "0";
                    first_number += ".";
                    tv_expression.setText(first_number);
                }
            } else {
                // في الرقم الثاني
                if (!second_number.contains(".")) {
                    if (second_number.isEmpty()) second_number = "0";
                    second_number += ".";
                    updateDisplay();
                }
            }
        });

        // ----- أزرار العمليات -----
        btn_add.setOnClickListener(v -> setOperator("+", "+"));
        btn_subtract.setOnClickListener(v -> setOperator("-", "-"));
        btn_multiply.setOnClickListener(v -> setOperator("*", "×"));
        btn_divide.setOnClickListener(v -> setOperator("÷", "÷"));

        // ----- زر C (مسح الكل) -----
        btn_clear.setOnClickListener(v -> {
            first_number  = "";
            second_number = "";
            operator      = "";
            justCalculated = false;
            tv_expression.setText("");
            tv_result.setText("0");
        });

        // ----- زر +/- -----
        btn_sign.setOnClickListener(v -> {
            if (operator.isEmpty()) {
                // نغير إشارة الرقم الأول
                if (!first_number.isEmpty()) {
                    double num = Double.parseDouble(first_number);
                    num *= -1;
                    first_number = formatNumber(num);
                    tv_expression.setText(first_number);
                }
            } else {
                // نغير إشارة الرقم الثاني
                if (!second_number.isEmpty()) {
                    double num = Double.parseDouble(second_number);
                    num *= -1;
                    second_number = formatNumber(num);
                    updateDisplay();
                }
            }
        });

        // ----- زر % -----
        btn_percent.setOnClickListener(v -> {
            if (operator.isEmpty()) {
                if (!first_number.isEmpty()) {
                    double num = Double.parseDouble(first_number);
                    num /= 100;
                    first_number = formatNumber(num);
                    tv_expression.setText(first_number);
                }
            } else {
                if (!second_number.isEmpty()) {
                    double num = Double.parseDouble(second_number);
                    num /= 100;
                    second_number = formatNumber(num);
                    updateDisplay();
                }
            }
        });

        // ----- زر = -----
        btn_equals.setOnClickListener(v -> calculate());
    }

    // ===== دالة إضافة الأرقام =====
    void appendNumber(String number) {
        // لو المستخدم ضغط رقم بعد = ابدأ حساب جديد
        if (justCalculated) {
            first_number   = number;
            second_number  = "";
            operator       = "";
            justCalculated = false;
            tv_expression.setText(first_number);
            tv_result.setText("0");
            return;
        }

        if (operator.isEmpty()) {
            // نضيف على الرقم الأول
            if (first_number.equals("0")) {
                first_number = number;
            } else {
                first_number += number;
            }
            tv_expression.setText(first_number);
        } else {
            // نضيف على الرقم الثاني
            if (second_number.equals("0")) {
                second_number = number;
            } else {
                second_number += number;
            }
            updateDisplay();
        }
    }

    // ===== دالة العمليات =====
    void setOperator(String op, String displayOp) {
        // لو ما في رقم أول لا تكمل
        if (first_number.isEmpty()) return;

        // لو عنده رقم ثاني احسب أولاً ثم خذ النتيجة كرقم أول
        if (!second_number.isEmpty()) {
            calculate();
            first_number  = tv_result.getText().toString();
            second_number = "";
        }

        operator       = op;
        justCalculated = false;
        tv_expression.setText(first_number + " " + displayOp + " ");
    }

    // ===== دالة الحساب =====
    void calculate() {
        if (first_number.isEmpty() || second_number.isEmpty() || operator.isEmpty()) {
            return;
        }

        try {
            double num1   = Double.parseDouble(first_number);
            double num2   = Double.parseDouble(second_number);
            double result = 0;

            switch (operator) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "÷":
                    if (num2 == 0) {
                        tv_result.setText("Error");
                        return;
                    }
                    result = num1 / num2;
                    break;
                default:
                    tv_result.setText("Error");
                    return;
            }

            String resultStr = formatNumber(result);
            tv_result.setText(resultStr);

            // بعد الحساب
            first_number   = resultStr;
            second_number  = "";
            operator       = "";
            justCalculated = true;

        } catch (Exception e) {
            tv_result.setText("Error");
        }

        // بعد ما تعرض النتيجة — احفظ في قاعدة البيانات
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(new Date());

        MyDatabase db = new MyDatabase(this);
        db.insertHistory(
                tv_expression.getText().toString(),  // العملية
                tv_result.getText().toString(),       // النتيجة
                time                                  // الوقت
        );
        db.close();


    }

    // ===== دالة تحديث الشاشة =====
    void updateDisplay() {
        String displayOp;
        switch (operator) {
            case "*": displayOp = "×"; break;
            case "÷": displayOp = "÷"; break;
            default:  displayOp = operator;
        }
        tv_expression.setText(first_number + " " + displayOp + " " + second_number);
    }

    // ===== دالة تنسيق الرقم (بدون .0 لو عدد صحيح) =====
    String formatNumber(double num) {
        if (num == (long) num) {
            return String.valueOf((long) num);
        } else {
            return String.valueOf(num);
        }
    }
}