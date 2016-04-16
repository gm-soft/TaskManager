package io.github.maximgorbatyuk.taskmanager.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.maximgorbatyuk.taskmanager.R;

import static android.support.v4.content.ContextCompat.getColorStateList;


public class TaskAdapter extends ArrayAdapter<Project> {

    private List<Project> list;
    private Context context;

    public TaskAdapter(Context context, List<Project> list){
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
        TextView id         = (TextView) taskItem.findViewById(R.id.listItemId);
        TextView createdAt  = (TextView) taskItem.findViewById(R.id.listItemCreatedAt);
        TextView status     = (TextView) taskItem.findViewById(R.id.listItemDoneStatus);

        title.setText(      list.get(position).getTitle());
        body.setText(       list.get(position).getBody());
        String deadline_str = context.getString(R.string.no_deadline);
        if (list.get(position).getDeadline() != null)
            deadline_str = new DateHelper().DateToShortString( list.get(position).getDeadline());

        deadline.setText( deadline_str );
        //done.setChecked(    );
        id.setText(         String.valueOf( list.get(position).getId()));
        createdAt.setText(  new DateHelper().DateToShortString( list.get(position).getCreatedAt()));


        status.setTextColor(getColorStateList(context, list.get(position).getIsDone() ? R.color.finishedColor : R.color.uncomplitedColor));
        status.setText(list.get(position).getIsDone() ? R.string.context_done : R.string.in_process);
        return taskItem;
        //return super.getView(position, taskItem, parent);
    }




}
