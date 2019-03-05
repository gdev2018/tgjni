/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master;

import android.text.TextUtils;

import com.github.gdev2018.master.tgnet.TLRPC;

import com.github.gdev2018.master.R;
///*import com.github.gdev2018.master.PhoneFormat.PhoneFormat;*/


public class UserObject {

    public static boolean isDeleted(TLRPC.User user) {
        return user == null || user instanceof TLRPC.TL_userDeleted_old2 || user instanceof TLRPC.TL_userEmpty || user.deleted;
    }

    public static boolean isContact(TLRPC.User user) {
        return user != null && (user instanceof TLRPC.TL_userContact_old2 || user.contact || user.mutual_contact);
    }

    public static boolean isUserSelf(TLRPC.User user) {
        return user != null && (user instanceof TLRPC.TL_userSelf_old3 || user.self);
    }

    public static String getUserName(TLRPC.User user) {
        if (user == null || isDeleted(user)) {
            return LocaleController.getString("HiddenName", R.string.HiddenName);
        }
///*        String name = ContactsController.formatName(user.first_name, user.last_name);
//        return name.length() != 0 || user.phone == null || user.phone.length() == 0 ? name : PhoneFormat.getInstance().format("+" + user.phone);*/
        return "Vasya";
    }

    public static String getFirstName(TLRPC.User user) {
        return getFirstName(user, true);
    }

    public static String getFirstName(TLRPC.User user, boolean allowShort) {
        if (user == null || isDeleted(user)) {
            return "DELETED";
        }
        String name = user.first_name;
///*        if (TextUtils.isEmpty(name)) {
//            name = user.last_name;
//        } else if (!allowShort && name.length() <= 2) {
//            return ContactsController.formatName(user.first_name, user.last_name);
//        }*/
        return !TextUtils.isEmpty(name) ? name : LocaleController.getString("HiddenName", R.string.HiddenName);
    }
}
