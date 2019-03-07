package com.wallace.rpg.openlegend

import com.wallace.rpg.openlegend.ontology.Openleged
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.yaml.snakeyaml.Yaml
import java.io.*
import java.net.URL

fun main(args: Array<String>) {
    val yaml = Yaml()

    val boons = getBoons(yaml)
    boonsToFile(boons)

    val banes = getBanes(yaml)
    banesToFile(banes)

    val feats = getFeats(yaml)
    featsToFile(feats)

    val perksFlawsInputStream =
        URL("https://raw.githubusercontent.com/wallaceg09/core-rules/master/core/src/05-perks-flaws/01-chapter-five.md")
            .openStream()

    parsePerksAndFlaws(perksFlawsInputStream)

//    val featPrereqTypes = feats.map { it.prerequisites.map { (it.value as Map<String, Any>).keys }.flatten() }.flatten().distinct()
//    println(featPrereqTypes)


}

fun getBoons(yaml: Yaml): List<Boon> {
    val boonStream = URL("https://raw.githubusercontent.com/wallaceg09/core-rules/master/boons/boons.yml").openStream()

    return yaml.load<List<Map<String, Any>>>(boonStream).map {
        Boon(it)
    }
}

fun boonsToFile(boons: List<Boon>) {
    val boonModel = ModelFactory.createDefaultModel().apply {
        setNsPrefix("ol", Openleged.NS)
    }

    boons.forEach {
        it.toModel(boonModel)
    }

    saveModel("boons.ttl", boonModel)
}

fun getBanes(yaml: Yaml): List<Bane> {
    val baneStream = URL("https://raw.githubusercontent.com/wallaceg09/core-rules/master/banes/banes.yml").openStream()

    return yaml.load<List<Map<String, Any>>>(baneStream).map {
        Bane(it)
    }
}

fun banesToFile(banes: List<Bane>) {
    val baneModel = ModelFactory.createDefaultModel().apply {
        setNsPrefix("ol", Openleged.NS)
    }

    banes.forEach {
        it.toModel(baneModel)
    }

    saveModel("banes.ttl", baneModel)
}

fun getFeats(yaml: Yaml): List<Feat> {
    val featStream = URL("https://raw.githubusercontent.com/wallaceg09/core-rules/master/feats/feats.yml").openStream()

    return yaml.load<List<Map<String, Any>>>(featStream).map {
        Feat(it)
    }
}

fun featsToFile(feats: List<Feat>) {
    val featModel = ModelFactory.createDefaultModel().apply {
        setNsPrefix("ol", Openleged.NS)
    }

    feats.forEach {
        it.toModel(featModel)
    }

    saveModel("feats.ttl", featModel)
}

fun saveModel(filename: String, model: Model, lang: Lang = Lang.TTL) {
    val file = File("build/turtle/$filename").apply {
        parentFile.mkdirs()
    }

    RDFDataMgr.write(FileOutputStream(file), model, lang)
}

private fun getYamlStream(filename: String): InputStream {
    return Yaml::class.java.classLoader.getResource(filename).openStream()
}

private enum class PerkParseState {
    NEITHER, IN_PERK, IN_FLAW
}

fun parsePerksAndFlaws(stream: InputStream) {
    val perkLines = mutableListOf<String>()
    val flawLines = mutableListOf<String>()

    var state = PerkParseState.NEITHER

    BufferedReader(InputStreamReader(stream)).useLines {
        val iterator = it.iterator()

        iterator.forEachRemaining { line ->
            when (state) {
                PerkParseState.IN_PERK -> perkLines.add(line)
                PerkParseState.IN_FLAW -> flawLines.add(line)
                else -> {
                }
            }
            if (line.contains("## Perk Descriptions")) state = PerkParseState.IN_PERK
            else if (line.contains("## Flaw Descriptions")) state = PerkParseState.IN_FLAW
        }
    }

    val perks = mutableListOf<Perk>()
    val flaws = mutableListOf<Flaw>()

    val nameRegex = """### (?<name>\w+[\s\w]*)""".toRegex()
    val effectRegx = """(?<effect>\w+[\s\w\p{Punct}]*)""".toRegex()

    var name: String? = null
    var effect: String? = null

    for (perk in perkLines) {
        if (perk.isBlank()) continue

        val nameMatch = nameRegex.find(perk)
        val effectMatch = effectRegx.find(perk)

        val matchedName = nameMatch?.groups?.get("name")?.value
        val matchedEffect = effectMatch?.groups?.get("effect")?.value

        if (matchedName != null) {
            name = matchedName
        } else if (matchedEffect != null) {
            effect = matchedEffect
            perks.add(Perk(name!!, effect))
        } else {
            println("Dis fucked: $perk")
        }
    }

    for (flaw in flawLines) {
        if (flaw.isBlank()) continue

        val nameMatch = nameRegex.find(flaw)
        val effectMatch = effectRegx.find(flaw)

        val matchedName = nameMatch?.groups?.get("name")?.value
        val matchedEffect = effectMatch?.groups?.get("effect")?.value

        if (matchedName != null) {
            name = matchedName
        } else if (matchedEffect != null) {
            effect = matchedEffect
            flaws.add(Flaw(name!!, effect))
        } else {
            println("Dis fucked: $flaw")
        }
    }

    val perkModel = ModelFactory.createDefaultModel().apply {
        setNsPrefix("ol", Openleged.NS)
    }

    perks.forEach {
        it.toModel(perkModel)
    }

    saveModel("perks.ttl", perkModel)

    val flawModel = ModelFactory.createDefaultModel().apply {
        setNsPrefix("ol", Openleged.NS)
    }

    flaws.forEach {
        it.toModel(flawModel)
    }

    saveModel("flaws.ttl", flawModel)
}