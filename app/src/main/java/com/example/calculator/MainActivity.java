package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    Button button, btnac, btnc;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.textView);
        btnac = findViewById(R.id.bac);
        btnc = findViewById(R.id.bc);

        btnac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText("");
            }
        });

        btnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = txt.getText().toString();
                if (!currentText.isEmpty()) {
                    txt.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });
    }

    public void num(View view) {
        Button button = (Button) view;
        String currentText = txt.getText().toString();
        txt.setText(currentText + button.getText().toString());
    }

    public void op(View view) {
        button = (Button) view;
        String currentText = txt.getText().toString();
        if (button.getText().toString().equals("Χ²")) {
            txt.setText(txt.getText().toString() + "^2");
        } else {
            if (!button.getText().toString().equals("=")) {
                if (currentText.isEmpty() || isOperator(currentText.charAt(currentText.length() - 1))) {

                } else {
                    txt.setText(currentText + button.getText().toString());
                }
            } else {
                try {
                    double result = evaluateExpression(currentText);
                    DecimalFormat df = new DecimalFormat("#.######");
                    txt.setText(df.format(result));
                } catch (ArithmeticException e) {
                    txt.setText("Error");
                }
            }
        }
    }

    private static double evaluateExpression(String expression) {
        expression = expression.replaceAll(" ", "").trim();
        try {
            return evaluateExpressionHelper(expression);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            throw new ArithmeticException("Invalid expression");
        }
    }

    private static double evaluateExpressionHelper(String expression) {
        Queue<String> postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    private static Queue<String> infixToPostfix(String expression) {
        Queue<String> output = new LinkedList<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char token = expression.charAt(i);

            if (Character.isDigit(token)) {
                StringBuilder operand = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    operand.append(expression.charAt(i));
                    i++;
                }
                output.add(operand.toString());
                i--;
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && getPrecedence(operators.peek()) >= getPrecedence(token)) {
                    output.add(operators.pop().toString());
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            output.add(operators.pop().toString());
        }

        return output;
    }


    private static double evaluatePostfix(Queue<String> postfix) {
        Stack<Double> operands = new Stack<>();

        for (String token : postfix) {
            if (isNumeric(token)) {
                operands.push(Double.parseDouble(token));
            } else {
                double b = operands.pop();
                double a = operands.pop();
                double result = applyOperator(a, b, token);
                operands.push(result);
            }
        }

        return operands.pop();
    }

    private static int getPrecedence(char operator) {
        if (operator == '^') {
            return 3;
        } else if (operator == 'X' || operator == '/') {
            return 2;
        } else if (operator == '+' || operator == '-') {
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private static double applyOperator(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "X":
                return a * b;
            case "/":
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            case "^":
                return Math.pow(a, b);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == 'X' || c == '/' || c == '^';
    }

}