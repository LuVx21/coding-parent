package org.luvx.coding.common.consts;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.luvx.coding.common.net.NetUtils;
import org.luvx.coding.common.util.StringUtilsV2;

import jakarta.annotation.Nullable;

@Slf4j
public class Systems {
    public static String appRunInDomain() {
        return STR."\{appRunInHost()}:\{appRunInPort()}";
    }

    /**
     * 主机名或IP
     */
    @Nullable
    public static String appRunInHost() {
        String v = getEnv("APP_RUN_IN_HOST");
        // v = StringUtilsV2.getIfEmpty(v, () -> NetUtils.getHostInfo().get("ip"));
        return v;
    }

    @Nullable
    public static Integer appRunInPort() {
        String v = getEnv("APP_RUN_IN_PORT");
        if (StringUtils.isBlank(v)) {
            return null;
        }
        return NumberUtils.toInt(v);
    }

    private static String getEnv(String p) {
        String env = System.getenv(p);
        if (env == null) {
            log.info("缺少环境变量:{}", p);
        }
        return env;
    }
}
