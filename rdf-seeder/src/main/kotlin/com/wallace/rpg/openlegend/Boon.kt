package com.wallace.rpg.openlegend

import com.wallace.rpg.openlegend.ontology.Openleged
import org.apache.jena.rdf.model.Model
import java.net.URLEncoder

data class Boon(private val map: Map<String, Any>) {
    val name: String by map
    val tags: List<String> by map
    val power: List<Int> by map
    val attribute: List<String> by map
    val invocationTime: String by map
    val duration: String by map
    val description: String by map
    val effect: String by map
    val special: String? by map.withDefault { null }

    fun toModel(model: Model) {
        val boonResource = model.createResource("ol:${URLEncoder.encode(name, "UTF-8")}", Openleged.Boon)

        tags.forEach {
            boonResource.addLiteral(Openleged.hasTag, it)
        }

        power.forEach {
            boonResource.addLiteral(Openleged.hasPower, it)
        }

        attribute.forEach {
            boonResource.addLiteral(Openleged.hasAttribute, it)
        }

        boonResource.addLiteral(Openleged.invocationTimeIs, invocationTime)

        boonResource.addLiteral(Openleged.durationIs, duration)

        boonResource.addLiteral(Openleged.descriptionIs, description)

        boonResource.addLiteral(Openleged.effectIs, effect)

        if (special != null) boonResource.addLiteral(Openleged.specialIs, special)
    }
}