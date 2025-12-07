package com.wilburomae.pezeshalms.users.dtos;

import com.wilburomae.pezeshalms.users.data.entities.CredentialEntity;
import com.wilburomae.pezeshalms.users.data.entities.PermissionEntity;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;

import java.util.List;

public record User(long id,
                   String name,
                   String type,
                   List<Contact> contacts,
                   List<Identification> identifications,
                   List<Role> roles,
                   Credential credential) {

    public static User from(UserEntity entity) {
        List<Contact> contacts = entity.getContacts()
                .stream()
                .map(e -> new Contact(e.getContact(), e.getContactType(), e.isPrimary()))
                .toList();

        List<Identification> identifications = entity.getIds()
                .stream()
                .map(e -> new Identification(e.getIdNumber(), e.getIdType().getName()))
                .toList();

        List<Role> roles = entity.getRoles()
                .stream()
                .map(e -> new Role(e.getName(), e.getPermissions().stream().map(PermissionEntity::getName).toList()))
                .toList();

        CredentialEntity credentialEntity = entity.getCredential();
        Credential credential = new Credential(credentialEntity.getHashedPassword(), credentialEntity.getStatus().getName());

        return new User(entity.getId(), entity.getName(), entity.getType().toString(), contacts, identifications, roles, credential);
    }
}
