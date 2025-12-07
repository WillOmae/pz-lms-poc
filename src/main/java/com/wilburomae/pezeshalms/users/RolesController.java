package com.wilburomae.pezeshalms.users;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.users.dtos.Role;
import com.wilburomae.pezeshalms.users.dtos.RoleRequest;
import com.wilburomae.pezeshalms.users.services.RolesDeletionService;
import com.wilburomae.pezeshalms.users.services.RolesFetchService;
import com.wilburomae.pezeshalms.users.services.RolesUpsertService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wilburomae.pezeshalms.users.Permissions.*;

@RestController
@RequestMapping("/roles")
public class RolesController {

    private final RolesUpsertService rolesUpsertService;
    private final RolesFetchService rolesFetchService;
    private final RolesDeletionService rolesDeletionService;

    public RolesController(RolesUpsertService rolesUpsertService, RolesFetchService rolesFetchService, RolesDeletionService rolesDeletionService) {
        this.rolesUpsertService = rolesUpsertService;
        this.rolesFetchService = rolesFetchService;
        this.rolesDeletionService = rolesDeletionService;
    }

    @RolesAllowed({WRITE_ROLES})
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response<Long>> create(@Valid @RequestBody RoleRequest request) {
        return rolesUpsertService.upsert(null, request).toEntity();
    }

    @RolesAllowed({READ_ROLES})
    @GetMapping(produces = "application/json")
    public ResponseEntity<Response<List<Role>>> fetch() {
        return rolesFetchService.fetchAll().toEntity();
    }

    @RolesAllowed({READ_ROLES})
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Response<Role>> fetchById(@PathVariable long id) {
        return rolesFetchService.fetchById(id).toEntity();
    }

    @RolesAllowed({WRITE_ROLES})
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Response<Long>> update(@PathVariable long id, RoleRequest request) {
        return rolesUpsertService.upsert(id, request).toEntity();
    }

    @RolesAllowed({DELETE_ROLES})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Response<Void>> delete(@PathVariable long id) {
        return rolesDeletionService.delete(id).toEntity();
    }
}
