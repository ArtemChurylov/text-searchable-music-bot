package com.music.bot.state;

import java.util.HashMap;
import java.util.Map;

public class UserStateStore {

    private static final Map<Long, UserState> usersState = new HashMap<>();

    public static void registerUser(Long chatId) {
        usersState.put(chatId, UserState.SHOW_MENU);
    }

    public static UserState getUserState(Long chatId) {
        if (!usersState.containsKey(chatId)) {
            registerUser(chatId);
        }
        return usersState.get(chatId);
    }

    public static void updateUserState(Long chatId, UserState state) {
        usersState.put(chatId, state);
    }
}
