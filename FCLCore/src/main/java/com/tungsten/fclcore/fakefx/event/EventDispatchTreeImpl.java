package com.tungsten.fclcore.fakefx.event;

public final class EventDispatchTreeImpl implements EventDispatchTree {
    /** Must be a power of two. */
    private static final int CAPACITY_GROWTH_FACTOR = 8;

    private static final int NULL_INDEX = -1;

    private EventDispatcher[] dispatchers;

    private int[] nextChildren;
    private int[] nextSiblings;

    private int reservedCount;
    private int rootIndex;
    private int tailFirstIndex;
    private int tailLastIndex;

    public EventDispatchTreeImpl() {
        rootIndex = NULL_INDEX;
        tailFirstIndex = NULL_INDEX;
        tailLastIndex = NULL_INDEX;
    }

    public void reset() {
        // shrink?
        for (int i = 0; i < reservedCount; ++i) {
            dispatchers[i] = null;
        }

        reservedCount = 0;
        rootIndex = NULL_INDEX;
        tailFirstIndex = NULL_INDEX;
        tailLastIndex = NULL_INDEX;
    }

    @Override
    public EventDispatchTree createTree() {
        return new EventDispatchTreeImpl();
    }

    private boolean expandTailFirstPath;

    @Override
    public EventDispatchTree mergeTree(final EventDispatchTree tree) {
        if (tailFirstIndex != NULL_INDEX) {
            if (rootIndex != NULL_INDEX) {
                expandTailFirstPath = true;
                expandTail(rootIndex);
            } else {
                rootIndex = tailFirstIndex;
            }

            tailFirstIndex = NULL_INDEX;
            tailLastIndex = NULL_INDEX;
        }

        final EventDispatchTreeImpl treeImpl = (EventDispatchTreeImpl) tree;
        int srcLevelIndex = (treeImpl.rootIndex != NULL_INDEX)
                                    ? treeImpl.rootIndex
                                    : treeImpl.tailFirstIndex;

        if (rootIndex == NULL_INDEX) {
            rootIndex = copyTreeLevel(treeImpl, srcLevelIndex);
        } else {
            mergeTreeLevel(treeImpl, rootIndex, srcLevelIndex);
        }

        return this;
    }

    @Override
    public EventDispatchTree append(final EventDispatcher eventDispatcher) {
        ensureCapacity(reservedCount + 1);

        dispatchers[reservedCount] = eventDispatcher;
        nextSiblings[reservedCount] = NULL_INDEX;
        nextChildren[reservedCount] = NULL_INDEX;
        if (tailFirstIndex == NULL_INDEX) {
            tailFirstIndex = reservedCount;
        } else {
            nextChildren[tailLastIndex] = reservedCount;
        }

        tailLastIndex = reservedCount;
        ++reservedCount;

        return this;
    }

    @Override
    public EventDispatchTree prepend(final EventDispatcher eventDispatcher) {
        ensureCapacity(reservedCount + 1);

        dispatchers[reservedCount] = eventDispatcher;
        nextSiblings[reservedCount] = NULL_INDEX;
        nextChildren[reservedCount] = rootIndex;

        rootIndex = reservedCount;
        ++reservedCount;

        return this;
    }

    @Override
    public Event dispatchEvent(final Event event) {
        if (rootIndex == NULL_INDEX) {
            if (tailFirstIndex == NULL_INDEX) {
                return event;
            }

            rootIndex = tailFirstIndex;
            tailFirstIndex = NULL_INDEX;
            tailLastIndex = NULL_INDEX;
        }

        // push current state
        final int savedReservedCount = reservedCount;
        final int savedRootIndex = rootIndex;
        final int savedTailFirstIndex = tailFirstIndex;
        final int savedTailLastIndex = tailLastIndex;

        Event returnEvent = null;
        int index = rootIndex;
        do {
            rootIndex = nextChildren[index];
            final Event branchReturnEvent =
                    dispatchers[index].dispatchEvent(event, this);
            if (branchReturnEvent != null) {
                returnEvent = (returnEvent != null) ? event
                                                    : branchReturnEvent;
            }

            index = nextSiblings[index];
        } while (index != NULL_INDEX);

        // pop saved state
        reservedCount = savedReservedCount;
        rootIndex = savedRootIndex;
        tailFirstIndex = savedTailFirstIndex;
        tailLastIndex = savedTailLastIndex;

        return returnEvent;
    }

    @Override
    public String toString() {
        int levelIndex = (rootIndex != NULL_INDEX) ? rootIndex : tailFirstIndex;
        if (levelIndex == NULL_INDEX) {
            return "()";
        }

        final StringBuilder sb = new StringBuilder();
        appendTreeLevel(sb, levelIndex);

        return sb.toString();
    }

    private void ensureCapacity(final int size) {
        final int newCapacity = (size + CAPACITY_GROWTH_FACTOR - 1)
                                    & ~(CAPACITY_GROWTH_FACTOR - 1);
        if (newCapacity == 0) {
            return;
        }

        if ((dispatchers == null) || (dispatchers.length < newCapacity)) {
            final EventDispatcher[] newDispatchers =
                    new EventDispatcher[newCapacity];
            final int[] newNextChildren = new int[newCapacity];
            final int[] newNextSiblings = new int[newCapacity];

            if (reservedCount > 0) {
                System.arraycopy(dispatchers, 0, newDispatchers, 0,
                                 reservedCount);
                System.arraycopy(nextChildren, 0, newNextChildren, 0,
                                 reservedCount);
                System.arraycopy(nextSiblings, 0, newNextSiblings, 0,
                                 reservedCount);
            }

            dispatchers = newDispatchers;
            nextChildren = newNextChildren;
            nextSiblings = newNextSiblings;
        }
    }

