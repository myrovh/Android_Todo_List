package myrovh.to_dolistreminder;

import java.util.Date;

public class Reminder {
    String title;
    String description;
    Date dueDate;
    boolean isComplete;

    public Reminder() {
        this.title = "Default";
        this.description = "Default Todo";
        this.dueDate = new Date();
        isComplete = false;
    }

    public Reminder(String title, String description, Date dueDate) {
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
