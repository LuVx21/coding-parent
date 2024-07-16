local key = KEYS[1];
local expectValue = ARGV[1];
local pttl = tonumber(ARGV[2]);
local newValue = ARGV[3];
local onlyRefreshTTL = ('true' == ARGV[4]);
local setnx = ('true' == ARGV[5]);

if setnx then // setnx 直接短路掉
    return redis.call('SET', key, newValue, 'PX', pttl, 'NX');
end;

if pttl < 0 then // 如果需要读 ttl 的话, 先读老 key 的 ttl
    pttl = redis.call('PTTL', key);
end;

// 再读老 key 的值; 否则有可能出现先读到老值, 读 ttl 时 key 已过期, 造成新写入的值不会过期
local currentValue = redis.call('GET', key);
if onlyRefreshTTL then
    newValue = currentValue;
end;

if currentValue == expectValue then
    if pttl < 0 then
        redis.call('SET', key, newValue);
    elseif pttl == 0 then
        redis.call('DEL', key);
    else
        redis.call('SET', key, newValue, 'PX', pttl);
    end
    return true;
else
    return false;
end;