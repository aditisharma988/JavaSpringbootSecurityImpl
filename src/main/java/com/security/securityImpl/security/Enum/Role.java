//package com.security.securityImpl.security.Enum;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//public enum Role {
//
//
//    USER(Set.of(USER_CREATE, USER_DELETE, USER_UPDATE, USER_READ)),
//
//    ADMIN(Set.of(ADMIN_READ, ADMIN_CREATE, ADMIN_DELETE, ADMIN_UPDATE));
//}
//
//@Getter
//private final Set<Permission> permissions;
//
//public List<SimpleGrantedAuthority> getAuthorities(){
//
//    var authorities = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toList());
//
//    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//
//    return authorities;
//
//}
