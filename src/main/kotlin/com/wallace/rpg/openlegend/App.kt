package com.wallace.rpg.openlegend

import org.yaml.snakeyaml.Yaml
import java.io.InputStream

fun main(args: Array<String>) {
    val yaml = Yaml()

    val boons = getBoons(yaml)
    println(boons)

    val banes = getBanes(yaml)
    println(banes)

    val feats = getFeats(yaml)
    println(feats)

    val featPrereqTypes = feats.map { it.prerequisites.map { (it.value as Map<String, Any>).keys }.flatten() }.flatten().distinct()
    println(featPrereqTypes)
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