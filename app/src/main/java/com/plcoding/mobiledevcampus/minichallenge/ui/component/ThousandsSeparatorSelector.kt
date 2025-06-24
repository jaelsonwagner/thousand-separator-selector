package com.plcoding.mobiledevcampus.minichallenge.ui.component

import androidx.annotation.FloatRange
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.mobiledevcampus.minichallenge.R
import com.plcoding.mobiledevcampus.minichallenge.domain.ThousandSeparator
import com.plcoding.mobiledevcampus.minichallenge.ui.theme.ThousandsSeparatorPickerTheme
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf


@Composable
internal fun ThousandsSeparatorSelector(
    options: ImmutableSet<ThousandSeparator>,
    selectedOption: ThousandSeparator?,
    onOptionSelected: (ThousandSeparator) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(options.isNotEmpty()) { "Options cannot be empty" }

    Column(modifier = modifier.padding(16.dp)) {
        SelectorTitle()
        SelectorOptions(options, selectedOption, onOptionSelected)
    }
}

@Composable
private fun SelectorTitle() {
    Text(
        text = stringResource(R.string.thousands_separator),
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun SelectorOptions(
    options: ImmutableSet<ThousandSeparator>,
    selectedOption: ThousandSeparator?,
    onOptionSelected: (ThousandSeparator) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp)
    ) {
        SlidingBackground(options, selectedOption)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            options.forEach { option ->
                val isOptionSelected = option == selectedOption
                SelectorOption(option, isOptionSelected, onOptionSelected)
            }
        }
    }
}

@Composable
private fun SlidingBackground(
    options: ImmutableSet<ThousandSeparator>,
    selectedOption: ThousandSeparator?,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    backgroundShape: RoundedCornerShape = RoundedCornerShape(12.dp)
) {
    // TODO: Is there a better way to calculate the item width?
    //  I would like to avoid this hard-coded 40.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val slidingBackgroundWidth by remember {
        mutableStateOf((screenWidthDp.dp - 40.dp) / options.size)
    }

    val selectedIndex = options.indexOf(selectedOption).coerceAtLeast(0)

    // x-axel sliding animation for the background
    val xOffset by animateDpAsState(
        targetValue = slidingBackgroundWidth * selectedIndex.coerceAtLeast(0),
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "background_sliding_animation"
    )

    Box(
        modifier = modifier
            .offset(x = xOffset)
            .size(width = slidingBackgroundWidth, height = 44.dp)
            .background(color = backgroundColor, shape = backgroundShape)
    )
}

@Composable
private fun RowScope.SelectorOption(
    option: ThousandSeparator,
    isOptionSelected: Boolean,
    onOptionSelected: (ThousandSeparator) -> Unit,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 2.0) selectedOptionTextScale: Float = 1.2f
) {
    Box(
        modifier = modifier
            .weight(1f)
            .height(44.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onOptionSelected(option) }
            ),
        contentAlignment = Alignment.Center
    ) {
        val animatedTextScale by animateFloatAsState(
            targetValue = if (isOptionSelected) selectedOptionTextScale else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        Text(
            text = option.value,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = if (isOptionSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isOptionSelected) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onPrimary.copy(alpha = 7.0f),
            modifier = Modifier.scale(animatedTextScale)
        )
    }
}

@Preview
@Composable
private fun ThousandSeparatorSelectorPreview() {
    val thousandSeparatorOptions = persistentSetOf(
        ThousandSeparator("1.000"),
        ThousandSeparator("1,000"),
        ThousandSeparator("1 000"),
    )
    val selectedThousandSeparator = thousandSeparatorOptions.last()

    ThousandsSeparatorPickerTheme(
        dynamicColor = false
    ) {
        Surface {
            ThousandsSeparatorSelector(
                options = thousandSeparatorOptions,
                selectedOption = selectedThousandSeparator,
                onOptionSelected = {},
            )
        }
    }
}
