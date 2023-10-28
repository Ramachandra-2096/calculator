package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

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
            if (button.getText().toString().equals("Χ²"))
            {
                txt.setText(txt.getText().toString()+"^2");
            }else {
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
            String[] tokens = expression.split("(?=[-+*/%^])|(?<=[-+*/%^])");

            double result = 0;
            double currentNumber = 0;
            String operator = "+";
            boolean expectingNumber = true;

            for (String token : tokens) {
                if (token.matches("[+\\-*/%^]")) {
                    operator = token;
                    expectingNumber = true;
                } else {
                    double value = Double.parseDouble(token);
                    if (expectingNumber) {
                        currentNumber = value;
                    } else {
                        if (operator.equals("+")) {
                            currentNumber += value;
                        } else if (operator.equals("-")) {
                            currentNumber -= value;
                        } else if (operator.equals("*")) {
                            currentNumber *= value;
                        } else if (operator.equals("/")) {
                            if (value == 0) {
                                throw new ArithmeticException("Division by zero");
                            }
                            currentNumber /= value;
                        } else if (operator.equals("%")) {
                            currentNumber %= value;
                        } else if (operator.equals("^")) {
                            currentNumber = Math.pow(currentNumber, 2);
                        }
                    }
                    expectingNumber = false;
                }
            }

            if (expectingNumber) {
                throw new ArithmeticException("Invalid expression");
            }

            return currentNumber;
        }


        private static boolean isOperator(char c) {
            return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^';
        }
    }