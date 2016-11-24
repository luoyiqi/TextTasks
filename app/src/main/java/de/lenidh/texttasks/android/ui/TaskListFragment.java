package de.lenidh.texttasks.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import de.lenidh.texttasks.android.R;
import de.lenidh.texttasks.android.core.Task;
import de.lenidh.texttasks.android.core.TaskListLoader;
import de.lenidh.texttasks.android.core.TaskMapper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Host}
 * interface.
 */
public class TaskListFragment extends Fragment {

    private static final int TASK_LIST_LOADER_ID = 0;

    private static final String ARG_FILE = "file";

    private final ActionModeCallbacks actionModeCallbacks = new ActionModeCallbacks();

    private EditText newTaskEditor;
    private ActionMode actionMode;

    private Host host;

    private TaskMapper file;
    private final TaskListAdapter adapter = new TaskListAdapter(new AdapterListener());

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskListFragment() {
    }

    public static TaskListFragment newInstance(File file) {
        if(file == null) throw new NullPointerException("file must not be null.");

        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILE, file);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File txtFile = null; // TODO: Handle missing file arg
        if (getArguments() != null) {
            txtFile = (File)getArguments().getSerializable(ARG_FILE);
            this.file = new TaskMapper(txtFile);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Set the adapter
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.list);
        Context context = view.getContext();
        listView.setLayoutManager(new LinearLayoutManager(context)); // show as list
        listView.setAdapter(this.adapter);

        this.newTaskEditor = (EditText) view.findViewById(R.id.create_task_input);
        this.newTaskEditor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER) && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    addTask();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(TASK_LIST_LOADER_ID, null, new TaskListLoaderCallbacks());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Host) {
            host = (Host) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        host = null;
    }

    private void addTask() {
        String taskStr = this.newTaskEditor.getText().toString();
        if(!"".equals(taskStr)) {
            Task newTask = new Task(taskStr);
            this.file.insert(0, newTask);
            this.newTaskEditor.setText("");
        }
    }

    private void deleteSelectedTasks() {
        List<Integer> selectedTasks = adapter.getSelectedItems();
        for(int i = selectedTasks.size() - 1; i >= 0; i--) {
            file.delete(selectedTasks.get(i));
        }
        adapter.clearSelections();
        actionMode.finish();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Host {
        void onTaskClick(int position, Task item);
        ActionMode startActionMode(ActionMode.Callback callback);
    }

    private class TaskListLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Task>> {

        @Override
        public Loader<List<Task>> onCreateLoader(int id, Bundle args) {
            return new TaskListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<Task>> loader, final List<Task> data) {
            adapter.updateData(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Task>> loader) {
            adapter.updateData(null);
        }
    }

    private class AdapterListener implements TaskListAdapter.Listener {

        @Override
        public void onTaskClick(int position, Task task) {
            host.onTaskClick(position, task);
        }

        @Override
        public void onTaskCompleteAction(int position, Task task) {
            task.complete();
            adapter.notifyItemChanged(position);
            file.update(position, task);
        }

        @Override
        public void onTaskScheduleAction(int position, Task task) {
            task.schedule();
            adapter.notifyItemChanged(position);
            file.update(position, task);
        }

        @Override
        public void onTaskSelectionChanged() {
            int selectedItemCount = adapter.getSelectedItemCount();
            if(selectedItemCount > 0 && actionMode == null) {
                actionMode = host.startActionMode(actionModeCallbacks);
            } else if(selectedItemCount == 0 && actionMode != null) {
                actionMode.finish();
            }
        }
    }

    private class ActionModeCallbacks implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.task_context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteSelectedTasks();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode = null;
        }
    }
}
