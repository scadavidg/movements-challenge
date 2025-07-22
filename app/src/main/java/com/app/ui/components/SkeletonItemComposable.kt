package com.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.ui.height12
import com.app.ui.height16
import com.app.ui.height20
import com.app.ui.height8
import com.app.ui.horizontalPadding16
import com.app.ui.padding16
import com.app.ui.width60

@Composable
fun SkeletonItemComposable() {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .horizontalPadding16(),
        shape = RoundedCornerShape(12.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Row(
            modifier = Modifier.padding16(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.6f)
                            .height16()
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp)),
                )
                Spacer(modifier = Modifier.height8())
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(0.4f)
                            .height12()
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp)),
                )
            }

            Box(
                modifier =
                    Modifier
                        .width60()
                        .height20()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp)),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkeletonItemComposablePreview() {
    SkeletonItemComposable()
}
