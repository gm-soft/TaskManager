package io.github.maximgorbatyuk.taskmanager.help;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.maximgorbatyuk.taskmanager.R;

/**
 * Created by Maxim on 09.04.2016.
 */
public class TaskAdapter extends ArrayAdapter<Task> {

    private List<Task> list;
    private Context context;

    public TaskAdapter(Context context, List<Task> list){
        super(context, R.layout.task_item, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View taskItem = inflater.inflate(R.layout.task_item, parent, false);

        TextView title      = (TextView) taskItem.findViewById(R.id.listItemTitle);
        TextView body       = (TextView) taskItem.findViewById(R.id.listItemBody);
        TextView deadline   = (TextView) taskItem.findViewById(R.id.listItemDeadline);
        Switch   done       = (Switch)   taskItem.findViewById(R.id.listItemDone);
        TextView id         = (TextView) taskItem.findViewById(R.id.listItemId);
        TextView createdAt  = (TextView) taskItem.findViewById(R.id.listItemCreatedAt);

        title.setText(      list.get(position).getTitle());
        body.setText(       list.get(position).getBody());
        deadline.setText(   DateToString( list.get(position).getDeadline()));
        done.setChecked(    list.get(position).getIsDone());
        id.setText(         "" + list.get(position).getId());
        createdAt.setText(  DateToString( list.get(position).getCreatedAt()));

        return taskItem;
        //return super.getView(position, convertView, parent);
    }

    @Nullable
    private String DateToString(Date date){
        try {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
            return format.format(date);

        } catch (Exception ex){
            return null;
        }
    }
}
