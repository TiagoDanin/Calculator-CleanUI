package com.tiagodanin.example.calculatorcleanui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView textInput;
    private TextView oldTextResult;
    private boolean isPending = false;
    private FunctionOperations lastOperation;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInput = findViewById(R.id.textInput);
        oldTextResult = findViewById(R.id.textOldResult);

        loadPadsNumbers();
        loadPadsFunctions();
    }

    @SuppressLint({"FindViewByIdCast", "SetTextI18n"})
    private void loadPadsNumbers() {
        Map<View, Integer> padsNumbers = new HashMap<>();
        padsNumbers.put(findViewById(R.id.pad00), 0);
        padsNumbers.put(findViewById(R.id.pad01), 1);
        padsNumbers.put(findViewById(R.id.pad02), 2);
        padsNumbers.put(findViewById(R.id.pad03), 3);
        padsNumbers.put(findViewById(R.id.pad04), 4);
        padsNumbers.put(findViewById(R.id.pad05), 5);
        padsNumbers.put(findViewById(R.id.pad06), 6);
        padsNumbers.put(findViewById(R.id.pad07), 7);
        padsNumbers.put(findViewById(R.id.pad08), 8);
        padsNumbers.put(findViewById(R.id.pad09), 9);

        for (final Map.Entry<View, Integer> padNumber : padsNumbers.entrySet()) {
            Button button = (Button) padNumber.getKey();
            int value = padNumber.getValue();

            button.setOnClickListener(v -> {
                textInput.setText(textInput.getText() + String.valueOf(value));
            });
        }

        findViewById(R.id.padPoint).setOnClickListener(v -> {
            textInput.setText(textInput.getText() + ".");
        });
    }

    private void loadPadsFunctions() {
        Map<View, FunctionOperations> padsFunctions = new HashMap<>();
        padsFunctions.put(findViewById(R.id.padSubtraction), FunctionOperations.Subtraction);
        padsFunctions.put(findViewById(R.id.padSum), FunctionOperations.Sum);
        padsFunctions.put(findViewById(R.id.padMultiplication), FunctionOperations.Multiplication);
        padsFunctions.put(findViewById(R.id.padDivision), FunctionOperations.Division);

        for (final Map.Entry<View, FunctionOperations> padFunction : padsFunctions.entrySet()) {
            Button button = (Button) padFunction.getKey();
            FunctionOperations operation = padFunction.getValue();

            button.setOnClickListener(v -> {
                if (isPending) {
                    doOperation(operation);
                }

                if (!isPending) {
                    lastOperation = operation;
                    isPending = true;
                    textInput.setText(textInput.getText() + operation.getCharRepresentation());
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void doOperation(FunctionOperations operation) {
        try {
            float[] values = getValuesInput();
            float result = 0;
            switch (operation) {
                case Sum:
                    result = values[0] + values[1];
                    break;
                case Subtraction:
                    result = values[0] - values[1];
                    break;
                case Multiplication:
                    result = values[0] * values[1];
                    break;
                case Division:
                    result = values[0] / values[1];
                    break;
                case Result:
                    result = values[0];
                    break;
            }

            isPending = false;
            if (operation == FunctionOperations.Result) {
                oldTextResult.setText(String.valueOf(result));
                textInput.setText("");
            } else {
                oldTextResult.setText("" + values[0] + operation.getCharRepresentation() + values[1] + " = " + result);
                textInput.setText(String.valueOf(result));
            }
        } catch (NumberFormatException ignored) {
            oldTextResult.setText("Invalid value");
            textInput.setText("");
        }
    }

    private float[] getValuesInput() {
        String input = (String) textInput.getText();
        String[] values = input.split("([\\+\\-x\\/])", 2);
        if (values[0].length() <= 0) {
            return new float[]{
                    (float) 0,
                    (float) 0
            };
        }

        if (values.length <= 1 || values[1].length() <= 0) {
            return new float[]{
                    Float.parseFloat(values[0]),
                    (float) 0
            };
        }

        return new float[]{
                Float.parseFloat(values[0]),
                Float.parseFloat(values[1])
        };
    }


    public void onClearInput(View view) {
        textInput.setText("");
    }

    public void onResultInput(View view) {
        if (lastOperation != null) {
            doOperation(lastOperation);
            lastOperation = null;
        } else {
            doOperation(FunctionOperations.Result);
        }
    }
}