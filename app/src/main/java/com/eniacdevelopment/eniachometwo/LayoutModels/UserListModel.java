package com.eniacdevelopment.eniachometwo.LayoutModels;

import com.eniacdevelopment.EniacHome.DataModel.User.User;
import com.eniacdevelopment.EniacHome.DataModel.User.UserRole;

import java.util.List;

/**
 * Created by denni on 1/16/2017.
 */

public class UserListModel {

    private User user;

    public UserListModel(User user) {
        this.user = user;
    }

    public String getUserName() {
        return user.Username;
    }

    public String getUserRole() {
        return user.Role.name();
    }

    public User getUser() {
        return user;
    }
}
