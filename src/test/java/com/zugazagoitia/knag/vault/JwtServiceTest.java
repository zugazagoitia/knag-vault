package com.zugazagoitia.knag.vault;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Optional;


class JwtServiceTest {

    private final String rawPublicKey = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6S7asUuzq5Q/3U9rbs+P
            kDVIdjgmtgWreG5qWPsC9xXZKiMV1AiV9LXyqQsAYpCqEDM3XbfmZqGb48yLhb/X
            qZaKgSYaC/h2DjM7lgrIQAp9902Rr8fUmLN2ivr5tnLxUUOnMOc2SQtr9dgzTONY
            W5Zu3PwyvAWk5D6ueIUhLtYzpcB+etoNdL3Ir2746KIy/VUsDwAM7dhrqSK8U2xF
            CGlau4ikOTtvzDownAMHMrfE7q1B6WZQDAQlBmxRQsyKln5DIsKv6xauNsHRgBAK
            ctUxZG8M4QJIx3S6Aughd3RZC4Ca5Ae9fd8L8mlNYBCrQhOZ7dS0f4at4arlLcaj
            twIDAQAB
            -----END PUBLIC KEY-----""";

    /* Private key
-----BEGIN PRIVATE KEY-----
MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDpLtqxS7OrlD/d
T2tuz4+QNUh2OCa2Bat4bmpY+wL3FdkqIxXUCJX0tfKpCwBikKoQMzddt+ZmoZvj
zIuFv9eploqBJhoL+HYOMzuWCshACn33TZGvx9SYs3aK+vm2cvFRQ6cw5zZJC2v1
2DNM41hblm7c/DK8BaTkPq54hSEu1jOlwH562g10vcivbvjoojL9VSwPAAzt2Gup
IrxTbEUIaVq7iKQ5O2/MOjCcAwcyt8TurUHpZlAMBCUGbFFCzIqWfkMiwq/rFq42
wdGAEApy1TFkbwzhAkjHdLoC6CF3dFkLgJrkB7193wvyaU1gEKtCE5nt1LR/hq3h
quUtxqO3AgMBAAECggEBANX6C+7EA/TADrbcCT7fMuNnMb5iGovPuiDCWc6bUIZC
Q0yac45l7o1nZWzfzpOkIprJFNZoSgIF7NJmQeYTPCjAHwsSVraDYnn3Y4d1D3tM
5XjJcpX2bs1NactxMTLOWUl0JnkGwtbWp1Qq+DBnMw6ghc09lKTbHQvhxSKNL/0U
C+YmCYT5ODmxzLBwkzN5RhxQZNqol/4LYVdji9bS7N/UITw5E6LGDOo/hZHWqJsE
fgrJTPsuCyrYlwrNkgmV2KpRrGz5MpcRM7XHgnqVym+HyD/r9E7MEFdTLEaiiHcm
Ish1usJDEJMFIWkF+rnEoJkQHbqiKlQBcoqSbCmoMWECgYEA/4379mMPF0JJ/EER
4VH7/ZYxjdyphenx2VYCWY/uzT0KbCWQF8KXckuoFrHAIP3EuFn6JNoIbja0NbhI
HGrU29BZkATG8h/xjFy/zPBauxTQmM+yS2T37XtMoXNZNS/ubz2lJXMOapQQiXVR
l/tzzpyWaCe9j0NT7DAU0ZFmDbECgYEA6ZbjkcOs2jwHsOwwfamFm4VpUFxYtED7
9vKzq5d7+Ii1kPKHj5fDnYkZd+mNwNZ02O6OGxh40EDML+i6nOABPg/FmXeVCya9
Vump2Yqr2fAK3xm6QY5KxAjWWq2kVqmdRmICSL2Z9rBzpXmD5o06y9viOwd2bhBo
0wB02416GecCgYEA+S/ZoEa3UFazDeXlKXBn5r2tVEb2hj24NdRINkzC7h23K/z0
pDZ6tlhPbtGkJodMavZRk92GmvF8h2VJ62vAYxamPmhqFW5Qei12WL+FuSZywI7F
q/6oQkkYT9XKBrLWLGJPxlSKmiIGfgKHrUrjgXPutWEK1ccw7f10T2UXvgECgYEA
nXqLa58G7o4gBUgGnQFnwOSdjn7jkoppFCClvp4/BtxrxA+uEsGXMKLYV75OQd6T
IhkaFuxVrtiwj/APt2lRjRym9ALpqX3xkiGvz6ismR46xhQbPM0IXMc0dCeyrnZl
QKkcrxucK/Lj1IBqy0kVhZB1IaSzVBqeAPrCza3AzqsCgYEAvSiEjDvGLIlqoSvK
MHEVe8PBGOZYLcAdq4YiOIBgddoYyRsq5bzHtTQFgYQVK99Cnxo+PQAvzGb+dpjN
/LIEAS2LuuWHGtOrZlwef8ZpCQgrtmp/phXfVi6llcZx4mMm7zYmGhh2AsA9yEQc
acgc4kgDThAjD7VlXad9UHpNMO8=
-----END PRIVATE KEY-----
     */
    JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(rawPublicKey);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserValidJwt(){
        /*
            Subject: "1234567890"
            Issuer:  "knag-users"
            Issued At: 1516239022
            Expires: 5516239022
        */
        String validJWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzUxMiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjo1NTE2MjM5MDIyLCJpc3MiOiJrbmFnLXVzZXJzIn0.iBXSph2ak0OYTvYGcY_GKMVto1H6JXcrSTpm16ijjjJzNvqGBOV0mX8PINzPFKJNxgoBGNC3acQ5BkDjgqWK0ZN48Qt2WTI262KOklzDOxXIowuDk_DfXAYrsDhfbHVPAoHmMTp8FLSbWb9AY2g3VWdXyEBnrgOTTXY3MWOxIvQAXV62tBDYz46ZbKtB6eJYHupzakBeCHvIZGoQtznccJ-XPoizvY-EHTF3SRMOFNNi5D-VPfVyRS23beFFkGuOQNHrLP3s-92JcdvAt94jlPLREoSytaL0NZfpnGVu9EHOKdv2YiS8BPk8FcHX_XdPTQKkwZoOEGxtahVFWwfdow";
        Optional<String> result = jwtService.getUser(validJWT);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("1234567890", result.get());
    }

    @Test
    void testGetUserInvalidJwt(){
        Optional<String> result = jwtService.getUser("invalidJWT");
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testGetUserEmptyJwt(){
        Optional<String> result = jwtService.getUser("");
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testGetUserNullJwt(){
        Optional<String> result = jwtService.getUser(null);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testGetUserWrongIssuer(){
        String wrongIssuerJWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzUxMiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjo1NTE2MjM5MDIyLCJpc3MiOiJrbmFnLXVzZXJzLW5vIn0.E1RSPjRX9PJxAvuct_3H4km0i9FLTR56AoDUunVeQeDcbWXHW19aFi772Jz8i1pr1D7CPOB-IG7Mwoy6v5kXf6oV-Vo2P8mvtywJ2YhPLNacN06ARyGivqoVBshiboSIbnVZdfB4-rY7-YrwKBIirh60wH3DKRh4XJE6Z4X-or3lsUZCDxu9C3-GGBvbyMiz0lBkn8Pd4kHtbJ8HiqYjqsc-7PquIG1NYGJXcK4Wyn6fcDcctpYPPh6xK9x2d1d0LNfmUaw7Vouryek1fs9TGVSqRRhujhX8_IhRDq5b2hOtYvUMH6xwMBTgJt64rkAk5N7lh_qtseTZ5b3v2gV2XQ";
        Optional<String> result = jwtService.getUser(wrongIssuerJWT);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testGetUserExpiredJWT(){
        String expiredJWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzUxMiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNjE2MjM5MDIyLCJpc3MiOiJrbmFnLXVzZXJzIn0.G6sIITdCOE6L6I_vTxFVIr5oopSqLPFMJPwje46XtnQFV6AhseYVQlc2o1KST-N3hXQlgVB0dxSH9fSFwA3V8D-S9w6HSEssTo1svOZqaygF4kOkSMVCxJOYICzMadC5gaS9njMDad_2KAh8dBqyrl-5ADOdUe7KBEPWhmscJH-4KlhWEZ5ZZbVU0xfjxw0PAtK7L4wExW7po_EukVqn4JP-dc93dR9DE_H95Kk5G8IzMqfvEmitwiSGlhfGkcKAmyyvJry0HYEHXeBBuXz89ybXsFGyOWw5ffVYg8iIZOtn_htmhvutf3a_Xj0uGr33F-BGopjT98Nx-9ks-M0Lig";
        Optional<String> result = jwtService.getUser(expiredJWT);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testGetUserNotYetValidJWT(){
        String notYetValidJWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzUxMiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MjUxNjIzOTAyMiwiZXhwIjo1NTE2MjM5MDIyLCJpc3MiOiJrbmFnLXVzZXJzIn0.wfOzlJ0nP0o7lno-UxjFbEnHArRxjb_vIRAK6RCl07ntQdBe1CqD5-LCtkBiMojdd_Z-HNTYCFU2xAOBr9dB6qpwUZxdvzWX38cRo10JKzX-AG_j16EfuFcB-_D_fBeB5eAy2rBYz80RCH79-1eON3QZa53G5I-ik-M9KeryeEmI_VOUrlR5iHAOfbqkWB5cSt9dViUaseXtY9dSufC4Sjb0Cmmt12jrx2jdR8xwSz_HeAWJR0BFmzBytrw9hOdoblSixO19-aXy8GXv75lj10RSJ02MWMnGuiWyWJJ92SImLtB-aiVvsBPqCXFya0hxf0jAPyN0OoNX4Qve-JNbGQ";
        Optional<String> result = jwtService.getUser(notYetValidJWT);
        Assertions.assertFalse(result.isPresent());
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme