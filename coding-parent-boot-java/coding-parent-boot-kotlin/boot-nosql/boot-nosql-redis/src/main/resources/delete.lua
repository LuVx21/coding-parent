-- 高效删除符合特定模式的 Key（如 cache:user:*），避免 KEYS 阻塞 Redis。

-- KEYS[1] = 匹配模式（如 "cache:user:*"）
local keys = redis.call('SCAN', 0, 'MATCH', KEYS[1], 'COUNT', 1000)[2]
if #keys > 0 then
    redis.call('DEL', unpack(keys))
end
return #keys