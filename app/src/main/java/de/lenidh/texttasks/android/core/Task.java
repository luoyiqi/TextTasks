package de.lenidh.texttasks.android.core;

import org.joda.time.LocalDate;

public class Task {

    private String raw;
    private TaskProperties cache;

    public Task(String taskStr) {
        validateTaskStr(taskStr);
        this.raw = taskStr;
        refreshCache();
    }

    public Task(LocalDate completionDate, LocalDate creationDate, TaskPriority priority, String text) {
        this.cache.completion = completionDate;
        this.cache.creation = creationDate;
        this.cache.priority = priority;
        if(text == null) text = "";
        this.cache.text = text;
    }

    public boolean isCompleted() {
        return this.cache.completed;
    }

    public void complete() {
        if(!isCompleted()) {
            this.cache.completed = true;
            this.cache.completion = LocalDate.now();
            syncCache();
        }
    }

    public void schedule() {
        if(isCompleted()) {
            this.cache.completed = false;
            this.cache.completion = null;
            syncCache();
        }
    }

    public String getTaskString() {
        return this.raw;
    }

    public void setTaskString(String taskStr) {
        if(!this.raw.equals(taskStr)) {
            validateTaskStr(taskStr);
            this.raw = taskStr;
            refreshCache();
        }
    }

    public TaskPriority getPriority() {
        return this.cache.priority;
    }

    public void setPriority(TaskPriority priority) {
        if(this.cache.priority != priority) {
            this.cache.priority = priority;
            syncCache();
        }
    }

    public LocalDate getCompletionDate() {
        return this.cache.completion;
    }

    public LocalDate getCreationDate() {
        return this.cache.creation;
    }

    public String getText() {
        return this.cache.text;
    }

    public void setText(String text) {
        if(text == null) throw new NullPointerException("task text must not be null");
        if(!this.cache.text.equals(text)) {
            this.cache.text = text;
            syncCache();
        }
    }

    public LocalDate getDueDate() {
        return this.cache.due;
    }

    public void setDueDate(LocalDate dueDate) {
        this.cache.due = dueDate;
    }

    @Override
    public String toString() {
        return TaskSerializer.serialize(this.cache);
    }

    private void refreshCache() {
        this.cache = TaskParser.parse(this.raw);
    }

    private void syncCache() {
        this.raw = TaskSerializer.serialize(this.cache);
        refreshCache();
    }

    private static void validateTaskStr(String taskStr) {
        if(taskStr == null) {
            throw new NullPointerException("task string must not be null");
        }
        if("".equals(taskStr)) {
            throw new IllegalArgumentException("task string must not be empty");
        }
        if(taskStr.contains("\n") || taskStr.contains("\n")) {
            throw new IllegalArgumentException("task string must be single-line");
        }
        if(taskStr.matches("^\\p{Blank}+$")) {
            throw new IllegalArgumentException("task string must not be blank");
        }
    }

    @Override
    public int hashCode() {
        return this.raw.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        //noinspection SimplifiableIfStatement
        if(o == null || !(o instanceof Task)) return false;
        return this.raw.equals(((Task)o).raw);
    }
}
