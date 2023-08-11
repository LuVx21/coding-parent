package org.luvx.coding.common;

import lombok.Getter;
import lombok.Setter;

public class OnOffSwitch {
    /**
     * 开关状态
     */
    @Getter
    private volatile boolean  on;
    /**
     * 开关的行为
     */
    @Setter
    private          Runnable action;

    private OnOffSwitch(boolean on) {
        this.on = on;
    }

    public OnOffSwitch switchStatus() {
        on = !on;
        return this;
    }

    public void onThen() {
        if (!on || action == null) {
            return;
        }
        action.run();
    }

    public void onThen(Runnable r) {
        if (!on) {
            return;
        }
        r.run();
    }

    public static OnOffSwitch of(boolean on) {
        return new OnOffSwitch(on);
    }
}
