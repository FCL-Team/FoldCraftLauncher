package com.tungsten.fclcore.auth.yggdrasil;

import static com.tungsten.fclcore.util.Lang.tryCast;

import com.tungsten.fclcore.auth.AccountFactory;
import com.tungsten.fclcore.auth.AuthenticationException;
import com.tungsten.fclcore.auth.CharacterSelector;
import com.tungsten.fclcore.util.fakefx.ObservableOptionalCache;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class YggdrasilAccountFactory extends AccountFactory<YggdrasilAccount> {

    public static final YggdrasilAccountFactory MOJANG = new YggdrasilAccountFactory(YggdrasilService.MOJANG);

    private final YggdrasilService service;

    public YggdrasilAccountFactory(YggdrasilService service) {
        this.service = service;
    }

    @Override
    public AccountLoginType getLoginType() {
        return AccountLoginType.USERNAME_PASSWORD;
    }

    @Override
    public YggdrasilAccount create(CharacterSelector selector, String username, String password, ProgressCallback progressCallback, Object additionalData) throws AuthenticationException {
        Objects.requireNonNull(selector);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        return new YggdrasilAccount(service, username, password, selector);
    }

    @Override
    public YggdrasilAccount fromStorage(Map<Object, Object> storage) {
        Objects.requireNonNull(storage);

        YggdrasilSession session = YggdrasilSession.fromStorage(storage);

        String username = tryCast(storage.get("username"), String.class)
                .orElseThrow(() -> new IllegalArgumentException("storage does not have username"));

        tryCast(storage.get("profileProperties"), Map.class).ifPresent(
                it -> {
                    @SuppressWarnings("unchecked")
                    Map<String, String> properties = it;
                    GameProfile selected = session.getSelectedProfile();
                    ObservableOptionalCache<UUID, CompleteGameProfile, AuthenticationException> profileRepository = service.getProfileRepository();
                    profileRepository.put(selected.getId(), new CompleteGameProfile(selected, properties));
                    profileRepository.invalidate(selected.getId());
                });

        return new YggdrasilAccount(service, username, session);
    }
}
