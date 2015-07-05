grails-spring-security-oauth-fiware
====================================

Fiware extension for [Grails Spring Security OAuth][spring-security-oauth-plugin] plugin

Installation
------------

Add the following plugin definition to your BuildConfig:
```groovy
// ...
plugins {
  // ...
  compile ':spring-security-oauth:2.0.2'
  compile ':spring-security-oauth-facebook:0.1'
  // ...
}
```

Usage
-----

Add to your Config:
```groovy
oauth {
  // ...
  providers {
    // ...
    fiware {
      api = org.scribe.builder.api.FiwareApi
      key = 'oauth_fiware_key'
      secret = 'oauth_fiware_secret'
      successUri = '/oauth/fiware/success'
      failureUri = '/oauth/fiware/error'
      callback = "${baseURL}/oauth/fiware/callback"
    }
    // ...
  }
}
```

In your view you can use the taglib exposed from this plugin and from OAuth plugin to create links and to know if the user is authenticated with a given provider:
```xml
<oauth:connect provider="fiware" id="fiware-connect-link">Fiware</oauth:connect>

Logged with fiware?
<s2o:ifLoggedInWith provider="fiware">yes</s2o:ifLoggedInWith>
<s2o:ifNotLoggedInWith provider="fiware">no</s2o:ifNotLoggedInWith>
```

Copyright and license
---------------------

Copyright 2015 Ideable Solutions under the [Apache License, Version 2.0](LICENSE).

[ideable]: http://www.ideable.net
[spring-security-oauth-plugin]: https://github.com/enr/grails-spring-security-oauth
