package com.tungsten.fclcore.observable.binding;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.property.Property;
import com.tungsten.fclcore.observable.value.ObservableValue;

import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Simplified reimplementation of {@code BidirectionalBinding}（仅保留泛型变体）：
 * <ul>
 * <li>{@code updating} 标志防回环；</li>
 * <li>对两个属性持弱引用，任一侧被 GC 后自动互相摘除；</li>
 * <li>传播失败时回滚到 oldValue（与 JavaFX 一致）；</li>
 * <li>equals/hashCode 与顺序无关地匹配同一对属性——unbindBidirectional 依赖这一点
 * 通过 equals 找到并移除既有绑定。</li>
 * </ul>
 */
final class BidirectionalBinding implements InvalidationListener, WeakListener {

    private static void checkParameters(Object property1, Object property2) {
        Objects.requireNonNull(property1, "Both properties must be specified.");
        Objects.requireNonNull(property2, "Both properties must be specified.");
        if (property1 == property2) {
            throw new IllegalArgumentException("Cannot bind property to itself");
        }
    }

    static <T> BidirectionalBinding bind(Property<T> property1, Property<T> property2) {
        checkParameters(property1, property2);
        final BidirectionalBinding binding = new BidirectionalBinding(property1, property2);
        property1.setValue(property2.getValue());
        property1.getValue();
        property1.addListener(binding);
        property2.addListener(binding);
        return binding;
    }

    static void unbind(Object property1, Object property2) {
        checkParameters(property1, property2);
        final BidirectionalBinding binding = new BidirectionalBinding(property1, property2);
        if (property1 instanceof ObservableValue) {
            ((ObservableValue<?>) property1).removeListener(binding);
        }
        if (property2 instanceof ObservableValue) {
            ((ObservableValue<?>) property2).removeListener(binding);
        }
    }

    private final WeakReference<Property<Object>> propertyRef1;
    private final WeakReference<Property<Object>> propertyRef2;
    private final int cachedHashCode;
    private Object oldValue;
    private boolean updating = false;

    @SuppressWarnings("unchecked")
    private BidirectionalBinding(Object property1, Object property2) {
        propertyRef1 = new WeakReference<>((Property<Object>) property1);
        propertyRef2 = new WeakReference<>((Property<Object>) property2);
        oldValue = property1 instanceof Property ? ((Property<?>) property1).getValue() : null;
        cachedHashCode = property1.hashCode() * property2.hashCode();
    }

    private Property<Object> getProperty1() {
        return propertyRef1.get();
    }

    private Property<Object> getProperty2() {
        return propertyRef2.get();
    }

    @Override
    public boolean wasGarbageCollected() {
        return (getProperty1() == null) || (getProperty2() == null);
    }

    @Override
    public void invalidated(Observable sourceProperty) {
        if (!updating) {
            final Property<Object> property1 = propertyRef1.get();
            final Property<Object> property2 = propertyRef2.get();
            if ((property1 == null) || (property2 == null)) {
                if (property1 != null) {
                    property1.removeListener(this);
                }
                if (property2 != null) {
                    property2.removeListener(this);
                }
            } else {
                try {
                    updating = true;
                    if (property1 == sourceProperty) {
                        final Object newValue = property1.getValue();
                        property2.setValue(newValue);
                        property2.getValue();
                        oldValue = newValue;
                    } else {
                        final Object newValue = property2.getValue();
                        property1.setValue(newValue);
                        property1.getValue();
                        oldValue = newValue;
                    }
                } catch (RuntimeException e) {
                    try {
                        if (property1 == sourceProperty) {
                            property1.setValue(oldValue);
                            property1.getValue();
                        } else {
                            property2.setValue(oldValue);
                            property2.getValue();
                        }
                    } catch (Exception e2) {
                        e2.addSuppressed(e);
                        unbind(property1, property2);
                        throw new RuntimeException(
                                "Bidirectional binding failed together with an attempt"
                                        + " to restore the source property to the previous value."
                                        + " Removing the bidirectional binding from properties " +
                                        property1 + " and " + property2, e2);
                    }
                    throw new RuntimeException(
                            "Bidirectional binding failed, setting to the previous value", e);
                } finally {
                    updating = false;
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return cachedHashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        final Object propertyA1 = getProperty1();
        final Object propertyA2 = getProperty2();
        if ((propertyA1 == null) || (propertyA2 == null)) {
            return false;
        }
        if (obj instanceof BidirectionalBinding) {
            final BidirectionalBinding otherBinding = (BidirectionalBinding) obj;
            final Object propertyB1 = otherBinding.getProperty1();
            final Object propertyB2 = otherBinding.getProperty2();
            if ((propertyB1 == null) || (propertyB2 == null)) {
                return false;
            }
            if (propertyA1 == propertyB1 && propertyA2 == propertyB2) {
                return true;
            }
            if (propertyA1 == propertyB2 && propertyA2 == propertyB1) {
                return true;
            }
        }
        return false;
    }
}
