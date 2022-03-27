package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Path;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.OnReceiveContentListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;

public class ComplexCalActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_C0;
    Button btn_C1;
    Button btn_C2;
    Button btn_C3;
    Button btn_C4;
    Button btn_C5;
    Button btn_C6;
    Button btn_C7;
    Button btn_C8;
    Button btn_C9;
    Button btn_Cmul;
    Button btn_CC;
    Button btn_Cdiv;
    Button btn_CBack;
    Button btn_Csub;
    Button btn_Cpoint;
    Button btn_Cplus;
    Button btn_Cequ;

    TextView CScreen;
    TextView CScreenResault;

    String InputText = "";

    String Point = "1";//是否可以输入小数点，在输入一个符号的之后可以输入

    String Operator = "0";//是否可以输入运算符,btn_Cdiv,btn_Cmul,btn_Cplus,  ***btn_Csub***  ,btn_Cequ

    String negtive = "1";//负号

    Boolean AnsExsit = false;
    String Ans = "";

    private void initView() {

        btn_C0 = (Button) findViewById(R.id.btn_C0);
        btn_C1 = (Button) findViewById(R.id.btn_C1);
        btn_C2 = (Button) findViewById(R.id.btn_C2);
        btn_C3 = (Button) findViewById(R.id.btn_C3);
        btn_C4 = (Button) findViewById(R.id.btn_C4);
        btn_C5 = (Button) findViewById(R.id.btn_C5);
        btn_C6 = (Button) findViewById(R.id.btn_C6);
        btn_C7 = (Button) findViewById(R.id.btn_C7);
        btn_C8 = (Button) findViewById(R.id.btn_C8);
        btn_C9 = (Button) findViewById(R.id.btn_C9);

        btn_Cmul = (Button) findViewById(R.id.btn_Cmul);
        btn_Cdiv = (Button) findViewById(R.id.btn_Cdiv);
        btn_Cplus = (Button) findViewById(R.id.btn_Cplus);
        btn_Csub = (Button) findViewById(R.id.btn_Csub);
        btn_Cpoint = (Button) findViewById(R.id.btn_Cpoint);
        btn_Cequ = (Button) findViewById(R.id.btn_Cequ);
        btn_CBack = (Button) findViewById(R.id.btn_CBack);
        btn_CC = (Button) findViewById(R.id.btn_CC);

        CScreen = (TextView) findViewById(R.id.Cscreen);
        CScreenResault = (TextView) findViewById(R.id.Cscreenresalt);

        InputText = "";
        Point = "1";
        Operator = "0";
        negtive = "1";
    }

    private void initEvent() {
        btn_C0.setOnClickListener(this);
        btn_C1.setOnClickListener(this);
        btn_C2.setOnClickListener(this);
        btn_C3.setOnClickListener(this);
        btn_C4.setOnClickListener(this);
        btn_C5.setOnClickListener(this);
        btn_C6.setOnClickListener(this);
        btn_C7.setOnClickListener(this);
        btn_C8.setOnClickListener(this);
        btn_C9.setOnClickListener(this);

        btn_Cmul.setOnClickListener(this);
        btn_Cdiv.setOnClickListener(this);
        btn_Cplus.setOnClickListener(this);
        btn_Csub.setOnClickListener(this);
        btn_Cpoint.setOnClickListener(this);
        btn_Cequ.setOnClickListener(this);
        btn_CBack.setOnClickListener(this);
        btn_CC.setOnClickListener(this);


    }

    private String Calculate() {
        //因为没有括号功能，所以一定是两个数字夹着一个运算符的情况，则先处理负号和小数的情况，然后处理乘除法，最后运算加减法
        if(InputText.length() == 0){
            return "";
        }
        ArrayList<Double> NumAL = new ArrayList<Double>();
        ArrayList<Character> OperatorAL = new ArrayList<Character>();
        Double Num = 0.0;
        Double exponent = 1.0;
        Boolean IfDouble = false;//是否为浮点数
        Boolean IfNegtive = false;//是否为负数
        String ret = "";
        for(int ix=0, Length = InputText.length(); ix < Length; ix++) {
            if(InputText.charAt(ix) == '.'){
                IfDouble = true;
                exponent = 1.0;
                continue;
            }
            if(InputText.charAt(ix)>='0' && InputText.charAt(ix)<='9'){
                if(IfDouble == true){
                    exponent *= 10;
                    Num += (InputText.charAt(ix) - '0')/exponent;
                }else{
                    Num *=10;
                    Num += (InputText.charAt(ix) - '0');
                }
                if(ix == Length - 1) {
                    if(IfNegtive == true){
                        NumAL.add(-1.0 * Num);
                    }else{
                        NumAL.add(Num);
                    }
                }
                continue;
            }
            //以下是符号的情况+,-,*,/
            if(ix == 0 || InputText.charAt(ix-1) == '+' || InputText.charAt(ix-1) == '-' || InputText.charAt(ix-1) == '*' || InputText.charAt(ix-1) == '÷'){
                //负号的情况
                IfNegtive = true;
            }else{
                if(IfNegtive == true){
                    NumAL.add(-1.0 * Num);
                }else{
                    NumAL.add(Num);
                }
                Num = 0.0;
                IfDouble = false;
                IfNegtive = false;
                exponent = 1.0;
                OperatorAL.add( InputText.charAt(ix) );
            }
        }
        /*for (int ix=0; ix<NumAL.size(); ix++){
            System.out.println(NumAL.get(ix));
            System.out.println("\n");
        }
        for (int ix=0; ix<OperatorAL.size(); ix++){
            System.out.println(OperatorAL.get(ix));
            System.out.println("\n");
        }*/
        if(NumAL.size() - OperatorAL.size() != 1){//结构不完张，例如："1+1- ="
            return "#";
        }
        Boolean IfRear = false;//判断记录连乘除是否开始记录
        for(int ix=0,front=0,rear=0; ix< OperatorAL.size(); ix++){//计算乘除法
            if(OperatorAL.get(ix)=='*' || OperatorAL.get(ix)=='÷') {
                if (IfRear == false) {
                    front = ix;
                    rear = ix;
                    IfRear = true;
                }else {
                    rear++;
                }
            }
            if( (OperatorAL.get(ix)=='+' || OperatorAL.get(ix)=='-'|| ix == OperatorAL.size() - 1) && IfRear == true){
                for(int jx = front; jx<=rear; jx++){
                    if(OperatorAL.get(jx)=='*'){
                        NumAL.set(front, NumAL.get(front) * NumAL.get(jx + 1));
                    }else if(NumAL.get(jx + 1) != 0){//除法
                        NumAL.set(front, NumAL.get(front) / NumAL.get(jx + 1));
                    }else {
                        return "ERROR: Divide By Zero";
                    }
                    OperatorAL.set(jx, '+');
                    NumAL.set(jx+1 , 0.0);
                }
                IfRear = false;
            }

        }
        /*for (int ix=0; ix<NumAL.size(); ix++){
            System.out.println(NumAL.get(ix));
            System.out.println(" ");
        }
        System.out.println("\n");
        for (int ix=0; ix<OperatorAL.size(); ix++){
            System.out.println(OperatorAL.get(ix));
            System.out.println(" ");
        }*/

        Double ans = NumAL.get(0);
        for(int ix=0; ix<OperatorAL.size(); ix++){
            if(OperatorAL.get(ix)=='+'){
                ans += NumAL.get(ix + 1);
            }else{//减法
                ans -= NumAL.get(ix + 1);
            }
        }
        //System.out.println(ans);
        return Double.toString(ans);
    }

    private void AnsToInputText(String Ans){//Ans转输入InPutText只需要考虑数字、小数点和负号的情况
        for(int ix=0; ix<Ans.length(); ix++){
            if( Ans.charAt(ix)>='0' && Ans.charAt(ix) <= '9'){
                //按下数字的情况
                InputText += Ans.charAt(ix);
                Point += Point.charAt(Point.length() - 1);
                //输入一个数字后一定可以输入运算符号或者负号
                Operator += "1";
                negtive += "1";
            }
            if(Ans.charAt(ix) == '.' ){
                if( Point.charAt(Point.length() - 1) == '1' ){
                    InputText += ".";
                    Point += "0"; // 小数点后一位不能是小数点或符号
                    negtive += "0";
                    Operator += "0";
                }
                continue;
            }
            if(Ans.charAt(ix) == '-' ) {
                InputText += "-";
                Point += "1";//负号后可以输入小数点
                Operator += "0";// 负号后一位不能是符号
                negtive += "0";
            }
        }
        return ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_cal);
        //init
        initView();
        initEvent();
    }




    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_C0 || v.getId() == R.id.btn_C1 || v.getId() == R.id.btn_C2 || v.getId() == R.id.btn_C3 || v.getId() == R.id.btn_C4 || v.getId() == R.id.btn_C5 || v.getId() == R.id.btn_C6 || v.getId() == R.id.btn_C7 || v.getId() == R.id.btn_C8 || v.getId() == R.id.btn_C9){
            //按下数字的情况
            Point += Point.charAt(Point.length() - 1);
            //输入一个数字后一定可以输入运算符号或者负号
            Operator += "1";
            negtive += "1";
        }
        if(v.getId() == R.id.btn_Cpoint){
            if( Point.charAt(Point.length() - 1) == '1' ){
                InputText += ".";
                Point += "0"; // 小数点后一位不能是小数点或符号
                negtive += "0";
                Operator += "0";
                CScreen.setText(InputText);
            }
            return;
        }

        if(v.getId() == R.id.btn_Cplus || v.getId() == R.id.btn_Csub || v.getId() == R.id.btn_Cmul || v.getId() == R.id.btn_Cdiv){
            if(InputText == "" && AnsExsit == true){
                AnsToInputText( Ans );//将之前算到的答案直接填入字符串InputIndex
            }
            Point += "1";//运算符号或负号后可以输入小数点
            if( Operator.charAt(Operator.length() - 1) == '1'){
                Operator += "0";// 运算符号后一位不能是符号
                negtive += "1";//运算符号后面可以是负号

            }else if(v.getId() == R.id.btn_Csub && negtive.charAt(negtive.length() - 1) == '1'){
                Operator += "0";// 负号后一位不能是符号
                negtive += "0";
            }else{
                return;
            }
        }
        switch (v.getId()) {
            case R.id.btn_C0:
                InputText += "0";
                break;
            case R.id.btn_C1:
                InputText += "1";
                break;
            case R.id.btn_C2:
                InputText += "2";
                break;
            case R.id.btn_C3:
                InputText += "3";
                break;
            case R.id.btn_C4:
                InputText += "4";
                break;
            case R.id.btn_C5:
                InputText += "5";
                break;
            case R.id.btn_C6:
                InputText += "6";
                break;
            case R.id.btn_C7:
                InputText += "7";
                break;
            case R.id.btn_C8:
                InputText += "8";
                break;
            case R.id.btn_C9:
                InputText += "9";
                break;
            case R.id.btn_Cplus:
                InputText += "+";
                break;
            case R.id.btn_Csub:
                InputText += "-";
                break;
            case R.id.btn_Cmul:
                InputText += "*";
                break;
            case R.id.btn_Cdiv:
                InputText += "÷";
                break;
            case R.id.btn_CBack:
                if(InputText.length() != 0){
                    InputText = InputText.substring(0, InputText.length()-1);
                    Operator = Operator.substring(0, Operator.length()-1);
                    Point = Point.substring(0, Point.length()-1);
                    negtive = negtive.substring(0, negtive.length()-1);
                }else{
                    return;
                }
                break;
            case R.id.btn_CC:
                InputText = "";
                Operator = "0";
                Point = "1";
                negtive = "1";
                AnsExsit = false;
                CScreenResault.setText("");
                CScreen.setText("0");
                break;
            case R.id.btn_Cequ:
                if(InputText.length() == 0 && AnsExsit == true ){
                    InputText = "";
                    Operator = "0";
                    Point = "1";
                    negtive = "1";
                    AnsToInputText( Ans );//将之前算到的答案直接填入字符串InputIndex
                    CScreen.setText( InputText );
                }
                if(InputText.length()!=0){
                    String temp = Calculate();
                    if(temp != "#"){//返回#说明式子结构不完张，例如式子"1+1-"
                        Ans = temp;
                        AnsExsit = true;
                        CScreenResault.setText( Ans );
                        InputText = "";
                        Operator = "0";
                        Point = "1";
                        negtive = "1";
                    }
                }
                break;
            default:
                return;
        }

        if(v.getId() != R.id.btn_Cequ && v.getId() != R.id.btn_CC){
            CScreen.setText(InputText);
        }

    }

}

