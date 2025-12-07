package com.wilburomae.pezeshalms.accounts.dtos;

import com.wilburomae.pezeshalms.accounts.data.entities.PartnerAccountEntity;
import com.wilburomae.pezeshalms.users.dtos.User;

public record PartnerAccount(long id, User user, Account account) {

    public static PartnerAccount from(PartnerAccountEntity entity) {
        User user = User.from(entity.getUser());
        Account account = Account.from(entity.getAccount());
        return new PartnerAccount(entity.getId(), user, account);
    }
}
