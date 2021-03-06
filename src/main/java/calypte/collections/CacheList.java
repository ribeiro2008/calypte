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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author Ribeiro
 */
public class CacheList<T> 
    implements List<T>,Serializable {

	private static final long serialVersionUID = -617590377196604703L;

    private final HugeArrayList<T> internalList;
    
    public CacheList(
            int maxCapacityElementsOnMemory,
            double fragmentFactorElements,
            Swapper<T> swapper){
        
        this.internalList = 
            new HugeArrayList<T>(
                maxCapacityElementsOnMemory, 
                fragmentFactorElements, 
                swapper);
        
        //this.internalList.setForceSwap(true);
    }
    
    public int size() {
        return this.internalList.size();
    }

    public boolean isEmpty() {
        return this.internalList.isEmpty();
    }

    public boolean contains(Object o) {
        return this.internalList.contains(o);
    }

    public Iterator<T> iterator() {
        return this.internalList.iterator();
    }

    public Object[] toArray() {
        return this.internalList.toArray();
    }

    public <K> K[] toArray(K[] a) {
        return this.internalList.toArray(a);
    }

    public boolean add(T e) {
        return this.internalList.add(e);
    }

    public boolean remove(Object o) {
        return this.internalList.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.internalList.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        return this.internalList.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return this.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return this.internalList.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.internalList.removeAll(c);
    }

    public void clear() {
        this.internalList.clear();
    }

    public T get(int index) {
        return this.internalList.get(index);
    }

    public T set(int index, T element) {
        return this.internalList.set(index, element);
    }

    public void add(int index, T element) {
        this.internalList.add(index, element);
    }

    public T remove(int index) {
        return this.remove(index);
    }

    public int indexOf(Object o) {
        return this.internalList.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.internalList.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return this.internalList.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return this.internalList.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return this.internalList.subList(fromIndex, toIndex);
    }

    public void setReadOnly(boolean value) {
        this.internalList.setReadOnly(value);
    }

    public boolean isReadOnly() {
        return this.internalList.isReadOnly();
    }

    public void flush(){
        this.internalList.flush();
    }
    
    private void writeObject(ObjectOutputStream stream) throws IOException {
        boolean original = this.internalList.isReadOnly();
        try{
            this.internalList.flush();
            this.internalList.setReadOnly(true);
            stream.defaultWriteObject();
        }
        finally{
            this.internalList.setReadOnly(original);
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }    
    
}
