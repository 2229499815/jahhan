package net.jahhan.cache.context;

import java.util.HashMap;
import java.util.Map;

import net.jahhan.context.BaseContext;
import net.jahhan.context.Variable;
import net.jahhan.lock.DistributedLock;
import net.jahhan.lock.impl.GlobalReentrantLock;

/**
 * 数据库线程局部变量
 * 
 */
public class RedisVariable extends Variable {
	// 分布式服务锁
	private Map<String, DistributedLock> serviceLocksMap = new HashMap<>();

	public boolean setServiceLock(String lockType, DistributedLock lock) {
		if (serviceLocksMap.containsKey(lockType)) {
			return false;
		}
		serviceLocksMap.put(lockType, lock);
		return true;
	}

	public DistributedLock getServiceLock(String lockType) {
		return serviceLocksMap.get(lockType);
	}

	public void removeServiceLock(String lockType) {
		serviceLocksMap.remove(lockType);
	}

	// 分布式全局锁
	private Map<String, GlobalReentrantLock> globalLocksMap = new HashMap<>();

	public void setGlobalLockMap(Map<String, GlobalReentrantLock> globalLocksMap) {
		this.globalLocksMap = globalLocksMap;
	}

	public Map<String, GlobalReentrantLock> getGlobalLockMap() {
		return globalLocksMap;
	}

	public boolean setGlobalLock(String lockType, GlobalReentrantLock lock) {
		if (globalLocksMap.containsKey(lockType)) {
			return false;
		}
		globalLocksMap.put(lockType, lock);
		return true;
	}

	public DistributedLock getGlobalLock(String lockType) {
		return globalLocksMap.get(lockType);
	}

	public void removeGlobalLock(String lockType) {
		globalLocksMap.remove(lockType);
	}

	public static RedisVariable getDBVariable() {
		RedisVariable variable = (RedisVariable) BaseContext.CTX.getVariableContext().getVariable("redis");
		if (null == variable) {
			variable = new RedisVariable();
			BaseContext.CTX.getVariableContext().putVariable("redis", variable);
		}
		return variable;
	}
}
