-- 原子递减: 从max开始, 每次递减down(如1), 直到min
-- 限频: 如每天仅能5次, redis.lua foo 1 0 5 86400
local key = KEYS[1];

local down = tonumber(ARGV[1]); -- 扣减数量
local min = tonumber(ARGV[2]) or math.mininteger; -- 递减下界
local max = tonumber(ARGV[3]); -- 递减上界
local exp = tonumber(ARGV[4]); -- 秒

local num = tonumber(redis.call('GET', key))
if num == nil then
    -- 如果没有设置过, 设置为max
    if max == nil then
        return 0
    end
    if exp == nil or exp <= 0 then
        redis.call('SET', key, max)
    else
        -- 设置初始值+过期时间
        redis.call('SET', key, max, 'EX', exp)
    end
    num = max
end

-- 如果当前值小于等于最小值, 直接返回0(不再递减)
if num <= min then
    return 0
end
if (num - down) < min then
    return 0
end
redis.call('DECRBY', key, down)
return 1
