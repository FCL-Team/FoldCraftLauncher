package com.tungsten.fclcore.fakefx.property.adapter;

import java.util.EventListener;

public interface VetoableChangeListener extends EventListener {
    void vetoableChange(PropertyChangeEvent var1) throws PropertyVetoException;
}