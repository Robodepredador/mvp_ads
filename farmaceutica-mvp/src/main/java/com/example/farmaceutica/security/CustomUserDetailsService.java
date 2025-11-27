package com.example.farmaceutica.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByUsername(username)
                .or(() -> usuarioRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + u.getRol());
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                u.isActivo(),
                true,
                true,
                true,
                Collections.singletonList(authority)
        );
    }
}
