package com.example.learnstack.viewmodels


data class RoadmapWithBoxes(
    val roadmapName: String = "",
    val boxes: List<BoxModel?> = emptyList(),
    val updatedAt: String = "" // Added to track roadmap timestamp
)

data class ResourceItem(
    val type: String,
    val title: String,
    val link: String,
    val updatedAt: String
)

data class TopicModel(
    val title: String = "",
    val description: String = "",
    val resources: List<ResourceItem> = emptyList(),
    val updatedAt: String = "",
    val isBookmarked: Boolean,
)

data class BoxModel(
    val name: String = "",
    val iconResId: Int? = null,
    val topics: List<TopicModel> = emptyList(),
    val updatedAt: String = ""
)
