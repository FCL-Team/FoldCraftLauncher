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
package com.tungsten.fclcore.util;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InvocationDispatcher<ARG> implements Consumer<ARG> {

    public static <ARG> InvocationDispatcher<ARG> runOn(Executor executor, Consumer<ARG> action) {
        return new InvocationDispatcher<>(arg -> executor.execute(() -> {
            synchronized (action) {
                action.accept(arg.get());
            }
        }));
    }

    private Consumer<Supplier<ARG>> handler;

    private AtomicReference<Optional<ARG>> pendingArg = new AtomicReference<>();

    public InvocationDispatcher(Consumer<Supplier<ARG>> handler) {
        this.handler = handler;
    }

    @Override
    public void accept(ARG arg) {
        if (pendingArg.getAndSet(Optional.ofNullable(arg)) == null) {
            handler.accept(() -> pendingArg.getAndSet(null).orElse(null));
        }
    }
}