package com.mydrive.app.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mydrive.app.ui.components.EmptyState
import com.mydrive.app.ui.components.MediaThumb
import com.mydrive.app.ui.theme.ChipShape
import com.mydrive.app.ui.theme.Copper
import com.mydrive.app.ui.theme.Graphite
import com.mydrive.app.ui.theme.Ink
import com.mydrive.app.ui.theme.Ivory
import com.mydrive.app.ui.theme.Mist
import com.mydrive.app.ui.theme.Spacing
import com.mydrive.app.ui.theme.Stroke

@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel,
    onMediaClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var searchOpen by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md, vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gallery",
                style = MaterialTheme.typography.headlineMedium,
                color = Ivory,
                modifier = Modifier.weight(1f)
            )
            IconButtonCircle(icon = if (searchOpen) Icons.Outlined.Close else Icons.Outlined.Search) {
                searchOpen = !searchOpen
                if (!searchOpen) viewModel.setQuery("")
            }
            Spacer(Modifier.size(Spacing.xs))
            IconButtonCircle(icon = Icons.Outlined.FilterList) { }
        }

        if (searchOpen) {
            SearchField(
                value = state.query,
                onValueChange = viewModel::setQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.md, vertical = Spacing.xs)
            )
        }

        FilterRow(
            selected = state.filter,
            onSelect = viewModel::setFilter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md, vertical = Spacing.sm)
        )

        if (state.groups.isEmpty()) {
            EmptyState(
                title = "No media yet",
                message = "Your backed up photos and videos will appear here.",
                icon = Icons.Outlined.PhotoLibrary,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = Spacing.md,
                    end = Spacing.md,
                    bottom = Spacing.lg
                ),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                state.groups.forEach { group ->
                    item(span = { GridItemSpan(3) }, key = "g-${group.label}") {
                        Text(
                            text = group.label,
                            style = MaterialTheme.typography.titleSmall,
                            color = Mist,
                            modifier = Modifier.padding(top = Spacing.md, bottom = Spacing.xs)
                        )
                    }
                    items(group.items, key = { it.id }) { item ->
                        MediaThumb(item = item, onClick = { onMediaClick(item.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    selected: GalleryFilter,
    onSelect: (GalleryFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        GalleryFilter.entries.forEach { filter ->
            val active = filter == selected
            val label = when (filter) {
                GalleryFilter.ALL -> "All"
                GalleryFilter.PHOTOS -> "Photos"
                GalleryFilter.VIDEOS -> "Videos"
                GalleryFilter.FAVORITES -> "Favorites"
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (active) Ink else Ivory,
                modifier = Modifier
                    .clip(ChipShape)
                    .background(if (active) Copper else Graphite)
                    .border(1.dp, if (active) Copper else Stroke, ChipShape)
                    .clickable { onSelect(filter) }
                    .padding(horizontal = Spacing.md, vertical = Spacing.xs)
            )
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(ChipShape)
            .background(Graphite)
            .border(1.dp, Stroke, ChipShape)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Search, contentDescription = null, tint = Mist, modifier = Modifier.size(18.dp))
        Spacer(Modifier.size(Spacing.xs))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Ivory),
            cursorBrush = SolidColor(Copper),
            modifier = Modifier.weight(1f),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text("Search by filename", style = MaterialTheme.typography.bodyMedium, color = Mist)
                }
                inner()
            }
        )
    }
}

@Composable
private fun IconButtonCircle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Graphite)
            .border(1.dp, Stroke, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Ivory, modifier = Modifier.size(20.dp))
    }
}
