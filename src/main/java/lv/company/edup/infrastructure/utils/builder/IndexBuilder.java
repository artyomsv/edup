package lv.company.edup.infrastructure.utils.builder;

import lv.company.edup.infrastructure.utils.AppCollectionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexBuilder<Key, Value> {

    private Transformer<Value, Key> key;
    private Predicate<Value> predicate;
    private Factory<Collection<Value>> factory;
    private Factory<Map<Key, Value>> index;
    private Factory<Map<Key, Collection<Value>>> collectionIndex;
    private Comparator<Value> comparator;

    private IndexBuilder() {
    }

    public static <Key, Value> IndexBuilder<Key, Value> get() {
        return new IndexBuilder<Key, Value>();
    }

    public Map<Key, Collection<Value>> mapToCollection(Collection<Value> values) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }

        AppCollectionUtils.removeNulls(values);

        checkPredicate();
        checkFactory();
        checkCollectionIndex();

        Map<Key, Collection<Value>> map = map(values, key, factory, predicate, collectionIndex);
        if (comparator != null) {
            for (Collection<Value> collection : map.values()) {
                List<Class<?>> interfaces = ClassUtils.getAllInterfaces(collection.getClass());
                if (interfaces.contains(List.class)) {
                    Collections.sort((List<Value>) collection, comparator);
                }
            }
        }

        return map;
    }

    public Map<Key, Value> map(Collection<Value> values) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }

        AppCollectionUtils.removeNulls(values);

        checkPredicate();
        checkFactory();
        checkIndex();

        return map(values, key, predicate, index);
    }

    public IndexBuilder<Key, Value> key(Transformer<Value, Key> key) {
        this.key = key;
        return this;
    }

    public IndexBuilder<Key, Value> factory(Factory<Collection<Value>> factory) {
        this.factory = factory;
        return this;
    }

    public IndexBuilder<Key, Value> predicate(Predicate<Value> predicate) {
        this.predicate = predicate;
        return this;
    }

    public IndexBuilder<Key, Value> comporator(Comparator<Value> comparator) {
        this.comparator = comparator;
        return this;
    }

    public IndexBuilder<Key, Value> index(Factory<Map<Key, Value>> index) {
        this.index = index;
        return this;
    }

    public IndexBuilder<Key, Value> indexForCollections(Factory<Map<Key, Collection<Value>>> index) {
        this.collectionIndex = index;
        return this;
    }

    private Map<Key, Collection<Value>> map(Collection<Value> coll,
                                            Transformer<Value, Key> extractor,
                                            Factory<Collection<Value>> factory,
                                            Predicate<Value> predicate,
                                            Factory<Map<Key, Collection<Value>>> index) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyMap();
        }

        Map<Key, Collection<Value>> map = index.create();
        for (Value value : coll) {
            if (predicate.evaluate(value)) {
                Key key = extractor.transform(value);

                if (key != null) {
                    if (map.containsKey(key)) {
                        map.get(key).add(value);
                    } else {
                        Collection<Value> collection = factory.create();
                        collection.add(value);
                        map.put(key, collection);
                    }
                }
            }
        }

        return map;
    }

    private Map<Key, Value> map(Collection<Value> values,
                                Transformer<Value, Key> extractor,
                                Predicate<Value> predicate,
                                Factory<Map<Key, Value>> index) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }

        Map<Key, Value> map = index.create();
        for (Value value : values) {
            if (predicate.evaluate(value)) {
                Key key = extractor.transform(value);
                if (key != null) {
                    map.put(key, value);
                }
            }
        }

        return map;
    }

    private void checkFactory() {
        if (factory == null) {
            factory = new Factory<Collection<Value>>() {
                @Override
                public Collection<Value> create() {
                    return new ArrayList<Value>();
                }
            };
        }
    }

    private void checkPredicate() {
        if (predicate == null) {
            predicate = new Predicate<Value>() {
                @Override
                public boolean evaluate(Value object) {
                    return true;
                }
            };
        }
    }

    private void checkCollectionIndex() {
        if (collectionIndex == null) {
            collectionIndex = new Factory<Map<Key, Collection<Value>>>() {
                @Override
                public Map<Key, Collection<Value>> create() {
                    return new HashMap<>();
                }
            };
        }
    }

    private void checkIndex() {
        if (index == null) {
            index = new Factory<Map<Key, Value>>() {
                @Override
                public Map<Key, Value> create() {
                    return new HashMap<>();
                }
            };
        }
    }
}
