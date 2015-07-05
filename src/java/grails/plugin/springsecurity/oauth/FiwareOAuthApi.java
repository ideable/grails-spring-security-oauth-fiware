package grails.plugin.springsecurity.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.springframework.security.crypto.codec.Base64;;

/**
 * Contains all the configuration needed to instantiate a valid {@link OAuthService}
 */
public class FiwareOAuthApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "https://account.lab.fiware.org/oauth2/authorize?" +
            "response_type=code" +
            "&client_id=%s" +
            "&state=xyz" +
            "&redirect_uri=%s";

    /**
     * {@inheritDoc}
     */
    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccessTokenEndpoint() {
        return "https://account.lab.fiware.org/oauth2/token";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    /**
     * {@inheritDoc}
     */
    public OAuthService createService(OAuthConfig config) {
        return new Service(this, config);
    }

    public class Service extends OAuth20ServiceImpl {

        private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
        private static final String GRANT_TYPE = "grant_type";

        private final DefaultApi20 api;
        private final OAuthConfig config;

        public Service(DefaultApi20 api, OAuthConfig config) {
            super(api, config);
            this.api = api;
            this.config = config;
        }

        @Override
        public Token getAccessToken(Token requestToken, Verifier verifier) {
            Verb verb = api.getAccessTokenVerb();
            OAuthRequest request = new OAuthRequest(verb, api.getAccessTokenEndpoint());
            switch (verb) {
                case POST:
                    request.addBodyParameter(GRANT_TYPE, GRANT_TYPE_AUTHORIZATION_CODE);
                    request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
                    request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());

                    // https://github.com/ging/fi-ware-idm/wiki/Using-the-FIWARE-LAB-instance
                    String authBasicClear = config.getApiKey() + ":" + config.getApiSecret();
                    String authBasicEncoded = new String(Base64.encode(authBasicClear.getBytes()));

                    request.addHeader("Authorization", "Basic " + authBasicEncoded);
                    break;
                case GET:
                default:
                    throw new UnsupportedOperationException("Unsupported operation. Only POST verb is supported");
            }
            Response response = request.send();
            return api.getAccessTokenExtractor().extract(response.getBody());
        }
    }
}
