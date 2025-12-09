package com.wilburomae.pezeshalms.helpers;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountTypeEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountTypeRepository;
import com.wilburomae.pezeshalms.security.data.entities.CredentialEntity;
import com.wilburomae.pezeshalms.security.data.entities.CredentialStatusEntity;
import com.wilburomae.pezeshalms.security.data.repositories.CredentialStatusRepository;
import com.wilburomae.pezeshalms.users.data.entities.IdentificationTypeEntity;
import com.wilburomae.pezeshalms.users.data.entities.PermissionEntity;
import com.wilburomae.pezeshalms.users.data.entities.RoleEntity;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import com.wilburomae.pezeshalms.users.data.repositories.IdentificationTypeRepository;
import com.wilburomae.pezeshalms.users.data.repositories.PermissionRepository;
import com.wilburomae.pezeshalms.users.data.repositories.RoleRepository;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import com.wilburomae.pezeshalms.users.dtos.Contact;
import com.wilburomae.pezeshalms.users.dtos.Identification;
import com.wilburomae.pezeshalms.users.dtos.RoleRequest;
import com.wilburomae.pezeshalms.users.dtos.UserRequest;
import com.wilburomae.pezeshalms.users.services.RolesUpsertService;
import com.wilburomae.pezeshalms.users.services.UsersUpsertService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.wilburomae.pezeshalms.integrationtests.AccountTypesIntegrationTests.ROOT_ACCOUNT_TYPES;

@Component
public class DbHelper {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    PlatformTransactionManager tm;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CredentialStatusRepository credentialStatusRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IdentificationTypeRepository identificationTypeRepository;
    @Autowired
    AccountTypeRepository accountTypeRepository;
    @Autowired
    UsersUpsertService usersUpsertService;
    @Autowired
    RolesUpsertService rolesUpsertService;

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
        List<AccountTypeEntity> accountTypes = initAccountTypes();
    }

    private List<AccountTypeEntity> initAccountTypes() {
        List<AccountTypeEntity> types = new ArrayList<>(ROOT_ACCOUNT_TYPES.size());
        for (String name : ROOT_ACCOUNT_TYPES) {
            AccountTypeEntity type = new AccountTypeEntity();
            type.setName(name);
            type.setDescription("Account type for " + name + ".");
            types.add(type);
        }
        return accountTypeRepository.saveAll(types);
    }

    private List<PermissionEntity> initPermissions() {
        List<String> entities = List.of("ROLES", "USERS", "ACCOUNTS", "ACCOUNT_BALANCES", "ACCOUNT_STATUSES", "ACCOUNT_TYPES", "CURRENCIES", "PARTNER_ACCOUNTS", "CREDENTIALS", "LOGIN_ATTEMPTS", "TRANSACTION_TYPES", "TRANSACTION_TYPE_COMPONENTS", "TRANSACTIONS", "TRANSACTION_ENTRIES");
        List<String> actions = List.of("READ", "WRITE", "DELETE");
        List<PermissionEntity> permissions = new ArrayList<>((actions.size() * entities.size()) + 1);
        for (String entity : entities) {
            for (String action : actions) {
                PermissionEntity permission = new PermissionEntity();
                permission.setName(action + "_" + entity);
                permission.setDescription("can " + action.toLowerCase() + " " + entity + ".");
                permissions.add(permission);
            }
        }

        PermissionEntity permission = new PermissionEntity();
        permission.setName("LOGIN");
        permission.setDescription("can login.");
        permissions.add(permission);

        return permissionRepository.saveAll(permissions);
    }

    private RoleEntity initRoles(List<PermissionEntity> permissions) {
        List<Long> ids = permissions.stream().map(PermissionEntity::getId).toList();
        RoleRequest roleRequest = new RoleRequest("SYSADMIN", "System administrator role", ids);
        Long id = rolesUpsertService.upsert(null, roleRequest).data();
        return roleRepository.findById(id).orElseThrow();
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
        Contact contact = new Contact("tester@test.com", "EMAIL", true);
        IdentificationTypeEntity idType = idTypes.getFirst();
        Identification identification = new Identification("", idType.getId(), idType.getName());
        UserRequest userRequest = new UserRequest("TESTER", "CUSTOMER", List.of(contact), List.of(identification), List.of(role.getId()));
        Long id = usersUpsertService.upsert(null, userRequest).data();

        UserEntity user = userRepository.findById(id).orElseThrow();
        CredentialEntity credential = new CredentialEntity();
        credential.setUser(user);
        // bcrypted 123456
        credential.setHashedPassword("$2a$12$8ExHKvfczHsllvqB.oe9meg0JXupXfi0l5a37Z.kkcmNUsA/s5BmK");
        credential.setStatus(statuses.stream().filter(s -> s.getName().equals("ACTIVE")).findFirst().orElseThrow());
        user.addCredential(credential);

        return userRepository.save(user);
    }
}
