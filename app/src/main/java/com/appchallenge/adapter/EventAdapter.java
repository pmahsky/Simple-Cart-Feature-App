package com.appchallenge.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.appchallenge.R;
import com.appchallenge.activities.CartActivity;
import com.appchallenge.activities.MainActivity;
import com.appchallenge.model.Event;
import com.appchallenge.util.CartDatabaseHandler;
import com.appchallenge.util.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by Prashant on 05/04/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {


    private final ArrayList<Event> eventsList;
    private final CartDatabaseHandler cartDataBaseHandler;
    private final String TAG;
    private Activity context;
    private final ImageLoader volleyImageLoader;

    public EventAdapter(Activity context, ArrayList<Event> eventsList, String TAG) {

        this.context = context;
        this.eventsList = eventsList;
        this.volleyImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        this.cartDataBaseHandler = new CartDatabaseHandler(context);
        this.TAG = TAG;

    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTitleTextView.setText(eventsList.get(position).getTitle());

        holder.mEndDateTextView.setText(eventsList.get(position).getEndDate());

        holder.mItemImageView.setImageUrl(eventsList.get(position).getThumbnailUrl(), volleyImageLoader);

        if (TAG.equalsIgnoreCase(CartActivity.class.getSimpleName()))
            holder.mCartQuantityTextView.setText(String.valueOf(eventsList.get(position).getEventCount()));

    }

    @Override
    public int getItemCount() {

        if (eventsList.size() == 0) {
            Snackbar.make(context.findViewById(R.id.coordinatorLayout), R.string.empty_error_msg, Snackbar.LENGTH_SHORT).show();
        }

        return eventsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final NetworkImageView mItemImageView;
        private final TextView mTitleTextView;
        private final TextView mEndDateTextView;
        private final ImageView mCartImageView;
        private final ImageButton mAddToCartButton;
        private final ImageButton mRemoveFromCartButton;
        private final TextView mCartQuantityTextView;
        private final LinearLayout mCartUpdateLayout;

        ViewHolder(View v) {
            super(v);

            mItemImageView = (NetworkImageView) v.findViewById(R.id.itemImageView);

            mTitleTextView = (TextView) v.findViewById(R.id.titleTextView);

            mEndDateTextView = (TextView) v.findViewById(R.id.endDateTextView);

            mCartImageView = (ImageView) v.findViewById(R.id.cartImageView);

            mAddToCartButton = (ImageButton) v.findViewById(R.id.addToCartButton);

            mRemoveFromCartButton = (ImageButton) v.findViewById(R.id.removeFromCartButton);

            mCartQuantityTextView = (TextView) v.findViewById(R.id.cartQtyTextView);

            mCartUpdateLayout = (LinearLayout) v.findViewById(R.id.cartUpdateLayout);

            if (TAG.equalsIgnoreCase(MainActivity.class.getSimpleName())) {

                mCartUpdateLayout.setVisibility(View.GONE);

                mCartImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_shopping_cart_black_24dp));

                mCartImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Item added to Cart, position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();

                        cartDataBaseHandler.addEvent(eventsList.get(getAdapterPosition()));
                    }
                });


            } else {

                mCartUpdateLayout.setVisibility(View.VISIBLE);

                mCartImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_remove_shopping_cart_black_24px));

                mCartImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.alert_title_text);
                        builder.setMessage(R.string.alert_message_text);

                        builder.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(context, "Item removed from Cart, position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                                cartDataBaseHandler.removeEvent(eventsList.get(getAdapterPosition()).getRowId());
                                eventsList.remove(getAdapterPosition());
                                notifyDataSetChanged();

                                dialogInterface.dismiss();
                            }
                        });

                        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

            }

            mAddToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cartDataBaseHandler.addEventInCart(eventsList.get(getAdapterPosition()));
                    CartDatabaseHandler cartDatabaseHandler = new CartDatabaseHandler(context);
                    eventsList.clear();
                    eventsList.addAll(cartDatabaseHandler.getEventList());
                    notifyDataSetChanged();
                    cartDatabaseHandler.close();

                }
            });

            mRemoveFromCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (eventsList.get(getAdapterPosition()).getEventCount() == 1) {
                        return;
                    }
                    cartDataBaseHandler.removeEventFromCart(eventsList.get(getAdapterPosition()));
                    CartDatabaseHandler cartDatabaseHandler = new CartDatabaseHandler(context);
                    eventsList.clear();
                    eventsList.addAll(cartDatabaseHandler.getEventList());
                    notifyDataSetChanged();
                    cartDatabaseHandler.close();

                }
            });


        }
    }
}
