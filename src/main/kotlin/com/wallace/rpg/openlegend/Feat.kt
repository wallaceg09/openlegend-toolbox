package com.wallace.rpg.openlegend

data class Feat(private val map: Map<String, Any>) {
    val name: String by map
    val prerequisites: Map<String, Any> by map
    val tags: List<String> by map
    val cost: Int by map
    val description: String by map
    val effect: String by map
    val special: String? by map
}