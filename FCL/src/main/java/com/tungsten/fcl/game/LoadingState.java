/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.game;

import android.content.Context;

import com.tungsten.fcl.R;

public enum LoadingState {
    DEPENDENCIES(R.string.launch_state_dependencies),
    MODS(R.string.launch_state_modpack),
    LOGGING_IN(R.string.launch_state_logging_in),
    LAUNCHING(R.string.launch_state_waiting_launching),
    DONE(R.string.launch_state_done);

    private final int id;

    LoadingState(int id) {
        this.id = id;
    }

    public String getLocalizedMessage(Context context) {
        return context.getString(id);
    }
}
