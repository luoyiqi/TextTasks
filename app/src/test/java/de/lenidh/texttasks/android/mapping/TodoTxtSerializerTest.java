package de.lenidh.texttasks.android.mapping;

/*public class TodoTxtSerializerTest {

    @Test
    public void serialize_allAttributes() {
        Task task = new Task(new LocalDate(2000, 2, 20), new LocalDate(2000, 1, 20), TaskPriority.A, "Buy milk @shop +food");

        TodoTxtSerializer serializer = new TodoTxtSerializer();
        String str = serializer.serialize(task);

        assertEquals("x 2000-02-20 2000-01-20 Buy milk @shop +food pri:A", str);
    }

    @Test
    public void serialize_withoutCompletionDate() {
        Task task = new Task(null, new LocalDate(2000, 1, 20), TaskPriority.T, "Buy milk @shop +food");

        TodoTxtSerializer serializer = new TodoTxtSerializer();
        String str = serializer.serialize(task);

        assertEquals("(T) 2000-01-20 Buy milk @shop +food", str);
    }

    @Test
    public void serialize_withoutCompletionDateAndPriority() {
        Task task = new Task(null, new LocalDate(2000, 1, 20), null, "Buy milk @shop +food");

        TodoTxtSerializer serializer = new TodoTxtSerializer();
        String str = serializer.serialize(task);

        assertEquals("2000-01-20 Buy milk @shop +food", str);
    }

    @Test
    public void serialize_withoutPriority() {
        Task task = new Task(new LocalDate(2000, 2, 20), new LocalDate(2000, 1, 20), null, "Buy milk @shop +food");

        TodoTxtSerializer serializer = new TodoTxtSerializer();
        String str = serializer.serialize(task);

        assertEquals("x 2000-02-20 2000-01-20 Buy milk @shop +food", str);
    }

    @Test
    public void serialize_withoutText() {
        Task task = new Task(null, new LocalDate(2000, 1, 20), null, null);

        TodoTxtSerializer serializer = new TodoTxtSerializer();
        String str = serializer.serialize(task);

        assertEquals("2000-01-20 ", str);
    }
}*/
