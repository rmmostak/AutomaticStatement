package com.rmproduct.automaticstatement;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static float TAX = 10, UNI_AUTH = 25, TCS = 5, PERCENT = 100;

    private ListView stateList;
    private FloatingActionButton addEntry;
    private String dateSt;
    private DatabaseHelper databaseHelper;
    private StatementAdapter adapter;
    private List<StatementModel> modelList = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            databaseHelper = new DatabaseHelper(this);
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
        }

        stateList = findViewById(R.id.stateList);
        addEntry = findViewById(R.id.addEntry);
        refreshLayout = findViewById(R.id.refreshLayout);

        int rowID = getIntent().getIntExtra("rowID", 1);
        Log.d("row", "ID " + rowID);

        if (databaseHelper.statementModelList().isEmpty()) {
            return;
        } else {
            modelList.addAll(databaseHelper.statementModelList());
            adapter = new StatementAdapter(MainActivity.this, modelList);
            stateList.setAdapter(adapter);

        }

        refreshLayout.setOnRefreshListener(() -> {
            modelList.clear();
            if (databaseHelper.statementModelList().isEmpty()) {
                return;
            } else {
                modelList.addAll(databaseHelper.statementModelList());
                adapter = new StatementAdapter(MainActivity.this, modelList);
                stateList.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
            }
        });

        stateList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        addEntry.setOnClickListener(v -> {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.add_entry_layout, null);

            dialogBuilder.setView(dialogView);

            EditText serialNo = dialogView.findViewById(R.id.serialNo);
            EditText addedAmount = dialogView.findViewById(R.id.addedMoney);
            EditText addDept = dialogView.findViewById(R.id.addDept);
            EditText sampleNo = dialogView.findViewById(R.id.addSample);
            TextView pickDate = dialogView.findViewById(R.id.pickDate);
            Button addEntry = dialogView.findViewById(R.id.submitData);
            Button cancel = dialogView.findViewById(R.id.cancel);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YYYY", Locale.getDefault());
            String str = sdf.format(new Date());
            pickDate.setText(str);
            dateSt = str;

            dialogBuilder.setTitle("Enter your Data");
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            pickDate.setOnClickListener(v12 -> {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        (view, year, month, dayOfMonth) -> {
                            //todo
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, month, dayOfMonth);
                            pickDate.setText(sdf.format(newDate.getTime()));
                            dateSt = sdf.format(newDate.getTime());
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            });

            cancel.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });

            addEntry.setOnClickListener(v2 -> {

                if (!serialNo.getText().toString().equals("")) {
                    if (!sampleNo.getText().toString().equals("")) {
                        if (!addedAmount.getText().toString().equals("")) {
                            if (!addDept.getText().toString().equals("")) {

                                String slSt = serialNo.getText().toString();
                                int slNo = Integer.parseInt(slSt);
                                int sample = Integer.parseInt(sampleNo.getText().toString());
                                float amountLong = Float.parseFloat(addedAmount.getText().toString());
                                String deptSt = addDept.getText().toString();
                                setDataToDatabase(slNo, sample, amountLong, deptSt, dateSt);

                                alertDialog.dismiss();

                            } else {
                                addDept.setError("Please enter department!!");
                                addDept.requestFocus();
                            }
                        } else {
                            addedAmount.setError("Please enter amount!!");
                            addedAmount.requestFocus();
                        }
                    } else {
                        sampleNo.setError("Please enter sample no!!");
                        sampleNo.requestFocus();
                    }
                } else {
                    serialNo.setError("Please enter serial no!!");
                    serialNo.requestFocus();
                }

            });
        });
    }

    private void setDataToDatabase(int slNo, int sampleNo, float amountLong, String deptSt, String dateSt) {
        float amount = amountLong;
        float tax = (amount * TAX) / PERCENT;
        float remainMoney = (amount - tax);
        float uniAuthority = (remainMoney * UNI_AUTH) / PERCENT;
        float tcsWing = (remainMoney * TCS) / PERCENT;
        float labCheck = remainMoney - uniAuthority - tcsWing;
        //Log.d("check", "\n" + slNo + "\n" + sampleNo + "\n" + dateSt + "\n" + deptSt + "\n" + amount + "tk\n" + tax + "tk\n" + remainMoney + "tk\n" + uniAuthority + "tk\n" + tcsWing + "tk\n" + labCheck + "tk");

        long row = databaseHelper.makeStatementRow(slNo, sampleNo, dateSt, deptSt, amount, tax, remainMoney, uniAuthority, tcsWing, labCheck);

        if (row == -1) {
            Toast.makeText(getApplicationContext(), "Data insertion failed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Your entry is added!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_export_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aboutApp) {


        } else if (id == R.id.exportData) {

        }
        return super.onOptionsItemSelected(item);
    }
}