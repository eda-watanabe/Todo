package jp.inc.eda.todo_master.presentation.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import jp.inc.eda.todo_master.R;
import jp.inc.eda.todo_master.model.OrmaDatabase;
import jp.inc.eda.todo_master.model.Todo;
import jp.inc.eda.todo_master.presentation.create.CreateTodoActivity;
import jp.inc.eda.todo_master.presentation.show.ShowTodoActivity;

public class TodoListActivity extends AppCompatActivity {

    private ListView todoList;

    private OrmaDatabase orma;
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        init();
        if (todoList != null) {
            settingTodoList(todoList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Todo> todos = Todo.findAll(orma);
        todoAdapter = new TodoAdapter(this, R.layout.list_item_todo, todos);
        todoList.setAdapter(todoAdapter);
    }

    private void init() {
        orma = OrmaDatabase.builder(this).build();
        // リスト
        todoList = (ListView) findViewById(R.id.todoList);
        // 新規作成ボタン
        findViewById(R.id.createButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // : 2017/07/06 新規作成画面へ遷移
                startActivity(new Intent(TodoListActivity.this, CreateTodoActivity.class));
            }
        });
    }

    private void settingTodoList(ListView listView) {
        listView.setOnItemClickListener(listener);
    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Todo todo = todoAdapter.getItem(position);
            // : 2017/07/06 詳細画面へ遷移
            startActivity(ShowTodoActivity.newIntent(TodoListActivity.this, todo.id));
        }
    };
}
