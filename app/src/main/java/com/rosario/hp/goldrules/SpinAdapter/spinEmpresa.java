package com.rosario.hp.goldrules.SpinAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rosario.hp.goldrules.Entidades.empresa;

import java.util.ArrayList;

public class spinEmpresa extends ArrayAdapter<empresa> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<empresa> values;

    public spinEmpresa(Context context, int textViewResourceId,
                                      ArrayList<empresa> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
        return values.size();
    }

    public empresa getItem(int position){
        return values.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(17);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getNom_empresa());
        label.setPadding(10, 10, 10, 20);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);

        label.setLayoutParams(layoutParams);
        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(17);
        label.setText(values.get(position).getNom_empresa());
        label.setPadding(10, 10, 10, 20);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);

        label.setLayoutParams(layoutParams);

        return label;
    }
}