package ru.nyxsed.postscan.presentation.screens.postsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.domain.models.GroupEntity

@Composable
fun GroupChip(
    group: GroupEntity,
    postCount: Int,
    isSelected: Boolean,
    onChipClicked: () -> Unit,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val textColor = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary

    val displayedText = if (group.name.length > 10) {
        group.name.substring(0, 10) + "..."
    } else {
        group.name
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .border(1.dp, color = textColor, shape = RoundedCornerShape(16.dp))
            .clickable { onChipClicked() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(4.dp),
                contentAlignment = Alignment.Center,
            )
            {
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(25.dp)
                        .alpha(0.4f),
                    model = group.avatarUrl,
                    placeholder = painterResource(R.drawable.ic_placeholder),
                    contentDescription = null,
                )
                Text(
                    text = postCount.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    style = TextStyle.Default.copy(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2f,2f),
                            blurRadius = 3f
                        )
                    )
                )
            }
            Text(
                modifier = Modifier
                    .padding(4.dp),
                text = displayedText.uppercase(),
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}