/**
 * 
 */
package org.ac.cst8277.luis.laurren.repository;

import org.ac.cst8277.luis.laurren.model.GitUsers;
import org.ac.cst8277.luis.laurren.model.User;
import org.springframework.data.repository.CrudRepository;

public interface GitUserRepository extends CrudRepository<GitUsers, Long> {

	GitUsers findByUserNameAndPassword(String userName, String password);

	GitUsers findByUserName(String userName);

	GitUsers findByAccessToken(String accessToken);
}
