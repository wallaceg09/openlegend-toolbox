package com.wallace.tools

import jena.schemagen
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class SchemagenTask : DefaultTask() {
    lateinit var input: File
    lateinit var output: File
    var ontology: Boolean = false
    var noIndividuals: Boolean = false
    var packageName: String? = null

    @TaskAction
    fun generate() {
        val options = mutableListOf<String>("-i", input.absolutePath, "-o", output.absolutePath)

        if(ontology) options.add("--ontology")
        if(noIndividuals) options.add("--noindividuals")
        if(packageName != null) {
            options.add("--package")
            options.add(packageName!!)
        }

        object : schemagen() {
            fun exec(args: Array<String>) {
                this.go(args)
            }
        }.exec(options.toTypedArray())
    }
}