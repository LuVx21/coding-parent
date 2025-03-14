-- KEYS[1] = 锁 Key（如 "lock:order123"）
-- ARGV[1] = 锁的 Value（唯一标识，如 UUID）
-- ARGV[2] = 锁的过期时间（秒，如 30）

local key = KEYS[1];

local value = ARGV[1];
local exp = ARGV[2];

if redis.call('SETNX', key, value) == 1 then
    redis.call('EXPIRE', key, exp)
    return 1
end

-- 锁已存在，检查是否自己的锁（防止误删别人的锁）
if redis.call('GET', key) == value then
    redis.call('EXPIRE', key, exp)
    return 1
else
    -- 锁被其他人持有
    return 0
end