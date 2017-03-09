package com.example.hpkorisnik.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

import bsh.Interpreter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    TextView textViewResult;
    String previousButton = "/";
    String lastOperation = "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        textViewResult = (TextView)findViewById(R.id.textViewResult);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnDel) {
            textView.setText("");
            textViewResult.setText("");
            previousButton = "/";
            lastOperation = "/";
        }

        else if (textView.getText().toString().length() == 35) {
            return;
        }

        else {

            switch (v.getId()) {

                case R.id.btn0:
                    if (previousButton.equals("0")) {
                        if (textView.getText() != "") {
                            String substring = "";
                            if (textView.getText().toString().split(Pattern.quote(lastOperation)).length > 0) {
                                substring = fetchLastNumber();
                            }
                            if (!substring.contains(".")) {
                                textView.setText(textView.getText().toString().substring(0, textView.getText().toString().length() - 1));
                            }
                        }
                    }
                    if (!textView.getText().toString().equals("")) {
                        setText(v);
                    }
                    break;

                case R.id.btn1:
                case R.id.btn2:
                case R.id.btn3:
                case R.id.btn4:
                case R.id.btn5:
                case R.id.btn6:
                case R.id.btn7:
                case R.id.btn8:
                case R.id.btn9:

                    System.out.println("fetched number: " + fetchLastNumber());
                    System.out.println("length of number: " + fetchLastNumber().length());
                    boolean isGoodLength = true;
                    if (((previousButton.equals(".")) || (previousButton.equals("0")) || (previousButton.equals("1")) || (previousButton.equals("2")) || (previousButton.equals("3")) || (previousButton.equals("4")) || (previousButton.equals("5")) || (previousButton.equals("6")) || (previousButton.equals("7")) || (previousButton.equals("8")) || (previousButton.equals("9")))) {
                        isGoodLength = fetchLastNumber().length() < 9;
                    }

                    if (isGoodLength) {
                        if (previousButton.equals("0")) {
                            if (textView.getText() != "") {
                                String substring = "";
                                if (textView.getText().toString().split(Pattern.quote(lastOperation)).length > 0) {
                                    substring = fetchLastNumber();
                                }
                                if (!substring.contains(".")) {
                                    textView.setText(textView.getText().toString().substring(0, textView.getText().toString().length() - 1));
                                }
                            }
                        }
                        setText(v);
                    }
                    break;

                case R.id.btnDivide:
                case R.id.btnMinus:
                case R.id.btnMultiply:
                case R.id.btnPlus:


                    if (previousButton.equals("=")) {
                        setText(v);
                        lastOperation = ((Button)v).getText().toString();
                        return;
                    }

                    if (!((textView.getText().toString().equals("")) ||(previousButton.equals(".")) || (previousButton.equals("/")) || (previousButton.equals("+")) || (previousButton.equals("-")) || (previousButton.equals("*")))) {
                        if (!(textViewResult.getText().toString() == "")) {
                            textViewResult.setText("" + (compute(textViewResult.getText().toString(), fetchLastNumber(), lastOperation)));
                            lastOperation = ((Button)v).getText().toString();
                        }

                        else {
                            String spliter[] = textView.getText().toString().split(Pattern.quote(lastOperation));
                            if (spliter.length == 2) {
                                textViewResult.setText("" + (compute(spliter[0],spliter[1], lastOperation)));
                            }
                            lastOperation = ((Button)v).getText().toString();
                        }
                        setText(v);
                    }

                    break;

                case R.id.btnDot:
                    if (textView.getText() != "") {
                        String substring = "";
                        if (textView.getText().toString().split(Pattern.quote(lastOperation)).length > 0) {
                            substring = fetchLastNumber();
                        }
                        if (!((substring.contains(".")) || (previousButton.equals("/")) || (previousButton.equals("+")) || (previousButton.equals("-")) || (previousButton.equals("*")))) {
                            setText(v);
                        }
                    }
                    else {
                        textView.setText("0" + ((Button)v).getText().toString());
                        previousButton = ((Button)v).getText().toString();
                    }
                    break;

                case R.id.btnEquals:
                    Interpreter interpreter = new Interpreter();
                    try {
                        Object checkIt = interpreter.eval(textView.getText().toString());
                        textViewResult.setText(checkIt.toString());

                    }
                    catch (Exception e) {
                        textViewResult.setText("NaN");
                        System.out.println(e);
                    }
                    /*
                    if (textView.getText().toString() != "") {
                        if (((textView.getText().toString().charAt(textView.getText().toString().length() - 1) >= '0') && (textView.getText().toString().charAt(textView.getText().toString().length() - 1) <= '9'))) {

                            if (!(textViewResult.getText().toString() == "")) {
                                System.out.println("------");
                                System.out.println("fetched in equals: " + fetchLastNumber());
                                System.out.println("last operation in equals: " + lastOperation);
                                System.out.println("textViewResult in equals: " + textViewResult.getText().toString());
                                System.out.println("textView in equals: " + textView.getText().toString());
                                textViewResult.setText("" + (compute(textViewResult.getText().toString(), fetchLastNumber(), lastOperation)));
                            } else {
                                String spliter[] = textView.getText().toString().split(Pattern.quote(lastOperation));
                                if (spliter.length == 2) {
                                    textViewResult.setText("" + (compute(spliter[0], spliter[1], lastOperation)));
                                }
                            }
                            previousButton = ((Button) v).getText().toString();
                        }
                    }
                    */

                    break;
            }
        }
    }


    public void setText(View v) {
        textView.setText(textView.getText().toString() + ((Button) v).getText().toString());
        previousButton = ((Button)v).getText().toString();
    }

    public double compute(String n1, String n2, String operator) {
        double number1 = Double.valueOf(n1);
        double number2 = Double.valueOf(n2);
        switch(operator) {
            case "+":
                return round(number1 + number2,2);
            case "-":
                return round(number1 - number2,2);
            case "*":
                return round(number1 * number2,2);
            case "/":
                if (number2 == 0) return Double.NaN;
                return round(number1 / number2,2);
        }
        return Double.NaN;
    }

    public String fetchLastNumber() {
        return  textView.getText().toString().split(Pattern.quote(lastOperation)) [ textView.getText().toString().split(Pattern.quote(lastOperation)).length - 1 ];
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
