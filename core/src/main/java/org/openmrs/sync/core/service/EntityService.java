package org.openmrs.sync.core.service;

import org.openmrs.sync.core.model.BaseModel;

import java.time.LocalDateTime;
import java.util.List;

public interface EntityService<M extends BaseModel> {

    /**
     * Saves an entity
     * @param entity the entity
     * @return BaseModel
     */
    M save(M entity);

    /**
     * get all models for the entity
     * @return a list of BaseModel
     */
    List<M> getAllModels();

    /**
     * getAll models for the entity
     * @return a list of BaseModel
     * @param lastSyncDate
     */
    List<M> getModels(LocalDateTime lastSyncDate);

    /**
     * get model with the given uuid
     * @return a BaseModel
     * @param uuid
     */
    M getModel(final String uuid);

    /**
     * get model with the given uuid
     * @return a BaseModel
     * @param id
     */
    M getModel(final Long id);
}
