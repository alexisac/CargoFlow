package com.example.backendcargoflow.common.security;

public record AuthenticatedUser(
   Long id,
   String email
) {}
