package de.lenidh.texttasks.android.core;

import org.joda.time.LocalDate;

class TaskProperties {
    boolean completed;
    LocalDate completion;
    LocalDate creation;
    LocalDate due;
    TaskPriority priority;
    String text;
}
