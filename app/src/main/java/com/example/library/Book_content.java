package com.example.library;

import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;

public class Book_content extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_content);

        ImageButton switch_to_another_window1 = findViewById(R.id.imagebutton1);
        switch_to_another_window1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Book_content.this, MainActivity.class);
                startActivity(intent);
                finish(); //закрытие старого окна
            }
        });

        Button book_description = findViewById(R.id.button3); //я оставлю пока но это нужно реализовать через смену текста а не чрез новое окно
        book_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Book_content.this);
                dialog.setContentView(R.layout.book_description);

                // закрыть диалоговое окно при нажатии вне его, установите следующее:
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
    }
}
