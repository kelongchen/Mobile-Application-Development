package com.example.temperature_converter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;

/**
 * @Author: Kelong Chen
 * @Banner  ID: A20452143
 * @Date: 07-Sep-2019
 * @Description: Temperature Converter
 * @Version: 1.0
 */


public class MainActivity extends AppCompatActivity {

    //put log tag
    private static final String TAG = "MainActivity";

    //define parameters
    private EditText Input;
    private TextView Output;
    private TextView FDT;
    private TextView CDT;
    private TextView history;
    private RadioButton FCB;
    private RadioButton CFB;
    String HistoryResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //log debug
        Log.d(TAG, "onCreate: This is in onCreate!");

        //bind variables to screen widgets
        Input = findViewById(R.id.Edit);
        Output = findViewById(R.id.Text);
        FDT= findViewById(R.id.FDegreesText);
        CDT= findViewById(R.id.CDegreesText);
        history = findViewById(R.id.HistoryText);
        FCB = findViewById(R.id.FCButton);
        CFB = findViewById(R.id.CFButton);

        //sets scroll movement to 'history' text
        history.setMovementMethod(new ScrollingMovementMethod());

    }



    //change button text when click different radio buttons
    public void clickRadio(View v) {
        if (CFB.isChecked()) {
            FDT.setText(R.string.CF);
            CDT.setText(R.string.FC);

        } else {
            CDT.setText(R.string.CF);
            FDT.setText(R.string.FC);
        }

        Input.setText("");
        Output.setText("");
    }

    //convert function
    public void convert(View v) {

        //initialize InputNumber to avoid app crash when there is no input
        double InputNumber = 0;
        double ConvertNumber;
        String Result;

        if (!(Input.getText().toString().equals(""))) {
            InputNumber = Double.parseDouble(Input.getText().toString());

            if (CFB.isChecked()) {
                ConvertNumber = (1.8 * InputNumber) + 32.0;
                Result = String.format("%,.1f", ConvertNumber);
                Output.setText(Result);
                HistoryResult = history.getText().toString();
                history.setText(InputNumber + " C " + "==> " + Result + " F" + "\n" + HistoryResult);

            } else {
                ConvertNumber = (InputNumber - 32.0) / 1.8;
                Result = String.format("%,.1f", ConvertNumber);
                Output.setText(Result);
                HistoryResult = history.getText().toString();
                history.setText(InputNumber + " F " + "==> " + Result + " C" + "\n"+ HistoryResult);
            }
        } else {
            //toast users when there is no input
            Toast.makeText(this, "Please type in numbers", Toast.LENGTH_SHORT).show();

        }

    }


    //clear history when click the clear button
    public void clearhistory(View v) {
        history.setText("");
        HistoryResult = ("");
    }

    //put info in bundle when rotating
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("HISTORY", history.getText().toString());
        outState.putString("HISTORYRESULT", HistoryResult);
        outState.putString("OUTPUT", Output.getText().toString());
        outState.putString("cdt",CDT.getText().toString());
        outState.putString("fdt",FDT.getText().toString());

        //call super last
        super.onSaveInstanceState(outState);

        //log debug
        Log.d(TAG, "onSaveInstanceState: This is in onSaveInstanceState");

    }

    //restore info from bundle when restart
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        //call super first
        super.onRestoreInstanceState(savedInstanceState);

        //log debug
        Log.d(TAG, "onRestoreInstanceState: This is in onRestoreInstanceState");

        history.setText(savedInstanceState.getString("HISTORY"));
        HistoryResult = savedInstanceState.getString("HISTORYRESULT");
        Output.setText(savedInstanceState.getString("OUTPUT"));
        CDT.setText(savedInstanceState.getString("cdt"));
        FDT.setText(savedInstanceState.getString("fdt"));

    }
}
