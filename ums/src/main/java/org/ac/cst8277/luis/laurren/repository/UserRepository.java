/**
 * 
 */
package org.ac.cst8277.luis.laurren.repository;

import org.ac.cst8277.luis.laurren.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUserId(String userId);
}
