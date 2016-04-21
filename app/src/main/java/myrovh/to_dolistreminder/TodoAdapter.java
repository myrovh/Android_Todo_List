package myrovh.to_dolistreminder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private Reminder[] todoList;

    // Constructor for data
    public TodoAdapter(Reminder[] todoDataList) {
        todoList = todoDataList;
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
        holder.titleView.setText(todoList[position].getTitle());
        holder.descriptionView.setText(todoList[position].getDescription());
        holder.dateView.setText(todoList[position].getDueDate().toString());

    }

    @Override
    public int getItemCount() {
        return todoList.length;
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

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getDescriptionView() {
            return descriptionView;
        }

        public TextView getDateView() {
            return dateView;
        }
    }
}
