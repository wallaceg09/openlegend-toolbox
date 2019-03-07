package com.wallace.rpg.openlegend

import com.wallace.rpg.openlegend.ontology.Openleged
import org.apache.jena.rdf.model.Model
import java.net.URLEncoder

data class Feat(private val map: Map<String, Any>) {
    private val nullDefaultMap = map.withDefault { null }

    val name: String by map
    val prerequisites: Map<String, Any> by map
    val tags: List<String>? by nullDefaultMap
    val cost: List<Int> by map
    val description: String by map
    val effect: String by map
    val special: String? by nullDefaultMap

    fun toModel(model: Model) {
        val featResource = model.createResource("ol:${URLEncoder.encode(name, "UTF-8")}", Openleged.Feat)

        if (tags != null) {
            tags!!.forEach {
                featResource.addLiteral(Openleged.hasTag, it)
            }
        }

        // TODO: Prerequisites

        cost.forEach {
            featResource.addLiteral(Openleged.hasCost, it)
        }

        featResource.addLiteral(Openleged.descriptionIs, description)

        featResource.addLiteral(Openleged.effectIs, effect)

        if (special != null) featResource.addLiteral(Openleged.specialIs, special)
    }
}