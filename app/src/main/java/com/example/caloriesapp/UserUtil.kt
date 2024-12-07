import android.util.Base64
import com.example.caloriesapp.network.RetrofitInstance.appContext
import org.json.JSONObject

class UserUtil {

    companion object {
        fun getUserInfoFromToken(): Map<String, String?>? {
            try {
                val token = appContext?.let { SharedPreferencesManager.getAccessToken(it) }

                val tokenParts = token?.split(".")

                if (tokenParts != null) {
                    if (tokenParts.size != 3) {
                        throw IllegalArgumentException("Invalid token format")
                    }
                }

                val decodedBytes = Base64.decode(tokenParts?.get(1), Base64.URL_SAFE or Base64.NO_PADDING)
                val decodedString = String(decodedBytes, Charsets.UTF_8)

                println(decodedString)

                val jsonObject = JSONObject(decodedString)

                val userId = jsonObject.optString("userUuid")
                val email = jsonObject.optString("email")
                val name = jsonObject.optString("firstName")

                return mapOf("userUuid" to userId, "email" to email, "firstName" to name)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}
