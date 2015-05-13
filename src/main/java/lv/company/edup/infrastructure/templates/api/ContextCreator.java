package lv.company.edup.infrastructure.templates.api;

import java.util.Map;

public interface ContextCreator<T> {

    Map<String, Object> create(T from);
}
