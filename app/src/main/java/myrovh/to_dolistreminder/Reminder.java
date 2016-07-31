package myrovh.to_dolistreminder;

import org.parceler.Parcel;

import java.util.Calendar;

@Parcel
public class Reminder {
    String title;
    String description;
    Calendar dueDate;
    boolean isComplete;

    Reminder() {
    }

    Reminder(String title, String description, Calendar dueDate, boolean isComplete) {
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

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    Calendar getDueDate() {
        return dueDate;
    }

    void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    boolean isComplete() {
        return isComplete;
    }

    void setComplete(boolean complete) {
        isComplete = complete;
    }
}
