package org.skynetsoftware.pathfinder.core.solver

import org.skynetsoftware.pathfinder.core.AStarSolverException
import org.skynetsoftware.pathfinder.core.model.Cell
import org.skynetsoftware.pathfinder.core.model.Map
import org.skynetsoftware.pathfinder.core.model.Node

//http://homepages.abdn.ac.uk/f.guerin/pages/teaching/CS1013/practicals/aStarTutorial.htm
private const val MOVEMENT_COST = 10

/**
 * Modified A* Algorithm implementation for 2d square-cells grid without diagonal jumps*/
class AStarSolver(readMap: () -> Map)
{

    val map: Map
    val rows: Int
    val cols: Int

    val startNode: Node
    val endNode: Node

    private val grid: Array<Array<Node?>>

    init
    {
        //read input
        map = readMap()
        if (map.cells.isEmpty())
        {
            throw AStarSolverException("Input map has no cells")
        }

        //calculate row and column count by adding row/column to list, sorting the list, and getting last number
        rows = map.cells.map { it.row }.toList().sorted().last() + 1
        cols = map.cells.map { it.col }.toList().sorted().last() + 1

        //create grid with all null values
        grid = Array(rows, { arrayOfNulls<Node>(cols) })

        //fill grid with values from input map
        for (cell in map.cells)
        {
            grid[cell.row][cell.col] = Node(null, cell.row, cell.col)
        }

        //validate start and end nodes
        assertStartEndPoint(grid, map.startPoint)
        assertStartEndPoint(grid, map.endPoint)

        startNode = Node(null, map.startPoint.row, map.startPoint.col)
        endNode = Node(null, map.endPoint.row, map.endPoint.col)
    }

    /**
     * */
    fun solve()
    {
        //lists for keeping track of checked nodes
        val openNodes: MutableList<Node> = ArrayList()
        val closedNodes: MutableList<Node> = ArrayList()

        //add start node to open nodes
        openNodes.add(startNode)

        var foundTarget = false

        //loop trough currently open nodes
        while (openNodes.isNotEmpty())
        {
            val currentNode = openNodes.removeAt(0)
            closedNodes.add(currentNode)

            //get adjacent nodes, only vertical and horizontal, this alg doesn't support vertical "jumps"
            val left = getLeftAdjacentNode(currentNode, grid)
            val right = getRightAdjacentNode(currentNode, grid)
            val top = getTopAdjacentNode(currentNode, grid)
            val bottom = getBottomAdjacentNode(currentNode, grid)

            //check each node
            checkNode(currentNode, left, endNode, openNodes, closedNodes)
            checkNode(currentNode, right, endNode, openNodes, closedNodes)
            checkNode(currentNode, top, endNode, openNodes, closedNodes)
            checkNode(currentNode, bottom, endNode, openNodes, closedNodes)

            //if current node is end node, we found the path, yay
            if (currentNode == endNode)
            {
                foundTarget = true
                endNode.parent = currentNode.parent
                break
            }
        }
        if (!foundTarget)
            throw AStarSolverException("Path not found")
    }

    /**
     * Main function that updates current node*/
    private fun checkNode(currentNode: Node, checkingNode: Node?, endNode: Node, openNodes: MutableList<Node>, closedNodes: MutableList<Node>)
    {
        if (checkingNode == null || closedNodes.contains(checkingNode))
            return

        if (openNodes.contains(checkingNode))
        {
            //if node is already in opeNodes, check if new path is better than old one by comparing 'G'
            val currentG = currentNode.G
            val newG = checkingNode.G
            if (newG < currentG)
                checkingNode.parent = currentNode
        }
        else
        {
            openNodes.add(checkingNode)
            checkingNode.parent = currentNode
        }
        //calculate G and H values
        checkingNode.G = checkingNode.parent!!.G + MOVEMENT_COST
        checkingNode.H = calculateHeuristic(checkingNode, endNode)

        //sort openNodes so that first node is always the best one (one with lowest 'F')
        openNodes.sortWith(compareBy { it.F })
    }

    /**
     * Calculate 'heuristic' by using 'Manhattan' function.
     * This simply calculates straight vertical jumps plus straight horizontal jumps and multiplies result by MOVEMENT_COST*/
    private fun calculateHeuristic(checkingNode: Node, endNode: Node): Int
    {
        val horizontal = Math.abs(endNode.row - checkingNode.row)
        val vertical = Math.abs(endNode.col - checkingNode.col)
        return (vertical + horizontal) * MOVEMENT_COST
    }

    /**
     * Get the node that is left of the current node, or null if cell is blocked or out of grid*/
    private fun getLeftAdjacentNode(currentNode: Node, grid: Array<Array<Node?>>): Node?
    {
        return grid.getOrNull(currentNode.row - 1)?.getOrNull(currentNode.col)
    }

    /**
     * Get the node that is right of the current node, or null if cell is blocked or out of grid*/
    private fun getRightAdjacentNode(currentNode: Node, grid: Array<Array<Node?>>): Node?
    {
        return grid.getOrNull(currentNode.row + 1)?.getOrNull(currentNode.col)
    }

    /**
     * Get the node that is top of the current node, or null if cell is blocked or out of grid*/
    private fun getTopAdjacentNode(currentNode: Node, grid: Array<Array<Node?>>): Node?
    {
        return grid.getOrNull(currentNode.row)?.getOrNull(currentNode.col - 1)
    }

    /**
     * Get the node that is bottom of the current node, or null if cell is blocked or out of grid*/
    private fun getBottomAdjacentNode(currentNode: Node, grid: Array<Array<Node?>>): Node?
    {
        return grid.getOrNull(currentNode.row)?.getOrNull(currentNode.col + 1)
    }

    private fun assertStartEndPoint(grid: Array<Array<Node?>>, cell: Cell)
    {
        if (grid.getOrNull(cell.row)?.getOrNull(cell.col) == null)
        {
            throw AStarSolverException("Invalid start/end point")
        }
    }
}