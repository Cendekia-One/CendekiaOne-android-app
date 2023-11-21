package com.capstone.cendekiaone.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseInputField(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    value: String = "",
    onChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp),
        value = value,
        onValueChange = onChange,
        label = { Text(placeholder) },
        trailingIcon =
        if (value.isNotEmpty()) {
            {
                IconButton(onClick = { onChange("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null )
                }
            }
        } else {
            null
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun BaseInputPrev(
) {
    var text by rememberSaveable { mutableStateOf("") }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(16.dp)) {

        BaseInputField(
            placeholder = "Masukan Passowrd",
            onChange = { text = it },
            value = text,

        )
    }


}