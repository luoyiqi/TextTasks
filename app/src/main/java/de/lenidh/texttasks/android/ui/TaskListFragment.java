package de.lenidh.texttasks.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
 * Activities containing this fragment MUST implement the {@link OnInteractionListener}
 * interface.
 */
public class TaskListFragment extends Fragment {

    private static final int TASK_LIST_LOADER_ID = 0;

    private static final String ARG_FILE = "file";

    private EditText newTaskEditor;

    private OnInteractionListener listener;

    private TaskMapper file;
    private final TaskListItemAdapter adapter = new TaskListItemAdapter(this);

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
                    onAddTask();
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
        if (context instanceof OnInteractionListener) {
            listener = (OnInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void onTaskClick(int position, Task task) {
        this.listener.onTaskClick(position, task);
    }

    public void onAddTask() {
        String taskStr = this.newTaskEditor.getText().toString();
        if(!"".equals(taskStr)) {
            Task newTask = new Task(taskStr);
            this.file.insert(0, newTask);
            this.newTaskEditor.setText("");
        }
    }

    public void onTaskCompleteAction(int position, Task task) {
        task.complete();
        adapter.notifyItemChanged(position);
        this.file.update(position, task);
    }

    public void onTaskScheduleAction(int position, Task task) {
        task.schedule();
        adapter.notifyItemChanged(position);
        this.file.update(position, task);
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
    public interface OnInteractionListener {
        void onTaskClick(int position, Task item);
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
}
