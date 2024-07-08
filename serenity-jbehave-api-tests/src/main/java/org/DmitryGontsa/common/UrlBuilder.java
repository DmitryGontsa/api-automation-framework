package org.DmitryGontsa.common;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class UrlBuilder {

    private UrlBuilder() {
    }

    public static <T> String buildFullUrl(final String commonUrl, final Class<T> type) {
        return Optional.ofNullable(type)
                .map(annotation -> type.getAnnotation(PartialUrl.class))
                .map(PartialUrl::value)
                .map(partialUrl -> String.format("%s/%s", commonUrl, partialUrl))
                .orElseThrow(() -> new IllegalStateException("Missing '@PartialUrl' in class!"));
    }

    public static <T> String buildWholeUrl(final String commonUrl, final Class<T> type) {

        if(type == null) {
            throw new IllegalStateException("Missing '@PartialUrl' in class!");
        }

        final PartialUrl partialUrlValue = type.getAnnotation(PartialUrl.class);
        final String value = partialUrlValue.value();

        if(StringUtils.isNotEmpty(value)) {
            return String.format("%s/%s", commonUrl, value);
        }

        throw new IllegalStateException("Missing 'Common URL part!'");
    }
}
