package presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.Dialog

@Composable
fun AddNewPlacesDialog(
    closeDialog: (String) -> Unit
){
    var inputPlace by remember { mutableStateOf("") }


    Box(
        Modifier.fillMaxSize()
    ) {
        Dialog(
            onDismissRequest = {closeDialog(inputPlace)}
        ) {
            TextField(
                value = inputPlace,
                onValueChange = { inputPlace = it },
                label = { Text("Add new place :") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {closeDialog(inputPlace)}),
                modifier = Modifier.onKeyEvent {
                    if (it.key == Key.Enter){
                        closeDialog(inputPlace) }
                    false
                }
                )
        }
    }
}