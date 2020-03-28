package com.fightcorona.main.feedback_adapter

import com.fightcorona.util.BaseUiModel

sealed class FeedbackItemType(val feedbackTypeEnum: FeedbackTypeEnum) : BaseUiModel

data class FeedbackItem(
    val description: String,
    val date: String
) : FeedbackItemType(FeedbackTypeEnum.FEEDBACK)

enum class FeedbackTypeEnum {
    FEEDBACK
}