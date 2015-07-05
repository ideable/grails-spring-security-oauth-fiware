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
import grails.util.Environment

/**
 * @author <a href='mailto:gonzalo@ideable.net'>Gonzalo Pérez</a>
 * @author <a href='mailto:unai@ideable.net'>Unai Martínez</a>
 * @author <a href='mailto:arkaitz@ideable.net'>Arkaitz Carbajo</a>
 */
class SpringSecurityOauthFiwareGrailsPlugin {

    def version = '0.2'
    def grailsVersion = '2.0 > *'
    def loadAfter = ['springSecurityOauth']

    def title = 'Fiware for Spring Security OAuth plugin'
    def author = 'Gonzalo Pérez'
    def authorEmail = 'gonzalo@ideable.net'
    def description = '''\
Integrate [Fiware|http://www.fiware.com] to [Spring Security OAuth plugin|http://grails.org/plugin/spring-security-oauth].
'''

    def documentation = 'http://grails.org/plugin/spring-security-oauth-fiware'

    def license = 'APACHE'

    def developers = [
            [name: 'Gonzalo Pérez', email: 'gonzalo@ideable.net'],
            [name: 'Unai Martinez', email: 'unai@ideable.net'],
            [name: 'Arkaitz Carbajo', email: 'arkaitz@ideable.net']
    ]
    def organization = [name: 'Ideable', url: 'http://www.ideable.net']

    def issueManagement = [system: 'GITHUB',
                           url   : 'https://github.com/donbeave/grails-spring-security-oauth-fiware/issues']
    def scm = [url: 'https://github.com/donbeave/grails-spring-security-oauth-fiware']

    def doWithSpring = {
        loadConfig(application.config)
    }

    private void loadConfig(ConfigObject config) {
        def classLoader = new GroovyClassLoader(getClass().classLoader)

        // Note here the order of objects when calling merge - merge OVERWRITES values in the target object
        // Load default config as a basis
        def newConfig = new ConfigSlurper(Environment.current.name).parse(
                classLoader.loadClass('DefaultFiwareOauthConfig')
        )

        newConfig.oauth.providers.fiware.merge(config.oauth.providers.fiware)

        // Now merge DefaultFiwareOauthConfig into the main config
        config.merge(newConfig)
    }

}
