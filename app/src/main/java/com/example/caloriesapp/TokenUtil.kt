import android.util.Base64
import org.json.JSONObject

class TokenUtils {

    companion object {
        fun getUserInfoFromToken(accessToken: String): Map<String, String?>? {
            try {
                println(accessToken)

                // Split the token into three parts (header, payload, signature)
                val tokenParts = accessToken.split(".")

                // Check if the token has three parts (header, payload, and signature)
                if (tokenParts.size != 3) {
                    throw IllegalArgumentException("Invalid token format")
                }

                // Decode the payload (second part of the token)
                val decodedBytes = Base64.decode(tokenParts[1], Base64.URL_SAFE or Base64.NO_PADDING)
                val decodedString = String(decodedBytes, Charsets.UTF_8)

                println(decodedString)

                // Parse the decoded string as JSON
                val jsonObject = JSONObject(decodedString)

                // Extract the user info
                val userId = jsonObject.optString("userId")
                val email = jsonObject.optString("email")
                val name = jsonObject.optString("name")

                // Return the extracted information as a map
                return mapOf("userId" to userId, "email" to email, "name" to name)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}
