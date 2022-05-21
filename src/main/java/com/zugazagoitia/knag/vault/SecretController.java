package com.zugazagoitia.knag.vault;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@RestController
public class SecretController {

    private final JwtService jwtService;

    private final SecretService secretService;

    @Autowired
    public SecretController(JwtService jwtService, SecretService secretService) {
        this.jwtService = jwtService;
        this.secretService = secretService;
    }

    @GetMapping("/secret/{id}")
    public SecretResponse getSecret(
            @PathVariable("id") Long id,
            @NotEmpty @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody SecretRequest secretRequest) {

        Optional<String> userId = parseAuthHeader(authorizationHeader);

        Optional<String> secret;

        // If the user is authorized, get the secret
        if (userId.isPresent()) {
            try {

                secret =
                        secretService.getDecryptedSecret(
                                id, secretRequest.getPassword(), userId.get());

                if (secret.isPresent()) {
                    return new SecretResponse(secret.get());
                } else throw new UnauthorizedException();

            } catch (CryptographyException e) {
                throw new RuntimeException(e);
            } catch (WrongKeyException e) {
                throw new UnauthorizedException();
            }
        } else throw new UnauthorizedException();
    }

    /**
     * Parses the authorization header and returns the user id.
     *
     * @param authorizationHeader The authorization header.
     * @return An instance of {@link Optional} containing the user id or {@link Optional#empty()} if
     *     the header is not valid.
     * @throws UnauthorizedException If the jwt is not valid.
     */
    private Optional<String> parseAuthHeader(String authorizationHeader)
            throws UnauthorizedException {
        String jwt = authorizationHeader.substring(7);

        Optional<String> userId;
        try {
            userId = jwtService.getUser(jwt);
        } catch (RuntimeException e) {
            throw new UnauthorizedException("Invalid JWT");
        }
        return userId;
    }

    @DeleteMapping("/secret/{id}")
    public ResponseEntity deleteSecret(
            @PathVariable("id") Long id,
            @NotEmpty @RequestHeader("Authorization") String authorizationHeader) {

        Optional<String> userId = parseAuthHeader(authorizationHeader);

        if (userId.isPresent()) {
            if (secretService.deleteSecret(id, userId.get()))
                return ResponseEntity.noContent().build();
            else return ResponseEntity.notFound().build();
        } else throw new UnauthorizedException();
    }

    @PostMapping("/secret")
    public CreatedSecretResponse createSecret(
            @NotEmpty @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CreateSecretRequest secretRequest) {

        Optional<String> userId = parseAuthHeader(authorizationHeader);
        if (userId.isPresent()) {
            try {
                Secret secret =
                        secretService.createSecret(
                                secretRequest.getSecret(),
                                secretRequest.getPassword(),
                                userId.get());
                return new CreatedSecretResponse(secret.getId());
            } catch (CryptographyException e) {
                throw new RuntimeException(e);
            }
        } else throw new UnauthorizedException();
    }

    @Value
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    static class SecretRequest {
        @Length(min = 8, max = 255)
        @NotEmpty
        String password;
    }

    @Value
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    static class SecretResponse {
        String secret;
    }

    @Value
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    static class CreatedSecretResponse {
        Long id;
    }

    @Value
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    static class CreateSecretRequest {
        @NotEmpty
        @Length(min = 8, max = 4096)
        String secret;

        @NotEmpty
        @Length(min = 8, max = 255)
        String password;
    }
}
