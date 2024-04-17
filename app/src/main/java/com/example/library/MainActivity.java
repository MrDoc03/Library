package com.example.library;

import static com.example.library.R.menu.genres;
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
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.my_menu, popup.getMenu());

                popup.show();
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuThemes) {
            ThemeUtils.toggleTheme(MainActivity.this);
            recreate(); // Пересоздаем активность для применения новой темы
            return true;
        }
        else if (item.getItemId() == R.id.menuGenres) {
            getMenuInflater().inflate(R.menu.genres, item.getSubMenu());
            return true;
        } else
        {

        }

        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(my_menu, menu);
        return true;
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
