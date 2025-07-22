package com.app.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Typography
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domain.models.TransactionType
import java.text.SimpleDateFormat
import java.util.Locale

fun Double.toCurrencyString(withSymbol: Boolean = false): String = if (withSymbol) "$${"%,.2f".format(this)}" else "%,.2f".format(this)

fun TransactionType.toColor(): Color =
    when (this) {
        TransactionType.INCOME -> Color(0xFF51BF72)
        TransactionType.EXPENSE -> Color(0xFFBF5153)
        else -> Color.Unspecified
    }

fun String.toFriendlyDate(): String =
    try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = parser.parse(this)
        if (date != null) formatter.format(date) else this
    } catch (e: Exception) {
        this
    }

fun Modifier.horizontalPadding16(): Modifier = this.then(Modifier.padding(horizontal = 16.dp))

fun Modifier.verticalPadding4(): Modifier = this.then(Modifier.padding(vertical = 4.dp))

fun Modifier.padding16(): Modifier = this.then(Modifier.padding(16.dp))

fun Modifier.height4(): Modifier = this.then(Modifier.height(4.dp))

fun Modifier.height8(): Modifier = this.then(Modifier.height(8.dp))

fun Modifier.height12(): Modifier = this.then(Modifier.height(12.dp))

fun Modifier.height16(): Modifier = this.then(Modifier.height(16.dp))

fun Modifier.height20(): Modifier = this.then(Modifier.height(20.dp))

fun Modifier.height24(): Modifier = this.then(Modifier.height(24.dp))

fun Modifier.width60(): Modifier = this.then(Modifier.width(60.dp))

val Typography.boldBodyLarge: TextStyle
    get() = this.bodyLarge.copy(fontWeight = FontWeight.Bold)

val Typography.normalBodyLarge: TextStyle
    get() = this.bodyLarge.copy(fontWeight = FontWeight.Normal)

val Typography.boldTitleMedium: TextStyle
    get() = this.titleMedium.copy(fontWeight = FontWeight.Bold)

val Typography.boldDisplayLarge: TextStyle
    get() = this.displayLarge.copy(fontWeight = FontWeight.Bold, fontSize = 36.sp)

val Typography.smallBodySmall: TextStyle
    get() = this.bodySmall
