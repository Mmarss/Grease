package net.mmarss.grease.ecs;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The core data manager for an entity-component system.
 * 
 * Entities should be created using an instance of this class. This class then
 * stores those entities, as well as any components associated with them.
 */
public class EntityManager {
	
	/**
	 * The counter used to keep track of which entity IDs have been used by this
	 * entity manager.
	 */
	private AtomicLong idCounter;
	
	/**
	 * The map used to translate entities in other managers to entities in this
	 * manager.
	 */
	private Map< UUID, Entity >												uuidMap;
	/** The map used to store all of the components associated with entities. */
	private Map< Class< ? extends Component >, Map< Entity, Component > >	componentMap;
	
	/**
	 * Constructs a new empty entity manager.
	 */
	public EntityManager() {
		
		idCounter = new AtomicLong();
		componentMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * Creates and registers a new entity with this entity manager.
	 * 
	 * @return the newly created entity.
	 */
	public Entity createEntity() {
		
		long id = idCounter.getAndIncrement();
		UUID uuid = UUID.randomUUID();
		Entity entity = new Entity(id, uuid);
		uuidMap.put(uuid, entity);
		return entity;
	}
	
	/**
	 * Deletes the specified entity and all of its associated data from this entity
	 * manager.
	 * 
	 * @param entity
	 *            the entity to be removed.
	 */
	public void deleteEntity(Entity entity) {
		
		for (Map< Entity, Component > entityComponentMap : componentMap.values()) {
			entityComponentMap.remove(entity);
		}
		
		uuidMap.remove(entity.getUuid());
	}
	
	/**
	 * Associates the given component with the specified entity, storing the
	 * relation in this manager. Note that if the entity already has a component of
	 * this type, then the old association will be removed.
	 * 
	 * If an entity should be able to have multiple components of a single type
	 * (for example, tag strings), then the user should create a component to hold a
	 * collection of such components (for example, a TagComponent that holds a
	 * collection of strings).
	 * 
	 * @param entity
	 *            the entity to which to associate the component.
	 * @param component
	 *            the component to store on the entity.
	 * @param componentType
	 *            the type of component being stored.
	 */
	public < T extends Component > void addComponent(Entity entity, T component, Class< T > componentType) {
		
		if (!componentMap.containsKey(componentType)) {
			componentMap.putIfAbsent(componentType, new ConcurrentHashMap< Entity, Component >());
		}
		
		componentMap.get(componentType).put(entity, component);
	}
	
	/**
	 * Gets the component of the specified type for the specified entity from this
	 * manager.
	 * 
	 * @param entity
	 *            the entity for which to get a component.
	 * @param componentType
	 *            the type of component to get.
	 * @return the associated component, or null if the entity has no component of
	 *         the requested type.
	 */
	public < T extends Component > T getComponent(Entity entity, Class< T > componentType) {
		
		Map< Entity, Component > entityComponentMap = componentMap.get(componentType);
		if (entityComponentMap == null) {
			return null;
		}
		return componentType.cast(entityComponentMap.get(entity));
	}
	
	/**
	 * Checks whether the specified entity has a component of the specified type.
	 * 
	 * @param entity
	 *            the entity to look for a component for.
	 * @param componentType
	 *            the type of component to check for.
	 * @return <code>true</code> if the specified entity has a component of the
	 *         specified type, or <code>false</code> if it doesn't.
	 */
	public boolean hasComponent(Entity entity, Class< ? extends Component > componentType) {
		
		Map< Entity, Component > entityComponentMap = componentMap.get(componentType);
		if (entityComponentMap == null) {
			return false;
		}
		return entityComponentMap.containsKey(entity);
	}
	
	/**
	 * Deletes the component of the specified type from the specified entity.
	 * 
	 * @param entity
	 *            the entity to delete a component from.
	 * @param componentType
	 *            the type of component to delete.
	 */
	public void deleteComponent(Entity entity, Class< ? extends Component > componentType) {
		
		Map< Entity, Component > entityComponentMap = componentMap.get(componentType);
		if (entityComponentMap != null) {
			entityComponentMap.remove(entity);
		}
	}
}
