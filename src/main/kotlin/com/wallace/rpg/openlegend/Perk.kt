package com.wallace.rpg.openlegend

import com.wallace.rpg.openlegend.ontology.Openleged
import org.apache.jena.rdf.model.Model
import java.net.URLEncoder

data class Perk(val name: String, val effect: String) {
    fun toModel(model: Model) {
        val flawResource = model.createResource("ol:${URLEncoder.encode(name, "UTF-8")}", Openleged.Flaw)

        flawResource.addLiteral(Openleged.nameIs, name)
        flawResource.addLiteral(Openleged.effectIs, effect)
    }
}