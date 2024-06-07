/**
 * 
 */
package org.ac.cst8277.luis.laurren.repository;

import java.util.List;

import org.ac.cst8277.luis.laurren.model.UserRoles;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRoles, Long> {

	List<UserRoles> findByUserId(Long userId);
}
