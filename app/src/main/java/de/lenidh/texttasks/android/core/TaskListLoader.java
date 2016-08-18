package de.lenidh.texttasks.android.core;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.File;
import java.util.List;

import de.lenidh.texttasks.android.Utils;

public class TaskListLoader extends AsyncTaskLoader<List<Task>> {

    private static final String TAG = "TaskListLoader";

    private static final int POLLING_INTERVAL = 100;

    private List<Task> cache;
    private DataSourceObserver observer;

    public TaskListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if(this.cache != null) {
            deliverResult(this.cache);
        }

        if(this.observer == null) {
            File file = Utils.getTodoTxtFile(getContext());
            this.observer = new DataSourceObserver(file);
        }
        this.observer.startWatching();

        if(takeContentChanged() || this.cache == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        if(this.observer != null) {
            this.observer.stopWatching();
        }
    }

    @Override
    protected void onReset() {
        if(this.observer != null) {
            this.observer.stopWatching();
            this.observer = null;
        }
    }

    @Override
    public List<Task> loadInBackground() {
        File file = Utils.getTodoTxtFile(getContext());
        TaskMapper mapper = new TaskMapper(file);
        return mapper.list();
    }

    @Override
    public void deliverResult(List<Task> data) {
        this.cache = data;
        super.deliverResult(data);
    }

    private class DataSourceObserver extends FilePathObserver {

        public DataSourceObserver(File pathname) {
            super(pathname, POLLING_INTERVAL);
        }

        @Override
        public void onChange() {
            Log.i(TAG, "Todo.txt file has changed. Notify listeners...");
            onContentChanged();
        }
    }
}
