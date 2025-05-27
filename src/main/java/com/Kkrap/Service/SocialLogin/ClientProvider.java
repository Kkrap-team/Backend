package com.Kkrap.Service.SocialLogin;

import java.util.Map;

public interface ClientProvider {

    Map<String, Object> getClient(String accessToken);

}
