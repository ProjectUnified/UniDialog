package io.github.projectunified.unidialog.paper.payload;

import io.papermc.paper.dialog.DialogResponseView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

final class PaperDialogPayloadMap {
    private static final Field compoundField;
    private static final Method keySetMethod;

    static {
        try {
            Class<?> responseViewClass = Class.forName("io.papermc.paper.dialog.PaperDialogResponseView");
            compoundField = responseViewClass.getDeclaredField("payload");
            compoundField.setAccessible(true);
            keySetMethod = compoundField.getType().getDeclaredMethod("keySet");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize PaperUtil", e);
        }
    }

    @SuppressWarnings("unchecked")
    static Map<String, String> convertDialogResponseToMap(DialogResponseView response) {
        try {
            Object compound = compoundField.get(response);
            Set<String> keys = (Set<String>) keySetMethod.invoke(compound);

            Map<String, String> map = new HashMap<>();
            for (String key : keys) {
                String text = response.getText(key);
                if (text != null) {
                    map.put(key, text);
                    continue;
                }

                Float number = response.getFloat(key);
                if (number != null) {
                    map.put(key, number.toString());
                    continue;
                }

                Boolean bool = response.getBoolean(key);
                if (bool != null) {
                    map.put(key, bool.toString());
                }
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
