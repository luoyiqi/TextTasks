package de.lenidh.texttasks.android.ui;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import de.lenidh.texttasks.android.R;
import de.lenidh.texttasks.android.core.Task;
import de.lenidh.texttasks.android.ui.TaskListFragment.OnInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task} and makes a call to the specified
 * {@link OnInteractionListener}.
 */
public class TaskListItemAdapter extends RecyclerView.Adapter<TaskListItemAdapter.ViewHolder> {

    private final List<Task> values = new ArrayList<>();
    private final TaskListFragment listener;

    public TaskListItemAdapter(TaskListFragment listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.item = values.get(position);
        holder.textView.setText(holder.item.getText());
        if(holder.item.getDueDate() == null) {
            holder.dueView.setText("");
            holder.dueView.setVisibility(View.GONE);
        } else {
            holder.dueView.setText(holder.item.getDueDate().toString("EE, d MMM yyyy"));
            holder.dueView.setVisibility(View.VISIBLE);
        }
        holder.safe_setChecked(holder.item.isCompleted());
        int paintFlags = holder.textView.getPaintFlags();
        if(holder.item.isCompleted()) {
            paintFlags |= Paint.STRIKE_THRU_TEXT_FLAG;
        } else {
            paintFlags &= ~Paint.STRIKE_THRU_TEXT_FLAG;
        }
        holder.textView.setPaintFlags(paintFlags);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void updateData(List<Task> tasks) {
        this.values.clear();
        this.values.addAll(tasks);
        this.notifyDataSetChanged();
        // FIXME:
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView textView;
        public final TextView dueView;
        public final CheckBox completedView;
        public Task item;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            this.view.setOnClickListener(onClickListener);

            this.textView = (TextView) view.findViewById(R.id.text);

            this.dueView = (TextView) view.findViewById(R.id.due_date);

            this.completedView = (CheckBox) view.findViewById(R.id.completed);
            this.completedView.setOnCheckedChangeListener(onCheckedChangeListener);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + this.textView.getText() + "'";
        }

        // set checked without event
        private void safe_setChecked(boolean checked) {
            this.completedView.setOnCheckedChangeListener(null);
            this.completedView.setChecked(checked);
            this.completedView.setOnCheckedChangeListener(onCheckedChangeListener);
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onTaskClick(getAdapterPosition(), item);
                }
            }
        };

        private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    listener.onTaskCompleteAction(getAdapterPosition(), item);
                } else {
                    listener.onTaskScheduleAction(getAdapterPosition(), item);
                }
            }
        };
    }
}
