package org.skynetsoftware.pathfinder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.skynetsoftware.pathfinder.core.model.Cell
import org.skynetsoftware.pathfinder.core.model.Map
import org.skynetsoftware.pathfinder.core.model.Path

const val SELECTION_TYPE_START = 0
const val SELECTION_TYPE_END = 1
const val SELECTION_TYPE_BLOCK = 2

class PathView : View {
    private val paint: Paint
    var rowCount: Int = 4
        set(value) {
            field = value
            recalculate()
            invalidate()
        }
    var colCount: Int = 4
        set(value) {
            field = value
            recalculate()
            invalidate()
        }

    private var cellSize: Int = 0
    private var gridWidth: Float = 0f
    private var gridHeight: Float = 0f
    private var gridX: Float = 0f
    private var gridY: Float = 0f

    var selectionType: Int = SELECTION_TYPE_START

    private var startRect: RectF? = null
    private var endRect: RectF? = null
    private var blockedRects: MutableList<RectF> = ArrayList()
    private var pathRects: MutableList<RectF> = ArrayList()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        paint = Paint()
    }

    private fun recalculate() {
        if (width == 0 || height == 0)
            return
        if (rowCount > colCount) {
            cellSize = height / rowCount
        } else {
            cellSize = width / colCount
        }
        gridWidth = (colCount * cellSize).toFloat()
        gridHeight = (rowCount * cellSize).toFloat()
        gridX = ((width - gridWidth) / 2)
        gridY = ((height - gridHeight) / 2)
        startRect = null
        endRect = null
        blockedRects.clear()
        pathRects.clear()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        recalculate()
    }

    override fun onDraw(canvas: Canvas) {

        if (startRect != null) {
            paint.color = Color.RED
            paint.style = Paint.Style.FILL
            canvas.drawRect(startRect, paint)
        }

        if (endRect != null) {
            paint.color = Color.GREEN
            paint.style = Paint.Style.FILL
            val rx = gridX + (((endRect!!.left - gridX) / cellSize).toInt() * cellSize).toFloat()
            val ry = gridY + (((endRect!!.top - gridY) / cellSize).toInt() * cellSize).toFloat()
            canvas.drawRect(rx, ry, rx + cellSize, ry + cellSize, paint)
        }

        for (rect in blockedRects) {
            paint.color = Color.BLACK
            paint.style = Paint.Style.FILL
            val rx = gridX + (((rect.left - gridX) / cellSize).toInt() * cellSize).toFloat()
            val ry = gridY + (((rect.top - gridY) / cellSize).toInt() * cellSize).toFloat()
            canvas.drawRect(rx, ry, rx + cellSize, ry + cellSize, paint)
        }

        for (rect in pathRects) {
            paint.color = Color.CYAN
            paint.style = Paint.Style.FILL
            val rx = gridX + (((rect.left - gridX) / cellSize).toInt() * cellSize).toFloat()
            val ry = gridY + (((rect.top - gridY) / cellSize).toInt() * cellSize).toFloat()
            canvas.drawRect(rx, ry, rx + cellSize, ry + cellSize, paint)
        }

        paint.color = Color.BLACK
        for (row in 0..rowCount) {
            canvas.drawLine(gridX, gridY + (row * cellSize).toFloat(), gridX + gridWidth, gridY + (row * cellSize).toFloat(), paint)
        }
        for (col in 0..colCount) {
            canvas.drawLine(gridX + (col * cellSize).toFloat(), gridY, gridX + (col * cellSize).toFloat(), gridY + gridHeight, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.x < gridX || event.x > gridX + gridWidth || event.y < gridY || event.y > gridY + gridHeight) {
            return super.onTouchEvent(event)
        }
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            pathRects.clear()
            when (selectionType) {
                SELECTION_TYPE_START -> {
                    updateRect(startRect, event, { rectF ->  startRect = rectF})
                    invalidate()
                }
                SELECTION_TYPE_END -> {
                    updateRect(endRect, event, { rectF ->  endRect = rectF})
                    invalidate()
                }
                SELECTION_TYPE_BLOCK -> {
                    val rx = gridX + (((event.x - gridX) / cellSize).toInt() * cellSize).toFloat()
                    val ry = gridY + (((event.y - gridY) / cellSize).toInt() * cellSize).toFloat()
                    val tmpRect = RectF()
                    tmpRect.set(rx, ry, rx + cellSize, ry + cellSize)
                    if(!blockedRects.contains(tmpRect)) {
                        blockedRects.add(tmpRect)
                    } else {
                        blockedRects.remove(tmpRect)
                    }
                    invalidate()
                }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun updateRect(rect: RectF?, event: MotionEvent, setRect: (RectF?) -> Unit) {
        val rx = gridX + (((event.x - gridX) / cellSize).toInt() * cellSize).toFloat()
        val ry = gridY + (((event.y - gridY) / cellSize).toInt() * cellSize).toFloat()
        if (rect == null) {
            val tmpRect = RectF(rx, ry, rx + cellSize, ry + cellSize)
            setRect(tmpRect)
        } else {
            if (rect.contains(event.x, event.y)) {
                setRect(null)
            } else {
                rect.set(rx, ry, rx + cellSize, ry + cellSize)
            }
        }
    }

    fun generateMap(): Map?
    {
        if(startRect == null || endRect == null)
            return null
        val tmpRect = RectF()
        val cells = ArrayList<Cell>()
        for(row in 0 until rowCount) {
            for (col in 0 until colCount) {
                val x = gridX + (col * cellSize).toFloat()
                val y = gridY + (row * cellSize).toFloat()
                tmpRect.set(x, y, x + cellSize, y + cellSize)
                if(blockedRects.contains(tmpRect)) {
                    continue
                }
                cells.add(Cell(row, col))
            }
        }
        return Map(cells, Cell(((startRect!!.top - gridY) / cellSize).toInt(), ((startRect!!.left - gridX) / cellSize).toInt()), Cell(((endRect!!.top - gridY) / cellSize).toInt(), ((endRect!!.left - gridX) / cellSize).toInt()))
    }

    fun setPath(path: Path)
    {
        pathRects.clear()
        for(point in path.points)
        {
            val x = gridX + point.col * cellSize
            val y = gridY + point.row * cellSize
            val rect = RectF(x, y, x + cellSize, y + cellSize)
            pathRects.add(rect)
        }
        invalidate()
    }
}