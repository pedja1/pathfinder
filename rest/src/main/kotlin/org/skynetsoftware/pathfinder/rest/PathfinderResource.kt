package org.skynetsoftware.pathfinder.rest

import org.skynetsoftware.pathfinder.core.model.Map
import org.skynetsoftware.pathfinder.core.model.Result
import org.skynetsoftware.pathfinder.core.solver.AStarSolver
import org.skynetsoftware.pathfinder.core.utils.Timer
import org.skynetsoftware.pathfinder.core.utils.generateImage
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/pathfinder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PathfinderResource
{
    @POST
    @Path("/json")
    @Consumes(MediaType.APPLICATION_JSON)
    fun findPathDisplayJson(map: Map): Response
    {
        val timer = Timer()
        //solve and measure time
        timer.start()

        val solver = AStarSolver({ map })
        solver.solve()

        val time = timer.end()

        //convert result to josn
        val result = Result()
        result.executonTimeInMs = time

        result.path = solver.buildPath(true)
        return successResponse(data = result)
    }

    @POST
    @Path("/image")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces("image/png")
    fun findPathDisplayImage(map: Map): Response
    {
        val solver = AStarSolver({ map })
        solver.solve()

        val bufferedImage = generateImage(solver.map!!, solver.rows, solver.cols, solver.startNode, solver.endNode)
        return Response.ok().entity(bufferedImage).build()
    }

}