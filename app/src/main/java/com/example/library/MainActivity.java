package com.example.library;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.ImageButton;

import java.io.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.GridLayout.LayoutParams;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Button download_image5 = findViewById(R.id.button5); //реализовать для фото кеширование и предзагрузку



        LinearLayout linearLayoutTest = (LinearLayout) findViewById(R.id.test1);


        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();
        db = databaseHelper.open();
        //получаем данные из бд в виде курсора
        userCursor = db.rawQuery("select * from book", null);
        userCursor.moveToFirst();
        do{ //крч этот цикл создает готовые окошки книг с картинкой и текстом снизу

            LinearLayout linearLayoutDouble = new  LinearLayout(new ContextThemeWrapper(this.getBaseContext(), R.style.linearLayoutDouble)); //объединим две книги в пару
            for (int i = 0; i < 2; i++) {
                LinearLayout linearLayout = new LinearLayout(new ContextThemeWrapper(MainActivity.this, R.style.BookLayout));
                android.widget.ImageButton button = new android.widget.ImageButton(new ContextThemeWrapper(this.getBaseContext(), R.style.BookButton), null, 0);
                TextView textView = new TextView(new ContextThemeWrapper(this.getBaseContext(), R.style.BookText));

                String filename = userCursor.getString(2);
                int resID = getResources().getIdentifier(filename , "drawable", getPackageName());
                button.setImageResource(resID);
                button.setId(userCursor.getInt(0));
                int width = 170;
                int height = 284;
                int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width,  getResources().getDisplayMetrics());
                int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height,  getResources().getDisplayMetrics());
                button.setLayoutParams(new ViewGroup.LayoutParams(w, h));

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        button.getId();
                        Intent intent = new Intent(MainActivity.this, Book_content.class);
                        intent.putExtra("id", button.getId());
                        startActivity(intent);
                        finish(); //закрытие старого окна
                    }
                });

                textView.setText(userCursor.getString(1));
                linearLayout.addView(button);
                linearLayout.addView(textView);

                linearLayoutDouble.addView(linearLayout);
            }

            linearLayoutTest.addView(linearLayoutDouble);

        } while(userCursor.moveToNext());





        //TextView textview = findViewById(R.id.textView2);
        //userCursor.moveToFirst();
        //textview.setText(userCursor.getString(1));



        /*
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
        */
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
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

}
