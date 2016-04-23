package myrovh.to_dolistreminder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private ArrayList<Reminder> data = new ArrayList<>();

    // Constructor for data
    public TodoAdapter(ArrayList<Reminder> todoDataList) {
        data = todoDataList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.titleView.setText(data.get(position).getTitle());
        holder.descriptionView.setText(data.get(position).getDescription());
        GregorianCalendar calendar = (GregorianCalendar) data.get(position).getDueDate();
        String dateString = calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        holder.dateView.setText(dateString);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //Define ViewHolder for Adapter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView descriptionView;
        private final TextView dateView;

        public ViewHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.titleView);
            descriptionView = (TextView) v.findViewById(R.id.descriptionView);
            dateView = (TextView) v.findViewById(R.id.dateView);
        }
    }
}
