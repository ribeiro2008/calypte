/*
 * Calypte http://calypte.uoutec.com.br/
 * Copyright (C) 2018 UoUTec. (calypte@uoutec.com.br)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package calypte;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import calypte.CalypteConfig;
import calypte.BasicCache;
import calypte.CacheErrors;
import calypte.ConcurrentCache;
import calypte.RecoverException;
import calypte.StorageException;
import calypte.TXCacheHelper.ConcurrentTask;
import calypte.tx.CacheTransaction;
import calypte.tx.CacheTransactionManager;
import calypte.tx.CacheTransactionManagerImp;
import calypte.tx.TXCache;
import junit.framework.TestCase;

/**
 * 
 * @author Ribeiro
 *
 */
public class TXCacheTest extends TestCase{

	private static final String KEY    = "teste";

	private static final String VALUE  = "value";

	private static final String VALUE2 = "val";
	
	private TXCache cache;
	
	public void setUp(){
		CalypteConfig config        = new TestCalypteConfig();
		CacheTransactionManager txm = new CacheTransactionManagerImp("./tx", TimeUnit.SECONDS.toMillis(30));
		BasicCache ntxCache         = new ConcurrentCache(config);
		cache                       = ntxCache.getTXCache(txm);
	}
	
	public void tearDown(){
		cache.destroy();
		cache = null;
		System.gc();
	}
	
	/* replace */
	
	public void testReplace() throws StorageException{
		TestCase.assertFalse(cache.replace(KEY, VALUE, 0, 0));
	}

	public void testReplaceSuccess() throws StorageException, RecoverException{
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		TestCase.assertTrue(cache.replace(KEY, VALUE2, 0, 0));
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
	}

	public void testReplaceStream() throws StorageException, IOException{
		TestCase.assertFalse(cache.replaceStream(KEY, CacheTestHelper.toStream(VALUE), 0, 0));
	}

