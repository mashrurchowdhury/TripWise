package com.example.tripwise.ui.common

import android.text.TextUtils
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    onValueChanged: (String) -> Unit,
    label: String,
    error: String = "",
    value: String = "",
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var text by remember {
        mutableStateOf(value)
    }
    Column() {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onValueChanged(it)},
            label = { Text(label) },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth().padding(start = 50.dp, end = 50.dp)

        )
        if(!TextUtils.isEmpty(error) && isError) {
            ShowErrorText(error)
        }

    }

}

@Composable
fun ShowErrorText(text:String){
    Text(
        text = text,
        color = Color.Red,
        modifier = Modifier.fillMaxWidth().height(50.dp).padding(start = 50.dp, end = 50.dp)
    )
}