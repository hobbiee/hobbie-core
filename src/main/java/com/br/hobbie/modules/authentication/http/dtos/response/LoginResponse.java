package com.br.hobbie.modules.authentication.http.dtos.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {

}
