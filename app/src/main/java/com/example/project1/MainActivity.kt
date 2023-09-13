package com.example.project1

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Stack
class MainActivity : AppCompatActivity() {
    private val currentOrientation = resources.configuration.orientation
    private lateinit var b0: Button
    private lateinit var b1: Button
    private lateinit var b2: Button
    private lateinit var b3: Button
    private lateinit var b4: Button
    private lateinit var b5: Button
    private lateinit var b6: Button
    private lateinit var b7: Button
    private lateinit var b8: Button
    private lateinit var b9: Button
    private lateinit var decimal: Button
    private lateinit var clear: Button
    private lateinit var plusMinus: Button
    private lateinit var percent: Button
    private lateinit var divide: Button
    private lateinit var multiply: Button
    private lateinit var minus: Button
    private lateinit var add: Button
    private lateinit var equals: Button
    private lateinit var sin: Button
    private lateinit var cos: Button
    private lateinit var tan: Button
    private lateinit var log10: Button
    private lateinit var ln: Button
    private var operandStack = Stack<String>()
    private var operatorStack = Stack<String>()
    private lateinit var btnNumbers: List<Button>
    private lateinit var btnOperators: List<Button>
    private lateinit var inputText: TextView
    private var currentInput = ""
    private var currentOp = ""


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("OPERAND_STACK", operandStack)
        outState.putSerializable("OPERATOR_STACK", operatorStack)
        outState.putString("CURRENT_INPUT", currentInput)
        outState.putString("PENDING_OP", currentOp)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {

            // Extract bundle
            val outState = savedInstanceState

            // Restore state
            /*operandStack = outState.getSerializable("OPERAND_STACK") as? Stack<String> ?: Stack()
            operatorStack = outState.getSerializable("OPERATOR_STACK") as? Stack<String> ?: Stack()
            currentInput = outState.getString("CURRENT_INPUT", "")
            currentOp = outState.getString("PENDING_OP", "")*/

            operandStack = outState.getSerializable("OPERAND_STACK") as? Stack<String> ?: Stack()
            operatorStack = outState.getSerializable("OPERATOR_STACK") as? Stack<String> ?: Stack()
            currentInput = outState.getString("CURRENT_INPUT", "") ?: ""
            currentOp = outState.getString("PENDING_OP", "") ?: ""





        }

        btnNumbers = listOf(b0, b1, b2, b3, b4, b5, b6, b7, b8, b9)
        btnOperators = listOf(percent, divide, multiply, minus, add, sin, cos, tan, log10, ln, plusMinus, percent)
        inputText = findViewById(R.id.tvResults)

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main)
        } else {
            setContentView(R.layout.activity_main)
        }
        btnNumbers.forEach { btn ->
            btn.setOnClickListener {
                inputText.text = inputText.text.toString() + btn.text
                currentInput = inputText.text.toString()
            }
        }
        btnOperators.forEach { btn ->
            btn.setOnClickListener {
                if(inputText.text.isNotEmpty()){
                    currentOp = btn.text.toString()
                    operatorStack.push(btn.text.toString())
                    operandStack.push(currentInput)
                    currentInput = ""
                }
            }
        }

        decimal.setOnClickListener {
            if(currentInput.isNotEmpty()){
                currentInput += decimal.text
            }
        }

        equals.setOnClickListener {
            val result = 0.0
            val op = operatorStack.pop()
            while(!operatorStack.isEmpty()) {
                if (isBinaryOperation(op)) {
                    val operand1 = operandStack.pop()
                    val operand2 = operandStack.pop()
                    val result = operate(op, operand1.toBigDecimal(), operand2.toBigDecimal())
                } else {
                    val operand = operandStack.pop()
                    val result = operate(op, operand.toBigDecimal())
                }
            }
            val scale = 2
            val roundingMode = RoundingMode.HALF_UP
            val resultString = result.toBigDecimal().setScale(scale, roundingMode).toString()
            inputText.text = resultString
        }
        clear.setOnClickListener {
            inputText.text = ""
            currentOp = ""
            currentInput = ""
            operandStack.removeAllElements()
            operatorStack.removeAllElements()
        }
    }

    private fun operate(op: String, vararg operands: BigDecimal): Double {
        return when(op) {
            "+" -> {
                (operands[0] + operands[1]).toDouble()
            }

            "-" -> {
                (operands[0] - operands[1]).toDouble()
            }

            "×" -> {
                (operands[0] * operands[1]).toDouble()
            }
            "/" -> {
                (operands[0] / operands[1]).toDouble()
            }
            "sin" -> {
                kotlin.math.sin(operands[0].toDouble())
            }
            "cos" -> {
                kotlin.math.cos(operands[0].toDouble())
            }
            "tan" -> {
                kotlin.math.tan(operands[0].toDouble())
            }
            "Log 10" -> {
                kotlin.math.log10(operands[0].toDouble())
            }
            "ln" -> {
                kotlin.math.ln(operands[0].toDouble())
            }
            "+/-" -> {
                (operands[0].toDouble() * -1)
            }
            "%" -> {
                (operands[0].toDouble() / 100)
            }
            else -> {
                throw IllegalArgumentException("Invalid operation $op")
            }
        }
    }
    private fun isBinaryOperation(op: String): Boolean{
        return when(op){
            "+","-","×","/" -> true
            else -> false
        }
    }
}