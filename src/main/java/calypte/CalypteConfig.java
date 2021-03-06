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

import java.io.Serializable;

import org.brandao.entityfilemanager.EntityFileManager;

import calypte.memory.Memory;

/**
 * Define a configuração de um cache.
 * <pre>
 * ex:
 *     CalypteConfig config = new CalypteConfig();
 *     ...
 *     Cache cache = new Cache(config);
 * </pre>
 * @author Ribeiro
 *
 */
public class CalypteConfig implements Serializable{

	private static final long serialVersionUID = 9065603898804344980L;

	protected long nodesBufferSize;
    
	protected long nodesPageSize;
    
	protected long indexBufferSize;
    
	protected long indexPageSize;
    
	protected long dataBufferSize;
    
	protected long dataBlockSize;
    
	protected long dataPageSize;
    
	protected long maxSizeEntry;
    
    protected int maxSizeKey;
    
    protected Memory memory;
    
    protected String dataPath;
    
    protected EntityFileManager entityFileManager;
    
	/**
	 * Obtém o local onde os dados do cache serão aramazenados.
	 * @return pasta.
	 */
	public String getDataPath() {
		return dataPath;
	}

	/**
	 * Define o local onde os dados do cache serão aramazenados.
	 * @param dataPath pasta.
	 */
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	/**
	 * Obtém o tamanho do buffer usado para armazenar os nós na memória.
	 * @return tamanho em bytes
	 */
	public long getNodesBufferSize() {
		return nodesBufferSize;
	}

	/**
	 * Define o tamanho do buffer usado para armazenar os nós na memória.
	 * @param nodesBufferSize tamanho em bytes.
	 */
	public void setNodesBufferSize(long nodesBufferSize) {
		this.nodesBufferSize = nodesBufferSize;
	}

	/**
	 * Obtém o tamanho da página do buffer de nós.
	 * @return tamanho em bytes
	 */
	public long getNodesPageSize() {
		return nodesPageSize;
	}

	/**
	 * Define o tamanho da página do buffer de nós.
	 * @param nodesPageSize
	 */
	public void setNodesPageSize(long nodesPageSize) {
		this.nodesPageSize = nodesPageSize;
	}

	/**
	 * Obtém o tamanho do buffer usado para armazenar os índices dos itens na memória.
	 * @return tamanho em bytes.
	 */
	public long getIndexBufferSize() {
		return indexBufferSize;
	}

	/**
	 * Define o tamanho do buffer usado para armazenar os índices dos itens na memória.
	 * @param indexBufferSize tamanho em bytes.
	 */
	public void setIndexBufferSize(long indexBufferSize) {
		this.indexBufferSize = indexBufferSize;
	}

	/**
	 * Obtém o tamanho da página do buffer de índices.
	 * @return tamanho em bytes.
	 */
	public long getIndexPageSize() {
		return indexPageSize;
	}

	/**
	 * Define o tamanho da página do buffer de índices.
	 * @param indexPageSize tamanho em bytes.
	 */
	public void setIndexPageSize(long indexPageSize) {
		this.indexPageSize = indexPageSize;
	}

	/**
	 * Obtém o tamanho do buffer usado para armazenar os itens na memória.
	 * @return tamanho em bytes.
	 */
	public long getDataBufferSize() {
		return dataBufferSize;
	}

	/**
	 * Define o tamanho do buffer usado para armazenar os itens na memória.
	 * @param dataBufferSize tamanho em bytes.
	 */
	public void setDataBufferSize(long dataBufferSize) {
		this.dataBufferSize = dataBufferSize;
	}

	/**
	 * Obtém o tamanho do bloco de dados.
	 * @return tamanho em bytes.
	 */
	public long getDataBlockSize() {
		return dataBlockSize;
	}

	/**
	 * Define o tamanho do bloco de dados.
	 * @param dataBlockSize tamanho em bytes.
	 */
	public void setDataBlockSize(long dataBlockSize) {
		this.dataBlockSize = dataBlockSize;
	}

	/**
	 * Obtém o tamanho da página do buffer de itens.
	 * @return tamanho em bytes.
	 */
	public long getDataPageSize() {
		return dataPageSize;
	}

	/**
	 * Define o tamanho da página do buffer de itens.
	 * @param dataPageSize tamanho em bytes.
	 */
	public void setDataPageSize(long dataPageSize) {
		this.dataPageSize = dataPageSize;
	}

	/**
	 * Obtém o tamanho máximo que um item pode ter para ser armazenado no cache.
	 * @return tamanho em bytes
	 */
	public long getMaxSizeEntry() {
		return maxSizeEntry;
	}

	/**
	 * Define o tamanho máximo que um item pode ter para ser armazenado no cache.
	 * @param maxSizeEntry tamanho em bytes.
	 */
	public void setMaxSizeEntry(long maxSizeEntry) {
		this.maxSizeEntry = maxSizeEntry;
	}

	/**
	 * Obtém o tamanho máximo que uma chave pode ter.
	 * @return tamanho em bytes.
	 */
	public int getMaxSizeKey() {
		return maxSizeKey;
	}

	/**
	 * Define o tamanho máximo que uma chave pode ter.
	 * @param maxSizeKey tamanho em bytes.
	 */
	public void setMaxSizeKey(int maxSizeKey) {
		this.maxSizeKey = maxSizeKey;
	}

	/**
	 * Obtém a estratégia de acesso a memória.
	 * @return estratégia.
	 */
	public Memory getMemory() {
		return memory;
	}

	/**
	 * Define a estratégia de acesso à memória.
	 * @param memory estratégia.
	 */
	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	/**
	 * Obtém o sistema de arquivos.
	 * @return Sistema de arquivos.
	 */
	public EntityFileManager getEntityFileManager() {
		return entityFileManager;
	}

	/**
	 * Define o sistema de arquivos.
	 * @param entityFileManager Sistema de arquivos.
	 */
	public void setEntityFileManager(EntityFileManager entityFileManager) {
		this.entityFileManager = entityFileManager;
	}
    
}
