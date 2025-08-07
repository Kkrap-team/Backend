package com.Kkrap.Service.SocialLoginRefreshToken;

import java.util.Map;

public interface ClientProvider {

    Map<String, Object> getClient(String accessToken);

}
