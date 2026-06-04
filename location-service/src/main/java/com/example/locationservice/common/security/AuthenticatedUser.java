package com.example.locationservice.common.security;

public record AuthenticatedUser(
   Long id,
   String email
) {}
