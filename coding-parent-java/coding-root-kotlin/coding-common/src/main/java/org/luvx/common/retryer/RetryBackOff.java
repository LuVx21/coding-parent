package org.luvx.common.retryer;

/**
 * 一个简单的BackOff接口，用来给出每次重试前需要等待的重试间隔。时间间隔指的是上次执行结束到下一次执行开始中间的时间.
 * RetryBackOff的实现有可能是有状态的
 */
interface RetryBackOff {
    /**
     * 返回第N次重试前，需要等待的时间. 同时还传入了前两次重试的间隔，以方便低成本无状态的实现斐波那契退避这样的策略..
     * 时间都是已毫秒为单位，因为Duration现在还是会创建新对像的
     *
     * @param retryTimes 重试的次数, 从1开始
     * @param lastDelayMills 上一次重试的间隔；如果没有上一次重试则是0
     * @param lastSecondDelayMillis 倒数第二次重试的间隔；如果没有倒数第二次重试则是0.
     */
    long nextIntervalOf(int retryTimes, long lastDelayMills, long lastSecondDelayMillis);
}
