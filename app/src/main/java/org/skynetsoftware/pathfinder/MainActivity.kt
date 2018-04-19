package org.skynetsoftware.pathfinder

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.skynetsoftware.pathfinder.core.AStarSolverException
import org.skynetsoftware.pathfinder.core.model.Result
import org.skynetsoftware.pathfinder.core.solver.AStarSolver
import org.skynetsoftware.pathfinder.core.utils.Timer
import org.skynetsoftware.pathfinder.net.MyResponse
import org.skynetsoftware.pathfinder.net.PathfinderRestApi
import org.skynetsoftware.pathfinder.net.PathfinderService
import org.skynetsoftware.pathfinder.net.RestApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity()
{
    private val restApi: RestApi<PathfinderService> = PathfinderRestApi()

    private val progressDialog: ProgressDialog by lazy {
        val pb = ProgressDialog(this)
        pb.setMessage(getString(R.string.please_wait))
        return@lazy pb
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCols.addTextChangedListener(object : TextWatcherAdapter()
        {
            override fun afterTextChanged(s: Editable?)
            {
                if (etCols.text.toString().isNotEmpty())
                {
                    pathView.colCount = etCols.text.toString().toInt()
                }
            }
        })

        etRows.addTextChangedListener(object : TextWatcherAdapter()
        {
            override fun afterTextChanged(s: Editable?)
            {
                if (etRows.text.toString().isNotEmpty())
                {
                    pathView.rowCount = etRows.text.toString().toInt()
                }
            }
        })

        etCols.setText(pathView.colCount.toString())
        etRows.setText(pathView.rowCount.toString())

        rgOptions.setOnCheckedChangeListener({ group, checkedId ->
            when (checkedId)
            {
                R.id.rbStart -> pathView.selectionType = SELECTION_TYPE_START
                R.id.rbEnd -> pathView.selectionType = SELECTION_TYPE_END
                R.id.rbBlocked -> pathView.selectionType = SELECTION_TYPE_BLOCK
            }
        })
    }

    fun findPath(view: View)
    {
        val timerTotal = Timer()
        val timerCalculation = Timer()
        timerTotal.start()
        val map = pathView.generateMap() ?: return
        if (rgFindOptions.checkedRadioButtonId == R.id.rbLocaly)
        {
            try
            {
                timerCalculation.start()
                val solver = AStarSolver({ map })
                solver.solve()//TODO do on background thread
                pathView.setPath(solver.buildPath(false))
                timerCalculation.end()
                val endCalc = timerCalculation.end()
                val endTotal = timerTotal.end()
                tvTimeCalculation.text = getString(R.string.time_calculation_, endCalc)
                tvTimeTotal.text = getString(R.string.time_total_, endTotal)
            }
            catch (e: AStarSolverException)
            {
                showToast(e.message)
            }
        }
        else
        {
            progressDialog.show()
            restApi.service.findPath(map).enqueue(object : Callback<MyResponse<Result>>
            {
                override fun onFailure(call: Call<MyResponse<Result>>?, t: Throwable?)
                {
                    showToast(t?.message)
                    progressDialog.dismiss()
                }

                override fun onResponse(call: Call<MyResponse<Result>>?, response: Response<MyResponse<Result>>)
                {
                    val path = response.body()?.data?.path
                    if(path != null)
                    {
                        if(path.points.size > 2)
                        {
                            path.points.removeAt(path.points.size - 1)
                            path.points.removeAt(0)
                        }
                        pathView.setPath(path)
                        timerTotal.end()
                        tvTimeTotal.text = getString(R.string.time_total_, timerTotal.end())
                        tvTimeCalculation.text = getString(R.string.time_calculation_, response.body()?.data?.executonTimeInMs)
                    }
                    else
                    {
                        showToast(response.body()?.message)
                    }
                    progressDialog.dismiss()
                }

            })
        }

    }

    private fun showToast(message: String?)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
