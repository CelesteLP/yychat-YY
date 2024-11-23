package com.yychat.model;
import lombok.Data;

import java.io.Serializable;
@Data
public class User implements Serializable {
    static final long serialVersionUID = 1L;
    String userName;
    String password;

    String userType;

}
