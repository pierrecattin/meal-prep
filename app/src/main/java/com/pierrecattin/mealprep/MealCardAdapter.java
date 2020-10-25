package com.pierrecattin.mealprep;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
class MealCardAdapter extends RecyclerView.Adapter<MealCardAdapter.ViewHolder>{
    private String[] carbs;
    private String[] sauces;
    public MealCardAdapter(String[] carbs, String[] sauces){
        this.carbs = carbs;
        this.sauces = sauces;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Define the view to be used for each data item
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    @Override
    public int getItemCount(){
        return carbs.length;
    }

    @Override
    public MealCardAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.meal_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        CardView cardView = holder.cardView;
        TextView carbTextView = cardView.findViewById(R.id.meal_carbs);
        TextView sauceTextView = cardView.findViewById(R.id.meal_sauce);
        carbTextView.setText(carbs[position]);
        sauceTextView.setText(sauces[position]);
    }
}
