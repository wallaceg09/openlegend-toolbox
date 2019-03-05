package com.wallace.rpg.openlegend

import com.wallace.rpg.openlegend.ontology.Openleged
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun main(args: Array<String>) {
    val yaml = Yaml()

    val boons = getBoons(yaml)
    boonsToFile(boons)

    val banes = getBanes(yaml)
    banesToFile(banes)

    val feats = getFeats(yaml)
    featsToFile(feats)

//    val featPrereqTypes = feats.map { it.prerequisites.map { (it.value as Map<String, Any>).keys }.flatten() }.flatten().distinct()
//    println(featPrereqTypes)


}

fun getBoons(yaml: Yaml): List<Boon> {
    val boonStream = getYamlStream("boons.yml")

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
    val baneStream = getYamlStream("banes.yml")

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
    val featStream = getYamlStream("feats.yml")

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
    val file = File("src/main/resources/$filename")

    RDFDataMgr.write(FileOutputStream(file), model, lang)
}

private fun getYamlStream(filename: String): InputStream {
    return Yaml::class.java.classLoader.getResource(filename).openStream()
}

private fun getResourceFile(filename: String): File {
    return File(Yaml::class.java.classLoader.getResource(filename).file)
}