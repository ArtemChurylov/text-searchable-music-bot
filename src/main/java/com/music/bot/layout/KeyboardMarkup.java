package com.music.bot.layout;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardMarkup {

    public static final ReplyKeyboardMarkup KEYBOARD_MENU;

    static {
        KEYBOARD_MENU = new ReplyKeyboardMarkup();
        KEYBOARD_MENU.setSelective(true);
        KEYBOARD_MENU.setResizeKeyboard(true);
        KEYBOARD_MENU.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton("Upload song"));
        row2.add(new KeyboardButton("Search by lyrics"));
        row3.add(new KeyboardButton("Get list of songs"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        KEYBOARD_MENU.setKeyboard(keyboard);
    }
}