	public void testReplaceStreamSuccess() throws StorageException, RecoverException, IOException, ClassNotFoundException{
		cache.putStream(KEY, CacheTestHelper.toStream(VALUE), 0, 0);
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.getStream(KEY)));
		TestCase.assertTrue(cache.replaceStream(KEY, CacheTestHelper.toStream(VALUE2), 0, 0));
		TestCase.assertEquals(VALUE2, CacheTestHelper.toObject(cache.getStream(KEY)));
	}
	
	public void testReplaceExact() throws StorageException{
		TestCase.assertFalse(cache.replace(KEY, VALUE, VALUE2, 0, 0));
	}

	public void testReplaceExactSuccess() throws StorageException, RecoverException{
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		TestCase.assertTrue(cache.replace(KEY, VALUE, VALUE2, 0, 0));
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
	}

	/* putIfAbsent */
	
	public void testputIfAbsent() throws StorageException, RecoverException{
		TestCase.assertNull(cache.putIfAbsent(KEY, VALUE, 0, 0));
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	public void testputIfAbsentExistValue() throws StorageException, RecoverException{
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, cache.putIfAbsent(KEY, VALUE2, 0, 0));
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	public void testputIfAbsentStream() throws StorageException, RecoverException, IOException, ClassNotFoundException{
		TestCase.assertNull(cache.putIfAbsentStream(KEY, CacheTestHelper.toStream(VALUE), 0, 0));
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.getStream(KEY)));
	}

	public void testputIfAbsentStreamExistValue() throws StorageException, RecoverException, IOException, ClassNotFoundException{
		cache.putStream(KEY, CacheTestHelper.toStream(VALUE), 0, 0);
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.putIfAbsentStream(KEY, CacheTestHelper.toStream(VALUE2), 0, 0)));
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.getStream(KEY)));
	}
	
	/* put */
	
	public void testPut() throws StorageException, RecoverException{
		TestCase.assertNull((String)cache.get(KEY));
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	/* get */
	
	public void testGet() throws StorageException, RecoverException{
		TestCase.assertNull((String)cache.get(KEY));
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	public void testGetOverride() throws StorageException, RecoverException{
		TestCase.assertNull((String)cache.get(KEY));
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		cache.put(KEY, VALUE2, 0, 0);
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
	}

	/* remove */
	
	public void testRemoveExact() throws StorageException, RecoverException{
		TestCase.assertNull((String)cache.get(KEY));
		TestCase.assertFalse(cache.remove(KEY, VALUE));
		
		cache.put(KEY, VALUE, 0, 0);
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		
		TestCase.assertFalse(cache.remove(KEY, VALUE2));
		TestCase.assertTrue(cache.remove(KEY, VALUE));
		TestCase.assertNull(cache.get(KEY));
	}

	public void testRemove() throws StorageException, RecoverException{
		TestCase.assertNull((String)cache.get(KEY));
		TestCase.assertFalse(cache.remove(KEY));
		
		cache.put(KEY, VALUE, 0, 0);
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		
		TestCase.assertTrue(cache.remove(KEY));
		TestCase.assertNull(cache.get(KEY));
	}
	
	/* with explicit transaction */

	/* replace */
	
	public void testExplicitTransactionReplace() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertFalse(cache.replace(KEY, VALUE, 0, 0));
		tx.commit();
		TestCase.assertFalse(cache.replace(KEY, VALUE, 0, 0));
	}

	public void testExplicitTransactionReplaceSuccess() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		TestCase.assertTrue(cache.replace(KEY, VALUE2, 0, 0));
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
		tx.commit();
		
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
	}

	public void testExplicitTransactionReplaceExact() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertFalse(cache.replace(KEY, VALUE, VALUE2, 0, 0));
		tx.commit();
		TestCase.assertFalse(cache.replace(KEY, VALUE, VALUE2, 0, 0));
	}

	public void testExplicitTransactionReplaceExactSuccess() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		TestCase.assertTrue(cache.replace(KEY, VALUE, VALUE2, 0, 0));
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
		tx.commit();
		
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
	}

	/* putIfAbsent */
	
	public void testExplicitTransactionPutIfAbsent() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertNull(cache.putIfAbsent(KEY, VALUE, 0, 0));
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		tx.commit();
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	public void testExplicitTransactionPutIfAbsentExistValue() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, cache.putIfAbsent(KEY, VALUE2, 0, 0));
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		tx.commit();
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	/* put */
	
	public void testExplicitTransactionPut() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertNull((String)cache.get(KEY));
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		tx.commit();
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	/* get */
	
	public void testExplicitTransactionGet() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertNull((String)cache.get(KEY));
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		tx.commit();
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	public void testExplicitTransactionGetOverride() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertNull((String)cache.get(KEY));
		cache.put(KEY, VALUE, 0, 0);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		cache.put(KEY, VALUE2, 0, 0);
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
		tx.commit();
		
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
	}

	/* remove */
	
	public void testExplicitTransactionRemoveExact() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		
		TestCase.assertNull((String)cache.get(KEY));
		TestCase.assertFalse(cache.remove(KEY, VALUE));
		
		cache.put(KEY, VALUE, 0, 0);
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		
		TestCase.assertFalse(cache.remove(KEY, VALUE2));
		TestCase.assertTrue(cache.remove(KEY, VALUE));
		tx.commit();
		
		TestCase.assertFalse(cache.remove(KEY, VALUE));
	}

	public void testExplicitTransactionRemove() throws Throwable{
		CacheTransaction tx = cache.beginTransaction();
		
		TestCase.assertNull((String)cache.get(KEY));
		TestCase.assertFalse(cache.remove(KEY));
		
		cache.put(KEY, VALUE, 0, 0);
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		
		TestCase.assertTrue(cache.remove(KEY));
		TestCase.assertNull(cache.get(KEY));
		tx.commit();
		
		TestCase.assertNull(cache.get(KEY));
	}	
	
	/* concurrent transaction*/
	
	/* replace */
	
	public void testConcurrentTransactionReplace() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, CacheTestHelper.toStream(VALUE), CacheTestHelper.toStream(VALUE2)){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.putStream(key, (InputStream)value, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertNull(cache.get(KEY, true));
		task.start();
		Thread.sleep(2000);
		TestCase.assertFalse(cache.replace(KEY, VALUE, 0, 0));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE, cache.get(KEY));
	}

	public void testConcurrentTransactionReplaceSuccess() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		
		cache.put(KEY, VALUE, 0, 0);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		TestCase.assertTrue(cache.replace(KEY, VALUE2, 0, 0));
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
		
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	public void testConcurrentTransactionReplaceStream() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, CacheTestHelper.toStream(VALUE), CacheTestHelper.toStream(VALUE2)){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.putStream(key, (InputStream)value, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertNull(cache.getStream(KEY, true));
		task.start();
		Thread.sleep(2000);
		TestCase.assertFalse(cache.replaceStream(KEY, CacheTestHelper.toStream(VALUE), 0, 0));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.getStream(KEY)));
	}

	public void testConcurrentTransactionReplaceStreamSuccess() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, CacheTestHelper.toStream(VALUE), CacheTestHelper.toStream(VALUE2)){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.putStream(key, (InputStream)value, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		
		cache.putStream(KEY, CacheTestHelper.toStream(VALUE), 0, 0);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.getStream(KEY)));
		TestCase.assertTrue(cache.replaceStream(KEY, CacheTestHelper.toStream(VALUE2), 0, 0));
		TestCase.assertEquals(VALUE2, CacheTestHelper.toObject(cache.getStream(KEY)));
		
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.getStream(KEY)));
	}
	
	public void testConcurrentTransactionReplaceExact() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		cache.get(KEY, true);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertFalse(cache.replace(KEY, VALUE, VALUE2, 0, 0));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	public void testConcurrentTransactionReplaceExactSuccess() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		cache.put(KEY, VALUE, 0, 0);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		TestCase.assertTrue(cache.replace(KEY, VALUE, VALUE2, 0, 0));
		TestCase.assertEquals(VALUE2, (String)cache.get(KEY));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
	}

	/* putIfAbsent */
	
	public void testConcurrentTransactionPutIfAbsent() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value2, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		cache.get(KEY, true);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertNull(cache.putIfAbsent(KEY, VALUE, 0, 0));
		TestCase.assertEquals(VALUE, cache.get(KEY));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE2, cache.get(KEY));
	}

	public void testConcurrentTransactionPutIfAbsentExistValue() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value2, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		cache.put(KEY, VALUE, 0, 0);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, cache.putIfAbsent(KEY, VALUE2, 0, 0));
		TestCase.assertEquals(VALUE, cache.get(KEY));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE2, cache.get(KEY));
	}

	public void testConcurrentTransactionPutIfAbsentStream() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, CacheTestHelper.toStream(VALUE), CacheTestHelper.toStream(VALUE2)){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.putStream(key, (InputStream)value2, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		cache.getStream(KEY, true);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertNull(cache.putIfAbsentStream(KEY, CacheTestHelper.toStream(VALUE), 0, 0));
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.getStream(KEY)));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE2, CacheTestHelper.toObject(cache.getStream(KEY)));
	}

	public void testConcurrentTransactionPutIfAbsentStreamExistValue() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value2, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		cache.putStream(KEY, CacheTestHelper.toStream(VALUE), 0, 0);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.putIfAbsentStream(KEY, CacheTestHelper.toStream(VALUE2), 0, 0)));
		TestCase.assertEquals(VALUE, CacheTestHelper.toObject(cache.getStream(KEY)));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE2, CacheTestHelper.toObject(cache.getStream(KEY)));
	}	
	/* put */
	
	public void testConcurrentTransactionPut() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value2, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertNull((String)cache.get(KEY));
		cache.put(KEY, VALUE, 0, 0);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, cache.get(KEY));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE2, cache.get(KEY));
	}

	/* get */
	
	public void testConcurrentTransactionGet() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value2, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertNull((String)cache.get(KEY));
		cache.put(KEY, VALUE, 0, 0);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, cache.get(KEY));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE2, cache.get(KEY));
	}

	public void testConcurrentTransactionGetOverride() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value2, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		TestCase.assertNull(cache.get(KEY));
		cache.put(KEY, VALUE, 0, 0);
		
		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, cache.get(KEY));
		cache.put(KEY, VALUE2, 0, 0);
		TestCase.assertEquals(VALUE2, cache.get(KEY));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE2, cache.get(KEY));
	}

	/* remove */
	
	public void testConcurrentTransactionRemoveExact() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value2, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		
		TestCase.assertNull((String)cache.get(KEY));
		TestCase.assertFalse(cache.remove(KEY, VALUE));
		cache.put(KEY, VALUE, 0, 0);

		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, cache.get(KEY));
		
		TestCase.assertFalse(cache.remove(KEY, VALUE2));
		TestCase.assertTrue(cache.remove(KEY, VALUE));
		TestCase.assertNull(cache.get(KEY));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE2, cache.get(KEY));
	}

	public void testConcurrentTransactionRemove() throws Throwable{
		ConcurrentTask task = new ConcurrentTask(cache, KEY, VALUE, VALUE2){

			@Override
			protected void execute(TXCache cache, String key, Object value,
					Object value2) throws Throwable {
				cache.put(key, value2, 0, 0);
			}
			
		};
		
		CacheTransaction tx = cache.beginTransaction();
		
		TestCase.assertNull((String)cache.get(KEY));
		TestCase.assertFalse(cache.remove(KEY));
		cache.put(KEY, VALUE, 0, 0);

		task.start();
		Thread.sleep(2000);
		
		TestCase.assertEquals(VALUE, (String)cache.get(KEY));
		TestCase.assertTrue(cache.remove(KEY));
		TestCase.assertNull(cache.get(KEY));
		tx.commit();
		
		Thread.sleep(1000);
		TestCase.assertEquals(VALUE2, cache.get(KEY));
	}

	/* timeToLive */
	
	public void testTimeToLive() throws InterruptedException{
		cache.put(KEY, VALUE, 1000, 0);
		assertEquals(cache.get(KEY), VALUE);
		Thread.sleep(800);
		assertEquals(cache.get(KEY), VALUE);
		Thread.sleep(400);
		assertNull(cache.get(KEY));
	}

	public void testTimeToLiveLessThanTimeToIdle() throws InterruptedException{
		cache.put(KEY, VALUE, 1000, 5000);
		assertEquals(cache.get(KEY), VALUE);
		Thread.sleep(1200);
		assertNull(cache.get(KEY));
	}

	public void testNegativeTimeToLive() throws InterruptedException{
		try{
			cache.put(KEY, VALUE, -1, 5000);
			fail();
		}
		catch(StorageException e){
			if(!e.getError().equals(CacheErrors.ERROR_1029)){
				fail();
			}
				
		}
	}

	/* TimeToIdle */
	
	public void testTimeToIdle() throws InterruptedException{
		cache.put(KEY, VALUE, 0, 1000);
		assertEquals(cache.get(KEY), VALUE);
		Thread.sleep(800);
		assertEquals(cache.get(KEY), VALUE);
		Thread.sleep(800);
		assertEquals(cache.get(KEY), VALUE);
		Thread.sleep(1200);
		assertNull(cache.get(KEY));
		
	}

	public void testTimeToIdleLessThanTimeToLive() throws InterruptedException{
		cache.put(KEY, VALUE, 20000, 1000);
		assertEquals(cache.get(KEY), VALUE);
		Thread.sleep(800);
		assertEquals(cache.get(KEY), VALUE);
		Thread.sleep(800);
		assertEquals(cache.get(KEY), VALUE);
		Thread.sleep(1200);
		assertNull(cache.get(KEY));
	}

	public void testNegativeTimeToIdle() throws InterruptedException{
		try{
			cache.put(KEY, VALUE, 0, -1);
			fail();
		}
		catch(StorageException e){
			if(!e.getError().equals(CacheErrors.ERROR_1028)){
				fail();
			}
				
		}
	}
	
}
