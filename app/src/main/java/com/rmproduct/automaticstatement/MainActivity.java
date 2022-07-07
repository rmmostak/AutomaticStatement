package com.rmproduct.automaticstatement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.rmproduct.automaticstatement.DatabaseHelper.LAB_CHECK;
import static com.rmproduct.automaticstatement.DatabaseHelper.REMAIN_MONEY;
import static com.rmproduct.automaticstatement.DatabaseHelper.TABLE_NAME;
import static com.rmproduct.automaticstatement.DatabaseHelper.TCS_WING;
import static com.rmproduct.automaticstatement.DatabaseHelper.TEST_COST;
import static com.rmproduct.automaticstatement.DatabaseHelper.TOTAL_MONEY;
import static com.rmproduct.automaticstatement.DatabaseHelper.UNI_AUTHORITY;

public class MainActivity extends AppCompatActivity {

    private static float TAX = 10, UNI_AUTH = 25, TCS = 5, TEST = 40, PI = 10, CHAIR = 5, TEACHERS = 20, LAB_DEV = 15, STAFF = 10, PERCENT = 100;
    public static final int REQUEST_EXTERNAL_PERMISSION_CODE = 666;

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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        stateList = findViewById(R.id.stateList);
        addEntry = findViewById(R.id.addEntry);
        refreshLayout = findViewById(R.id.refreshLayout);

        /*Log.d("Total", databaseHelper.getSum(DatabaseHelper.TOTAL_MONEY) + "TK\n" +
                databaseHelper.getSum(DatabaseHelper.TAX) + "TK\n" +
                databaseHelper.getSum(DatabaseHelper.REMAIN_MONEY) + "TK");*/

        try {
            modelList.addAll(databaseHelper.statementModelList());
            adapter = new StatementAdapter(MainActivity.this, modelList);
            stateList.setAdapter(adapter);

        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }

        refreshLayout.setOnRefreshListener(() -> {
            try {
                modelList.clear();
                modelList.addAll(databaseHelper.statementModelList());
                adapter = new StatementAdapter(MainActivity.this, modelList);
                stateList.setAdapter(adapter);
                refreshLayout.setRefreshing(false);

            } catch (NullPointerException e) {
                Log.d("Exception", e.getMessage());
                refreshLayout.setRefreshing(false);
            }
            refreshLayout.setRefreshing(false);
        });

