package de.lenidh.texttasks.android.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import de.lenidh.texttasks.android.R;
import de.lenidh.texttasks.android.Utils;
import de.lenidh.texttasks.android.core.Task;
import de.lenidh.texttasks.android.core.TaskMapper;

public class TaskActivity extends AppCompatActivity {

    public static final String ARG_TASK_INDEX = "task_index";

    private TaskMapper taskMapper;
    private int index;
    private int initialHash; // change detection

    private Fragment editorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.taskMapper = new TaskMapper(Utils.getTodoTxtFile(this));

        this.index = getIntent().getIntExtra(ARG_TASK_INDEX, -1);
        if(this.index >= 0) {
            Task task = this.taskMapper.list().get(index);
            this.initialHash = task.hashCode();
            updateContent(task);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        Task task = getEditorValue();
        if(task != null && this.initialHash != task.hashCode()) {
            this.taskMapper.update(this.index, task);
        }
        super.onPause();
    }

    private void updateContent(Task task) {
        if(this.index < 0) return;

        this.editorFragment = PlainTextTaskFragment.newInstance(task.getTaskString());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.fragment_container, this.editorFragment);
        t.commit();
    }

    private Task getEditorValue() {
        Task task = null;
        if(this.editorFragment instanceof PlainTextTaskFragment) {
            task = ((PlainTextTaskFragment)this.editorFragment).getTask();
        }
        return task;
    }
}
