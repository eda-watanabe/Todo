package jp.inc.eda.todo_master.model;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


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
    public static Observable<Long> save(final OrmaDatabase ormaDatabase, final Todo todo) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> e) throws Exception {
                e.onNext(ormaDatabase.insertIntoTodo(todo));
                e.onComplete();
            }
        });
    }

    /**
     * Todoを更新する
     * @param ormaDatabase
     * @param todo
     * @return
     */
    public static Observable<Integer> update(final OrmaDatabase ormaDatabase, final Todo todo) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(ormaDatabase.updateTodo()
                        .idEq(todo.id)
                        .title(todo.title)
                        .description(todo.description)
                        .updateAt(System.currentTimeMillis())
                        .execute());
                e.onComplete();
            }
        });
    }

    /**
     * Todoを削除する
     * @param ormaDatabase
     * @param id
     */
    public static Observable<Integer> delete(final OrmaDatabase ormaDatabase, final long id) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(ormaDatabase.deleteFromTodo().idEq(id).execute());
                e.onComplete();
            }
        });
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
