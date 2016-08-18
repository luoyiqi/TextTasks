package de.lenidh.texttasks.android.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/*public class TodoTxtParserTest {

    @Test
    public void parse_completedTask() {
        String str = "x 2000-02-20 2000-01-20 Buy milk @shop +food pri:C";

        TodoTxtParser parser = new TodoTxtParser();
        Task task = parser.parse(str);

        assertEquals(new LocalDate(2000, 02, 20), task.getCompletionDate());
        assertEquals(new LocalDate(2000, 01, 20), task.getCreationDate());
        assertEquals(TaskPriority.C, task.getPriority());
        assertEquals("Buy milk @shop +food", task.getText());
    }

    @Test
    public void parse_openTask() {
        String str = "(X) 2000-01-20 Buy milk @shop +food";

        TodoTxtParser parser = new TodoTxtParser();
        Task task = parser.parse(str);

        assertNull(task.getCompletionDate());
        assertEquals(new LocalDate(2000, 01, 20), task.getCreationDate());
        assertEquals(TaskPriority.X, task.getPriority());
        assertEquals("Buy milk @shop +food", task.getText());
    }
}*/
