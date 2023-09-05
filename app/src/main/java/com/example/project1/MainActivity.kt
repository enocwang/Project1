import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.ArithmeticException


class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private var currentInput = ""
    private var result = 0.0
    private var currentOperator = ""
    private var isNewInput = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
    }

    // Implement methods for button clicks and calculations

    fun onDigit(view: View) {
        if (textView.text == "0") {
            textView.text = ""
        }
        textView.append((view as Button).text)
        isNewInput = true
    }

    fun onBksp(view: View) {
        if (textView.text.isNotBlank()) {
            textView.text = textView.text.substring(0, textView.text.length - 1)
        }
    }

    fun onClear(view: View) {
        textView.text = "0"
        currentInput = ""
        result = 0.0
        currentOperator = ""
        isNewInput = true
    }

    fun decimalPoint(view: View) {
        if (isNewInput && !currentInput.contains(".")) {
            textView.append(".")
            isNewInput = false
        }
    }

    fun onOperator(view: View) {
        if (isNewInput) {
            val operator = (view as Button).text.toString()
            currentInput = textView.text.toString()
            currentOperator = operator
            isNewInput = false
        }
    }

    fun onEqual(view: View) {
        if (!isNewInput) {
            val secondNumber = textView.text.toString()
            try {
                val num1 = currentInput.toDouble()
                val num2 = secondNumber.toDouble()
                val resultValue = when (currentOperator) {
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    "*" -> num1 * num2
                    "/" -> num1 / num2
                    else -> num2
                }
                textView.text = formatResult(resultValue)
                currentInput = textView.text.toString()
                currentOperator = ""
                isNewInput = true
            } catch (e: ArithmeticException) {
                textView.text = "Error"
            }
        }
    }

    private fun formatResult(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }
}
