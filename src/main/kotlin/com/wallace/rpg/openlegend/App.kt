package com.wallace.rpg.openlegend

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
//    println(boons)

    val banes = getBanes(yaml)
//    println(banes)

    banes.forEach {
        try {
            it.attackAttributes
        } catch (e: Exception) {
            println(it.name)
        }
    }

    val feats = getFeats(yaml)
//    println(feats)

//    val featPrereqTypes = feats.map { it.prerequisites.map { (it.value as Map<String, Any>).keys }.flatten() }.flatten().distinct()
//    println(featPrereqTypes)

    val baneModels = banes.map(Bane::toModel)

    val baneModel = ModelFactory.createDefaultModel()

    baneModels.forEach {
        baneModel.add(it)
    }

    val banesFile = File("src/main/resources/banes.ttl")

    RDFDataMgr.write(FileOutputStream(banesFile), baneModel, Lang.TTL)
}

fun getBoons(yaml: Yaml): List<Boon> {
    val boonStream = getYamlStream("boons.yml")

    return yaml.load<List<Map<String, Any>>>(boonStream).map {
        Boon(it)
    }
}

fun getBanes(yaml: Yaml): List<Bane> {
    val baneStream = getYamlStream("banes.yml")

    return yaml.load<List<Map<String, Any>>>(baneStream).map {
        Bane(it)
    }
}

fun getFeats(yaml: Yaml): List<Feat> {
    val featStream = getYamlStream("feats.yml")

    return yaml.load<List<Map<String, Any>>>(featStream).map {
        Feat(it)
    }
}

private fun getYamlStream(filename: String): InputStream {
    return Yaml::class.java.classLoader.getResource(filename).openStream()
}

private fun getResourceFile(filename: String): File {
    return File(Yaml::class.java.classLoader.getResource(filename).file)
}