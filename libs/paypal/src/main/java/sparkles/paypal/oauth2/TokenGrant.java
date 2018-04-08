package sparkles.paypal.oauth2;

import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

/**
 * A response that grants a token.
 *
 * ```json
 * {
 *   "scope": "https://uri.paypal.com/services/subscriptions https://api.paypal.com/v1/payments/.* https://api.paypal.com/v1/vault/credit-card https://uri.paypal.com/services/applications/webhooks openid https://uri.paypal.com/payments/payouts https://api.paypal.com/v1/vault/credit-card/.*",
 *   "nonce": "2017-06-08T18:30:28ZCl54Q_OlDqP6-4D03sDT8wRiHjKrYlb5EH7Di0gRrds",
 *   "access_token": "Access-Token",
 *   "token_type": "Bearer",
 *   "app_id": "APP-80W284485P519543T",
 *   "expires_in": 32398
 * }
 * ```
 */
@Accessors(fluent  = true)
@Data
public class TokenGrant {

  @Json(name = "scope")
  private String scope;

  @Json(name = "nonce")
  private String nonce;

  @Json(name = "access_token")
  private String accessToken;

  @Json(name = "token_type")
  private String tokenType;

  @Json(name = "app_id")
  private String appId;

  @Json(name = "expires_in")
  private long expiresIn;

}
