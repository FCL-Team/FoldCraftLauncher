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

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public final class InvocationDispatcher<T> implements Consumer<T> {

    public static <T> InvocationDispatcher<T> runOn(Executor executor, Consumer<T> action) {
        return new InvocationDispatcher<>(executor, action);
    }

    private final Executor executor;
    private final Consumer<T> action;
    private final AtomicReference<Holder<T>> pendingArg = new AtomicReference<>();

    private InvocationDispatcher(Executor executor, Consumer<T> action) {
        this.executor = executor;
        this.action = action;
    }

    @Override
    public void accept(T t) {
        if (pendingArg.getAndSet(new Holder<>(t)) == null) {
            executor.execute(() -> {
                synchronized (InvocationDispatcher.this) {
                    action.accept(pendingArg.getAndSet(null).value);
                }
            });
        }
    }
}