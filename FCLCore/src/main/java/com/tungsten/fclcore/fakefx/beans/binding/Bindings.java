package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.Property;
import com.tungsten.fclcore.fakefx.beans.value.ObservableBooleanValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableDoubleValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableFloatValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableIntegerValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableLongValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableNumberValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableObjectValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableStringValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.binding.BidirectionalBinding;
import com.tungsten.fclcore.fakefx.binding.BidirectionalContentBinding;
import com.tungsten.fclcore.fakefx.binding.ContentBinding;
import com.tungsten.fclcore.fakefx.binding.DoubleConstant;
import com.tungsten.fclcore.fakefx.binding.FloatConstant;
import com.tungsten.fclcore.fakefx.binding.IntegerConstant;
import com.tungsten.fclcore.fakefx.binding.LongConstant;
import com.tungsten.fclcore.fakefx.binding.ObjectConstant;
import com.tungsten.fclcore.fakefx.binding.SelectBinding;
import com.tungsten.fclcore.fakefx.binding.StringConstant;
import com.tungsten.fclcore.fakefx.binding.StringFormatter;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ImmutableObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableArray;
import com.tungsten.fclcore.fakefx.collections.ObservableFloatArray;
import com.tungsten.fclcore.fakefx.collections.ObservableIntegerArray;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.fakefx.util.StringConverter;

