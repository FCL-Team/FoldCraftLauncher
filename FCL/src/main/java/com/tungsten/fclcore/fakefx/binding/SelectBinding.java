package com.tungsten.fclcore.fakefx.binding;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.WeakInvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.Binding;
import com.tungsten.fclcore.fakefx.beans.binding.BooleanBinding;
import com.tungsten.fclcore.fakefx.beans.binding.DoubleBinding;
import com.tungsten.fclcore.fakefx.beans.binding.FloatBinding;
import com.tungsten.fclcore.fakefx.beans.binding.IntegerBinding;
import com.tungsten.fclcore.fakefx.beans.binding.LongBinding;
import com.tungsten.fclcore.fakefx.beans.binding.ObjectBinding;
import com.tungsten.fclcore.fakefx.beans.binding.StringBinding;
import com.tungsten.fclcore.fakefx.beans.value.ObservableBooleanValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableNumberValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.property.JavaBeanAccessHelper;
import com.tungsten.fclcore.fakefx.property.PropertyReference;

import java.util.Arrays;

/**
 * A binding used to get a member, such as <code>a.b.c</code>. The value of the
 * binding will be "c", or null if c could not be reached (due to "b" not having
 * a "c" property, or "b" being null). "a" must be passed to the constructor of
 * the SelectBinding and may be any dependency. All subsequent links are simply
 * PropertyReferences.
 * <p>
 * With a SelectBinding, "a" must always exist. Usually "a" will refer to
 * "this", or some concrete object. "b"* will be some intermediate step in the
 * select binding.
 */
public class SelectBinding {

    private SelectBinding() {}

    public static class AsObject<T> extends ObjectBinding<T> {

        private final SelectBindingHelper helper;

