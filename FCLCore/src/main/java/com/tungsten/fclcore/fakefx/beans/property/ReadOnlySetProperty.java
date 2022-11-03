package com.tungsten.fclcore.fakefx.beans.property;

import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.binding.SetExpression;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;

import java.util.Set;

public abstract class ReadOnlySetProperty<E> extends SetExpression<E> implements ReadOnlyProperty<ObservableSet<E>>  {

    /**
     * The constructor of {@code ReadOnlySetProperty}.
     */
    public ReadOnlySetProperty() {
    }

    /**
     * Creates a bidirectional content binding of the {@link ObservableSet}, that is
     * wrapped in this {@code ReadOnlySetProperty}, and another {@code ObservableSet}.
     * <p>
     * A bidirectional content binding ensures that the content of two {@code ObservableSets} is the
     * same. If the content of one of the sets changes, the other one will be updated automatically.
     *
     * @param set the {@code ObservableSet} this property should be bound to
     * @throws NullPointerException if {@code set} is {@code null}
     * @throws IllegalArgumentException if {@code set} is the same set that this {@code ReadOnlySetProperty} points to
     */
    public void bindContentBidirectional(ObservableSet<E> set) {
        Bindings.bindContentBidirectional(this, set);
    }

    /**
     * Deletes a bidirectional content binding between the {@link ObservableSet}, that is
     * wrapped in this {@code ReadOnlySetProperty}, and another {@code Object}.
     *
     * @param object the {@code Object} to which the bidirectional binding should be removed
     * @throws NullPointerException if {@code object} is {@code null}
     * @throws IllegalArgumentException if {@code object} is the same set that this {@code ReadOnlySetProperty} points to
     */
    public void unbindContentBidirectional(Object object) {
        Bindings.unbindContentBidirectional(this, object);
    }

    /**
     * Creates a content binding between the {@link ObservableSet}, that is
     * wrapped in this {@code ReadOnlySetProperty}, and another {@code ObservableSet}.
     * <p>
     * A content binding ensures that the content of the wrapped {@code ObservableSets} is the
     * same as that of the other set. If the content of the other set changes, the wrapped set will be updated
     * automatically. Once the wrapped set is bound to another set, you must not change it directly.
     *
     * @param set the {@code ObservableSet} this property should be bound to
     * @throws NullPointerException if {@code set} is {@code null}
     * @throws IllegalArgumentException if {@code set} is the same set that this {@code ReadOnlySetProperty} points to
     */
    public void bindContent(ObservableSet<E> set) {
        Bindings.bindContent(this, set);
    }

    /**
     * Deletes a content binding between the {@link ObservableSet}, that is
     * wrapped in this {@code ReadOnlySetProperty}, and another {@code Object}.
     *
     * @param object the {@code Object} to which the binding should be removed
     * @throws NullPointerException if {@code object} is {@code null}
     * @throws IllegalArgumentException if {@code object} is the same set that this {@code ReadOnlySetProperty} points to
     */
    public void unbindContent(Object object) {
        Bindings.unbindContent(this, object);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Set))
            return false;
        Set c = (Set) obj;
        if (c.size() != size())
            return false;
        try {
            return containsAll(c);
        } catch (ClassCastException unused)   {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }
    }

    /**
     * Returns a hash code for this {@code ReadOnlySetProperty} object.
     * @return a hash code for this {@code ReadOnlySetProperty} object.
     */
    @Override
    public int hashCode() {
        int h = 0;
        for (E e : this) {
            if (e != null)
                h += e.hashCode();
        }
        return h;
    }

    /**
     * Returns a string representation of this {@code ReadOnlySetProperty} object.
     * @return a string representation of this {@code ReadOnlySetProperty} object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder(
                "ReadOnlySetProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

}
