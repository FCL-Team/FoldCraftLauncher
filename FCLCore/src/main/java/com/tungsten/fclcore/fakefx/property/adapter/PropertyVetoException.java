package com.tungsten.fclcore.fakefx.property.adapter;

public class PropertyVetoException extends Exception {
    private static final long serialVersionUID = 129596057694162164L;
    private PropertyChangeEvent evt;

    public PropertyVetoException(String mess, PropertyChangeEvent evt) {
        super(mess);
        this.evt = evt;
    }

    public PropertyChangeEvent getPropertyChangeEvent() {
        return this.evt;
    }
}