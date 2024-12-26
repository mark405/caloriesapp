import android.app.Application
import com.example.caloriesapp.network.RetrofitInstance

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitInstance.init(this)
    }
}
