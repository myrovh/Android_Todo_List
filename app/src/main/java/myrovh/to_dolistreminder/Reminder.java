package myrovh.to_dolistreminder;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

import java.util.Calendar;

@Parcel
public class Reminder {
    int id;
    String title;
    String description;
    Calendar dueDate;
    boolean isComplete;
    Double latitude;
    Double longitude;

    Reminder() {
    }

    //If lat and long not set pass 'null' into the constructor for both values
    Reminder(int id, String title, String description, Calendar dueDate, boolean isComplete, Double latitude, Double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isComplete = isComplete;
        //Ensure null input always apply to both lat and long
        if(latitude == null || longitude == null) {
            this.latitude = null;
            this.longitude = null;
        }
        else {
            this.latitude = latitude;
            this.longitude = longitude;
        }
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

    LatLng getLocation() {
        if (latitude != null) {
            return new LatLng(latitude, longitude);
        } else {
            return null;
        }
    }

    void setLocation(LatLng location) {
        if (location != null) {
            this.latitude = location.latitude;
            this.longitude = location.longitude;
        } else {
            this.latitude = null;
            this.longitude = null;
        }
    }
}