        addEntry.setOnClickListener(v -> {
            Log.d("Exception", "e.getMessage()");
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.add_entry_layout, null);

            dialogBuilder.setView(dialogView);

            EditText serialNo = dialogView.findViewById(R.id.serialNo);
            EditText addedAmount = dialogView.findViewById(R.id.addedMoney);
            EditText addDept = dialogView.findViewById(R.id.addDept);
            EditText sampleNo = dialogView.findViewById(R.id.addSample);
            TextView pickDate = dialogView.findViewById(R.id.pickDate);
            ImageButton addEntry = dialogView.findViewById(R.id.submitData);
            ImageButton cancel = dialogView.findViewById(R.id.cancel);

            try {
                if (databaseHelper.countRow() < 1) {
                    serialNo.setText("1");
                } else {
                    serialNo.setText(String.valueOf(databaseHelper.countRow() + 1));
                }
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }

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

                                try {
                                    String slSt = serialNo.getText().toString();
                                    int slNo = Integer.parseInt(slSt);
                                    int sample = Integer.parseInt(sampleNo.getText().toString());
                                    float amountLong = Float.parseFloat(addedAmount.getText().toString());
                                    String deptSt = addDept.getText().toString();
                                    if (!slSt.equals(databaseHelper.getValue(DatabaseHelper.SERIAL_NO, slSt))) {
                                        setDataToDatabase(slNo, sample, amountLong, deptSt, dateSt);
                                        alertDialog.dismiss();
                                    } else {
                                        serialNo.setError("Your entered serial no is exist,\n please enter a new one!");
                                        serialNo.requestFocus();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
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
        float labTest = (labCheck * TEST) / PERCENT;
        float pi = (labCheck * PI) / PERCENT;
        float chair = (labCheck * CHAIR) / PERCENT;
        float teachers = (labCheck * TEACHERS) / PERCENT;
        float labDev = (labCheck * LAB_DEV) / PERCENT;
        float staff = (labCheck * STAFF) / PERCENT;
        //Log.d("check", "\n" + slNo + "\n" + sampleNo + "\n" + dateSt + "\n" + deptSt + "\n" + amount + "tk\n" + tax + "tk\n" + remainMoney + "tk\n" + uniAuthority + "tk\n" + tcsWing + "tk\n" + labCheck + "tk\n"+labTest+"tk\n"+pi+"tk\n"+chair+"tk\n"+teachers+"tk\n"+labDev+"tk\n"+staff+"tk");

        try {
            long row = databaseHelper.makeStatementRow(slNo, sampleNo, dateSt, deptSt, amount, tax, remainMoney, uniAuthority, tcsWing, labCheck, labTest, pi, chair, teachers, labDev, staff);

            if (row == -1) {
                Toast.makeText(getApplicationContext(), "Data insertion failed!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Your entry is added!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_export_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static final String[] PERMISSIONS_EXTERNAL_STORAGE = {
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
    };

    public boolean checkExternalStoragePermission(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }

        int readStoragePermissionState = ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE);
        int writeStoragePermissionState = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        boolean externalStoragePermissionGranted = readStoragePermissionState == PackageManager.PERMISSION_GRANTED &&
                writeStoragePermissionState == PackageManager.PERMISSION_GRANTED;
        if (!externalStoragePermissionGranted) {
            requestPermissions(PERMISSIONS_EXTERNAL_STORAGE, REQUEST_EXTERNAL_PERMISSION_CODE);
        }

        return externalStoragePermissionGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_EXTERNAL_PERMISSION_CODE) {
                if (checkExternalStoragePermission(MainActivity.this)) {
                    // Continue with your action after permission request succeed
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.aboutApp) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));

        } else if (id == R.id.exportData) {
            if (checkExternalStoragePermission(MainActivity.this)) {
                String directory = Environment.getExternalStorageDirectory().getPath() + "/RmProduct/Automatic/Statement/Export/";
                File file = new File(directory);
                if (!file.exists()) {
                    Log.v("File Created", String.valueOf(file.mkdirs()));
                }
                SQLiteToExcel sqLiteToExcel = new SQLiteToExcel(MainActivity.this, "Statement.db", directory);

                databaseHelper.makeStatementRow(10000, 0, dateSt, "Total", databaseHelper.getSum(TOTAL_MONEY), databaseHelper.getSum(DatabaseHelper.TAX), databaseHelper.getSum(REMAIN_MONEY), databaseHelper.getSum(UNI_AUTHORITY), databaseHelper.getSum(TCS_WING), databaseHelper.getSum(LAB_CHECK), databaseHelper.getSum(TEST_COST), databaseHelper.getSum(DatabaseHelper.PI), databaseHelper.getSum(DatabaseHelper.CHAIR), databaseHelper.getSum(DatabaseHelper.TEACHERS), databaseHelper.getSum(DatabaseHelper.LAB_DEV), databaseHelper.getSum(DatabaseHelper.STAFF));

                sqLiteToExcel.exportAllTables("export.xls", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {
                        Log.d("Started", "Running!");
                    }

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onCompleted(String filePath) {
                        databaseHelper.deleteStatement(10000);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YYYY", Locale.getDefault());
                        String str = sdf.format(new Date());

                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CSIRL JUST Exported on " + str);
                        Log.d("tag", "CSIRL JUST Exported on " + str);

                        File root = new File("file://" + filePath);
                        if (!file.exists() || !file.canRead()) {
                            return;
                        }
                        Uri uri;
                        if (Build.VERSION.SDK_INT < 24) {
                            uri = Uri.fromFile(root);
                        } else {
                            uri = Uri.parse(root.getPath()); // My work-around for new SDKs, worked for me in Android 10 using Solid Explorer Text Editor as the external editor.
                        }
                        Log.d("root", uri.toString());
                        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(emailIntent, "Share file using..."));
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("export", e.getMessage());
                    }
                });
            } else {
                Log.d("Error", "Permission denied!");
            }
        } else if (id == R.id.deleteTable) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            dialogBuilder.setTitle("Alert!");
            dialogBuilder.setMessage("Do you want to delete this statement data?");
            dialogBuilder.setPositiveButton("Yes", (dialog, which) -> {

                try {
                    if (databaseHelper.dropStatement(TABLE_NAME)) {
                        //sendEmail(filePath);
                        Toast.makeText(MainActivity.this, "You have successfully deleted statement data!", Toast.LENGTH_LONG).show();
                    } else {
                        //sendEmail(filePath);
                        Toast.makeText(MainActivity.this, "Something went wrong, Please try again later!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();

            }).setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }
}