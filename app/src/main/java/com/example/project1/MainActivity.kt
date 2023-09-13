package com.example.project1
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.math.RoundingMode
import java.util.Stack



class MainActivity : AppCompatActivity() {
    private var operandStack = Stack<String>()
    private var operatorStack = Stack<String>()
    private lateinit var btnNumbers: List<Button>
    private lateinit var btnOperators: List<Button>
    private lateinit var inputText: TextView
    private var currentInput = ""
    private var currentOp = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate() started")
        if (savedInstanceState != null) {
            // Extract bundle
            // Restore state
            operandStack = savedInstanceState.getSerializable("OPERAND_STACK") as Stack<String>
            operatorStack = savedInstanceState.getSerializable("OPERATOR_STACK") as Stack<String>
            currentInput = savedInstanceState.getSerializable("CURRENT_INPUT").toString()
            currentOp = savedInstanceState.getSerializable("PENDING_OP").toString()
        }
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){

            // Load landscape layout
            setContentView(R.layout.activity_main)
            btnOperators = listOf(findViewById<Button>(R.id.bPlusMinus), findViewById<Button>(R.id.bPercent), findViewById<Button>(R.id.bDivide), findViewById<Button>(R.id.bMultiply), findViewById<Button>(R.id.bSubtract), findViewById<Button>(R.id.bPlus), findViewById<Button>(R.id.bSin), findViewById<Button>(R.id.bCos), findViewById<Button>(R.id.bTan), findViewById<Button>(R.id.bLog10), findViewById<Button>(R.id.bLn))
            Log.d("ButtonOperatorIDs", btnOperators.toString())
            btnOperators.forEach { id ->
                Log.d("ButtonNumberIDs", id.toString())
            }
        } else {

            // Load portrait layout
            setContentView(R.layout.activity_main)
            btnOperators = listOf(findViewById<Button>(R.id.bPlusMinus), findViewById<Button>(R.id.bPercent), findViewById<Button>(R.id.bDivide), findViewById<Button>(R.id.bMultiply), findViewById<Button>(R.id.bSubtract), findViewById<Button>(R.id.bPlus))
            Log.d("ButtonOperatorIDs", btnOperators.toString())
            btnOperators.forEach { id ->
                Log.d("ButtonNumberIDs", id.toString())
            }
        }
        val decimal = findViewById<Button>(R.id.bDecimal)
        val clear = findViewById<Button>(R.id.bClear)
        val equals = findViewById<Button>(R.id.bEquals)
        btnNumbers = listOf(findViewById<Button>(R.id.b0), findViewById<Button>(R.id.b1),findViewById<Button>(R.id.b2),findViewById<Button>(R.id.b3),findViewById<Button>(R.id.b4),findViewById<Button>(R.id.b5),findViewById<Button>(R.id.b6),findViewById<Button>(R.id.b7),findViewById<Button>(R.id.b8),findViewById<Button>(R.id.b9))
        Log.d("ButtonNumberIDs", btnNumbers.toString())
        btnNumbers.forEach { id ->
            Log.d("ButtonNumberIDs", id.toString())
        }

        inputText = findViewById(R.id.tvResults)

        if(btnNumbers != null) {
            btnNumbers.forEach { btn ->
                btn.setOnClickListener {
                    inputText.text = inputText.text.toString() + btn.text
                    currentInput = inputText.text.toString()

                }
            }
        }

        if(btnOperators != null) {
            btnOperators.forEach { btn ->
                btn.setOnClickListener {
                    Log.d("OperandStack", "${operandStack.size}")
                    Log.d("OperatorStack", "${operatorStack.size}")
                    currentOp = btn.text.toString()
                    operatorStack.push(btn.text.toString())
                    operandStack.push(currentInput)
                    currentInput = ""
                    inputText.text = ""
                    Log.d("OperandStack", "${operandStack.size}")
                    Log.d("OperatorStack", "${operatorStack.size}")
                }
            }
        }

        decimal?.setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput += "."
                inputText.text = inputText.text.toString() + "."
            }
        }


        equals?.setOnClickListener {
            operandStack.push(currentInput)
            var result = 0.0
            Log.d("OperandStack", "${operandStack.size}")
            Log.d("OperatorStack", "${operatorStack.size}")
            while(operatorStack.isNotEmpty()) {
                val op = operatorStack.pop()
                if (isBinaryOperation(op)) {
                    val operand1 = operandStack.pop()
                    val operand2 = operandStack.pop()
                    result = operate(op, operand1.toDouble(), operand2.toDouble())
                    Log.d("OperandStack", "${operandStack.size}")
                    Log.d("OperatorStack", "${operatorStack.size}")
                } else {
                    val operand = operandStack.pop()
                    result = operate(op, operand.toDouble())
                    Log.d("OperandStack", "${operandStack.size}")
                    Log.d("OperatorStack", "${operatorStack.size}")
                }
            }
            Log.d("OperandStack", "${operandStack.size}")
            Log.d("OperatorStack", "${operatorStack.size}")
            val scale = 2
            val roundingMode = RoundingMode.HALF_UP
            val resultString = result.toString()
            inputText.text = resultString
            currentOp = ""
            currentInput = ""

        }
        clear.setOnClickListener {
            inputText.text = ""
            currentOp = ""
            currentInput = ""
            operatorStack.removeAllElements()
        }
    }



    private fun operate(op: String, vararg operands: Double): Double {
        return when(op) {
            "+" -> {
                (operands[0] + operands[1]).toDouble()
            }

            "-" -> {
                (operands[1] - operands[0]).toDouble()
            }

            "×" -> {
                (operands[0] * operands[1]).toDouble()
            }
            "/" -> {
                (operands[1] / operands[0]).toDouble()
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
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("OPERAND_STACK", operandStack)
        outState.putSerializable("OPERATOR_STACK", operatorStack)
        outState.putString("CURRENT_INPUT", currentInput)
        outState.putString("PENDING_OP", currentOp)
    }
    private fun isBinaryOperation(op: String): Boolean{
        return when(op){
            "+","-","×","/" -> true
            else -> false
        }
    }


}




