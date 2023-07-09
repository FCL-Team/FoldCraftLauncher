package com.tungsten.fclcore.auth.offline;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Pair.pair;

import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.auth.yggdrasil.GameProfile;
import com.tungsten.fclcore.auth.yggdrasil.TextureModel;
import com.tungsten.fclcore.util.KeyUtils;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.gson.UUIDTypeAdapter;
import com.tungsten.fclcore.util.io.HttpServer;
import com.tungsten.fclcore.util.png.fakefx.PNGFakeFXUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class YggdrasilServer extends HttpServer {

    private final Map<UUID, Character> charactersByUuid = new HashMap<>();
    private final Map<String, Character> charactersByName = new HashMap<>();

    public YggdrasilServer(int port) {
        super(port);

        addRoute(Method.GET, Pattern.compile("^/$"), this::root);
        addRoute(Method.GET, Pattern.compile("/status"), this::status);
        addRoute(Method.POST, Pattern.compile("/api/profiles/minecraft"), this::profiles);
        addRoute(Method.GET, Pattern.compile("/sessionserver/session/minecraft/hasJoined"), this::hasJoined);
        addRoute(Method.POST, Pattern.compile("/sessionserver/session/minecraft/join"), this::joinServer);
        addRoute(Method.GET, Pattern.compile("/sessionserver/session/minecraft/profile/(?<uuid>[a-f0-9]{32})"), this::profile);
        addRoute(Method.GET, Pattern.compile("/textures/(?<hash>[a-f0-9]{64})"), this::texture);
    }

    private Response root(Request request) {
        return ok(mapOf(
                pair("signaturePublickey", KeyUtils.toPEMPublicKey(getSignaturePublicKey())),
                pair("skinDomains", Arrays.asList(
                        "127.0.0.1",
                        "localhost"
                )),
                pair("meta", mapOf(
                        pair("serverName", "FCL"),
                        pair("implementationName", "FCL"),
                        pair("implementationVersion", "1.0"),
                        pair("feature.non_email_login", true)
                ))
        ));
    }

    private Response status(Request request) {
        return ok(mapOf(
                pair("user.count", charactersByUuid.size()),
                pair("token.count", 0),
                pair("pendingAuthentication.count", 0)
        ));
    }

    private Response profiles(Request request) throws IOException {
        List<String> names = JsonUtils.fromNonNullJsonFully(request.getSession().getInputStream(), new TypeToken<List<String>>() {
        }.getType());
        return ok(names.stream().distinct()
                .map(this::findCharacterByName)
                .flatMap(Lang::toStream)
                .map(Character::toSimpleResponse)
                .collect(Collectors.toList()));
    }

    private Response hasJoined(Request request) {
        if (!request.getQuery().containsKey("username")) {
            return badRequest();
        }

        Optional<Character> character = findCharacterByName(request.getQuery().get("username"));

        //Workaround for JDK-8138667
        //noinspection OptionalIsPresent
        if (character.isPresent()) {
            return ok(character.get().toCompleteResponse(getRootUrl()));
        } else {
            return HttpServer.noContent();
        }
    }

    private Response joinServer(Request request) {
        return noContent();
    }

    private Response profile(Request request) {
        String uuid = request.getPathVariables().group("uuid");

        Optional<Character> character = findCharacterByUuid(UUIDTypeAdapter.fromString(uuid));

        //Workaround for JDK-8138667
        //noinspection OptionalIsPresent
        if (character.isPresent()) {
            return ok(character.get().toCompleteResponse(getRootUrl()));
        } else {
            return HttpServer.noContent();
        }
    }

    private Response texture(Request request) {
        String hash = request.getPathVariables().group("hash");

        if (Texture.hasTexture(hash)) {
            Texture texture = Texture.getTexture(hash);
            byte[] data = PNGFakeFXUtils.writeImageToArray(texture.getImage());
            Response response = newFixedLengthResponse(Response.Status.OK, "image/png", new ByteArrayInputStream(data), data.length);
            response.addHeader("Etag", String.format("\"%s\"", hash));
            response.addHeader("Cache-Control", "max-age=2592000, public");
            return response;
        } else {
            return notFound();
        }
    }

    private Optional<Character> findCharacterByUuid(UUID uuid) {
        return Optional.ofNullable(charactersByUuid.get(uuid));
    }

    private Optional<Character> findCharacterByName(String uuid) {
        return Optional.ofNullable(charactersByName.get(uuid));
    }

    public void addCharacter(Character character) {
        charactersByUuid.put(character.getUUID(), character);
        charactersByName.put(character.getName(), character);
    }

    public static class Character {
        private final UUID uuid;
        private final String name;
        private final Skin.LoadedSkin skin;

        public Character(UUID uuid, String name, Skin.LoadedSkin skin) {
            this.uuid = uuid;
            this.name = name;
            this.skin = skin;
        }

        public UUID getUUID() {
            return uuid;
        }

        public String getName() {
            return name;
        }

        public GameProfile toSimpleResponse() {
            return new GameProfile(uuid, name);
        }

        public Object toCompleteResponse(String rootUrl) {
            Map<String, Object> realTextures = new HashMap<>();
            if (skin != null && skin.getSkin() != null) {
                if (skin.getModel() == TextureModel.ALEX) {
                    realTextures.put("SKIN", mapOf(
                            pair("url", rootUrl + "/textures/" + skin.getSkin().getHash()),
                            pair("metadata", mapOf(
                                    pair("model", "slim")
                            ))));
                } else {
                    realTextures.put("SKIN", mapOf(pair("url", rootUrl + "/textures/" + skin.getSkin().getHash())));
                }
            }
            if (skin != null && skin.getCape() != null) {
                realTextures.put("CAPE", mapOf(pair("url", rootUrl + "/textures/" + skin.getCape().getHash())));
            }

            Map<String, Object> textureResponse = mapOf(
                    pair("timestamp", System.currentTimeMillis()),
                    pair("profileId", uuid),
                    pair("profileName", name),
                    pair("textures", realTextures)
            );

            return mapOf(
                    pair("id", uuid),
                    pair("name", name),
                    pair("properties", properties(true,
                            pair("textures", new String(
                                    Base64.getEncoder().encode(
                                            JsonUtils.GSON.toJson(textureResponse).getBytes(UTF_8)
                                    ), UTF_8))))
            );
        }
    }

    // === Signature ===

    private static final KeyPair keyPair = KeyUtils.generateKey();

    public static PublicKey getSignaturePublicKey() {
        return keyPair.getPublic();
    }

    private static String sign(String data) {
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(keyPair.getPrivate(), new SecureRandom());
            signature.update(data.getBytes(UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    // === properties ===

    @SafeVarargs
    public static List<?> properties(Map.Entry<String, String>... entries) {
        return properties(false, entries);
    }

    @SafeVarargs
    public static List<?> properties(boolean sign, Map.Entry<String, String>... entries) {
        return Stream.of(entries)
                .map(entry -> {
                    LinkedHashMap<String, String> property = new LinkedHashMap<>();
                    property.put("name", entry.getKey());
                    property.put("value", entry.getValue());
                    if (sign) {
                        property.put("signature", sign(entry.getValue()));
                    }
                    return property;
                })
                .collect(Collectors.toList());
    }

}
