package io.github.projectunified.unidialog.paper;

import io.papermc.paper.dialog.DialogResponseView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface PaperUtil {
    @SuppressWarnings("unchecked")
    static Map<String, String> convertDialogResponseToMap(DialogResponseView response) {
        try {
            Field compoundField = response.getClass().getDeclaredField("payload");
            compoundField.setAccessible(true);

            Object compound = compoundField.get(response);
            Method keySetMethod = compound.getClass().getDeclaredMethod("keySet");
            keySetMethod.setAccessible(true);

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