import java.lang.ref.WeakReference;
import java.text.Format;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public final class Bindings {

    private Bindings() {
    }

    // =================================================================================================================
    // Helper functions to create custom bindings

    public static BooleanBinding createBooleanBinding(final Callable<Boolean> func, final Observable... dependencies) {
        return new BooleanBinding() {
            {
                bind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return  ((dependencies == null) || (dependencies.length == 0))?
                            FXCollections.emptyObservableList()
                        : (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static DoubleBinding createDoubleBinding(final Callable<Double> func, final Observable... dependencies) {
        return new DoubleBinding() {
            {
                bind(dependencies);
            }

            @Override
            protected double computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    return 0.0;
                }
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return  ((dependencies == null) || (dependencies.length == 0))?
                            FXCollections.emptyObservableList()
                        : (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static FloatBinding createFloatBinding(final Callable<Float> func, final Observable... dependencies) {
        return new FloatBinding() {
            {
                bind(dependencies);
            }

            @Override
            protected float computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    return 0.0f;
                }
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return  ((dependencies == null) || (dependencies.length == 0))?
                            FXCollections.emptyObservableList()
                        : (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static IntegerBinding createIntegerBinding(final Callable<Integer> func, final Observable... dependencies) {
        return new IntegerBinding() {
            {
                bind(dependencies);
            }

            @Override
            protected int computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    return 0;
                }
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            /**
             * Returns an immutable list of the dependencies of this binding.
             */
            @Override
            public ObservableList<?> getDependencies() {
                return  ((dependencies == null) || (dependencies.length == 0))?
                            FXCollections.emptyObservableList()
                        : (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    /**
     * Helper function to create a custom {@link LongBinding}.
     *
     * @param func The function that calculates the value of this binding
     * @param dependencies The dependencies of this binding
     * @return The generated binding
     * @since JavaFX 2.1
     */
    public static LongBinding createLongBinding(final Callable<Long> func, final Observable... dependencies) {
        return new LongBinding() {
            {
                bind(dependencies);
            }

            @Override
            protected long computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    return 0L;
                }
            }

            /**
             * Calls {@link LongBinding#unbind(Observable...)}.
             */
            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            /**
             * Returns an immutable list of the dependencies of this binding.
             */
            @Override
            public ObservableList<?> getDependencies() {
                return  ((dependencies == null) || (dependencies.length == 0))?
                            FXCollections.emptyObservableList()
                        : (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    /**
     * Helper function to create a custom {@link ObjectBinding}.
     *
     * @param <T> the type of the bound {@code Object}
     * @param func The function that calculates the value of this binding
     * @param dependencies The dependencies of this binding
     * @return The generated binding
     * @since JavaFX 2.1
     */
    public static <T> ObjectBinding<T> createObjectBinding(final Callable<T> func, final Observable... dependencies) {
        return new ObjectBinding<T>() {
            {
                bind(dependencies);
            }

            @Override
            protected T computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    return null;
                }
            }

            /**
             * Calls {@link ObjectBinding#unbind(Observable...)}.
             */
            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            /**
             * Returns an immutable list of the dependencies of this binding.
             */
            @Override
            public ObservableList<?> getDependencies() {
                return  ((dependencies == null) || (dependencies.length == 0))?
                            FXCollections.emptyObservableList()
                        : (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    /**
     * Helper function to create a custom {@link StringBinding}.
     *
     * @param func The function that calculates the value of this binding
     * @param dependencies The dependencies of this binding
     * @return The generated binding
     * @since JavaFX 2.1
     */
    public static StringBinding createStringBinding(final Callable<String> func, final Observable... dependencies) {
        return new StringBinding() {
            {
                bind(dependencies);
            }

            @Override
            protected String computeValue() {
                try {
                    return func.call();
                } catch (Exception e) {
                    return "";
                }
            }

            /**
             * Calls {@link StringBinding#unbind(Observable...)}.
             */
            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            /**
             * Returns an immutable list of the dependencies of this binding.
             */
            @Override
            public ObservableList<?> getDependencies() {
                return  ((dependencies == null) || (dependencies.length == 0))?
                            FXCollections.emptyObservableList()
                        : (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }


    // =================================================================================================================
    // Select Bindings

    public static <T> ObjectBinding<T> select(ObservableValue<?> root, String... steps) {
        return new SelectBinding.AsObject<T>(root, steps);
    }

    public static DoubleBinding selectDouble(ObservableValue<?> root, String... steps) {
        return new SelectBinding.AsDouble(root, steps);
    }

    public static FloatBinding selectFloat(ObservableValue<?> root, String... steps) {
        return new SelectBinding.AsFloat(root, steps);
    }

    public static IntegerBinding selectInteger(ObservableValue<?> root, String... steps) {
        return new SelectBinding.AsInteger(root, steps);
    }

    public static LongBinding selectLong(ObservableValue<?> root, String... steps) {
        return new SelectBinding.AsLong(root, steps);
    }

    public static BooleanBinding selectBoolean(ObservableValue<?> root, String... steps) {
        return new SelectBinding.AsBoolean(root, steps);
    }

    public static StringBinding selectString(ObservableValue<?> root, String... steps) {
        return new SelectBinding.AsString(root, steps);
    }

    public static <T> ObjectBinding<T> select(Object root, String... steps) {
        return new SelectBinding.AsObject<T>(root, steps);
    }

    public static DoubleBinding selectDouble(Object root, String... steps) {
        return new SelectBinding.AsDouble(root, steps);
    }

    public static FloatBinding selectFloat(Object root, String... steps) {
        return new SelectBinding.AsFloat(root, steps);
    }

    public static IntegerBinding selectInteger(Object root, String... steps) {
        return new SelectBinding.AsInteger(root, steps);
    }

    public static LongBinding selectLong(Object root, String... steps) {
        return new SelectBinding.AsLong(root, steps);
    }

    public static BooleanBinding selectBoolean(Object root, String... steps) {
        return new SelectBinding.AsBoolean(root, steps);
    }

    public static StringBinding selectString(Object root, String... steps) {
        return new SelectBinding.AsString(root, steps);
    }

    public static When when(final ObservableBooleanValue condition) {
        return new When(condition);
    }

    // =================================================================================================================
    // Bidirectional Bindings

    public static <T> void bindBidirectional(Property<T> property1, Property<T> property2) {
        BidirectionalBinding.bind(property1, property2);
    }

    public static <T> void unbindBidirectional(Property<T> property1, Property<T> property2) {
        BidirectionalBinding.unbind(property1, property2);
    }

    public static void unbindBidirectional(Object property1, Object property2) {
        BidirectionalBinding.unbind(property1, property2);
    }

    public  static void bindBidirectional(Property<String> stringProperty, Property<?> otherProperty, Format format) {
        BidirectionalBinding.bind(stringProperty, otherProperty, format);
    }

    public static <T> void bindBidirectional(Property<String> stringProperty, Property<T> otherProperty, StringConverter<T> converter) {
        BidirectionalBinding.bind(stringProperty, otherProperty, converter);
    }

    public static <E> void bindContentBidirectional(ObservableList<E> list1, ObservableList<E> list2) {
        BidirectionalContentBinding.bind(list1, list2);
    }

    public static <E> void bindContentBidirectional(ObservableSet<E> set1, ObservableSet<E> set2) {
        BidirectionalContentBinding.bind(set1, set2);
    }

    public static <K, V> void bindContentBidirectional(ObservableMap<K, V> map1, ObservableMap<K, V> map2) {
        BidirectionalContentBinding.bind(map1, map2);
    }

    public static void unbindContentBidirectional(Object obj1, Object obj2) {
        BidirectionalContentBinding.unbind(obj1, obj2);
    }

    public static <E> void bindContent(List<E> list1, ObservableList<? extends E> list2) {
        ContentBinding.bind(list1, list2);
    }

    /**
     * Generates a content binding between an {@link ObservableSet} and a {@link Set}.
     * <p>
     * A content binding ensures that the {@code Set} contains the same elements as the {@code ObservableSet}.
     * If the content of the {@code ObservableSet} changes, the {@code Set} will be updated automatically.
     * <p>
     * Once a {@code Set} is bound to an {@code ObservableSet}, the {@code Set} must not be changed directly
     * anymore. Doing so would lead to unexpected results.
     * <p>
     * A content-binding can be removed with {@link #unbindContent(Object, Object)}.
     *
     * @param <E>
     *            the type of the {@code Set} elements
     * @param set1
     *            the {@code Set}
     * @param set2
     *            the {@code ObservableSet}
     * @throws NullPointerException
     *            if one of the sets is {@code null}
     * @throws IllegalArgumentException
     *            if {@code set1} == {@code set2}
     * @since JavaFX 2.1
     */
    public static <E> void bindContent(Set<E> set1, ObservableSet<? extends E> set2) {
        ContentBinding.bind(set1, set2);
    }

    /**
     * Generates a content binding between an {@link ObservableMap} and a {@link Map}.
     * <p>
     * A content binding ensures that the {@code Map} contains the same elements as the {@code ObservableMap}.
     * If the content of the {@code ObservableMap} changes, the {@code Map} will be updated automatically.
     * <p>
     * Once a {@code Map} is bound to an {@code ObservableMap}, the {@code Map} must not be changed directly
     * anymore. Doing so would lead to unexpected results.
     * <p>
     * A content-binding can be removed with {@link #unbindContent(Object, Object)}.
     *
     * @param <K>
     *            the type of the key elements of the {@code Map}
     * @param <V>
     *            the type of the value elements of the {@code Map}
     * @param map1
     *            the {@code Map}
     * @param map2
     *            the {@code ObservableMap}
     * @throws NullPointerException
     *            if one of the maps is {@code null}
     * @throws IllegalArgumentException
     *            if {@code map1} == {@code map2}
     * @since JavaFX 2.1
     */
    public static <K, V> void bindContent(Map<K, V> map1, ObservableMap<? extends K, ? extends V> map2) {
        ContentBinding.bind(map1, map2);
    }

    /**
     * Remove a content binding.
     *
     * @param obj1
     *            the first {@code Object}
     * @param obj2
     *            the second {@code Object}
     * @throws NullPointerException
     *            if one of the {@code Objects} is {@code null}
     * @throws IllegalArgumentException
     *            if {@code obj1} == {@code obj2}
     * @since JavaFX 2.1
     */
    public static void unbindContent(Object obj1, Object obj2) {
        ContentBinding.unbind(obj1, obj2);
    }



    // =================================================================================================================
    // Negation

    public static NumberBinding negate(final ObservableNumberValue value) {
        if (value == null) {
            throw new NullPointerException("Operand cannot be null.");
        }

        if (value instanceof ObservableDoubleValue) {
            return new DoubleBinding() {
                {
                    super.bind(value);
                }

                @Override
                public void dispose() {
                    super.unbind(value);
                }

                @Override
                protected double computeValue() {
                    return -value.doubleValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return FXCollections.singletonObservableList(value);
                }
            };
        } else if (value instanceof ObservableFloatValue) {
            return new FloatBinding() {
                {
                    super.bind(value);
                }

                @Override
                public void dispose() {
                    super.unbind(value);
                }

                @Override
                protected float computeValue() {
                    return -value.floatValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return FXCollections.singletonObservableList(value);
                }
            };
        } else if (value instanceof ObservableLongValue) {
            return new LongBinding() {
                {
                    super.bind(value);
                }

                @Override
                public void dispose() {
                    super.unbind(value);
                }

                @Override
                protected long computeValue() {
                    return -value.longValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return FXCollections.singletonObservableList(value);
                }
            };
        } else {
            return new IntegerBinding() {
                {
                    super.bind(value);
                }

                @Override
                public void dispose() {
                    super.unbind(value);
                }

                @Override
                protected int computeValue() {
                    return -value.intValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return FXCollections.singletonObservableList(value);
                }
            };
        }
    }

    // =================================================================================================================
    // Sum

    private static NumberBinding add(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new DoubleBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected double computeValue() {
                    return op1.doubleValue() + op2.doubleValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new FloatBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected float computeValue() {
                    return op1.floatValue() + op2.floatValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new LongBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected long computeValue() {
                    return op1.longValue() + op2.longValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new IntegerBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected int computeValue() {
                    return op1.intValue() + op2.intValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static NumberBinding add(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return Bindings.add(op1, op2, op1, op2);
    }

    public static DoubleBinding add(final ObservableNumberValue op1, double op2) {
        return (DoubleBinding) Bindings.add(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static DoubleBinding add(double op1, final ObservableNumberValue op2) {
        return (DoubleBinding) Bindings.add(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding add(final ObservableNumberValue op1, float op2) {
        return Bindings.add(op1, FloatConstant.valueOf(op2), op1);
    }

    public static NumberBinding add(float op1, final ObservableNumberValue op2) {
        return Bindings.add(FloatConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding add(final ObservableNumberValue op1, long op2) {
        return Bindings.add(op1, LongConstant.valueOf(op2), op1);
    }

    public static NumberBinding add(long op1, final ObservableNumberValue op2) {
        return Bindings.add(LongConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding add(final ObservableNumberValue op1, int op2) {
        return Bindings.add(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static NumberBinding add(int op1, final ObservableNumberValue op2) {
        return Bindings.add(IntegerConstant.valueOf(op1), op2, op2);
    }

    // =================================================================================================================
    // Diff

    private static NumberBinding subtract(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new DoubleBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected double computeValue() {
                    return op1.doubleValue() - op2.doubleValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new FloatBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected float computeValue() {
                    return op1.floatValue() - op2.floatValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new LongBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected long computeValue() {
                    return op1.longValue() - op2.longValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new IntegerBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected int computeValue() {
                    return op1.intValue() - op2.intValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static NumberBinding subtract(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return Bindings.subtract(op1, op2, op1, op2);
    }

    public static DoubleBinding subtract(final ObservableNumberValue op1, double op2) {
        return (DoubleBinding) Bindings.subtract(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static DoubleBinding subtract(double op1, final ObservableNumberValue op2) {
        return (DoubleBinding) Bindings.subtract(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding subtract(final ObservableNumberValue op1, float op2) {
        return Bindings.subtract(op1, FloatConstant.valueOf(op2), op1);
    }

    public static NumberBinding subtract(float op1, final ObservableNumberValue op2) {
        return Bindings.subtract(FloatConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding subtract(final ObservableNumberValue op1, long op2) {
        return Bindings.subtract(op1, LongConstant.valueOf(op2), op1);
    }

    public static NumberBinding subtract(long op1, final ObservableNumberValue op2) {
        return Bindings.subtract(LongConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding subtract(final ObservableNumberValue op1, int op2) {
        return Bindings.subtract(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static NumberBinding subtract(int op1, final ObservableNumberValue op2) {
        return Bindings.subtract(IntegerConstant.valueOf(op1), op2, op2);
    }

    // =================================================================================================================
    // Multiply

    private static NumberBinding multiply(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new DoubleBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected double computeValue() {
                    return op1.doubleValue() * op2.doubleValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new FloatBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected float computeValue() {
                    return op1.floatValue() * op2.floatValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new LongBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected long computeValue() {
                    return op1.longValue() * op2.longValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new IntegerBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected int computeValue() {
                    return op1.intValue() * op2.intValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static NumberBinding multiply(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return Bindings.multiply(op1, op2, op1, op2);
    }

    public static DoubleBinding multiply(final ObservableNumberValue op1, double op2) {
        return (DoubleBinding) Bindings.multiply(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static DoubleBinding multiply(double op1, final ObservableNumberValue op2) {
        return (DoubleBinding) Bindings.multiply(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding multiply(final ObservableNumberValue op1, float op2) {
        return Bindings.multiply(op1, FloatConstant.valueOf(op2), op1);
    }

    public static NumberBinding multiply(float op1, final ObservableNumberValue op2) {
        return Bindings.multiply(FloatConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding multiply(final ObservableNumberValue op1, long op2) {
        return Bindings.multiply(op1, LongConstant.valueOf(op2), op1);
    }

    public static NumberBinding multiply(long op1, final ObservableNumberValue op2) {
        return Bindings.multiply(LongConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding multiply(final ObservableNumberValue op1, int op2) {
        return Bindings.multiply(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static NumberBinding multiply(int op1, final ObservableNumberValue op2) {
        return Bindings.multiply(IntegerConstant.valueOf(op1), op2, op2);
    }

    // =================================================================================================================
    // Divide

    private static NumberBinding divide(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new DoubleBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected double computeValue() {
                    return op1.doubleValue() / op2.doubleValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new FloatBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected float computeValue() {
                    return op1.floatValue() / op2.floatValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new LongBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected long computeValue() {
                    return op1.longValue() / op2.longValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new IntegerBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected int computeValue() {
                    return op1.intValue() / op2.intValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static NumberBinding divide(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return Bindings.divide(op1, op2, op1, op2);
    }

    public static DoubleBinding divide(final ObservableNumberValue op1, double op2) {
        return (DoubleBinding) Bindings.divide(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static DoubleBinding divide(double op1, final ObservableNumberValue op2) {
        return (DoubleBinding) Bindings.divide(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding divide(final ObservableNumberValue op1, float op2) {
        return Bindings.divide(op1, FloatConstant.valueOf(op2), op1);
    }

    public static NumberBinding divide(float op1, final ObservableNumberValue op2) {
        return Bindings.divide(FloatConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding divide(final ObservableNumberValue op1, long op2) {
        return Bindings.divide(op1, LongConstant.valueOf(op2), op1);
    }

    public static NumberBinding divide(long op1, final ObservableNumberValue op2) {
        return Bindings.divide(LongConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding divide(final ObservableNumberValue op1, int op2) {
        return Bindings.divide(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static NumberBinding divide(int op1, final ObservableNumberValue op2) {
        return Bindings.divide(IntegerConstant.valueOf(op1), op2, op2);
    }

    // =================================================================================================================
    // Equals

    private static BooleanBinding equal(final ObservableNumberValue op1, final ObservableNumberValue op2, final double epsilon, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return Math.abs(op1.doubleValue() - op2.doubleValue()) <= epsilon;
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return Math.abs(op1.floatValue() - op2.floatValue()) <= epsilon;
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return Math.abs(op1.longValue() - op2.longValue()) <= epsilon;
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return Math.abs(op1.intValue() - op2.intValue()) <= epsilon;
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static BooleanBinding equal(final ObservableNumberValue op1, final ObservableNumberValue op2, final double epsilon) {
        return Bindings.equal(op1, op2, epsilon, op1, op2);
    }

    public static BooleanBinding equal(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return equal(op1, op2, 0.0, op1, op2);
    }

    public static BooleanBinding equal(final ObservableNumberValue op1, final double op2, final double epsilon) {
        return equal(op1, DoubleConstant.valueOf(op2), epsilon,  op1);
    }

    public static BooleanBinding equal(final double op1, final ObservableNumberValue op2, final double epsilon) {
        return equal(DoubleConstant.valueOf(op1), op2, epsilon, op2);
    }

    public static BooleanBinding equal(final ObservableNumberValue op1, final float op2, final double epsilon) {
        return equal(op1, FloatConstant.valueOf(op2), epsilon, op1);
    }

    public static BooleanBinding equal(final float op1, final ObservableNumberValue op2, final double epsilon) {
        return equal(FloatConstant.valueOf(op1), op2, epsilon, op2);
    }

    public static BooleanBinding equal(final ObservableNumberValue op1, final long op2, final double epsilon) {
        return equal(op1, LongConstant.valueOf(op2), epsilon, op1);
    }

    public static BooleanBinding equal(final ObservableNumberValue op1, final long op2) {
        return equal(op1, LongConstant.valueOf(op2), 0.0, op1);
    }

    public static BooleanBinding equal(final long op1, final ObservableNumberValue op2, final double epsilon) {
        return equal(LongConstant.valueOf(op1), op2, epsilon, op2);
    }

    public static BooleanBinding equal(final long op1, final ObservableNumberValue op2) {
        return equal(LongConstant.valueOf(op1), op2, 0.0, op2);
    }

    public static BooleanBinding equal(final ObservableNumberValue op1, final int op2, final double epsilon) {
        return equal(op1, IntegerConstant.valueOf(op2), epsilon, op1);
    }

    public static BooleanBinding equal(final ObservableNumberValue op1, final int op2) {
        return equal(op1, IntegerConstant.valueOf(op2), 0.0, op1);
    }

    public static BooleanBinding equal(final int op1, final ObservableNumberValue op2, final double epsilon) {
        return equal(IntegerConstant.valueOf(op1), op2, epsilon, op2);
    }

    public static BooleanBinding equal(final int op1, final ObservableNumberValue op2) {
        return equal(IntegerConstant.valueOf(op1), op2, 0.0, op2);
    }

    // =================================================================================================================
    // Not Equal

    private static BooleanBinding notEqual(final ObservableNumberValue op1, final ObservableNumberValue op2, final double epsilon, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return Math.abs(op1.doubleValue() - op2.doubleValue()) > epsilon;
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return Math.abs(op1.floatValue() - op2.floatValue()) > epsilon;
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return Math.abs(op1.longValue() - op2.longValue()) > epsilon;
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return Math.abs(op1.intValue() - op2.intValue()) > epsilon;
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static BooleanBinding notEqual(final ObservableNumberValue op1, final ObservableNumberValue op2, final double epsilon) {
        return Bindings.notEqual(op1, op2, epsilon, op1, op2);
    }

    public static BooleanBinding notEqual(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return notEqual(op1, op2, 0.0, op1, op2);
    }

    public static BooleanBinding notEqual(final ObservableNumberValue op1, final double op2, final double epsilon) {
        return notEqual(op1, DoubleConstant.valueOf(op2), epsilon, op1);
    }

    public static BooleanBinding notEqual(final double op1, final ObservableNumberValue op2, final double epsilon) {
        return notEqual(DoubleConstant.valueOf(op1), op2, epsilon, op2);
    }

    public static BooleanBinding notEqual(final ObservableNumberValue op1, final float op2, final double epsilon) {
        return notEqual(op1, FloatConstant.valueOf(op2), epsilon, op1);
    }

    public static BooleanBinding notEqual(final float op1, final ObservableNumberValue op2, final double epsilon) {
        return notEqual(FloatConstant.valueOf(op1), op2, epsilon, op2);
    }

    public static BooleanBinding notEqual(final ObservableNumberValue op1, final long op2, final double epsilon) {
        return notEqual(op1, LongConstant.valueOf(op2), epsilon, op1);
    }

    public static BooleanBinding notEqual(final ObservableNumberValue op1, final long op2) {
        return notEqual(op1, LongConstant.valueOf(op2), 0.0, op1);
    }

    public static BooleanBinding notEqual(final long op1, final ObservableNumberValue op2, final double epsilon) {
        return notEqual(LongConstant.valueOf(op1), op2, epsilon, op2);
    }

    public static BooleanBinding notEqual(final long op1, final ObservableNumberValue op2) {
        return notEqual(LongConstant.valueOf(op1), op2, 0.0, op2);
    }

    public static BooleanBinding notEqual(final ObservableNumberValue op1, final int op2, final double epsilon) {
        return notEqual(op1, IntegerConstant.valueOf(op2), epsilon, op1);
    }

    public static BooleanBinding notEqual(final ObservableNumberValue op1, final int op2) {
        return notEqual(op1, IntegerConstant.valueOf(op2), 0.0, op1);
    }

    public static BooleanBinding notEqual(final int op1, final ObservableNumberValue op2, final double epsilon) {
        return notEqual(IntegerConstant.valueOf(op1), op2, epsilon, op2);
    }

    public static BooleanBinding notEqual(final int op1, final ObservableNumberValue op2) {
        return notEqual(IntegerConstant.valueOf(op1), op2, 0.0, op2);
    }

    // =================================================================================================================
    // Greater Than

    private static BooleanBinding greaterThan(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return op1.doubleValue() > op2.doubleValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return op1.floatValue() > op2.floatValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return op1.longValue() > op2.longValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return op1.intValue() > op2.intValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static BooleanBinding greaterThan(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return Bindings.greaterThan(op1, op2, op1, op2);
    }

    public static BooleanBinding greaterThan(final ObservableNumberValue op1, final double op2) {
        return greaterThan(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThan(final double op1, final ObservableNumberValue op2) {
        return greaterThan(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding greaterThan(final ObservableNumberValue op1, final float op2) {
        return greaterThan(op1, FloatConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThan(final float op1, final ObservableNumberValue op2) {
        return greaterThan(FloatConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding greaterThan(final ObservableNumberValue op1, final long op2) {
        return greaterThan(op1, LongConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThan(final long op1, final ObservableNumberValue op2) {
        return greaterThan(LongConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding greaterThan(final ObservableNumberValue op1, final int op2) {
        return greaterThan(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThan(final int op1, final ObservableNumberValue op2) {
        return greaterThan(IntegerConstant.valueOf(op1), op2, op2);
    }

    // =================================================================================================================
    // Less Than

    private static BooleanBinding lessThan(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        return greaterThan(op2, op1, dependencies);
    }

    public static BooleanBinding lessThan(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return lessThan(op1, op2, op1, op2);
    }

    public static BooleanBinding lessThan(final ObservableNumberValue op1, final double op2) {
        return lessThan(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThan(final double op1, final ObservableNumberValue op2) {
        return lessThan(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding lessThan(final ObservableNumberValue op1, final float op2) {
        return lessThan(op1, FloatConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThan(final float op1, final ObservableNumberValue op2) {
        return lessThan(FloatConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding lessThan(final ObservableNumberValue op1, final long op2) {
        return lessThan(op1, LongConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThan(final long op1, final ObservableNumberValue op2) {
        return lessThan(LongConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding lessThan(final ObservableNumberValue op1, final int op2) {
        return lessThan(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThan(final int op1, final ObservableNumberValue op2) {
        return lessThan(IntegerConstant.valueOf(op1), op2, op2);
    }

    // =================================================================================================================
    // Greater Than or Equal

    private static BooleanBinding greaterThanOrEqual(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return op1.doubleValue() >= op2.doubleValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return op1.floatValue() >= op2.floatValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return op1.longValue() >= op2.longValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new BooleanBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected boolean computeValue() {
                    return op1.intValue() >= op2.intValue();
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static BooleanBinding greaterThanOrEqual(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return greaterThanOrEqual(op1, op2, op1, op2);
    }

    public static BooleanBinding greaterThanOrEqual(final ObservableNumberValue op1, final double op2) {
        return greaterThanOrEqual(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThanOrEqual(final double op1, final ObservableNumberValue op2) {
        return greaterThanOrEqual(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding greaterThanOrEqual(final ObservableNumberValue op1, final float op2) {
        return greaterThanOrEqual(op1, FloatConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThanOrEqual(final float op1, final ObservableNumberValue op2) {
        return greaterThanOrEqual(FloatConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding greaterThanOrEqual(final ObservableNumberValue op1, final long op2) {
        return greaterThanOrEqual(op1, LongConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThanOrEqual(final long op1, final ObservableNumberValue op2) {
        return greaterThanOrEqual(LongConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding greaterThanOrEqual(final ObservableNumberValue op1, final int op2) {
        return greaterThanOrEqual(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThanOrEqual(final int op1, final ObservableNumberValue op2) {
        return greaterThanOrEqual(IntegerConstant.valueOf(op1), op2, op2);
    }

    // =================================================================================================================
    // Less Than or Equal

    private static BooleanBinding lessThanOrEqual(final ObservableNumberValue op1, final ObservableNumberValue op2, Observable... dependencies) {
        return greaterThanOrEqual(op2, op1, dependencies);
    }

    public static BooleanBinding lessThanOrEqual(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return lessThanOrEqual(op1, op2, op1, op2);
    }

    public static BooleanBinding lessThanOrEqual(final ObservableNumberValue op1, final double op2) {
        return lessThanOrEqual(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThanOrEqual(final double op1, final ObservableNumberValue op2) {
        return lessThanOrEqual(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding lessThanOrEqual(final ObservableNumberValue op1, final float op2) {
        return lessThanOrEqual(op1, FloatConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThanOrEqual(final float op1, final ObservableNumberValue op2) {
        return lessThanOrEqual(FloatConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding lessThanOrEqual(final ObservableNumberValue op1, final long op2) {
        return lessThanOrEqual(op1, LongConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThanOrEqual(final long op1, final ObservableNumberValue op2) {
        return lessThanOrEqual(LongConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding lessThanOrEqual(final ObservableNumberValue op1, final int op2) {
        return lessThanOrEqual(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThanOrEqual(final int op1, final ObservableNumberValue op2) {
        return lessThanOrEqual(IntegerConstant.valueOf(op1), op2, op2);
    }

    // =================================================================================================================
    // Minimum

    private static NumberBinding min(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new DoubleBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected double computeValue() {
                    return Math.min(op1.doubleValue(), op2.doubleValue());
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new FloatBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected float computeValue() {
                    return Math.min(op1.floatValue(), op2.floatValue());
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new LongBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected long computeValue() {
                    return Math.min(op1.longValue(), op2.longValue());
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new IntegerBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected int computeValue() {
                    return Math.min(op1.intValue(), op2.intValue());
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static NumberBinding min(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return min(op1, op2, op1, op2);
    }

    public static DoubleBinding min(final ObservableNumberValue op1, final double op2) {
        return (DoubleBinding) min(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static DoubleBinding min(final double op1, final ObservableNumberValue op2) {
        return (DoubleBinding) min(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding min(final ObservableNumberValue op1, final float op2) {
        return min(op1, FloatConstant.valueOf(op2), op1);
    }

    public static NumberBinding min(final float op1, final ObservableNumberValue op2) {
        return min(FloatConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding min(final ObservableNumberValue op1, final long op2) {
        return min(op1, LongConstant.valueOf(op2), op1);
    }

    public static NumberBinding min(final long op1, final ObservableNumberValue op2) {
        return min(LongConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding min(final ObservableNumberValue op1, final int op2) {
        return min(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static NumberBinding min(final int op1, final ObservableNumberValue op2) {
        return min(IntegerConstant.valueOf(op1), op2, op2);
    }

    // =================================================================================================================
    // Maximum

    private static NumberBinding max(final ObservableNumberValue op1, final ObservableNumberValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        if ((op1 instanceof ObservableDoubleValue) || (op2 instanceof ObservableDoubleValue)) {
            return new DoubleBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected double computeValue() {
                    return Math.max(op1.doubleValue(), op2.doubleValue());
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableFloatValue) || (op2 instanceof ObservableFloatValue)) {
            return new FloatBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected float computeValue() {
                    return Math.max(op1.floatValue(), op2.floatValue());
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else if ((op1 instanceof ObservableLongValue) || (op2 instanceof ObservableLongValue)) {
            return new LongBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected long computeValue() {
                    return Math.max(op1.longValue(), op2.longValue());
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        } else {
            return new IntegerBinding() {
                {
                    super.bind(dependencies);
                }

                @Override
                public void dispose() {
                    super.unbind(dependencies);
                }

                @Override
                protected int computeValue() {
                    return Math.max(op1.intValue(), op2.intValue());
                }

                @Override
                public ObservableList<?> getDependencies() {
                    return (dependencies.length == 1)?
                            FXCollections.singletonObservableList(dependencies[0])
                            : new ImmutableObservableList<Observable>(dependencies);
                }
            };
        }
    }

    public static NumberBinding max(final ObservableNumberValue op1, final ObservableNumberValue op2) {
        return max(op1, op2, op1, op2);
    }

    public static DoubleBinding max(final ObservableNumberValue op1, final double op2) {
        return (DoubleBinding) max(op1, DoubleConstant.valueOf(op2), op1);
    }

    public static DoubleBinding max(final double op1, final ObservableNumberValue op2) {
        return (DoubleBinding) max(DoubleConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding max(final ObservableNumberValue op1, final float op2) {
        return max(op1, FloatConstant.valueOf(op2), op1);
    }

    public static NumberBinding max(final float op1, final ObservableNumberValue op2) {
        return max(FloatConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding max(final ObservableNumberValue op1, final long op2) {
        return max(op1, LongConstant.valueOf(op2), op1);
    }

    public static NumberBinding max(final long op1, final ObservableNumberValue op2) {
        return max(LongConstant.valueOf(op1), op2, op2);
    }

    public static NumberBinding max(final ObservableNumberValue op1, final int op2) {
        return max(op1, IntegerConstant.valueOf(op2), op1);
    }

    public static NumberBinding max(final int op1, final ObservableNumberValue op2) {
        return max(IntegerConstant.valueOf(op1), op2, op2);
    }

    // boolean
    // =================================================================================================================

     private static class BooleanAndBinding extends BooleanBinding {

        private final ObservableBooleanValue op1;
        private final ObservableBooleanValue op2;
        private final InvalidationListener observer;

        public BooleanAndBinding(ObservableBooleanValue op1, ObservableBooleanValue op2) {
            this.op1 = op1;
            this.op2 = op2;

            observer = new ShortCircuitAndInvalidator(this);

            op1.addListener(observer);
            op2.addListener(observer);
        }


        @Override
        public void dispose() {
            op1.removeListener(observer);
            op2.removeListener(observer);
        }

        @Override
        protected boolean computeValue() {
            return op1.get() && op2.get();
        }

        @Override
        public ObservableList<?> getDependencies() {
            return new ImmutableObservableList<>(op1, op2);
        }
    }

    private static class ShortCircuitAndInvalidator implements InvalidationListener {

        private final WeakReference<BooleanAndBinding> ref;

        private ShortCircuitAndInvalidator(BooleanAndBinding binding) {
            assert binding != null;
            ref = new WeakReference<>(binding);
        }

        @Override
        public void invalidated(Observable observable) {
            final BooleanAndBinding binding = ref.get();
            if (binding == null) {
                observable.removeListener(this);
            } else {
                // short-circuit invalidation. This BooleanBinding becomes
                // only invalid if the first operator changes or the
                // first parameter is true.
                if ((binding.op1.equals(observable) || (binding.isValid() && binding.op1.get()))) {
                    binding.invalidate();
                }
            }
        }

    }

    public static BooleanBinding and(final ObservableBooleanValue op1, final ObservableBooleanValue op2) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new BooleanAndBinding(op1, op2);
    }

    private static class BooleanOrBinding extends BooleanBinding {

        private final ObservableBooleanValue op1;
        private final ObservableBooleanValue op2;
        private final InvalidationListener observer;

        public BooleanOrBinding(ObservableBooleanValue op1, ObservableBooleanValue op2) {
            this.op1 = op1;
            this.op2 = op2;
            observer = new ShortCircuitOrInvalidator(this);
            op1.addListener(observer);
            op2.addListener(observer);
        }


        @Override
        public void dispose() {
            op1.removeListener(observer);
            op2.removeListener(observer);
        }

        @Override
        protected boolean computeValue() {
            return op1.get() || op2.get();
        }

        @Override
        public ObservableList<?> getDependencies() {
            return new ImmutableObservableList<>(op1, op2);
        }
    }


    private static class ShortCircuitOrInvalidator implements InvalidationListener {

        private final WeakReference<BooleanOrBinding> ref;

        private ShortCircuitOrInvalidator(BooleanOrBinding binding) {
            assert binding != null;
            ref = new WeakReference<>(binding);
        }

        @Override
        public void invalidated(Observable observable) {
            final BooleanOrBinding binding = ref.get();
            if (binding == null) {
                observable.removeListener(this);
            } else {
                // short circuit invalidation. This BooleanBinding becomes
                // only invalid if the first operator changes or the
                // first parameter is false.
                if ((binding.op1.equals(observable) || (binding.isValid() && !binding.op1.get()))) {
                    binding.invalidate();
                }
            }
        }

    }

    public static BooleanBinding or(final ObservableBooleanValue op1, final ObservableBooleanValue op2) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new BooleanOrBinding(op1, op2);
    }

    public static BooleanBinding not(final ObservableBooleanValue op) {
        if (op == null) {
            throw new NullPointerException("Operand cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return !op.get();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static BooleanBinding equal(final ObservableBooleanValue op1, final ObservableBooleanValue op2) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op1, op2);
            }

            @Override
            public void dispose() {
                super.unbind(op1, op2);
            }

            @Override
            protected boolean computeValue() {
                return op1.get() == op2.get();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<ObservableBooleanValue>(op1, op2);
            }
        };
    }

    public static BooleanBinding notEqual(final ObservableBooleanValue op1, final ObservableBooleanValue op2) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op1, op2);
            }

            @Override
            public void dispose() {
                super.unbind(op1, op2);
            }

            @Override
            protected boolean computeValue() {
                return op1.get() != op2.get();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<ObservableBooleanValue>(op1, op2);
            }
        };
    }

    // String
    // =================================================================================================================

    public static StringExpression convert(ObservableValue<?> observableValue) {
        return StringFormatter.convert(observableValue);
    }

    public static StringExpression concat(Object... args) {
        return StringFormatter.concat(args);
    }

    public static StringExpression format(String format, Object... args) {
        return StringFormatter.format(format, args);
    }

    public static StringExpression format(Locale locale, String format,
            Object... args) {
        return StringFormatter.format(locale, format, args);
    }

    private static String getStringSafe(String value) {
        return value == null ? "" : value;
    }

    private static BooleanBinding equal(final ObservableStringValue op1, final ObservableStringValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        return new BooleanBinding() {
            {
                super.bind(dependencies);
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                final String s1 = getStringSafe(op1.get());
                final String s2 = getStringSafe(op2.get());
                return s1.equals(s2);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static BooleanBinding equal(final ObservableStringValue op1, final ObservableStringValue op2) {
        return equal(op1, op2, op1, op2);
    }

    public static BooleanBinding equal(final ObservableStringValue op1, String op2) {
        return equal(op1, StringConstant.valueOf(op2), op1);
    }

    public static BooleanBinding equal(String op1, final ObservableStringValue op2) {
        return equal(StringConstant.valueOf(op1), op2, op2);
    }

    private static BooleanBinding notEqual(final ObservableStringValue op1, final ObservableStringValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        return new BooleanBinding() {
            {
                super.bind(dependencies);
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                final String s1 = getStringSafe(op1.get());
                final String s2 = getStringSafe(op2.get());
                return ! s1.equals(s2);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static BooleanBinding notEqual(final ObservableStringValue op1, final ObservableStringValue op2) {
        return notEqual(op1, op2, op1, op2);
    }

    public static BooleanBinding notEqual(final ObservableStringValue op1, String op2) {
        return notEqual(op1, StringConstant.valueOf(op2), op1);
    }

    public static BooleanBinding notEqual(String op1, final ObservableStringValue op2) {
        return notEqual(StringConstant.valueOf(op1), op2, op2);
    }

    private static BooleanBinding equalIgnoreCase(final ObservableStringValue op1, final ObservableStringValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        return new BooleanBinding() {
            {
                super.bind(dependencies);
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                final String s1 = getStringSafe(op1.get());
                final String s2 = getStringSafe(op2.get());
                return s1.equalsIgnoreCase(s2);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static BooleanBinding equalIgnoreCase(final ObservableStringValue op1, final ObservableStringValue op2) {
        return equalIgnoreCase(op1, op2, op1, op2);
    }

    public static BooleanBinding equalIgnoreCase(final ObservableStringValue op1, String op2) {
        return equalIgnoreCase(op1, StringConstant.valueOf(op2), op1);
    }

    public static BooleanBinding equalIgnoreCase(String op1, final ObservableStringValue op2) {
        return equalIgnoreCase(StringConstant.valueOf(op1), op2, op2);
    }

    private static BooleanBinding notEqualIgnoreCase(final ObservableStringValue op1, final ObservableStringValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        return new BooleanBinding() {
            {
                super.bind(dependencies);
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                final String s1 = getStringSafe(op1.get());
                final String s2 = getStringSafe(op2.get());
                return ! s1.equalsIgnoreCase(s2);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static BooleanBinding notEqualIgnoreCase(final ObservableStringValue op1, final ObservableStringValue op2) {
        return notEqualIgnoreCase(op1, op2, op1, op2);
    }

    public static BooleanBinding notEqualIgnoreCase(final ObservableStringValue op1, String op2) {
        return notEqualIgnoreCase(op1, StringConstant.valueOf(op2), op1);
    }

    public static BooleanBinding notEqualIgnoreCase(String op1, final ObservableStringValue op2) {
        return notEqualIgnoreCase(StringConstant.valueOf(op1), op2, op2);
    }

    private static BooleanBinding greaterThan(final ObservableStringValue op1, final ObservableStringValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        return new BooleanBinding() {
            {
                super.bind(dependencies);
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                final String s1 = getStringSafe(op1.get());
                final String s2 = getStringSafe(op2.get());
                return s1.compareTo(s2) > 0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static BooleanBinding greaterThan(final ObservableStringValue op1, final ObservableStringValue op2) {
        return greaterThan(op1, op2, op1, op2);
    }

    public static BooleanBinding greaterThan(final ObservableStringValue op1, String op2) {
        return greaterThan(op1, StringConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThan(String op1, final ObservableStringValue op2) {
        return greaterThan(StringConstant.valueOf(op1), op2, op2);
    }

    private static BooleanBinding lessThan(final ObservableStringValue op1, final ObservableStringValue op2, final Observable... dependencies) {
        return greaterThan(op2, op1, dependencies);
    }

    public static BooleanBinding lessThan(final ObservableStringValue op1, final ObservableStringValue op2) {
        return lessThan(op1, op2, op1, op2);
    }

    public static BooleanBinding lessThan(final ObservableStringValue op1, String op2) {
        return lessThan(op1, StringConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThan(String op1, final ObservableStringValue op2) {
        return lessThan(StringConstant.valueOf(op1), op2, op2);
    }

    private static BooleanBinding greaterThanOrEqual(final ObservableStringValue op1, final ObservableStringValue op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        return new BooleanBinding() {
            {
                super.bind(dependencies);
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                final String s1 = getStringSafe(op1.get());
                final String s2 = getStringSafe(op2.get());
                return s1.compareTo(s2) >= 0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static BooleanBinding greaterThanOrEqual(final ObservableStringValue op1, final ObservableStringValue op2) {
        return greaterThanOrEqual(op1, op2, op1, op2);
    }

    public static BooleanBinding greaterThanOrEqual(final ObservableStringValue op1, String op2) {
        return greaterThanOrEqual(op1, StringConstant.valueOf(op2), op1);
    }

    public static BooleanBinding greaterThanOrEqual(String op1, final ObservableStringValue op2) {
        return greaterThanOrEqual(StringConstant.valueOf(op1), op2, op2);
    }

    private static BooleanBinding lessThanOrEqual(final ObservableStringValue op1, final ObservableStringValue op2, final Observable... dependencies) {
        return greaterThanOrEqual(op2, op1, dependencies);
    }

    public static BooleanBinding lessThanOrEqual(final ObservableStringValue op1, final ObservableStringValue op2) {
        return lessThanOrEqual(op1, op2, op1, op2);
    }

    public static BooleanBinding lessThanOrEqual(final ObservableStringValue op1, String op2) {
        return lessThanOrEqual(op1, StringConstant.valueOf(op2), op1);
    }

    public static BooleanBinding lessThanOrEqual(String op1, final ObservableStringValue op2) {
        return lessThanOrEqual(StringConstant.valueOf(op1), op2, op2);
    }

    public static IntegerBinding length(final ObservableStringValue op) {
        if (op == null) {
            throw new NullPointerException("Operand cannot be null");
        }

        return new IntegerBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected int computeValue() {
                return getStringSafe(op.get()).length();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static BooleanBinding isEmpty(final ObservableStringValue op) {
        if (op == null) {
            throw new NullPointerException("Operand cannot be null");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return getStringSafe(op.get()).isEmpty();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static BooleanBinding isNotEmpty(final ObservableStringValue op) {
        if (op == null) {
            throw new NullPointerException("Operand cannot be null");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return !getStringSafe(op.get()).isEmpty();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    // Object
    // =================================================================================================================

    private static BooleanBinding equal(final ObservableObjectValue<?> op1, final ObservableObjectValue<?> op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        return new BooleanBinding() {
            {
                super.bind(dependencies);
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                final Object obj1 = op1.get();
                final Object obj2 = op2.get();
                return obj1 == null ? obj2 == null : obj1.equals(obj2);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static BooleanBinding equal(final ObservableObjectValue<?> op1, final ObservableObjectValue<?> op2) {
        return equal(op1, op2, op1, op2);
    }

    public static BooleanBinding equal(final ObservableObjectValue<?> op1, Object op2) {
        return equal(op1, ObjectConstant.valueOf(op2), op1);
    }

    public static BooleanBinding equal(Object op1, final ObservableObjectValue<?> op2) {
        return equal(ObjectConstant.valueOf(op1), op2, op2);
    }

    private static BooleanBinding notEqual(final ObservableObjectValue<?> op1, final ObservableObjectValue<?> op2, final Observable... dependencies) {
        if ((op1 == null) || (op2 == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }
        assert (dependencies != null) && (dependencies.length > 0);

        return new BooleanBinding() {
            {
                super.bind(dependencies);
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            protected boolean computeValue() {
                final Object obj1 = op1.get();
                final Object obj2 = op2.get();
                return obj1 == null ? obj2 != null : ! obj1.equals(obj2);
            }

            @Override
            public ObservableList<?> getDependencies() {
                return (dependencies.length == 1)?
                        FXCollections.singletonObservableList(dependencies[0])
                        : new ImmutableObservableList<Observable>(dependencies);
            }
        };
    }

    public static BooleanBinding notEqual(final ObservableObjectValue<?> op1, final ObservableObjectValue<?> op2) {
        return notEqual(op1, op2, op1, op2);
    }

    public static BooleanBinding notEqual(final ObservableObjectValue<?> op1, Object op2) {
        return notEqual(op1, ObjectConstant.valueOf(op2), op1);
    }

    public static BooleanBinding notEqual(Object op1, final ObservableObjectValue<?> op2) {
        return notEqual(ObjectConstant.valueOf(op1), op2, op2);
    }

    public static BooleanBinding isNull(final ObservableObjectValue<?> op) {
        if (op == null) {
            throw new NullPointerException("Operand cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return op.get() == null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static BooleanBinding isNotNull(final ObservableObjectValue<?> op) {
        if (op == null) {
            throw new NullPointerException("Operand cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return op.get() != null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    // List
    // =================================================================================================================

    public static <E> IntegerBinding size(final ObservableList<E> op) {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }

        return new IntegerBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected int computeValue() {
                return op.size();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <E> BooleanBinding isEmpty(final ObservableList<E> op) {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return op.isEmpty();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <E> BooleanBinding isNotEmpty(final ObservableList<E> op)     {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return !op.isEmpty();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <E> ObjectBinding<E> valueAt(final ObservableList<E> op, final int index) {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        return new ObjectBinding<E>() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected E computeValue() {
                try {
                    return op.get(index);
                } catch (IndexOutOfBoundsException ex) {

                }
                return null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <E> ObjectBinding<E> valueAt(final ObservableList<E> op, final ObservableIntegerValue index) {
        return valueAt(op, (ObservableNumberValue)index);
    }

    public static <E> ObjectBinding<E> valueAt(final ObservableList<E> op, final ObservableNumberValue index) {
        if ((op == null) || (index == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new ObjectBinding<E>() {
            {
                super.bind(op, index);
            }

            @Override
            public void dispose() {
                super.unbind(op, index);
            }

            @Override
            protected E computeValue() {
                try {
                    return op.get(index.intValue());
                } catch (IndexOutOfBoundsException ex) {

                }
                return null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, index);
            }
        };
    }

    public static BooleanBinding booleanValueAt(final ObservableList<Boolean> op, final int index) {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                try {
                    final Boolean value = op.get(index);
                    if (value == null) {

                    } else {
                        return value;
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return false;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static BooleanBinding booleanValueAt(final ObservableList<Boolean> op, final ObservableIntegerValue index) {
        return booleanValueAt(op, (ObservableNumberValue)index);
    }

    public static BooleanBinding booleanValueAt(final ObservableList<Boolean> op, final ObservableNumberValue index) {
        if ((op == null) || (index == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op, index);
            }

            @Override
            public void dispose() {
                super.unbind(op, index);
            }

            @Override
            protected boolean computeValue() {
                try {
                    final Boolean value = op.get(index.intValue());
                    if (value == null) {

                    } else {
                        return value;
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return false;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, index);
            }
        };
    }

    public static DoubleBinding doubleValueAt(final ObservableList<? extends Number> op, final int index) {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        return new DoubleBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected double computeValue() {
                try {
                    final Number value = op.get(index);
                    if (value == null) {

                    } else {
                        return value.doubleValue();
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0.0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static DoubleBinding doubleValueAt(final ObservableList<? extends Number> op, final ObservableIntegerValue index) {
        return doubleValueAt(op, (ObservableNumberValue)index);
    }

    public static DoubleBinding doubleValueAt(final ObservableList<? extends Number> op, final ObservableNumberValue index) {
        if ((op == null) || (index == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new DoubleBinding() {
            {
                super.bind(op, index);
            }

            @Override
            public void dispose() {
                super.unbind(op, index);
            }

            @Override
            protected double computeValue() {
                try {
                    final Number value = op.get(index.intValue());
                    if (value == null) {

                    } else {
                        return value.doubleValue();
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0.0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, index);
            }
        };
    }

    public static FloatBinding floatValueAt(final ObservableList<? extends Number> op, final int index) {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        return new FloatBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected float computeValue() {
                try {
                    final Number value = op.get(index);
                    if (value == null) {

                    } else {
                        return value.floatValue();
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0.0f;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static FloatBinding floatValueAt(final ObservableList<? extends Number> op, final ObservableIntegerValue index) {
        return floatValueAt(op, (ObservableNumberValue)index);
    }

    public static FloatBinding floatValueAt(final ObservableList<? extends Number> op, final ObservableNumberValue index) {
        if ((op == null) || (index == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new FloatBinding() {
            {
                super.bind(op, index);
            }

            @Override
            public void dispose() {
                super.unbind(op, index);
            }

            @Override
            protected float computeValue() {
                try {
                    final Number value = op.get(index.intValue());
                    if (value == null) {

                    } else {
                        return value.floatValue();
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0.0f;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, index);
            }
        };
    }

    public static IntegerBinding integerValueAt(final ObservableList<? extends Number> op, final int index) {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        return new IntegerBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected int computeValue() {
                try {
                    final Number value = op.get(index);
                    if (value == null) {

                    } else {
                        return value.intValue();
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static IntegerBinding integerValueAt(final ObservableList<? extends Number> op, final ObservableIntegerValue index) {
        return integerValueAt(op, (ObservableNumberValue)index);
    }

    public static IntegerBinding integerValueAt(final ObservableList<? extends Number> op, final ObservableNumberValue index) {
        if ((op == null) || (index == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new IntegerBinding() {
            {
                super.bind(op, index);
            }

            @Override
            public void dispose() {
                super.unbind(op, index);
            }

            @Override
            protected int computeValue() {
                try {
                    final Number value = op.get(index.intValue());
                    if (value == null) {

                    } else {
                        return value.intValue();
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, index);
            }
        };
    }

    public static LongBinding longValueAt(final ObservableList<? extends Number> op, final int index) {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        return new LongBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected long computeValue() {
                try {
                    final Number value = op.get(index);
                    if (value == null) {

                    } else {
                        return value.longValue();
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0L;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static LongBinding longValueAt(final ObservableList<? extends Number> op, final ObservableIntegerValue index) {
        return longValueAt(op, (ObservableNumberValue)index);
    }

    public static LongBinding longValueAt(final ObservableList<? extends Number> op, final ObservableNumberValue index) {
        if ((op == null) || (index == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new LongBinding() {
            {
                super.bind(op, index);
            }

            @Override
            public void dispose() {
                super.unbind(op, index);
            }

            @Override
            protected long computeValue() {
                try {
                    final Number value = op.get(index.intValue());
                    if (value == null) {

                    } else {
                        return value.longValue();
                    }
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0L;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, index);
            }
        };
    }

    public static StringBinding stringValueAt(final ObservableList<String> op, final int index) {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        return new StringBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected String computeValue() {
                try {
                    return op.get(index);
                } catch (IndexOutOfBoundsException ex) {

                }
                return null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static StringBinding stringValueAt(final ObservableList<String> op, final ObservableIntegerValue index) {
        return stringValueAt(op, (ObservableNumberValue)index);
    }

    public static StringBinding stringValueAt(final ObservableList<String> op, final ObservableNumberValue index) {
        if ((op == null) || (index == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new StringBinding() {
            {
                super.bind(op, index);
            }

            @Override
            public void dispose() {
                super.unbind(op, index);
            }

            @Override
            protected String computeValue() {
                try {
                    return op.get(index.intValue());
                } catch (IndexOutOfBoundsException ex) {

                }
                return null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, index);
            }
        };
    }

    // Set
    // =================================================================================================================

    public static <E> IntegerBinding size(final ObservableSet<E> op) {
        if (op == null) {
            throw new NullPointerException("Set cannot be null.");
        }

        return new IntegerBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected int computeValue() {
                return op.size();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <E> BooleanBinding isEmpty(final ObservableSet<E> op) {
        if (op == null) {
            throw new NullPointerException("Set cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return op.isEmpty();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <E> BooleanBinding isNotEmpty(final ObservableSet<E> op)     {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return !op.isEmpty();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    // Array
    // =================================================================================================================

    public static IntegerBinding size(final ObservableArray op) {
        if (op == null) {
            throw new NullPointerException("Array cannot be null.");
        }

        return new IntegerBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected int computeValue() {
                return op.size();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static FloatBinding floatValueAt(final ObservableFloatArray op, final int index) {
        if (op == null) {
            throw new NullPointerException("Array cannot be null.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        return new FloatBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected float computeValue() {
                try {
                    return op.get(index);
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0.0f;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static FloatBinding floatValueAt(final ObservableFloatArray op, final ObservableIntegerValue index) {
        return floatValueAt(op, (ObservableNumberValue)index);
    }

    public static FloatBinding floatValueAt(final ObservableFloatArray op, final ObservableNumberValue index) {
        if ((op == null) || (index == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new FloatBinding() {
            {
                super.bind(op, index);
            }

            @Override
            public void dispose() {
                super.unbind(op, index);
            }

            @Override
            protected float computeValue() {
                try {
                    return op.get(index.intValue());
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0.0f;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<>(op, index);
            }
        };
    }

    public static IntegerBinding integerValueAt(final ObservableIntegerArray op, final int index) {
        if (op == null) {
            throw new NullPointerException("Array cannot be null.");
        }
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative");
        }

        return new IntegerBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected int computeValue() {
                try {
                    return op.get(index);
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static IntegerBinding integerValueAt(final ObservableIntegerArray op, final ObservableIntegerValue index) {
        return integerValueAt(op, (ObservableNumberValue)index);
    }

    public static IntegerBinding integerValueAt(final ObservableIntegerArray op, final ObservableNumberValue index) {
        if ((op == null) || (index == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new IntegerBinding() {
            {
                super.bind(op, index);
            }

            @Override
            public void dispose() {
                super.unbind(op, index);
            }

            @Override
            protected int computeValue() {
                try {
                    return op.get(index.intValue());
                } catch (IndexOutOfBoundsException ex) {

                }
                return 0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<>(op, index);
            }
        };
    }

    // Map
    // =================================================================================================================

    public static <K, V> IntegerBinding size(final ObservableMap<K, V> op) {
        if (op == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        return new IntegerBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected int computeValue() {
                return op.size();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K, V> BooleanBinding isEmpty(final ObservableMap<K, V> op) {
        if (op == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return op.isEmpty();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K, V> BooleanBinding isNotEmpty(final ObservableMap<K, V> op)     {
        if (op == null) {
            throw new NullPointerException("List cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                return !op.isEmpty();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K, V> ObjectBinding<V> valueAt(final ObservableMap<K, V> op, final K key) {
        if (op == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        return new ObjectBinding<V>() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected V computeValue() {
                try {
                    return op.get(key);
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K, V> ObjectBinding<V> valueAt(final ObservableMap<K, V> op, final ObservableValue<? extends K> key) {
        if ((op == null) || (key == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new ObjectBinding<V>() {
            {
                super.bind(op, key);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected V computeValue() {
                try {
                    return op.get(key.getValue());
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, key);
            }
        };
    }

    public static <K> BooleanBinding booleanValueAt(final ObservableMap<K, Boolean> op, final K key) {
        if (op == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected boolean computeValue() {
                try {
                    final Boolean value = op.get(key);
                    if (value == null) {

                    } else {
                        return value;
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return false;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K> BooleanBinding booleanValueAt(final ObservableMap<K, Boolean> op, final ObservableValue<? extends K> key) {
        if ((op == null) || (key == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new BooleanBinding() {
            {
                super.bind(op, key);
            }

            @Override
            public void dispose() {
                super.unbind(op, key);
            }

            @Override
            protected boolean computeValue() {
                try {
                    final Boolean value = op.get(key.getValue());
                    if (value == null) {

                    } else {
                        return value;
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return false;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, key);
            }
        };
    }

    public static <K> DoubleBinding doubleValueAt(final ObservableMap<K, ? extends Number> op, final K key) {
        if (op == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        return new DoubleBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected double computeValue() {
                try {
                    final Number value = op.get(key);
                    if (value == null) {

                    } else {
                        return value.doubleValue();
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return 0.0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K> DoubleBinding doubleValueAt(final ObservableMap<K, ? extends Number> op, final ObservableValue<? extends K> key) {
        if ((op == null) || (key == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new DoubleBinding() {
            {
                super.bind(op, key);
            }

            @Override
            public void dispose() {
                super.unbind(op, key);
            }

            @Override
            protected double computeValue() {
                try {
                    final Number value = op.get(key.getValue());
                    if (value == null) {
                    } else {
                        return value.doubleValue();
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return 0.0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, key);
            }
        };
    }

    public static <K> FloatBinding floatValueAt(final ObservableMap<K, ? extends Number> op, final K key) {
        if (op == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        return new FloatBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected float computeValue() {
                try {
                    final Number value = op.get(key);
                    if (value == null) {
                    } else {
                        return value.floatValue();
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return 0.0f;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K> FloatBinding floatValueAt(final ObservableMap<K, ? extends Number> op, final ObservableValue<? extends K> key) {
        if ((op == null) || (key == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new FloatBinding() {
            {
                super.bind(op, key);
            }

            @Override
            public void dispose() {
                super.unbind(op, key);
            }

            @Override
            protected float computeValue() {
                try {
                    final Number value = op.get(key.getValue());
                    if (value == null) {
                    } else {
                        return value.floatValue();
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return 0.0f;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, key);
            }
        };
    }

    public static <K> IntegerBinding integerValueAt(final ObservableMap<K, ? extends Number> op, final K key) {
        if (op == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        return new IntegerBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected int computeValue() {
                try {
                    final Number value = op.get(key);
                    if (value == null) {
                    } else {
                        return value.intValue();
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return 0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K> IntegerBinding integerValueAt(final ObservableMap<K, ? extends Number> op, final ObservableValue<? extends K> key) {
        if ((op == null) || (key == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new IntegerBinding() {
            {
                super.bind(op, key);
            }

            @Override
            public void dispose() {
                super.unbind(op, key);
            }

            @Override
            protected int computeValue() {
                try {
                    final Number value = op.get(key.getValue());
                    if (value == null) {
                    } else {
                        return value.intValue();
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return 0;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, key);
            }
        };
    }

    public static <K> LongBinding longValueAt(final ObservableMap<K, ? extends Number> op, final K key) {
        if (op == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        return new LongBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected long computeValue() {
                try {
                    final Number value = op.get(key);
                    if (value == null) {
                    } else {
                        return value.longValue();
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return 0L;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K> LongBinding longValueAt(final ObservableMap<K, ? extends Number> op, final ObservableValue<? extends K> key) {
        if ((op == null) || (key == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new LongBinding() {
            {
                super.bind(op, key);
            }

            @Override
            public void dispose() {
                super.unbind(op, key);
            }

            @Override
            protected long computeValue() {
                try {
                    final Number value = op.get(key.getValue());
                    if (value == null) {
                    } else {
                        return value.longValue();
                    }
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return 0L;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, key);
            }
        };
    }

    public static <K> StringBinding stringValueAt(final ObservableMap<K, String> op, final K key) {
        if (op == null) {
            throw new NullPointerException("Map cannot be null.");
        }

        return new StringBinding() {
            {
                super.bind(op);
            }

            @Override
            public void dispose() {
                super.unbind(op);
            }

            @Override
            protected String computeValue() {
                try {
                    return op.get(key);
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(op);
            }
        };
    }

    public static <K> StringBinding stringValueAt(final ObservableMap<K, String> op, final ObservableValue<? extends K> key) {
        if ((op == null) || (key == null)) {
            throw new NullPointerException("Operands cannot be null.");
        }

        return new StringBinding() {
            {
                super.bind(op, key);
            }

            @Override
            public void dispose() {
                super.unbind(op, key);
            }

            @Override
            protected String computeValue() {
                try {
                    return op.get(key.getValue());
                } catch (ClassCastException ex) {
                    // ignore
                } catch (NullPointerException ex) {
                    // ignore
                }
                return null;
            }

            @Override
            public ObservableList<?> getDependencies() {
                return new ImmutableObservableList<Observable>(op, key);
            }
        };
    }


}
