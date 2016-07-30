package myrovh.to_dolistreminder;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private static OnItemClickListener listener;
    private ArrayList<Reminder> data = new ArrayList<>();

    // Constructor for data
    TodoAdapter(ArrayList<Reminder> todoDataList) {
        data = todoDataList;
        listener = null;
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
        if (data.get(position).isComplete()) {
            holder.titleView.setPaintFlags(holder.titleView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.descriptionView.setText(data.get(position).getDescription());
        GregorianCalendar calendar = (GregorianCalendar) data.get(position).getDueDate();
        String dateString = calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        holder.dateView.setText(dateString);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // Define the method that allows the parent activity or fragment to define the listener
    void setOnItemClickListener(OnItemClickListener inListener) {
        listener = inListener;
    }

    // Define the listener interface
    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    //Define ViewHolder for Adapter
    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView descriptionView;
        private final TextView dateView;

        ViewHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.titleView);
            descriptionView = (TextView) v.findViewById(R.id.descriptionView);
            dateView = (TextView) v.findViewById(R.id.dateView);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }
}
