package com.example.library;


import static com.example.library.R.menu.my_menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Console;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    LinearLayout linearLayoutTest;
    EditText searchBarText;
    ImageButton searchImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //реализовать для фото кеширование и предзагрузку

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();
        db = databaseHelper.open();
        //получаем данные из бд в виде курсора

        LinearLayout linearLayoutTest = (LinearLayout) findViewById(R.id.test1);
        linearLayoutTest.removeAllViews();

        String sql = "select * from book";
        DBselect(sql);

        EditText searchBarText = findViewById(R.id.editText1);
        searchBarText.setText("");
        ImageButton searchImageButton = findViewById(R.id.ImageButton1);
        searchImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String task = searchBarText.getText().toString();

                String sqltask = "select * from book where (Title like \"%" +task+ "%\" or Author like \"%" +task+ "%\" or Year like \"%" +task+ "%\" or Genre like \"%" +task+ "%\") ";
                //select * from book where (Author like "%task%" or Year like "%task%" or Genre like "%task%")
                DBselect(sqltask);
            }
        });

        Button buttonheart = (Button) findViewById(R.id.button2) ;
        buttonheart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText searchBarText = findViewById(R.id.editText1);
                searchBarText.setText("");
                searchBarText.clearFocus();
                String sqltask = "select * from book join favoriteBooks on book.BookId=favoriteBooks.BookId";
                DBselect(sqltask);
            }
        });

        Button buttonhome = (Button) findViewById(R.id.button3) ;
        buttonhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText searchBarText = findViewById(R.id.editText1);
                searchBarText.setText("");
                searchBarText.clearFocus();
                String sqltask = "select * from book";
                DBselect(sqltask);
            }
        });


        Button menu4 = findViewById(R.id.button4);

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                //popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Тема");
                popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "Жанры");
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case 0:
                                ThemeUtils.toggleTheme(MainActivity.this);
                                recreate(); // Пересоздаем активность для применения новой темы

                                break;
                            case 1:
                                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "LitRPG");
                                popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "Боевое фэнтези");
                                popup.getMenu().add(Menu.NONE, 2, Menu.NONE, "Борьба за выживание");
                                popup.getMenu().add(Menu.NONE, 3, Menu.NONE, "Виртуальная реальность");
                                popup.getMenu().add(Menu.NONE, 4, Menu.NONE, "Героическое фэнтези");
                                popup.getMenu().add(Menu.NONE, 5, Menu.NONE, "Роман");
                                popup.getMenu().add(Menu.NONE, 6, Menu.NONE, "Боевая фантастика");
                                popup.getMenu().add(Menu.NONE, 7, Menu.NONE, "Бояръ-Аниме");
                                popup.getMenu().add(Menu.NONE, 8, Menu.NONE, "Фэнтези");
                                popup.show();
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        showToast(menuItem.getTitle().toString());
                                        DBselect("select * from book where (Genre like \"%" +menuItem.getTitle()+ "%\") ");
                                        return true;
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });
            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuThemes) {
            ThemeUtils.toggleTheme(MainActivity.this);
            recreate(); // Пересоздаем активность для применения новой темы
            return true;
        } else if (id == R.id.LitRPG) {
            showToast("LitRPG");
            return true;
        } else if (id == R.id.BattleFantasy) {
            showToast("Боевое фэнтези");
            return true;
        } else if (id == R.id.Survival) {
            showToast("Борьба за выживание");
            return true;
        } else if (id == R.id.VR) {
            showToast("Виртуальная реальность");
            return true;
        } else if (id == R.id.HeroicFantasy) {
            showToast("Героическое фэнтези");
            return true;
        } else if (id == R.id.Romance) {
            showToast("Роман");
            return true;
        } else if (id == R.id.BattleFiction) {
            showToast("Боевая фантастика");
            return true;
        } else if (id == R.id.Anime) {
            showToast("Бояръ-Аниме");
            return true;
        } else if (id == R.id.Fantasy) {
            showToast("Фэнтези");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }







    private void DBselect(String sql) {
        LinearLayout linearLayoutTest = (LinearLayout) findViewById(R.id.test1);
        linearLayoutTest.removeAllViews();
        userCursor = db.rawQuery(sql, null);
        if ((userCursor!= null) && (userCursor.getCount() > 0) ) {
            userCursor.moveToFirst();

            do { //крч этот цикл создает готовые окошки книг с картинкой и текстом

                android.widget.ImageButton button = new android.widget.ImageButton(new ContextThemeWrapper(this.getBaseContext(), R.style.BookButton), null, 0);
                LinearLayout linearLayout = new LinearLayout(new ContextThemeWrapper(MainActivity.this, R.style.BookLayoutText));
                TextView textView = new TextView(new ContextThemeWrapper(this.getBaseContext(), R.style.BookText));
                TextView textViewInfo = new TextView(new ContextThemeWrapper(this.getBaseContext(), R.style.BookTextInfo));
                LinearLayout linearLayoutFull = new LinearLayout(new ContextThemeWrapper(MainActivity.this, R.style.BookLayout));
                linearLayoutFull.setOrientation(LinearLayout.HORIZONTAL);


                textView.setText(userCursor.getString(1));
                String info = userCursor.getString(3) + "\n" + userCursor.getString(4) + "\n" + userCursor.getString(5);
                textViewInfo.setText(info);

                String filename = userCursor.getString(2);
                int resID = getResources().getIdentifier(filename, "drawable", getPackageName());
                button.setImageResource(resID);
                button.setId(userCursor.getInt(0));
                int width = 175;
                int height = 284;
                int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
                int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(w, h));

                button.setLayoutParams(new ViewGroup.LayoutParams(w, h));
                //linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, Book_content.class);
                        intent.putExtra("id", button.getId());
                        startActivity(intent);
                        finish(); //закрытие старого окна
                    }
                });


                linearLayout.addView(textView);
                linearLayout.addView(textViewInfo);
                linearLayoutFull.addView(button);
                linearLayoutFull.addView(linearLayout);
                linearLayoutTest.addView(linearLayoutFull);
            } while (userCursor.moveToNext());
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ничего не найдено!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();

    }

}
