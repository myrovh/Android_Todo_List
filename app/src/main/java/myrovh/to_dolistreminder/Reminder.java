package myrovh.to_dolistreminder;

import java.util.Calendar;

public class Reminder {
    String title;
    String description;
    Calendar dueDate;
    boolean isComplete;

    public Reminder() {
        this.title = "Default";
        this.description = "Default Todo";
        this.dueDate = Calendar.getInstance();
        isComplete = false;
    }

    public Reminder(String title, String description, Calendar dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        isComplete = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
