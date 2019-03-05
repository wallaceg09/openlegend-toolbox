package com.wallace.rpg.openlegend

import com.wallace.rpg.openlegend.ontology.Openleged
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.ResourceFactory
import java.net.URLEncoder

data class Bane(private val map: Map<String, Any>) {
    val name: String by map
    val tags: List<String> by map
    val power: List<Int> by map
    val attackAttributes: List<String> by map
    val attack: List<String> by map
    val invocationTime: String by map
    val duration: String by map
    val description: String by map
    val effect: String by map
    val special: String? by map.withDefault { null }

    private fun scrubString(string: String) = URLEncoder.encode(string, "UTF-8")

    fun toModel(model: Model) {
        val baneResource = model.createResource("ol:${scrubString(name)}", Openleged.Bane)

        tags.forEach {
            baneResource.addLiteral(Openleged.hasTag, it)
        }

        power.forEach {
            baneResource.addLiteral(Openleged.hasPower, it)
        }

        attackAttributes.forEach {
            val attrResource = model.createResource("ol:${scrubString(it)}")
            baneResource.addProperty(Openleged.hasAttackAttribute, attrResource)
        }

        attack.forEach {
            baneResource.addLiteral(Openleged.hasAttack, it)
        }

        baneResource.addLiteral(Openleged.invocationTimeIs, invocationTime)

        baneResource.addLiteral(Openleged.durationIs, duration)

        baneResource.addLiteral(Openleged.descriptionIs, description)

        baneResource.addLiteral(Openleged.effectIs, effect)

        if (special != null) baneResource.addLiteral(Openleged.specialIs, special)
    }
}