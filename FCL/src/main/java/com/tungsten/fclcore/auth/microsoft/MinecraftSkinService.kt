package com.tungsten.fclcore.auth.microsoft

import com.tungsten.fclcore.auth.AuthenticationException
import com.tungsten.fclcore.auth.ServerDisconnectException
import com.tungsten.fclcore.auth.ServerResponseMalformedException
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.io.FileUtils
import com.tungsten.fclcore.util.io.HttpMultipartRequest
import com.tungsten.fclcore.util.io.NetworkUtils
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

/**
 * Service for Minecraft skin and cape operations via the Minecraft API.
 * These endpoints use the Minecraft access token (obtained via Microsoft authentication).
 *
 * @see <a href="https://wiki.vg/Mojang_API">Mojang API Documentation</a>
 */
object MinecraftSkinService {

    private const val BASE_URL = "https://api.minecraftservices.com/minecraft/profile"

    /**
     * Upload a new skin from a local file.
     *
     * @param accessToken Minecraft access token (Bearer)
     * @param model       skin model: "classic" for Steve, "slim" for Alex
     * @param file        path to the skin PNG file
     * @throws AuthenticationException on API errors
     */
    @JvmStatic
    fun uploadSkin(accessToken: String, model: String, file: Path) {
        try {
            val con = NetworkUtils.createHttpConnection(URL("$BASE_URL/skins"))
            con.requestMethod = "POST"
            con.setRequestProperty("Authorization", "Bearer $accessToken")
            con.doOutput = true
            HttpMultipartRequest(con).use { request ->
                request.param("variant", model)
                Files.newInputStream(file).use { fis ->
                    request.file(
                        "file",
                        FileUtils.getName(file),
                        "image/${FileUtils.getExtension(file)}",
                        fis
                    )
                }
            }
            val responseCode = con.responseCode
            val response = NetworkUtils.readData(con)
            if (responseCode / 100 != 2) {
                throw ServerResponseMalformedException("Failed to upload skin: HTTP $responseCode - $response")
            }
            LOG.info("Skin uploaded successfully, model: $model")
        } catch (e: IOException) {
            throw ServerDisconnectException(e)
        }
    }

    /**
     * Reset the active skin (remove custom skin, revert to default).
     *
     * @param accessToken Minecraft access token (Bearer)
     * @throws AuthenticationException on API errors
     */
    @JvmStatic
    fun resetSkin(accessToken: String) {
        try {
            val con = NetworkUtils.createHttpConnection(URL("$BASE_URL/skins/active"))
            con.requestMethod = "DELETE"
            con.setRequestProperty("Authorization", "Bearer $accessToken")
            val responseCode = con.responseCode
            if (responseCode / 100 != 2) {
                val response = NetworkUtils.readData(con)
                throw ServerResponseMalformedException("Failed to reset skin: HTTP $responseCode - $response")
            }
            LOG.info("Skin reset successfully")
        } catch (e: IOException) {
            throw ServerDisconnectException(e)
        }
    }

    /**
     * Show a specific cape by setting it as active.
     *
     * @param accessToken Minecraft access token (Bearer)
     * @param capeId      the UUID of the cape to show
     * @throws AuthenticationException on API errors
     */
    @JvmStatic
    fun showCape(accessToken: String, capeId: String) {
        try {
            val payload = """{"capeId":"$capeId"}""".toByteArray(Charsets.UTF_8)
            val con = NetworkUtils.createHttpConnection(URL("$BASE_URL/capes/active"))
            con.requestMethod = "PUT"
            con.doOutput = true
            con.setRequestProperty("Authorization", "Bearer $accessToken")
            con.setRequestProperty("Content-Type", "application/json")
            con.outputStream.use { it.write(payload) }
            val responseCode = con.responseCode
            if (responseCode / 100 != 2) {
                val response = NetworkUtils.readData(con)
                throw ServerResponseMalformedException("Failed to show cape: HTTP $responseCode - $response")
            }
            LOG.info("Cape activated successfully, capeId: $capeId")
        } catch (e: IOException) {
            throw ServerDisconnectException(e)
        }
    }

    /**
     * Hide the active cape.
     *
     * @param accessToken Minecraft access token (Bearer)
     * @throws AuthenticationException on API errors
     */
    @JvmStatic
    fun hideCape(accessToken: String) {
        try {
            val con = NetworkUtils.createHttpConnection(URL("$BASE_URL/capes/active"))
            con.requestMethod = "DELETE"
            con.setRequestProperty("Authorization", "Bearer $accessToken")
            val responseCode = con.responseCode
            if (responseCode / 100 != 2) {
                val response = NetworkUtils.readData(con)
                throw ServerResponseMalformedException("Failed to hide cape: HTTP $responseCode - $response")
            }
            LOG.info("Cape hidden successfully")
        } catch (e: IOException) {
            throw ServerDisconnectException(e)
        }
    }
}
