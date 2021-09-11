package com.rmproduct.automaticstatement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class StatementAdapter extends ArrayAdapter<StatementModel> {

    private Activity context;
    private List<StatementModel> modelList;
    private TextView slNo, sampleNo, dateAccept, dept, totalMoney, tax, remainMoney, uniAuthority, tcsWing, labCheck;
    private LinearLayout linearRow;
    private DatabaseHelper databaseHelper;

    public StatementAdapter(Activity context, List<StatementModel> modelList) {
        super(context, R.layout.list_row, modelList);
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View itemView = inflater.inflate(R.layout.list_row, null, true);

        slNo = itemView.findViewById(R.id.slNo);
        sampleNo = itemView.findViewById(R.id.sampleNo);
        dateAccept = itemView.findViewById(R.id.dateAccept);
        dept = itemView.findViewById(R.id.dept);
        totalMoney = itemView.findViewById(R.id.totalEarn);
        tax = itemView.findViewById(R.id.tax);
        remainMoney = itemView.findViewById(R.id.remainMoney);
        uniAuthority = itemView.findViewById(R.id.uniAuthority);
        tcsWing = itemView.findViewById(R.id.tcsWing);
        labCheck = itemView.findViewById(R.id.moneyCheck);
        linearRow=itemView.findViewById(R.id.linearRow);

        StatementModel model = modelList.get(position);

        slNo.setText(String.valueOf(model.getSlNo()));
        sampleNo.setText(String.valueOf(model.getSampleNo()));
        dateAccept.setText(model.getDateAccept());
        dept.setText(model.getDept());
        totalMoney.setText(String.valueOf(model.getTotalEarned()));
        tax.setText(String.valueOf(model.getTax()));
        remainMoney.setText(String.valueOf(model.getRemainMoney()));
        uniAuthority.setText(String.valueOf(model.getUniAuthority()));
        tcsWing.setText(String.valueOf(model.getTcsWing()));
        labCheck.setText(String.valueOf(model.getLabCheck()));

        linearRow.setOnClickListener(v -> {
            databaseHelper = new DatabaseHelper(v.getContext());
            SQLiteDatabase database = databaseHelper.getWritableDatabase();

            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
            dialogBuilder.setTitle("Are you sure to delete this row?");
            dialogBuilder.setPositiveButton("Yes", (dialog, which) -> {

                databaseHelper.deleteStatement(model.getSlNo());

            }).setNegativeButton("No", (dialog, which) -> {
                Log.d("GET", databaseHelper.getValue(DatabaseHelper.DATE_ACCEPT, model.getDateAccept()));
            }).show();
        });

        return itemView;
    }
}
