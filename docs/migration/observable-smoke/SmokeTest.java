import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.WeakInvalidationListener;
import com.tungsten.fclcore.observable.binding.Bindings;
import com.tungsten.fclcore.observable.binding.StringBinding;
import com.tungsten.fclcore.observable.collections.*;
import com.tungsten.fclcore.observable.property.*;
import com.tungsten.fclcore.observable.value.ChangeListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SmokeTest {

    static int passed = 0, failed = 0;

    static void check(String name, boolean cond) {
        if (cond) { passed++; System.out.println("PASS " + name); }
        else { failed++; System.out.println("FAIL " + name); }
    }

    public static void main(String[] args) throws Exception {
        testObjectProperty();
        testStringPropertyEquals();
        testBooleanPropertyBaseHook();
        testChangeListenerOnlyOnRealChange();
        testBind();
        testBoundSetThrows();
        testBindBidirectional();
        testStringBindingLazy();
        testConcat();
        testListChanges();
        testSetAllComposite();
        testExtractorBubbling();
        testReadOnlyListWrapper();
        testWeakListeners();
        testListProperty();
        testBindContent();
        testMapSet();
        testMultiListenerReset();

        System.out.println("\n== " + passed + " passed, " + failed + " failed ==");
        System.exit(failed == 0 ? 0 : 1);
    }

    static void testObjectProperty() {
        AtomicInteger inv = new AtomicInteger();
        SimpleObjectProperty<String> p = new SimpleObjectProperty<>("a");
        p.addListener((InvalidationListener) o -> inv.incrementAndGet());
        String sameRef = p.get();
        p.set(sameRef); // 同引用不触发
        check("object same-ref no fire", inv.get() == 0);
        p.set("b");
        check("object new-ref fires once", inv.get() == 1);
        p.get(); // 重新生效（fakefx：get 重置 valid，连续 set 只在 valid→invalid 边沿触发一次）
        p.set(new String("b")); // 不同引用但 equals 相同 → fakefx ObjectPropertyBase 用 != 仍触发
        check("object equal-but-new-ref fires", inv.get() == 2);
    }

    static void testStringPropertyEquals() {
        AtomicInteger inv = new AtomicInteger();
        SimpleStringProperty p = new SimpleStringProperty("x");
        p.addListener((InvalidationListener) o -> inv.incrementAndGet());
        p.set(new String("x")); // equals → 不触发
        check("string equals no fire", inv.get() == 0);
        p.set("y");
        check("string different fires", inv.get() == 1);
        p.get(); // 重新生效
        p.set(null);
        check("string to null fires", inv.get() == 2);
        p.get();
        p.set(null);
        check("string null to null no fire", inv.get() == 2);
    }

    static void testBooleanPropertyBaseHook() {
        AtomicInteger hookCalls = new AtomicInteger();
        BooleanProperty p = new BooleanPropertyBase() {
            @Override protected void invalidated() { super.invalidated(); hookCalls.incrementAndGet(); }
            @Override public Object getBean() { return this; }
            @Override public String getName() { return "test"; }
        };
        p.set(true);
        check("boolean hook on change", hookCalls.get() == 1);
        p.set(true);
        check("boolean no hook on same", hookCalls.get() == 1);
        check("boolean get", p.get());
        p.setValue(Boolean.FALSE);
        check("boolean setValue", !p.get() && hookCalls.get() == 2);
    }

    static void testChangeListenerOnlyOnRealChange() {
        AtomicReference<String> order = new AtomicReference<>("");
        SimpleObjectProperty<List<String>> p = new SimpleObjectProperty<>(new ArrayList<>(Arrays.asList("a")));
        p.addListener((InvalidationListener) o -> order.set(order.get() + "I"));
        AtomicInteger changeFires = new AtomicInteger();
        p.addListener((ChangeListener<List<String>>) (obs, oldV, newV) -> { order.set(order.get() + "C"); changeFires.incrementAndGet(); });
        // equals 相同但不同引用：invalidation 触发，change 不触发
        p.set(new ArrayList<>(Arrays.asList("a")));
        check("change listener skipped on equals", changeFires.get() == 0);
        check("invalidation before change", order.get().equals("I"));
        p.set(new ArrayList<>(Arrays.asList("b")));
        check("change listener fires on real change", changeFires.get() == 1 && order.get().equals("IIC"));
    }

    static void testBind() {
        SimpleIntegerProperty source = new SimpleIntegerProperty(1);
        SimpleIntegerProperty target = new SimpleIntegerProperty(0);
        AtomicInteger inv = new AtomicInteger();
        target.addListener((InvalidationListener) o -> inv.incrementAndGet());
        target.bind(source);
        check("bind fires once", inv.get() == 1);
        check("bind syncs value", target.get() == 1);
        source.set(42);
        check("bind propagates", target.get() == 42 && inv.get() == 2);
        target.unbind();
        check("unbind freezes value", target.get() == 42);
        source.set(7);
        check("unbind stops propagation", target.get() == 42 && inv.get() == 2);
        check("isBound false after unbind", !target.isBound());
    }

    static void testBoundSetThrows() {
        SimpleBooleanProperty a = new SimpleBooleanProperty(true);
        SimpleBooleanProperty b = new SimpleBooleanProperty(false);
        b.bind(a);
        boolean thrown = false;
        try { b.set(true); } catch (RuntimeException e) { thrown = e.getMessage().contains("A bound value cannot be set."); }
        check("bound set throws", thrown);
        b.unbind();
    }

    static void testBindBidirectional() {
        SimpleDoubleProperty a = new SimpleDoubleProperty(1.5);
        SimpleDoubleProperty b = new SimpleDoubleProperty(0);
        a.bindBidirectional(b);
        check("bidir initial sync a<-b", a.get() == 0.0);
        a.set(2.5);
        check("bidir a->b", b.get() == 2.5);
        b.set(3.5);
        check("bidir b->a", a.get() == 3.5);
        a.unbindBidirectional(b);
        a.set(9.0);
        check("unbindBidirectional stops sync", b.get() == 3.5);

        // 防回环：字符串双向绑定不会死循环
        SimpleStringProperty s1 = new SimpleStringProperty("x");
        SimpleStringProperty s2 = new SimpleStringProperty("y");
        s1.bindBidirectional(s2);
        s1.set("z");
        check("bidir string no loop", s2.get().equals("z"));
        s1.unbindBidirectional(s2);
    }

    static void testStringBindingLazy() {
        AtomicInteger computes = new AtomicInteger();
        SimpleStringProperty dep = new SimpleStringProperty("a");
        StringBinding binding = Bindings.createStringBinding(() -> { computes.incrementAndGet(); return dep.get() + "!"; }, dep);
        check("binding lazy: not computed", computes.get() == 0);
        check("binding compute on get", binding.get().equals("a!") && computes.get() == 1);
        binding.get();
        check("binding cached", computes.get() == 1);
        dep.set("b");
        check("binding invalidated by dep", !binding.isValid());
        check("binding recompute", binding.get().equals("b!") && computes.get() == 2);

        // 绑定到属性：dep 变化经 binding 传播到 target
        SimpleStringProperty target = new SimpleStringProperty();
        target.bind(binding);
        check("property bound to binding", target.get().equals("b!"));
        dep.set("c");
        check("dep -> binding -> property", target.get().equals("c!"));
        target.unbind();
    }

    static void testConcat() {
        SimpleStringProperty name = new SimpleStringProperty("Steve");
        StringBinding concat = Bindings.concat(name, " - ", "char");
        check("concat value", concat.get().equals("Steve - char"));
        name.set("Alex");
        check("concat follows dep", concat.get().equals("Alex - char"));
        StringBinding constant = Bindings.concat("a", 1, (Object) null);
        check("concat constant (null -> \"null\")", constant.get().equals("a1null"));
    }

    static void testListChanges() {
        ObservableList<String> list = FXCollections.observableArrayList("a", "b");
        AtomicInteger inv = new AtomicInteger();
        List<String> events = new ArrayList<>();
        list.addListener((InvalidationListener) o -> inv.incrementAndGet());
        list.addListener((ListChangeListener<String>) c -> {
            while (c.next()) {
                if (c.wasReplaced()) events.add("replaced:" + c.getFrom() + "-" + c.getTo() + ":" + c.getRemoved());
                else if (c.wasUpdated()) events.add("update:" + c.getFrom() + "-" + c.getTo());
                else if (c.wasAdded()) events.add("add:" + c.getFrom() + "-" + c.getTo());
                else if (c.wasRemoved()) events.add("remove:" + c.getFrom() + ":" + c.getRemoved());
            }
        });
        list.add("c");
        check("list add event", events.equals(Collections.singletonList("add:2-3")) && inv.get() == 1);
        events.clear();
        list.remove(0);
        check("list remove event", events.equals(Collections.singletonList("remove:0:[a]")));
        events.clear();
        list.set(0, "x");
        check("list set event wasReplaced", events.equals(Collections.singletonList("replaced:0-1:[b]")));
        events.clear();
        list.clear();
        check("list clear event", events.equals(Collections.singletonList("remove:0:[x, c]")));
        check("list content", list.isEmpty());
    }

    static void testSetAllComposite() {
        ObservableList<String> list = FXCollections.observableArrayList("a", "b");
        AtomicInteger fires = new AtomicInteger();
        List<Boolean> replacedFlags = new ArrayList<>();
        list.addListener((ListChangeListener<String>) c -> {
            fires.incrementAndGet();
            while (c.next()) { replacedFlags.add(c.wasReplaced() || c.wasRemoved() || c.wasAdded()); }
        });
        list.setAll("x", "y", "z");
        check("setAll single event", fires.get() == 1);
        check("setAll composite content", list.equals(Arrays.asList("x", "y", "z")));
    }

    static class Item implements Observable {
        private List<InvalidationListener> listeners = new ArrayList<>();
        final SimpleStringProperty name = new SimpleStringProperty();
        Item() { name.addListener((InvalidationListener) o -> fire()); }
        void fire() { for (InvalidationListener l : new ArrayList<>(listeners)) l.invalidated(this); }
        @Override public void addListener(InvalidationListener listener) { listeners.add(listener); }
        @Override public void removeListener(InvalidationListener listener) { listeners.remove(listener); }
    }

    static void testExtractorBubbling() {
        // Accounts/Controllers 的存盘链路：元素属性变化 → 列表 update 事件
        Item item = new Item();
        ObservableList<Item> list = FXCollections.observableArrayList(i -> new Observable[]{i});
        list.add(item);
        AtomicInteger updates = new AtomicInteger();
        AtomicInteger updateIndex = new AtomicInteger(-1);
        list.addListener((ListChangeListener<Item>) c -> {
            while (c.next()) {
                if (c.wasUpdated()) { updates.incrementAndGet(); updateIndex.set(c.getFrom()); }
            }
        });
        item.name.set("new");
        check("extractor bubbles update", updates.get() == 1 && updateIndex.get() == 0);
        // 元素移出后不再冒泡
        list.remove(0);
        item.name.set("again");
        check("extractor detached after remove", updates.get() == 1);
    }

    static void testReadOnlyListWrapper() {
        ObservableList<String> backing = FXCollections.observableArrayList("a");
        ReadOnlyListWrapper<String> wrapper = new ReadOnlyListWrapper<>(backing);
        ReadOnlyListProperty<String> ro = wrapper.getReadOnlyProperty();
        AtomicInteger inv = new AtomicInteger();
        AtomicInteger listChange = new AtomicInteger();
        ro.addListener((InvalidationListener) o -> inv.incrementAndGet());
        ro.addListener((ListChangeListener<String>) c -> listChange.incrementAndGet());
        backing.add("b");
        check("ro wrapper content event", inv.get() == 1 && listChange.get() == 1);
        check("ro wrapper get", ro.get().equals(Arrays.asList("a", "b")) && ro.size() == 2);
        // 只读视图收到的 Change.getList() 指向属性本身（SourceAdapterChange）
        AtomicReference<Object> changeList = new AtomicReference<>();
        ReadOnlyListProperty<String> ro2 = wrapper.getReadOnlyProperty();
        ro2.addListener((ListChangeListener<String>) c -> changeList.set(c.getList()));
        backing.add("c");
        check("ro wrapper change.getList() is property", changeList.get() == ro2);
    }

    static void testWeakListeners() throws Exception {
        SimpleBooleanProperty p = new SimpleBooleanProperty(false);
        AtomicInteger calls = new AtomicInteger();
        InvalidationListener strong = o -> calls.incrementAndGet();
        WeakReference<InvalidationListener> strongRef = new WeakReference<>(strong);
        p.addListener(new WeakInvalidationListener(strong));
        p.set(true);
        check("weak listener fires while alive", calls.get() == 1);
        strong = null;
        for (int i = 0; i < 20 && strongRef.get() != null; i++) { System.gc(); Thread.sleep(50); }
        check("weak target collected", strongRef.get() == null);
        p.set(false);
        check("weak listener no fire after gc", calls.get() == 1);
    }

    static void testListProperty() {
        SimpleListProperty<String> lp = new SimpleListProperty<>(FXCollections.observableArrayList("a"));
        AtomicInteger inv = new AtomicInteger();
        AtomicInteger listChange = new AtomicInteger();
        AtomicInteger replaced = new AtomicInteger();
        lp.addListener((InvalidationListener) o -> inv.incrementAndGet());
        lp.addListener((ListChangeListener<String>) c -> {
            listChange.incrementAndGet();
            while (c.next()) { if (c.wasReplaced()) replaced.incrementAndGet(); }
        });
        lp.add("b"); // 内容变更冒泡
        check("listProperty content bubbles", inv.get() == 1 && listChange.get() == 1);
        lp.set(FXCollections.observableArrayList("x", "y", "z")); // 整体替换 → wasReplaced
        check("listProperty replace fires wasReplaced", replaced.get() == 1 && inv.get() == 2);
        check("listProperty delegation", lp.size() == 3 && lp.get(0).equals("x"));
        check("sizeProperty", lp.sizeProperty().get() == 3 && !lp.emptyProperty().get());
    }

    static void testBindContent() {
        ObservableList<String> source = FXCollections.observableArrayList("a", "b");
        SimpleListProperty<String> target = new SimpleListProperty<>(FXCollections.observableArrayList());
        Bindings.bindContent(target, source);
        check("bindContent initial", target.equals(Arrays.asList("a", "b")));
        source.add("c");
        check("bindContent follows add", target.equals(Arrays.asList("a", "b", "c")));
        source.remove(0);
        check("bindContent follows remove", target.equals(Arrays.asList("b", "c")));
        source.set(0, "z");
        check("bindContent follows set", target.equals(Arrays.asList("z", "c")));
        Bindings.unbindContent(target, source);
        source.add("d");
        check("unbindContent stops", target.equals(Arrays.asList("z", "c")));
    }

    static void testMapSet() {
        ObservableMap<String, Integer> map = FXCollections.observableMap(new TreeMap<>());
        AtomicInteger inv = new AtomicInteger();
        List<String> events = new ArrayList<>();
        map.addListener((InvalidationListener) o -> inv.incrementAndGet());
        map.addListener((MapChangeListener<String, Integer>) c ->
                events.add((c.wasAdded() ? "+" : "") + (c.wasRemoved() ? "-" : "") + c.getKey()));
        map.put("a", 1);
        check("map put added", events.equals(Collections.singletonList("+a")) && inv.get() == 1);
        map.put("a", 2);
        check("map put replaced", events.get(1).equals("+-a"));
        map.remove("a");
        check("map remove", events.get(2).equals("-a"));

        ObservableSet<String> set = FXCollections.observableSet(new HashSet<>());
        AtomicInteger setInv = new AtomicInteger();
        set.addListener((InvalidationListener) o -> setInv.incrementAndGet());
        set.add("x");
        set.add("x"); // 重复 add 不触发
        check("set add/dup", setInv.get() == 1);
        set.remove("x");
        check("set remove", setInv.get() == 2);

        SimpleMapProperty<String, Integer> mp = new SimpleMapProperty<>(FXCollections.observableMap(new TreeMap<>()));
        AtomicInteger mpInv = new AtomicInteger();
        mp.addListener((InvalidationListener) o -> mpInv.incrementAndGet());
        mp.put("k", 1); // 内容变更冒泡
        check("mapProperty content bubbles", mpInv.get() == 1);
        mp.set(FXCollections.observableMap(new TreeMap<>()));
        check("mapProperty replace", mpInv.get() == 2);
    }

    static void testMultiListenerReset() {
        // 一个 Change 实例分发给多个 listener 时，每个之前 reset
        ObservableList<String> list = FXCollections.observableArrayList();
        AtomicInteger l1 = new AtomicInteger();
        AtomicInteger l2 = new AtomicInteger();
        ListChangeListener<String> counter1 = c -> { while (c.next()) l1.incrementAndGet(); };
        ListChangeListener<String> counter2 = c -> { while (c.next()) l2.incrementAndGet(); };
        list.addListener(counter1);
        list.addListener(counter2);
        list.setAll("a", "b", "c");
        check("multi listener both iterate", l1.get() > 0 && l1.get() == l2.get());
    }
}
