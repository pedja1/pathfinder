package org.skynetsoftware.pathfinder.cli

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import joptsimple.OptionParser
import joptsimple.OptionSet
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.skynetsoftware.pathfinder.core.model.Map
import org.skynetsoftware.pathfinder.core.model.Result
import org.skynetsoftware.pathfinder.core.solver.AStarSolver
import org.skynetsoftware.pathfinder.core.utils.*
import org.skynetsoftware.pathfinder.rest.PathfinderResourceConfig
import java.io.File
import javax.ws.rs.core.UriBuilder

private const val PARAM_INPUT = "i"
private const val PARAM_OUTPUT = "o"
private const val PARAM_DISPLAY = "s"
private const val PARAM_DAEMON = "d"
private const val PARAM_DAEMON_HOST = "a"
private const val PARAM_DAEMON_PORT = "p"

fun main(args: Array<String>) {
    //parse cli params
    val parser = OptionParser("${PARAM_INPUT}:${PARAM_OUTPUT}:${PARAM_DISPLAY}${PARAM_DAEMON}${PARAM_DAEMON_HOST}:${PARAM_DAEMON_PORT}:h*")
    val optionSet = parser.parse(*args)

    if (optionSet.has(PARAM_DAEMON)) {
        initDaemon(optionSet)
    } else {
        initCli(optionSet)
    }

}

fun initDaemon(optionSet: OptionSet) {
    // Create HttpServer
    var port = "8080"
    var host = "localhost"
    if (optionSet.has(PARAM_DAEMON_PORT)) {
        port = optionSet.valueOf(PARAM_DAEMON_PORT).toString()
    }
    if (optionSet.has(PARAM_DAEMON_HOST)) {
        host = optionSet.valueOf(PARAM_DAEMON_HOST).toString()
    }
    val serverLocal = GrizzlyHttpServerFactory.createHttpServer(UriBuilder.fromUri("http://$host:$port").build(), PathfinderResourceConfig(), false)

    serverLocal.start()
}

fun initCli(optionSet: OptionSet) {
    try {
        //init timer to measure exec time
        val timer = Timer()

        val inputPath = optionSet.valueOf(PARAM_INPUT) as String?
                ?: throw IllegalStateException("input cannot be null")
        val input = File(inputPath)
        assertFileReadable(input)

        val outputPath = optionSet.valueOf(PARAM_OUTPUT) as String?
        var output: File?
        if (outputPath == null) {
            output = createDefaultOut()
        } else {
            output = File(outputPath)
            if (!isFileReadable(output))
                output = createDefaultOut()
        }

        //parse input html
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())

        //solve and measure time
        timer.start()

        val solver = AStarSolver({ mapper.readValue<Map>(input, Map::class.java) })
        solver.solve()

        val time = timer.end()

        //create mapper from output json
        val jsonMapper = ObjectMapper()
        jsonMapper.registerModule(KotlinModule())

        //convert result to josn
        val result = Result()
        result.executonTimeInMs = time

        result.path = solver.buildPath(true)

        //write json
        jsonMapper.writeValue(output, result)

        println("Output written to: ${output.absolutePath}")

        //display result as image
        if (optionSet.has(PARAM_DISPLAY)) {
            val bufferedImage = generateImage(solver.map!!, solver.rows, solver.cols, solver.startNode, solver.endNode)
            displayImage("Solved", bufferedImage)
        }
    } catch (e: Exception) {
        println(e.message)
    }
}

fun createDefaultOut(): File {
    return File("", "output.json")
}




