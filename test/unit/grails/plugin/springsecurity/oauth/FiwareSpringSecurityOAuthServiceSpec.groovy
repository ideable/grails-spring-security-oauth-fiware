/*
 * Copyright 2012 the original author or authors
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

import spock.lang.Specification
import org.scribe.model.Token

class FiwareSpringSecurityOAuthServiceSpec extends Specification {
  
    FiwareSpringSecurityOAuthService service
    
    def oauthService
    
    def setup() {
        service = new FiwareSpringSecurityOAuthService()
        oauthService = [:]
    }

    def "should throw OAuthLoginException for unexpected response"() {
        given:
            def exception = null
            def oauthAccessToken = new Token('token', 'secret', 'rawResponse=rawResponse')
            def response = [body: responseBody]
            oauthService.getFiwareResource = { accessToken, url ->
                return response
            }
            service.oauthService = oauthService
        and:
            try {
                def token = service.createAuthToken( oauthAccessToken )
            } catch (Throwable throwable) {
                exception = throwable
            }
        expect:
            exception instanceof OAuthLoginException
        where:
            responseBody      |  _
            ''                |  _
            null              |  _
            '{}'              |  _
            '{"test"="test"}' |  _
    }
    
    def "should return the correct OAuth token"() {
        given:
            def responseBody = '''{
              id: 1,
              displayName: "Demo user",
              email: "demo@fiware.org",
              roles: [
                {
                  id: 15,
                  name: "Manager"
                },
                {
                  id: 7,
                  name: "Ticket manager"
                }
              ],
              organizations: [
                {
                   id: 12,
                   name: "Universidad Politecnica de Madrid",
                   roles: [
                     {
                       id: 14,
                       name: "Admin"
                     }
                  ]
                }
              ]
            }'''
            def oauthAccessToken = new Token('token', 'secret', 'rawResponse=rawResponse')
            def response = [body:responseBody]
            oauthService.getFiwareResource = { accessToken, url ->
                return response
            }
            service.oauthService = oauthService
        when:
            def token = service.createAuthToken( oauthAccessToken )
        then:
            token.principal == 'demo@fiware.org'
            token.socialId == '1'
            token.displayName == 'Demo user'
            token.providerName == 'fiware'
    }
}
