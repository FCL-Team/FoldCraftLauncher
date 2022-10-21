package com.tungsten.fclcore.auth.yggdrasil;

import com.tungsten.fclcore.auth.AuthenticationException;

import java.net.URL;
import java.util.UUID;

/**
 * @see <a href="http://wiki.vg">http://wiki.vg</a>
 */
public interface YggdrasilProvider {

    URL getAuthenticationURL() throws AuthenticationException;

    URL getRefreshmentURL() throws AuthenticationException;

    URL getValidationURL() throws AuthenticationException;

    URL getInvalidationURL() throws AuthenticationException;

    /**
     * URL to upload skin.
     *
     * Headers:
     *     Authentication: Bearer &lt;access token&gt;
     *
     * Payload:
     *     The payload for this API consists of multipart form data. There are two parts (order does not matter b/c of boundary):
     *     model: Empty string for the default model and "slim" for the slim model
     *     file: Raw image file data
     *
     * @see <a href="https://wiki.vg/Mojang_API#Upload_Skin">https://wiki.vg/Mojang_API#Upload_Skin</a>
     * @return url to upload skin
     * @throws AuthenticationException if url cannot be generated. e.g. some parameter or query is malformed.
     * @throws UnsupportedOperationException if the Yggdrasil provider does not support third-party skin uploading.
     */
    URL getSkinUploadURL(UUID uuid) throws AuthenticationException, UnsupportedOperationException;

    URL getProfilePropertiesURL(UUID uuid) throws AuthenticationException;

}
