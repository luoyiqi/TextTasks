package de.lenidh.texttasks.android.model;

import org.junit.Test;

import de.lenidh.texttasks.android.core.TaskPriority;

import static org.junit.Assert.*;

public class TaskPriorityTest {

    @Test
    public void values_allTodoTxtPrioritiesSupported() {
        for (char c = 'A'; c < 'Z'; c++) {
            TaskPriority.valueOf(String.valueOf(c));
        }
    }

    @Test
    public void values_onlyTodoTxtPriorities() {
        for(TaskPriority p : TaskPriority.values()) {
           String name = p.name();
            assertEquals(1, name.length());

            char c = name.charAt(0);
            assertTrue(c >= 'A' && c <= 'Z');
        }
    }

    @Test
    public void values_noDuplicates() {
        assertEquals(26, TaskPriority.values().length);
    }
}
