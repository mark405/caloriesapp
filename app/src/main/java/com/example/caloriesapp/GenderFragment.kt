import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.caloriesapp.R
import com.example.caloriesapp.UserOptionsActivity

class GenderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gender, container, false)

        val maleButton: Button = view.findViewById(R.id.maleButton)
        val femaleButton: Button = view.findViewById(R.id.femaleButton)

        maleButton.setOnClickListener {
            saveGender("Чоловік")
        }

        femaleButton.setOnClickListener {
            saveGender("Жінка")
        }

        return view
    }

    private fun saveGender(gender: String) {
        val parentActivity = activity as? UserOptionsActivity
        parentActivity?.userGender = gender

        parentActivity?.viewPager?.currentItem = 1
    }
}
