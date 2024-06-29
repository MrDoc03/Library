package com.example.library;


import static android.content.ContentValues.TAG;
import static com.example.library.R.menu.my_menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.bumptech.glide.Glide;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.io.Console;

public class MainActivity extends AppCompatActivity {

    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;
    // Идентификатор канала
    private static String CHANNEL_ID = "Cat channel";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this); // Передайте контекст вашей активности или другого контекста
        // Загрузка пользователей
        new FetchUsersDataTask(databaseHelper).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        // Загрузка книг
        new FetchBooksDataTask(databaseHelper, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        // Загрузка истории чтения
        new FetchReadingHistoriesTask(databaseHelper).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        // Загрузка избранных книг
        new FetchFavoriteBooksTask(databaseHelper).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        db = databaseHelper.getReadableDatabase();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        LinearLayout BookList = (LinearLayout) findViewById(R.id.BookList);
        BookList.removeAllViews();

        String sqltask = "select * from books";
        DBselect(sqltask);

        EditText searchBarText = findViewById(R.id.editText1);
        searchBarText.setText("");
        ImageButton searchImageButton = findViewById(R.id.ImageButton1);
        searchImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String task = searchBarText.getText().toString();

                String sqltask = "select * from books where (Title like \"%" +task+ "%\" or Author like \"%" +task+ "%\" or Year like \"%" +task+ "%\" or Genre like \"%" +task+ "%\") ";
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
                // Получаем экземпляр синглтона UserManager
                UserManager userManager = UserManager.getInstance();

                // Получаем значение почты
                String email = userManager.getEmail();
                String sqltask = "select * from books join favoriteBooks on books.BookId=favoritebooks.BookId WHERE favoritebooks.Email = " + "\"" +email + "\"";
                Log.d(TAG, sqltask);
                DBselect(sqltask);
            }
        });

        Button buttonhome = (Button) findViewById(R.id.button3) ;
        buttonhome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText searchBarText = findViewById(R.id.editText1);
                searchBarText.setText("");
                searchBarText.clearFocus();
                String sqltask = "select * from books";
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
                                        DBselect("select * from books where (Genre like \"%" +menuItem.getTitle()+ "%\") ");
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

    public void DBselect(String sql) {
        LinearLayout BookList = findViewById(R.id.BookList); // Получаем ссылку на ваш LinearLayout
        BookList.removeAllViews();

        // Проверяем, что база данных инициализирована
        if (databaseHelper == null) {
            initDatabase();
        } else{
            db = databaseHelper.getReadableDatabase();
        }
        // Выполняем запрос и получаем курсор

        userCursor = db.rawQuery(sql, null);
        if ((userCursor!= null) && (userCursor.getCount() > 0) ) {
            userCursor.moveToFirst();

            do { //крч этот цикл создает готовые окошки книг с картинкой и текстом
                LayoutInflater inflater = LayoutInflater.from(this);
                View book_example = inflater.inflate(R.layout.book_example, BookList, false);
                TextView Title = book_example.findViewById(R.id.Title);
                Title.setText(userCursor.getString(1));

                TextView Author = book_example.findViewById(R.id.Author);
                Author.setText(userCursor.getString(3));

                TextView Type = book_example.findViewById(R.id.Type);
                Type.setText(userCursor.getString(5));

                TextView Deadline = book_example.findViewById(R.id.Deadline);
                Deadline.setText(userCursor.getString(4));

                LinearLayout BookLayout = book_example.findViewById(R.id.BookLayout);
                BookLayout.setId(Integer.parseInt(userCursor.getString(0)));

                Button gear_button = book_example.findViewById(R.id.gear_button);
                gear_button.setId(Integer.parseInt(userCursor.getString(0)));

                gear_button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, BookCreateOrUpdate.class);
                        intent.putExtra("id", BookLayout.getId());
                        startActivity(intent);
                        finish(); //закрытие старого окна
                    }
                });
                BookList.addView(book_example);
                Log.d(TAG, userCursor.getString(1));
            } while (userCursor.moveToNext());
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ничего не найдено!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }

    private void CreateBooks(){

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();

    }

}
