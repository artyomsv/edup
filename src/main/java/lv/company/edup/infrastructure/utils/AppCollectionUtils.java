package lv.company.edup.infrastructure.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AppCollectionUtils {

    public static Collection<Long> toLongs(Collection<String> strings) {
        Collection<Long> collect = CollectionUtils.collect(strings, new Transformer<String, Long>() {
            @Override
            public Long transform(String input) {
                return StringUtils.isNumeric(input) ? Long.valueOf(input) : null;
            }
        });
        collect.remove(null);

        return collect;
    }

    public static Collection<String> toStrings(Collection<Long> longs) {
        return CollectionUtils.collect(longs, new Transformer<Long, String>() {
            @Override
            public String transform(Long input) {
                return String.valueOf(input);
            }
        });
    }


    public static <T> List<List<T>> splitListToChunks(Collection<T> source, int chunkSize) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<List<T>> chunkList = new ArrayList<List<T>>();
        List<T> list = new ArrayList<T>(source);
        int i = source.size();
        while (i > 0) {
            List<T> chunk = list.subList(Math.max(i - chunkSize, 0), i);
            i -= chunkSize;
            chunkList.add(chunk);
        }

        return chunkList;
    }

    public static <T> T getFirstResult(Collection<T> collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            return collection.iterator().next();
        }
        return null;
    }

    public static <T> T getSingleResult(Collection<T> collection) {
        if (CollectionUtils.isNotEmpty(collection) && collection.size() == 1) {
            return collection.iterator().next();
        }
        return null;
    }

    public static <T> boolean contains(Collection<T> c, T o) {
        if (CollectionUtils.isEmpty(c)) {
            return false;
        }

        for (T t : c) {
            if (t != null) {
                if (t.equals(o)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static <E> void removeNulls(Collection<E> c) {
        if (CollectionUtils.isNotEmpty(c)) {
            for (Iterator<E> iterator = c.iterator(); iterator.hasNext(); ) {
                E next = iterator.next();
                if (next == null) {
                    iterator.remove();
                }
            }
        }
    }

}
