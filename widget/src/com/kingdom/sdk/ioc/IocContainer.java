package com.kingdom.sdk.ioc;

import java.util.HashMap;
import java.util.Map;

import com.kingdom.sdk.ioc.Instance.AsAlians;
import com.kingdom.sdk.ioc.Instance.InstanceScope;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;


/***
 * 实例容器
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("rawtypes")
public class IocContainer {

	static IocContainer share;

	Application application;

	public IocContainer() {
		if (share == null) {
			share = this;
		}
		this.instanceByClazz = new HashMap<Class, Instance>();
		this.instanceByName = new HashMap<String, Instance>();
	}

	public static IocContainer getShare() {
		if (share == null) {
			share = new IocContainer();
		}
		return share;
	}

	/**
	 * 
	 * @param application
	 */
	public void initApplication(Application application) {
		this.application = application;
	}

	@SuppressWarnings("unchecked")
	public <T extends Application> T getApplication() {
		return (T) application;
	}

	// 托管的实例
	Map<String, Instance> instanceByName;

	// 托管的实例
	Map<Class, Instance> instanceByClazz;

	/**
	 * 通过 别名获取
	 * 
	 * @param name
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T) instanceByName.get(name).get(
				application.getApplicationContext());
	}

	public Context getApplicationContext() {
		return application.getApplicationContext();
	}

	/**
	 * 通过接口获取
	 * 
	 * @param clazz
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		T t = getSysService(clazz);
		if (t != null)
			return t;
		Instance instance = instanceByClazz.get(clazz);
		if (instance != null) {
			return (T) instance.get(getApplicationContext());
		} else {
			bind(clazz).to(clazz).scope(InstanceScope.SCOPE_SINGLETON);
			Instance newins = instanceByClazz.get(clazz);
			if (newins != null) {
				return (T) newins.get(getApplicationContext());
			}
		}
		return null;
	}

	/**
	 * 获取一个 tag相同的对象 如果不存在就创建,存在就获取
	 * 
	 * @param clazz
	 * @param tag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz, String tag) {
		Instance instance = instanceByClazz.get(clazz);
		if (instance != null) {
			return (T) instance.get(getApplicationContext(), tag);
		} else {
			bind(clazz).to(clazz).scope(InstanceScope.SCOPE_SINGLETON);
			instance = instanceByClazz.get(clazz);
			return (T) instance.get(getApplicationContext(), tag);
		}
	}

	/**
	 * 对象配置
	 * 
	 * @param clazz
	 * @return
	 */
	public Instance bind(Class clazz) {
		Instance instance = new Instance(clazz);
		instance.setAsAlians(new AsAlians() {
			public void as(Instance ins, String name, Class toClazz) {
				if (name != null) {
					if (!instanceByName.containsKey(name)) {
						instanceByName.put(name, ins);
					} else {
						// 重复第一次的为准
						// throw new IocException("name:"+name+"重复");
					}
				}
				if (toClazz != null) {
					if (!instanceByClazz.containsKey(toClazz)) {
						instanceByClazz.put(toClazz, ins);
					} else {
						// 重复第一次的为准
						// throw new IocException("class:"+toClazz+"重复");
					}
				}
			}
		});
		return instance;
	}

	public Instance getInstance(Class clazz) {
		return instanceByClazz.get(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSysService(Class<T> clazz) {
		T t = null;
		if (clazz == NotificationManager.class) {
			t = (T) getApplicationContext().getSystemService(
					Context.NOTIFICATION_SERVICE);
		}else if(clazz==ActivityManager.class){
			t = (T) getApplicationContext().getSystemService(
					Context.ACTIVITY_SERVICE);
		}else if(clazz == PackageManager.class){
			t=(T) getApplicationContext().getPackageManager();
		}else if(clazz==AssetManager.class){
			t=(T) getApplicationContext().getAssets();
		}
		return t;

	}

}
