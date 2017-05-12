package myrovh.to_dolistreminder.ListModels;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import myrovh.to_dolistreminder.R;

public class HeaderItem extends AbstractItem<HeaderItem, HeaderItem.ViewHolder> {
    public String title;

    public HeaderItem(String title) {
        this.title = title;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_headeritem_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.header_row_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(holder, payloads);
        holder.titleView.setText(title);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView titleView;

        public ViewHolder(View view) {
            super(view);
            this.titleView = (TextView) view.findViewById(R.id.titleView);

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
