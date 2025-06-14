package com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.city_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dindaka.mapsfilterapplication.R
import com.dindaka.mapsfilterapplication.data.model.CityData

@Composable
fun CityListScreen(
    onItemClick: (CityData) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val list = listOf(
        CityData(1, "1", "a", 123.223, 1231.12312323, true),
        CityData(2, "2", "a", 123.223, 1231.12312323, true),
        CityData(3, "3", "a", 123.223, 1231.12312323, true),
        CityData(4, "4", "a", 123.223, 1231.12312323, true),
        CityData(5, "5", "a", 123.223, 1231.12312323, true)
    )
    Column(
        Modifier
            .systemBarsPadding()
            .fillMaxSize()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { searchQuery = "" },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.clear),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            placeholder = { Text(stringResource(R.string.filter)) },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(list) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onItemClick(item) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.name, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    }
}
