package de.lenidh.texttasks.android.ui;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import de.lenidh.texttasks.android.R;
import de.lenidh.texttasks.android.core.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that provides views for a list of {@link Task}s.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private final List<Task> values = new ArrayList<>();
    private final TaskListAdapter.Listener listener;
    private final SparseBooleanArray selectedItems = new SparseBooleanArray();

    public TaskListAdapter(TaskListAdapter.Listener listener) {
        if(listener == null) throw new NullPointerException("listener must not be null");
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
        holder.view.setSelected(selectedItems.get(position));
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
    }

    private boolean toggleSelection(int position, boolean notify) {
        boolean newState;
        if(selectedItems.get(position)) {
            newState = false;
            selectedItems.delete(position);
        } else {
            newState = true;
            selectedItems.put(position, true);
        }
        if (notify) {
            notifyItemChanged(position);
            listener.onTaskSelectionChanged();
        }
        return newState;
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> result = new ArrayList<>(selectedItems.size());
        for(int i = 0; i < selectedItems.size(); i++) {
            result.add(selectedItems.keyAt(i));
        }
        return result;
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
            this.view.setOnLongClickListener(onLongClickListener);

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
                if(selectedItems.size() > 0) {
                    boolean selected = toggleSelection(getAdapterPosition(), false);
                    v.setSelected(selected);
                    listener.onTaskSelectionChanged();
                } else {
                    listener.onTaskClick(getAdapterPosition(), item);
                }
            }
        };

        private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean selected = toggleSelection(getAdapterPosition(), false);
                v.setSelected(selected);
                listener.onTaskSelectionChanged();
                return true;
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

    public interface Listener {
        void onTaskClick(int position, Task task);
        void onTaskCompleteAction(int position, Task task);
        void onTaskScheduleAction(int position, Task task);
        void onTaskSelectionChanged();
    }
}
