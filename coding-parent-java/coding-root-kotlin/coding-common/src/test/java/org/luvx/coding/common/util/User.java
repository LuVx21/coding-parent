package org.luvx.coding.common.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private Long    userId;
    private String  userName;
    private String  passWord;
    @JsonProperty(access = Access.WRITE_ONLY)
    private Integer age;
    private int     valid;

    public String getTest() {
        return userName + ":" + passWord;
    }
}