package io.github.mjcorless.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mjcorless.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                Surface(

                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

//region composable tags

private val defaultTip = 15.0

@Composable
fun TipTimeScreen() {
    var amountInput by remember { mutableStateOf("") }
    var amountTip by remember { mutableStateOf("15.0") }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = amountTip.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount = amount, tipPercent = tipPercent)

    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))
        EditNumberField(cost = amountInput,
            onValueChange = {
                if (isValidInt(it)) {
                    amountInput = it
                }
            }
        )
        TipField(tip = amountTip,
            onValueChange = {
                if (isValidInt(it)) {
                    amountTip = it
                }
            })
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.tip_amount, tip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun EditNumberField(
    cost: String, onValueChange: (String) -> Unit
) {
    TextField(
        value = cost,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.cost_of_service)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun TipField(
    tip: String, onValueChange: (String) -> Unit
) {
    TextField(
        value = tip,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.tip_percent)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

//endregion

//region helper functions
private fun calculateTip(amount: Double, tipPercent: Double = defaultTip): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

private fun isValidInt(input: String): Boolean {
    if (input.isEmpty() || input.toDoubleOrNull() != null) {
        val periodIndex = input.lastIndexOf(".")
        if (periodIndex == -1 || (input.length - periodIndex < 4)) {
            return true
        }
    }

    return false
}

//endregion

//region preview
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalculatorTheme {
        TipTimeScreen()
    }
}

//endregion