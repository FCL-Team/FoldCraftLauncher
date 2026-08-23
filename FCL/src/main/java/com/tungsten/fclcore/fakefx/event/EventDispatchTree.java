package com.tungsten.fclcore.fakefx.event;

public interface EventDispatchTree extends EventDispatchChain {
    EventDispatchTree createTree();

    EventDispatchTree mergeTree(EventDispatchTree tree);

    @Override
    EventDispatchTree append(EventDispatcher eventDispatcher);

    @Override
    EventDispatchTree prepend(EventDispatcher eventDispatcher);
}
