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

import java.io.InputStream;

/**
 * É a classe central do Calypte. 
 * <p>Ele faz o mapeamento chave-valor. Uma chave 
 * somente pode estar associado a um valor. Não são
 * permtidos chaves duplicadas.</p> 
 * 
 * <pre>
 * ex:
 *    
 *    Cache cache = ...;
 *    cache.put("uma_chave", meuObjeto, 1200, 0);
 *    
 * ex2:
 *    
 *    MeuObjeto o = cache.get("uma_chave");
 *
 * </pre>
 * @author Ribeiro
 *
 */
public interface Cache {

	/*
	 * Busca por uma chave ou agrupamento de chaves. Se key for null, todas as chaves do 
	 * cache serão retornadas.
	 * @param key Chave a ser buscada.
	 * @param result Objeto que receberá as chaves encontradas.
	 */
	//void find(String key, FindResult result);
	
	/**
	 * Obtém a configuração do cache.
	 * @return Configuração.
	 */
	CalypteConfig getConfig();
	
    /**
     * Substitui o valor associado à chave somente se ele existir.
     * @param key chave associada ao valor.
     * @param value valor para ser associado à chave.
	 * @param timeToLive é a quantidade máxima de tempo que um item expira após sua criação.
	 * @param timeToIdle é a quantidade máxima de tempo que um item expira após o último acesso.
     * @return <code>true</code> se o valor for substituido. Caso contrário, <code>false</code>.
     * @throws StorageException Lançada se ocorrer alguma falha ao tentar inserir o item.
     */
	boolean replace(String key, Object value, 
			long timeToLive, long timeToIdle) throws StorageException;
	
    /**
     * Substitui o fluxo de bytes associado à chave somente se ele existir.
     * @param key chave associada ao valor.
	 * @param timeToLive é a quantidade máxima de tempo que um item expira após sua criação.
	 * @param timeToIdle é a quantidade máxima de tempo que um item expira após o último acesso.
     * @param inputData fluxo de bytes do valor.
     * @return <code>true</code> se o valor for substituido. Caso contrário, <code>false</code>.
     * @throws StorageException Lançada se ocorrer alguma falha ao tentar inserir o item.
     */
    boolean replaceStream(String key, InputStream inputData, 
    		long timeToLive, long timeToIdle) throws StorageException;
	
	/**
	 * Substitui o valor associado à chave somente se ele for igual a um determinado valor.
	 * @param key chave associada ao valor.
	 * @param oldValue valor esperado associado à chave.
	 * @param newValue valor para ser associado à chave.
	 * @param timeToLive é a quantidade máxima de tempo que um item expira após sua criação.
	 * @param timeToIdle é a quantidade máxima de tempo que um item expira após o último acesso.
	 * @return <code>true</code> se o valor for substituido. Caso contrário, <code>false</code>.
     * @throws StorageException Lançada se ocorrer alguma falha ao tentar inserir o item.
	 */
	boolean replace(String key, Object oldValue, 
			Object newValue, long timeToLive, long timeToIdle) throws StorageException;
	
	/**
	 * Associa o valor à chave somente se a chave não estiver associada a um valor.
	 * @param key chave associada ao valor.
	 * @param value valor para ser associado à chave.
	 * @param timeToLive é a quantidade máxima de tempo que um item expira após sua criação.
	 * @param timeToIdle é a quantidade máxima de tempo que um item expira após o último acesso.
	 * @return valor anterior associado à chave.
     * @throws StorageException Lançada se ocorrer alguma falha ao tentar inserir o item.
	 */
	Object putIfAbsent(String key,
			Object value, long timeToLive, long timeToIdle) throws StorageException;
	
    /**
     * Associa o fluxo de bytes do valor à chave somente se a chave não estiver associada a um valor.
     * @param key chave associada ao valor.
	 * @param timeToLive é a quantidade máxima de tempo que um item expira após sua criação.
	 * @param timeToIdle é a quantidade máxima de tempo que um item expira após o último acesso.
     * @param inputData fluxo de bytes do valor.
     * @return fluxo associado à chave ou <code>null</code>.
     * @throws StorageException Lançada se ocorrer alguma falha ao tentar inserir o item.
     */
    InputStream putIfAbsentStream(String key, InputStream inputData, 
    		long timeToLive, long timeToIdle) throws StorageException;
	
	/**
	 * Associa o valor à chave.
	 * @param key chave associada ao valor.
	 * @param value valor para ser associado à chave.
	 * @param timeToLive é a quantidade máxima de tempo que um item expira após sua criação.
	 * @param timeToIdle é a quantidade máxima de tempo que um item expira após o último acesso.
     * @return <code>true</code> se o item for substituido. Caso contrário, <code>false</code>
     * @throws StorageException Lançada se ocorrer alguma falha ao tentar inserir o item.
	 */
	boolean put(String key, Object value, long timeToLive, long timeToIdle) throws StorageException;
	
