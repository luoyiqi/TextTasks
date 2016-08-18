package de.lenidh.texttasks.android.core;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

class TaskSerializer {

    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    private TaskSerializer() {
    }

    public static String serialize(TaskProperties props) {
        StringBuilder rawBuilder = new StringBuilder();
        if(props.completed) {
            rawBuilder.append("x ");
            if(props.completion != null) {
                rawBuilder.append(props.completion.toString(DATE_FORMATTER));
                rawBuilder.append(" ");
            }
        }
        if(!props.completed && props.priority != null) {
            rawBuilder.append('(');
            rawBuilder.append(props.priority.name());
            rawBuilder.append(") ");
        }
        if(props.creation != null) {
            rawBuilder.append(props.creation.toString(DATE_FORMATTER));
            rawBuilder.append(" ");
        }
        rawBuilder.append(props.text);
        if(props.due != null) {
            if(!"".equals(props.text)) {
                rawBuilder.append(' ');
            }
            rawBuilder.append("due:");
            rawBuilder.append(props.due.toString(DATE_FORMATTER));
        }
        if(props.completed && props.priority != null) {
            if(!"".equals(props.text)) {
                rawBuilder.append(' ');
            }
            rawBuilder.append("pri:");
            rawBuilder.append(props.priority.name());
        }
        return rawBuilder.toString();
    }
}
