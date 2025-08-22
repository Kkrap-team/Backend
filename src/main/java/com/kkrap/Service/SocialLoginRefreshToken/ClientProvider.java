package com.kkrap.Service.SocialLoginRefreshToken;

import java.util.Map;

public interface ClientProvider {

    Map<String, Object> getClient(String accessToken);

}
