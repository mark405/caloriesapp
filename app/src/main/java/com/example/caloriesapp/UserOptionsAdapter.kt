import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.caloriesapp.GoalFragment
import com.example.caloriesapp.ActivityFragment
import com.example.caloriesapp.HeightFragment

class UserOptionsAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        GenderFragment(),
        AgeFragment(),
        HeightFragment(),
        WeightFragment(),
        GoalFragment(),
        ActivityFragment(),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
