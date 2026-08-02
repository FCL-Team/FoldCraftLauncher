package com.tungsten.fclcore.observable.collections;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.util.Callback;

import java.util.IdentityHashMap;

/**
 * Port of {@code ElementObserver}：
 * extractor 模式的核心——为列表元素挂失效监听，元素失效时冒泡列表 update 事件。
 * 用 IdentityHashMap + 计数器处理同一元素在列表中出现多次的情况。
 */
final class ElementObserver<E> {

    private static class ElementsMapElement {

        InvalidationListener listener;
        int counter;

        public ElementsMapElement(InvalidationListener listener) {
            this.listener = listener;
            this.counter = 1;
        }

        public void increment() {
            counter++;
        }

        public int decrement() {
            return --counter;
        }

        private InvalidationListener getListener() {
            return listener;
        }
    }

    private Callback<E, Observable[]> extractor;
    private final Callback<E, InvalidationListener> listenerGenerator;
    private final ObservableListBase<E> list;
    private IdentityHashMap<E, ElementsMapElement> elementsMap = new IdentityHashMap<>();

    ElementObserver(Callback<E, Observable[]> extractor, Callback<E, InvalidationListener> listenerGenerator, ObservableListBase<E> list) {
        this.extractor = extractor;
        this.listenerGenerator = listenerGenerator;
        this.list = list;
    }

    void attachListener(final E e) {
        if (elementsMap != null && e != null) {
            if (elementsMap.containsKey(e)) {
                elementsMap.get(e).increment();
            } else {
                InvalidationListener listener = listenerGenerator.call(e);
                for (Observable o : extractor.call(e)) {
                    o.addListener(listener);
                }
                elementsMap.put(e, new ElementsMapElement(listener));
            }
        }
    }

    void detachListener(E e) {
        if (elementsMap != null && e != null) {
            ElementsMapElement el = elementsMap.get(e);
            for (Observable o : extractor.call(e)) {
                o.removeListener(el.getListener());
            }
            if (el.decrement() == 0) {
                elementsMap.remove(e);
            }
        }
    }
}
