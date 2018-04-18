package org.skynetsoftware.pathfinder.core.utils

import org.skynetsoftware.pathfinder.core.model.Map
import org.skynetsoftware.pathfinder.core.model.Node
import java.awt.BorderLayout
import java.awt.Color
import java.awt.image.BufferedImage
import javax.swing.*

fun generateImage(map: Map, rows: Int, cols: Int, startNode: Node, endNode: Node): BufferedImage
{
    //generate new image based on grid size
    //we multiply by 100 to get visible image
    val bufferedImage = BufferedImage(rows * 100, cols * 100, BufferedImage.TYPE_INT_RGB)
    val graphic = bufferedImage.graphics

    //draw available cells, since image is black by default everything else will be black
    graphic.color = Color.WHITE
    for (cel in map.cells)
    {
        graphic.fillRect(cel.col * 100, cel.row * 100, 100, 100)
    }

    //draw start and end points
    graphic.color = Color.YELLOW
    graphic.fillRect(map.startPoint.col * 100, map.startPoint.row * 100, 100, 100)
    graphic.fillRect(map.endPoint.col * 100, map.endPoint.row * 100, 100, 100)

    //draw solution
    graphic.color = Color.GREEN
    var currentDrawingNode = endNode.parent
    while (currentDrawingNode != null && currentDrawingNode != startNode)
    {
        graphic.fillRect(currentDrawingNode.col * 100, currentDrawingNode.row * 100, 100, 100)

        currentDrawingNode = currentDrawingNode.parent
    }

    //draw grid lines
    graphic.color = Color.BLACK
    for (row in 0..rows)
    {
        graphic.drawLine(0, row * 100, bufferedImage.width, row * 100)
    }
    for (col in 0..cols)
    {
        graphic.drawLine(col * 100, 0, col * 100, bufferedImage.height)
    }
    return bufferedImage
}

fun displayImage(title: String, image: BufferedImage)
{
    SwingUtilities.invokeLater {
        val editorFrame = JFrame(title)
        editorFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        val imageIcon = ImageIcon(image)
        val jLabel = JLabel()
        jLabel.icon = imageIcon
        editorFrame.contentPane.add(jLabel, BorderLayout.CENTER)

        editorFrame.pack()
        editorFrame.setLocationRelativeTo(null)
        editorFrame.isVisible = true
    }
}

