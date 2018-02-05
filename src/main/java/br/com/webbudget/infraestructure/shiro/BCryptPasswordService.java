/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.infraestructure.shiro;

import javax.enterprise.context.RequestScoped;
import lombok.Getter;
import org.apache.shiro.authc.credential.PasswordService;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 03/02/2018
 */
@RequestScoped
public class BCryptPasswordService implements PasswordService {

    @Getter
    private final int logRounds;

    /**
     * 
     */
    public BCryptPasswordService() {
        this.logRounds = 10;
    }

    /**
     * 
     * @param logRounds 
     */
    public BCryptPasswordService(int logRounds) {
        this.logRounds = logRounds;
    }

    /**
     * 
     * @param plaintextPassword
     * @return 
     */
    @Override
    public String encryptPassword(Object plaintextPassword) {
        if (plaintextPassword instanceof String) {
            return BCrypt.hashpw(
                    (String) plaintextPassword, BCrypt.gensalt(this.logRounds));
        }
        throw new IllegalArgumentException(
                "encryptPassword only support java.lang.String credential");
    }

    /**
     * 
     * @param submittedPlaintext
     * @param encrypted
     * @return 
     */
    @Override
    public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
        if (submittedPlaintext instanceof char[]) {
            return BCrypt.checkpw(String.valueOf(
                    (char[]) submittedPlaintext), encrypted);
        }
        throw new IllegalArgumentException("Only char[] is supported");
    }
}
