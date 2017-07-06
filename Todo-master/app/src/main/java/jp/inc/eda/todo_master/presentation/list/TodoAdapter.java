package jp.inc.eda.todo_master.presentation.list;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.inc.eda.todo_master.R;
import jp.inc.eda.todo_master.model.Todo;

/**
 * Created by watanabe on 2017/07/06.
 */

public class TodoAdapter extends ArrayAdapter<Todo> {

    private int layoutRes;

    public TodoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Todo> objects) {
        super(context, resource, objects);
        this.layoutRes = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutRes, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Todo item = getItem(position);
        viewHolder.bind(item);
        return convertView;
    }

    class ViewHolder {

        TextView title;
        TextView description;
        TextView date;

        ViewHolder(View v) {
            this.title = (TextView) v.findViewById(R.id.title);
            this.description = (TextView) v.findViewById(R.id.description);
            this.date = (TextView) v.findViewById(R.id.date);
        }

        void bind(Todo todo) {
            this.title.setText(todo.title);
            if (todo.description == null || todo.description.isEmpty()) {
                this.description.setText("なし");
            } else {
                this.description.setText(todo.description);
            }
            this.date.setText(todo.getFormatCreateAt());
        }
    }
}
