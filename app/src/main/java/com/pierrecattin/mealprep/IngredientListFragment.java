package com.pierrecattin.mealprep;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class IngredientListFragment extends ListFragment {

    private static final String ARG_INGREDIENTS= "ingredients";
    private List<Ingredient> ingredients;

    public IngredientListFragment() {
        // Required empty public constructor
    }

    public IngredientListFragment(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /*public static IngredientListFragment newInstance(List<Ingredient> ingredients) {
        IngredientListFragment fragment = new IngredientListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_INGREDIENTS, (Serializable)ingredients);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingredients = (List)getArguments().getSerializable(ARG_INGREDIENTS);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //String[] names = new String[ingredients.size()];
        String[] names = new String[5];
        for (int i = 0; i < names.length; i++) {
            //names[i] = ingredients.get(i).getName();
            names[i] = "ingredient "+i;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                inflater.getContext(), android.R.layout.simple_list_item_1,
                names);
        setListAdapter(adapter);
        //return inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*@Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            TextView ingredientName = (TextView) view.findViewById(R.id.textView);
            if(ingredients != null && ingredients.size()>0){
                ingredientName.setText(ingredients.get(0).getName());
            } else{
                ingredientName.setText("No ingredient selected");
            }
        }
    } */

    public void setIngredients(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }
}