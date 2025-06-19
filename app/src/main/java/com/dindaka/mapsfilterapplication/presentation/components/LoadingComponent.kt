package com.dindaka.mapsfilterapplication.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoadingComponent(@StringRes text: Int) {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally).testTag("loader"))
            Text(
                stringResource(text),
                modifier = Modifier.padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}