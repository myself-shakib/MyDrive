package com.mydrive.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object Spacing {
    val xxs = 4.dp
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 24.dp
    val xxl = 32.dp
    val xxxl = 40.dp
}

object Radius {
    val sm = 10.dp
    val md = 14.dp
    val lg = 18.dp
    val xl = 24.dp
    val pill = 100.dp
}

val CardShape = RoundedCornerShape(Radius.lg)
val MediaShape = RoundedCornerShape(Radius.md)
val ChipShape = RoundedCornerShape(Radius.pill)
val SheetShape = RoundedCornerShape(topStart = Radius.xl, topEnd = Radius.xl)
