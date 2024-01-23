package org.luvx.coding.common.consts;

import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.Nullable;

public class Systems {
    public static String appRunInDomain() {
        return STR."\{appRunInHost()}:\{appRunInPort()}";
    }

    /**
     * 主机名或IP
     */
    @Nullable
    public static String appRunInHost() {
        return System.getenv("APP_RUN_IN_HOST");
    }

    public static int appRunInPort() {
        String env = System.getenv("APP_RUN_IN_POST");
        return NumberUtils.toInt(env);
    }
}
