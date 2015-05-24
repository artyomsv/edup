package lv.company.edup.infrastructure.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AppCollectionUtils {

    public interface KeyTransformer<Value, Key> {
        Key transform(Value value);
    }

    public static <Key, Value> Map<Key, Value> map(Collection<Value> collection, KeyTransformer<Value, Key> keyTransformer) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }

        Map<Key, Value> map = new HashMap<Key, Value>();
        for (Value value : collection) {
            if (value != null) {
                Key key = keyTransformer.transform(value);
                if (key != null) {
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    public static <Key, Value> Map<Key, Collection<Value>> mapToCollection(Collection<Value> collection, KeyTransformer<Value, Key> keyTransformer) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }

        Map<Key, Collection<Value>> map = new HashMap<Key, Collection<Value>>();
        for (Value value : collection) {
            if (value != null) {
                Key key = keyTransformer.transform(value);
                if (key != null) {
                    if (!map.containsKey(key)) {
                        map.put(key, new ArrayList<Value>());
                    }
                    map.get(key).add(value);
                }
            }
        }
        return map;
    }


}
