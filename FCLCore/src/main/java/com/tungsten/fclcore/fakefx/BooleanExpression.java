package com.tungsten.fclcore.fakefx;

public abstract class BooleanExpression implements ObservableBooleanValue {

    public BooleanExpression() {
    }

    @Override
    public Boolean getValue() {
        return get();
    }

    public static BooleanExpression booleanExpression(
            final ObservableBooleanValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof BooleanExpression) ? (BooleanExpression) value
                : new BooleanBinding() {
                    {
                        super.bind(value);
                    }

                    @Override
                    public void dispose() {
                        super.unbind(value);
                    }

                    @Override
                    protected boolean computeValue() {
                        return value.get();
                    }
                };
    }

    public static BooleanExpression booleanExpression(final ObservableValue<Boolean> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return (value instanceof BooleanExpression) ? (BooleanExpression) value
                : new BooleanBinding() {
            {
                super.bind(value);
            }

            @Override
            public void dispose() {
                super.unbind(value);
            }

            @Override
            protected boolean computeValue() {
                final Boolean val = value.getValue();
                return val == null ? false : val;
            }
        };
    }
}