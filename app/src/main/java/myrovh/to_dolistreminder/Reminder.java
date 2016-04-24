package myrovh.to_dolistreminder;

import org.parceler.Parcel;

import java.util.Calendar;

@Parcel
public class Reminder {
    String title;
    String description;
    Calendar dueDate;
    boolean isComplete;

    public Reminder() {
    }

    public Reminder(String title, String description, Calendar dueDate, boolean isComplete) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isComplete = isComplete;
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
