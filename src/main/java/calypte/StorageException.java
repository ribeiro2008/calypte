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
 * Exceção lançada quando ocorre uma falha ao tentar armazenar um item em um cache.
 * 
 * @author Ribeiro
 */
public class StorageException extends CacheException{
    
	private static final long serialVersionUID = 2848276802048022365L;

    public StorageException(Throwable thrwbl, CacheError error, Object ... params) {
        super(thrwbl, error, params);
    }

    public StorageException(CacheError error, Object ... params) {
        super(error, params);
    }
    
}