    /**
	 * Associa o fluxo de bytes do valor à chave.
	 * @param key chave associada ao fluxo.
	 * @param timeToLive é a quantidade máxima de tempo que um item expira após sua criação.
	 * @param timeToIdle é a quantidade máxima de tempo que um item expira após o último acesso.
     * @param inputData fluxo de bytes do valor.
     * @return <code>true</code> se o item for substituido. Caso contrário, <code>false</code>
     * @throws StorageException Lançada se ocorrer alguma falha ao tentar inserir o item.
     */
    boolean putStream(String key, InputStream inputData, 
    		long timeToLive, long timeToIdle) throws StorageException;
	
    /* métodos de coleta */
	
	/**
	 * Obtém o valor associado à chave.
	 * @param key chave associada ao valor.
     * @return valor associado à chave ou <code>null</code>.
     * @throws RecoverException Lançada se ocorrer alguma falha ao tentar obter o
     * item.
	 */
	Object get(String key) throws RecoverException;

    /**
     * Obtém o fluxo de bytes do valor associado à chave.
     * @param key chave associada ao fluxo.
     * @return fluxo de bytes do valor ou <code>null</code>.
     * @throws RecoverException Lançada se ocorrer alguma falha ao tentar obter o
     * item.
     */
    InputStream getStream(String key) throws RecoverException;
	
    /* métodos de remoção */

	/**
	 * Remove o valor associado à chave somente se ele for igual a um determinado valor.
	 * @param key chave associada ao valor.
	 * @param value valor esperado associado à chave.
	 * @return <code>true</code> se o valor for removido. Caso contrário, <code>false</code>.
	 * @throws StorageException Lançada se ocorrer alguma falha ao tentar remover o
     * item.
	 */
	boolean remove(String key, Object value) throws StorageException;
	
    /**
     * Remove o valor associado à chave.
     * @param key chave associada ao valor.
     * @return <code>true</code> se o valor for removido. Caso contrário <code>false</code>.
     * @throws StorageException Lançada se ocorrer alguma falha ao tentar remover o
     * item.
     */
    boolean remove(String key) throws StorageException;
	
    /* métodos de manipulação*/
    
	/**
	 * Obtém a quantidade de itens contido no cache.
	 * @return quantidade de itens.
	 */
	long size();

	/**
	 * Verifica se o cache está vazio.
	 * @return <code>true</code> se o cache estiver vazio. Caso contrário, <code>false</code>.
	 */
	boolean isEmpty();

	/**
	 * Verifica se uma chave está associado a um valor.
	 * @param key chave associada ao valor.
	 * @return <code>true</code> se existir um valor associado à chave. Caso contrário, <code>false</code>.
     * @throws RecoverException Lançada se ocorrer alguma falha ao tentar obter o
     * item.
	 */
	boolean containsKey(String key) throws RecoverException;

    /**
     * Obtém a quantidade de itens recuperados.
     * @return Quantidade de itens recuperados.
     */
    long getCountRead();

    /**
     * Obtém a quantidade de itens armazenados.
     * @return Quantidade de itens armazenados.
     */
    long getCountWrite();

    /**
     * Obtém a quantidade de itens removidos.
     * @return Quantidade de itens removidos.
     */
    long getCountRemoved();

    /**
     * Obtém a quantidade de bytes recuperados.
     * @return Quantidade de bytes recuperados.
     */
    long getCountReadData();
    
    /**
     * Obtém a quantidade de bytes armazenados.
     * @return Quantidade de bytes armazenados.
     */
    long getCountWriteData();

    /**
     * Obtém a quantidade de bytes removidos.
     * @return Quantidade de bytes removidos.
     */
    long getCountRemovedData();
    
    /**
     * Verifica se os arquivos contidos na pasta de dados serão 
     * destruidos junto com essa instância. 
     * @return <code>true</code> para destruir todos os arquivos. 
     * Caso contrário, <code>false</code>.
     */
    boolean isDeleteOnExit();

    /**
     * Define que os arquivos contidos na pasta de dados sejam 
     * destruidos junto com essa instância. 
     * @param deleteOnExit <code>true</code> para destruir todos os arquivos. 
     * Caso contrário, <code>false</code>.
     */
	void setDeleteOnExit(boolean deleteOnExit);

	/**
	 * Remove todas as entradas contidas no cache.
	 */
	void clear();
	
	/**
	 * Destrói os dados contidos nesta instância. Deve ser executado se {@link #isDeleteOnExit()} 
	 * for <code>false</code>.
	 */
	void destroy();

	/*
	public interface FindResult{
		
		void found(String key, Cache cache);
		
	}
	*/
}
