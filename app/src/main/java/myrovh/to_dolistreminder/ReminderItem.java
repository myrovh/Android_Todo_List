package myrovh.to_dolistreminder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

public class ReminderItem extends AbstractItem<ReminderItem, ReminderItem.ViewHolder> {
    public String title;
    public String description;
    public String dueDate;

    public ReminderItem(String title, String description, String dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.fastadapter_reminderitem_id;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.todo_row_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder);

        viewHolder.titleView.setText(title);
        viewHolder.descriptionView.setText(description);
        viewHolder.dueDateView.setText(dueDate);
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView titleView;
        protected TextView descriptionView;
        protected TextView dueDateView;

        public ViewHolder(View view) {
            super(view);
            this.titleView = (TextView) view.findViewById(R.id.titleView);
            this.descriptionView = (TextView) view.findViewById(R.id.descriptionView);
            this.dueDateView = (TextView) view.findViewById(R.id.dateView);

            /*
             * Uncomment this block to enforce single line text for reminders in the list.
             *
            titleView.setSingleLine();
            titleView.setEllipsize(TextUtils.TruncateAt.END);
            descriptionView.setSingleLine();
            descriptionView.setEllipsize(TextUtils.TruncateAt.END);
            dueDateView.setSingleLine();
            */
        }
    }
}
