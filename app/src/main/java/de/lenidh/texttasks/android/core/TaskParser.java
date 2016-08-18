package de.lenidh.texttasks.android.core;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TaskParser {

    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    private TaskParser() {
    }

    public static TaskProperties parse(CharSequence plainText) {
        StringBuilder input = new StringBuilder(plainText);
        TaskProperties props = new TaskProperties();
        completed(input, props);
        return props;
    }

    private static void completed(StringBuilder input, TaskProperties props) {
        if(input.charAt(0) == 'x' && input.charAt(1) == ' ') {
            props.completed = true;
            input.delete(0, 2);

            completed_completion(input, props);
        } else {
            priority(input, props);
        }
    }

    private static void completed_completion(StringBuilder input, TaskProperties props) {
        if(Pattern.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2} .*$", input)) {
            props.completion = tryParseDate(input.substring(0, 10));
            if(props.completion != null) input.delete(0, 11);
        }
        completed_creation(input, props);
    }

    private static void completed_creation(StringBuilder input, TaskProperties props) {
        if(Pattern.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2} .*$", input)) {
            props.creation = tryParseDate(input.substring(0, 10));
            if(props.creation != null) input.delete(0, 11);
        }
        completed_priority(input, props);
    }

    private static void completed_priority(StringBuilder input, TaskProperties props) {
        Pattern p = Pattern.compile("^.*( pri:([A-Z]))(?: .*)?$");
        Matcher m = p.matcher(input);
        if(m.matches()) {
            props.priority = TaskPriority.valueOf(m.group(2));
            input.delete(m.start(1), m.end(1));
        }

        due(input, props);
    }

    private static void priority(StringBuilder input, TaskProperties props) {
        if(Pattern.matches("^\\([A-Z]\\) .*$", input)) {
            props.priority = TaskPriority.valueOf(input.substring(1, 2));
            input.delete(0, 4);
        }
        creation(input, props);
    }

    private static void creation(StringBuilder input, TaskProperties props) {
        if(Pattern.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2} .*$", input)) {
            props.creation = tryParseDate(input.substring(0, 10));
            if(props.creation != null) input.delete(0, 11);
        }
        due(input, props);
    }

    private static void due(StringBuilder input, TaskProperties props) {
        Pattern p = Pattern.compile("^.*( due:([0-9]{4}-[0-9]{2}-[0-9]{2}))(?: .*)?$");
        Matcher m = p.matcher(input);
        if(m.matches()) {
            props.due = tryParseDate(m.group(2));
            if(props.due != null) input.delete(m.start(1), m.end(1));
        }

        text(input, props);
    }

    private static void text(StringBuilder input, TaskProperties props) {
        props.text = input.toString();
    }

    private static LocalDate tryParseDate(String input) {
        LocalDate date = null;
        try {
            date = DATE_FORMATTER.parseLocalDate(input);
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
            // continue
        }
        return date;
    }
}
