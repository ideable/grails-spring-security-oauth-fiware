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

import org.scribe.model.Token

/**
 * Spring Security authentication token for Fiware users. It's a standard {@link OAuthToken}
 * that returns the Fiware name as the principal.
 *
 * @author <a href='mailto:gonzalo@ideable.net'>Gonzalo Pérez</a>
 */
class FiwareOAuthToken extends OAuthToken {

    public static final String PROVIDER_NAME = 'fiware'

    String profileId
    String displayName
    String email


    FiwareOAuthToken(Token accessToken, String profileId, String displayName, String email) {
        super(accessToken)
        this.profileId = profileId
        this.displayName = displayName
        this.email = email
        this.principal = email
    }

    String getSocialId() {
        return profileId
    }

    String getScreenName() {
        return displayName
    }

    String getProviderName() {
        return PROVIDER_NAME
    }

}
