package com.wilburomae.pezeshalms.common.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class CollectionUtilities {
    private CollectionUtilities() {
    }

    public static <T, U> Map<T, U> listToMap(List<U> list, Function<U, T> keyExtractor) {
        Map<T, U> map = new HashMap<>();
        for (U item : list) {
            map.put(keyExtractor.apply(item), item);
        }
        return map;
    }
}
