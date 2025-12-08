package com.wilburomae.pezeshalms.helpers;

import com.wilburomae.pezeshalms.security.data.entities.CredentialEntity;
import com.wilburomae.pezeshalms.security.data.entities.CredentialStatusEntity;
import com.wilburomae.pezeshalms.security.data.repositories.CredentialRepository;
import com.wilburomae.pezeshalms.security.data.repositories.CredentialStatusRepository;
import com.wilburomae.pezeshalms.users.data.entities.*;
import com.wilburomae.pezeshalms.users.data.repositories.IdentificationTypeRepository;
import com.wilburomae.pezeshalms.users.data.repositories.PermissionRepository;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DbHelper {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private PlatformTransactionManager tm;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CredentialStatusRepository credentialStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private IdentificationTypeRepository identificationTypeRepository;

    @SuppressWarnings("unchecked")
    public void cleanDatabase() {
        TransactionTemplate tx = new TransactionTemplate(tm);
        tx.execute(status -> {
            List<String> tables = em.createNativeQuery("SELECT tablename FROM pg_tables WHERE schemaname = 'lms'").getResultList();
            for (Object table : tables) {
                em.createNativeQuery("TRUNCATE TABLE lms.\"" + table + "\" RESTART IDENTITY CASCADE").executeUpdate();
            }
            return null;
        });

        List<PermissionEntity> permissions = initPermissions();
        RoleEntity role = initRoles(permissions);
        List<CredentialStatusEntity> credentialStatuses = initCredentialStatuses();
        List<IdentificationTypeEntity> idTypes = initIdentificationTypes();
        UserEntity user = initUsers(role, credentialStatuses, idTypes);
    }

    private List<PermissionEntity> initPermissions() {
        List<String> entities = List.of("ROLES", "USERS", "LOGIN", "ACCOUNTS", "ACCOUNT_BALANCES", "ACCOUNT_STATUSES", "ACCOUNT_TYPES", "CURRENCIES", "PARTNER_ACCOUNTS", "CREDENTIALS", "LOGIN_ATTEMPTS", "TRANSACTION_TYPES", "TRANSACTION_TYPE_COMPONENTS", "TRANSACTIONS", "TRANSACTION_ENTRIES");
        List<String> actions = List.of("READ", "WRITE", "DELETE");
        List<PermissionEntity> permissions = new ArrayList<>(actions.size() * entities.size());
        for (String entity : entities) {
            for (String action : actions) {
                PermissionEntity permission = new PermissionEntity();
                permission.setName(action + "_" + entity);
                permission.setDescription("can " + action.toLowerCase() + " " + entity + ".");
                permissions.add(permission);
            }
        }
        return permissionRepository.saveAll(permissions);
    }

    private RoleEntity initRoles(List<PermissionEntity> permissions) {
        RoleEntity role = new RoleEntity();
        role.setName("SYSADMIN");
        role.setDescription("system administrator role");
        role.setPermissions(new HashSet<>(permissions));
        return roleRepository.save(role);
    }

    private List<CredentialStatusEntity> initCredentialStatuses() {
        List<String> names = List.of("ACTIVE", "INACTIVE", "FROZEN", "BLOCKED");
        List<CredentialStatusEntity> statuses = new ArrayList<>(names.size());
        for (String name : names) {
            CredentialStatusEntity status = new CredentialStatusEntity();
            status.setName(name);
            status.setDescription("Credential status for " + name + ".");
            statuses.add(status);
        }
        return credentialStatusRepository.saveAll(statuses);
    }

    private List<IdentificationTypeEntity> initIdentificationTypes() {
        List<String> names = List.of("National ID", "Passport");
        List<IdentificationTypeEntity> types = new ArrayList<>(names.size());
        for (String name : names) {
            IdentificationTypeEntity status = new IdentificationTypeEntity();
            status.setName(name);
            status.setDescription("Identification type for " + name + ".");
            types.add(status);
        }
        return identificationTypeRepository.saveAll(types);
    }

    private UserEntity initUsers(RoleEntity role, List<CredentialStatusEntity> statuses, List<IdentificationTypeEntity> idTypes) {
        UserEntity user = new UserEntity();
        ContactEntity contact = new ContactEntity();
        contact.setContact("tester@test.com");
        contact.setUser(user);
        contact.setPrimary(true);
        contact.setContactType("EMAIL");

        IdentificationEntity identification = new IdentificationEntity();
        identification.setIdNumber("123456789");
        identification.setIdType(idTypes.getFirst());
        identification.setUser(user);

        user.setName("TESTER");
        user.setType(EntityType.CUSTOMER);
        user.setContacts(Set.of(contact));
        user.setIds(Set.of(identification));
        user.setRoles(Set.of(role));
        user = userRepository.save(user);

        CredentialEntity credential = new CredentialEntity();
        credential.setUser(user);
        credential.setHashedPassword("$2a$12$8ExHKvfczHsllvqB.oe9meg0JXupXfi0l5a37Z.kkcmNUsA/s5BmK");
        credential.setStatus(statuses.stream().filter(s -> s.getName().equals("ACTIVE")).findFirst().orElseThrow());
        credential = credentialRepository.save(credential);

        user.setCredential(credential);
        return userRepository.save(user);
    }
}
