package jp.inc.eda.todo_master.model;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by watanabe on 2017/07/06.
 */
@Table
public class Todo {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column(indexed = true)
    public String title;

    @Column
    @Nullable
    public String description;

    @Column
    public long createAt;

    @Column
    public long updateAt;

    /**
     * 保存されているすべてのTODOを取得する
     * @param ormaDatabase
     * @return
     */
    public static List<Todo> findAll(OrmaDatabase ormaDatabase) {
        return ormaDatabase.selectFromTodo().toList();
    }

    /**
     * TODOを保存する
     * @param ormaDatabase
     * @param todo
     */
    public static void save(final OrmaDatabase ormaDatabase, final Todo todo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ormaDatabase.insertIntoTodo(todo);
            }
        }).start();
    }

    /**
     * TODOをidから取得する
     * @param ormaDatabase
     * @param id
     * @return
     */
    public static Todo find(OrmaDatabase ormaDatabase, long id) {
        return ormaDatabase.selectFromTodo().idEq(id).get(0);
    }

    /**
     * 日付を文字列で取得する
     * @return
     */
    public String getFormatCreateAt() {
        Date d = new Date(createAt);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPAN);
        return df.format(d.getTime());
    }
}
