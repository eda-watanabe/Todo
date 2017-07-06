package jp.inc.eda.todo_master.presentation.create;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.inc.eda.todo_master.R;
import jp.inc.eda.todo_master.model.OrmaDatabase;
import jp.inc.eda.todo_master.model.Todo;

public class CreateTodoActivity extends AppCompatActivity {


    private OrmaDatabase orma;

    private EditText editTitle;
    private EditText editDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        init();
    }

    private void init() {
        orma = OrmaDatabase.builder(this).build();

        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);

        findViewById(R.id.buttonSave).setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (editTitle == null || editDescription == null) {
                return;
            }
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            if (title.isEmpty()) {
                Toast.makeText(editTitle.getContext(), "タイトルを入力してください", Toast.LENGTH_SHORT).show();
                return;
            }
            Todo todo = new Todo();
            todo.title = title;
            todo.description = description;
            todo.createAt = System.currentTimeMillis();
            Todo.save(orma, todo);

            Toast.makeText(editTitle.getContext(), "作成しました", Toast.LENGTH_SHORT).show();

            finish();
        }
    };
}
