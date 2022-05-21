package com.zugazagoitia.knag.vault;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import static com.zugazagoitia.knag.vault.CryptoUtils.*;

@Slf4j
@Service("secretService")
public class SecretService {

    private final SecretRepository repository;

    @Autowired
    public SecretService(SecretRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves a {@link Secret} by its id and decrypts it with the given password, if it exists
     * and the user is the owner.
     *
     * @param id the id of the {@link Secret} to retrieve
     * @param password the password to use to decrypt the secret
     * @param userId the id of the user who wants to retrieve the secret
     * @return An {@link Optional} object if the secret exists and the user is the owner, or an
     *     empty one otherwise
     * @throws CryptographyException if the secret could not be decrypted
     * @throws WrongKeyException if the password is wrong
     * @throws UnauthorizedException if the user is not the owner
     */
    public Optional<String> getDecryptedSecret(Long id, String password, String userId)
            throws CryptographyException, WrongKeyException, UnauthorizedException {

        Optional<Secret> secret = repository.findById(id);
        if (secret.isPresent()) {
            if (secret.get().getUserId().equals(userId)) {
                return Optional.of(decryptSecret(secret.get(), password));
            } else {
                throw new UnauthorizedException("User does not match secret owner");
            }
        }

        return Optional.empty();
    }

    /**
     * Decrypts a string with the given password.
     *
     * @param secret the base64 encoded string to decrypt
     * @param password the password to use to decrypt the string
     * @return the decrypted secret string
     * @throws CryptographyException if the string could not be decrypted
     * @throws WrongKeyException if the password is wrong
     */
    private String decryptSecret(Secret secret, String password)
            throws CryptographyException, WrongKeyException {

        try {
            return decrypt(secret.getSecret(), password, secret.getSalt());
        } catch (InvalidAlgorithmParameterException
                | NoSuchPaddingException
                | InvalidKeySpecException
                | NoSuchAlgorithmException e) {
            log.error("Missing algorithm or key spec", e);
            throw new CryptographyException(e);
        } catch (IllegalBlockSizeException | InvalidKeyException e) {
            log.error("Invalid key, padding or data formatting", e);
            throw new CryptographyException(e);
        } catch (BadPaddingException e) {
            throw new WrongKeyException(e);
        }
    }

    /**
     * Deletes a {@link Secret} by its id if it exists and the user is the owner.
     *
     * @param id the id of the secret to delete
     * @param userId the id of the user who wants to delete the secret
     * @return true if the secret was deleted, false otherwise
     */
    public boolean deleteSecret(Long id, String userId) {
        Optional<Secret> secret = repository.findById(id);

        if (secret.isPresent()) {
            if (secret.get().getUserId().equals(userId)) {
                repository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a {@link Secret} with the given data and encrypts it with the given password.
     *
     * @param rawSecret the secret to encrypt
     * @param password the password to use to encrypt the secret
     * @param userId the id of the user who wants to create the secret
     * @return the created {@link Secret}
     * @throws CryptographyException if the secret could not be encrypted
     */
    public Secret createSecret(String rawSecret, String password, String userId)
            throws CryptographyException {

        String salt = generateSalt();
        String encryptedSecret = encryptSecret(rawSecret, password, salt);
        Secret newSecret =
                Secret.builder().userId(userId).salt(salt).secret(encryptedSecret).build();

        return repository.save(newSecret);
    }

    /**
     * Encrypts a string with the given password.
     *
     * @param rawSecret the string to encrypt
     * @param password the password to use to encrypt the string
     * @param salt the salt to use to encrypt the string
     * @return the encrypted string in base64 encoding
     * @throws CryptographyException if the string could not be encrypted
     */
    private String encryptSecret(String rawSecret, String password, String salt)
            throws CryptographyException {

        try {
            return encrypt(rawSecret, password, salt);

        } catch (InvalidAlgorithmParameterException
                | NoSuchPaddingException
                | InvalidKeySpecException
                | NoSuchAlgorithmException e) {
            log.error("Missing algorithm or key spec", e);
            throw new CryptographyException(e);
        } catch (IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            log.error("Invalid key, padding or data formatting", e);
            throw new CryptographyException(e);
        }
    }
}
