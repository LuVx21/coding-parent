local key = KEYS[1]

local now = tonumber(ARGV[1]) -- 毫秒值
local rate = tonumber(ARGV[2])
local capacity = tonumber(ARGV[3])
local requested = tonumber(ARGV[4])

local jan_1_2025 = 1735689600
if now <= 0 then
    now = redis.call("TIME")
    now = ((now[1] - jan_1_2025) * 1000) + (now[2] / 1000)
end

local bucket_state = redis.call("HMGET", key, "lastTime", "water")
local lastTime = tonumber(bucket_state[1]) or 0
local water = tonumber(bucket_state[2]) or 0

-- 计算漏出的水量
local elapsed = now - lastTime
local leaked = math.floor(elapsed * rate / 1000)
-- 漏桶内剩余的水量, 最小为0
water = math.max(water - leaked, 0)

-- 检查是否有足够的容量(有没有超过最大请求量)
local new_water = water + requested
if new_water <= capacity then
    redis.call("HMSET", key, "lastTime", now, "water", new_water)
    redis.call("EXPIRE", key, math.ceil(capacity / rate))
    return {1, water, 0}
else
    local retry_after = (new_water - capacity) / rate
    return {0, water, retry_after}
end