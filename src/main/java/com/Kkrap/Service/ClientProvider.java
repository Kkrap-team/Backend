package com.Kkrap.Service;

import java.util.Map;

public interface ClientProvider {

    Map<String, Object> getClient(String accessToken);

}
