package com.kingdom.sdk.ioc;


/**
 * 
 * 全局的数据修复接口
 * 应用中基本需要实现的
 *
 */
public interface ValueFix {
	public Object fix(Object o,String type);
}
