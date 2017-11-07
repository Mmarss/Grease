package net.mmarss.grease.ecs;

import java.util.UUID;

/**
 * Represents the abstract concept of an object in a game.
 */
public final class Entity {
	
	/** The internal identifier for this entity. */
	private final long	id;
	/** The universal identifier for this entity. */
	private final UUID	uuid;
	
	/**
	 * Constructs a new entity.
	 * 
	 * @param id
	 *            the internal identifier for this entity.
	 * @param uuid
	 *            the universal identifier for this entity.
	 */
	/* package */ Entity(long id, UUID uuid) {
		
		this.id = id;
		this.uuid = uuid;
	}
	
	/**
	 * @return the internal identifier for this entity.
	 */
	/* package */ long getId() {
		
		return id;
	}
	
	/**
	 * @return the universal identifier for this entity.
	 */
	/* package */ UUID getUuid() {
		
		return uuid;
	}
	
	/**
	 * Converts this entity to a string. The current implementation uses only the
	 * internal identifier for this string, which is not guaranteed to be unique
	 * across multiple entity managers.
	 * 
	 * @return a string representing this entity.
	 */
	@Override
	public String toString() {
		
		return "Entity#" + Long.toHexString(id);
	}
	
	@Override
	public boolean equals(Object other) {
		
		if (other == null) {
			return false;
		}
		
		if (!(other instanceof Entity)) {
			return false;
		}
		
		return uuid.equals(((Entity) other).uuid);
	}
	
	@Override
	public int hashCode() {
		
		return Long.hashCode(id);
	}
}
