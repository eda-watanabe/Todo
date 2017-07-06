package jp.inc.eda.todo_master.presentation.show;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import jp.inc.eda.todo_master.R;
import jp.inc.eda.todo_master.model.OrmaDatabase;
import jp.inc.eda.todo_master.model.Todo;

public class ShowTodoActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    private OrmaDatabase ormaDatabase;

    private TextView textTitle;
    private TextView textDescription;
    private TextView textDate;

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
        if (getIntent().hasExtra(EXTRA_ID)) {
            long id = getIntent().getLongExtra(EXTRA_ID, 0);
            Todo todo = Todo.find(ormaDatabase, id);
            showTodo(todo);
        } else {
            Toast.makeText(this, "不正なアクセスです", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        ormaDatabase = OrmaDatabase.builder(this).build();

        textTitle = (TextView) findViewById(R.id.textTitle);
        textDescription = (TextView) findViewById(R.id.textDescription);
        textDate = (TextView) findViewById(R.id.textDate);
    }

    private void showTodo(Todo todo) {
        textTitle.setText(todo.title);
        textDate.setText(todo.getFormatCreateAt());
        textDescription.setText(todo.description);
    }
}
