package com.example.library;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();

        db = databaseHelper.open();
        //получаем данные из бд в виде курсора
        userCursor = db.rawQuery("select * from book", null);

        Button download_image5 = findViewById(R.id.button5); //реализовать для фото кеширование и предзагрузку
        TextView textview = findViewById(R.id.textView2);
        userCursor.moveToFirst();
        textview.setText(userCursor.getString(1));

        Glide.with(this)
                .load("https://litres.ru/pub/c/cover/6130095.jpg")
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        download_image5.setBackground(resource);
                    }
                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        // Обработка очистки ресурса, если необходимо
                    }
                });
        Button download_image6 = findViewById(R.id.button6); //реализовать для фото кеширование и предзагрузку
        Glide.with(this)
                .load("https://s4.knigavuhe.org/2/covers/4173/3-2.jpg?2")
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        download_image6.setBackground(resource);
                    }
                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        // Обработка очистки ресурса, если необходимо
                    }
                });
        Button switc_to_another_window5 = findViewById(R.id.button5);
        switc_to_another_window5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Book_content.class);
                startActivity(intent);
                finish(); //закрытие старого окна
            }
        });
        Button menu4 = findViewById(R.id.button4);
        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.my_menu, popup.getMenu());
                popup.show();
            }
        });
    }
}
