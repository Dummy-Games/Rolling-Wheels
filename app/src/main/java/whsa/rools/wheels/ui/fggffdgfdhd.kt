package whsa.rools.wheels.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import whsa.rools.wheels.R
import whsa.rools.wheels.util.gsdsgdgsdg
import whsa.rools.wheels.util.hgjrdfgd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random
import kotlin.random.nextInt

class fggffdgfdhd : Fragment(R.layout.fdgffsdf) {

    private var fdgfdgd: Job? = null
    private var bbfdghgfnfhgf = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gfdhhfrehfd(view)
        view.findViewById<Button>(R.id.button).setOnClickListener { gfdhhfrehfd(view) }
    }

    private fun gfdhhfrehfd(view: View) {
        fdgfdgd?.cancel()
        with(view) {
            val ivContainer = findViewById<FrameLayout>(R.id.flImages)
            fdgfdgd = viewLifecycleOwner.lifecycleScope.launch {
                listOf<ImageView>(
                    findViewById(R.id.iv1),
                    findViewById(R.id.iv2),
                    findViewById(R.id.iv3),
                    findViewById(R.id.iv4),
                    findViewById(R.id.iv5),
                ).hgjrdfgd { fdhhfdgrdfgfdh(it, ivContainer) }.forEach {
                    it.join()
                }
                gfdhhfrehfd(view)
                Toast.makeText(requireContext(), "You won!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun fdhhfdgrdfgfdh(iv: ImageView, container: FrameLayout): Job {
        iv.isVisible = true
        iv.setImageResource(requireContext().gsdsgdgsdg("s" + Random.nextInt(1..5)))
        iv.x = container.width * Random.nextInt(20) * 0.04f
        iv.y = container.height * Random.nextInt(20) * 0.04f
        iv.animate().x((if (Random.nextBoolean()) 1 else -1) * 1000f).y((if (Random.nextBoolean()) 1 else -1) * 1000f).setDuration(5_000L).start()
        return withContext(Dispatchers.Main) { launch { fdgfdhtdgdcvbgfn(iv) } }
    }

    private suspend fun fdgfdhtdgdcvbgfn(iv: ImageView) = suspendCoroutine<Unit> { cont ->
        iv.setOnClickListener {
            bbfdghgfnfhgf += 100
            view?.findViewById<TextView>(R.id.tvScore)?.text = "SCORE: $bbfdghgfnfhgf"
            iv.isVisible = false
            cont.resume(Unit)
        }
    }
}
