package org.skynetsoftware.pathfinder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.skynetsoftware.pathfinder.net.PathfinderRestApi
import org.skynetsoftware.pathfinder.net.PathfinderService
import org.skynetsoftware.pathfinder.net.RestApi

class MainActivity : AppCompatActivity() {

    val restApi: RestApi<PathfinderService> = PathfinderRestApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCols.addTextChangedListener(object: TextWatcherAdapter()
        {
            override fun afterTextChanged(s: Editable?) {
                if(etCols.text.toString().isNotEmpty()) {
                    pathView.colCount = etCols.text.toString().toInt()
                }
            }
        })

        etRows.addTextChangedListener(object: TextWatcherAdapter()
        {
            override fun afterTextChanged(s: Editable?) {
                if(etRows.text.toString().isNotEmpty()) {
                    pathView.rowCount = etRows.text.toString().toInt()
                }
            }
        })

        etCols.setText(pathView.colCount.toString())
        etRows.setText(pathView.rowCount.toString())

        rgOptions.setOnCheckedChangeListener({group, checkedId ->
            when(checkedId) {
                R.id.rbStart -> pathView.selectionType = SELECTION_TYPE_START
                R.id.rbEnd -> pathView.selectionType = SELECTION_TYPE_END
                R.id.rbBlocked -> pathView.selectionType = SELECTION_TYPE_BLOCK
            }
        })
    }

    fun solve(view: View) {

    }
}
