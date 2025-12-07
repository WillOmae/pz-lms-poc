package com.wilburomae.pezeshalms.users;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.users.dtos.User;
import com.wilburomae.pezeshalms.users.dtos.UserRequest;
import com.wilburomae.pezeshalms.users.services.UsersDeletionService;
import com.wilburomae.pezeshalms.users.services.UsersFetchService;
import com.wilburomae.pezeshalms.users.services.UsersUpsertService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.wilburomae.pezeshalms.users.Permissions.*;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    private final UsersUpsertService usersUpsertService;
    private final UsersFetchService usersFetchService;
    private final UsersDeletionService usersDeletionService;

    public UsersController(UsersUpsertService usersUpsertService, UsersFetchService usersFetchService, UsersDeletionService usersDeletionService) {
        this.usersUpsertService = usersUpsertService;
        this.usersFetchService = usersFetchService;
        this.usersDeletionService = usersDeletionService;
    }

    @RolesAllowed({WRITE_USERS})
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response<Long>> create(@Valid @RequestBody UserRequest request) {
        return usersUpsertService.upsert(null, request).toEntity();
    }

    @RolesAllowed({READ_USERS})
    @GetMapping(produces = "application/json")
    public ResponseEntity<Response<List<User>>> fetch(@RequestParam(required = false) String contact) {
        return usersFetchService.fetchAll(contact).toEntity();
    }

    @RolesAllowed({READ_USERS})
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Response<User>> fetchById(@PathVariable long id) {
        return usersFetchService.fetchById(id).toEntity();
    }

    @RolesAllowed({WRITE_USERS})
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response<Long>> update(@PathVariable long id, @RequestBody UserRequest request) {
        return usersUpsertService.upsert(id, request).toEntity();
    }

    @RolesAllowed({DELETE_USERS})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Response<Void>> delete(@PathVariable long id) {
        return usersDeletionService.delete(id).toEntity();
    }
}
