import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.caloriesapp.Step1Fragment
import com.example.caloriesapp.Step2Fragment
import com.example.caloriesapp.Step3Fragment

class UserOptionsAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        Step1Fragment(),
        Step2Fragment(),
        Step3Fragment(),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Step1Fragment()
            1 -> Step2Fragment()
            2 -> Step3Fragment()
            else -> Step1Fragment()
        }
    }}
