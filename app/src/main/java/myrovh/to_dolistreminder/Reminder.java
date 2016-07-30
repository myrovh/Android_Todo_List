package myrovh.to_dolistreminder;

import org.parceler.Parcel;

import java.util.Calendar;

@Parcel
public class Reminder {
    int id;
    String title;
    String description;
    Calendar dueDate;
    boolean isComplete;

    Reminder() {
    }

    Reminder(int id, String title, String description, Calendar dueDate, boolean isComplete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isComplete = isComplete;
    }

    public int getId() {
        return id;
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

    Long getDueDateAsEpoc() {
        return dueDate.getTimeInMillis();
    }

    boolean isComplete() {
        return isComplete;
    }

    void setComplete(boolean complete) {
        isComplete = complete;
    }
}
