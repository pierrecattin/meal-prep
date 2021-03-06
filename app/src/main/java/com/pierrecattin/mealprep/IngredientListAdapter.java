package com.pierrecattin.mealprep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder> {
    private final LayoutInflater inflater;
    private List<Ingredient> ingredients;
    private Listener listener;

    IngredientListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, final int position) {
        if (ingredients != null) {
            Ingredient current = ingredients.get(position);
            holder.ingredientItemView.setText(current.getName());
            View itemView = holder.itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(position);
                    }
                }
            });

        } else {
            // Covers the case of data not being ready yet.
            holder.ingredientItemView.setText("No Ingredient");
        }
    }

    void setIngredients(List<Ingredient> ingredients){
        if(ingredients!=null) {
            this.ingredients = ingredients;
            sortIngredients();
        } else {
            this.ingredients.clear();
        }
        notifyDataSetChanged();
    }

    private void sortIngredients(){
        Collections.sort(ingredients);
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (ingredients != null)
            return ingredients.size();
        else return 0;
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final TextView ingredientItemView;

        private IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientItemView = itemView.findViewById(R.id.ingredientTextView);
        }
    }
    interface Listener {
        void onClick(int position);
    }
    public void setListener(Listener listener){
        this.listener = listener;
    }
    public Ingredient getIngredientAtPosition (int position) {
        return ingredients.get(position);
    }

}