    private void expandTail(final int levelIndex) {
        int index = levelIndex;
        while (index != NULL_INDEX) {
            if (nextChildren[index] != NULL_INDEX) {
                expandTail(nextChildren[index]);
            } else {
                if (expandTailFirstPath) {
                    nextChildren[index] = tailFirstIndex;
                    expandTailFirstPath = false;
                } else {
                    final int childLevelIndex =
                            copyTreeLevel(this, tailFirstIndex);
                    nextChildren[index] = childLevelIndex;
                }
            }

            index = nextSiblings[index];
        }
    }

    private void mergeTreeLevel(final EventDispatchTreeImpl srcTree,
                                final int dstLevelIndex,
                                final int srcLevelIndex) {
        int srcIndex = srcLevelIndex;
        while (srcIndex != NULL_INDEX) {
            final EventDispatcher srcDispatcher = srcTree.dispatchers[srcIndex];
            int dstIndex = dstLevelIndex;
            int lastDstIndex = dstLevelIndex;

            while ((dstIndex != NULL_INDEX)
                    && (srcDispatcher != dispatchers[dstIndex])) {
                lastDstIndex = dstIndex;
                dstIndex = nextSiblings[dstIndex];
            }

            if (dstIndex == NULL_INDEX) {
                final int siblingIndex = copySubtree(srcTree, srcIndex);
                nextSiblings[lastDstIndex] = siblingIndex;
                nextSiblings[siblingIndex] = NULL_INDEX;
            } else {
                int nextDstLevelIndex = nextChildren[dstIndex];
                final int nextSrcLevelIndex = getChildIndex(srcTree, srcIndex);
                if (nextDstLevelIndex != NULL_INDEX) {
                    mergeTreeLevel(srcTree,
                                   nextDstLevelIndex,
                                   nextSrcLevelIndex);
                } else {
                    nextDstLevelIndex = copyTreeLevel(srcTree,
                                                      nextSrcLevelIndex);
                    nextChildren[dstIndex] = nextDstLevelIndex;
                }
            }

            srcIndex = srcTree.nextSiblings[srcIndex];
        }
    }

    private int copyTreeLevel(final EventDispatchTreeImpl srcTree,
                              final int srcLevelIndex) {
        if (srcLevelIndex == NULL_INDEX) {
            return NULL_INDEX;
        }

        int srcIndex = srcLevelIndex;
        final int dstLevelIndex = copySubtree(srcTree, srcIndex);
        int lastDstIndex = dstLevelIndex;

        srcIndex = srcTree.nextSiblings[srcIndex];
        while (srcIndex != NULL_INDEX) {
            int dstIndex = copySubtree(srcTree, srcIndex);
            nextSiblings[lastDstIndex] = dstIndex;

            lastDstIndex = dstIndex;
            srcIndex = srcTree.nextSiblings[srcIndex];
        }

        nextSiblings[lastDstIndex] = NULL_INDEX;
        return dstLevelIndex;
    }

    private int copySubtree(final EventDispatchTreeImpl srcTree,
                            final int srcIndex) {
        ensureCapacity(reservedCount + 1);
        final int dstIndex = reservedCount++;

        final int dstChildLevelIndex =
                copyTreeLevel(srcTree, getChildIndex(srcTree, srcIndex));
        dispatchers[dstIndex] = srcTree.dispatchers[srcIndex];
        nextChildren[dstIndex] = dstChildLevelIndex;

        return dstIndex;
    }

    private void appendTreeLevel(final StringBuilder sb,
                                 final int levelIndex) {
        sb.append('(');

        int index = levelIndex;
        appendSubtree(sb, index);

        index = nextSiblings[index];
        while (index != NULL_INDEX) {
            sb.append(",");
            appendSubtree(sb, index);
            index = nextSiblings[index];
        }

        sb.append(')');
    }

    private void appendSubtree(final StringBuilder sb,
                               final int index) {
        sb.append(dispatchers[index]);

        final int childIndex = getChildIndex(this, index);
        if (childIndex != NULL_INDEX) {
            sb.append("->");
            appendTreeLevel(sb, childIndex);
        }
    }

    private static int getChildIndex(final EventDispatchTreeImpl tree,
                                     final int index) {
        int childIndex = tree.nextChildren[index];
        if ((childIndex == NULL_INDEX)
                && (index != tree.tailLastIndex)) {
            childIndex = tree.tailFirstIndex;
        }

        return childIndex;
    }

//    void dumpInternalData() {
//        LogFileUtil.getInstance().writeLog("reservedCount: " + reservedCount);
//        LogFileUtil.getInstance().writeLog("rootIndex: " + rootIndex);
//        LogFileUtil.getInstance().writeLog("tailFirstIndex: " + tailFirstIndex);
//        LogFileUtil.getInstance().writeLog("tailLastIndex: " + tailLastIndex);
//
//        System.out.print("dispatchers:");
//        for (int i = 0; i < reservedCount; ++i) {
//            System.out.print(" " + dispatchers[i]);
//        }
//        LogFileUtil.getInstance().writeLog();
//
//        System.out.print("nextSiblings:");
//        for (int i = 0; i < reservedCount; ++i) {
//            System.out.print(" " + nextSiblings[i]);
//        }
//        LogFileUtil.getInstance().writeLog();
//
//        System.out.print("nextChildren:");
//        for (int i = 0; i < reservedCount; ++i) {
//            System.out.print(" " + nextChildren[i]);
//        }
//        LogFileUtil.getInstance().writeLog();
//    }
}
