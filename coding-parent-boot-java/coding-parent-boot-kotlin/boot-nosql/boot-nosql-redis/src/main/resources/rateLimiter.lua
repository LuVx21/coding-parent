local key = KEYS[1]
local limit = tonumber(ARGV[1])
local currentCount = tonumber(redis.call('get', key) or "0")

if currentCount + 1 > limit
then return 0
else
    redis.call('INCRBY', key, 1)
    redis.call('EXPIRE', key, ARGV[2])
    return currentCount + 1
end
