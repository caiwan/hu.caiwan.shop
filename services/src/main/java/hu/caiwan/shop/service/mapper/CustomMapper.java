package hu.caiwan.shop.service.mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.caiwan.utils.Pair;

/**
 * Custom object mapper that relies on reflection works only with basic types
 * 
 * @author caiwan
 *
 */
public abstract class CustomMapper<D, E> implements AbstractMapper<D, E> {

	private Pattern getterPattern = Pattern.compile("get(\\w*)");
	private Pattern setterPattern = Pattern.compile("set(\\w*)");

	private Pattern baseTypePattern = Pattern.compile("java\\.lang\\.(\\w*)");

	protected List<Pair<Method, Method>> entity2Dto;
	protected List<Pair<Method, Method>> dto2Entity;

	public CustomMapper(Class<D> dtoClass, Class<E> entityClass) {
		createMethodMap(entityClass, dtoClass);
	}

	@SuppressWarnings("rawtypes")
	protected void createMethodMap(Class entity, Class dto) {
		if (entity2Dto != null && dto2Entity != null)
			return;

		Map<String, Method> entitySetters = new HashMap<>();
		Map<String, Method> entityGetters = new HashMap<>();

		extractMethods(entity.getMethods(), entitySetters, entityGetters);

		Map<String, Method> dtoSetters = new HashMap<>();
		Map<String, Method> dtoGetters = new HashMap<>();

		extractMethods(dto.getMethods(), dtoSetters, dtoGetters);

		entity2Dto = new LinkedList<>();
		createMethodPairs(entityGetters, dtoSetters, entity2Dto);

		dto2Entity = new LinkedList<>();
		createMethodPairs(dtoGetters, entitySetters, dto2Entity);
	}

	/**
	 * Default mapping based on maps that already build
	 * 
	 * @param e
	 * @param d
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected void defaultEntity2Dto(Object e, Object d) throws RuntimeException {
		mapOne2Other(entity2Dto, e, d);
	}

	/**
	 * Default mapping based on maps that has already build
	 * 
	 * @param d
	 * @param e
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected void defaultDto2Entity(Object d, Object e) throws RuntimeException {
		mapOne2Other(dto2Entity, d, e);
	}

	private void mapOne2Other(List<Pair<Method, Method>> reflection, Object one, Object other) throws RuntimeException {
		try {
			for (Pair<Method, Method> pair : reflection) {
				Object ret;
				ret = pair.getLeft().invoke(one);
				pair.getRight().invoke(other, ret);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Matches together the get/set pairs These maps are without get/set prefix
	 * 
	 * @See extractMethods
	 * 
	 * @param oneMethods
	 * @param otherMethods
	 * @param resultPairs
	 *            method pairs of one->other
	 */
	private void createMethodPairs(Map<String, Method> oneMethods, Map<String, Method> otherMethods,
			List<Pair<Method, Method>> resultPairs) {
		for (String key : oneMethods.keySet()) {
			if (otherMethods.containsKey(key)) {
				resultPairs.add(new Pair<Method, Method>(oneMethods.get(key), otherMethods.get(key)));
			}
		}
	}

	/**
	 * Extracts the getters and setters out of the class
	 * 
	 * @param methods
	 *            list of methods in class
	 * @param setters
	 *            out
	 * @param getters
	 *            out
	 */
	private void extractMethods(Method[] methods, Map<String, Method> setters, Map<String, Method> getters) {
		getters.clear();
		setters.clear();

		for (Method method : methods) {
			String name = method.getName();

			Matcher getterMatcher = getterPattern.matcher(name);
			if (getterMatcher.matches()) {
				String pkg = method.getReturnType().getCanonicalName();
				if (baseTypePattern.matcher(pkg).matches()) {
					getters.put(getterMatcher.group(1).toLowerCase(), method);
				}
			}

			Matcher setterMatcher = setterPattern.matcher(name);
			if (setterMatcher.matches() && method.getParameterCount() == 1) {
				String pkg = method.getParameterTypes()[0].getCanonicalName();
				if (baseTypePattern.matcher(pkg).matches()) {
					setters.put(setterMatcher.group(1).toLowerCase(), method);
				}
			}
		}

	}

}
