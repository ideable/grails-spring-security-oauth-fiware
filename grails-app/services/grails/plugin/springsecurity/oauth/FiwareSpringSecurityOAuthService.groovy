/*
 * Copyright 2015 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.springsecurity.oauth

import grails.converters.JSON
import org.scribe.model.Token

/**
 * @author <a href='mailto:gonzalo@ideable.net'>Gonzalo Pérez</a>
 */
class FiwareSpringSecurityOAuthService {

    def oauthService

    def createAuthToken(Token accessToken) {
        def response = oauthService.getFiwareResource(accessToken, 'https://account.lab.fiware.org/user')
        def user
        try {
            user = JSON.parse(response.body)
        } catch (Exception e) {
            log.error "Error parsing response from Fiware. Response:\n${response.body}"
            throw new OAuthLoginException("Error parsing response from Fiware", e)
        }
        if (!user?.id) {
            log.error "No user id from Fiware. Response:\n${response.body}"
            throw new OAuthLoginException("No user id from Fiware")
        }

        String profileId = "${user.id}"
        String displayName = "${user.displayName}"
        String email = "${user.email}"

        return new FiwareOAuthToken(accessToken, profileId, displayName, email)
    }

}

