package com.wallace.rpg.openlegend

data class Boon(private val map: Map<String, Any>) {
    val name: String by map
    val tags: List<String> by map
    val power: List<Int> by map
    val attribute: List<String> by map
    val invocationTime: String by map
    val duration: String by map
    val description: String by map
    val effect: String by map
    val special: String? by map
}