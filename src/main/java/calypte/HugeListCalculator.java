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

/**
 * 
 * @author Ribeiro
 *
 */
class HugeListCalculator {

	public static HugeListInfo calculate(
			long dataBufferSize, long dataPageSize, 
			long blockSize, double dataSwapFactor){
		
    	if(dataBufferSize <= 0)
    		throw new IllegalArgumentException("buffer size <= 0");

    	if(dataPageSize <= 0)
    		throw new IllegalArgumentException("slab size <= 0");

    	if(blockSize <= 0)
    		throw new IllegalArgumentException("block size <= 0");

    	if(dataPageSize > dataBufferSize)
    		throw new IllegalArgumentException("page size > buffer size");

    	if(blockSize > dataPageSize)
    		throw new IllegalArgumentException("block size > page size");
    	
    	double subLists       = (dataBufferSize / (blockSize*2048L));
    	//subLists = 1;
    	subLists              = subLists > 12? 12 : subLists;
    	subLists              = subLists == 0? 1 : subLists;
    	
    	dataBufferSize        = (long)(dataBufferSize / subLists);
    	
    	//Quantidade de blocos na memória.
    	double blocksLength   = dataBufferSize/blockSize;
    	blocksLength          = dataBufferSize%blockSize > 0? blocksLength + 1 : blocksLength;
    	
    	//Quantidade de blocos em uma página
    	double blocksPerPage  = dataPageSize/blockSize;
    	blocksPerPage         = dataPageSize%blockSize > 0? blocksPerPage + 1 : blocksPerPage;
    	
    	//Fator de páginas. Usado para definir o fator de fragmentação da lista.
    	double pageFactor     = blocksPerPage/blocksLength;
    	//Quantidade de páginas na memória.
    	//double slabs          = blocksLength/blocksPerSlab;
    	
    	//Tamanho do buffer usado para fazer a permuta.
    	double swapBufferSize = dataBufferSize*dataSwapFactor;
    	//Quantidade de blocos que sofrerão permuta.
    	double swapBlocks     = swapBufferSize/blockSize;
    	swapBlocks            = swapBufferSize%blockSize > 0? swapBlocks + 1 : swapBlocks;
    	//Fator de permuta. Usado para definir o fator de permuta da lista.
        double swapFactor     = swapBlocks/blocksLength;
        
    	if(swapBlocks <= 0)
    		throw new IllegalArgumentException("swap factor is invalid: " + swapBlocks);

        return new HugeListInfo((int)blocksLength, swapFactor, pageFactor, (int)subLists);    			
	}
	
	public static class HugeListInfo{
		
		private int maxCapacityElements;
		
		private double clearFactorElements;
        
		private double fragmentFactorElements;

		private int subLists;
		
		public HugeListInfo(int maxCapacityElements,
				double clearFactorElements, double fragmentFactorElements, int subLists) {
			this.maxCapacityElements = maxCapacityElements;
			this.clearFactorElements = clearFactorElements;
			this.fragmentFactorElements = fragmentFactorElements;
			this.subLists = subLists;
		}

		public int getSubLists() {
			return subLists;
		}

		public void setSubLists(int subLists) {
			this.subLists = subLists;
		}

		public int getMaxCapacityElements() {
			return maxCapacityElements;
		}

		public void setMaxCapacityElements(int maxCapacityElements) {
			this.maxCapacityElements = maxCapacityElements;
		}

		public double getClearFactorElements() {
			return clearFactorElements;
		}

		public void setClearFactorElements(double clearFactorElements) {
			this.clearFactorElements = clearFactorElements;
		}

		public double getFragmentFactorElements() {
			return fragmentFactorElements;
		}

		public void setFragmentFactorElements(double fragmentFactorElements) {
			this.fragmentFactorElements = fragmentFactorElements;
		}

		@Override
		public String toString() {
			return "HugeListInfo [maxCapacityElements=" + maxCapacityElements
					+ ", clearFactorElements=" + clearFactorElements
					+ ", fragmentFactorElements=" + fragmentFactorElements
					+ ", subLists=" + subLists + "]";
		}
        
	}
}
