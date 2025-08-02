package ru.interprocom.axioma.cache.core;

import lombok.Data;

@Data
public abstract class AxiomaCache {
	private long maxRowstamp;
	private long recordCount;

	public abstract void load();

	/**
	 * Синхронизация записей между базой данных и хранилищем кэша. Синхронизация осуществляется для простых действий
	 * в БД, например, удаление или добавление родительской записи, а также изменение родительской и в некоторых случаях
	 * дочерних записей. Для полноценной перезагрузки необходимо использовать механизм обновления записей при
	 * непосредственном добавлении / изменении / удалении, либо через реализацию метода reload(recordName) / reloadAll()
	 */
	public abstract void sync();

	public abstract void reloadAll();

	public abstract void reload(String recordName);
}
