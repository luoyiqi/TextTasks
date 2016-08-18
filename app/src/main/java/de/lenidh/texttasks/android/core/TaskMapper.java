package de.lenidh.texttasks.android.core;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TaskMapper {

    private final File file;

    public TaskMapper(File txtFile) {
        if(txtFile == null) throw new NullPointerException("txtFile must not be null.");
        this.file = new File(txtFile, "");
    }

    public void insert(final int position, final Task task) {
        access(new FileOperation() {
            @Override
            public void apply(List<String> lines) {
                lines.add(position, task.getTaskString());
            }
        });
    }

    public void update(final int position, final Task task) {
        access(new FileOperation() {
            @Override
            public void apply(List<String> lines) {
                lines.set(position, task.getTaskString());
            }
        });
    }

    public void delete(final int position) {
        access(new FileOperation() {
            @Override
            public void apply(List<String> lines) {
                lines.remove(position);
            }
        });
    }

    public List<Task> list() {
        final List<Task> tasks = new ArrayList<>();
        access(new FileOperation() {
            @Override
            public void apply(List<String> lines) {
                for(String line : lines) {
                    tasks.add(new Task(line));
                }
            }
        });
        return tasks;
    }

    private void access(FileOperation operation) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(this.file));
            List<String> taskLines = new LinkedList<>();
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.matches("^\\p{Blank}*$")) { // skip empty lines
                    taskLines.add(line);
                }
            }

            int hash = taskLines.hashCode();
            operation.apply(taskLines);

            // if one or more task lines were changed by the operation, update
            // the file.
            if(hash != taskLines.hashCode()) {
                writer = new BufferedWriter(new FileWriter(this.file));
                for(String taskLine : taskLines) {
                    writer.write(taskLine);
                    writer.write("\n");
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("TodoTxtTaskRepository", e.toString());
            // TODO:
        } catch (IOException e) {
            Log.e("TodoTxtTaskRepository", e.toString());
            // TODO:
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private interface FileOperation {
        void apply(List<String> lines);
    }
}
