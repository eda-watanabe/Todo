package jp.inc.eda.todo_master.presentation.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.inc.eda.todo_master.R;
import jp.inc.eda.todo_master.model.OrmaDatabase;
import jp.inc.eda.todo_master.model.Todo;

/**
 * Created by watanabe on 2017/07/07.
 */

public class EditTodoActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";

    private OrmaDatabase orma;

    private EditText editTitle;
    private EditText editDescription;

    private long id;

    public static Intent newIntent(Context context, long id) {
        Intent intent = new Intent(context, EditTodoActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        if (getIntent().hasExtra(EXTRA_ID)) {
            init();
            id = getIntent().getLongExtra(EXTRA_ID, 0);
            showTodo(Todo.find(orma, id));
        } else {
            Toast.makeText(this, "不正アクセスです", Toast.LENGTH_LONG).show();
        }
    }


    private void init() {
        orma = OrmaDatabase.builder(this).build();

        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);

        findViewById(R.id.buttonSave).setOnClickListener(listener);
    }

    private void showTodo(Todo todo) {
        editTitle.setText(todo.title);
        if (todo.description == null) {
            return;
        }
        editDescription.setText(todo.description);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // : 2017/07/07 バリデーションと更新
            if (editTitle == null || editDescription == null) {
                return;
            }
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(editTitle.getContext(), "タイトルを入力してください", Toast.LENGTH_SHORT).show();
                return;
            }
            Todo todo = Todo.find(orma, id);
            todo.title = title;
            todo.description = description;
            Todo.update(orma, todo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer aLong) throws Exception {
                            Toast.makeText(EditTodoActivity.this, "更新しました", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
        }
    };
}
