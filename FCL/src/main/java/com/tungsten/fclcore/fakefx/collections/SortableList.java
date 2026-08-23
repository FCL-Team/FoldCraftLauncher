/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.tungsten.fclcore.fakefx.collections;

import java.util.Comparator;
import java.util.List;

/**
 * SortableList is a list that can sort itself in an efficient way, in contrast to the
 * Collections.sort() method which threat all lists the same way.
 * E.g. ObservableList can sort and fire only one notification.
 * @param <E>
 */
public interface SortableList<E> extends List<E> {

    /**
     * Sort using default comparator
     * @throws ClassCastException if some of the elements cannot be cast to Comparable
     * @throws UnsupportedOperationException if list's iterator doesn't support set
     */
    public void sort();

    /**
     * Sort using comparator
     * @param comparator the comparator to use
     * @throws ClassCastException if the list contains elements that are not
     *         <i>mutually comparable</i> using the specified comparator.
     * @throws UnsupportedOperationException if the specified list's
     *         list-iterator does not support the <tt>set</tt> operation.
     */
    public void sort(Comparator<? super E> comparator);

}
