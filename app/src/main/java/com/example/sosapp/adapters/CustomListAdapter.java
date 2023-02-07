package com.example.sosapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sosapp.EditNumber;
import com.example.sosapp.R;
import com.example.sosapp.db.DatabaseManagement;
import com.example.sosapp.helper.Helper;
import com.example.sosapp.state.LockFunctions;
import com.example.sosapp.state.Status;

import java.util.Arrays;

public class CustomListAdapter extends ArrayAdapter<Helper> {
    Activity context;
    Helper[] helpers;
    LayoutInflater layoutInflater;

    public CustomListAdapter(Activity context , Helper [] helpers) {
        super(context , R.layout.activity_list_view , helpers);
        Arrays.sort(helpers);
        this.context = context;
        this.helpers = helpers;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return helpers.length;
    }

    @Override
    public Helper getItem(int i) {
        return helpers[i];
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View listView;
        if (helpers.length > 0) {
            listView = layoutInflater.inflate(R.layout.activity_list_view, viewGroup, false);
            Arrays.sort(helpers);
            TextView viewName = listView.findViewById(R.id.helperName);
            viewName.setText(helpers[position].getName());
            TextView viewNumber = listView.findViewById(R.id.helperNumber);
            viewNumber.setText(helpers[position].getNumber());
            ImageView viewEdit = listView.findViewById(R.id.edit);

            DatabaseManagement db = new DatabaseManagement(context);
            viewEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditNumber.class);
                intent.putExtra("name", helpers[position].getName());
                intent.putExtra("number", helpers[position].getNumber());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });

            ImageView viewDelete = listView.findViewById(R.id.delete);
            viewDelete.setOnClickListener(v -> {
                try {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Delete contact");
                    alert.setMessage("Do you want to delete " + helpers[position].getName() + "'s contacts?");
                    alert.setPositiveButton("Yes" , ((dialogInterface, i) -> {
                        db.delete(helpers[position]);
                        helpers = db.readData().toArray(new Helper[0]);
                        notifyDataSetChanged();
                    }));
                    alert.setNegativeButton("Cancel" , null);
                    alert.show();
                } catch (Exception e) {
                    Log.d("Delete error" , e.getMessage());
                }
            });
        }
        else{
            listView = layoutInflater.inflate(R.layout.activity_empty_view , viewGroup , false);
        }

        return listView;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }


}
