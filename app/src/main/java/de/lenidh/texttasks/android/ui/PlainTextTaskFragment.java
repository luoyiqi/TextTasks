package de.lenidh.texttasks.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import de.lenidh.texttasks.android.R;
import de.lenidh.texttasks.android.core.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlainTextTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlainTextTaskFragment extends Fragment {

    public static final String ARG_TASK_STRING = "task_string";

    private EditText editor;

    public PlainTextTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param task task string.
     * @return A new instance of fragment PlainTextTaskFragment.
     */
    public static PlainTextTaskFragment newInstance(String task) {
        PlainTextTaskFragment fragment = new PlainTextTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_STRING, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plain_text_task, container, false);
        this.editor = (EditText) v.findViewById(R.id.editor);
        if(getArguments() != null) {
            this.editor.setText(getArguments().getString(ARG_TASK_STRING));
        }
        return v;
    }

    public Task getTask() {
        return new Task(this.editor.getText().toString());
    }
}
