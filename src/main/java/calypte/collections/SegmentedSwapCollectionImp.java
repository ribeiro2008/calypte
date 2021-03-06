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

package calypte.collections;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;

/**
 * @author Ribeiro
 */
public class SegmentedSwapCollectionImp<T> 
    implements SwapCollection<T>, Serializable{
    
	private static final long serialVersionUID = 7817500681111470845L;

	private static final int MIN_ITENS_PER_SEGMENT = 5;
	
    protected SwapCollection<T>[] swapCollections;

    private transient File path;
    
    private transient boolean hasCreatePath;
    
    private int maxCapacity;
    
    private int maxSegmentCapacity;
    
    private int fragmentSize;
    
    private double clearFactor;
    
    protected boolean readOnly;
    
    private boolean forceSwap;
    
    private boolean live;
    
    @SuppressWarnings("unchecked")
	public SegmentedSwapCollectionImp(int maxCapacity, double fragmentFactor, Swapper<T> swap) {

        this.fragmentSize        = (int)(maxCapacity * fragmentFactor);
        this.maxCapacity         = maxCapacity;
        this.maxSegmentCapacity  = (int)(maxCapacity/fragmentSize) + (maxCapacity % fragmentSize != 0? 1 : 0) ;
        this.readOnly            = false;
        this.forceSwap           = true;
        this.live                = true;
        

        if(swap == null) {
        	throw new NullPointerException("swap");
        }
        
        if(this.fragmentSize < MIN_ITENS_PER_SEGMENT) {
        	throw new IllegalStateException("fragmentSize");
        }

        if(this.maxCapacity <= 0 ) {
        	throw new IllegalStateException("maxCapacity");
        }
        
        this.swapCollections = new SwapCollectionImp[maxSegmentCapacity];
        
        int countMaxCapacity = maxCapacity;
        
        for(int i=0;i<this.swapCollections.length;i++){
        	int capacity = 
        			(countMaxCapacity - fragmentSize) > 0? 
        					fragmentSize : 
        					countMaxCapacity;        			
        	this.swapCollections[i] = new SwapCollectionImp<T>(
        			swap, forceSwap, 
        			capacity,
					readOnly);
        	
        	countMaxCapacity = countMaxCapacity - fragmentSize;
        }
        
    }
    
	public void add(long index, T item) {
		swapCollections[(int)index%swapCollections.length].add(index, item);
	}

	public T set(long index, T item) {
		return swapCollections[(int)index%swapCollections.length].set(index, item);
	}

	public T get(long index) {
		return swapCollections[(int)index%swapCollections.length].get(index);
	}

	public boolean replace(long index, T oldValue, T item) {
		return swapCollections[(int)index%swapCollections.length].replace(index, oldValue, item);
	}

	public T replace(long index, T item) {
		return swapCollections[(int)index%swapCollections.length].replace(index, item);
	}

	public T putIfAbsent(long index, T item) {
		return swapCollections[(int)index%swapCollections.length].putIfAbsent(index, item);
	}
    
    public boolean isLive() {
		return live;
	}

    public void flush(){
    	for(SwapCollection<T> seg: this.swapCollections){
    		seg.flush();
    	}
    }
    
    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public boolean isHasCreatePath() {
        return hasCreatePath;
    }

    public void setHasCreatePath(boolean hasCreatePath) {
        this.hasCreatePath = hasCreatePath;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public long getMaxSegmentCapacity() {
        return maxSegmentCapacity;
    }

    public void setMaxSegmentCapacity(int maxSegmentCapacity) {
        this.maxSegmentCapacity = maxSegmentCapacity;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }

    public void setFragmentSize(int fragmentSize) {
        this.fragmentSize = fragmentSize;
    }

    public double getClearFactor() {
        return clearFactor;
    }

    public void setClearFactor(float clearFactor) {
        this.clearFactor = clearFactor;
    }

    public void setReadOnly(boolean value){
        this.readOnly = value;
    }
    
    public boolean isReadOnly(){
        return this.readOnly;
    }

    public boolean isForceSwap() {
        return forceSwap;
    }

    public void setForceSwap(boolean forceSwap) {
        this.forceSwap = forceSwap;
    }

    public void clear(){
    	for(SwapCollection<T> seg: swapCollections){
    		seg.clear();
    	}
    }
    
    public void destroy(){
    	for(SwapCollection<T> seg: swapCollections){
    		seg.destroy();
    	}
    }

	public long getId() {
		return swapCollections[0].getId();
	}

	public Lock getLock() {
		return swapCollections[0].getLock();
	}

	public Swapper<T> getSwap() {
		return swapCollections[0].getSwap();
	}

	public Lock getGroupLock(long index) {
		return swapCollections[(int)(index%swapCollections.length)].getLock();
	}

	public int getNumberOfGroups() {
		return swapCollections.length;
	}
	
}
