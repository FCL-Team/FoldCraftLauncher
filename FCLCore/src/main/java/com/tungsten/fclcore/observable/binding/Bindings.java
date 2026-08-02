package com.tungsten.fclcore.observable.binding;

import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.collections.ObservableList;
import com.tungsten.fclcore.observable.property.Property;
import com.tungsten.fclcore.observable.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Minimal reimplementation of {@code Bindings}，
 * 只保留外部实际用到的方法子集：createXxxBinding / concat / bindContent / unbindContent /
 * bindBidirectional / unbindBidirectional。
 * computeValue 抛异常时的兜底返回值与 JavaFX 一致（boolean→false、Object→null、String→""）。
 */
public final class Bindings {

    private Bindings() {
    }

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
        };
    }

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

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }
        };
    }

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

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }
        };
    }

    // ================================================================================================================
    // concat（对应 binding.StringFormatter.concat：null 值按 StringBuilder.append 语义拼为 "null"）

    private static Object extractValue(Object obj) {
        return obj instanceof ObservableValue ? ((ObservableValue<?>) obj).getValue() : obj;
    }

    private static Observable[] extractDependencies(Object... args) {
        final List<Observable> dependencies = new ArrayList<>();
        for (final Object obj : args) {
            if (obj instanceof Observable) {
                dependencies.add((Observable) obj);
            }
        }
        return dependencies.toArray(new Observable[0]);
    }

    private static StringBinding constantStringBinding(final String value) {
        return new StringBinding() {
            @Override
            protected String computeValue() {
                return value;
            }
        };
    }

    public static StringBinding concat(final Object... args) {
        if ((args == null) || (args.length == 0)) {
            return constantStringBinding("");
        }
        if (args.length == 1) {
            final Object cur = args[0];
            if (cur instanceof ObservableValue) {
                final ObservableValue<?> observableValue = (ObservableValue<?>) cur;
                return new StringBinding() {
                    {
                        super.bind(observableValue);
                    }

                    @Override
                    public void dispose() {
                        super.unbind(observableValue);
                    }

                    @Override
                    protected String computeValue() {
                        final Object value = observableValue.getValue();
                        return (value == null) ? "null" : value.toString();
                    }
                };
            }
            return constantStringBinding(String.valueOf(cur));
        }
        final Observable[] dependencies = extractDependencies(args);
        if (dependencies.length == 0) {
            final StringBuilder builder = new StringBuilder();
            for (final Object obj : args) {
                builder.append(obj);
            }
            return constantStringBinding(builder.toString());
        }
        return new StringBinding() {
            {
                super.bind(dependencies);
            }

            @Override
            public void dispose() {
                super.unbind(dependencies);
            }

            @Override
            protected String computeValue() {
                final StringBuilder builder = new StringBuilder();
                for (final Object obj : args) {
                    builder.append(extractValue(obj));
                }
                return builder.toString();
            }
        };
    }

    // ================================================================================================================
    // 内容绑定

    public static <E> void bindContent(List<E> list1, ObservableList<? extends E> list2) {
        ContentBinding.bind(list1, list2);
    }

    public static void unbindContent(Object obj1, Object obj2) {
        ContentBinding.unbind(obj1, obj2);
    }

    // ================================================================================================================
    // 双向绑定

    public static <T> void bindBidirectional(Property<T> property1, Property<T> property2) {
        BidirectionalBinding.bind(property1, property2);
    }

    public static void unbindBidirectional(Object property1, Object property2) {
        BidirectionalBinding.unbind(property1, property2);
    }
}
