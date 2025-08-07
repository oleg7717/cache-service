package ru.interprocom.axioma.cache.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.interprocom.axioma.cache.annotation.DeletingCache;
import ru.interprocom.axioma.cache.annotation.KeyParam;
import ru.interprocom.axioma.cache.annotation.StoringCache;
import ru.interprocom.axioma.cache.exception.AnnotationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class CachingService {
	@Autowired
	CacheDBManager cacheDBManager;

	@AfterReturning(value = "@annotation(ru.interprocom.axioma.cache.annotation.StoringCache)", returning = "returnValue")
	public void storingCache(JoinPoint joinPoint, Object returnValue)
			throws NoSuchFieldException, IllegalAccessException {
		log.info("Executing method annotated with @StoringCache");
		CacheParam cacheParam = getCacheName(joinPoint, StoringCache.class);
		Map<Object, Object> cache = cacheDBManager.getMap(cacheParam.getCacheName());
		cache.put(getParameterValue(joinPoint, cacheParam.getCacheKey()), returnValue);
	}

	@After(value = "@annotation(ru.interprocom.axioma.cache.annotation.DeletingCache)")
	public void deletingCache(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
		log.info("Executing method annotated with @DeletingCache");
		CacheParam cacheParam = getCacheName(joinPoint, DeletingCache.class);
		Map<Object, Object> cache = cacheDBManager.getMap(cacheParam.getCacheName());
		cache.remove(getParameterValue(joinPoint, cacheParam.getCacheKey()));
	}

	private CacheParam getCacheName(JoinPoint joinPoint, Class<? extends Annotation> customAnnotation) {
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		if (customAnnotation.getSimpleName().equals(StoringCache.class.getSimpleName())) {
			StoringCache storingCache = method.getAnnotation(StoringCache.class);
			return new CacheParam(storingCache.cacheName(), storingCache.key());
		} else if (customAnnotation.getSimpleName().equals(DeletingCache.class.getSimpleName())) {
			DeletingCache deletingCache = method.getAnnotation(DeletingCache.class);
			return new CacheParam(deletingCache.cacheName(), deletingCache.key());
		}

		throw new AnnotationException("Annotation " + customAnnotation.getSimpleName() + " doesn't diclared.");
	}

	private Object getParameterValue(JoinPoint joinPoint,String cacheKey) throws NoSuchFieldException, IllegalAccessException {
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		Parameter[] parameters = method.getParameters();
		Object[] args = joinPoint.getArgs();

		for (int i = 0; i < args.length; ++i) {
			if (parameters[i].isAnnotationPresent(KeyParam.class)) {
				if (cacheKey.contains(".")) {
					Field field = args[i].getClass().getDeclaredField(cacheKey.split("\\.")[1]);
					field.setAccessible(true);
					return field.get(args[i]);
				} else {
					return args[i];
				}
			}
		}

		throw new AnnotationException("Method use StoringCache or DeletingCache, but parameter with annotation " +
				"KeyObject not found.");
	}

	@AllArgsConstructor
	@Getter
	private class CacheParam {
		private String cacheName;
		private String cacheKey;
	}
}