        public AsObject(ObservableValue<?> root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        public AsObject(Object root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        @Override
        public void dispose() {
            helper.unregisterListener();
        }

        @Override
        protected void onInvalidating() {
            helper.unregisterListener();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected T computeValue() {
            final ObservableValue<?> observable = helper.getObservableValue();
            if (observable == null) {
                return null;
            }
            try {
                return (T)observable.getValue();
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
            return null;
        }


        @Override
        public ObservableList<ObservableValue<?>> getDependencies() {
            return helper.getDependencies();
        }

    }

    public static class AsBoolean extends BooleanBinding {

        private static final boolean DEFAULT_VALUE = false;

        private final SelectBindingHelper helper;

        public AsBoolean(ObservableValue<?> root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        public AsBoolean(Object root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        @Override
        public void dispose() {
            helper.unregisterListener();
        }

        @Override
        protected void onInvalidating() {
            helper.unregisterListener();
        }

        @Override
        protected boolean computeValue() {
            final ObservableValue<?> observable = helper.getObservableValue();
            if (observable == null) {
                return DEFAULT_VALUE;
            }
            if (observable instanceof ObservableBooleanValue) {
                return ((ObservableBooleanValue)observable).get();
            }
            try {
                return (Boolean)observable.getValue();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
            return DEFAULT_VALUE;
        }

        @Override
        public ObservableList<ObservableValue<?>> getDependencies() {
            return helper.getDependencies();
        }

    }

    public static class AsDouble extends DoubleBinding {

        private static final double DEFAULT_VALUE = 0.0;

        private final SelectBindingHelper helper;

        public AsDouble(ObservableValue<?> root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        public AsDouble(Object root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        @Override
        public void dispose() {
            helper.unregisterListener();
        }

        @Override
        protected void onInvalidating() {
            helper.unregisterListener();
        }

        @Override
        protected double computeValue() {
            final ObservableValue<?> observable = helper.getObservableValue();
            if (observable == null) {
                return DEFAULT_VALUE;
            }
            if (observable instanceof ObservableNumberValue) {
                return ((ObservableNumberValue)observable).doubleValue();
            }
            try {
                return ((Number)observable.getValue()).doubleValue();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
            return DEFAULT_VALUE;
        }

        @Override
        public ObservableList<ObservableValue<?>> getDependencies() {
            return helper.getDependencies();
        }

    }

    public static class AsFloat extends FloatBinding {

        private static final float DEFAULT_VALUE = 0.0f;

        private final SelectBindingHelper helper;

        public AsFloat(ObservableValue<?> root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        public AsFloat(Object root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        @Override
        public void dispose() {
            helper.unregisterListener();
        }

        @Override
        protected void onInvalidating() {
            helper.unregisterListener();
        }

        @Override
        protected float computeValue() {
            final ObservableValue<?> observable = helper.getObservableValue();
            if (observable == null) {
                return DEFAULT_VALUE;
            }
            if (observable instanceof ObservableNumberValue) {
                return ((ObservableNumberValue)observable).floatValue();
            }
            try {
                return ((Number)observable.getValue()).floatValue();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
            return DEFAULT_VALUE;
        }

        @Override
        public ObservableList<ObservableValue<?>> getDependencies() {
            return helper.getDependencies();
        }

    }

    public static class AsInteger extends IntegerBinding {

        private static final int DEFAULT_VALUE = 0;

        private final SelectBindingHelper helper;

        public AsInteger(ObservableValue<?> root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        public AsInteger(Object root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        @Override
        public void dispose() {
            helper.unregisterListener();
        }

        @Override
        protected void onInvalidating() {
            helper.unregisterListener();
        }

        @Override
        protected int computeValue() {
            final ObservableValue<?> observable = helper.getObservableValue();
            if (observable == null) {
                return DEFAULT_VALUE;
            }
            if (observable instanceof ObservableNumberValue) {
                return ((ObservableNumberValue)observable).intValue();
            }
            try {
                return ((Number)observable.getValue()).intValue();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
            return DEFAULT_VALUE;
        }

        @Override
        public ObservableList<ObservableValue<?>> getDependencies() {
            return helper.getDependencies();
        }

    }

    public static class AsLong extends LongBinding {

        private static final long DEFAULT_VALUE = 0L;

        private final SelectBindingHelper helper;

        public AsLong(ObservableValue<?> root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        public AsLong(Object root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        @Override
        public void dispose() {
            helper.unregisterListener();
        }

        @Override
        protected void onInvalidating() {
            helper.unregisterListener();
        }

        @Override
        protected long computeValue() {
            final ObservableValue<?> observable = helper.getObservableValue();
            if (observable == null) {
                return DEFAULT_VALUE;
            }
            if (observable instanceof ObservableNumberValue) {
                return ((ObservableNumberValue)observable).longValue();
            }
            try {
                return ((Number)observable.getValue()).longValue();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
            return DEFAULT_VALUE;
        }

        @Override
        public ObservableList<ObservableValue<?>> getDependencies() {
            return helper.getDependencies();
        }

    }

    public static class AsString extends StringBinding {

        private static final String DEFAULT_VALUE = null;

        private final SelectBindingHelper helper;

        public AsString(ObservableValue<?> root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        public AsString(Object root, String... steps) {
            helper = new SelectBindingHelper(this, root, steps);
        }

        @Override
        public void dispose() {
            helper.unregisterListener();
        }

        @Override
        protected void onInvalidating() {
            helper.unregisterListener();
        }

        @Override
        protected String computeValue() {
            final ObservableValue<?> observable = helper.getObservableValue();
            if (observable == null) {
                return DEFAULT_VALUE;
            }
            try {
                return observable.getValue().toString();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                // return default
                return DEFAULT_VALUE;
            }
        }

        @Override
        public ObservableList<ObservableValue<?>> getDependencies() {
            return helper.getDependencies();
        }

    }

    private static class SelectBindingHelper implements InvalidationListener {

        private final Binding<?> binding;
        private final String[] propertyNames;
        private final ObservableValue<?>[] properties;
        private final PropertyReference<?>[] propRefs;
        private final WeakInvalidationListener observer;

        private ObservableList<ObservableValue<?>> dependencies;

        private SelectBindingHelper(Binding<?> binding, ObservableValue<?> firstProperty, String... steps) {
            if (firstProperty == null) {
                throw new NullPointerException("Must specify the root");
            }
            if (steps == null) {
                steps = new String[0];
            }

            this.binding = binding;

            final int n = steps.length;
            for (int i = 0; i < n; i++) {
                if (steps[i] == null) {
                    throw new NullPointerException("all steps must be specified");
                }
            }

            observer = new WeakInvalidationListener(this);
            propertyNames = new String[n];
            System.arraycopy(steps, 0, propertyNames, 0, n);
            propRefs = new PropertyReference<?>[n];
            properties = new ObservableValue<?>[n + 1];
            properties[0] = firstProperty;
            properties[0].addListener(observer);
        }

        private static ObservableValue<?> checkAndCreateFirstStep(Object root, String[] steps) {
            if (root == null || steps == null || steps[0] == null) {
                throw new NullPointerException("Must specify the root and the first property");
            }
            try {
                return JavaBeanAccessHelper.createReadOnlyJavaBeanProperty(root, steps[0]);
            } catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException("The first property '" + steps[0] + "' doesn't exist");
            }
        }

        private SelectBindingHelper(Binding<?> binding, Object root, String... steps) {
            this(binding, checkAndCreateFirstStep(root, steps), Arrays.copyOfRange(steps, 1, steps.length));
        }

        @Override
        public void invalidated(Observable observable) {
            binding.invalidate();
        }

        public ObservableValue<?> getObservableValue() {
            // Step through each of the steps, and at each step add a listener as
            // appropriate, accumulating the result.
            final int n = properties.length;
            for (int i = 0; i < n - 1; i++) {
                final Object obj = properties[i].getValue();
                try {
                    if ((propRefs[i] == null)
                            || (!obj.getClass().equals(
                            propRefs[i].getContainingClass()))) {
                        propRefs[i] = new PropertyReference<Object>(obj.getClass(),
                                propertyNames[i]);
                    }
                    if (propRefs[i].hasProperty()) {
                        properties[i + 1] = propRefs[i].getProperty(obj);
                    } else {
                        properties[i + 1] = JavaBeanAccessHelper.createReadOnlyJavaBeanProperty(obj, propRefs[i].getName());
                    }
                } catch (NoSuchMethodException ex) {
                    ex.printStackTrace();
                    // return default
                    updateDependencies();
                    return null;
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                    // return default
                    updateDependencies();
                    return null;
                }
                properties[i + 1].addListener(observer);
            }
            updateDependencies();
            final ObservableValue<?> result = properties[n-1];
            if (result == null) {

            }
            return result;
        }

        private String stepsToString() {
            return Arrays.toString(propertyNames);
        }

        private void unregisterListener() {
            final int n = properties.length;
            for (int i = 1; i < n; i++) {
                if (properties[i] == null) {
                    break;
                }
                properties[i].removeListener(observer);
                properties[i] = null;
            }
            updateDependencies();
        }

        private void updateDependencies() {
            if (dependencies != null) {
                dependencies.clear();
                final int n = properties.length;
                for (int i = 0; i < n; i++) {
                    if (properties[i] == null) {
                        break;
                    }
                    dependencies.add(properties[i]);
                }
            }
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            if (dependencies == null) {
                dependencies = FXCollections.observableArrayList();
                updateDependencies();
            }

            return FXCollections.unmodifiableObservableList(dependencies);
        }

    }

}
