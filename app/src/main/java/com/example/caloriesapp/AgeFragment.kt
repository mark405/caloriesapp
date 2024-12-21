import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.caloriesapp.R
import com.example.caloriesapp.UserOptionsActivity

class AgeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_age, container, false)

        val ageInput: EditText = view.findViewById(R.id.ageInput)
        val nextButton: Button = view.findViewById(R.id.nextAgeButton)

        nextButton.setOnClickListener {
            val age = ageInput.text.toString()
            if (age.isNotEmpty()) {
                val parentActivity = activity as? UserOptionsActivity
                parentActivity?.userAge = age
                // Navigate to GenderFragment
                parentActivity?.viewPager?.currentItem = 2
            } else {
                Toast.makeText(requireContext(), "Please enter your age", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}
