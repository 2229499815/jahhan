/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.cluster.support;

import java.util.List;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.cluster.Directory;

import lombok.extern.slf4j.Slf4j;
import net.jahhan.exception.JahhanException;
import net.jahhan.spi.LoadBalance;

/**
 * 失败安全，出现异常时，直接忽略，通常用于写入审计日志等操作。
 * 
 * <a href="http://en.wikipedia.org/wiki/Fail-safe">Fail-safe</a>
 * 
 * @author william.liangf
 */
@Slf4j
public class FailsafeClusterInvoker<T> extends AbstractClusterInvoker<T>{
    
    public FailsafeClusterInvoker(Directory<T> directory) {
        super(directory);
    }
    
    public Result doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance) throws JahhanException {
        try {
            checkInvokers(invokers, invocation);
            Invoker<T> invoker = select(loadbalance, invocation, invokers, null);
            return invoker.invoke(invocation);
        } catch (Throwable e) {
            log.error("Failsafe ignore exception: " + e.getMessage(), e);
            return new RpcResult(); // ignore
        }
    }
}