package com.jty.util;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;

/**
 * Fragment helper.
 * 
 */
public class FragmentHelper {

	// ---------------------------------------------------- Constructor
	private FragmentHelper() {
	}

	// ---------------------------------------------------- Public methods
	/**
	 * 添加fragment.
	 * 
	 * @param fm
	 *            fragment manager.
	 * @param fragment
	 *            fragment.
	 * @param resid
	 *            container resource id.
	 * @param addToBackStack
	 *            是否加入fragment回退栈。
	 */
	public static void add(FragmentManager fm, Fragment fragment, int resid,
			boolean addToBackStack) {
		FragmentTransaction ft = fm.beginTransaction();

		ft.replace(resid, fragment);

		if (addToBackStack) {
			ft.addToBackStack(fragment.getClass().getName());
		}

		ft.commit();
	}
	public static void add(FragmentManager fm, Fragment fragment, int resid) {
		add(fm, fragment, resid, false);
	}

	/**
	 * 添加fragment.
	 * 
	 * @param fm
	 *            fragment manager.
	 * @param fragment
	 *            fragment.
	 * @param resid
	 *            container resource id.
	 * @param addToBackStack
	 *            是否加入fragment回退栈。
	 */
	public static void add(FragmentManager fm, Context context, String fname,
			int resid, boolean addToBackStack) {

		// 查找盏中是否存在当前fragment，如果存在把它上层包括它自己全部弹出
		popBackStack(fm, fname, true);

		FragmentTransaction ft = fm.beginTransaction();

		Fragment fragment = Fragment.instantiate(context, fname);

		ft.replace(resid, fragment);

		if (addToBackStack) {
			ft.addToBackStack(fname);
		}

		ft.commit();

	}
	public static void add(FragmentManager fm, Context context, String fname,
			int resid) {
		add(fm, context, fname, resid, false);
	}

	/**
	 * 移除fragment。
	 * 
	 * @param fm
	 *            fragment manager.
	 * @param fragment
	 *            fragment.
	 */
	public static void remove(FragmentManager fm, Fragment fragment) {
		FragmentTransaction ft = fm.beginTransaction();

		ft.remove(fragment);

		ft.commitAllowingStateLoss();
	}

	/**
	 * 根据类名移除fragment.
	 * 
	 * @param fm
	 *            fragment manager.
	 * 
	 * @param fname
	 *            fragment类名.
	 */
	public static void remove(FragmentManager fm, String fname) {
		FragmentTransaction ft = fm.beginTransaction();

		List<Fragment> fragments = fm.getFragments();
		if (fragments == null) {
			return;
		}

		for (Fragment fragment : fragments) {
			if (fragment == null) {
				continue;
			}

			if (fragment.getClass().getName().equals(fname)) {
				ft.remove(fragment);
				ft.commitAllowingStateLoss();
				return;
			}
		}
	}

	/**
	 * 移除所有的fragment.
	 * 
	 * @param fm
	 *            fragment manager.
	 */
	public static void removeAll(FragmentManager fm) {
		FragmentTransaction ft = fm.beginTransaction();

		List<Fragment> fragments = fm.getFragments();
		if (fragments == null) {
			return;
		}

		for (Fragment fragment : fragments) {
			if (fragment == null) {
				continue;
			}

			ft.remove(fragment);
		}
		ft.commitAllowingStateLoss();
	}

	/**
	 * 根据类名查找fragment.
	 * 
	 * @param fm
	 *            fragment manager.
	 * @param fname
	 *            fragment类名。
	 * @return 匹配的fragment：null - 没有匹配的fragment.
	 */
	public static Fragment find(FragmentManager fm, String fname) {
		List<Fragment> fragments = fm.getFragments();
		if (fragments == null) {
			return null;
		}

		for (Fragment fragment : fragments) {
			if (fragment == null) {
				continue;
			}
			if (fragment.getClass().getName().equals(fname)) {

				return fragment;
			} else {
				find(fragment.getChildFragmentManager(), fname);
			}
		}

		return null;
	}

	/**
	 * 按回退键时，若Fragment回退栈里有fragment，则弹出顶层的fragment。
	 * 
	 * @param fm
	 *            FragmentManager。
	 * @return 是否有fragment弹出。
	 */
	public static boolean onBackPressed(FragmentManager fm) {
		if (fm == null) {
			return false;
		}

		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
			return true;
		}

		List<Fragment> fragList = fm.getFragments();
		if (fragList == null) {
			return false;
		}

		for (Fragment frag : fragList) {
			if (frag == null) {
				continue;
			}

			if (frag.isVisible()
					&& onBackPressed(frag.getChildFragmentManager())) {
				return true;
			}
		}

		return false;
	}

	public static void popBackStackInclusive(FragmentManager fm) {
		FragmentTransaction ft = fm.beginTransaction();
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

		ft.commitAllowingStateLoss();
	}

	/**
	 * 弹出当前fragment上的所有盏
	 * 
	 * @param fm
	 *            FragmentManager
	 * @param fragmentName
	 *            fragment名
	 * 
	 * @return true弹出成功，false弹出失败
	 */
	public static boolean popUpperBackStack(FragmentManager fm,
			String fragmentName) {
		if (fm == null) {
			return false;
		}

		// 获取所有fragmentlist
		List<Fragment> fragList = fm.getFragments();
		if (fragList == null) {
			return false;
		}

		// 遍历fragment list
		for (Fragment frag : fragList) {

			if (frag == null) {
				continue;
			}

			// 找到对应的fragment
			if (frag.getClass().getName().equals(fragmentName)) {

				// 遍历弹出上层fragment
				for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
					BackStackEntry stackEntry = fm.getBackStackEntryAt(i);
					if (stackEntry.getClass().getName().equals(fragmentName)) {
						fm.popBackStack(stackEntry.getName(),
								FragmentManager.POP_BACK_STACK_INCLUSIVE);
					} else {
						fm.popBackStack(stackEntry.getName(),
								FragmentManager.POP_BACK_STACK_INCLUSIVE);
					}

				}

				return true;

			} else {

				popUpperBackStack(frag.getChildFragmentManager(), fragmentName);
			}
		}

		return false;
	}

	public static void popBackStack(FragmentManager fm, String fname) {

		popBackStack(fm, fname, false);
	}

	public static void popBackStack(FragmentManager fm, String fname,
			boolean isContains) {
		for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
			BackStackEntry stackEntry = fm.getBackStackEntryAt(i);
			if (stackEntry.getName().equals(fname)) {
				fm.popBackStack(fname,
						isContains ? FragmentManager.POP_BACK_STACK_INCLUSIVE
								: 0);
			}
		}
	}
}
