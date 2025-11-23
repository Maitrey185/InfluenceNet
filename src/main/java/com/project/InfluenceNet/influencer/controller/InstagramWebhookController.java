package com.project.InfluenceNet.influencer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@RestController
@RequestMapping("/webhook/instagram")
@Slf4j
public class InstagramWebhookController {

    @Value("${instagram.webhook.verify-token:my_secure_verify_token_12345}")
    private String VERIFY_TOKEN;

    @Value("${instagram.app.secret:}")
    private String APP_SECRET;

    /**
     * GET endpoint for webhook verification
     * Instagram will call this to verify your webhook endpoint
     */
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam(value = "hub.mode", required = false) String mode,
            @RequestParam(value = "hub.verify_token", required = false) String token,
            @RequestParam(value = "hub.challenge", required = false) String challenge
    ) {
        log.info("Webhook verification request received - mode: {}, token: {}", mode, token != null ? "***" : "null");
        
        if (mode == null || token == null || challenge == null) {
            log.warn("Missing required parameters for webhook verification");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Missing required parameters");
        }

        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            log.info("Webhook verified successfully");
            return ResponseEntity.ok(challenge);
        }
        
        log.warn("Webhook verification failed - invalid token or mode");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Verification failed");
    }

    /**
     * POST endpoint for receiving webhook updates
     * Instagram will send updates about media, comments, etc.
     */
    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            @RequestBody String payload
    ) {
        log.info("Webhook update received");
        log.debug("Payload: {}", payload);

        // Verify signature if app secret is configured
        if (APP_SECRET != null && !APP_SECRET.isEmpty()) {
            if (signature == null || !verifySignature(payload, signature)) {
                log.warn("Invalid webhook signature");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid signature");
            }
        }

        try {
            // Process webhook payload
            log.info("Processing webhook: {}", payload);
            
            // TODO: Parse payload and process updates
            // - New media posted
            // - Comments on media
            // - Mentions
            // - Story mentions
            
            return ResponseEntity.ok("EVENT_RECEIVED");
            
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing webhook");
        }
    }

    /**
     * Verify the webhook signature using HMAC SHA-256
     */
    private boolean verifySignature(String payload, String signature) {
        try {
            // Remove 'sha256=' prefix if present
            String expectedSignature = signature.startsWith("sha256=") 
                    ? signature.substring(7) 
                    : signature;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    APP_SECRET.getBytes(StandardCharsets.UTF_8), 
                    "HmacSHA256"
            );
            mac.init(secretKeySpec);
            
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String computedSignature = HexFormat.of().formatHex(hash);

            boolean isValid = computedSignature.equals(expectedSignature);
            log.debug("Signature verification: {}", isValid ? "PASSED" : "FAILED");
            
            return isValid;
            
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error verifying signature", e);
            return false;
        }
    }
}
