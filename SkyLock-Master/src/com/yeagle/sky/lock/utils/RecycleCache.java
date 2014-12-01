package com.yeagle.sky.lock.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class RecycleCache<K, V> {
	
	private final HashMap<K, V> mMap = new LinkedHashMap<K, V>(16, 0.75f, true){ // true 就可以起到用到的放在上面的排�?

		@Override
		protected final boolean removeEldestEntry(Entry<K, V> eldest) {
			return size() > 16; // true 就移掉eldest�?
		}
	};
	
	private final HashMap<K, KeyReference<K, V>> mReferenceMap = new HashMap<K, KeyReference<K, V>>();
	private ReferenceQueue<V> mRecyleQueue = new ReferenceQueue<V>();
	private OnRecycleListener mListener;
	
	private void clearRecyleQueue() {
		KeyReference<K, V> reference = (KeyReference<K, V>)mRecyleQueue.poll();
		while (reference != null) {
			if (mListener != null) {
				mListener.recycle(reference.get());
			}
			
			mReferenceMap.remove(reference.key);
			reference = (KeyReference<K, V>)mRecyleQueue.poll();
		}
	}
	
	public V put(K key, V value) {
		clearRecyleQueue();
		mMap.put(key, value);
		
		KeyReference<K, V> reference = new KeyReference<K, V>(key, value, mRecyleQueue);
		KeyReference<K, V> ref = mReferenceMap.put(key, reference);
		if (ref == null) // new value�?
			return null;
		else
			return ref.get();
	}
	
	public V getValue(K key) {
		clearRecyleQueue();
		V v = mMap.get(key);
		
		if (v != null)
			return v;
		
		KeyReference<K, V> reference = mReferenceMap.get(key);
		if (reference != null)
			return reference.get();
		
		return null;
	}
	
	public void setOnRecycleListener(OnRecycleListener listener) {
		this.mListener = listener;
	}
	
	public interface OnRecycleListener {
		public void recycle(Object object);
	}
	
	private static class KeyReference<K, V> extends WeakReference<V> {
		K key;
		
		public KeyReference(K k, V v, ReferenceQueue<? super V> q) {
			super(v, q);
			this.key = k;
		}
	}
}
