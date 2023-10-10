@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.calculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme
import com.google.android.gms.wallet.button.ButtonConstants

val mainColor = "#626262".toColor()
val inputColor = "#909090".toColor()
val btnTextColor = "#ffac00".toColor()

fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainArea()
        }
    }
}

@Composable
fun MainArea(){
    val enteredValue = remember {
        mutableStateOf("0")
    }

    Column(
        modifier = Modifier
            .background(mainColor)
            .fillMaxSize()
    ) {
        Output(enteredValue)
        KeyBoard(enteredValue)
    }
}

@Composable
fun Output(enteredValue: MutableState<String>){
    Row(
        modifier = Modifier
            .padding(10.dp)
            .background(inputColor)
            .fillMaxWidth()
            .fillMaxSize(0.5f),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Text(modifier = Modifier.padding(15.dp),
             text = enteredValue.value,
            fontSize = 50.sp)
    }
}

@Composable
fun KeyBoard(enteredValue: MutableState<String>){
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CreateKeyBoardButton(label = "7", enteredValue)
            CreateKeyBoardButton(label = "8", enteredValue)
            CreateKeyBoardButton(label = "9", enteredValue)
            CreateKeyBoardButton(label = "/", enteredValue)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CreateKeyBoardButton(label = "4", enteredValue)
            CreateKeyBoardButton(label = "5", enteredValue)
            CreateKeyBoardButton(label = "6", enteredValue)
            CreateKeyBoardButton(label = "x", enteredValue)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CreateKeyBoardButton(label = "1", enteredValue)
            CreateKeyBoardButton(label = "2", enteredValue)
            CreateKeyBoardButton(label = "3", enteredValue)
            CreateKeyBoardButton(label = "-", enteredValue)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CreateKeyBoardButton(label = "0", enteredValue)
            CreateKeyBoardButton(label = ".", enteredValue)
            CreateKeyBoardButton(label = "+", enteredValue)
            CreateKeyBoardButton(label = "=", enteredValue)
        }
        Row {
            CreateKeyBoardButton(label = "AC", enteredValue)
        }
    }
}

@Composable
fun CreateKeyBoardButton(label: String, enteredValue: MutableState<String>){
    Box(
        modifier = Modifier
            .widthIn(min = 70.dp, max = 90.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    )
    {
        Button(
            //shape = RoundedCornerShape(0.dp),
            onClick = {
                when(label){
                    "AC" -> enteredValue.value = "0"
                    "=" -> DoCalculation(enteredValue)
                    else -> {
                        if (enteredValue.value == "0") enteredValue.value = ""
                        enteredValue.value += label
                        //enteredValue.value += " "
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = btnTextColor,
                containerColor = Color.Black)
        ){
            Text(text = label,
                fontSize = 30.sp)
        }
    }
}

fun DoCalculation(enteredValue: MutableState<String>) {
    val expression = enteredValue.value
    try {
        val result = EvaluateExpression(expression)
        enteredValue.value = result.toString()
    } catch (e: Exception) {
        enteredValue.value = e.toString()
    }
}

fun EvaluateExpression(expression: String): String {
    try {
        val result = Calculate(expression)
        return result.toString()
    } catch (e: Exception) {
        throw e
    }
}

fun Calculate(expression: String): Double {
    val parts = expression.split(Regex("[-+x/]"))
    val operand1 = parts[0].toDouble()
    val operator = expression.substring(parts[0].length, parts[0].length + 1)
    val operand2 = parts[1].toDouble()

    return when (operator) {
        "+" -> operand1 + operand2
        "-" -> operand1 - operand2
        "x" -> operand1 * operand2
        "/" -> operand1 / operand2
        else -> throw IllegalArgumentException("Недопустимый оператор")
    }
}