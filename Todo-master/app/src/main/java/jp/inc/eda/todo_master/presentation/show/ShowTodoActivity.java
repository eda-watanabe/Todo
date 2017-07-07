package jp.inc.eda.todo_master.presentation.show;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.inc.eda.todo_master.R;
import jp.inc.eda.todo_master.model.OrmaDatabase;
import jp.inc.eda.todo_master.model.Todo;
import jp.inc.eda.todo_master.presentation.edit.EditTodoActivity;

public class ShowTodoActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    private OrmaDatabase ormaDatabase;

    private TextView textTitle;
    private TextView textDescription;
    private TextView textDate;

    private long id;

    public static Intent newIntent(Context context, long id) {
        Intent intent = new Intent(context, ShowTodoActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_todo);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra(EXTRA_ID)) {
            id = getIntent().getLongExtra(EXTRA_ID, 0);
            Todo todo = Todo.find(ormaDatabase, id);
            showTodo(todo);
        } else {
            Toast.makeText(this, "不正なアクセスです", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_show, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            showAlert();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        ormaDatabase = OrmaDatabase.builder(this).build();

        textTitle = (TextView) findViewById(R.id.textTitle);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textDate = (TextView) findViewById(R.id.textDate);

        findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EditTodoActivity.newIntent(ShowTodoActivity.this, id));
            }
        });
    }

    private void showTodo(Todo todo) {
        textTitle.setText(todo.title);
        textDate.setText(todo.getFormatCreateAt());
        textDescription.setText(todo.description);
    }

    private void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("削除してもよろしいですか")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Todo.delete(ormaDatabase, id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Integer>() {
                                    @Override
                                    public void accept(@NonNull Integer integer) throws Exception {
                                        finish();
                                    }
                                });
                    }
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }
}
