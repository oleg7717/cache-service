package ru.interprocom.axioma.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* Аннотация StoringCache указывает, что метод или методы класса сохраняет объекта возвращаемый методом в кэширующую БД.
 * @cacheName имя кэша в кэширующей БД
 * @key ключ, по которому происходит добавление / поиск для обновления. Принимает значение из параметра метода,
 * помеченного аннотацией @KeyParam. Значение может быть как простой переменной, так и объектом. В таком случае ключ
 * указывается следующим образом <имя переменной объекта>.<имя поля в объектк>, например, propDto.propname
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StoringCache {
	String cacheName();
	String key();
}
